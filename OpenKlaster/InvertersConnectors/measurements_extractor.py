import pymongo
import growatt
import json
import requests


def growat(username, password):
    with growatt.GrowattApi() as api:
        api.login(username, password)
        plant_info = api.plant_list()
        print(plant_info['totalData']['totalEnergySum'].replace(' kWh', ''))
        print(plant_info['totalData']['currentPowerSum'].replace(' W', ''))
        post_power_production(plant_info['totalData']['currentPowerSum'].replace(' W', ''))


def post_power_production(value):
    headers = {'content-type': 'application/json'}
    url = 'http://localhost:8082/api/1/powerProduction'
    params = {'apiToken': '4J8p6gh7ThYU'}
    obj = {"installationId": "installation:3", 'value': float(value)}
    print(headers)
    print(params)
    print(obj)
    x = requests.post(url, data=json.dumps(obj), headers=headers, params=params)

    if x.status_code == 200:
        print("Post request with power production sent: " + str(obj))
    else:
        print("Problem with sending post request with power production. Status code: " + str(x.status_code))


myclient = pymongo.MongoClient("mongodb://localhost:27017/")
mydb = myclient["DEFAULT_DB"]
mycol = mydb["installations"]
mydoc = mycol.find({}, {"inverter.manufacturer": 1, "inverter.credentials": 1, "_id": 0})

growat('test56323', 'fwej32rf3R')

for x in mydoc:
    if (x['inverter']['manufacturer'] == 'string'):
        print(x['inverter']['credentials'])
