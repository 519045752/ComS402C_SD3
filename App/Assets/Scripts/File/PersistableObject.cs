using UnityEngine;

[DisallowMultipleComponent]
public class PersistableObject : MonoBehaviour {

    /// <summary>
    // Stored here if this object holds text info
    /// </summary>
    public string text;

    /// <summary>
    /// The classification of this type of object (text, door, image, etc)+
    /// </summary>
    public int type;

    public Vector3 localPosition;
    public Quaternion localRotation;
    public Vector3 localScale;

    public PersistableObject()
    {

    }

    public PersistableObject(int type, GameObject obj)
    {
        this.type = type;
        this.localPosition = obj.transform.localPosition;
        this.localRotation = obj.transform.localRotation;
        this.localScale = obj.transform.localScale;
    }


    public virtual void Save (GameDataWriter writer) {
		writer.Write(this.localPosition);
		writer.Write(this.localRotation);
		writer.Write(this.localScale);
	}

	public virtual void Load (GameDataReader reader) {
		this.localPosition = reader.ReadVector3();
		this.localRotation = reader.ReadQuaternion();
		this.localScale = reader.ReadVector3();
	}
}