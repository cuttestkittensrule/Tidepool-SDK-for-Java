package com.tidepool.tidepoolsdkjava.config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a configuration for interfacing with the tidepool backend
 * 
 * @since alpha-0.0.1
 */
public class TidepoolBackendConfig {
	/**
	 * The address of the associated server
	 * 
	 * @deprecated Use {@link Environment#getServerAddress()} instead
	 */
	@Deprecated(since = "alpha-0.2.0", forRemoval = true)
	private final String serverAddress;
	/**
	 * The client id of this application
	 * 
	 * @since alpha-0.0.1
	 */
	private final String client_id;
	/**
	 * The environment that is being interfaced with
	 * 
	 * @since alpha-0.0.1
	 */
	private final Environment env;

	/**
	 * The current access token
	 * 
	 * @since alpha-0.0.1
	 */
	private String currentAccessToken;
	/**
	 * The current refresh token
	 * 
	 * @since alpha-0.0.1
	 */
	private String currentRefreshToken;
	/**
	 * How long it takes from recieving the token does it expire in
	 * 
	 * @since alpha-0.0.1
	 */
	private int expiresIn;
	/**
	 * How long it takes from recieving the refresh token does it expire in
	 * 
	 * @since alpha-0.0.1
	 */
	private int refreshExpiresIn;
	/**
	 * When the current access token was recieved (with
	 * {@link System#currentTimeMillis()})
	 * 
	 * @since alpha-0.0.1
	 */
	private long recievedToken;

	/**
	 * Creates a configuration to access the tidepool backend.
	 * <strong>May not work properly if you put your own server address</strong>
	 * 
	 * @param env           The {@link Environment} that you are interfacing with
	 * @param client_id     The starting {@code client_id}
	 * @param serverAddress The address of the server that you are interfacing with
	 * @since alpha-0.0.1
	 * @deprecated Custom serverAddress not used in code anyways.
	 */
	@Deprecated(since = "alpha-0.2.0", forRemoval = true)
	public TidepoolBackendConfig(Environment env, String client_id, String serverAddress) {
		this.env = env;
		this.serverAddress = serverAddress;
		this.client_id = client_id;
	}

	/**
	 * Creates a configuration to access the tidepool backend.
	 * 
	 * @param env       The {@link Environment} that you are interfacing with
	 * @param client_id The starting {@code client_id}
	 * @since alpha-0.0.1
	 */
	public TidepoolBackendConfig(Environment env, String client_id) {
		this.env = env;
		this.serverAddress = null;
		this.client_id = client_id;
	}

	/**
	 * Gets the current {@link #currentAccessToken access token}
	 * 
	 * @return the current {@link #currentAccessToken access token}
	 * @since alpha-0.0.1
	 */
	public String getAccessToken() {
		return currentAccessToken;
	}

	/**
	 * Gets the current {@link #currentRefreshToken refresh token}
	 * 
	 * @return the current {@link #currentRefreshToken refresh token}
	 * @since alpha-0.0.1
	 */
	String getRefreshToken() {
		return currentRefreshToken;
	}

	/**
	 * @deprecated Use {@link Environment#getServerAddress()} instead
	 *             Gets the set server address
	 * @return the set server address
	 * @since alpha-0.0.1
	 */
	@Deprecated(since = "alpha-0.2.0", forRemoval = true)
	public String getServerAddress() {
		return serverAddress;
	}

	/**
	 * Gets the set {@link #env enviroment}
	 * 
	 * @return {@link #env enviroment}
	 * @since alpha-0.0.1
	 */
	public Environment getEnvironment() {
		return env;
	}

	/**
	 * Returns the realm string for the set enviroment
	 * 
	 * @return The Realm for the enviroment (ex. qa1)
	 * @since alpha-0.0.1
	 */
	public String getEnvironmentRealm() {
		return env.getRealm();
	}

	/**
	 * Gets the {@link #client_id}
	 * 
	 * @return the {@link #client_id}
	 * @since alpha-0.0.1
	 */
	public String getClientID() {
		return client_id;
	}

	/**
	 * Checks if the current token should be valid
	 * 
	 * @return {@code true} if the current token should be invalid
	 * @since alpha-0.0.1
	 */
	public boolean tokenRefreshRequired() {
		return System.currentTimeMillis() - recievedToken >= expiresIn * 1_000;
	}

	/**
	 * Checks if the current refresh token should be valid
	 * 
	 * @return {@code true} if the current refresh token should be invalid
	 * @since alpha-0.0.1
	 */
	public boolean refreshTokenExpired() {
		return System.currentTimeMillis() - recievedToken >= refreshExpiresIn * 1_000;
	}

	/**
	 * Updates the values related to the current {@link #currentAccessToken access
	 * token}
	 * 
	 * @param json the reply to the https POST request
	 * @since alpha-0.0.1
	 */
	void UpdateAcessToken(JSONObject json) {
		try {
			currentAccessToken = json.getString("acess_token");
			expiresIn = json.getInt("expires_in");
			currentRefreshToken = json.getString("refresh_token");
			refreshExpiresIn = Integer.parseInt(json.getString("refresh_expires_in"));
			recievedToken = System.currentTimeMillis();
		} catch (JSONException e) {
			throw new RuntimeException("Recieved invalid json", e);
		}
	}

	/**
	 * Updates the acess token using the refresh token
	 * 
	 * @since alpha-0.0.1
	 */
	public void UpdateAcessToken() {
		new UpdateAccessToken.Builder("refresh_token", this).build().run();
	}
}
