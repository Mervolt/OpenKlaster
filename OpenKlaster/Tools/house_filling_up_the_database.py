import json
import numpy
import random
import requests
from datetime import date

i = 0
production_power_sum = 0
load_power_sum = 0
period_of_time = 60*5

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


def post_power(id_type, id, energy, now):
    if id_type == "inverterId":
        url = 'http://localhost:8082/power/production'
    elif id_type == "receiverId":
        url = 'http://localhost:8082/power/consumption'

    obj = {id_type: id, 'value': round(energy, 3), 'timestamp': now}
    headers = {'content-type': 'application/json'}
    x = requests.post(url, data=json.dumps(obj), headers=headers)
    if x.status_code == 200:
        print("Post request sent " + str(obj))
    else:
        print("Problem with sending post request. Status code: " + str(x.status_code))


def house(hour, minute, second):
    global period_of_time, i, production_power_sum, load_power_sum
    production_power_sum += count_function(energy_production_function, hour + (minute * 60 + second) / 3600)
    load_power_sum += count_function(energy_load_function, hour + (minute * 60 + second) / 3600)

    i += 1
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

        now = str(date.today()) + "_" + hour + ":" + str(minute) + ":" + str(second)

        post_power("inverterId", 42, production_power_sum / i + 1, now)
        post_power("receiverId", 23, load_power_sum / i + 1, now)
        i = 0
        production_power_sum = 0
        load_power_sum = 0



if __name__ == '__main__':
    for hour in range(0, 24):
        for minute in range(0, 60):
            for second in range(0, 60):
                house(hour, minute, second)
