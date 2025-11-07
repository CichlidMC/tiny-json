package fish.cichlidmc.tinyjson.value.primitive;

import fish.cichlidmc.tinyjson.value.JsonPrimitive;

public final class JsonNull extends JsonPrimitive<Object> {
	@Override
	public Object value() {
		return null;
	}

	@Override
	public JsonNull copy() {
		return new JsonNull();
	}

	@Override
	public JsonNull asNull() {
		return this;
	}

	@Override
	public String toString() {
		return "null";
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof JsonNull;
	}

	@Override
	public int hashCode() {
		return 0;
	}
}
