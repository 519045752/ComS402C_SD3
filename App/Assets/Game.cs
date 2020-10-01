using System.Collections.Generic;
using UnityEngine;

public class Game : PersistableObject {

	public PersistableObject prefab;

	public KeyCode createKey = KeyCode.C;
	public KeyCode newGameKey = KeyCode.N;
	public KeyCode saveKey = KeyCode.S;
	public KeyCode loadKey = KeyCode.L;

	public PersistentStorage storage;

	List<PersistableObject> objects;

	void Awake () {
		objects = new List<PersistableObject>();
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

	void CreateObject (int type) {
        // Switch case to handle different objects in future
        switch (type)
        {
            case 0: // add text
                PersistableObject o = Instantiate(prefab);
                objects.Add(o);
                break;
        }

	}

	public override void Save (GameDataWriter writer) {
		writer.Write(objects.Count);
		for (int i = 0; i < objects.Count; i++) {
			objects[i].Save(writer);
		}
	}

	public override void Load (GameDataReader reader) {
		int count = reader.ReadInt();
		for (int i = 0; i < count; i++) {
			PersistableObject o = Instantiate(prefab);
			o.Load(reader);
			objects.Add(o);
		}
	}
}