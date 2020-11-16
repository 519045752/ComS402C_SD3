using System.Collections;
using System.Collections.Generic;
using TMPro;
using UnityEngine;
using UnityEngine.SceneManagement;

public class DropDownView : MonoBehaviour
{
    public HouseSelectionViewHandler hsh;
    public int val;
    public TMP_Dropdown dropdown;
    public List<string> options;

    // Start is called before the first frame update
    void Start()
    {
        hsh = gameObject.GetComponent<HouseSelectionViewHandler>();
    }

    public void PopulateDropdown()
    {
        foreach (House house in hsh.houses.data)
        {
            Debug.Log(house + "\n" + house.address);
            options.Add(house.address);
        }
        dropdown.AddOptions(options);
    }

    public void HandleInput(int val)
    {
        this.val = val;
    }

    public void ConfirmHouse()
    {
        User.house = hsh.houses.data[val];
        //Debug.Log(User.house.address + " " + User.house.hid);
        SceneManager.LoadScene("PersistentCloudAnchors");
    }
}
