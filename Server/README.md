# Backend Server of the Coms402c AR_project 

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)
**Server Address: coms-402-sd-8.cs.iastate.edu:8080**
The server is based on Spring and mySQL.
It will be deployed to the university server after passing the first stage test.
  - Basic account server (Login, GetData, Register)
  - Magic

# Documents
  - API docs incoming!
# Files
- Ar_backend.postman_collection.json:
    postman Testing File
- db_init.sql
    Mysql initialization query
- ./sound
    Server prompt sounds Storage
### API
| API |Status|explanation|Param.
| ------ | ------ | ------ | ------ |
| /greeting |✖|return a random quote|
| /user/all |✔|return info of all user|
| /user/add |✔|add a new user directly|
| /user/register |✔|register a user|<ins>username, password, category</ins>
| /user/login |✔|login|username, password
| /user/getUserByID |✔|get user info by uid|uid
| /user/checkUsernameNotUsed |✔|check if the username is NOT used|username
| /user/logout |...|
| /user/setAvator |...|
| /user/setEmail |...|
| /user/setPhone |...|

### Status Code
These are the status code returned by the server.
| Code |Explanation|
| ------ | ------ |
|200|Request is done successfully.|
|500|Failed to process the request, reason is unknown.|
|501|Can't find the username in database.|
|502|Either username doesn't exist or the username and password mismatch.|
|520|Provided username is already used.|
|521|Provided wrong info to register the account.|
