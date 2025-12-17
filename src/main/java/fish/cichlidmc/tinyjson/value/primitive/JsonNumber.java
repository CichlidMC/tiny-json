package fish.cichlidmc.tinyjson.value.primitive;

import fish.cichlidmc.tinyjson.JsonException;
import fish.cichlidmc.tinyjson.value.JsonPrimitive;

import java.text.NumberFormat;
import java.util.Locale;

public final class JsonNumber extends JsonPrimitive<Double> {
	private static final NumberFormat formatter;

	static {
		formatter = NumberFormat.getNumberInstance(Locale.ROOT);
		formatter.setGroupingUsed(false);
		formatter.setMinimumFractionDigits(0);
		formatter.setMinimumIntegerDigits(1);
	}

	private final Double value;

	public JsonNumber(Double value) {
		this.value = value;
	}

	public JsonNumber(int i) {
		this((double) i);
	}

	public JsonNumber(long l) {
		this((double) l);
	}

	public JsonNumber(float f) {
		this((double) f);
	}

	@Override
	public Double value() {
		return this.value;
	}

	/// Get a wrapper for the value of this JsonNumber that validates that the value is valid for the desired type.
	/// For example, for a value of 1.1, calling intValue will throw.
	public Number strictValue() {
		return new StrictAccess();
	}

	@Override
	public JsonNumber copy() {
		return new JsonNumber(this.value);
	}

	@Override
	public JsonNumber asNumber() {
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof JsonNumber that && this.value.equals(that.value);
	}

	@Override
	public int hashCode() {
		return this.value.hashCode();
	}

	@Override
	public String toString() {
		return formatter.format(this.value.doubleValue());
	}

	public class StrictAccess extends Number {
		@Override
		public byte byteValue() {
			if (value.byteValue() == value) {
				return value.byteValue();
			}
			throw this.typeError("a byte");
		}

		@Override
		public short shortValue() {
			if (value.shortValue() == value) {
				return value.shortValue();
			}
			throw this.typeError("a short");
		}

		@Override
		public int intValue() {
			if (value.intValue() == value) {
				return value.intValue();
			}
			throw this.typeError("an int");
		}

		@Override
		public long longValue() {
			if (value.longValue() == value) {
				return value.longValue();
			}
			throw this.typeError("a long");
		}

		@Override
		public float floatValue() {
			if (value.floatValue() == value) {
				return value.floatValue();
			}
			throw this.typeError("a float");
		}

		@Override
		public double doubleValue() {
			return value;
		}

		private JsonException typeError(String type) {
			if (hasPath()) {
				return new JsonException(getPath() + " is not " + type);
			} else {
				return new JsonException("Not " + type);
			}
		}
	}
}
