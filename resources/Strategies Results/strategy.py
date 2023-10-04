class Strategy:
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
        result = ""

        if serialization == "Json":
            if words[9] == "ops/s":
                result = words[8]
            elif words[10] == "ops/s":
                result = words[9]
                useChangeFile = words[5]
                useGzip = words[6]
            elif words[11] == "ops/s":
                result = words[8]
                useChangeFile = words[4]
                useGzip = words[5]
            else:
                result = words[9]
            
            if useGzip == "true":
                self.gzip = True
            else:
                self.gzip = False
        else:
            if len(words) == 8:
                result = words[6]
                useChangeFile = words[4]
            else:
                result = words[8]

                if words[8] == "ops/s":
                    result = words[7]
                else:
                    result = words[8]

            self.gzip = False

        result = result.replace(',', '.')

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
            (benchmark[1], dataCount): (result, 0)
        }

    def __str__(self):
        result = f"Serialization: {self.serialization} | Change File: {self.changefile} | Dynamic Chunk: {self.dynamic} | "

        if self.serialization == "Json":
            result += f"Gzip: {self.gzip} | "

        if self.dynamic:
            result += f"Max Elements/Chunk: {self.maxchunkelements} | Min Elements/Group: {self.mingroupelements}"
        else:
            result += f"Chunk Amount: {self.chunkamount}x{self.chunkamount}"

        result += f" | Score: {self.score()}"

        return result
    
    def label(self):
        result = f"{self.serialization}\n"
        
        if self.dynamic:
            result += " Dynamic\n"
        else:
            result += " Static\n"
        
        if self.changefile:
            result += " Change File\n"

        if self.gzip:
            result += " GZip\n"

        if self.dynamic:
            result += f" ({self.maxchunkelements}, {self.mingroupelements})"
        else:
            result += f" ({self.chunkamount}x{self.chunkamount})"

        return result
    
    def results_str(self, results_len):
        function = "function      "
        dcount = "data count" 
        opsresult = "ops/s   "
        score = "score"
        wall = " | "

        result = function + wall + dcount + wall + opsresult + wall + score + "\n"
        result += "-" * len(result) + "\n"
        for (key, value) in self.results.items():
            (fun,cnt) = key
            (ops,sco) = value

            sco = str(sco) + "/" + str(results_len)

            fun_len = len(function) - len(fun)
            cnt_len = len(dcount) - len(cnt)
            ops_len = len(opsresult) - len(ops)
            
            result += fun + " " * fun_len + wall
            result += cnt + " " * cnt_len + wall
            result += ops + " " * ops_len + wall
            result += sco
            result += "\n" 

            #result += f"{key} => ({value[0]}ops/s, {value[1]}/{results_len})\n"
        return result
    
    def __eq__(self, other):
        """Overrides the default implementation"""
        if isinstance(other, Strategy):
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
        for strategy in list:
            if strategy == self:
                in_list = True
                break
            index += 1

        if in_list:
            list[index].results.update(self.results)
        else:
            list.append(self)

        return list
    
    def set_score(self, key, score):
        self.results[key] = (self.results[key][0], score)
    
    def score(self):
        return sum([value[1] for (key, value) in self.results.items()])