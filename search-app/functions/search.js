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
    //const results =context.services.get("mongodb-atlas").db("search").collection("data").findOne();
    const results = context.services.get("mongodb-atlas").db("search").collection("data").aggregate(agg_pipeline);
    console.log("params:{q:\""+q+"\", f: "+f+", lat:"+lat+", lng:"+lng+", r:"+r+", l:"+l+", c:"+c+", j:"+j+", e:"+e+", p:"+p+"}");
    return results; 
};
