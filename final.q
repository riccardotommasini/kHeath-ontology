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
PREFIX time: <http://www.w3.org/2006/time#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX asthma: <http://www.knoesis.org/khealth/asthma#>
PREFIX : <http://www.knoesis.org/kheath#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>


SELECT ?p ?symptom ?time ?val
WHERE { 
?obs ssn:featureOfInterest ?p ;
ssn:observationResultTime ?instant ; ssn:observationResult ?res ; ssn:observedProperty ?symptom .
?instant time:xsdDateTime ?time .
?symptom a asthma:Symptom .
?res ssn:hasValue ?val .
?val :hasObservationValue ?qv . 
}

[QueryItem="Materialization"]


[QueryItem="Sleep"]
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX asthma: <http://www.knoesis.org/khealth/asthma#>
PREFIX : <http://www.knoesis.org/kheath#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>


SELECT ?sleep ?time ?qv
WHERE { 
?obs a :SleepObservation ; ssn:observationResultTime ?time ; ssn:observedProperty ?sleep .
?sleep a asthma:SleepDisorder .
?res ssn:hasValue ?val .
?val :hasObservationValue ?qv . 
FILTER (?time > "2015-06-17T00:00:00"^^xsd:dateTime) 
FILTER ( ?qv = "true"^^xsd:boolean)
}
]]
