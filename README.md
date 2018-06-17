# company-spring-rest

#### Get all companies
~~~~
curl -X GET http://34.230.90.186:8080/companies
~~~~

#### Create company (e.g.)
~~~~
curl -X POST http://34.230.90.186:8080/company -H 'Content-Type: application/json' -d '{"name":"company1", "address":"address1", "city":"city", "country":"be", "email":"some@one.com", "phoneNumber":"123456789", "owners":[{"name":"owner1"}]}'
~~~~

#### Get company (e.g. for company 1)
~~~~
curl -X GET http://34.230.90.186:8080/company/1
~~~~

#### Update company (e.g. for company 1)
~~~~
curl -X PUT http://34.230.90.186:8080/company/1 -H 'Content-Type: application/json' -d '{"name":"updated company1", "address":"address1", "city":"city", "country":"be", "email":"some@one.com", "phoneNumber":"123456789", "owners":[{"name":"owner1"}]}'
~~~~

#### Add owners (e.g. for company 1) 
~~~~
curl -X PUT http://34.230.90.186:8080/company/1/owners -H 'Content-Type: application/json' -d '[{"name":"owner2"}]'
~~~~
