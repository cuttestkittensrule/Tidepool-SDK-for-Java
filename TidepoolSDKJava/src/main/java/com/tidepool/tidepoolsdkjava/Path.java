package com.tidepool.tidepoolsdkjava;

import java.util.Map;

/**
 * @deprecated Not necessarily needed, for removal. Use {@link String#format(String, Object...)} instead
 * Creates a path with placeholders for variables.<br>
 * Below is an example of how to use this class.
 * 
 * <pre>
 * 	Path path = new Path("/v1/clinics/{clinicId}/migrations/{userId}");
 * 	Map&lt;String, String&gt; pathArgs = new HashMap&lt;&gt;();
 * 	pathArgs.put("clinicId", "clinic-id-here");
 * 	pathArgs.put("userId", "user-id-here");
 * 	String completedPath = path.apply(pathArgs);
 * </pre>
 * @since alpha-0.1.0
 */
@Deprecated(since = "alpha-0.1.0", forRemoval = true)
public class Path {
	private String path;

	/**
	 * Creates a {@link Path}
	 * @param path The string path
	 */
	public Path(String path) {
		this.path = path;
	}

	/**
	 * Applies a map of Strings to the path.
	 * @param params The map of Strings to apply
	 * @return The applied String
	 * @exception IllegalArgumentException if there wasn't a key in the map that was supposed to be there
	 * @exception IllegalArgumentException if the defined path string was invalid
	 */
	public String apply(Map<String, String> params) {
		String result = "";
		String paramName = "";
		boolean param = false;
		for (int i = 0; i < path.length(); i++) {
			char chr = path.charAt(i);
			if (!param) {
				if (chr != '{') {
					param = true;
				} else {
					result += chr;
				}
			} else {
				if (chr != '}') {
					paramName += chr;
				} else if (chr == '{') {
					throw new IllegalArgumentException("Duplicate opening curly brace");
				} else {
					if (params.containsKey(paramName)) {
						result += params.get(paramName);
						paramName = "";
						param = false;
					} else {
						throw new IllegalArgumentException("params did not include an element with the key of " + paramName);
					}
				}
			}
		}
		if (param) {
			throw new IllegalArgumentException("Unmatched curly brace");
		}
		return result;
	}
}
