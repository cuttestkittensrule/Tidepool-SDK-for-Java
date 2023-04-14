package com.tidepool.tidepoolsdkjava.config;


/**
 * The enviroment that you are in
 */
public enum Environment {
	/** dev */
	dev("dev1", "https://dev1.dev.tidepool.org"),
	/** qa2 */
	qa1("qa1", "https://qa1.development.tidepool.org"),
	/** qa1 */
	qa2("qa2", "https://qa2.development.tidepool.org"),
	/** integration */
	int_("integration", "https://external.integration"),
	/** default enviroment (tidepool) */
	prod("tidepool", "https://api.tidepool.org");
	private final String realm;
	private final String serverAddress;
	private Environment(String realm, String serverAddress) {
		this.realm = realm;
		this.serverAddress = serverAddress;
	}

	/**
	 * Gets the realm for the Environment
	 * @return the realm for the environment
	 */
	public String getRealm() {
		return realm;
	}

	/**
	 * Gets the default server address for this environment
	 * @return The default server address for this environment
	 */
	public String getServerAddress() {
		return serverAddress;
	}
}
