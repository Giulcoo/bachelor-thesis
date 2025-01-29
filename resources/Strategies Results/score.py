import os
from strategy import Strategy
import matplotlib.pyplot as plt
import numpy as np

PATH = os.path.realpath(os.path.dirname(__file__))
SCORE_FILE = PATH + "\\Score.txt"
BENCHMARK = PATH + "\\Benchmarks"
PLOT = PATH + "\\Plots"

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
    keys = [(function, "1000"), (function, "10000"), (function, "100000")]
    i = 0

    for s in strategies:
        value = 0
        for key in keys: 
            if key in s.results:
                value += s.results[key][1]
        results.append((i, value))
        i += 1

    return list(reversed(sort(results)))

def get_sorted_datacount(strategies, datacount):
    results = []
    keys = [(func, datacount) for func in get_functions(strategies)]
    i = 0

    for s in strategies:
        value = 0
        for key in keys: 
            if key in s.results:
                value += s.results[key][1]
        results.append((i, value))
        i += 1

    return list(reversed(sort(results)))

def create_table(title, strategies, results, use_extra_column, key):
    table = "Strategy" + " " * 18 + "| " + "Chunks" + " " * 61 + "| Score "

    equal = "="*102 + "\n"
    if use_extra_column and key == None:
        table += " | Category Score "
        equal = "="*120 + "\n"
    elif key != None:
        table += " | ops/s       "
        equal = "="*120 + "\n"

    width = len(table)
    table += "\n" + "-" * width + "\n"

    table = "="*(len(title)+2) + "\n" + title + " ║\n" + equal + table

    print()
    print(title)

    is_func = False
    function = ""
    func_lines = "\\hline\nStrategie & Datenmenge & ops/s \\\\ \n\\hline \n"
    func_index = 1
    if title.split()[-2] == "function":
        is_func = True
        function = title.split()[-1]

    for result in results:
        s = strategies[result[0]]
        print(s.latex_table(key))

        if is_func:
            for count in ['1000', '10000', '100000']:
                func_lines += str(func_index) + " & " + count + " & " + s.results[(function, count)][0] + "\\\\ \n"
            
            if func_index != 5:
                func_lines +=  " & & \\\\ \n"
            func_index += 1
        table += s.table()

        if use_extra_column and key == None:
            table += "| " + str(result[1])
        elif key != None:
            table += "| " + s.results[key][0]
        
        table += "\n"

    if is_func:
        print(func_lines + "\\hline\n")

    print()

    return table + "-"*width + "\n\n"

def write_sorted(strategies):
    with open(SCORE_FILE, "w", encoding="utf-8") as f:
        results = get_sorted(strategies)
        f.write(create_table("Top 10", strategies, results[:10], False, None))

        binary = []
        json = []
        dynamic = []
        static = []
        for result in results:
            s = strategies[result[0]]

            if s.serialization == "Binary":
                binary.append(result) 
            else:
                json.append(result)

            if s.dynamic:
                dynamic.append(result) 
            else:
                static.append(result)

        results = get_sorted(strategies)
        f.write(create_table("Top 10 with binary Serialization", strategies, binary[:10], False, None))
        f.write(create_table("Top 10 with JSON-Serialization", strategies, json[:10], False, None))
        f.write(create_table("Top 5 with dynamic Chunk-System", strategies, dynamic[:5], False, None))
        f.write(create_table("Top 5 with static Chunk-System", strategies, static[:5], False, None))

        for function in get_functions(strategies):
            f.write(create_table(f"Top 5 with function {function}", strategies, get_sorted_function(strategies, function)[:5], True, None))

        for datacount in ['1000', '10000', '100000']:
            f.write(create_table(f"Top 5 with {datacount} data count", strategies, get_sorted_datacount(strategies, datacount)[:5], True, None))

        for key in get_keys(strategies):
            f.write(create_table(f"Top 5 with {key}", strategies, get_sorted_key(strategies, key)[:5], True, key))


        f.write("\n"*3)
        f.write("\nAll results (ordered by best average performing):\n")
        for result in results:
            s = strategies[result[0]]
            f.write("="*110 + "\n")
            f.write(str(s) + "\n\n")
            f.write(s.results_str(len(results)))

def plot_top(strategies):
    x = []
    y = []

    for result in get_sorted(strategies)[:10]:
        s = strategies[result[0]]
        x.append(s.label())
        y.append(s.score())

    x_axis = np.arange(len(x))

    fig = plt.figure()
    plt.bar(x,y)
    plt.xlabel("Strategien")
    plt.ylabel("Score")
    plt.xticks(x_axis, x)
    plt.yticks([100*x for x in range(0,14)]) 
    plt.title("Top 10 Strategien")
    plt.gcf().set_size_inches(14, 5)
    plt.savefig(PLOT + "//top.png", dpi=300, bbox_inches='tight')
    plt.close(fig)

def plot_serialization(strategies, serialization):
    x = []
    y = []

    i = 0
    for result in get_sorted(strategies):
        s = strategies[result[0]]
        if s.serialization == serialization: 
            x.append(s.label())
            y.append(s.score())
            i += 1
        
        if i >= 10:
            break

    x_axis = np.arange(len(x))

    fig = plt.figure()
    plt.bar(x,y)
    plt.xlabel("Strategien")
    plt.ylabel("Score")
    plt.xticks(x_axis, x)
    plt.title("Top 10 Strategien mit " + serialization + "-Serialisierung")
    plt.gcf().set_size_inches(14, 5)
    plt.savefig(PLOT + "//" + serialization + ".png", dpi=300, bbox_inches='tight')
    plt.close(fig)

def plot_chunk_system(strategies, dynamic):
    x = []
    y = []

    i = 0
    for result in get_sorted(strategies):
        s = strategies[result[0]]
        if s.dynamic == dynamic: 
            x.append(s.label())
            y.append(s.score())
            i += 1
        
        if i >= 5:
            break

    x_axis = np.arange(len(x))

    fig = plt.figure()
    plt.bar(x,y)
    plt.xlabel("Strategien")
    plt.ylabel("Score")
    plt.xticks(x_axis, x)
    plt.gcf().set_size_inches(7, 5)

    if dynamic:
        plt.title("Top 10 Strategien mit dynamischen Chunk-System")
        plt.savefig(PLOT + "//dynamisch.png", dpi=300, bbox_inches='tight')
    else:
        plt.title("Top 10 Strategien mit statischen Chunk-System")
        plt.savefig(PLOT + "//statisch.png", dpi=300, bbox_inches='tight')

    plt.close(fig)

def plot_func(strategies, function):
    x = []
    y1 = []
    y2 = []
    y3 = []

    for result in get_sorted_function(strategies, function)[:5]:
        s = strategies[result[0]]
        x.append(s.label())
        y1.append(float(s.results[(function, "1000")][0]))
        y2.append(float(s.results[(function, "10000")][0]))
        y3.append(float(s.results[(function, "100000")][0]))

    x_axis = np.arange(len(x))

    fig = plt.figure()
    plt.bar(x_axis -0.2, y1, 0.2, label = 'Data count 1000')
    plt.bar(x_axis, y2, 0.2, label = 'Data count 10000')
    plt.bar(x_axis + 0.2, y3, 0.2, label = 'Data count 100000')
    plt.xticks(x_axis, x)
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.xlabel("Strategien")
    plt.ylabel("ops/s")
    plt.title("Top 5 bei der Funktion " + function)
    plt.yscale('log',base=2) 
    plt.savefig(PLOT + "//" + function + ".png", dpi=300, bbox_inches='tight')
    plt.close(fig)

def plot_datacount(strategies, datacount):
    x = []
    y = [[], [], [], [], []]

    functions = get_functions(strategies)

    for result in get_sorted_datacount(strategies, datacount)[:5]:
        s = strategies[result[0]]
        x.append(s.label())
        i = 0
        for function in functions:
            y[i].append(float(s.results[(function, datacount)][0]))
            i += 1

    x_axis = np.arange(len(x))

    fig = plt.figure()

    i = 0
    for yVal in y:
        plt.bar(x_axis + (i - 2) * 0.1, yVal, 0.1, label = functions[i])
        i += 1

    plt.xticks(x_axis, x)
    plt.legend(loc='center left', bbox_to_anchor=(1, 0.5))
    plt.xlabel("Strategien")
    plt.ylabel("ops/s")
    plt.title("Top 5 mit " + datacount + " Datengröße")
    plt.yscale('log',base=2) 
    plt.savefig(PLOT + "//" + datacount + ".png", dpi=300, bbox_inches='tight')
    plt.close(fig)

def plot(strategies):
    plot_top(strategies)
    plot_serialization(strategies, "Json")
    plot_serialization(strategies, "Binary")
    plot_chunk_system(strategies, True)
    plot_chunk_system(strategies, False)

    for function in get_functions(strategies):
        plot_func(strategies, function)
    
    for datacount in ['1000','10000', '100000']:
        plot_datacount(strategies, datacount)


if __name__ == "__main__":
    #Convert file data to strategy objects
    print("> Getting Data")
    strategies = []
    for file in get_txt():
        strategies = file_to_strategies(strategies, file)

    print("> Calculate and write score")
    #Calculate score and print to file
    write_sorted(calc_score(strategies))

    print("> Create Plots")
    #Plot results
    plot(strategies)
    print("> Finished")