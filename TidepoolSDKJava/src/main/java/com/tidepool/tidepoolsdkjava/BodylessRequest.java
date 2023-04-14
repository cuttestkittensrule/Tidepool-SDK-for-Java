package com.tidepool.tidepoolsdkjava;

import java.util.List;
import java.util.Map;

import com.tidepool.tidepoolsdkjava.config.TidepoolBackendConfig;

/**
 * A request that doesn't have a body
 * 
 * @since alpha-0.2.0
 */
public abstract class BodylessRequest extends BaseRequest {
	/**
	 * Creates a bodyless request
	 * @param headerArgs Things to put in the HTTPS header. Keys are the name of the
	 *                   header, value is the value
	 * @param cnf The backend configuration to use
	 * @param queryArgs The query arguments (for none make it an empy map)
	 */
	protected BodylessRequest(Map<String, Object> headerArgs, TidepoolBackendConfig cnf,
			Map<String, List<Object>> queryArgs) {
		super(headerArgs, cnf, queryArgs);
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected final boolean getDoOutput() {
		return false;
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected String getContentType() {
		return "application/json";
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected final byte[] getPackage() {
		return null;
	}
}
