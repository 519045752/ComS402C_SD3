# Backend Server of the Coms402c AR_project 

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

The server is based on Spring and mySQL.
It will be deployed to the university server after passing the first stage test.
  - Basic account server (Login, GetData, Register)
  - Magic

# Documents
  - API docs incoming!
# Files
  - Ar_backend.postman_collection.json:
        Postman Testing File
  - db_init.sql
        Mysql initialization query
### API

Dillinger is currently extended with the following plugins. Instructions on how to use them in your own application are linked below.

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



