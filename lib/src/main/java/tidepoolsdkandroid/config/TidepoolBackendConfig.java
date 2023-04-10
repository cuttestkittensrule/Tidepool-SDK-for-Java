package tidepoolsdkandroid.config;

import org.json.JSONException;
import org.json.JSONObject;

public class TidepoolBackendConfig {
	private final Server server;
	private final String client_id;
	private final Environment env;

	private String currentAcessToken;
	private String currentRefreshToken;
	private int expiresIn;
	private int refreshExpiresIn;
	private long recievedToken;

	/**
	 * Creates a configuration to acess the tidepool backend 
	 * @param env
	 * @param client_id
	 * @param defaultServer
	 */
	public TidepoolBackendConfig(Environment env, String client_id, Server defaultServer) {
		this.env = env;
		this.server = defaultServer;
		this.client_id = client_id;
	}

	public TidepoolBackendConfig(Environment env, String client_id) {
		this(env, client_id, env.getDefaultServer());
	}

	public String getAcessToken() {
		return currentAcessToken;
	}

	public String getRefreshToken() {
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
	public Environment getEnviroment() {
		return env;
	}

	/**
	 * Returns the realm string for the set enviroment
	 * @return The Realm for the enviroment (ex. qa1)
	 */
	public String getEnviromentRealm() {
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
	 * @return
	 */
	public boolean refreshTokenExpired() {
		return System.currentTimeMillis() - recievedToken >= refreshExpiresIn * 1_000;
	}

	void UpdateAcessToken(JSONObject json) {
		try {
			currentAcessToken = json.getString("acess_token");
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
		new UpdateAcessToken(this).run();
	}
}
