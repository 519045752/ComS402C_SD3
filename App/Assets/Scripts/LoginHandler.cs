using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;

public class LoginHandler : MonoBehaviour
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

    private static string login_url = "coms-402-sd-8.cs.iastate.edu:8080/user/login";

    // Start is called before the first frame update
    void Start()
    {
        ufield = GameObject.Find("Username/Text Area/Text");
        pfield = GameObject.Find("Password/Text Area/Text");
    }

    public void LoginAttempt()
    {
        username = ufield.GetComponent<TextMeshProUGUI>().text;
        password = pfield.GetComponent<TextMeshProUGUI>().text;

        StartCoroutine(Login());

    }
    IEnumerator Login()
    {
        var txt = loginResult.GetComponent<TextMeshProUGUI>();
        yield return StartCoroutine(Upload());

        Credentials user = JsonUtility.FromJson<Credentials>(request.downloadHandler.text);

        switch (user.code)
        {
            case 200:
                txt.text = "Login Sucessful!";
                break;
            case 502:
                txt.text = "Username/Password Incorrect";
                break;
            default:
                txt.text = "Error: Code #" + request.responseCode;
                break;

        }
    }
    IEnumerator Upload()
    { 
        request = UnityWebRequest.Get(CreateURL(username,password));

        yield return request.SendWebRequest();

        if (request.isNetworkError || request.isHttpError)
        {
            print("Error downloading: " + request.error);
        }
        else
        {
            Debug.Log("POST complete!");
            Debug.Log(request.downloadHandler.text);
        }
    }
    private string CreateURL(string username, string password)
    {
        string urlText = login_url + "?password=" + password + "&username=" + username;
        Debug.Log(urlText);
        return urlText;
    }
}
