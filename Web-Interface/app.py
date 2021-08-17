from os import urandom
from flask import Flask, render_template, request, session, redirect
from .util.queries import *
from .util.triples import *
from .include.prefix import prefixes
from re import match

app = Flask(__name__)
app.config["SECRET_KEY"] = urandom(16)
app.config["SESSION_TYPE"] = "memcached"

def get_session_settings():
	settings = {}
	varNames = ["host", "port", "database", "valid"]

	for var in varNames:
		if var in session:
			settings[var] = session[var]

	return settings

def set_session_settings(conn_settings):
	session["host"] = conn_settings["host"]
	session["port"] = conn_settings["port"]
	session["database"] = conn_settings["database"]
	session["valid"] = conn_settings["valid"]

@app.context_processor
def inject_connection_details():
	return dict(session=get_session_settings())


@app.route("/", methods=["GET", "POST"])
def index():
	if request.method == "POST":
		host = request.form.get("host")
		port = request.form.get("port")
		database = request.form.get("database")
		location = request.form.get("location")
		
		conn_settings = {
			"host": host,
			"port": port,
			"database": database,
			"valid": 0
		}

		missing = None
		
		if host == None or host == '':
			missing = "host"
		elif port == None or port == '':
			missing = "port"
		elif database == None or database == '':
			missing = "database"
		else:
			try:
				port_int = int(port)
				if not (port_int < 65555 and port_int > 0):
					missing = "port"
			except:
				missing = "port"
				
			if match(r'[-a-zA-Z0-9@:%._\+~#=]{1,256}[\.]*[a-zA-Z0-9()]{1,6}\b([-a-zA-Z0-9()@:%_\+.~#?&//=]*)', host):
				conn_settings["valid"] = 1

		set_session_settings(conn_settings)
		if missing != None:
			return render_template(
				"loading.html",
				missing=missing
			)
		else:
			return redirect(location, 302)
	
	return render_template(
		"loading.html",
		settings=get_session_settings()
		)

@app.route("/listAll", methods=["GET"])
def listAll():
	graph=request.args.get("graph")

	if graph == None:
		graphs=parseResults(listGraphs())
		return render_template(
			"listGraphs.html",
			graphs=graphs,
			title_suffix="Graphs"
		)
	else:
		subjects=parseAll(listQuery(graph))
		return render_template(
			"list.html",
			graph=graph,
			subjects=subjects,
			title_suffix="All"
		)

@app.route("/listSubject", methods=["GET"])
def listSubject():
	graph=request.args.get("graph")
	subject=request.args.get("subject")
	subjects=parseResults(listQuery(graph,subject=subject),subject)
	return render_template(
		"list.html",
		graph=graph,
		subjects=subjects,
		queried=subject,
		title_suffix="Subjects",
		queryOn="subject"
	)
	
@app.route("/listPredicate", methods=["GET"])
def listPredicate():
	graph=request.args.get("graph")
	predicate=request.args.get("predicate")
	subjects=parseResults(listQuery(graph,predicate=predicate),predicate)
	return render_template(
		"list.html",
		graph=graph,
		subjects=subjects,
		queried=predicate,
		title_suffix="Predicates",
		queryOn="predicate"
	)

@app.route("/listObject", methods=["GET"])
def listObject():
	graph=request.args.get("graph")
	object=request.args.get("object")
	subjects=parseResults(listQuery(graph,object=object),object)
	return render_template(
		"list.html",
		graph=graph,
		subjects=subjects,
		queried=object,
		title_suffix="Objects",
		queryOn="object"
	)

@app.route("/search", methods=["GET"])
def searchDB():
	graph=request.args.get("graph")
	searchString=request.args.get("searchString")
	
	value = ""
	vars = searchString.split()
	for i, v in enumerate(vars):
		if not v.startswith("?"):
			value=v
			if i == 2:
				try:
					pre, _, suf = value.partition(":")
					if pre in prefixes:
						searchString = searchString.replace(value, f"\"{prefixes[pre]}{suf}\"")
					else:
						raise KeyboardInterrupt
				except:
					searchString = searchString.replace(value,f"\"{value}\"")
	
	subjects=parseResults(listQuery(graph,searchString=searchString),value)
	return render_template(
		"list.html",
		graph=graph,
		subjects=subjects,
		queried=value,
		searchString=searchString,
		title_suffix="Search Results",
		queryOn="Search"
	)