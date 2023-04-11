package tidepoolsdkandroid.config;

/**
 * The link to various backend servers
 */
public enum Server {
	/** Development */
	dev("https://auth.dev.tidepool.org"),
	/** QA */
	qa("https://auth.qa2.tidepool.org"),
	/** External */
	ext("https://auth.external.tidepool.org"),
	/** Production */
	prod("https://auth.tidepool.org");
	/** The URL to this server */
	private final String address;
	/**
	 * Creates a {@link Server}
	 * @param address the URL to this {@link Server}
	 */
	private Server(String address) {
		this.address = address;
	}

	/**
	 * Gets the URL to this server
	 * @return The URL to this {@link Server}
	 */
	public String getAddress() {
		return address;
	}
}
