package com.tidepool.tidepoolsdkjava;

import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import javax.net.ssl.HttpsURLConnection;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URI;
import java.net.URISyntaxException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.tidepool.tidepoolsdkjava.config.Environment;
import com.tidepool.tidepoolsdkjava.config.TidepoolBackendConfig;

/**
 * Will superclass all requests
 * 
 * @since alpha-0.2.0
 */
abstract public class BaseRequest implements Runnable {
	/**
	 * Represents the status of the request
	 * 
	 * @since alpha-0.2.0
	 */
	public enum RequestStatus {
		/**
		 * Signifies that the request has resulted in a succesess, and that
		 * {@link BaseRequest#parseResponse(String)} has been called
		 * 
		 * @since alpha-0.2.0
		 */
		Success,
		/**
		 * Signifies that the request has resulted in a failure, and that
		 * {@link BaseRequest#parseResponse(String)} has been called
		 * 
		 * @since alpha-0.2.0
		 */
		Failure,
		/**
		 * Signifies that the request is in progress
		 * 
		 * @since alpha-0.2.0
		 */
		InProgress,
		/**
		 * Signifies that the request hasn't started
		 * 
		 * @since alpha-0.2.0
		 */
		NotStarted,
		/**
		 * Signifies that the request is currently in an illegal state
		 * 
		 * @since alpha-0.2.0
		 */
		IllegalState,
		/**
		 * Signifies that the arguments given are illegal
		 * 
		 * @since alpha-0.2.0
		 */
		IllegalArguments,
		/**
		 * Signifies that an exception was raised, and the request has exited
		 * 
		 * @since alpha-0.2.0
		 */
		ExceptionRaised,
		/**
		 * Signifies that the starting condition didn't pass
		 * 
		 * @since alpha-0.2.0
		 */
		StartingConditionFailed
	}

	public static enum RequestType {
		/**
		 * https GET requst
		 * 
		 * @since alpha-0.2.0
		 */
		GET("GET"),
		/**
		 * https POST request
		 * 
		 * @since alpha-0.2.0
		 */
		POST("POST"),
		/**
		 * https HEAD request
		 * 
		 * @since alpha-0.2.0
		 */
		HEAD("HEAD"),
		/**
		 * https OPTIONS request
		 * 
		 * @since alpha-0.2.0
		 */
		OPTIONS("OPTIONS"),
		/**
		 * https PUT request
		 * 
		 * @since alpha-0.2.0
		 */
		PUT("PUT"),
		/**
		 * https DELETE request
		 * 
		 * @since alpha-0.2.0
		 */
		DELETE("DELETE"),
		/**
		 * https Trace request
		 * 
		 * @since alpha-0.2.0
		 */
		TRACE("TRACE"),
		/**
		 * https PATCH request.
		 * 
		 * @since alpha-0.2.0
		 */
		PATCH("PATCH");

		/**
		 * The request code
		 * 
		 * @since alpha-0.2.0
		 */
		private final String code;

		/**
		 * Creates a {@link RequestType}
		 * 
		 * @param code The request code
		 * @since alpha-0.2.0
		 */
		private RequestType(String code) {
			this.code = code;
		}

		/**
		 * Gets the HTTPS code for the request type
		 * 
		 * @return the HTTPS code for the request type
		 * @since alpha-0.2.0
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
		 * @since alpha-0.2.0
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

	/**
	 * Creates a BaseRequest with things required for the instances
	 * 
	 * @param headerArgs Things to put in the HTTPS header. Keys are the name of the
	 *                   header, value is the value
	 * @param cnf        The backend configuration to use
	 * @param queryArgs  The arguments for the query, if applicable. make empty if
	 *                   not applicable
	 * @since alpha-0.2.0
	 */
	protected BaseRequest(Map<String, Object> headerArgs, TidepoolBackendConfig cnf,
			Map<String, List<Object>> queryArgs) {
		this.cnf = cnf;
		this.queryArgs = queryArgs;
		this.headerArgs = headerArgs;
	}

	/**
	 * The things to put into the header*
	 * *Not including Content-Type, or the session ID,
	 * those are automatically put in.
	 * The objects will be automatically turned into strings
	 * <br>
	 * Individual to each instance
	 * 
	 * @since alpha-0.2.0
	 */
	private final Map<String, Object> headerArgs;

	/**
	 * The configuration of the backend.<br>
	 * Individual to each instance
	 * 
	 * @since alpha-0.2.0
	 */
	public final TidepoolBackendConfig cnf;

	/**
	 * The arguments for everything after the question mark.
	 * The Objects will be turned into strings.
	 * 
	 * @since alpha-0.2.0
	 */
	private final Map<String, List<Object>> queryArgs;

	/**
	 * A {@link CountDownLatch} that counts down to zero on exiting the runnable
	 * 
	 * @since alpha-0.2.0
	 */
	private CountDownLatch latch;

	/**
	 * If the result is a {@link JSONObject},
	 * this should contain the result
	 * 
	 * @since alpha-0.2.0
	 */
	protected JSONObject jsonObject;

	/**
	 * If the result is a {@link JSONArray},
	 * this should contain the result
	 * 
	 * @since alpha-0.2.0
	 */
	protected JSONArray jsonArray;

	/**
	 * The current status of the request
	 * 
	 * @since alpha-0.2.0
	 */
	private RequestStatus status = RequestStatus.NotStarted;

	/**
	 * Called to get the environment map
	 * 
	 * @return A map mapping the environment to the server address
	 * @since alpha-0.2.0
	 */
	protected Map<Environment, String> getEnvMap() {
		Map<Environment, String> result = new HashMap<>();
		for (Environment env : Environment.values()) {
			result.put(env, env.getServerAddress());
		}
		return Collections.unmodifiableMap(result);
	}

	/**
	 * Put in a custom starting condition
	 * 
	 * @return {@code true} if the starting condition passes
	 */
	protected boolean startingCondition() {
		return true;
	}

	/**
	 * This code runs when the starting condition fails
	 */
	protected void onStartingConditionFail() {
	}

	/**
	 * Should return the URI for a singular request
	 * 
	 * @return The URI for a request
	 * @since alpha-0.2.0
	 */
	abstract protected String getURI();

	/**
	 * Should return the {@link RequestType} of a singular request
	 * 
	 * @return the {@link RequestType} of this request
	 * @since alpha-0.2.0
	 */
	abstract protected RequestType getRequestType();

	/**
	 * Should return if a certain type of request does an output
	 * 
	 * @return if this type of request does output
	 * @since alpha-0.2.0
	 */
	abstract protected boolean getDoOutput();

	/**
	 * Should return the Content Type of the body for a type of request
	 * 
	 * @return The string representation of the content type.
	 * @since alpha-0.2.0
	 */
	abstract protected String getContentType();

	/**
	 * Should return if a request requires the session token in the header
	 * 
	 * @return {@code true} if the request requires a session token
	 * @since alpha-0.2.0
	 */
	abstract protected boolean requiresSessionToken();

	private Consumer<Integer> onSuccsessListeners = (l) -> {};
	private Consumer<Integer> onFailureListeners = (l) -> {};

	/**
	 * Adds a {@link Consumer} that is called when the result of the request is a succsess.
	 * The {@link Integer} passed in is the response code
	 * @param onSuccsessListener The {@link Consumer} that is called on a succsess
	 */
	public void addOnSuccessListener(Consumer<Integer> onSuccsessListener) {
		onSuccsessListeners = onSuccsessListeners.andThen(onSuccsessListener);
	}

	/**
	 * Adds a {@link Consumer} that is called when the result of the request is a failure.
	 * The {@link Integer} passed in is the response code
	 * @param onSuccsessListener The {@link Consumer} that is called on a falure
	 */
	public void addOnFailureListener(Consumer<Integer> onFailureListener) {
		onFailureListeners = onFailureListeners.andThen(onFailureListener);
	}

	/**
	 * Called with the string response, use this to parse the response from the
	 * backend
	 * 
	 * @param response The response from the backend, in string form
	 * @since alpha-0.2.0
	 */
	abstract protected void parseResponse(String response);

	/**
	 * Determines if the request was a succsess
	 * 
	 * @param responseCode The response code
	 * @return {@code true} if it should be handled as a succsess
	 * @since alpha-0.2.0
	 */
	protected boolean isSuccsess(int responseCode) {
		return responseCode == HttpsURLConnection.HTTP_OK;
	}

	/**
	 * Gets the package of the body
	 * 
	 * @return The body to send to the backend
	 * @since alpha-0.2.0
	 */
	abstract protected byte[] getPackage();

	private static final int timeoutDurationSeconds = 30;
	private static final Duration timeoutDuration = Duration.ofSeconds(timeoutDurationSeconds);

	/**
	 * Waits until the request is finished.
	 * 
	 * Times out after {@value #timeoutDurationSeconds} seconds
	 * 
	 * @throws InterruptedException
	 * @throws IllegalStateException When this is called before running this request
	 * @return {@code true} if the request finished or {@code false} if the timeout
	 *         duration elapsed before the request finished
	 * @since alpha-0.2.0
	 */
	public final boolean awaitCompletion() throws InterruptedException {
		return awaitCompletion(timeoutDuration);
	}

	/**
	 * Waits until the request is finished.
	 * Times out after the given time
	 * 
	 * @param waitTime the duration to wait before timing out
	 * @throws InterruptedException
	 * @throws IllegalStateException When this is called before running this request
	 * @return {@code true} if the request finished or {@code false} if the timeout
	 *         duration elapsed before the request finished
	 * @since alpha-0.2.0
	 */
	public final boolean awaitCompletion(Duration waitTime) throws InterruptedException {
		if (latch == null) {
			throw new IllegalStateException("The Request hasn't been started");
		}
		return latch.await(waitTime.toMillis(), TimeUnit.MILLISECONDS);
	}

	/**
	 * Returns the {@link JSONArray} from the response.
	 * Note: Override to make public if the response is a {@link JSONArray}
	 * 
	 * @return the {@link JSONArray} from the response
	 * @since alpha-0.2.0
	 */
	protected JSONArray getJsonArray() {
		return jsonArray;
	}

	/**
	 * Returns the {@link JSONObject} from the response.
	 * Note: Override to make public if the response is a {@link JSONObject}
	 * 
	 * @return the {@link JSONObject} from the response
	 * @since alpha-0.2.0
	 */
	protected JSONObject getJsonObject() {
		return jsonObject;
	}

	/**
	 * Runs the request.
	 * 
	 * @since alpha-0.2.0
	 */
	@Override
	public void run() {
		latch = new CountDownLatch(1);
		// Getting the request status set up
		status = RequestStatus.InProgress;
		if (!startingCondition()) {
			status = RequestStatus.StartingConditionFailed;
			onStartingConditionFail();
			latch.countDown();
			return;
		}
		
		// Getting the url set up;
		String serverURL = getEnvMap().get(cnf.getEnvironment());
		String URI = getURI();
		String queryParams = generateQueryParams(queryArgs);
		String full_url = serverURL + URI + queryParams;

		try {
			// Creating the URI object
			URI uri = new URI(full_url);

			HttpRequest.Builder builder = HttpRequest.newBuilder(uri).header("Content-Type", getContentType());
			if (requiresSessionToken()) {
				builder = builder.header("X-Tidepool-Session-Token", cnf.getAccessToken());
			}

			for (Map.Entry<String, Object> pair : headerArgs.entrySet()) {
				builder = builder.header(pair.getKey(), pair.getValue().toString());
			}

			HttpRequest request = builder.method(getRequestType().getCode(),
					getDoOutput() ? HttpRequest.BodyPublishers.ofByteArray(getPackage())
							: HttpRequest.BodyPublishers.noBody())
					.build();

			HttpResponse<String> response = HttpClient.newHttpClient().send(request,
					HttpResponse.BodyHandlers.ofString());

			parseResponse(response.body());
			if (isSuccsess(response.statusCode())) {
				status = RequestStatus.Success;
				onSuccsessListeners.accept(response.statusCode());
			} else {
				status = RequestStatus.Failure;	
				onFailureListeners.accept(response.statusCode());
			}
			latch.countDown();

		} catch (IOException | URISyntaxException | InterruptedException e) {
			status = RequestStatus.ExceptionRaised;
			latch.countDown();
			throw new RuntimeException("An exception was raised during excecution", e);
		}

	}

	/**
	 * Gets the status of the request
	 * 
	 * @return The status of the request
	 * @since alpha-0.2.0
	 */
	public RequestStatus getStatus() {
		return status;
	}

	/**
	 * Generates everything after the question mark
	 * 
	 * @param params The query params
	 * @return A string for the given params
	 * @since alpha-0.2.0
	 */
	protected static final String generateQueryParams(Map<String, List<Object>> params) {
		String result = "?";
		for (Map.Entry<String, List<Object>> pair : params.entrySet()) {
			result += pair.getKey();
			result += "=";
			for (Object str : pair.getValue()) {
				result += str.toString();
				result += ",";
			}
			result = result.substring(0, result.length() - 1);
			result += "&";
		}
		return result.substring(0, result.length() - 1);
	}
}
