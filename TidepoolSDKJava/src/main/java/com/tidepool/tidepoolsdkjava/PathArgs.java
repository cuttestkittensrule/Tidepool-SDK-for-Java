package com.tidepool.tidepoolsdkjava;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Arguments added to the end of the path.
 * (ex: ?argName=argValue&amp;argName=argValue
 * or ?argName=argValue,secondArgValue&amp;argName=argValue
 * @deprecated This will be replaced by structures in Request's builders
 * @since alpha-0.1.0
 */
@Deprecated(since = "alpha-0.2.0", forRemoval = true)
public class PathArgs {
	private final List<String> validArgs;
	private Map<String, List<String>> validValues;
	private Map<String, Pattern> validRegexs;

	public PathArgs(List<String> validArgs) {
		this.validArgs = validArgs;
		validValues = new HashMap<>();
	}

	public PathArgs(String... validArgs) {
		this(Arrays.asList(validArgs));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof PathArgs) {
			PathArgs p = (PathArgs) o;
			return validArgs.equals(p.validArgs) && validValues.equals(p.validValues) && validRegexs.equals(p.validRegexs);
		}
		return false;
	}

	public void addValidValue(String name, String... values) {
		addValidValue(name, Arrays.asList(values));
	}

	public void addValidValue(String name, List<String> values) {
		if (setValid(name)) {
			throw new IllegalArgumentException("There are allready valid values for this");
		}
		validValues.put(name, values);
	}

	public void addValidRegex(String name, String regex) {
		if (setValid(name)) {
			throw new IllegalArgumentException("There are allready valid values for this");
		}
		try {
		validRegexs.put(name, Pattern.compile(regex));
		} catch (PatternSyntaxException e) {
			String message = "The given regex(" + regex + ") was invalid";
			throw new IllegalArgumentException(message, e);
		}
	}

	/**
	 * Checks if there are valid values set for the specified argument 
	 * @param name the argument you want to search for
	 * @return {@code true} if there are valid values set for the specified argument
	 */
	private boolean setValid(String name) {
		return validValues.containsKey(name) || validRegexs.containsKey(name);
	}

	/**
	 * Checks if the given arguments and values are valid
	 * @param name the argument
	 * @param values the values
	 * @return {@code true} if the arguments and values are all valid
	 */
	public boolean validArgs(String name, String... values) {
		if (validArgs.contains(name)) {
			if (validValues.containsKey(name)) {
				for (String value : values) {
					if (!validValues.get(name).contains(value)) {
						return false;
					}
				}
			} else if (validRegexs.containsKey(name)) {
				Pattern p = validRegexs.get(name);
				for (String value : values) {
					Matcher m = p.matcher(value);
					if (!m.matches()) {
						return false;
					}
				} 
			}
			return true;
		}
		return false;
	}

	public class PathArgBuilder {
		private Map<String, List<String>> args = new HashMap<>();

		public PathArgBuilder with(String name, String... values) {
			Objects.requireNonNull(name, "name must not be null");
			Objects.requireNonNull(values, "values must not be null");
			if (!validArgs(name, values)) {
				throw new IllegalArgumentException("Name and value were not valid");
			} else if (args.containsKey(name)) {
				throw new IllegalArgumentException("There is allready a value for this");
			}
			args.put(name, Arrays.asList(values));
			return this;
		}

		public String build() {
			String result = "?";
			for (Map.Entry<String, List<String>> pair : args.entrySet()) {
				result += pair.getKey();
				result += "=";
				for (String str : pair.getValue()) {
					result += str;
					result += ",";
				}
				result = result.substring(0, result.length() - 1);
				result += "&";
			}
			result = result.substring(0, result.length() - 1);
			return result;
		}
	}
}
