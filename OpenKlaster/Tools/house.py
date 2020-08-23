import json
import sys
import threading
import numpy
import datetime
import random
import requests
import yaml

from datetime import date

headers = {'content-type': 'application/json'}
i = 0
production_power_sum = 0
load_power_sum = 0


# If we get a positive value from the function, it is returned. If negative we draw a small number.
def count_function(function, x):
    y = function(x);
    if y > 0:
        return y
    else:
        return random.randint(1, 10) / 1000


def production_power_function(x):
    return numpy.sin(numpy.pi / 13 * (x - 4))


def load_power_function(x):
    return numpy.sin(numpy.pi / 6 * x - 2)


def get_json_object(id, value, timestamp = None):
    if timestamp == None:
        return {"installationId": id, 'value': round(value, 3)}
    else:
        return {"installationId": id, 'value': round(value, 3), 'timestamp': timestamp}


def get_params(token):
    return {'apiToken': token}


def post_power_production(params, obj):
    global headers
    url = cfg["powerProduction"]

    x = requests.post(url, data=json.dumps(obj), headers=headers, params=params)

    if x.status_code == 200:
        print("Post request with power production sent: " + str(obj))
    else:
        print("Problem with sending post request with power production. Status code: " + str(x.status_code))


def post_power_consumption(params, obj):
    global headers
    url = cfg["powerConsumption"]

    x = requests.post(url, data=json.dumps(obj), headers=headers, params=params)

    if x.status_code == 200:
        print("Post request with power consumption sent: " + str(obj))
    else:
        print("Problem with sending post request with power consumption. Status code: " + str(x.status_code))


def house():
    global period_of_time, i, production_power_sum, load_power_sum, id
    now = datetime.datetime.now()
    production_power_sum += count_function(production_power_function, now.hour + (now.minute * 60 + now.second) / 3600)
    load_power_sum += count_function(load_power_function, now.hour + (now.minute * 60 + now.second) / 3600)

    i += 1
    if i == period_of_time:
        post_power_production(get_params(token), get_json_object(id, production_power_sum / (i + 1)))
        post_power_consumption(get_params(token), get_json_object(id, load_power_sum / (i + 1)))
        i = 0
        production_power_sum = 0
        load_power_sum = 0

    threading.Timer(1, house).start()

def fill_up(hour, minute, second):
    global period_of_time, i, production_power_sum, load_power_sum, id
    production_power_sum += count_function(production_power_function, hour + (minute * 60 + second) / 3600)
    load_power_sum += count_function(load_power_function, hour + (minute * 60 + second) / 3600)

    if i == period_of_time:
        if hour < 10:
            hour = "0" + str(hour)
        else:
            hour = str(hour)

        if minute < 10:
            minute = "0" + str(minute)
        else:
            minute = str(minute)

        if second < 10:
            second = "0" + str(second)
        else:
            second = str(second)

        now = str(date.today()) + " " + hour + ":" + str(minute) + ":" + str(second)

        post_power_production(get_params(token), get_json_object(id, production_power_sum / (i + 1), now))
        post_power_consumption(get_params(token), get_json_object(id, load_power_sum / (i + 1), now))
        i = 0
        production_power_sum = 0
        load_power_sum = 0
    i += 1

if __name__ == '__main__':
    global token, id, period_of_time
    if len(sys.argv) < 4:
        print(
            "As a parameter, enter a token, installationId and number specifying every how many seconds post request should be sent.")
        print("Example: py house.py 5CS4hU55fWyy installation:0 60")
    else:
        token = sys.argv[1]
        id = sys.argv[2]
        period_of_time = int(sys.argv[3])

        with open("house_config.yml", "r") as ymlfile:
            cfg = yaml.safe_load(ymlfile)

        if cfg["mode"] == "STANDARD":
            house()
        elif cfg["mode"] == "FILLUP":
            for hour in range(0, 24):
               for minute in range(0, 60):
                   for second in range(0, 60):
                       fill_up(hour, minute, second)
