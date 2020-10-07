using System.IO;
using UnityEngine;

public class PersistentStorage : MonoBehaviour {

	public string savePath;

	void Awake () {
		savePath = Path.Combine(Application.persistentDataPath, "savePath");
	}

	public void Save (PersistableObject o) {
		using (
			var writer = new BinaryWriter(File.Open(savePath, FileMode.Create))
		) {
			o.Save(new GameDataWriter(writer));
            Debug.Log("Saved to: " + savePath);
		}
	}

	public void Load (PersistableObject o) {
		using (
			var reader = new BinaryReader(File.Open(savePath, FileMode.Open))
		) {
            o.Load(new GameDataReader(reader));
		}
	}
}
