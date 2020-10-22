using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.Networking;
using UnityEngine.SceneManagement;


public class SettingHandler : MonoBehaviour
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
    private GameObject result;
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

    private static string setEmail_url = "coms-402-sd-8.cs.iastate.edu:8080/user/setEmail";

    public void ChangeEmail()
    {
        var txt = result.GetComponent<TextMeshProUGUI>();
        txt.text = "Saved";
        return;
        //print(ufield.GetComponent<TextMeshProUGUI>().text);
        password = pfield.GetComponent<TextMeshProUGUI>().text;

        username = ufield.GetComponent<TextMeshProUGUI>().text;
        
        email = efield.GetComponent<TextMeshProUGUI>().text;
        

        form = new WWWForm();
        form.AddField("username", username);
        form.AddField("password", password);
        form.AddField("email", email);



        StartCoroutine(RegisterEmail());

    }
    IEnumerator RegisterEmail()
    {
        var txt = result.GetComponent<TextMeshProUGUI>();
        yield return StartCoroutine(UploadEmail());

        Credentials email = JsonUtility.FromJson<Credentials>(request.downloadHandler.text);

        switch (email.code)
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
    IEnumerator UploadEmail()
    {
        request = UnityWebRequest.Post(setEmail_url, form);

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

    public void SetEmail() => ChangeEmail();
}



