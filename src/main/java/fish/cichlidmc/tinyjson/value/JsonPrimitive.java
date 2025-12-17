package fish.cichlidmc.tinyjson.value;

import fish.cichlidmc.tinyjson.value.primitive.JsonBool;
import fish.cichlidmc.tinyjson.value.primitive.JsonNull;
import fish.cichlidmc.tinyjson.value.primitive.JsonNumber;
import fish.cichlidmc.tinyjson.value.primitive.JsonString;

/// A JsonPrimitive is a JsonValue that holds some direct value and not other JsonValues.
public abstract sealed class JsonPrimitive<T> extends JsonValue permits JsonString, JsonNumber, JsonBool, JsonNull {
	/// Value held by this JSON value. Always non-null UNLESS this is a JsonNull.
	public abstract T value();
}
