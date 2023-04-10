package tidepoolsdkandroid.config;

public enum Server {
	dev("https://auth.dev.tidepool.org"),
	qa("https://auth.qa2.tidepool.org"),
	ext("https://auth.external.tidepool.org"),
	prod("https://auth.tidepool.org");
	private final String address;
	private Server(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}
}
