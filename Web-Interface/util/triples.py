from json import loads
from ..include.prefix import namespaces
from re import match
from ..util.queries import GRAPH_NAME, OBJECT_NAME, PREDICATE_NAME, SUBJECT_NAME

def parseResults(toParse, queried=None, vars={'subject': SUBJECT_NAME, 'predicate': PREDICATE_NAME, 'object': OBJECT_NAME, 'graph': GRAPH_NAME}):
    if toParse == False:
        return False

    loaded=loads(toParse)

    varNames = loaded["head"]["vars"]
    
    triples = loaded["results"]["bindings"]


    graphVar = None
    subjectVar = None
    predicateVar = None
    objectVar = None

    for k,v in vars.items():
        if v in varNames:
            if k == "graph":
                graphVar=v
            elif k == "subject":
                subjectVar=v
            elif k == "predicate":
                predicateVar=v
            elif k == "object":
                objectVar=v
            else:
                raise Exception("Unknown field.")
    
    returnedData = {}

    for triple in triples:
        subjectValue = None
        predicateValue = None
        objectValue = None
        
        if graphVar == None:
            if subjectVar != None:
                subjectValue = shortenPrefix(triple[subjectVar]["value"])
            else:
                subjectValue = queried

            if predicateVar != None:
                predicateValue = shortenPrefix(triple[predicateVar]["value"])
            else:
                predicateValue = queried
            
            if objectVar != None:
                objectValue = shortenPrefix(triple[objectVar]["value"])
            else:
                objectValue = queried

            if not subjectValue in returnedData:
                returnedData[subjectValue] = {}
            if not predicateValue in returnedData[subjectValue]:
                returnedData[subjectValue][predicateValue] = objectValue
        else:
            returnedData[triples.index(triple)] = triple[graphVar]["value"]

    return returnedData

def parseAll(toParse):
    if toParse == False:
        return False
    loaded = loads(toParse)

    returnedData = {}

    varNames = loaded["head"]["vars"]
    subjectVar = varNames[0]
    predicateVar = varNames[1]
    objectVar = varNames[2]

    triples = loaded["results"]["bindings"]
    for triple in triples:
        subjectValue = shortenPrefix(triple[subjectVar]["value"])
        predicateValue = shortenPrefix(triple[predicateVar]["value"])
        objectValue = shortenPrefix(triple[objectVar]["value"])

        if not subjectValue in returnedData:
            returnedData[subjectValue] = {}
        if not predicateValue in returnedData[subjectValue]:
            returnedData[subjectValue][predicateValue] = objectValue

    return returnedData

def shortenPrefix(iri):
    if match(r"https?:\/\/[a-zA-Z0-9\.\/\-\_\+]+\#[a-zA-Z0-9\.\/\-\_\+]+", iri):
        namespace, suffix = getPrefixSuffix(iri)
        if namespace in namespaces:
            prefix = namespaces[namespace]
        else:
            prefix = namespace
        return prefix + ":" + suffix
    else:
        return iri

def getPrefixSuffix(iri):
    namespace, _, suffix = iri.partition("#")
    namespace += "#"
    return namespace, suffix