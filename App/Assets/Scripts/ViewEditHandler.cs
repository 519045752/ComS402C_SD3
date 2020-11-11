using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class ViewEditHandler : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {
        
    }

    public void LoadEdit()
    {
        SceneManager.LoadScene("HouseSelectionEdit");
        User.mode = PersistentCloudAnchorsController.ApplicationMode.Hosting; 
    }

    public void LoadView()
    {
        SceneManager.LoadScene("HouseSelectionView");
        User.mode = PersistentCloudAnchorsController.ApplicationMode.Resolving;
    }

}
