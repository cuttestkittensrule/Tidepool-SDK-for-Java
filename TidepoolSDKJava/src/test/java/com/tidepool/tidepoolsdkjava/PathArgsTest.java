package com.tidepool.tidepoolsdkjava;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.*;

public class PathArgsTest {
	static PathArgs arguments;
	@BeforeClass
	public static void initialize() {
		List<String> validArgs = Arrays.asList(
			"deviceID",
			"endData",
			"latest",
			"type",
			"subtype",
			"uploadId"
		);
		arguments = new PathArgs(validArgs);
		arguments.addValidValue("latest", "true", "false");
	}
	@Test(expected = IllegalArgumentException.class)
	public void duplicateTest() {
		arguments.new PathArgBuilder().with("endData", "blah", "blah2").with("endData", "duplicate").build();
	}

	@Test
	public void correctOutputTest() {
		String actual = arguments.new PathArgBuilder().with("endData", "blah").with("uploadId", "0123456789abcdef").build();
		String expected = "?uploadId=0123456789abcdef&endData=blah";
		assertEquals("The outputs of the tests were not the same.", expected, actual);
	}

	@Test
	public void correctMultipleOutputTest() {
		String actual = arguments.new PathArgBuilder().with("endData", "blah", "hah").with("uploadId", "0123456789abcdef").build();
		String expected = "?uploadId=0123456789abcdef&endData=blah,hah";
		assertEquals("The outputs of the tests were not the same.", expected, actual);
	}
}
