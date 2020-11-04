using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Assets.PersistentCloudAnchors.Scripts
{


    /*
     * Holds info for objects returned from server
     * 
     */
    public class ServerObject
    {
        public string cloudid;
        public string msg;
        public int type;

        public ServerObject(string id, string text, int val)
        {
            cloudid = id;
            text = msg;
            type = val;
        }
    }
}
