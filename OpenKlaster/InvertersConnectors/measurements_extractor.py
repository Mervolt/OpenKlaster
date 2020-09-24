import pymongo

myclient = pymongo.MongoClient("mongodb://localhost:27017/")
mydb = myclient["DEFAULT_DB"]
mycol = mydb["installations"]
mydoc = mycol.find({}, {"inverter.manufacturer": 1, "inverter.credentials": 1, "_id": 0})

for x in mydoc:
    if (x['inverter']['manufacturer'] == 'string'):
        print(x['inverter']['credentials'])
