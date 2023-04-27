package com.tidepool.tidepoolsdkjava;
/**
 * @since alpha-0.1.0
 */
public class Constants {
	/**
	 * Represents a type of upload. call {@link Object#toString()} to get the string code for the type.
	 * @since alpha-0.2.0
	 */
	public static enum uploadType {
		// TODO: Add descriptions of what each type represents (needs person with backend knowledge)
		/**
		 * An alert that was sent
		 * @since alpha-0.2.0
		 */
		ALERT("alert"),
		/**
		 * Scheduled basal or temporary basal
		 * @since alpha-0.2.0
		 */
		BASAL("basal"),
		/**
		 * Blook Ketone levels from a blood keytone meter
		 * @since alpha-0.2.0
		 */
		BLOOD_KETONE("bloodKetone"),
		/**
		 * Boluses
		 * @since alpha-0.2.0
		 */
		BOLUS("bolus"),
		/**
		 * CGM data
		 * @since alpha-0.2.0
		 */
		CBG("cbg"),
		/**
		 * When settings related to CGMs are changed
		 * @since alpha-0.2.0
		 */
		CGM_SETTINGS("cgmSettings"),
		/**
		 * Therapy settings changed
		 * @since alpha-0.2.0
		 */
		CONTROLLER_SETTINGS("controllerSettings"),
		/**
		 * Closed loop on/off?
		 * TODO: verify this documentation
		 * @since alpha-0.2.0
		 */
		CONTROLLER_STATUS("controllerStatus"),
		/**
		 * Whenever there is a coms connection with CGM or pump
		 * @since alpha-0.2.0
		 */
		DEVICE_EVENT("deviceEvent"),
		/**
		 * Status changes of a device
		 * @since alpha-0.2.0
		 */
		DEVICE_STATUS("deviceStatus"),
		/**
		 * Dosing descisions made by Loop
		 * @since alpha-0.2.0
		 */
		DOSING_DESCISION("dosingDescision"),
		/**
		 * Generally carbs
		 * TODO: more food types?
		 * @since alpha-0.2.0
		 */
		FOOD("food"),
		/**
		 * Insulin deliveries
		 * @since alpha-0.2.0
		 */
		INSULIN("insulin"),
		/**
		 * Workout information imported from apple health via Tidepool Mobile
		 * @since alpha-0.2.0
		 */
		PHYSICAL_ACTIVITY("physicalActivity"),
		/**
		 * Pump-specific settings changed
		 * @since alpha-0.2.0
		 */
		PUMP_SETTINGS("pumpSettings"),
		/**
		 * Status of a pump
		 * @since alpha-0.2.0
		 */
		PUMP_STATUS("pumpStatus"),
		/**
		 * TODO: documentation needed (don't know what this is)
		 * @since alpha-0.2.0
		 */
		REPORTED_STATE("reportedState"),
		/**
		 * Glucose readings from a BGM
		 * @since alpha-0.2.0
		 */
		SMBG("smbg"),
		/**
		 * TODO: documentation needed (don't know what this is)
		 * @since alpha-0.2.0
		 */
		UPLOAD("upload"),
		/**
		 * TODO: documentation needed (don't know what this is)
		 * @since alpha-0.2.0
		 */
		WATER("water"),
		/**
		 * Data from a traditional pump bolus wizzard
		 * @since alpha-0.2.0
		 */
		WIZARD("wizard");
		/**
		 * The string value that represents the uploadType in the backend
		 * @since alpha-0.2.0
		 */
		private final String stringValue;
		/**
		 * Creates an uploadType
		 * @param stringValue The representation of this type in the backend
		 * @since alpha-0.2.0
		 */
		private uploadType(String stringValue) {
			this.stringValue = stringValue;
		}
		/**
		 * Returns the {@link String} value representing this type
		 * @return The {@link String} value representing this type
		 * @since alpha-0.2.0
		 */
		@Override
		public String toString() {
			return stringValue;
		}
	}
}
