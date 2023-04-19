package com.tidepool.tidepoolsdkjava.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.tidepool.tidepoolsdkjava.UrlEncodedRequest;

/**
 * Gets new access tokens
 * 
 * @since alpha-0.2.0
 */
public final class UpdateAccessToken extends UrlEncodedRequest {
	/**
	 * Used to create an UpdateAccessToken request
	 * 
	 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token">stoplight docs</a>
	 * @since alpha-0.2.0
	 */
	public static class Builder {
		/**
		 * The backend configuration
		 * 
		 * @since alpha-0.2.0
		 */
		private final TidepoolBackendConfig cnf;
		/**
		 * The parameters for the body
		 * 
		 * @since alpha-0.2.0
		 */
		private Map<String, String> bodyParams;

		/**
		 * Creates a Builder for {@link UpdateAccessToken}
		 * @param grantType The grant type
		 * @param cnf The backend configuration
		 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token">stoplight docs</a>
		 * @since alpha-0.2.0
		 */
		public Builder(String grantType, TidepoolBackendConfig cnf) {
			this.cnf = cnf;
			if (grantType != "refresh_token" && grantType != "authorization_code"
					&& grantType != "urn:ietf:params:oauth:grant-type:token-exchange") {
				throw new IllegalArgumentException(
						"Expected \"refresh_token\" or \"authorization_code\" or \"urn:ietf:params:oauth:grant-type:token-exchange\", but was: \""
								+ grantType + "\"");
			}
			bodyParams.put("grant_type", "grant_type");
			bodyParams.put("client_id", cnf.getClientID());

		}

		/**
		 * Sets The code
		 * @param code The code
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token#request-body">stoplight docs</a>
		 */
		public Builder setCode(String code) {
			bodyParams.put("code", code);
			return this;
		}

		/**
		 * Sets the code verifier
		 * @param codeverifier The code verifier
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token#request-body">stoplight docs</a>
		 */
		public Builder setCodeVerifier(String codeverifier) {
			bodyParams.put("code_verifier", codeverifier);
			return this;
		}

		/**
		 * Sets the client secret
		 * @param secret The client secret
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token#request-body">stoplight docs</a>
		 */
		public Builder setClientSecret(String secret) {
			bodyParams.put("client_secret", secret);
			return this;
		}

		/**
		 * Sets the subject token
		 * @param subjectToken The subject token
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token#request-body">stoplight docs</a>
		 */
		public Builder setSubjectToken(String subjectToken) {
			bodyParams.put("subject_token", subjectToken);
			return this;
		}

		/**
		 * Sets the subject token type
		 * @param subjectTokenType the subject token type
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token#request-body">stoplight docs</a>
		 */
		public Builder setSubjectTokenType(String subjectTokenType) {
			if (subjectTokenType != "urn:ietf:params:oauth:token-type:access_token"
					&& subjectTokenType != "urn:ietf:params:oauth:token-type:jwt") {
				throw new IllegalArgumentException(
						"Expected \"urn:ietf:params:oauth:token-type:access_token\" or \"urn:ietf:params:oauth:token-type:access_token\", but was: \""
								+ subjectTokenType + "\"");
			}
			bodyParams.put("subject_token_type", subjectTokenType);
			return this;
		}

		/**
		 * Sets the requested token type
		 * @param requestedTokenType The requested token type
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token#request-body">stoplight docs</a>
		 */
		public Builder setRequestedTokenType(String requestedTokenType) {
			if (requestedTokenType != "urn:ietf:params:oauth:token-type:access_token"
					&& requestedTokenType != "urn:ietf:params:oauth:token-type:refresh_token") {
				throw new IllegalArgumentException(
						"Expected \"urn:ietf:params:oauth:token-type:access_token\" or \"urn:ietf:params:oauth:token-type:refresh_token\", but was: \""
								+ requestedTokenType + "\"");
			}
			bodyParams.put("requested_token_type", requestedTokenType);
			return this;
		}

		/**
		 * Sets the subject Issuer
		 * @param subjectIssuer The subject issuer
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token#request-body">stoplight docs</a>
		 */
		public Builder setSubjectIssuer(String subjectIssuer) {
			bodyParams.put("subject_issuer", subjectIssuer);
			return this;
		}

		/**
		 * Puts the current refresh token into the parameters
		 * @return {@code this} for chaining
		 * @since alpha-0.2.0
		 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token#request-body">stoplight docs</a>
		 */
		public Builder putRefreshToken() {
			bodyParams.put("refresh_token", cnf.getRefreshToken());
			return this;
		}

		/**
		 * Builds this into an {@link UpdateAccessToken} request
		 * @return an {@link UpdateAccessToken} request
		 * @since alpha-0.2.0
		 */
		public UpdateAccessToken build() {
			return new UpdateAccessToken(this);
		}
	}

	/**
	 * Creates an {@link UpdateAccessToken} request
	 * @param builder the builder to create this with
	 * @since alpha-0.2.0
	 */
	private UpdateAccessToken(Builder builder) {
		super(new HashMap<>(), builder.cnf, new HashMap<>(), builder.bodyParams);
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected String getURI() {
		return String.format("/realms/%s/protocol/openid-connect/token", cnf.getEnvironmentRealm());
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected RequestType getRequestType() {
		return RequestType.POST;
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected boolean requiresSessionToken() {
		return false;
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected void parseResponse(String response) {
		jsonObject = new JSONObject(response);
	}

	/**
	 * @since alpha-0.2.0
	 */
	@Override
	protected Map<Environment, String> getEnvMap() {
		Map<Environment, String> result = new HashMap<>();
		result.put(Environment.dev, "https://auth.dev.tidepool.org");
		result.put(Environment.int_, "https://auth.external.tidepool.org");
		result.put(Environment.prod, "https://auth.tidepool.org");
		result.put(Environment.qa1, "https://auth.qa2.tidepool.org");
		result.put(Environment.qa2, "https://auth.qa2.tidepool.org");
		return Collections.unmodifiableMap(result);
	}

	/**
	 * @since alpha-0.2.0
	 * @see <a href="https://tidepool.stoplight.io/docs/tidepool-full-api/094dd3fd48c42-obtain-token#request-body">stoplight docs</a>
	 */
	@Override
	public JSONObject getJsonObject() {
		return super.getJsonObject();
	}

	/**
	 * Returns {@code true} if the refresh token hasn't expired
	 * @since alpha-1.0.0
	 */
	@Override
	protected boolean startingCondition() {
		return !cnf.refreshTokenExpired();
	}

	/**
	 * @since alpha-2.1.0
	 */
	@Override
	protected String accept() {
		return "application/json";
	}

}
