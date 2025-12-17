package fish.cichlidmc.tinyjson.value.composite;

import fish.cichlidmc.tinyjson.JsonException;
import fish.cichlidmc.tinyjson.value.JsonValue;
import fish.cichlidmc.tinyjson.value.primitive.JsonBool;
import fish.cichlidmc.tinyjson.value.primitive.JsonNull;
import fish.cichlidmc.tinyjson.value.primitive.JsonNumber;
import fish.cichlidmc.tinyjson.value.primitive.JsonString;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public final class JsonArray extends JsonValue implements Iterable<JsonValue> {
	private final List<JsonValue> values = new ArrayList<>();

	public static JsonArray of(JsonValue... values) {
		JsonArray array = new JsonArray();
		array.addAll(values);
		return array;
	}

	public JsonArray add(int i, JsonValue value) {
		this.setChildPath(i, value);
		this.values.add(i, value);
		return this;
	}

	// utilities for adding types directly
	public JsonArray add(JsonValue value) 	{ return this.add(this.size(), value); 		}
	public JsonArray add(boolean value) 	{ return this.add(new JsonBool(value)); 	}
	public JsonArray addNull() 				{ return this.add(new JsonNull()); 			}
	public JsonArray add(double value) 		{ return this.add(new JsonNumber(value));	}
	public JsonArray add(String value) 		{ return this.add(new JsonString(value));	}

	public JsonArray addAll(Iterable<JsonValue> iterable) {
		iterable.forEach(this::add);
		return this;
	}

	public JsonArray addAll(JsonValue... values) {
		for (JsonValue value : values) {
			this.add(value);
		}
		return this;
	}

	public JsonValue remove(int i) {
		JsonValue value = this.values.get(i);
		value.setPath(null);
		return value;
	}

	public JsonValue get(int i) {
		if (i < 0 || i >= this.size()) {
			throw new JsonException(this.makePath(i) + " is out of range");
		}
		return this.values.get(i);
	}

	public int size() {
		return this.values.size();
	}

	@Override
	public Iterator<JsonValue> iterator() {
		return new JsonArrayIterator(this.values.iterator());
	}

	public Stream<JsonValue> stream() {
		return this.values.stream();
	}

	@Override
	public JsonArray asArray() {
		return this;
	}

	@Override
	public JsonArray copy() {
		JsonArray copy = new JsonArray();
		this.values.forEach(v -> copy.add(v.copy()));
		return copy;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof JsonArray that && that.values.equals(this.values);
	}

	@Override
	public int hashCode() {
		return this.values.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < this.size(); i++) {
			JsonValue value = this.get(i);
			// increase indent of all lines
			String string = value.toString().replace("\n", "\n\t");
			builder.append("\n\t").append(string);
			if (i != this.size() - 1) {
				builder.append(',');
			}
		}
		return builder.append("\n]").toString();
	}

	@Override
	public void setPath(@Nullable String path) {
		super.setPath(path);
		for (int i = 0; i < this.size(); i++) {
			JsonValue value = this.get(i);
			// reset path first
			value.setPath(null);
			this.setChildPath(i, value);
		}
	}

	private void setChildPath(int i, JsonValue value) {
		value.setPath(this.makePath(i));
	}

	private String makePath(int i) {
		return this.getPath() + '[' + i + ']';
	}

	private static final class JsonArrayIterator implements Iterator<JsonValue> {
		private final Iterator<JsonValue> wrapped;
		@Nullable
		private JsonValue lastReturned;

		private JsonArrayIterator(Iterator<JsonValue> wrapped) {
			this.wrapped = wrapped;
		}

		@Override
		public JsonValue next() {
			JsonValue next = this.wrapped.next();
			this.lastReturned = next;
			return next;
		}

		@Override
		public boolean hasNext() {
			return this.wrapped.hasNext();
		}

		@Override
		public void remove() {
			if (this.lastReturned != null) {
				this.lastReturned.setPath(null);
				this.wrapped.remove();
			} else {
				throw new IllegalStateException("Nothing to remove");
			}
		}
	}
}
