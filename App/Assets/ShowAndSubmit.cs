using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

/**
 * Canvas --> Input Field
 * This class handles the input field, which gives the user an interface from which to enter the text description. On Submit, spawns text at touch
 * location, with input text
 */
public class ShowAndSubmit : MonoBehaviour
{
    public TMP_Text objectToSpawn; // same reference as in ObjectSpawner.cs
    public PlacementIndicator placementIndicator;
    public CanvasGroup canvasGroup;
    public TMP_InputField Input_Tex;

    public Game data; 
    public GameObject save; // holds data for loaded scene


    // Start is called before the first frame update
    void Start()
    {
        Input_Tex.onSubmit.AddListener(Submit);
    }

    // Update is called once per frame
    void Update()
    {
        if (Input.touchCount > 0 && Input.touches[0].phase == TouchPhase.Began && !canvasGroup.blocksRaycasts)
        {
            Show();
            //GameObject obj = Instantiate(objectToSpawn, placementIndicator.transform.position, placementIndicator.transform.rotation);
        }
    }

    void Submit(string msg)
    {
        canvasGroup.alpha = 0f; //this makes everything transparent
        canvasGroup.blocksRaycasts = false; //this prevents the UI element to receive input events
        objectToSpawn.text = msg;
        TMP_Text obj = Instantiate(objectToSpawn, placementIndicator.transform.position, placementIndicator.transform.rotation);
        Debug.Log("Running Submit");
        data.CreateObject(0, obj);
        //data.AddObject(obj);
    }

    void Show()
    {
        Input_Tex.text = "";
        canvasGroup.alpha = 1f;
        canvasGroup.blocksRaycasts = true;
    }
}
