import os
from strategy import Strategy
import matplotlib.pyplot as plt
import numpy as np

PATH = os.path.realpath(os.path.dirname(__file__))
SCORE_FILE = PATH + "\\Score.txt"
BENCHMARK = PATH + "\\Benchmarks"

def get_txt():
    result = []
    for root, dirs, files in os.walk(BENCHMARK, topdown=False):
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
        for line in lines:
            words = line.split()
            if len(words) < 3 or words[0] == "Benchmark":
                continue
            
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

def create_table(title, strategies, results, use_extra_column):
    table = "Strategy" + " " * 18 + "| " + "Chunks" + " " * 60 + "| Score "

    equal = "="*102 + "\n"
    if use_extra_column:
        table += " | Category Score "
        equal = "="*120 + "\n"

    width = len(table)
    table += "\n" + "-" * width + "\n"

    table = "="*(len(title)+2) + "\n" + title + " ║\n" + equal + table
    for result in results:
        s = strategies[result[0]]
        table += s.str_short()

        if use_extra_column:
            table += "| " + str(result[1])
        
        table += "\n"

    return table + "-"*width + "\n\n"

def write_sorted(strategies):
    with open(SCORE_FILE, "w", encoding="utf-8") as f:
        results = get_sorted(strategies)
        f.write(create_table("Top 10", strategies, results[:9], False))

        for function in get_functions(strategies):
            f.write(create_table(f"Top 5 with function {function}", strategies, get_sorted_function(strategies, function)[:5], True))

        for key in get_keys(strategies):
            f.write(create_table(f"Top 5 with {key}", strategies, get_sorted_key(strategies, key)[:5], True))

        f.write("\n"*3)
        f.write("\nAll results:\n")
        for result in results:
            s = strategies[result[0]]
            f.write("="*145 + "\n")
            f.write(str(s) + "\n")
            f.write(s.results_str(len(results)))

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
    #plot(strategies)