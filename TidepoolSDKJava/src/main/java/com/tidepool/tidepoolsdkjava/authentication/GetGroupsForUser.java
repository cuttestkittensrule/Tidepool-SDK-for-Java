package com.tidepool.tidepoolsdkjava.authentication;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONObject;

import com.tidepool.tidepoolsdkjava.BodylessRequest;
import com.tidepool.tidepoolsdkjava.config.TidepoolBackendConfig;


/*
	TODO: If and/or when the current (4/25/2023) Authentication category is deprecated, and removed from the docs, replace with new name
*/
/**
 * A Request that gets all users that a user with the given {@code userId} can
 * see.
 * 
 * @see <a href=
 *      "https://tidepool.stoplight.io/docs/tidepool-full-api/ee1592b7e4a48-get-groups-for-user">stoplight
 *      docs</a>
 * @since alpha-2.1.0
 */
public class GetGroupsForUser extends BodylessRequest {
	private final String userId;

	/**
	 * Creates a {@link GetGroupsForUser} request
	 * 
	 * @param cnf    The {@link TidepoolBackendConfig backend configuration} to use
	 * @param userId The user id
	 * @since alpha-2.1.0
	 */
	public GetGroupsForUser(TidepoolBackendConfig cnf, String userId) {
		super(new HashMap<>(), cnf, new HashMap<>());
		this.userId = userId;
	}

	/**
	 * @since alpha-2.1.0
	 */
	@Override
	protected String getURI() {
		return String.format("/access/groups/%s", userId);
	}

	/**
	 * @since alpha-2.1.0
	 */
	@Override
	protected RequestType getRequestType() {
		return RequestType.GET;
	}

	/**
	 * @since alpha-2.1.0
	 */
	@Override
	protected boolean requiresSessionToken() {
		return true;
	}

	/**
	 * @since alpha-2.1.0
	 */
	@Override
	protected void parseResponse(String response) {
		jsonObject = new JSONObject(response);
	}

	/**
	 * Gets all userIds that you have the "note" premission for
	 * 
	 * @return All userIds that you have the "note" premission for
	 * @since alpha-2.1.0
	 */
	public Set<String> getWithNotePerm() {
		Set<String> result = new HashSet<>();
		for (String key : getJsonObject().keySet()) {
			JSONObject obj = jsonObject.getJSONObject(key);
			if (obj.has("note")) {
				result.add(key);
			}
		}
		return result;
	}

	/**
	 * Gets all userIds that you have the "upload" premission for
	 * 
	 * @return All userIds that you have the "upload" premission for
	 * @since alpha-2.1.0
	 */
	public Set<String> getWithUploadPerm() {
		Set<String> result = new HashSet<>();
		for (String key : getJsonObject().keySet()) {
			JSONObject obj = jsonObject.getJSONObject(key);
			if (obj.has("upload")) {
				result.add(key);
			}
		}
		return result;
	}

	/**
	 * Gets all userIds that you have the "view" premission for
	 * 
	 * @return All userIds that you have the "view" premission for
	 * @since alpha-2.1.0
	 */
	public Set<String> getWithViewPerm() {
		Set<String> result = new HashSet<>();
		for (String key : getJsonObject().keySet()) {
			JSONObject obj = jsonObject.getJSONObject(key);
			if (obj.has("view")) {
				result.add(key);
			}
		}
		return result;
	}

	/**
	 * @since alpha-2.1.0
	 */
	@Override
	public JSONObject getJsonObject() {
		return super.getJsonObject();
	}

	@Override
	protected String accept() {
		return "application/json";
	}

}
