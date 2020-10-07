using UnityEngine;
using System.Collections;
using UnityEngine.UI;

public class errorPopUp : MonoBehaviour
{
    public Image PopUp;
    string error;
    // Use this for initialization
    void Start()
    {

    }

    // Update is called once per frame
    void Update()
    {

    }

    void OnEnable()
    {
        Application.logMessageReceived += HandleLog;
    }

    void OnDisable()
    {
        Application.logMessageReceived -= HandleLog;
    }

    void HandleLog(string logString, string stackTrace, LogType type)
    {

        if (type == LogType.Error)
        {
            error = error + "\n" + logString;
            PopUp.gameObject.SetActive(true);
            PopUp.transform.GetChild(0).GetComponent<Text>().text = "Error";
            PopUp.transform.GetChild(1).GetComponent<Text>().text = error;
        }
    }

    public void Dismiss()
    {
        PopUp.gameObject.SetActive(false);
    }

}