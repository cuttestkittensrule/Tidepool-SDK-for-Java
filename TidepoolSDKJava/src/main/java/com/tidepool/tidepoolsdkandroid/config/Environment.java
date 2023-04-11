package com.tidepool.tidepoolsdkandroid.config;

/**
 * The enviroment that you are in
 */
public enum Environment {
	/** dev */
	dev("dev1", Server.dev),
	/** qa2 */
	qa1("qa1", Server.qa),
	/** qa1 */
	qa2("qa2", Server.qa),
	/** integration */
	int_("integration", Server.ext),
	/** default enviroment (tidepool) */
	prod("tidepool", Server.prod);
	private final String realm;
	private final Server defaultServer;
	private Environment(String realm, Server defaultServer) {
		this.realm = realm;
		this.defaultServer = defaultServer;
	}

	/**
	 * Gets the realm for the Environment
	 * @return the realm for the environment
	 */
	public String getRealm() {
		return realm;
	}

	/**
	 * Gets the default {@link Server} for this environment
	 * @return The default {@link Server} for this environment
	 */
	public Server getDefaultServer() {
		return defaultServer;
	}
}
