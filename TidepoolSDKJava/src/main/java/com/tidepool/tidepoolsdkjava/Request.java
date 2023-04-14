package com.tidepool.tidepoolsdkjava;

import com.tidepool.tidepoolsdkjava.PathArgs.PathArgBuilder;
import com.tidepool.tidepoolsdkjava.config.Environment;
import com.tidepool.tidepoolsdkjava.config.TidepoolBackendConfig;

import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * A request to the tidepool backend.
 * Usually subclassed, so that the code can be easily called
 */
public class Request implements Runnable {
	public enum RequestStatus {
		Success,
		Failure,
		InProgress,
		NotStarted,
		IllegalState,
		IllegalArguments,
		ExceptionRaised
	}

	private static Map<Environment, String> BASE_MAP;

	public static enum RequestType {
		/** https GET request */
		GET("GET", false),
		/** https POST request */
		POST("POST", true),
		/** https HEAD request */
		HEAD("HEAD", true),
		/** https OPTIONS request */
		OPTIONS("OPTIONS", true),
		/** https PUT request */
		PUT("PUT", true),
		/** https DELETE request */
		DELETE("DELETE", true),
		/** https TRACE request */
		TRACE("TRACE", true),
		/**
		 * https PATCH request. <br>
		 * <strong>Probably won't work</strong> (see {@link HttpsURLConnection#setRequestMethod(String)})
		 */
		PATCH("PATCH", true);

		private String code;
		private boolean doOutput;

		private RequestType(String code, boolean doOutput) {
			this.code = code;
		}

		public boolean getDoOutput() {
			return doOutput;
		}

		/**
		 * Gets the HTTPS code for the request type
		 * 
		 * @return the HTTPS code for the request type
		 */
		public String getCode() {
			return code;
		}

		/**
		 * Gets the {@link RequestType} with the given {@link String} as it's HTTPS
		 * code.
		 * 
		 * @param code The HTTPS code to look for a enum constant with.
		 * @return the {@link RequestType} with the given {@link String} as it's HTTPS
		 *         code, or {@code null} if there wasn't one.
		 */
		public static RequestType getById(String code) {
			for (RequestType type : RequestType.values()) {
				if (type.code == code) {
					return type;
				}
			}
			return null;
		}
	}

	public static String getParams(Map<String, String> params) {
		String result = "";
		for (Map.Entry<String, String> pair : params.entrySet()) {
			result.concat(pair.getKey());
			result.concat("=");
			result.concat(pair.getValue());
			result.concat("&");
		}

		if (result.length() > 0) {
			result = result.substring(0, result.length() - 2);
		}
		return result;
	}

	public static class Builder {
		// Required Values
		private RequestType requestType;
		private boolean sessionTokenHeader;
		private Path path;
		private TidepoolBackendConfig cnf;

		// Optional Values
		private Map<String, String> bodyArgs = new HashMap<>();
		private Map<Environment, String> environmentMap = Request.getBaseMap();
		private Map<String, List<String>> queryParams = new LinkedHashMap<>();

		public Builder(RequestType requestType, boolean sessionTokenHeader, Path path, TidepoolBackendConfig cnf) {
			this.requestType = requestType;
			this.sessionTokenHeader = sessionTokenHeader;
			this.path = path;
			this.cnf = cnf;
		}

		/**
		 * To add an argument to the body of the request
		 * @param arg the argument
		 * @param value the argument value
		 * @return {@code this} to allow chaining
		 */
		public Builder addBodyArg(String arg, String value) {
			bodyArgs.put(arg, value);
			return this;
		}

		/**
		 * To set the argMap
		 * @param arg the argument
		 * @return {@code this} to allow chaining
		 */
		public Builder setBodyArgs(Map<String, String> argMap) {
			bodyArgs = argMap;
			return this;
		}

		/**
		 * Map the environments to custom servers
		 * @param envMap A map that maps Environments to server addresses
		 * @return {@code this} to allow chaining
		 */
		public Builder setEnvMap(Map<Environment, String> envMap) {
			environmentMap = envMap;
			return this;
		}

		public Builder addQueryParam(String name, List<String> value) {
			queryParams.put(name, value);
			return this;
		}

		public Builder addQueryParam(String name, String... value) {
			return addQueryParam(name, Arrays.asList(value));
		}

		public Request build() {
			return new Request(this);
		}

	}

	// Required Values
	private RequestType requestType;
	private boolean sessionTokenHeader;
	private Path path;
	private TidepoolBackendConfig cnf;

	// Optional Values
	private Map<String, String> args;
	private Map<Environment, String> environmentMap;
	private Map<String, List<String>> queryParams;

	protected Request(Builder builder) {
		requestType = builder.requestType;
		sessionTokenHeader = builder.sessionTokenHeader;
		path = builder.path;
		cnf = builder.cnf;
		args = builder.bodyArgs;
		environmentMap = builder.environmentMap;
		queryParams = builder.queryParams;
	}

	private RequestStatus status = RequestStatus.NotStarted;
	private JSONObject json;

	@Override
	public void run() {
		// Updating status
		status = RequestStatus.InProgress;
		// Piecing together the parts of the URL
		String serverAddress = environmentMap.get(cnf.getEnvironment());
		String urlPath = path.apply(new HashMap<>());
		// Getting the full URL
		String full_url = serverAddress + buildUrl(urlPath, queryParams);
		// Getting the parameters that will be passed
		String params = getParams(args);
		try {
			// Creating the URL object
			URL url = new URL(full_url);

			// Opening the connection
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod(requestType.getCode());

			// Setting if it outputs
			con.setDoOutput(requestType.doOutput);
			// If specified, adds the session token to the header
			if (sessionTokenHeader)
				con.addRequestProperty("X-Tidepool-Session-Token", cnf.getAccessToken());
			
			con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			
			// if needed, writes to the stream.
			if (requestType.doOutput) {
				try (OutputStream stream = con.getOutputStream()) {
					stream.write(params.getBytes());
					stream.flush();
				}
			}

			// Handling Responses
			int responseCode = con.getResponseCode();

			// If the request succsedded
			if (responseCode == HttpsURLConnection.HTTP_OK) {
				// reading slowly instead of all at once just inv case
				try (BufferedReader stream = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = stream.readLine()) != null) {
						response.append(inputLine);
					}
					json = new JSONObject(response.toString());
				}
				status = RequestStatus.Success;
			} else { // Something went wrong
				// reading slowly instead of all at once just in case
				try (BufferedReader stream = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = stream.readLine()) != null) {
						response.append(inputLine);
					}
					json = new JSONObject(response.toString());
				}
				status = RequestStatus.Failure;
			}

		} catch (IOException e) {
			status = RequestStatus.ExceptionRaised;
		}

	}

	private static String buildUrl(String path, Map<String, List<String>> queryParams) {
		String result = "?";
		for (Map.Entry<String, List<String>> pair : queryParams.entrySet()) {
			result += pair.getKey();
			result += "=";
			for (String str : pair.getValue()) {
				result += str;
				result += ",";
			}
			result = result.substring(0, result.length() - 1);
			result += "&";
		}
		return path + result.substring(0, result.length() - 1);
	}

	private static void populateBaseMap() {
		BASE_MAP = new HashMap<>();
		for (Environment env : Environment.values()) {
			BASE_MAP.put(env, env.getRealm());
		}
	}

	public static Map<Environment, String> getBaseMap() {
		if (BASE_MAP == null) {
			populateBaseMap();
		}
		return BASE_MAP;
	}

	/**
	 * Returns the {@link PathArgs.PathArgBuilder PathArgBuilder}
	 * to add to the end of the link
	 * Note: Subclasses should make a public static version of this so that it can be used to create {@link PathArgs.PathArgBuilder builders}
	 * @return The {@link PathArgs} instance for this {@link Request}. <strong>May be null</strong>
	 */
	protected PathArgBuilder getPathArgBuilder() {
		// There will be a nullity check.
		// By default, pathArgs are disabled,
		// Override to return your PathArgs
		return null;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public JSONObject getJSON() {
		return json;
	}
}
