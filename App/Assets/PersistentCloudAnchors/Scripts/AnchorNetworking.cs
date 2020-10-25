using System.Collections;
using UnityEngine;
using UnityEngine.Networking;
using System.Collections.Generic;


public class AnchorNetworking : MonoBehaviour
{
    public UnityWebRequest request;

    private static string baseurl = "coms-402-sd-8.cs.iastate.edu:8080/arObject/";

    // Start is called before the first frame update
    void Start()
    {
    }

    public IEnumerator AddCloudID(string cloudid, string desc, int hid, int type)
    {

        Debug.Log("Begin AddCloudID");
        WWWForm form = new WWWForm();
        form.AddField("cloudid", cloudid);
        form.AddField("description", desc);
        form.AddField("hid", hid);
        form.AddField("type", type);

        // from https://docs.unity3d.com/ScriptReference/Networking.UnityWebRequest.Post.html
        using (UnityWebRequest www = UnityWebRequest.Post(baseurl + "add", form))
        {
            yield return www.SendWebRequest();

            if (www.isNetworkError || www.isHttpError)
            {
                Debug.Log(www.error);
            }
            else
            {
                Debug.Log("ARObject: Form upload complete!");
            }
        }
    }

    public IEnumerator getCloudId(List<string> cloudids)
    {

        Debug.Log("Begin getCloudId");

        // from https://docs.unity3d.com/ScriptReference/Networking.UnityWebRequest.Post.html
        using (UnityWebRequest www = UnityWebRequest.Get(baseurl + "all"))
        {
            yield return www.SendWebRequest();

            if (www.isNetworkError || www.isHttpError)
            {
                Debug.Log(www.error);
            }
            else
            {
                Debug.Log("ARObject: result of get was --> \n " + www.downloadHandler.text);
                for (int i = 0; i < 1; i++)
                {
                    
                }
            }
        }
    }
}
