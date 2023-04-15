package com.tidepool.tidepoolsdkjava;
/**
 * @since alpha-0.1.0
 */
public class Constants {
	/**
	 * A Regulaar Expression that matches an ISO-8601 formatted time and date string
	 * in UTC time
	 * @deprecated Not used, and won't be used
	 * @since alpha-0.1.0
	 */
	@Deprecated(since = "alpha-0.2.0", forRemoval = true)
	public static final String ISO_8601_REGEX = "[0-9]{4}-(0[0-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(0[0-9]|1[0-9]|2[0-4]):(0[0-9]|[1-5][0-9]):(0[0-9]|[1-5][0-9]|60)Z";
	/**
	 * 
	 * A Regulaar Expression that matches an ISO-8601 formatted time and date string
	 * in any time zone
	 * <strong>Warning: will match time zone "-00:00", even though it is invalid</strong>
	 * @deprecated Not used, and won't be used
	 * @since alpha-0.1.0
	 */
	@Deprecated(since = "alpha-0.2.0", forRemoval = true)
	public static final String ISO_8601_REGEX_ANY = "[0-9]{4}-(0[0-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(0[0-9]|1[0-9]|2[0-4]):(0[0-9]|[1-5][0-9]):(0[0-9]|[1-5][0-9]|60)(Z|(+[0-9]{2}:[0-9]{2}|-([0-9]{2})(?(?=00)([0-9](?(?=0)[1-9]|[0-9])|:[0-9]{2})))";

	/**
	 * Represents a type of upload. call {@link Object#toString()} to get the string code for the type.
	 * @since alpha-0.2.0
	 */
	public static enum uploadType {
		// TODO: Add descriptions of what each type represents (needs person with in-depth backend knowledge)
		/**@since alpha-0.2.0 */
		ALERT("alert"),
		/**@since alpha-0.2.0 */
		BASAL("basal"),
		/**@since alpha-0.2.0 */
		BLOOD_KETONE("bloodKetone"),
		/**@since alpha-0.2.0 */
		BOLUS("bolus"),
		/**
		 * aka cgm data
		 * @since alpha-0.2.0
		 */
		CBG("cbg"),
		/**@since alpha-0.2.0 */
		CGM_SETTINGS("cgmSettings"),
		/**@since alpha-0.2.0 */
		CONTROLLER_SETTINGS("controllerSettings"),
		/**@since alpha-0.2.0 */
		CONTROLLER_STATUS("controllerStatus"),
		/**@since alpha-0.2.0 */
		DEVICE_EVENT("deviceEvent"),
		/**@since alpha-0.2.0 */
		DEVICE_STATUS("deviceStatus"),
		/**@since alpha-0.2.0 */
		DOSING_DESCISION("dosingDescision"),
		/**@since alpha-0.2.0 */
		FOOD("food"),
		/**@since alpha-0.2.0 */
		INSULIN("insulin"),
		/**@since alpha-0.2.0 */
		PHYSICAL_ACTIVITY("physicalActivity"),
		/**@since alpha-0.2.0 */
		PUMP_SETTINGS("pumpSettings"),
		/**@since alpha-0.2.0 */
		PUMP_STATUS("pumpStatus"),
		/**@since alpha-0.2.0 */
		REPORTED_STATE("reportedState"),
		/**@since alpha-0.2.0 */
		SMBG("smbg"),
		/**@since alpha-0.2.0 */
		UPLOAD("upload"),
		/**@since alpha-0.2.0 */
		WATER("water"),
		/**@since alpha-0.2.0 */
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
