package com.tidepool.tidepoolsdkjava;

public class Constants {
	/**
	 * A Regulaar Expression that matches an ISO-8601 formatted time and date string
	 * in UTC time
	 */
	public static final String ISO_8601_REGEX = "[0-9]{4}-(0[0-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(0[0-9]|1[0-9]|2[0-4]):(0[0-9]|[1-5][0-9]):(0[0-9]|[1-5][0-9]|60)Z";
	/**
	 * A Regulaar Expression that matches an ISO-8601 formatted time and date string
	 * in any time zone
	 * <strong>Warning: will match time zone "-00:00", even though it is invalid</strong>
	 */
	public static final String ISO_8601_REGEX_ANY = "[0-9]{4}-(0[0-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T(0[0-9]|1[0-9]|2[0-4]):(0[0-9]|[1-5][0-9]):(0[0-9]|[1-5][0-9]|60)(Z|(+[0-9]{2}:[0-9]{2}|-([0-9]{2})(?(?=00)([0-9](?(?=0)[1-9]|[0-9])|:[0-9]{2})))";
}
