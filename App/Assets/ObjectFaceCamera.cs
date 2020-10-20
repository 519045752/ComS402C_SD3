using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Animations;

public class ObjectFaceCamera : MonoBehaviour
{
    GameObject[] ARObjects;
    // Start is called before the first frame update
    void Start()
    {
        GameObject.FindGameObjectsWithTag("ARObject");
    }

    // Update is called once per frame
    void Update()
    {
        foreach(GameObject obj in ARObjects)
        {
            
        }
    }
}
