Build Application
-------------------------

cd car-rental/
mvn install


Start External Service
-------------------------
Run fg.car.external.service.ExternalService

With maven:

Configuring jmx:
export MAVEN_OPTS='-Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=9019 -Dcom.sun.management.jmxremote.local.only=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false'


cd mock-external-service/
mvn exec:exec

Start Car Rental Service
-------------------------
Run fg.car.rental.Application

With maven:
cd car-rental-service/
mvn exec:exec


Testing Web  Service
-------------------------

Query countries:
curl -X GET http://localhost:8080/services/countries

Query car list:
curl -X GET http://localhost:8080/services/cars

Query car details:
curl -X GET http://localhost:8080/services/cars/2

Book a car (This example run without error twice since two cars are available from this type):
curl -X POST http://localhost:8080/services/cars/book -H "Content-Type: application/json" -d '{"customerName":"John Smith","customerAddress":"Kobago utca 3","customerID":"AXC","carId":5,"usage":"DOMESTIC","from":1521126720000,"to":1521133920000}'

Book Lada for Russia (Accepts):
curl -X POST http://localhost:8080/services/cars/book -H "Content-Type: application/json" -d '{"customerName":"John Smith","customerAddress":"Kobago utca 3","customerID":"AXC","carId":8,"usage":"FOREIGN","from":1521126720000,"to":1521133920000,"countryCode":"RU"}'

Book Lada for Germany (Rejects, Lada cannot be used in Germany)
curl -X POST http://localhost:8080/services/cars/book -H "Content-Type: application/json" -d '{"customerName":"John Smith","customerAddress":"Kobago utca 3","customerID":"AXC","carId":8,"usage":"FOREIGN","from":1521126720000,"to":1521133920000,"countryCode":"DE"}'


Access to application
-------------------------

Open a browser
http://localhost:8080/app/index.html
