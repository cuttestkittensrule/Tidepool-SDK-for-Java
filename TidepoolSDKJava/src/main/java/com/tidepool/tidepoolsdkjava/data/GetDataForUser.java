package com.tidepool.tidepoolsdkjava.data;

import com.tidepool.tidepoolsdkjava.Constants;
import com.tidepool.tidepoolsdkjava.Path;
import com.tidepool.tidepoolsdkjava.PathArgs;
import com.tidepool.tidepoolsdkjava.Request;
import com.tidepool.tidepoolsdkjava.PathArgs.PathArgBuilder;
import com.tidepool.tidepoolsdkjava.config.TidepoolBackendConfig;

public class GetDataForUser extends Request {
	private PathArgBuilder pathArgs;
	private PathArgs pathArgConfig;
	public GetDataForUser(TidepoolBackendConfig cnf, String userId, PathArgBuilder pathArgs) {
		super(new Request.Builder(
				RequestType.GET,
				true,
				new Path("/data/{userId}"),
				cnf)
				.addPathArg("userId", userId)
				);
	}
	public PathArgs getPathArgConfig() {
		if (pathArgConfig != null) {
			return pathArgConfig;
		}
		PathArgs cnf = new PathArgs(
			"deviceId",
			"endDate",
			"latest",
			"startDate",
			"subType",
			"type",
			"uploadId",
			"carelink",
			"dexcom",
			"medtronic"
		);
		
		cnf.addValidRegex("deviceId", ".+");
		cnf.addValidRegex("endDate", Constants.ISO_8601_REGEX);
		cnf.addValidValue("latest", "true", "false");
		// cnf.addValidRegex("startDate", null);
		cnf.addValidValue(
			"type",
			"alert",
			"basal",
			"bloodKetone",
			"bolus",
			"cbg",
			"cgmsettings",
			"controllerSettings",
			"controllerStatus",
			"deviceEvent",
			"deviceStatus",
			"dosingDescription",
			"food",
			"insulin",
			"physicalActivity",
			"pumpSettings",
			"pumpStatus",
			"reportedState",
			"smbg",
			"upload",
			"water",
			"wizzard"
		);
		cnf.addValidRegex("uploadId", "^([0-9a-f]{32}|upid_[0-9a-f]{12})$");
		cnf.addValidValue("carelink", "true", "false");
		cnf.addValidValue("dexcom", "true", "false");
		cnf.addValidValue("medtronic", "true", "false");

		return cnf;
	}
}
