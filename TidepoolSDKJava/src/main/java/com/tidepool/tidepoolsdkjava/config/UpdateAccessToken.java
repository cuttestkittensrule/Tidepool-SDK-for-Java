package com.tidepool.tidepoolsdkjava.config;

import com.tidepool.tidepoolsdkjava.authentication.ObtainToken;

/**
 * Gets new access tokens
 * 
 * @since alpha-0.2.0
 */
final class UpdateAccessToken extends ObtainToken {
	public UpdateAccessToken(TidepoolBackendConfig cnf) {
		super(new ObtainToken.Builder("refresh_token", cnf).setRefreshToken(cnf.getRefreshToken()));
	}
}
