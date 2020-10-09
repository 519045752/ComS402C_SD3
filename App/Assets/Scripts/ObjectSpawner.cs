using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using TMPro;

/**
 * This class handles the input field, which gives the user an interface from which to enter the text description. On Submit, spawns text at touch
 * location, with input text
 */

public class ObjectSpawner : MonoBehaviour
{

    public TMP_Text objectToSpawn;
    private PlacementIndicator placementIndicator;

    // Start is called before the first frame update
    void Start()
    {
        placementIndicator = FindObjectOfType<PlacementIndicator>();
        
    }

    // Update is called once per frame
    void Update()
    {
    if (Input.touchCount > 0 && Input.touches[0].phase == TouchPhase.Began)
        {

            TMP_Text obj = Instantiate(objectToSpawn, placementIndicator.transform.position, placementIndicator.transform.rotation);
        }

    }
}
