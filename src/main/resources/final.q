[QueryGroup="Queries"] @collection [[
[QueryItem="Symptom"]
PREFIX time: <http://www.w3.org/2006/time#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX asthma: <http://www.knoesis.org/khealth/asthma#>
PREFIX : <http://www.knoesis.org/khealth#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>


SELECT ?obs ?p ?qv
WHERE { 
?obs ssn:featureOfInterest ?p ;
ssn:observationResultTime ?instant ; ssn:observationResult ?res ; ssn:observedProperty ?symptom .
?instant time:xsdDateTime ?time .
?symptom a asthma:Symptom .
?res ssn:hasValue ?val .
?val :hasObservationValue ?qv
}

[QueryItem="Sleep"]
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX asthma: <http://www.knoesis.org/khealth/asthma#>
PREFIX : <http://www.knoesis.org/khealth#>
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

[QueryItem="Drug"]
PREFIX time: <http://www.w3.org/2006/time#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX asthma: <http://www.knoesis.org/khealth/asthma#>
PREFIX : <http://www.knoesis.org/khealth#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>


SELECT ?obs ?p ?qv
WHERE { 
?obs ssn:featureOfInterest ?p ;
ssn:observationResultTime ?instant ; ssn:observationResult ?res ; ssn:observedProperty ?symptom .
?instant time:xsdDateTime ?time .
?symptom a asthma:Symptom .
?res ssn:hasValue ?val .
?val :hasObservationValue ?qv
}

[QueryItem="ControlLevel"]
PREFIX : <http://www.knoesis.org/khealth#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>
PREFIX asthma: <http://www.knoesis.org/khealth/asthma#>
PREFIX fn: <http://www.w3.org/2005/xpath-functions>

SELECT ?p ?c ?t
WHERE{
?p a :Patient ; asthma:hasControlLevel  ?c .
?c ?p ?t
}

[QueryItem="AQI"]
PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/weather/WeatherOntology_1.03.owl#>
PREFIX : <http://www.knoesis.org/khealth#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX time: <http://www.w3.org/2006/time#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>
PREFIX asthma: <http://www.knoesis.org/khealth/asthma#>


SELECT *
WHERE {
?obs a :AQIObservation ; ssn:observationResult ?res ; ssn:observationResultTime ?instant .
?res ssn:hasValue ?val . 
?instant time:xsdDateTime ?time .

?val :hasObservationValue ?qv .

}

[QueryItem="Temperature"]
PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/weather/WeatherOntology_1.03.owl#>
PREFIX : <http://www.knoesis.org/khealth#>
PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
PREFIX time: <http://www.w3.org/2006/time#>
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#>
PREFIX asthma: <http://www.knoesis.org/khealth/asthma#>


SELECT *
WHERE {
?obs a :OutdoorTemperatureObservation ; ssn:observationResult  ?res ; ssn:observationResultTime ?instant .
?instant time:xsdDateTime ?time .
?res ssn:hasValue ?val . 

FILTER (?time >= "2014-08-22T00:00:00"^^xsd:dateTime) 
FILTER (?time < "2014-10-27T00:00:00"^^xsd:dateTime) 
}
]]
[QueryItem="eclipse"]
PREFIX wea: <https://www.auto.tuwien.ac.at/downloads/thinkhome/ontology/WeatherOntology.owl#> 
PREFIX : <http://www.knoesis.org/khealth#> PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> 
PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> PREFIX time: <http://www.w3.org/2006/time#> 
PREFIX ssn: <http://purl.oclc.org/NET/ssnx/ssn#> PREFIX asthma: <http://www.knoesis.org/khealth/asthma#> 
SELECT ?time  ?qv
WHERE {  + "?val :hasObservationValue ?qv . 
?instant time:xsdDateTime ?time .  }
