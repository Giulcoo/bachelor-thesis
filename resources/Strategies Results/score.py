import os

PATH = os.path.realpath(os.path.dirname(__file__))

class Strategie:
    def __init__(self, serialization, line):
        self.serialization = serialization

        words = line.split()

        benchmark = words[0].split(".")
        chunkMaxElements = words[1]
        chunkMinElements = words[2]
        dataCount = words[3]
        staticChunkAmount = words[4]
        useChangeFile = words[5]
        useGzip = words[6]
        score = ""

        if serialization == "Json":
            score = words[9]
            
            if useGzip == "true":
                self.gzip = True
            else:
                self.gzip = False
        else:
            score = words[8]
            self.gzip = False

        if useChangeFile == "true":
            self.changefile = True
        else:
            self.changefile = False

        if benchmark[0] == "DynamicBenchmark":
            self.dynamic = True
            self.maxchunkelements = int(chunkMaxElements)
            self.mingroupelements = int(chunkMinElements)
            self.chunkamount = 0
        else:
            self.dynamic = False
            self.chunkamount = staticChunkAmount
            self.maxchunkelements = 0
            self.mingroupelements = 0

        self.results = {
            (benchmark[1], dataCount): score
        }

        self.score = 0

    def __str__(self):
        result = f"Serialization: {self.serialization} | Change File: {self.changefile} | "
        if self.gzip:
            result += f"Gzip: {self.gzip} | "
        
        result += f"Dynamic Chunk: {self.dynamic} | "

        if self.dynamic:
            result += f"Max Elements/Chunk: {self.maxchunkelements} | Min Elements/Group: {self.mingroupelements}"
        else:
            result += f"Chunk Amount: {self.chunkamount}x{self.chunkamount}"

        return result
    
    def __eq__(self, other):
        """Overrides the default implementation"""
        if isinstance(other, Strategie):
            result = self.serialization == other.serialization
            result = result and self.changefile == other.changefile
            result = result and self.gzip == other.gzip
            result = result and self.dynamic == other.dynamic
            result = result and self.maxchunkelements == other.maxchunkelements
            result = result and self.mingroupelements == other.mingroupelements
            result = result and self.chunkamount == other.chunkamount
            return result
        return False
    
    def add(self, list):
        in_list = False
        index = 0
        for strategie in list:
            if strategie == self:
                in_list = True
            index += 1

        if in_list:
            list[index].results.update(self.results)
        else:
            list.append(self)

        return list



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
            print(line)
            strategie = Strategie(serialization, line)
            strategies = strategie.add(strategies)

    return strategies


if __name__ == "__main__":
    strategies = []
    for file in get_txt():
        strategies = file_to_strategies(strategies, file)
