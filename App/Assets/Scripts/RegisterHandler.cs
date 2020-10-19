using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.SceneManagement;

public class RegisterHandler : MonoBehaviour
{
    [SerializeField]
    private GameObject ufield;
    [SerializeField]
    private GameObject pfield;
    [SerializeField]
    private GameObject efield;
    [SerializeField]
    private GameObject nfield;
    [SerializeField]
    private GameObject registerResult;
    [SerializeField]
    private string username;
    [SerializeField]
    private string password;
    [SerializeField]
    private string email;
    [SerializeField]
    private string phone;

    private WWWForm form;

    public UnityWebRequest request;

    private static string register_url = "coms-402-sd-8.cs.iastate.edu:8080/user/register";

    // Start is called before the first frame update
    void Start()
    {

    }

    public void RegisterAttempt(string userType)
    {
        username = ufield.GetComponent<TextMeshProUGUI>().text;
        password = pfield.GetComponent<TextMeshProUGUI>().text;
        email = efield.GetComponent<TextMeshProUGUI>().text;
        phone = nfield.GetComponent<TextMeshProUGUI>().text;

        form = new WWWForm();
        form.AddField("category", userType);
        form.AddField("username", username);
        form.AddField("password", password);
        form.AddField("email", email);
        form.AddField("phone", phone);


        StartCoroutine(Register());

    }
    IEnumerator Register()
    {
        var txt = registerResult.GetComponent<TextMeshProUGUI>();
        yield return StartCoroutine(Upload());

        Credentials user = JsonUtility.FromJson<Credentials>(request.downloadHandler.text);

        switch (user.code)
        {
            case 200:
                txt.text = "Register Sucessful!";
                break;
            case 520:
                txt.text = "Username already in use";
                break;
            default:
                txt.text = "Error: Code #" + request.responseCode;
                break;

        }
    }
    IEnumerator Upload()
    {
        request = UnityWebRequest.Post(register_url, form);

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

    public void RegisterTenant() => RegisterAttempt("tenant");

    public void RegisterLandlord() => RegisterAttempt("landlord");

}
