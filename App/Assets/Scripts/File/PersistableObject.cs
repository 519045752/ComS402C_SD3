using UnityEngine;

[DisallowMultipleComponent]
public class PersistableObject : MonoBehaviour {

    public PersistableObject()
    {

    }

    public PersistableObject(int type, GameObject obj)
    {
        this.type = type;
        transform.localPosition = obj.transform.localPosition;
        transform.localRotation = obj.transform.localRotation;
        transform.localScale = obj.transform.localScale;
    }

    /// <summary>
    // Stored here if this object holds text info
    /// </summary>
    public string text;

    /// <summary>
    /// The classification of this type of object (text, door, image, etc)+
    /// </summary>
    public int type;

	public virtual void Save (GameDataWriter writer) {
		writer.Write(transform.localPosition);
		writer.Write(transform.localRotation);
		writer.Write(transform.localScale);
	}

	public virtual void Load (GameDataReader reader) {
		transform.localPosition = reader.ReadVector3();
		transform.localRotation = reader.ReadQuaternion();
		transform.localScale = reader.ReadVector3();
	}
}