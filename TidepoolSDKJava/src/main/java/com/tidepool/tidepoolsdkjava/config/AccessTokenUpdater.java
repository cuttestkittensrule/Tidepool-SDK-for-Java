package com.tidepool.tidepoolsdkjava.config;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.tidepool.tidepoolsdkjava.authentication.ObtainToken;

public class AccessTokenUpdater implements AutoCloseable {
	private static ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);

	private ObtainToken createRequest() {
		if (refreshToken != null && refreshTokenExpiry != null && Instant.now().isBefore(refreshTokenExpiry)) {
			ObtainToken.Builder builder = new ObtainToken.Builder("refresh_token", config);
			builder.setRefreshToken(refreshToken);
			ObtainToken runnable = builder.build();
			runnable.addOnFailureListener(this::onFailure);
			runnable.addOnSuccessListener(this::onSuccess);
			return runnable;
		} else {
			refreshToken = null;
			refreshTokenExpiry = null;
			// TODO: Deal with expiry better
			throw new IllegalStateException("Refresh Token Expired");
		}
	}

	public String getAccessToken() {
		if (accessTokenExpiry != null && Instant.now().isAfter(accessTokenExpiry)) {
			accessToken = null;
			accessTokenExpiry = null;
		}
		if (accessToken == null) {
			if (task.getDelay(TimeUnit.MILLISECONDS) > 500) {
				task.cancel(false);
				runnable = createRequest();
				executor.schedule(runnable, 0, TimeUnit.SECONDS);
			}
			try {
				runnable.awaitCompletion();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			} catch (IllegalStateException | NullPointerException e) {
				throw new IllegalStateException("Did not start background thread with the start method", e);
			}
		}
		return accessToken;
	}

	private ScheduledFuture<?> task;
	private ObtainToken runnable;
	private Instant lastAccess;

	private Instant accessTokenExpiry;
	private Instant refreshTokenExpiry;

	private String accessToken;
	private String refreshToken;

	private TidepoolBackendConfig config;

	/**
	 * Starts updating the access token
	 * 
	 * @param token
	 */
	public void start(TidepoolBackendConfig config, String refreshToken) {
		if (task != null) {
			task.cancel(true);
		}
		this.config = config;
		this.refreshToken = refreshToken;
		runnable = createRequest();
		task = executor.schedule(runnable, 0, TimeUnit.SECONDS);
	}

	private void onSuccess(int code) {
		JSONObject json = runnable.getJsonObject();
		Instant now = Instant.now();
		accessToken = json.getString("acess_token");
		long expires_in = json.getLong("expires_in");
		accessTokenExpiry = now.plusSeconds(expires_in);
		refreshToken = json.getString("refresh_token");
		long refresh_expires_in = json.getLong("refresh_expires_in");
		refreshTokenExpiry = now.plusSeconds(refresh_expires_in);

		long delay = expires_in - 2;

		if (lastAccess.plus(expires_in * 2, ChronoUnit.SECONDS).isAfter(now)) {
			delay = refresh_expires_in - 4;
		}

		if (delay < 0) {
			delay = 0;
		}

		runnable = createRequest();
		task = executor.schedule(runnable, delay, TimeUnit.SECONDS);
	}

	private void onFailure(int code) {
		// TODO: do meaningful things in here (like only close if it is unrecoverable)
		close();
	}

	@Override
	public void close() {
		task.cancel(true);
		task = null;
		runnable = null;
		lastAccess = null;
		accessTokenExpiry = null;
		refreshTokenExpiry = null;
		config = null;
	}
}
