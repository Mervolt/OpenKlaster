import growatt
import json
import pymongo
import requests
import yaml

# Todo error handling, logging, refactor, SOLVE PROBLEMS WITH UNITS

def get_json_object(id, value):
    return {"installationId": id, 'value': round(value, 3)}


def post_measurements_growatt(id, username, password):
    with growatt.GrowattApi() as api:
        api.login(username, password)
        plant_info = api.plant_list()
        if " W" in plant_info['totalData']['currentPowerSum']:
            post_power_production(float(plant_info['totalData']['currentPowerSum'].replace(' W', '')) * 0.001, id)
        elif " kW" in plant_info['totalData']['currentPowerSum']:
            post_power_production(float(plant_info['totalData']['currentPowerSum'].replace(' kW', '')), id)
        post_energy_produced(float(plant_info['totalData']['totalEnergySum'].replace(' kWh', '')), id)


def post_power_production(value, id):
    global power_production_url, headers, params
    obj = get_json_object(id, value)
    x = requests.post(power_production_url, data=json.dumps(obj), headers=headers, params=params)

    if x.status_code == 200:
        print("Post request with power production sent: " + str(obj))
    else:
        print("Problem with sending post request with power production. Status code: " + str(x.status_code))


def post_energy_produced(value, id):
    global energy_produced_url, headers, params
    obj = get_json_object(id, value)
    x = requests.post(energy_produced_url, data=json.dumps(obj), headers=headers, params=params)

    if x.status_code == 200:
        print("Post request with energy produced sent: " + str(obj))
    else:
        print("Problem with sending post request with energy produced. Status code: " + str(x.status_code))


if __name__ == '__main__':
    with open("config.yml", "r") as ymlfile:
        config = yaml.safe_load(ymlfile)

        power_production_url = config["request"]["powerProductionUrl"]
        energy_produced_url = config["request"]["powerProducedUrl"]
        headers = config["request"]["headers"]
        params = config["request"]["params"]

        mongo_client = pymongo.MongoClient(config["address"])
        database = mongo_client[config["database"]]
        collection = database[config["collection"]]
        documents = collection.find({}, {"inverter.manufacturer": 1, "inverter.credentials": 1, "_id": 1})

        for installation in documents:
            credentials = json.loads(installation["inverter"]["credentials"])
            if installation["inverter"]["manufacturer"] == config["producers"]["growatt"]:
                post_measurements_growatt(installation["_id"], credentials["username"], credentials["password"])
