using System.Collections.Generic;
using System.IO;
using TMPro;
using UnityEngine;

public class Game : PersistableObject {

	public PersistableObject prefab;
    public PersistableObject prefabText;
	public PersistentStorage storage;

	List<PersistableObject> objects;
    string savePath;

	void Awake () {
		objects = new List<PersistableObject>();
        savePath = Path.Combine(Application.persistentDataPath, "savePath");
        Debug.Log("Load from: " + savePath);
        using (
            var reader = new BinaryReader(File.Open(savePath, FileMode.Open)))
        {
            Load(new GameDataReader(reader));
        }
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

	public void CreateObject (int type, TMP_Text newO) {

        PersistableObject o;

        // Switch case to handle different objects in future
        switch (type)
        {
            case 0: // add text
                o = Instantiate(prefabText);
                o.transform.localPosition = newO.transform.localPosition;
                o.transform.localRotation = newO.transform.localRotation;
                o.transform.localScale = newO.transform.localScale;
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

	public override void Save (GameDataWriter writer) {
		writer.Write(objects.Count);
		for (int i = 0; i < objects.Count; i++) {
            Debug.Log("Saved object:" + i);
            objects[i].Save(writer);
		}
	}

	public override void Load (GameDataReader reader) {
        // get world position
        //GetComponent<Camera>().getWorldPosition();

        int count = reader.ReadInt();
        Debug.Log("Count is: " + count);
		for (int i = 0; i < count; i++) {
            // need to handle differenct objects
            Debug.Log("Load object:" + i);
            PersistableObject o = Instantiate(prefabText);
			o.Load(reader);
			objects.Add(o);
		}
	}
}