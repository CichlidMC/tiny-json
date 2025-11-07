package fish.cichlidmc.tinyjson.test.def;

import fish.cichlidmc.tinyjson.JsonException;
import fish.cichlidmc.tinyjson.TinyJson;
import fish.cichlidmc.tinyjson.test.framework.Group;
import fish.cichlidmc.tinyjson.test.framework.Resource;
import fish.cichlidmc.tinyjson.value.JsonValue;
import fish.cichlidmc.tinyjson.value.composite.JsonArray;
import fish.cichlidmc.tinyjson.value.primitive.JsonNumber;
import fish.cichlidmc.tinyjson.value.primitive.JsonString;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@Group("simple")
public final class SimpleTests {
	@Test
	public void singleInt(@Resource String text) {
		JsonValue parsed = TinyJson.parseOrThrow(text);
		assertEquals(new JsonNumber(1), parsed);
	}

	@Test
	public void singleString(@Resource String text) {
		JsonValue parsed = TinyJson.parseOrThrow(text);
		assertEquals(new JsonString("test string"), parsed);
	}

	@Test
	public void stringArray(@Resource String text) {
		assertEquals(
				JsonArray.of(
						new JsonString("first"),
						new JsonString("second")
				),
				TinyJson.parseOrThrow(text)
		);
	}

	@Test
	public void comicallyLargeNumber(@Resource String text) {
		JsonValue parsed = TinyJson.parseOrThrow(text);
		assertEquals(new JsonNumber(1e30), parsed);
	}

	@Test
	public void garbage(@Resource String text) {
		failToParse(text, "Could not parse value at line 1, col. 1");
	}

	@Test
	public void arrayTrailingComma(@Resource String text) {
		failToParse(text, "Expected value after comma at line 6, col. 1");
	}

	@Test
	public void objectTrailingComma(@Resource String text) {
		failToParse(text, "Expected entry after comma at line 8, col. 1");
	}

	@Test
	public void brokenString(@Resource String text) {
		failToParse(text, "Found linebreak interrupting string on line 2");
	}

	@Test
	public void missingColon(@Resource String text) {
		failToParse(text, "Expected colon before value at line 2, col. 7");
	}

	@Test
	public void unquotedString(@Resource String text) {
		failToParse(text, "Could not parse value at line 2, col. 3");
	}

	@Test
	public void arrayMissingComma(@Resource String text) {
		failToParse(text, "Expected comma before value at line 4, col. 4");
	}

	@Test
	public void objectMissingComma(@Resource String text) {
		failToParse(text, "Expected comma before entry at line 4, col. 4");
	}

	@Test
	public void incompleteObject(@Resource String text) {
		failToParse(text, "Unexpected EOF at line 3, col. 1");
	}

	private static void failToParse(String text, String errorMessage) {
		JsonException exception = assertThrowsExactly(JsonException.class, () -> TinyJson.parseOrThrow(text));
		assertEquals(errorMessage, exception.getMessage());
	}
}
