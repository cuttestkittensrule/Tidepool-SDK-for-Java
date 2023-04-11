package com.tidepool.tidepoolsdkandroid.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

/**
 * Updates the access token
 */
public class UpdateAccessToken implements Runnable {
	/** the output JSON */
	private JSONObject json;
	/** A {@link CountDownLatch} for waiting until the operation is complete */
	private final Optional<CountDownLatch> latch;
	private Optional<Boolean> succsess = Optional.empty();
	/** The {@link TidepoolBackendConfig} */
	private TidepoolBackendConfig cnf;

	/**
	 * Creates a runnable to update the acess token
	 * 
	 * @param latch the {@link CountDownLatch} to have counted down when the
	 *              runnable finishes
	 * @param cnf The {@link TidepoolBackendConfig configuration} to use for the request
	 */
	public UpdateAccessToken(CountDownLatch latch, TidepoolBackendConfig cnf) {
		this.latch = Optional.ofNullable(latch);
		this.cnf = cnf;
	}

	/**
	 * Creates a runnable to update the acess token without a {@link CountDownLatch}
	 * @param cnf The {@link TidepoolBackendConfig configuration} to use for the request
	 */
	public UpdateAccessToken(TidepoolBackendConfig cnf) {
		this(null, cnf);
	}

	/**
	 * Sends a HTTPS POST request to the selected Tidepool server, and gives the
	 * results to the given {@link TidepoolBackendConfig}.
	 * If given a {@link CountDownLatch}, the latch will count down when this
	 * finishes.
	 */
	@Override
	public void run() {
		if (cnf.refreshTokenExpired()) {
			System.err.println("The refresh token expired");
			return;
		}
		try {
			// TODO: Add parameter for the refresh token
			// the params for the POST
			String params = String.format("grant_type=refresh_token&client_id=%s", cnf.getClientID());
			// The url to POST to
			String full_URL = String.format("%s/realms/%s/protocol/openid-connect/token", cnf.getServerAddress(),
					cnf.getEnvironmentRealm());
			// creating a URL object with full_URL
			URL url = new URL(full_URL);

			// opening the connection
			HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
			urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlConnection.addRequestProperty("X-Tidepool-Session-Token", cnf.getRefreshToken());
			urlConnection.setDoOutput(true);

			// writing to the connection's stream (and closing it)
			try (OutputStream stream = urlConnection.getOutputStream()) {
				stream.write(params.getBytes());

				stream.flush();
			}

			// Handling responses

			int responseCode = urlConnection.getResponseCode();

			// if the response was ok (200)
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				// reading slowly instead of all at once just in case
				try (BufferedReader stream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = stream.readLine()) != null) {
						response.append(inputLine);
					}
					json = new JSONObject(response.toString());
					succsess = Optional.of(true);
					cnf.UpdateAcessToken(json);
				}
			} else if (responseCode == HttpsURLConnection.HTTP_BAD_REQUEST) {
				// reading slowly instead of all at once just in case
				try (BufferedReader stream = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = stream.readLine()) != null) {
						response.append(inputLine);
					}
					json = new JSONObject(response.toString());
					succsess = Optional.of(false);
					System.out.printf("The error code \"%s\" came up with a message of \"%s\"\n", json.getString("error"));
				}
			}

			cnf.UpdateAcessToken(json);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		latch.ifPresent((CountDownLatch latch) -> latch.countDown());
	}

	/**
	 * Sleeps the thread until this finishes running
	 */
	public void awaitLatch() {
		try {
			if (latch.isPresent()) {
				latch.get().await();
			} else {
				throw new IllegalStateException("Cannot wait for the CountDownLatch since there is no CountDownLatch");
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * A return value of an optional containing a value, it means that the json was set, and the value describes if it was a success.
	 * If the return value is an empy optional, than either execution hasnt't reached the point where the json is created
	 * @return if the response was a success, failure, or hasn't happened yet
	 */
	public Optional<Boolean> getSuccess() {
		return succsess;
	}
}
