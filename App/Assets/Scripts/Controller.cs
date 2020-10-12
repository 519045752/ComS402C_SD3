 using System.Collections.Generic;
    using GoogleARCore;
    using GoogleARCore.Examples.Common;
    using UnityEngine;
    using UnityEngine.EventSystems;
    using TMPro;

#if UNITY_EDITOR
// Set up touch input propagation while using Instant Preview in the editor.
using Input = GoogleARCore.InstantPreviewInput;
#endif

    /// <summary>
    /// Controls the HelloAR example.
    /// </summary>
    public class Controller : MonoBehaviour
    {
        /// <summary>
        /// The Depth Setting Menu.
        /// </summary>
        public DepthMenu DepthMenu;

        /// <summary>
        /// The Instant Placement Setting Menu.
        /// </summary>
        public InstantPlacementMenu InstantPlacementMenu;

        /// <summary>
        /// A prefab to place when an instant placement raycast from a user touch hits an instant
        /// placement point.
        /// </summary>
        public GameObject InstantPlacementPrefab;

        /// <summary>
        /// The first-person camera being used to render the passthrough camera image (i.e. AR
        /// background).
        /// </summary>
        public Camera FirstPersonCamera;

        /// <summary>
        /// A prefab to place when a raycast from a user touch hits a vertical plane.
        /// </summary>
        public GameObject GameObjectVerticalPlanePrefab;

        /// <summary>
        /// A prefab to place when a raycast from a user touch hits a horizontal plane.
        /// </summary>
        public GameObject GameObjectHorizontalPlanePrefab;

        /// <summary>
        /// A prefab to place when a raycast from a user touch hits a feature point.
        /// </summary>
        public GameObject GameObjectPointPrefab;

        /// <summary>
        /// The rotation in degrees need to apply to prefab when it is placed.
        /// </summary>
        private const float _prefabRotation = 180.0f;

        /// <summary>
        /// True if the app is in the process of quitting due to an ARCore connection error,
        /// otherwise false.
        /// </summary>
        private bool _isQuitting = false;

    private TrackableHit HIT;

    // Choose the prefab based on the Trackable that got hit.
    private GameObject prefab;

    private bool endPlace = true;

    // parent of placed objects
    public CanvasGroup canvasGroup;

    // get input from user
    public TMP_InputField Input_Tex;

    // Sets the object type to spawn
    public int objectType;

    // holds info related to game processing (e.g. saving/loading)
    public Game data;

    /// <summary>
    /// The Unity Awake() method.
    /// </summary>
    public void Awake()
        {
            // Enable ARCore to target 60fps camera capture frame rate on supported devices.
            // Note, Application.targetFrameRate is ignored when QualitySettings.vSyncCount != 0.
            Application.targetFrameRate = 60;
            Input_Tex.onSubmit.AddListener(Submit);
            objectType = 0;
        }

        /// <summary>
        /// The Unity Update() method.
        /// </summary>
        public void Update()
        {
            UpdateApplicationLifecycle();

            // If the player has not touched the screen, we are done with this update.
            Touch touch;
            if (Input.touchCount < 1 || (touch = Input.GetTouch(0)).phase != TouchPhase.Began)
            {
                return;
            }

            // Should not handle input if the player is pointing on UI.
            if (EventSystem.current.IsPointerOverGameObject(touch.fingerId))
            {
                return;
            }

            // Raycast against the location the player touched to search for planes.
            bool foundHit = false;
            TrackableHit hit;
            if (InstantPlacementMenu.IsInstantPlacementEnabled())
            {
                foundHit = Frame.RaycastInstantPlacement(
                    touch.position.x, touch.position.y, 1.0f, out hit);
            }
            else
            {
                TrackableHitFlags raycastFilter = TrackableHitFlags.PlaneWithinPolygon |
                    TrackableHitFlags.FeaturePointWithSurfaceNormal;
                foundHit = Frame.Raycast(
                    touch.position.x, touch.position.y, raycastFilter, out hit);
            }

            if (foundHit)
            {
                // Use hit pose and camera pose to check if hittest is from the
                // back of the plane, if it is, no need to create the anchor.
                if ((hit.Trackable is DetectedPlane) &&
                    Vector3.Dot(FirstPersonCamera.transform.position - hit.Pose.position,
                        hit.Pose.rotation * Vector3.up) < 0)
                {
                    Debug.Log("Hit at back of the current DetectedPlane");
                }
                else
                {
                    if (DepthMenu != null)
                    {
                        // Show depth card window if necessary.
                        DepthMenu.ConfigureDepthBeforePlacingFirstAsset();
                    }

                GameObject prefabL;
                    if (hit.Trackable is InstantPlacementPoint)
                    {
                    prefabL = InstantPlacementPrefab;
                    }
                    else if (hit.Trackable is FeaturePoint)
                    {
                    prefabL = GameObjectPointPrefab;
                    }
                    else if (hit.Trackable is DetectedPlane)
                    {
                        DetectedPlane detectedPlane = hit.Trackable as DetectedPlane;
                        if (detectedPlane.PlaneType == DetectedPlaneType.Vertical)
                        {
                        prefabL = GameObjectVerticalPlanePrefab;
                        }
                        else
                        {
                        prefabL = GameObjectHorizontalPlanePrefab;
                        }
                    }
                    else
                    {
                    prefabL = GameObjectHorizontalPlanePrefab;
                    }

                switch (objectType)
                {
                    case 0:
                        if (endPlace)
                        {
                            endPlace = false;
                            prefab = prefabL;
                            HIT = hit;
                            Show();
                        }
                        break;
                    default:
                        break;
                }


                // added game object -> you know drill


                // Compensate for the hitPose rotation facing away from the raycast (i.e.
                // camera).
                gameObject.transform.Rotate(0, _prefabRotation, 0, Space.Self);

                    // Create an anchor to allow ARCore to track the hitpoint as understanding of
                    // the physical world evolves.
                    var anchor = hit.Trackable.CreateAnchor(hit.Pose);

                    // Make game object a child of the anchor.
                    gameObject.transform.parent = anchor.transform;

                    // Initialize Instant Placement Effect.
                    if (hit.Trackable is InstantPlacementPoint)
                    {
                        gameObject.GetComponentInChildren<InstantPlacementEffect>()
                            .InitializeWithTrackable(hit.Trackable);
                    }
                }
            }
        }


    void Show()
    {
        Input_Tex.text = "";
        canvasGroup.alpha = 1f;
        canvasGroup.blocksRaycasts = true;
        // to debug: adb logcat -s Unity PackageManager dalvikvm DEBUG

    }

    // If not text, msg = ""
    void Submit(string msg)
    {
        canvasGroup.alpha = 0f; //this makes everything transparent
        canvasGroup.blocksRaycasts = false; //this prevents the UI element to receive input events

        //TMP_Text obj = Instantiate(objectToSpawn, placementIndicator.transform.position, placementIndicator.transform.rotation);

        if (prefab != null)
        {
            var gameObject = Instantiate(prefab, HIT.Pose.position, HIT.Pose.rotation);
            if (msg != "") gameObject.transform.GetComponent<TMP_Text>().text = msg;
            data.CreateObject(0, gameObject);
            endPlace = true;
        }
        Debug.Log("Running Submit");
    }

    /// <summary>
    /// Check and update the application lifecycle.
    /// </summary>
    private void UpdateApplicationLifecycle()
        {
            // Exit the app when the 'back' button is pressed.
            if (Input.GetKey(KeyCode.Escape))
            {
                Application.Quit();
            }

            // Only allow the screen to sleep when not tracking.
            if (Session.Status != SessionStatus.Tracking)
            {
                Screen.sleepTimeout = SleepTimeout.SystemSetting;
            }
            else
            {
                Screen.sleepTimeout = SleepTimeout.NeverSleep;
            }

            if (_isQuitting)
            {
                return;
            }

            // Quit if ARCore was unable to connect and give Unity some time for the toast to
            // appear.
            if (Session.Status == SessionStatus.ErrorPermissionNotGranted)
            {
                ShowAndroidToastMessage("Camera permission is needed to run this application.");
                _isQuitting = true;
                Invoke("DoQuit", 0.5f);
            }
            else if (Session.Status.IsError())
            {
                ShowAndroidToastMessage(
                    "ARCore encountered a problem connecting.  Please start the app again.");
                _isQuitting = true;
                Invoke("DoQuit", 0.5f);
            }
        }

        /// <summary>
        /// Actually quit the application.
        /// </summary>
        private void DoQuit()
        {
            Application.Quit();
        }

        /// <summary>
        /// Show an Android toast message.
        /// </summary>
        /// <param name="message">Message string to show in the toast.</param>
        private void ShowAndroidToastMessage(string message)
        {
            AndroidJavaClass unityPlayer = new AndroidJavaClass("com.unity3d.player.UnityPlayer");
            AndroidJavaObject unityActivity =
                unityPlayer.GetStatic<AndroidJavaObject>("currentActivity");

            if (unityActivity != null)
            {
                AndroidJavaClass toastClass = new AndroidJavaClass("android.widget.Toast");
                unityActivity.Call("runOnUiThread", new AndroidJavaRunnable(() =>
                {
                    AndroidJavaObject toastObject =
                        toastClass.CallStatic<AndroidJavaObject>(
                            "makeText", unityActivity, message, 0);
                    toastObject.Call("show");
                }));
            }
        }
    }

