[QueryGroup="SymptomObservation"] @collection [[
[QueryItem="SymptomMonitor"]
PREFIX : <http://www.knoesis.org/kheath#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>


SELECT ?sm
WHERE { ?sm a :SymptomsMonitor }

[QueryItem="SymptomsValues"]
PREFIX : <http://www.knoesis.org/kheath#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>

SELECT ?s ?p ?v ?q
WHERE
{ ?s ssn:observationResult ?p .
  ?p ssn:hasValue ?v .
  ?v :hasQualitativeValue ?q }

[QueryItem="SymptomsObservation"]
PREFIX : <http://www.knoesis.org/kheath#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>


SELECT distinct ?obs
WHERE { ?obs a :SymptomObservation }

[QueryItem="SymptomMonitorOutput"]
PREFIX : <http://www.knoesis.org/kheath#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>


SELECT ?ores
WHERE { ?ores a :SymptomsMonitorOutput }

[QueryItem="ObservationOutput"]
PREFIX : <http://www.knoesis.org/kheath#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>


SELECT ?res ?val ?qv
WHERE { ?res ssn:hasValue ?val .
   	  ?val :hasQualitativeValue ?qv}

[QueryItem="Materialization"]

]]
