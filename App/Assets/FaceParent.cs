using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class FaceParent : MonoBehaviour
{
    public Camera cam;
    // Start is called before the first frame update
    void Start()
    {
        cam = GameObject.Find("Camera").GetComponent<Camera>();
        Debug.Log("Finding Camera");
    }

    // Update is called once per frame
    void Update()
    {
        //transform.rotation = transform.parent.rotation;
        //transform.rotation = new Quaternion(transform.parent.rotation.x, transform.parent.rotation.y, 0, transform.parent.rotation.w);
        transform.localPosition = new Vector3(1, 1, 0);
        transform.rotation = Quaternion.LookRotation(transform.position - cam.transform.position);

        Gizmos.color = Color.red;
        Gizmos.DrawLine(transform.position, transform.parent.position);


    }
}
