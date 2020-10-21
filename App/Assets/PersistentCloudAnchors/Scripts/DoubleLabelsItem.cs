    using UnityEngine;
    using UnityEngine.UI;

    /// <summary>
    /// A helper component used by <see cref="MultiselectionDropdown"/> on displaying option data.
    /// </summary>
    public class DoubleLabelsItem : MonoBehaviour
    {
        /// <summary>
        /// The Text component of the first label, used to display the
        /// <see cref="MultiselectionDropdown.OptionData.MajorInfo"/>.
        /// </summary>
        public Text FirstLabel;

        /// <summary>
        /// The Text component for the second label, used to display the
        /// <see cref="MultiselectionDropdown.OptionData.MinorInfo"/>.
        /// </summary>
        public Text SecondLabel;

        /// <summary>
        /// Set the contents of two labels.
        /// </summary>
        /// <param name="first">The content for first label.</param>
        /// <param name="second">The content for second label.</param>
        public void SetLabels(string first, string second)
        {
            if (FirstLabel != null)
            {
                FirstLabel.text = first;
            }

            if (SecondLabel != null)
            {
                SecondLabel.text = second;
            }
        }
    }
