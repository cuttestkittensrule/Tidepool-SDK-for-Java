package tidepoolsdkandroid.config;

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
	def("tidepool", Server.prod);
	private final String realm;
	private final Server defaultServer;
	private Environment(String realm, Server defaultServer) {
		this.realm = realm;
		this.defaultServer = defaultServer;
	}

	/** get the string code for the realm */
	public String getRealm() {
		return realm;
	}

	/** gets the default server for this enviroment */
	public Server getDefaultServer() {
		return defaultServer;
	}
}
