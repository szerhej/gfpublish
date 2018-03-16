Description
-------------------------
Demo application to model a car rental service.
It does not have persistence layer. Stores all saved information in web service.
On UI list of cars are displayed. Clicking on any of them it opens the car details.
Activating the 'Book' button it changes the screen in edit mode.
Filling the required fields the booking can be saved.
One car type can be booked more times for the same period depending on how many cars are available from that type.
In case of any errors error message is displayed. Message does not have error code but it can be extended.


Build Application
-------------------------

cd car-rental/
mvn clean install


Start External Service
-------------------------
Run fg.car.external.service.ExternalService

With maven:

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
curl -X POST http://localhost:8080/services/cars/book -H "Content-Type: application/json" -d '{"customerName":"John Smith","customerAddress":"Kobago utca 3","customerID":"AXC","carId":5,"usage":"DOMESTIC","from":1529171700000,"to":1529430900000}'

Book Lada for Russia (Accepts):
curl -X POST http://localhost:8080/services/cars/book -H "Content-Type: application/json" -d '{"customerName":"John Smith","customerAddress":"Kobago utca 3","customerID":"AXC","carId":8,"usage":"FOREIGN","from":1529171700000,"to":1529430900000,"countries":["RU"]}'

Book Lada for Germany (Rejects, Lada cannot be used in Germany)
curl -X POST http://localhost:8080/services/cars/book -H "Content-Type: application/json" -d '{"customerName":"John Smith","customerAddress":"Kobago utca 3","customerID":"AXC","carId":8,"usage":"FOREIGN","from":1529171700000,"to":1529430900000,"countries":["RU","DE"]}'


Access to application
-------------------------

Open a browser
http://localhost:8080/app/index.html
