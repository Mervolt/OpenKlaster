import growatt
import json
import pymongo
import requests
import yaml
import sys
import concurrent.futures
from unit_converter.converter import converts


def get_json_object(id, value):
    return {"installationId": id, 'value': value}


def post_measurements_growatt(id, username, password):
    with growatt.GrowattApi() as api:
        api.login(username, password)
        plant_info = api.plant_list()
        power_value = float(converts(plant_info['totalData']['currentPowerSum'], 'kW').split()[0])
        power_status_code = post_power_production(power_value, id)
        energy_value = float(
            converts(plant_info['totalData']['totalEnergySum'].replace('Wh', 'W*h'), 'kW*h').split()[0])
        energy_status_code = post_energy_produced(energy_value, id)
        return id, power_status_code, energy_status_code


def post_power_production(value, id):
    global power_production_url, headers, params
    obj = get_json_object(id, value)
    response = requests.post(power_production_url, data=json.dumps(obj), headers=headers, params=params, timeout=5)
    return response.status_code


def post_energy_produced(value, id):
    global energy_produced_url, headers, params
    obj = get_json_object(id, value)
    response = requests.post(energy_produced_url, data=json.dumps(obj), headers=headers, params=params, timeout=5)
    return response.status_code


if __name__ == '__main__':
    config_yml = "config-dev.yml"
    if len(sys.argv) >= 2:
        config_yml = sys.argv[1]

    with open(config_yml, "r") as ymlfile:
        config = yaml.safe_load(ymlfile)

        power_production_url = config["request"]["powerProductionUrl"]
        energy_produced_url = config["request"]["powerProducedUrl"]
        headers = config["request"]["headers"]
        params = config["request"]["params"]

        mongo_client = pymongo.MongoClient(config["address"])
        database = mongo_client[config["database"]]
        collection = database[config["collection"]]
        documents = collection.find({}, {"inverter.manufacturer": 1, "inverter.credentials": 1, "_id": 1})

        with concurrent.futures.ThreadPoolExecutor(max_workers=5) as executor:
            futures = []
            for installation in documents:
                try:
                    credentials = json.loads(json.dumps(installation["inverter"]["credentials"]))
                    if installation["inverter"]["manufacturer"] == config["producers"]["growatt"]:
                        futures.append(
                            executor.submit(post_measurements_growatt, installation["_id"], credentials["Username"],
                                            credentials["Password"]))
                except Exception as e:
                    # theoretically, the error will not occur here if the database contains correct data
                    print(e)

            for future in concurrent.futures.as_completed(futures):
                try:
                    id, power_status_code, energy_status_code = future.result()
                    print('Post for %s - Status code (Power: %s Energy: %s)' % (id, power_status_code, energy_status_code))
                except Exception as exc:
                    print('%s generated an exception: %s' % (installation["_id"], exc))
