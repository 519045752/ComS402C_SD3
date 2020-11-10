using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.SceneManagement;

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
    /*Attempts Login using user info, then saves successful attempt to global*/
    IEnumerator Login()
    {
        var txt = loginResult.GetComponent<TextMeshProUGUI>();
        yield return StartCoroutine(Upload());

        Credentials user = JsonUtility.FromJson<Credentials>(request.downloadHandler.text);

        switch (user.code)
        {
            case 200:
                txt.text = "Login Sucessful!";
                User.userData = user.data;
                SceneManager.LoadScene("HouseSelection");

                break;
            case 502:
                txt.text = "Username/Password Incorrect";
                break;
            default:
                txt.text = "Error: Code #" + request.responseCode;
                break;

        }
    }
    /*sends Get request to constructed URL*/
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
    /*Constructs URL for GET request. Would've preffered making a POST request, but this works */
    private string CreateURL(string username, string password)
    {
        string urlText = login_url + "?password=" + password + "&username=" + username;
        Debug.Log(urlText);
        return urlText;
    }

    public void Register()
    {
        SceneManager.LoadScene("Register");
    }

}

static public class User //global variable to store user info
{
    public static UserData userData;
}

