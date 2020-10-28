using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class PrefabGallery : MonoBehaviour
{
    //where to spawn the object. todo change to hitpose
    public Transform spawnPoint;

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

    public PrefabGallery(Dropdown drp, Button btn, Transform spwnPt)
    {
        prefabDropdown = drp;
        confirmButton = btn;
        spawnPoint = spwnPt;
    }
    /// <summary>
    ///  Main method to spawn prefab from dropdown menu and a confirm button
    /// </summary>
    void DropdownController()
    {

        LoadDropdownMenu();


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
        dropdown.onValueChanged.AddListener(delegate { DropdownItemSelected(dropdown); });
        ButtonOnclick();
    }
    /// <summary>
    /// Handles Button onclick event
    /// </summary>
    void ButtonOnclick()
    {
        Button btn = confirmButton.GetComponent<Button>();
        btn.onClick.AddListener(SpawnPrefabOnScene);

    }
    /// <summary>
    /// Handles event when user select an item from dropdown menu
    /// </summary>
    /// <param name="dropdown"></param>
    void DropdownItemSelected(Dropdown dropdown)
    {

        int index = dropdown.value;
        prefabToSpawn = (GameObject)prefabList[index];

        var temp = (Resources.Load<GameObject>("Prefab/" + prefabToSpawn.name));
        prefabToSpawn = temp as GameObject;

    }
    /// <summary>
    /// Spawn prefab to scene
    /// </summary>
    public void SpawnPrefabOnScene()
    {

        confirm = true;
        //change this to hitpose
        Instantiate(prefabToSpawn,spawnPoint);

    }


}




