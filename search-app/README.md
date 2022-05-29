# search-app
## Getting start

1. Create Atlas Cluster
   - Create database & collection (search.data)
   - Create Search Index
2. Create Realm application
   - Create Realm function
   - Create HTTPS endpoints
3. Update index.html

## 1. Create Atlas Cluster
Create your Atlas cluster and grant accessto an account
Cluster url: atlasSearch.uskpz.mongodb.net
Database user: search
Database password: search

### 1.1. Create database & collection
Load your data into the collection (search.data) with following schema
```
{
  "title": "Search Data",
  "description": "An entry for search",
  "type": "object",
  "properties": {
    "_id": {
      "description": "The unique identifier",
      "type": "ObjectId"
    },
    "location": {
      "description": "The location of the item for GEO search",
      "type": "object"
    },
    "content": {
      "description": "The content for search",
      "type": ["object","string"]
    },
  "required": [ "_id", "content" ]
}

```
import the data to your cluster
```
mongoimport --uri "mongodb+srv://<Database user>:<Database password>@<Cluster url>" --db search -c data --file=data.json
mongoimport --uri "mongodb+srv://search:search@atlasSearch.uskpz.mongodb.net" --db search -c data --file=public_estate.json
```
### 1.2. Create Search Index
Create Search Index for the collection
Geo Index on location field & multi language index on content field
```
{
  "mappings": {
    "dynamic": false,
    "fields": {
      "content": [
        {
          "dynamic": true,
          "type": "document"
        },
        {
          "multi": {
            "chinese": {
              "analyzer": "lucene.chinese",
              "searchAnalyzer": "lucene.chinese",
              "type": "string"
            },
            "english": {
              "analyzer": "lucene.english",
              "searchAnalyzer": "lucene.english",
              "type": "string"
            },
            "japanese": {
              "analyzer": "lucene.japanese",
              "searchAnalyzer": "lucene.japanese",
              "type": "string"
            }
          },
          "type": "string"
        }
      ],
      "location": {
        "type": "geo"
      }
    }
  }
}
```
## 2. Create Realm Application
Create the Realm application and enable one of the authentication provider, 
For API Keys copy the API Key to the index.html

### 2.1. Realm function
Create the Realm function that will run an aggregation query based on the request parameters
```
// This function is the endpoint's request handler.
//exports = function({ query, headers, body}, response) {
exports = function(query) {
    // Data can be extracted from the request as follows:

    // Query params, e.g. '?arg1=hello&arg2=world' => {arg1: "hello", arg2: "world"}
    const {q, f, lat, lng, r, l, e, c, j, p} = query;

    // Headers, e.g. {"Content-Type": ["application/json"]}
    //const contentTypes = headers["Content-Type"];

    // Raw request body (if the client sent one).
    // This is a binary object that can be accessed as a string using .text()
    //const reqBody = body;

    //console.log("query, fuzzy, lat, lng, radius, limit: ", q, f, lat, lng, r, l);
    //console.log("Content-Type:", JSON.stringify(contentTypes));
    //console.log("Request body:", reqBody);
    
    // You can use 'context' to interact with other Realm features.
    // Accessing a value:
    // var x = context.values.get("value_name");

    // Querying a mongodb service:
    // const doc = context.services.get("mongodb-atlas").db("dbname").collection("coll_name").findOne();

    // Calling a function:
    // const result = context.functions.execute("function_name", arg1, arg2);
    let must=[];
    let fuzzy = {};
    if(f>0){
      fuzzy = {
        'maxEdits': parseInt(f),
        'prefixLength': 0
      };
    }
    let paths = [];
    if(p){
      paths.push(p);
    }
    if(e){
      paths.push({"value": 'content', "multi": "english"});
    }
    if(c){
      paths.push({"value": 'content', "multi": "chinese"});
    }
    if(j){
      paths.push({"value": 'content', "multi": "japanese"});
    }
    if(!p && !e && !c && !j){
      paths.push({'wildcard': '*'});
    }
    if(q){
      must.push({
          'text': {
              'query': q,
              'path': paths,
              'fuzzy': fuzzy
          }
      });
    }
    if(lat && lng && r){
      must.push({
          'geoWithin': { 
              'path' : 'location',
              'circle' : { 'center': { "type": "Point", "coordinates": [parseFloat(lng), parseFloat(lat)]}, 'radius': parseInt(r) }
          }
      });
    }
    const search = {
        '$search': {
            'index':'default',
            'compound':{
                'must':must
            },
            'highlight': { 
                'path': paths
            }
        }
    };
    
    const agg_pipeline = [
        search, {
            '$project': {
                'document': "$$ROOT",
                'highlights': {"$meta": "searchHighlights"},
                'score': {
                    '$meta': 'searchScore'
                }
            }
        },
        {
          '$limit': parseInt(l)
        }
    ];
    //const results =context.services.get("atlas-search").db("search").collection("data").findOne();
    const results = context.services.get("atlas-search").db("search").collection("data").aggregate(agg_pipeline);
    console.log("params:{q:\""+q+"\", f: "+f+", lat:"+lat+", lng:"+lng+", r:"+r+", l:"+l+", c:"+c+", j:"+j+", e:"+e+", p:"+p+"}");
    return results; 
};
```
### 2.2. Create HTTPS endpoint
Create the HTTPS endpoint that serve requests to run the function

## 3. Update index.html

- Application Id: search-application-hhpfp
- API Key: HSinvK2vBWW67iyKzXnhfCqyRNirxm4l7FiT4g3rMn3NJN3T5JACnAFSF4zzFLWy
