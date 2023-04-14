package com.tidepool.tidepoolsdkjava.config;

/**
 * The enviroment that you are in
 * 
 * @since alpha-0.0.1
 */
public enum Environment {
	/**
	 * dev
	 * 
	 * @since alpha-0.1.0
	 */
	dev("dev1", "https://dev1.dev.tidepool.org"),
	/**
	 * qa1
	 * 
	 * @since alpha-0.1.0
	 */
	qa1("qa1", "https://qa1.development.tidepool.org"),
	/**
	 * qa2
	 * 
	 * @since alpha-0.1.0
	 */
	qa2("qa2", "https://qa2.development.tidepool.org"),
	/**
	 * integration
	 * 
	 * @since alpha-0.1.0
	 */
	int_("integration", "https://external.integration"),
	/**
	 * production (default environment)
	 * 
	 * @since alpha-0.1.0
	 */
	prod("tidepool", "https://api.tidepool.org");

	/**
	 * The realm id of this environment
	 * 
	 * @since alpha-0.0.1
	 */
	private final String realm;
	/**
	 * The address of the server associated with this environment
	 * 
	 * @since alpha-0.1.0
	 */
	private final String serverAddress;

	/**
	 * Creates an environment
	 * 
	 * @param realm         The realm id of this environment
	 * @param serverAddress The address of the associated server
	 * @since alpha-0.1.0
	 */
	private Environment(String realm, String serverAddress) {
		this.realm = realm;
		this.serverAddress = serverAddress;
	}

	/**
	 * Gets the realm for the Environment
	 * 
	 * @return the realm for the environment
	 * @since alpha-0.0.1
	 */
	public String getRealm() {
		return realm;
	}

	/**
	 * Gets the default server address for this environment
	 * 
	 * @return The default server address for this environment
	 * @since alpha-0.1.0
	 */
	public String getServerAddress() {
		return serverAddress;
	}
}
