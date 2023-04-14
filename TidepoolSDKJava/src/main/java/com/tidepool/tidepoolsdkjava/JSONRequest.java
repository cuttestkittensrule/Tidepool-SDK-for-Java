package com.tidepool.tidepoolsdkjava;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;
import com.tidepool.tidepoolsdkjava.config.Environment;
import com.tidepool.tidepoolsdkjava.PathArgs.PathArgBuilder;
import com.tidepool.tidepoolsdkjava.Request.RequestStatus;
import com.tidepool.tidepoolsdkjava.Request.RequestType;
import com.tidepool.tidepoolsdkjava.config.TidepoolBackendConfig;

public class JSONRequest implements Runnable {
	public static class Builder {
		// Required Parameters
		private RequestType requestType;
		private boolean sessionTokenHeader;
		private Path path;
		private TidepoolBackendConfig cnf;

		// Optional Parameters
		private Map<String, String> pathArgs = new HashMap<>();
		private Map<Environment, String> envMap = Request.getBaseMap();

		public Builder(RequestType requestType, Path path, TidepoolBackendConfig cnf) {
			this.requestType = requestType;
			this.path = path;
			this.cnf = cnf;
		}

		public Builder addPathArg(String name, String value) {
			pathArgs.put(name, value);
			return this;
		}

		public Builder setEnvMap(Map<Environment, String> envMap) {
			this.envMap = envMap;
			return this;
		}

		public JSONRequest build() {
			return new JSONRequest(this);
		}
	}

	// Required Parameters
	private final RequestType requestType;
	private final boolean sessionTokenHeader;
	private final Path path;
	private final TidepoolBackendConfig cnf;

	// Optional Parameters
	private final Map<String, String> pathArgs;
	private final Map<Environment, String> envMap;


	// Results
	private JSONObject json;

	// One JSON type must be set, but not both.
	/**
	 * The body will be a {@link JSONObject}.
	 * Exculsive from {@link #jsonArrParam}
	 * (so if this is set, {@link #jsonArrParam} must not be)
	 */
	private Optional<JSONObject> jsonObjParam = Optional.empty();
	/**
	 * The body will be a {@link JSONArray}.
	 * Exculsive from {@link #jsonObjParam}
	 * (so if this is set, {@link #jsonObjParam} must not be)
	 */
	private Optional<JSONArray> jsonArrParam = Optional.empty();
	/**
	 * The status of the request
	 */
	private RequestStatus status = RequestStatus.NotStarted;
	/**
	 * Private constructor.
	 * call {@link Builder#build()} if you want to create
	 * a {@link JSONRequest}
	 * @param builder the builder that contains all the options for the request
	 */
	private JSONRequest(Builder builder) {
		requestType = builder.requestType;
		sessionTokenHeader = builder.sessionTokenHeader;
		path = builder.path;
		cnf = builder.cnf;
		pathArgs = builder.pathArgs;
		envMap = builder.envMap;
	}

	/**
	 * Set the JSON parameter of the body to be a {@link JSONObject}
	 * @param jsonParam The body of the HTTPS request
	 */
	public void setJsonParam(JSONObject jsonParam) {
		jsonObjParam = Optional.of(jsonParam);
	}

	/**
	 * Set the JSON parameter of the body to be a {@link JSONArray}
	 * @param jsonParam The body of the HTTPS request
	 */
	public void setJsonParam(JSONArray jsonParam) {
		jsonArrParam = Optional.of(jsonParam);
	}

	@Override
	public void run() {
		// Updating status
		status = RequestStatus.InProgress;
		if (!(jsonArrParam.isPresent() ^ jsonObjParam.isPresent())) {
			status = RequestStatus.IllegalState;
			return;
		}
		// Getting the address of the request
		String serverAddress = envMap.get(cnf.getEnvironment());
		String urlPath = path.apply(pathArgs);
		String pathArgs = "";
		PathArgBuilder pathArgBuilder = getPathArgBuilder();
		if (pathArgBuilder != null) {
			pathArgs = pathArgBuilder.build();
		}
		String full_path = serverAddress + urlPath + pathArgs;
		try {
			// Creating URL object
			URL url = new URL(full_path);

			// Opening the connection
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.setRequestMethod(requestType.getCode());

			// Setting it to output
			con.setDoOutput(true);
			if (sessionTokenHeader)
				con.addRequestProperty("X-Tidepool-Session-Token", cnf.getAccessToken());

			// writing the correct JSON
			try (OutputStream stream = con.getOutputStream()) {
				String params;
				if (jsonArrParam.isPresent()) {
					params = jsonArrParam.get().toString();
				} else {
					params = jsonObjParam.get().toString();
				}
				stream.write(params.getBytes());
				stream.flush();
			}

			// Handling Responses
			int responseCode = con.getResponseCode();

			if (responseCode == HttpsURLConnection.HTTP_OK) {
				// reading slowly instead of all at once just in case
				try (BufferedReader stream = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = stream.readLine()) != null) {
						response.append(inputLine);
					}
					json = new JSONObject(response.toString());
				}
				status = RequestStatus.Success;
			} else {
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

	public RequestStatus getStatus() {
		return status;
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

	public JSONObject getJSON() {
		return json;
	}
}
