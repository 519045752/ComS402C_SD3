using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;
using SimpleJSON;
public class HouseSelectionHandler : MonoBehaviour
{
    public UnityWebRequest request, requestHouses;

    public Hid hids;
    public List<HouseResponse> houses;
    public DropDown drop;

    private static string house_url = "coms-402-sd-8.cs.iastate.edu:8080/house/";
    private static string ownedHouses_url = "coms-402-sd-8.cs.iastate.edu:8080/user/getOwnedHouse?uid=";

    // Start is called before the first frame update
    void Start()
    {
        Debug.LogFormat("User is: {0}", User.userData.username);

        StartCoroutine(FindHouses());


    }


    IEnumerator FindHouses()
    {
        request = UnityWebRequest.Get(ownedHouses_url + User.userData.uid); //grab houses owned by user

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

        hids = JsonUtility.FromJson<Hid>(request.downloadHandler.text); //easiest solution, even though the very simple class looks dumb

        yield return StartCoroutine(HouseInfo());

    }

    IEnumerator HouseInfo()
    {
        foreach(int hid in hids.data){
            requestHouses = UnityWebRequest.Get(house_url + hid); //grab relevant house information

            yield return requestHouses.SendWebRequest();

            if (requestHouses.isNetworkError || requestHouses.isHttpError)

            {
                print("Error downloading: " + requestHouses.error);
            }
            else
            {
                Debug.Log("POST complete!");
                Debug.Log(requestHouses.downloadHandler.text);
            }

            houses.Add(JsonUtility.FromJson<HouseResponse>(requestHouses.downloadHandler.text)); //again, dumb but works
        }

        drop = gameObject.GetComponent<DropDown>(); //invoking dropdown population now
        drop.PopulateDropdown();
    }
}

[Serializable]
public class Hid
{
    public int[] data;
}

[Serializable]
public class HouseResponse
{
    public House data;
}

[Serializable]
public class House
{
    public string address;
    public int hid;
    public string info;
    public string price;
}
