package tidepoolsdkandroid.config;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Represents a configuration for interfacing with the tidepool backend
 */
public class TidepoolBackendConfig {
	private final Server server;
	private final String client_id;
	private final Environment env;

	private String currentAccessToken;
	private String currentRefreshToken;
	private int expiresIn;
	private int refreshExpiresIn;
	private long recievedToken;

	/**
	 * Creates a configuration to access the tidepool backend.
	 * <strong>May not work properly if you put your own {@link Server}</strong>
	 * @param env The {@link Environment} that you are interfacing with
	 * @param client_id The starting {@code client_id}
	 * @param defaultServer The {@link Server} that you are interfacing with
	 */
	public TidepoolBackendConfig(Environment env, String client_id, Server defaultServer) {
		this.env = env;
		this.server = defaultServer;
		this.client_id = client_id;
	}

	/**
	 * Creates a configuration to access the tidepool backend.
	 * @param env The {@link Environment} that you are interfacing with
	 * @param client_id The starting {@code client_id}
	 */
	public TidepoolBackendConfig(Environment env, String client_id) {
		this(env, client_id, env.getDefaultServer());
	}

	/**
	 * Gets the current {@link #currentAccessToken access token}
	 * @return the current  {@link #currentAccessToken access token}
	 */
	public String getAccessToken() {
		return currentAccessToken;
	}

	/**
	 * Gets the current {@link #currentRefreshToken refresh token}
	 * @return the current {@link #currentRefreshToken refresh token}
	 */
	String getRefreshToken() {
		return currentRefreshToken;
	}

	/**
	 * Gets the set server address
	 * @return the set server address
	 */
	public String getServerAddress() {
		return server.getAddress();
	}

	/**
	 * Gets the set {@link #server}
	 * @return the set server {@link #server}
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Gets the set {@link #env enviroment}
	 * @return {@link #env enviroment}
	 */
	public Environment getEnvironment() {
		return env;
	}

	/**
	 * Returns the realm string for the set enviroment
	 * @return The Realm for the enviroment (ex. qa1)
	 */
	public String getEnvironmentRealm() {
		return env.getRealm();
	}

	/**
	 * Gets the {@link #client_id}
	 * @return the {@link #client_id}
	 */
	public String getClientID() {
		return client_id;
	}

	/**
	 * Checks if the current token should be valid
	 * @return {@code true} if the current token should be invalid
	 */
	public boolean tokenRefreshRequired() {
		return System.currentTimeMillis() - recievedToken >= expiresIn * 1_000;
	}

	/**
	 * Checks if the current refresh token should be valid
	 * @return {@code true} if the current refresh token should be invalid
	 */
	public boolean refreshTokenExpired() {
		return System.currentTimeMillis() - recievedToken >= refreshExpiresIn * 1_000;
	}

	/**
	 * Updates the values related to the current {@link #currentAccessToken access token}
	 * @param json the reply to the https POST request
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
	 */
	public void UpdateAcessToken() {
		new UpdateAccessToken(this).run();
	}
}
