package fish.cichlidmc.tinyjson.value.primitive;

import fish.cichlidmc.tinyjson.value.JsonPrimitive;

public final class JsonString extends JsonPrimitive<String> {
	private final String value;

	public JsonString(String value) {
		this.value = value;
	}

	@Override
	public String value() {
		return this.value;
	}

	@Override
	public JsonString copy() {
		return new JsonString(this.value);
	}

	@Override
	public JsonString asString() {
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof JsonString that && this.value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public String toString() {
		return '"' + this.value + '"';
	}
}
