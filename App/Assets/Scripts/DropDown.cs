using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.UI;

public class DropDown : MonoBehaviour
{
    public HouseSelectionHandler hsh;
    public int val;
    public TMP_Dropdown dropdown;
    public List<string> options;

    // Start is called before the first frame update
    void Start()
    {
        hsh = gameObject.GetComponent<HouseSelectionHandler>();
    }

    public void PopulateDropdown()
    {
        foreach (HouseResponse house in hsh.houses)
        {
            Debug.Log(house.data + "\n" + house.data.address);
            options.Add(house.data.address);
        }
        dropdown.AddOptions(options);
    }

    public void HandleInput(int val)
    {
        this.val = val;
    }

    public void ConfirmHouse()
    {
        User.house = hsh.houses[val].data;  
    }
}
