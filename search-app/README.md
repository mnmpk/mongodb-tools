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
[search.js](functions/search.js)
### 2.2. Create HTTPS endpoint
Create the HTTPS endpoint that serve requests to run the function

## 3. Update index.html

- Application Id: search-app-zznhw
