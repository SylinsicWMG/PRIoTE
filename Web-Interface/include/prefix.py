from csv import DictReader
from os import path

prefixes = {}
namespaces = {}

def loadPrefixes():
    this_dir, this_filename = path.split(__file__)
    prefixCSV = path.join(this_dir, "prefixes.csv")
    with open(prefixCSV, "r") as csvFile:
        csvReader = DictReader(csvFile, delimiter=",")
        for row in csvReader:
            prefixes[row["Prefix"]] = row["Namespace"]
    for i in prefixes:
        namespaces[prefixes[i]] = i

def addPrefix(prefix, namespace):
    prefixes[prefix] = namespace