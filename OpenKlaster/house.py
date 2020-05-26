import json
import sys
import threading
import numpy
import datetime
import random
import requests


i = 0
energy_produced = 0
energy_consumed = 0


# If we get a positive value from the function, it is returned. If negative we draw a small number.
def count_function(function, x):
    y = function(x);
    if y > 0:
        return y
    else:
        return random.randint(1, 10) / 100


def energy_production_function(x):
    return numpy.sin(numpy.pi / 13 * (x - 4))


def energy_load_function(x):
    return numpy.sin(numpy.pi / 6 * x - 2)


def post_energy(id_type, id, energy):
    if id_type == "inverterId":
        url = 'http://localhost:8082/power/production'
    elif id_type == "receiverId":
        url = 'http://localhost:8082/power/consumption'

    obj = {id_type: id, 'value': round(energy, 3)}
    headers = {'content-type': 'application/json'}
    x = requests.post(url, data=json.dumps(obj), headers=headers)
    if x.status_code == 200:
        print("Post request sent " + str(obj))
    else:
        print("Problem with sending post request. Status code: " + str(x.status_code))


def house():
    global period_of_time, i, energy_produced, energy_consumed
    now = datetime.datetime.now()
    energy_produced += count_function(energy_production_function, now.hour + (now.minute * 60 + now.second) / 3600)
    energy_consumed += count_function(energy_load_function, now.hour + (now.minute * 60 + now.second) / 3600)

    i += 1
    if i == period_of_time:
        post_energy("inverterId", 42, energy_produced)
        post_energy("receiverId", 23, energy_consumed)
        i = 0
        energy_produced = 0
        energy_consumed = 0

    threading.Timer(1, house).start()


if __name__ == '__main__':
    global period_of_time
    if len(sys.argv) < 2:
        print("As a parameter, enter a number specifying every how many seconds post request should be sent.")
    else:
        period_of_time = int(sys.argv[1])
        house()
