import os
from strategy import Strategy
import matplotlib.pyplot as plt
import numpy as np

PATH = os.path.realpath(os.path.dirname(__file__))
SCORE_FILE = PATH + "\\Score.txt"

def get_txt():
    result = []
    for root, dirs, files in os.walk(PATH, topdown=False):
        for name in files:
            file = os.path.join(root, name)
            if not os.path.dirname(file) == PATH and file[-3:] == 'txt':
                result.append(file)
    return result

def file_to_strategies(strategies, file_path):
    file_copy = file_path[:]
    type = file_copy.split("\\")[-1].split(".")[0]
    serialization = ""

    if type == "BinSave":
        serialization = "Binary"
    else:
        serialization = "Json"

    with open(file_path, "r") as f:
        lines = f.readlines()
        for line in lines[1:]:
            strategy = Strategy(serialization, line)
            strategies = strategy.add(strategies)

    return strategies

def is_sorted(list):
    for i in range(len(list)-1):
        if list[i][1] > list[i+1][1]:
            return False
    return True

def sort(list):
    while not is_sorted(list):
        for i in range(len(list)-1):
            if list[i][1] > list[i+1][1]:
                (list[i], list[i+1]) = (list[i+1], list[i])

    return list

def get_keys(strategies):
    keys = []
    for s in strategies:
        for key in s.results.keys():
            if not key in keys:
                keys.append(key)
    return keys

def get_functions(strategies):
    functions = []

    for key in get_keys(strategies):
        if not key[0] in functions:
            functions.append(key[0])
    
    return functions

def calc_score(strategies):
    for key in get_keys(strategies):
        i = 0
        results = []

        for s in strategies:
            if key in s.results:
                results.append((i, float(s.results[key][0])))
            else:
                print(str(key) + " not in " + str(s)) #TODO: Get missing benchmarks
            i += 1
       
        results = sort(results)
        i = 0

        for result in results:
            strategies[result[0]].set_score(key, i)
            i += 1

    return strategies

def get_sorted(strategies):
    results = []
    i = 0

    for s in strategies:
        results.append((i, s.score()))
        i += 1

    return list(reversed(sort(results)))

def get_sorted_key(strategies, key):
    results = []
    i = 0

    for s in strategies:
        if key in s.results:
            results.append((i, s.results[key][1]))
        else:
            results.append((i, 0))
        i += 1

    return list(reversed(sort(results)))

def get_sorted_function(strategies, function):
    results = []
    i = 0
    keys = [(function, "1000"), (function, "10000"), (function, "100000")]

    for s in strategies:
        value = 0
        for key in keys: 
            if key in s.results:
                value += s.results[key][1]
        results.append((i, value))
        i += 1

    return list(reversed(sort(results)))

def write_sorted(strategies):
    with open(SCORE_FILE, "w") as f:
        results = get_sorted(strategies)
        f.write("Top 10:\n")
        for result in results[:9]:
            s = strategies[result[0]]
            f.write(str(s) + "\n")

        for function in get_functions(strategies):
            f.write(f"\nTop 5 with function {function}:\n")
            for result in get_sorted_function(strategies, function)[:5]:
                s = strategies[result[0]]
                f.write(str(s) + " | " + str(result[1]) + "\n")

        for key in get_keys(strategies):
            f.write(f"\nTop 5 with {key}:\n")
            for result in get_sorted_key(strategies, key)[:5]:
                s = strategies[result[0]]
                f.write(str(s) + " | " + str(s.results[key][0]) + " ops/s\n")

        f.write("\nAll results:\n")
        for result in results:
            s = strategies[result[0]]
            f.write("="*145 + "\n")
            f.write(str(s) + "\n")
            for (key, value) in s.results.items():
                f.write(f"{key} => ({value[0]}, {value[1]}/{len(results)})\n")

def plot_func(strateigies, function):
    x = []
    y1 = []
    y2 = []
    y3 = []

    for result in get_sorted_function(strategies, function)[:10]:
        s = strategies[result[0]]
        x.append(s.label())
        y1.append(float(s.results[(function, "1000")][0]))
        y2.append(float(s.results[(function, "10000")][0]))
        y3.append(float(s.results[(function, "100000")][0]))

    x_axis = np.arange(len(x))

    plt.bar(x_axis -0.2, y1, 0.2, label = '1000')
    plt.bar(x_axis, y2, 0.2, label = '10000')
    plt.bar(x_axis + 0.2, y3, 0.2, label = '100000')
    plt.xticks(x_axis, x)
    plt.legend()
    plt.xlabel("Strategien")
    plt.ylabel("ops/s")
    plt.title("Top 5 " + function)
    plt.yscale('log',base=2) 
    plt.show()

def plot(strategies):
    plot_func(strategies, "createGame")


if __name__ == "__main__":
    #Convert file data to strategy objects
    strategies = []
    for file in get_txt():
        strategies = file_to_strategies(strategies, file)

    #Calculate score and print to file
    write_sorted( calc_score(strategies))

    #Plot results
    plot(strategies)