import threading
import numpy
import datetime
import random
import requests

period_of_time = 30

i = 0
energy_produced = 0
energy_consumed = 0


def energy_porduction(x):
    produced = numpy.sin(numpy.pi / 13 * (x - 4))
    if produced > 0:
        return produced
    else:
        return random.randint(1, 10) / 100


def energy_load(x):
    load = numpy.sin(numpy.pi / 6 * x - 2)
    if load > 0:
        return load
    else:
        return random.randint(1, 10) / 100


def post_energy_porduction(energy_produced, device_id):
    url = 'http://localhost:8082/power/production/'
    myobj = {'deviceId': device_id, 'value': round(energy_produced, 3)}
    print(myobj)
    x = requests.post(url, data=myobj)


def post_energy_load(energy_consumed, inverter_id):
    url = 'http://localhost:8082power/consumption/'
    myobj = {'inverterId': inverter_id, 'value': round(energy_consumed, 3)}
    print(myobj)
    x = requests.post(url, data=myobj)


def house():
    global period_of_time, i, energy_produced, energy_consumed
    now = datetime.datetime.now()
    energy_produced += energy_porduction(now.hour + (now.minute * 60 + now.second) / 3600)
    energy_consumed += energy_load(now.hour + (now.minute * 60 + now.second) / 3600)

    i += 1
    if i == period_of_time:
        post_energy_porduction(energy_produced, 42)
        post_energy_load(energy_consumed, 23)
        i = 0
        energy_produced = 0
        energy_consumed = 0

    threading.Timer(1, house).start()


if __name__ == '__main__':
    house()
