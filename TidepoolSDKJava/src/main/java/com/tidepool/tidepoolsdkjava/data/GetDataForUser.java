package com.tidepool.tidepoolsdkjava.data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.tidepool.tidepoolsdkjava.BaseRequest;
import com.tidepool.tidepoolsdkjava.BodylessRequest;
import com.tidepool.tidepoolsdkjava.Constants;
import com.tidepool.tidepoolsdkjava.config.TidepoolBackendConfig;

/**
 * A request that queries the backend for data.
 * 
 * @see <a href=
 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user">stoplight
 *      Docs</a>
 * @see BaseRequest
 * @since alpha-0.2.0
 */
public class GetDataForUser extends BodylessRequest {
	/**
	 * Used to create a {@link GetDataForUser} request
	 * 
	 * @see <a href=
	 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Request">stoplight
	 *      docs</a>
	 */
	public static class Builder {
		/**
		 * The backend configuration
		 * 
		 * @since alpha-0.2.0
		 */
		private TidepoolBackendConfig cnf;
		/**
		 * The arguments for the query
		 * 
		 * @since alpha-0.2.0
		 */
		private Map<String, List<Object>> queryArgs;
		/**
		 * The user Id
		 * 
		 * @sice alpha-0.2.0
		 */
		private final String userId;

		/**
		 * Creates a builder for {@link GetDataForUser}
		 * 
		 * @param cnf    The backend configuration to use
		 * @param userId The user id
		 * @since alpha-0.2.0
		 * @see <a href=
		 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Query-Parameters">stoplight
		 *      docs</a>
		 */
		public Builder(TidepoolBackendConfig cnf, String userId) {
			this.cnf = cnf;
			this.userId = userId;
			queryArgs = new HashMap<>();
		}

		/**
		 * Sets the device id
		 * 
		 * @param deviceId The device id
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href=
		 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Query-Parameters">stoplight
		 *      docs</a>
		 */
		public Builder setDeviceId(String deviceId) {
			put("deviceId", deviceId);
			return this;
		}

		/**
		 * Sets the end date
		 * 
		 * @param endDate The end date
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href=
		 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Query-Parameters">stoplight
		 *      docs</a>
		 */
		public Builder setEndDate(ZonedDateTime endDate) {
			put("endDate", endDate);
			return this;
		}

		/**
		 * Sets the latest boolean
		 * 
		 * @param latest The latest boolean
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href=
		 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Query-Parameters">stoplight
		 *      docs</a>
		 */
		public Builder setLatest(boolean latest) {
			put("latest", latest);
			return this;
		}

		/**
		 * Sets the start date
		 * 
		 * @param startDate The start date
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href=
		 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Query-Parameters">stoplight
		 *      docs</a>
		 */
		public Builder setStartDate(ZonedDateTime startDate) {
			put("startDate", startDate);
			return this;
		}

		//TODO: add subType (requires additional research)

		/**
		 * Sets the types of data
		 * @param types The types of data
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href=
		 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Query-Parameters">stoplight
		 *      docs</a>
		 */
		public Builder setTypes(Constants.uploadType... types) {
			put("type", (Object[]) types);
			return this;
		}

		/**
		 * Sets the upload id
		 * @param uploadId The upload id
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href=
		 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Query-Parameters">stoplight
		 *      docs</a>
		 */
		public Builder setUploadId(String uploadId) {
			put("uploadId", uploadId);
			return this;
		}

		/**
		 * Sets the carelink boolean
		 * @param carelink The carelink boolean
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href=
		 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Query-Parameters">stoplight
		 *      docs</a>
		 */
		public Builder setCarelink(boolean carelink) {
			put("carelink", carelink);
			return this;
		}
		/**
		 * Sets the dexcom boolean
		 * @param dexcom The dexcom boolean
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href=
		 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Query-Parameters">stoplight
		 *      docs</a>
		 */
		public Builder setDexcom(boolean dexcom) {
			put("carelink", dexcom);
			return this;
		}
		/**
		 * Sets the medtronic boolean
		 * @param medtronic The medtronic boolean
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href=
		 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/e8fd4582725ef-get-data-for-user#Query-Parameters">stoplight
		 *      docs</a>
		 */
		public Builder setMedtronic(boolean medtronic) {
			put("carelink", medtronic);
			return this;
		}

		/**
		 * Puts a key and some set values into the parameters
		 * 
		 * @param key    The key
		 * @param values The value(s)
		 * @since alpha-0.2.0
		 */
		private void put(String key, Object... values) {
			List<Object> args = new ArrayList<>();
			for (Object arg : values) {
				args.add(arg);
			}
			queryArgs.put(key, args);
		}

		public GetDataForUser build() {
			return new GetDataForUser(this);
		}
	}

	/**
	 * The userId
	 * 
	 * @since alpha-0.2.0
	 */
	private final String userId;

	/**
	 * protected constructor that uses builder
	 * 
	 * @param builder The builder
	 * @since alpha-0.2.0
	 */
	protected GetDataForUser(Builder builder) {
		super(new HashMap<>(), builder.cnf, builder.queryArgs);
		userId = builder.userId;
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected String getURI() {
		return String.format("/data/@s", userId);
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected RequestType getRequestType() {
		return RequestType.GET;
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected boolean requiresSessionToken() {
		return true;
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected void parseResponse(String response) {
		jsonArray = new JSONArray(response);
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	public JSONArray getJsonArray() {
		return super.getJsonArray();
	}

	/**
	 * @since alpha-2.1.0
	 */
	@Override
	protected String accept() {
		return "application/json";
	}
}
