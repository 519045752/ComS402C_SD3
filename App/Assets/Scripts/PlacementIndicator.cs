using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.XR.ARFoundation;
using UnityEngine.XR.ARSubsystems;

public class PlacementIndicator : MonoBehaviour
{

    private ARRaycastManager arRayManager;
    private GameObject visual;

    // Start is called before the first frame update
    void Start()
    {
        // get components
        arRayManager = FindObjectOfType<ARRaycastManager>(); // what if duplicate raycastmanagers / undefined?
        visual = transform.GetChild(0).gameObject;

        // hide placement visual until plane/placement area is found
        visual.SetActive(false);
    }

    // Update is called once per frame
    void Update()
    {
        // shoot raycast from center of the screen (Using ar raycast manager)
        List<ARRaycastHit> hits = new List<ARRaycastHit>();
        arRayManager.Raycast(new Vector2(Screen.width / 2, Screen.height / 2) /*Center of screen */, hits, TrackableType.Planes);

        // if hit AR plane, update position & rotation to match world
        if (hits.Count > 0)
        {
            // is set to first object. Need to find which object belongs to which graphic if displaying more than 1 graphic
            transform.position = hits[0].pose.position;
            transform.rotation = hits[0].pose.rotation;

            if (!visual.activeInHierarchy) visual.SetActive(true);
        }

    }
}
