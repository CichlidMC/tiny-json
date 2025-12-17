package fish.cichlidmc.tinyjson;

import fish.cichlidmc.tinyjson.value.JsonValue;

/// Exception that may be thrown when managing JSON. This may be thrown for a parsing error or
/// for a deserialization error (such as calling [JsonValue#asObject()] on a non-object)
public final class JsonException extends RuntimeException {
	public JsonException(String message) {
		super(message);
	}

	/// Create a new JsonException with the given value as context. The path is added to the message.
	/// Format: ${path}: ${message}
	public JsonException(JsonValue value, String message) {
		this(value.getPath() + ": " + message);
	}
}
