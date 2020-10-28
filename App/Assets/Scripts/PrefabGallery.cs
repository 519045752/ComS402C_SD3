using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PrefabGallery : MonoBehaviour
{
    //where to spawn the object. todo change to hitpose
    public GameObject spawnPoint;

    //list that store all the prefabs from path "Resources/Prefab"
    private Object[] prefabList;

    //prefab selected from dropdown to spawn
    GameObject prefabToSpawn;

    //dropdown menu for prefabs
    public Dropdown prefabDropdown;

    //confirm button to spawn prefab
    public Button confirmButton;

    //Confirm button pressed
    bool confirm = false;

    public PrefabGallery(Dropdown drp, Button btn, GameObject spwnPt)
    {
        prefabDropdown = drp;
        confirmButton = btn;
        spawnPoint = spwnPt;
    }

    void Start()
    {
        DropdownController();
    }
    /// <summary>
    ///  Main method to spawn prefab from dropdown menu and a confirm button
    /// </summary>
    public void DropdownController()
    {

        LoadDropdownMenu();
        ButtonOnclick();

    }
    /// <summary>
    /// Store prefabs from folder to dropdown menu. Handles event change of dropdown menu
    /// </summary>
    void LoadDropdownMenu()
    {
        //get all the prefab from the folder "Asset/Resources/Prefab"
        prefabList = Resources.LoadAll("Prefab", typeof(GameObject));

        Dropdown dropdown = prefabDropdown.GetComponent<Dropdown>();


        dropdown.options.Clear();

        //adding all the prefabs name into dropdown button
        foreach (var p in prefabList)
        {
            dropdown.options.Add(new Dropdown.OptionData() { text = p.name });

        }
        prefabToSpawn = (GameObject)prefabList[0];
        dropdown.onValueChanged.AddListener(delegate { prefabToSpawn = getDropdownItemSelected(dropdown); });
        

    }
    /// <summary>
    /// Handles Button onclick event
    /// </summary>
    void ButtonOnclick()
    {

        
        confirmButton.onClick.AddListener(SpawnPrefabOnScene);
        //unable to spawn anymore stuff after click button once
        
    }
    /// <summary>
    /// Handles event when user select an item from dropdown menu
    /// </summary>
    /// <param name="dropdown"></param>
    public GameObject getDropdownItemSelected(Dropdown dropdown)
    {

        int index = dropdown.value;
        GameObject prefab = (GameObject)prefabList[index];

        var temp = (Resources.Load<GameObject>("Prefab/" + prefabToSpawn.name));
        prefab = temp as GameObject;
        return prefab;

    }
    /// <summary>
    /// Spawn prefab to scene
    /// </summary>
    public void SpawnPrefabOnScene()
    {

        confirm = true;
        //change this to hitpose
        Instantiate(prefabToSpawn,spawnPoint.transform);
        //disable button after spawn;
       confirmButton.enabled = false;
    }


}




