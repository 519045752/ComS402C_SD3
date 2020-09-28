# Backend Server of the Coms402c AR_project 

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

**Server Address: coms-402-sd-8.cs.iastate.edu:8080**

The server is based on Spring and mySQL.
It will be deployed to the university server after passing the first stage test.
  - Basic account server (Login, GetData, Register)
  - Magic

# Files
- Ar_backend.postman_collection.json:
    postman Testing File
- db_init.sql
    Mysql initialization query
- ./sound
    Server prompt sounds Storage
### API
**please read the API documents at /apis or simply visit the server directly**
<coms-402-sd-8.cs.iastate.edu:8080>

| API |Status|explanation|Param.
| ------ | :-: | ------ |:-: |
| /test |✔|greeting for the visitor|
| /user/<UID> |✔|check the public user info and greet|append uid directly, e.g.: /user/<UID>
| /user/all |✔|return info of all user|
| /user/add |✔|add a new user directly <run db_init.sql after using>|
| /user/register |✔|register a user|username, password, category
| /user/login |✔|login|username, password|
| /user/loginByUid |✔|login by uid|uid, password|
| /user/getUserByID |✔|get user info by uid|uid
|/findUidByUsername|✔|get uid by usernmae|usernmae
| /user/checkUsernameNotUsed |✔|check if the username is NOT used|username
| /user/logout |...|will come with session manager|
| /user/setAvator |...|debugging
| /user/setEmail |✔|set the email address| username, password, email|
| /user/setPhone |✔|set the phone number|username, password, phone|
| /user/setPassword |✔|reset password,require a uew passowrd|username, password, newPassword|
| /user/setUsername |✔|reset username, require a unused and new username|username, password, newUsername|

### Status Code
These are the status code returned by the server.

| Code |Explanation|
| ------ | ------ |
|200|Request is done successfully|
|500|Failed to process the request, reason is unknown|
|501|Can't find the username in database.|
|502|Either username doesn't exist or the username and password mismatch.|
|520|Provided username is already used.|
|521|Provided wrong info to register the account.|
|999|Something weird happened.|