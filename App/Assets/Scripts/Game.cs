using System.Collections.Generic;
using System.IO;
using TMPro;
using UnityEngine;

public class Game : MonoBehaviour {

	public GameObject TextObj;
	public PersistentStorage storage;

	List<PersistableObject> objects;
    string savePath;

	void Awake () {
		objects = new List<PersistableObject>();
        savePath = Path.Combine(Application.persistentDataPath, "savePath");
        //Debug.Log("Load from: " + savePath);
        //using (
        //    var reader = new BinaryReader(File.Open(savePath, FileMode.Open)))
        //{
        //    Load(new GameDataReader(reader));
        //}
    }

	void Update () {
	}

    // this can be used to clear area
	void ClearMap () {
		for (int i = 0; i < objects.Count; i++) {
			Destroy(objects[i].gameObject);
		}
		objects.Clear();
	}

	public void CreateObject (int type, GameObject newO, string cloudid) {

        PersistableObject o;

        // Switch case to handle different objects in future
        switch (type)
        {
            case 0: // add text
                o = new PersistableObject(cloudid, type, newO);
                o.text = newO.transform.GetChild(0).GetComponent<TMP_Text>().text;
                objects.Add(o);
                break;
        }

        using (
            var writer = new BinaryWriter(File.Open(savePath, FileMode.Create))
)
        {
            Save(new GameDataWriter(writer));
            Debug.Log("Saved to: " + savePath);
        }

    }

	public void Save (GameDataWriter writer) {
		writer.Write(objects.Count);
		for (int i = 0; i < objects.Count; i++) {
            Debug.Log("Saved object:" + i);
            objects[i].Save(writer);
		}
	}

	public void Load (GameDataReader reader) {
        // get world position
        //GetComponent<Camera>().getWorldPosition();

        int count = reader.ReadInt();
        Debug.Log("Count is: " + count);
		for (int i = 0; i < count; i++) {
            // need to handle differenct objects
            Debug.Log("Load object:" + i);

            // todo - check for type to decide if text should be read or not (else we will get corrupt readings from file)
            PersistableObject o = new PersistableObject();
			o.Load(reader);
			objects.Add(o);


            // Add obj to map (need cloud anchors)
            TextObj.transform.localPosition = o.localPosition;
            TextObj.transform.localRotation = o.localRotation;
            TextObj.transform.localScale = o.localScale;

            TextObj.transform.GetChild(0).GetComponent<TMP_Text>().text = "I AM AT INDEX " + i;

            Instantiate(TextObj);


        }
	}
}