// This function is the endpoint's request handler.
//exports = function({ query, headers, body}, response) {
exports = function(query) {
    // Data can be extracted from the request as follows:

    // Query params, e.g. '?arg1=hello&arg2=world' => {arg1: "hello", arg2: "world"}
    const {q, f, lat, lng, r, l, k, e, c, j, p} = query;

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
    if(q){
      let should = [];
      if(k){
        var kp = {"value": p, "multi": "keyword"};
        paths.push(kp);
        should.push({ "regex": { "query": ".*"+q+".*", "path": [kp] } });
      }
      if(e){
        var ep = {"value": p, "multi": "english"};
        paths.push(ep);
        should.push({ "text": { "query": q, "path": [ep], 'fuzzy': fuzzy } });
      }
      if(c){
        var cp = {"value": p, "multi": "chinese"};
        paths.push(cp);
        should.push({ "text": { "query": q, "path": [cp], 'fuzzy': fuzzy } });
      }
      if(j){
        var jp = {"value": p, "multi": "japnaese"};
        paths.push(jp);
        should.push({ "text": { "query": q, "path": [jp], 'fuzzy': fuzzy } });
      }
      if(!e && !c && !j && !k){
        if(p){
          paths.push(p);
          should.push({ "text": { "query": q, "path": p, 'fuzzy': fuzzy } });
        }else{
          var wildcard = {'wildcard': '*'};
          paths.push(wildcard);
          should.push({ "text": { "query": q, "path": wildcard}});
        }
      }
      
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
                'must':must,
                'should':should
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
    console.log("params:{q:\""+q+"\", f: "+f+", lat:"+lat+", lng:"+lng+", r:"+r+", l:"+l+", k:"+k+", e:"+e+", c:"+c+", j:"+j+", p:"+p+"}");
    return results; 
};
