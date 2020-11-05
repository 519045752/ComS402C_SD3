

    using System.Collections.Generic;
    using System.Text.RegularExpressions;
    using GoogleARCore.CrossPlatform;
    using GoogleARCore.Examples.Common;
    using GoogleARCore;
    using UnityEngine.EventSystems;
    using UnityEngine.UI;
    using TMPro;
    using UnityEngine;
using Assets.PersistentCloudAnchors.Scripts;

#if ARCORE_IOS_SUPPORT
    using UnityEngine.XR.iOS;
#endif

/// <summary>
/// A manager component that helps with hosting and resolving Cloud Anchors.
/// </summary>
public class ARViewManager : MonoBehaviour
    {
        /// <summary>
        /// The main controller for Persistent Cloud Anchors sample.
        /// </summary>
        public PersistentCloudAnchorsController Controller;

        /// <summary>
        /// The 3D object that represents a Cloud Anchor.
        /// </summary>
        /// to change back to public.
         public GameObject CloudAnchorPrefab;

        /// <summary>
        /// The game object that includes <see cref="MapQualityIndicator"/> to visualize
        /// map quality result.
        /// </summary>
        public GameObject MapQualityIndicatorPrefab;

        /// <summary>
        /// The UI element that displays the instructions to guide hosting experience.
        /// </summary>
        public GameObject InstructionBar;

        

        /// <summary>
        /// The UI panel that allows the user to copy the Cloud Anchor Id and share it.
        /// </summary>
        public GameObject CopyPanel;

        /// <summary>
        /// The UI element that displays warning message for invalid input name.
        /// </summary>
        public GameObject InputFieldWarning;

        /// <summary>
        /// The instruction text in the top instruction bar.
        /// </summary>
        public Text InstructionText;

        /// <summary>
        /// The debug text in bottom snack bar.
        /// </summary>
        public Text DebugText;

        /// <summary>
        /// The button to save current cloud anchor id into clipboard.
        /// </summary>
        public Button ShareButton;

        /// <summary>
        /// The time between enters AR View and ARCore session starts to host or resolve.
        /// </summary>
        private const float _startPrepareTime = 3.0f;

        /// <summary>
        /// The timer to indicate whether the AR View has passed the start prepare time.
        /// </summary>
        private float _timeSinceStart;

        /// <summary>
        /// True if the app is in the process of returning to home page due to an invalid state,
        /// otherwise false.
        /// </summary>
        private bool _isReturning;

        /// <summary>
        /// True if ARCore is hosting the anchor given by previous hit test result.
        /// </summary>
        private bool _isHosting;

        /// <summary>
        /// The MapQualityIndicator that attaches to the placed object.
        /// </summary>
        private MapQualityIndicator _qualityIndicator = null;

        /// <summary>
        /// The history data that represents the current hosted Cloud Anchor.
        /// </summary>
        private CloudAnchorHistory _hostedCloudAnchor;

        /// <summary>
        /// The hit pose from platfrom-specific hit test result.
        /// </summary>
        private Pose? _hitPose = null;

        /// <summary>
        /// A platform-specific component indicating the pawn object has been placed
        /// on a flat surface.
        /// </summary>
        private Component _anchorComponent = null;

        /// <summary>
        /// A list for caching all resolved results.
        /// </summary>
        private List<Component> _cachedComponents = new List<Component>();

        /// <summary>
        /// A set for caching all pending resolving AsyncTasks.
        /// </summary>
        private HashSet<string> _pendingTask = new HashSet<string>();

        private Color _activeColor;

        // holds reference to most recencly placed object
        private GameObject gameRef;

        public AnchorNetworking networker;

        // prefab contains text data
        public GameObject prefabToPlace;
        private List<GameObject> prefabsOnMap; // list of prefabs on map
        private Transform currentCloudTransform;

        private bool CanPlace = true;
        private string cloudid;
        private bool submitlock = false;

        private HashSet<ServerObject> svrObjects;
        private GameObject prevText;

        // parent of placed objects
        public CanvasGroup canvasGroup;

        // get input from user
        public TMP_InputField Input_Tex;

        // Sets the object type to spawn
        public int objectType;

        // holds info related to game processing (e.g. saving/loading)
        public Game data;

        // trackable hit
        TrackableHit arcoreHitResult;

        //Store the list of all placable prefab from path "Resources/Prefab"
        private GameObject[] prefabList;

        //the Dropdown menu for showing all placable prefab
        public Dropdown prefabDropdown;
        
        //Spawn prefab from dropdown menu;
        private PrefabGallery prefabGallery;

        //
        public Button confirmButton;
        bool isConfirmButtonPressed = false;
    

#if ARCORE_IOS_SUPPORT
        private List<ARHitTestResult> _hitResultList = new List<ARHitTestResult>();
        private Dictionary<string, ARPlaneAnchorAlignment> _arPlaneAligmentMapping =
            new Dictionary<string, ARPlaneAnchorAlignment>();
#endif

        /// <summary>
        /// Get the camera pose for the current frame.
        /// </summary>
        /// <returns>The camera pose of the current frame.</returns>
        public static Pose GetCameraPose()
        {
            Pose framePose = new Pose();
            if (Application.platform == RuntimePlatform.IPhonePlayer)
            {
#if ARCORE_IOS_SUPPORT
                var session = UnityARSessionNativeInterface.GetARSessionNativeInterface();
                if (session != null)
                {
                    var matrix = session.GetCameraPose();
                    framePose.position = UnityARMatrixOps.GetPosition(matrix);
                    framePose.rotation = UnityARMatrixOps.GetRotation(matrix);
                }
#endif
            }
            else
            {
                framePose = Frame.Pose;
            }

            return framePose;
        }

        /// <summary>
        /// Callback handling "Share" button click event.
        /// </summary>
        public void OnShareButtonClicked()
        {
#if UNITY_2018_4_OR_NEWER
            GUIUtility.systemCopyBuffer = _hostedCloudAnchor.Id;
#else
            // On 2017.4, GUIUtility.systemCopyBuffer doesn't support Android or iOS.
            // Pops up a text field and let user to manually copy the Cloud Anchor Id.
            var textField = CopyPanel.GetComponentInChildren<InputField>();
            textField.text = _hostedCloudAnchor.Id;
            CopyPanel.SetActive(true);
#endif
            DebugText.text = "Copied cloud id: " + _hostedCloudAnchor.Id;
        }

        /// <summary>
        /// Callback handling "Done" button click event in copy panel.
        /// </summary>
        public void OnCopyCompleted()
        {
            CopyPanel.SetActive(false);
        }

        /// <summary>
        /// The Unity Awake() method.
        /// </summary>
        public void Awake()
        {

            svrObjects = new HashSet<ServerObject>();
            StartCoroutine(networker.getCloudIds(svrObjects));
            foreach (ServerObject obj in svrObjects) { Controller.ResolvingSet.Add(obj.cloudid); }

            prefabToPlace.transform.Find("textInfoWindow").gameObject.SetActive(false);
            prefabsOnMap = new List<GameObject>();
            //Input_Tex.onSubmit.AddListener(Submit);

            //Store all prefab from "Resources/Prefab" in the array
            prefabList = Resources.LoadAll<GameObject>("Prefab");
            if (prefabList == null)
            {
                Debug.Log("prefab List is null, Resources.LoadAll failed");
            }
        confirmButton.onClick.AddListener(Submit);
            objectType = 0;
#if ARCORE_IOS_SUPPORT
            if (Application.platform == RuntimePlatform.IPhonePlayer)
            {
                UnityARSessionNativeInterface.ARAnchorAddedEvent += AddARPlane;
                UnityARSessionNativeInterface.ARAnchorRemovedEvent += RemoveARPlane;
            }
#endif
        }

        /// <summary>
        /// The Unity OnEnable() method.
        /// </summary>
        public void OnEnable()
        {
            _timeSinceStart = 0.0f;
            _isReturning = false;
            _isHosting = false;
            _hitPose = null;
            _anchorComponent = null;
            _qualityIndicator = null;
            _cachedComponents.Clear();

            InstructionBar.SetActive(true);
            //NamePanel.SetActive(false);
            CopyPanel.SetActive(false);
            InputFieldWarning.SetActive(false);
            ShareButton.gameObject.SetActive(false);
            Controller.PlaneGenerator.SetActive(true);
            prefabDropdown.gameObject.SetActive(false);
            confirmButton.gameObject.SetActive(false);
            switch (Controller.Mode)
            {
                case PersistentCloudAnchorsController.ApplicationMode.Ready:
                    ReturnToHomePage("Invalid application mode, returning to home page...");
                    break;
                case PersistentCloudAnchorsController.ApplicationMode.Hosting:
                case PersistentCloudAnchorsController.ApplicationMode.Resolving:
                    InstructionText.text = "Detecting flat surface...";
                    DebugText.text = "ARCore is preparing for " + Controller.Mode;
                    break;
            }
        }

        /// <summary>
        /// The Unity OnDisable() method.
        /// </summary>
        public void OnDisable()
        {
            if (_pendingTask.Count > 0)
            {
                Debug.LogFormat("Cancelling pending tasks for {0} Cloud Anchor(s): {1}",
                    _pendingTask.Count,
                    string.Join(",", new List<string>(_pendingTask).ToArray()));
                foreach (string id in _pendingTask)
                {
                    XPSession.CancelCloudAnchorAsyncTask(id);
                }

                _pendingTask.Clear();
            }

            if (_qualityIndicator != null)
            {
                Destroy(_qualityIndicator.gameObject);
                _qualityIndicator = null;
            }

            if (_anchorComponent != null)
            {
                Destroy(_anchorComponent.gameObject);
                _anchorComponent = null;
            }

            if (_cachedComponents.Count > 0)
            {
                foreach (var anchor in _cachedComponents)
                {
                    Destroy(anchor.gameObject);
                }

                _cachedComponents.Clear();
            }
        }

#if ARCORE_IOS_SUPPORT
        /// <summary>
        /// The Unity OnDestroy() method.
        /// </summary>
        public void OnDestroy()
        {
            _arPlaneAligmentMapping.Clear();
            if (Application.platform == RuntimePlatform.IPhonePlayer)
            {
                UnityARSessionNativeInterface.ARAnchorAddedEvent -= AddARPlane;
                UnityARSessionNativeInterface.ARAnchorRemovedEvent -= RemoveARPlane;
            }

        }
#endif

        /// <summary>
        /// The Unity Update() method.
        /// </summary>
        public void Update()
        {

        if (submitlock) Debug.Log("Current value for " + prefabDropdown.GetComponent<Dropdown>().value + 
            " is: " + "Prefab/" + prefabList[prefabDropdown.GetComponent<Dropdown>().value].name);

        // Check if touching object
        if (Input.touchCount > 0) {
            Touch touch = Input.GetTouch(0);
            if (!((touch.phase != TouchPhase.Began) || EventSystem.current.IsPointerOverGameObject(touch.fingerId)))
            {
                // Detect if raycast hits object
                Debug.Log("Raycasting to see if touch is on object.");
                touchObject();
            }
        }



        // Give ARCore some time to prepare for hosting or resolving.
        if (_timeSinceStart < _startPrepareTime)
            {
                _timeSinceStart += Time.deltaTime;
                if (_timeSinceStart >= _startPrepareTime)
                {
                    UpdateInitialInstruction();
                }

                return;
            }

            ARCoreLifecycleUpdate();
            if (_isReturning)
            {
                return;
            }

            if (Controller.Mode == PersistentCloudAnchorsController.ApplicationMode.Resolving)
            {
                ResolvingCloudAnchors();    // called every update           
            }
            else if (Controller.Mode == PersistentCloudAnchorsController.ApplicationMode.Hosting)
            {
                // Perform hit test and place an anchor on the hit test result.
                if (_hitPose == null)
                {

                    if (!CanPlace)
                        return;
                    // If the player has not touched the screen then the update is complete.
                    Touch touch;
                    if ((Input.touchCount < 1 || (touch = Input.GetTouch(0)).phase != TouchPhase.Began))
                    {
                        return;
                    }

                    // Ignore the touch if it's pointing on UI objects.
                    if (EventSystem.current.IsPointerOverGameObject(touch.fingerId))
                    {
                        return;
                    }

                    // Perform hit test and place a pawn object.
                    PerformHitTest(touch.position);
                }
            
            
            HostingCloudAnchor();
        }
        }

    private void touchObject()
    {
        Ray raycast = Camera.main.ScreenPointToRay(Input.GetTouch(0).position);
        RaycastHit raycastHit;
        if (Physics.Raycast(raycast, out raycastHit))
        {
            string name = raycastHit.collider.transform.parent.gameObject.name;
            Debug.Log(name + " was hit");

            foreach (GameObject objName in prefabsOnMap)
            {
                Debug.Log("The name was objname:" + objName.name);
                if (objName.name == name)
                {
                    var tex = objName.transform.Find("textInfoWindow").gameObject;
                    if (tex.activeSelf)
                    {
                        prevText.transform.Find("textInfoWindow").gameObject.SetActive(false);
                        prevText = null;
                    }
                    else
                    {
                        if (prevText) prevText.transform.Find("textInfoWindow").gameObject.SetActive(false);

                        tex.SetActive(true);
                        prevText = objName;

                        //Debug.LogFormat(tex.transform.GetComponent<TMP_Text>().text);
                        DebugText.text = tex.transform.GetComponent<TMP_Text>().text;
                    }
                    return;
                }

                
            }
        }
    }


        private void PerformHitTest(Vector2 touchPos)
        {
            var planeType = DetectedPlaneType.HorizontalUpwardFacing;
            if (Application.platform == RuntimePlatform.IPhonePlayer)
            {
#if ARCORE_IOS_SUPPORT
                var session = UnityARSessionNativeInterface.GetARSessionNativeInterface();
                var viewportPoint = Controller.MainCamera.ScreenToViewportPoint(touchPos);
                ARPoint arPoint = new ARPoint
                {
                    x = viewportPoint.x,
                    y = viewportPoint.y
                };

                _hitResultList = session.HitTest(arPoint,
                    ARHitTestResultType.ARHitTestResultTypeExistingPlaneUsingExtent);
                if (_hitResultList.Count > 0)
                {
                    // Fetch the closest hit result.
                    int minDistanceIndex = GetMinDistanceIndex(_hitResultList);

                    string identifier = _hitResultList[minDistanceIndex].anchorIdentifier;
                    if (_arPlaneAligmentMapping.ContainsKey(identifier))
                    {
                        planeType = _arPlaneAligmentMapping[identifier] ==
                            ARPlaneAnchorAlignment.ARPlaneAnchorAlignmentVertical ?
                            DetectedPlaneType.Vertical : DetectedPlaneType.HorizontalUpwardFacing;
                    }
                    else
                    {
                        Debug.LogWarningFormat("Didn't find anchor identifier: {0}", identifier);
                        return;
                    }

                    Pose hitPose = new Pose();
                    hitPose.position = UnityARMatrixOps.GetPosition(
                        _hitResultList[minDistanceIndex].worldTransform);
                    if (planeType == DetectedPlaneType.Vertical)
                    {
                        hitPose.rotation = UnityARMatrixOps.GetRotation(
                            _hitResultList[minDistanceIndex].worldTransform);
                    }
                    else
                    {
                        // Point the hitPose rotation roughly away from the raycast/camera
                        // to match ARCore.
                        hitPose.rotation.eulerAngles =
                            new Vector3(0.0f, Controller.MainCamera.transform.eulerAngles.y, 0.0f);
                    }

                    _hitPose = hitPose;
                    var anchorGO = new GameObject("ARUserAnchor");
                    _anchorComponent = anchorGO.AddComponent<UnityARUserAnchorComponent>();
                    anchorGO.transform.position = hitPose.position;
                    anchorGO.transform.rotation = hitPose.rotation;
                }
#endif
            }
            else
            {
                arcoreHitResult = new TrackableHit();
                if (Frame.Raycast(touchPos.x, touchPos.y, TrackableHitFlags.PlaneWithinPolygon,
                    out arcoreHitResult))
                {
                    DetectedPlane plane = arcoreHitResult.Trackable as DetectedPlane;
                    if (plane == null)
                    {
                        Debug.LogWarning("Hit test result has invalid trackable type: " +
                            arcoreHitResult.Trackable.GetType());
                        return;
                    }

                    planeType = plane.PlaneType;
                    _hitPose = arcoreHitResult.Pose;
                    _anchorComponent =
                        arcoreHitResult.Trackable.CreateAnchor(arcoreHitResult.Pose);
                }
            }

            if (_anchorComponent != null)
            {
            
                gameRef = Instantiate(CloudAnchorPrefab, _anchorComponent.transform);

                // Attach map quality indicator to this pawn.
                var indicatorGO =
                    Instantiate(MapQualityIndicatorPrefab, _anchorComponent.transform);
                _qualityIndicator = indicatorGO.GetComponent<MapQualityIndicator>();
                _qualityIndicator.DrawIndicator(planeType, Controller.MainCamera);

                InstructionText.text = " To save this location, walk around the object to " +
                    "capture it from different angles";
                DebugText.text = "Waiting for sufficient mapping quaility...";

                // Hide plane generator so users can focus on the object they placed.
                Controller.PlaneGenerator.SetActive(false);
            }
        }

        void Show()
        {
            Input_Tex.text = "";
            canvasGroup.alpha = 1f;
            canvasGroup.blocksRaycasts = true;
            CanPlace = false;
            // to debug: adb logcat -s Unity PackageManager dalvikvm DEBUG

        }

        // If not text, msg = ""
        void Submit()
        {
            if (submitlock)
            {
                submitlock = false;
                Debug.Log("Running Submit");

                string msg = Input_Tex.text;

                //get the index of prefab selected by user from the dropdown menu
                //might be because of null exception, since nothing is chosen yet.
                //Get the prefab from folder "Resources/Prefab/{Name of object selected}"
                prefabToPlace = Resources.Load("Prefab/" + prefabList[prefabDropdown.GetComponent<Dropdown>().value].name) as GameObject;

                gameRef = Instantiate(prefabToPlace,currentCloudTransform);
                prefabsOnMap.Add(gameRef);
                
                prefabDropdown.gameObject.SetActive(false);
                confirmButton.gameObject.SetActive(false);

                canvasGroup.alpha = 0f; //this makes everything transparent
                canvasGroup.blocksRaycasts = false; //this prevents the UI element to receive input events

                gameRef.transform.Find("textInfoWindow").gameObject.transform.GetComponent<TMP_Text>().text = msg;

                string id = cloudid;
                string description = msg;
                int t = objectType;
                StartCoroutine(networker.AddCloudID(id, description, 1, prefabDropdown.GetComponent<Dropdown>().value));
                CanPlace = true;
                
                
            }
        }

        private void HostingCloudAnchor()
        {
            // There is no anchor for hosting.
            if (_anchorComponent == null)
            {
                return;
            }

            // There is a pending hosting task.
            if (_isHosting)
            {
                return;
            }

            // Hosting instructions:
            var cameraDist = (_qualityIndicator.transform.position -
                Controller.MainCamera.transform.position).magnitude;
            if (cameraDist < _qualityIndicator.Radius * 1.5f)
            {
                InstructionText.text = "You are too close, move backward.";
                return;
            }
            else if (cameraDist > 10.0f)
            {
                InstructionText.text = "You are too far, come closer.";
                return;
            }
            else if (_qualityIndicator.ReachTopviewAngle)
            {
                InstructionText.text =
                    "You are looking from the top view, move around from all sides.";
                return;
            }
            else if (!_qualityIndicator.ReachQualityThreshold)
            {
                InstructionText.text = "Save the object here by capturing it from all sides.";
                // Can pass in ANY valid camera pose to the mapping quality API.
                // Ideally, the pose should represent users’ expected perspectives.
                DebugText.text = "Current mapping quality: " +
                    XPSession.EstimateFeatureMapQualityForHosting(GetCameraPose());
                return;
            }

            // Start hosting:
            _isHosting = true;
            InstructionText.text = "Processing...";
            DebugText.text = "Mapping quality has reached sufficient threshold, " +
                "creating Cloud Anchor.";
            DebugText.text = string.Format(
                "FeatureMapQuality has reached {0}, triggering CreateCloudAnchor.",
                XPSession.EstimateFeatureMapQualityForHosting(GetCameraPose()));

#if ARCORE_IOS_SUPPORT
            var anchor = (UnityARUserAnchorComponent)_anchorComponent;
#else
            var anchor = (Anchor)_anchorComponent;
#endif

        // Creating a Cloud Anchor with lifetime = 1 day.
        // This is configurable up to 365 days when keyless authentication is used.
        XPSession.CreateCloudAnchor(anchor, 1).ThenAction(result =>
            {
                if (!_isHosting)
                {
                    // This is the pending task from previous session.
                    return;
                }

                if (result.Response != CloudServiceResponse.Success)
                {
                    Debug.LogFormat("Failed to host cloud anchor: {0}", result.Response);
                    OnAnchorHostedFinished(false, result.Response.ToString());
                }
                else
                {
                    Debug.LogFormat("Succeed to host cloud anchor: {0}", result.Anchor.CloudId);
                    int count = Controller.LoadCloudAnchorHistory().Collection.Count;
                    cloudid = result.Anchor.CloudId;
                    submitlock = true;
                    _hostedCloudAnchor = new CloudAnchorHistory(cloudid, cloudid);
                    OnAnchorHostedFinished(true, result.Anchor.CloudId);
                    //todo, work on this part
                    //load available prefrab from Resource/Prefab
                    //Do Dropdown List, allow user to select the prefab. 
                    //then press confirm(Button) to spawn prefab at the hitpose.

                    InstructionText.text = "Please select a prefab to place";
                    prefabDropdown.gameObject.SetActive(true);
                    confirmButton.gameObject.SetActive(true);


                    currentCloudTransform = result.Anchor.transform;


                    Show();
                }
            });
        }

        private void ResolvingCloudAnchors()
        {

            var hash = new HashSet<string>();

            // No Cloud Anchor for resolving.
            if (Controller.ResolvingSet.Count == 0)
            {
                return;
            }

            // ARCore session is not ready for resolving.
            if (Session.Status != SessionStatus.Tracking)
            {
                return;
            }

            Debug.LogFormat("Attempting to resolve {0} anchor(s): {1}",
                Controller.ResolvingSet.Count,
                string.Join(",", new List<string>(Controller.ResolvingSet).ToArray()));
            foreach (ServerObject obj in svrObjects)
            {
                string cloudId = obj.cloudid;

                _pendingTask.Add(cloudId);
                XPSession.ResolveCloudAnchor(cloudId).ThenAction(result =>
                {
                    _pendingTask.Remove(cloudId);
                    if (result.Response != CloudServiceResponse.Success)
                    {
                        Debug.LogFormat("Failed to resolve cloud anchor {0} for {1}",
                            cloudId, result.Response);
                        OnAnchorResolvedFinished(false, result.Response.ToString());
                        hash.Add(cloudid);
                    }
                    else
                    {
                        Debug.LogFormat("Succeed to resolve cloud anchor: {0}", cloudId);
                        OnAnchorResolvedFinished(true, cloudId);

                        prefabToPlace = Resources.Load("Prefab/" + prefabList[obj.type].name) as GameObject;

                        gameRef = Instantiate(prefabToPlace, currentCloudTransform);
                        gameRef.transform.Find("textInfoWindow").gameObject.transform.GetComponent<TMP_Text>().text = obj.msg;
                        prefabsOnMap.Add(gameRef);

                        _cachedComponents.Add(result.Anchor);
                    }
                });
            }

            Controller.ResolvingSet = hash;
        }

        private void OnAnchorHostedFinished(bool success, string response)
        {
            if (success)
            {
                InstructionText.text = "Finish!";
                Invoke("DoHideInstructionBar", 1.5f);
                DebugText.text = "Succeed to host cloud anchor: " + response;

                _hostedCloudAnchor.Name = cloudid;
                Controller.SaveCloudAnchorHistory(_hostedCloudAnchor);
                DebugText.text = string.Format("Saved Cloud Anchor:\n{0}.", _hostedCloudAnchor.Name);
            }
            else
            {
                InstructionText.text = "Host failed.";
                DebugText.text = "Failed to host cloud anchor: " + response;
            }
        }

        private void OnAnchorResolvedFinished(bool success, string response)
        {
            if (success)
            {
                InstructionText.text = "Resolve success!";
                DebugText.text = "Succeed to resolve cloud anchor: " + response;
            }
            else
            {
                InstructionText.text = "Resolve failed.";
                DebugText.text = "Failed to resolve cloud anchor: " + response;
            }
        }

        private void UpdateInitialInstruction()
        {
            switch (Controller.Mode)
            {
                case PersistentCloudAnchorsController.ApplicationMode.Hosting:
                    // Initial instruction for hosting flow:
                    InstructionText.text = "Tap to place an object.";
                    DebugText.text = "Tap a vertical or horizontal plane...";
                    return;
                case PersistentCloudAnchorsController.ApplicationMode.Resolving:
                    // Initial instruction for resolving flow:
                    InstructionText.text =
                        "Look at the location you expect to see the AR experience appear.";
                    DebugText.text = string.Format("Attempting to resolve {0} anchors...",
                        Controller.ResolvingSet.Count);
                    return;
                default:
                    return;
            }
        }

        private void ARCoreLifecycleUpdate()
        {
            var sleepTimeout = SleepTimeout.NeverSleep;
#if !UNITY_IOS
            // Only allow the screen to sleep when not tracking.
            if (Session.Status != SessionStatus.Tracking)
            {
                sleepTimeout = SleepTimeout.SystemSetting;
            }
#endif

            Screen.sleepTimeout = sleepTimeout;

            if (_isReturning)
            {
                return;
            }

            // Return to home page if ARCore is in error status.
            if (Session.Status == SessionStatus.ErrorPermissionNotGranted)
            {
                ReturnToHomePage("Camera permission is needed to run this application.");
            }
            else if (Session.Status.IsError())
            {
                ReturnToHomePage("ARCore encountered a problem connecting. " +
                    "Please start the app again.");
            }
        }

        private void ReturnToHomePage(string reason)
        {
            Debug.Log("Returning home for reason: " + reason);
            if (_isReturning)
            {
                return;
            }

            _isReturning = true;
            DebugText.text = reason;
            Invoke("DoReturnToHomePage", 3.0f);
        }

        private void DoReturnToHomePage()
        {
            Controller.SwitchToHomePage();
        }

        private void DoHideInstructionBar()
        {
            InstructionBar.SetActive(false);
        }

   

   

#if ARCORE_IOS_SUPPORT
        private void AddARPlane(ARPlaneAnchor arPlane)
        {
            _arPlaneAligmentMapping[arPlane.identifier] = arPlane.alignment;
        }

        private void RemoveARPlane(ARPlaneAnchor arPlane)
        {
            _arPlaneAligmentMapping.Remove(arPlane.identifier);
        }

        private int GetMinDistanceIndex(List<ARHitTestResult> results)
        {
            if (results.Count == 0)
            {
                return -1;
            }

            int minDistanceIndex = 0;
            for (int i = 1; i < results.Count; i++)
            {
                if (results[i].distance < results[minDistanceIndex].distance)
                {
                    minDistanceIndex = i;
                }
            }

            return minDistanceIndex;
        }
#endif
    }