using System.Collections;
using UnityEngine;
using UnityEngine.Networking;
using System.Collections.Generic;
using System.IO;
using SimpleJSON;

public class AnchorNetworking : MonoBehaviour
{
    public UnityWebRequest request;

    private string jsonString;

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

    public IEnumerator getCloudIds(List<string> cloudids)
    {

        Debug.Log("Begin getCloudIds");

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
                string jsonstring = www.downloadHandler.text;
                JSONNode data = JSON.Parse(jsonstring);

                Debug.Log("ARObject: Begin get--> \n " + data["data"]);



                foreach (JSONNode record in data["data"])
                {
                    Debug.Log("Added object with fields: cloudid: " + record["cloudid"].Value + "\n" +
                        "description: " + record["description"].Value + "\n" +
                        "hid: " + record["hid"].AsInt + "\n" +
                        "type: " + record["type"].AsInt + "\n" +
                        "oid: " + record["oid"].AsInt);
                    cloudids.Add(record["cloudid"].Value);
                }
                Debug.Log("Num of cloudids = " + cloudids.Count);
            }
        }
    }
}
