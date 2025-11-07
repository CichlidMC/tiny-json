package fish.cichlidmc.tinyjson.test.tests;

import fish.cichlidmc.tinyjson.test.framework.Group;
import fish.cichlidmc.tinyjson.test.framework.Test;
import fish.cichlidmc.tinyjson.value.composite.JsonArray;
import fish.cichlidmc.tinyjson.value.primitive.JsonNumber;
import fish.cichlidmc.tinyjson.value.primitive.JsonString;
import fish.cichlidmc.tinyjson.value.JsonValue;

@Group("simple")
public class SimpleJsonTests {
	@Test
	public static final JsonValue SINGLE_INT = new JsonNumber(1);
	@Test
	public static final JsonValue SINGLE_STRING = new JsonString("test string");
	@Test
	public static final JsonValue STRING_ARRAY = JsonArray.of(new JsonString("first"), new JsonString("second"));
	@Test
	public static final JsonValue COMICALLY_LARGE_NUMBER = new JsonNumber(1e30);

	@Test(fails = true)
	public static final String GARBAGE = "Could not parse value at line 1, col. 1";
	@Test(fails = true)
	public static final String ARRAY_TRAILING_COMMA = "Expected value after comma at line 6, col. 1";
	@Test(fails = true)
	public static final String OBJECT_TRAILING_COMMA = "Expected entry after comma at line 8, col. 1";
	@Test(fails = true)
	public static final String BROKEN_STRING = "Found linebreak interrupting string on line 2";
	@Test(fails = true)
	public static final String MISSING_COLON = "Expected colon before value at line 2, col. 7";
	@Test(fails = true)
	public static final String UNQUOTED_STRING = "Could not parse value at line 2, col. 3";
	@Test(fails = true)
	public static final String ARRAY_MISSING_COMMA = "Expected comma before value at line 4, col. 4";
	@Test(fails = true)
	public static final String OBJECT_MISSING_COMMA = "Expected comma before entry at line 4, col. 4";
	@Test(fails = true)
	public static final String INCOMPLETE_OBJECT = "Unexpected EOF at line 3, col. 1";
}
