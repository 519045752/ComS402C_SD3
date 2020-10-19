using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using TMPro;

public class ButtonManager : MonoBehaviour
{
    public GameObject txt;
    // Start is called before the first frame update
    void Start()
    {
        txt = GameObject.Find("App Title");
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public void ChangeScene()
    {
        txt.GetComponent<TextMeshProUGUI>().text = "Loading Camera...";
        SceneManager.LoadScene("AR_Edit");
    }
}
