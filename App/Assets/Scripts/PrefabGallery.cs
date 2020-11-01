using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PrefabGallery : MonoBehaviour
{



    //list that store all the prefabs from path "Resources/Prefab"
    private Object[] prefabList;

    //prefab selected from dropdown to spawn
    GameObject prefabToSpawn;

    //dropdown menu for prefabs
    public Dropdown prefabDropdown;

    //confirm button to spawn prefab
    public Button confirmButton;




    void Start()
    {
        populateDropdown();
    }

    /// <summary>
    /// Populate the dropdown menu with every available prefabs
    /// </summary>
    public void populateDropdown()
    {
        //get all the prefab from the folder "Asset/Resources/Prefab"
        prefabList = Resources.LoadAll("Prefab", typeof(GameObject));



        prefabDropdown.options.Clear();

        //showing the name of prefab on the dropdown menu
        foreach (var p in prefabList)
        {
            prefabDropdown.options.Add(new Dropdown.OptionData() { text = p.name });

        }
    }
    /// <summary>
    /// Handles Button onclick event
    /// </summary>
    public void ButtonOnclick()
    {

        confirmButton.onClick.AddListener(ConfirmButtonClicked);


    }
    public void dropdownValueChange(int index)
    {
        prefabToSpawn = (GameObject)prefabList[index];

        ButtonOnclick();

    }
    /// <summary>
    /// Handles event when user select an item from dropdown menu
    /// </summary>
    /// <param name="dropdown"></param>
    public void getDropdownItemSelected(int index)
    {
        //load the prefab from "Prefab" folder
        var temp = (Resources.Load<GameObject>("Prefab/" + prefabList[index].name));
        prefabToSpawn = temp as GameObject;



    }
    /// <summary>
    /// Spawn prefab to scene
    /// </summary>
    void ConfirmButtonClicked()
    {
        //disable button and dropdown menu after confirm button is pressed;
        confirmButton.enabled = false;
        prefabDropdown.enabled = false;

    }



}




