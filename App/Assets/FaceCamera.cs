using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FaceCamera : MonoBehaviour
{
    public Camera cam;
    // Start is called before the first frame update
    void Start()
    {
        cam = GameObject.Find("Camera").GetComponent<Camera>();
        Debug.Log("Finding Camera");
        transform.localPosition = new Vector3(0, 1, 0);
    }

    // Update is called once per frame
    void Update()
    {
        Debug.Log("Camera position:" + cam.transform);
        transform.rotation = Quaternion.LookRotation(transform.position - cam.transform.position);

        Debug.Log("Anchor position:" + this.transform);
    }
}
