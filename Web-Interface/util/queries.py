from flask import session
from requests import post
from ..include import prefix

GRAPH_NAME="g"
SUBJECT_NAME="s"
PREDICATE_NAME="p"
OBJECT_NAME="o"

def listQuery(graph, subject=None, predicate=None, object=None, searchString=None):
    host = session["host"]
    port = session["port"]
    database = session["database"]
    valid = session["valid"]

    if valid:
        url = f"http://{host}:{port}/{database}/sparql"
        prefixes = generatePrefixes("hqdm", "magmardl", "magmauser", "rdf", "rdfs")

        selectFromGraph = prefixes + " SELECT * FROM <" + graph + "> "

        if subject != None and subject != '':
            query = selectFromGraph + "WHERE { " + subject + " ?" + PREDICATE_NAME + " ?" + OBJECT_NAME + " }"
        elif predicate != None and predicate != '':
            query = selectFromGraph + "WHERE { ?" + SUBJECT_NAME + " " + predicate + " ?" + OBJECT_NAME + " }"
        elif object != None and object != '':
            try:
                pre, _, suf = object.partition(":")
                if not pre in prefix.prefixes:
                    object = f"\"{object}\""
                else:
                    object = f"\"{prefix.prefixes[pre]}{suf}\""
            except:
                object = f"\"{object}\""
            query = selectFromGraph + "WHERE { ?" + SUBJECT_NAME + " ?" + PREDICATE_NAME + " " + object + " }"
        elif searchString != None and searchString != '':
            query = selectFromGraph + "WHERE {" + searchString + "}"
        else:
            query = selectFromGraph + "WHERE { ?" + SUBJECT_NAME + " ?" + PREDICATE_NAME + " ?" + OBJECT_NAME + " }"

        data = submitQuery(url, query)

        return data
    else:
        return False

def listGraphs():
    host = session["host"]
    port = session["port"]
    database = session["database"]
    valid = session["valid"]

    if valid:
        url = f"http://{host}:{port}/{database}/sparql"
        
        query = "SELECT DISTINCT ?g WHERE { GRAPH ?g { ?s ?p ?o } }"

        data = submitQuery(url, query)

        return data
    else:
        return False

def submitQuery(url, query, format="json"):
    payload = {
        "query": query,
        "format": format
    }

    try:
        resp = post(url, data=payload)
        return resp.content.decode('utf-8')
    except:
        return False

def generatePrefixes(*suppliedPrefixes):
    prefixStrings = []

    for suppliedPrefix in suppliedPrefixes:
        if suppliedPrefix in prefix.prefixes:
            prefixStrings.append(f"PREFIX {suppliedPrefix}: <{prefix.prefixes[suppliedPrefix]}>")

    return " ".join(prefixStrings)