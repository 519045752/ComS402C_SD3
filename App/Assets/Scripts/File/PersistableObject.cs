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

    private string cloudid;

    public Vector3 localPosition;
    public Quaternion localRotation;
    public Vector3 localScale;

    public PersistableObject()
    {

    }

    public PersistableObject(string id, int type, GameObject obj)
    {
        this.cloudid = id;
        this.type = type;
        this.localPosition = obj.transform.parent.localPosition; // get anchor
        this.localRotation = obj.transform.parent.localRotation; // get anchor
        this.localScale = obj.transform.localScale;
    }


    public virtual void Save (GameDataWriter writer) {
        writer.Write(this.cloudid);
        writer.Write(this.type);
		writer.Write(this.localPosition);
		writer.Write(this.localRotation);
		writer.Write(this.localScale);
	}

	public virtual void Load (GameDataReader reader) {
        this.cloudid = reader.ReadString();
        this.type = reader.ReadInt();
        this.localPosition = reader.ReadVector3();
		this.localRotation = reader.ReadQuaternion();
		this.localScale = reader.ReadVector3();
       
	}
}