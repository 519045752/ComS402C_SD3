# Digital Concierge Mobile AR Synopsis
AirBnB has forever changed the travel experience. More and more travelers are opting for “local” experiences than ever before, staying away from large hotels and resort complexes. When renters book a private room (~34% of all listing types) off of AirBnB, they often come with a built-in personal concierge, the homeowner, to help them determine what to explore. What about the scenario in which the renter rents an entire home on AirBnB (~64% of all listings)? Who helps these folks find their way around?  Have you ever gone on vacation and weren’t sure what to do or where to go? Technology can help solve this problem for many travelers.

In addition to simply identifying activities, the concierge can also help guests navigate the home and its features. Does the home have a sauna, hot tub, or other unit specific amenity? Do you know how to work it? Simply hold the phone up to the sauna and the directions appear right there, allowing for a stress-free experience. Since each home is different, the host would need to be able to create and catalog these instructions as well as map out his or her property.


One usage example for hiking: Let’s presume there is a nice hiking trail about a mile from the home. Our host heads to the trail and geotags the trailhead. She then proceeds to use the app to track her hike along the trail, tracing the route. She gives this hike a short description and saves it, attaching it to her rental. Once this is complete, a future renter that finds her home on AirBnB can download the app prior to arriving at the property. From here, our renter can be standing on the porch, open the app, and use his device to scan the area for activities. He will see the hike in front of him on the screen and be able to tap for more details including the notes provided by the host. The guest could potentially add comments or ratings to the activity created by the host.

# Important Links
Website: https://seniord.cs.iastate.edu/2020-Dec-SD3/

Trello: https://trello.com/b/eIL2WDEr/coms-402c

# Instructions to run

Need:
*  Unity Community Edition
*  Device compatible with Google ARCore: https://developers.google.com/ar/discover/supported-devices
*  Application password (listed in important info on trello: https://trello.com/c/OzMJ28pj/37-important-info)
*  The Cisco vpn installed for android (https://play.google.com/store/apps/details?id=com.cisco.anyconnect.vpn.android.avf&hl=en_US&gl=US) connected to the Iowa State network

Clone this repository, and open in Unity. The keystore needs to be authorized, and so enter the password at Project Settings -> Player -> Android (see keystore @https://docs.unity3d.com/Manual/class-PlayerSettingsAndroid.html#Publishing)

File -> Build & Run to specify the build settings for android, and the location to save the .apk file. If an android device is connected by usb, it will build the application to the phone.


