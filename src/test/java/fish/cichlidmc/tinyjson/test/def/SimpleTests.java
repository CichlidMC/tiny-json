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
		JsonException exception = assertThrowsExactly(JsonException.class, () -> TinyJson.parseOrThrow(text));
		assertEquals("Could not parse value at line 1, col. 1", exception.getMessage());
	}

	@Test
	public void arrayTrailingComma(@Resource String text) {
		JsonException exception = assertThrowsExactly(JsonException.class, () -> TinyJson.parseOrThrow(text));
		assertEquals("Expected value after comma at line 6, col. 1", exception.getMessage());
	}

	@Test
	public void objectTrailingComma(@Resource String text) {
		JsonException exception = assertThrowsExactly(JsonException.class, () -> TinyJson.parseOrThrow(text));
		assertEquals("Expected entry after comma at line 8, col. 1", exception.getMessage());
	}

	@Test
	public void brokenString(@Resource String text) {
		JsonException exception = assertThrowsExactly(JsonException.class, () -> TinyJson.parseOrThrow(text));
		assertEquals("Found linebreak interrupting string on line 2", exception.getMessage());
	}

	@Test
	public void missingColon(@Resource String text) {
		JsonException exception = assertThrowsExactly(JsonException.class, () -> TinyJson.parseOrThrow(text));
		assertEquals("Expected colon before value at line 2, col. 7", exception.getMessage());
	}

	@Test
	public void unquotedString(@Resource String text) {
		JsonException exception = assertThrowsExactly(JsonException.class, () -> TinyJson.parseOrThrow(text));
		assertEquals("Could not parse value at line 2, col. 3", exception.getMessage());
	}

	@Test
	public void arrayMissingComma(@Resource String text) {
		JsonException exception = assertThrowsExactly(JsonException.class, () -> TinyJson.parseOrThrow(text));
		assertEquals("Expected comma before value at line 4, col. 4", exception.getMessage());
	}

	@Test
	public void objectMissingComma(@Resource String text) {
		JsonException exception = assertThrowsExactly(JsonException.class, () -> TinyJson.parseOrThrow(text));
		assertEquals("Expected comma before entry at line 4, col. 4", exception.getMessage());
	}

	@Test
	public void incompleteObject(@Resource String text) {
		JsonException exception = assertThrowsExactly(JsonException.class, () -> TinyJson.parseOrThrow(text));
		assertEquals("Unexpected EOF at line 3, col. 1", exception.getMessage());
	}
}
