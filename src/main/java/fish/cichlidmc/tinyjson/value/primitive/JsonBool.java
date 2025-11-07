package fish.cichlidmc.tinyjson.value.primitive;

import fish.cichlidmc.tinyjson.value.JsonPrimitive;

public final class JsonBool extends JsonPrimitive<Boolean> {
	private final Boolean value;

	public JsonBool(Boolean value) {
		this.value = value;
	}

	public JsonBool(boolean value) {
		this(Boolean.valueOf(value));
	}

	@Override
	public Boolean value() {
		return this.value;
	}

	@Override
	public JsonBool copy() {
		return new JsonBool(this.value);
	}

	@Override
	public JsonBool asBoolean() {
		return this;
	}

	@Override
	public String toString() {
		return this.value.toString();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof JsonBool that && this.value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}
}
