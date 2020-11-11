using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Networking;

public class HouseSelectionViewHandler : MonoBehaviour
{
    public UnityWebRequest request, requestHouses;

    public Hid hids;
    public AllHouses houses;
    public DropDownView drop;

    private static string house_url = "coms-402-sd-8.cs.iastate.edu:8080/house/all";

    // Start is called before the first frame update
    void Start()
    {
        //Debug.LogFormat("User is: {0}", User.userData.username);

        StartCoroutine(FindHouses());


    }


    IEnumerator FindHouses()
    {
        request = UnityWebRequest.Get(house_url); //grab all houses

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

        houses = JsonUtility.FromJson <AllHouses>(request.downloadHandler.text); //easiest solution, even though the very simple class looks dumb

        yield return StartCoroutine(HouseInfo());

    }

    IEnumerator HouseInfo()
    {
        /*foreach (House house in houses)
        {
            houses.Add(JsonUtility.FromJson<HouseResponse>(requestHouses.downloadHandler.text)); //again, dumb but works
        } */

        drop = gameObject.GetComponent<DropDownView>(); //invoking dropdown population now
        drop.PopulateDropdown();
        yield return null;
    }
}

[Serializable]
public class AllHouses
{
    public House[] data;
}
