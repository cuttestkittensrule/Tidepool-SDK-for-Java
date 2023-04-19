package com.tidepool.tidepoolsdkjava;

import java.util.List;
import java.util.Map;

import com.tidepool.tidepoolsdkjava.config.TidepoolBackendConfig;

/**
 * A request that has the "key=value&amp;key=value" as it's body
 * 
 * @since alpha-0.2.0
 */
public abstract class UrlEncodedRequest extends BaseRequest {
	/**
	 * The parameters for the body
	 * @since alpha-0.2.0
	 */
	private final Map<String, String> bodyParams;
	/**
	 * creates a UrlEncodedRequest
	 * 
	 * @param headerArgs Things to put in the HTTPS header. Keys are the name of the
	 *                   header, value is the value
	 * @param cnf        The backend configuration to use
	 * @param queryArgs  The query arguments (for none make it an empy map)
	 * @param bodyParams The keys and values to put into the body
	 * @since alpha-0.2.0
	 */
	public UrlEncodedRequest(Map<String, Object> headerArgs, TidepoolBackendConfig cnf,
			Map<String, List<Object>> queryArgs, Map<String, String> bodyParams) {
		super(headerArgs, cnf, queryArgs);
		this.bodyParams = bodyParams;
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected final boolean doesOutput() {
		return true;
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected final String contentType() {
		return "application/x-www-form-urlencoded";
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected final byte[] getPackage() {
		String result = "";
		for (Map.Entry<String, String> pair : bodyParams.entrySet()) {
			result = result.concat(pair.getKey());
			result = result.concat("=");
			result = result.concat(pair.getValue());
			result = result.concat("&");
		}
		result = result.substring(0, result.length() - 1);
		return result.getBytes();
	}
}
