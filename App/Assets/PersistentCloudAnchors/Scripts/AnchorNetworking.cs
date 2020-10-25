using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.SceneManagement;

public class AnchorNetworking : MonoBehaviour
{
    [SerializeField]
    private GameObject ufield;
    [SerializeField]
    private GameObject pfield;
    [SerializeField]
    private GameObject loginResult;
    [SerializeField]
    private string username;
    [SerializeField]
    private string password;

    public UnityWebRequest request;

    private static string baseurl = "coms-402-sd-8.cs.iastate.edu:8080/arObject/";

    // Start is called before the first frame update
    void Start()
    {
    }

    public void getCloudIds()
    {
        request = UnityWebRequest.Get(baseurl + "all");
        Debug.Log(request.downloadHandler.text);
    }

    // example POST: http://coms-402-sd-8.cs.iastate.edu:8080/arObject/add?cloudid=testid&description=this%20is%20some%20text&hid=1&type=0
    public void AddCloudID(string cloudid, string desc, int hid, int type)
    {
        var url = baseurl + "add?cloudid=" + cloudid + "&description=" + desc + "&hid= " + hid + "&type=" + type;
        Debug.Log("Posting arobject as: " + url);
        UnityWebRequest.Post(url, "");
    }
}
