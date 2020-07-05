import json
import sys
import threading
import numpy
import datetime
import random
import requests

i = 0
production_power_sum = 0
load_power_sum = 0

# If we get a positive value from the function, it is returned. If negative we draw a small number.
def count_function(function, x):
    y = function(x);
    if y > 0:
        return y
    else:
        return random.randint(1, 10) / 100


def production_power_function(x):
    return numpy.sin(numpy.pi / 13 * (x - 4))


def load_power_function(x):
    return numpy.sin(numpy.pi / 6 * x - 2)


def post_power(id_type, id, power):
    if id_type == "inverterId":
        url = 'http://localhost:8082/power/production'
    elif id_type == "receiverId":
        url = 'http://localhost:8082/power/consumption'

    obj = {id_type: id, 'value': round(power, 3)}
    headers = {'content-type': 'application/json'}
    x = requests.post(url, data=json.dumps(obj), headers=headers)
    if x.status_code == 200:
        print("Post request sent " + str(obj))
    else:
        print("Problem with sending post request. Status code: " + str(x.status_code))


def house():
    global period_of_time, i, production_power_sum, load_power_sum
    now = datetime.datetime.now()
    production_power_sum += count_function(production_power_function, now.hour + (now.minute * 60 + now.second) / 3600)
    load_power_sum += count_function(load_power_function, now.hour + (now.minute * 60 + now.second) / 3600)

    i += 1
    if i == period_of_time:
        post_power("inverterId", 42, production_power_sum / i + 1)
        post_power("receiverId", 23, load_power_sum / i + 1)
        i = 0
        production_power_sum = 0
        load_power_sum = 0

    threading.Timer(1, house).start()


if __name__ == '__main__':
    global period_of_time
    if len(sys.argv) < 2:
        print("As a parameter, enter a number specifying every how many seconds post request should be sent.")
    else:
        period_of_time = int(sys.argv[1])
        house()
