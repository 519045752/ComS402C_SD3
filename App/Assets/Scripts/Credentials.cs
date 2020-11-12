using System;
using System.Collections;
using System.Collections.Generic;
using System.Security.Cryptography;
using UnityEngine;

using SimpleJSON;

[Serializable]
public class Credentials 
{
    public long code;
    public string msg;
    public UserData data;
}

[Serializable]
public class UserData
{
    public long uid;
    public string username;
    public string password;
    public string category;
    public string email;
    public string phone;
}
