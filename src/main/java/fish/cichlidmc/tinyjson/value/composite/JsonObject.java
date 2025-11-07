package fish.cichlidmc.tinyjson.value.composite;

import fish.cichlidmc.tinyjson.JsonException;
import fish.cichlidmc.tinyjson.value.JsonValue;
import fish.cichlidmc.tinyjson.value.primitive.JsonBool;
import fish.cichlidmc.tinyjson.value.primitive.JsonNull;
import fish.cichlidmc.tinyjson.value.primitive.JsonNumber;
import fish.cichlidmc.tinyjson.value.primitive.JsonString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SequencedCollection;
import java.util.SequencedSet;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Class for objects represented in JSON. Stores a map of keys to other JsonValues.
 * Also tracks which fields have been queried directly for verification purposes.
 */
public final class JsonObject extends JsonValue {
	private final LinkedHashMap<String, JsonValue> entries = new LinkedHashMap<>();
	private final Set<String> queried = new HashSet<>();

	public JsonObject put(String key, JsonValue value) {
		this.setChildPath(key, value);
		JsonValue removed = this.entries.put(key, value);
		if (removed != null) {
			removed.setPath(null);
		}
		return this;
	}

	// utilities for adding types directly
	public JsonObject put(String key, boolean value)	{ return this.put(key, new JsonBool(value));	}
	public JsonObject putNull(String key)				{ return this.put(key, new JsonNull());			}
	public JsonObject put(String key, double value)		{ return this.put(key, new JsonNumber(value));	}
	public JsonObject put(String key, String value)		{ return this.put(key, new JsonString(value));	}

	/**
	 * @return the value associated with the given key, or null if no value exists
	 */
	@Nullable
	public JsonValue get(String key) {
		this.queried.add(key);
		return this.entries.get(key);
	}

	/**
	 * @return an optional holding the value associated with the given key, or empty if no value exists
	 */
	public Optional<JsonValue> getOptional(String key) {
		return Optional.ofNullable(this.get(key));
	}

	/**
	 * @return the value associated with the given key, if present
	 * @throws JsonException if there is no value associated with the given key
	 */
	public JsonValue getOrThrow(String key) throws JsonException {
		JsonValue value = this.get(key);
		if (value == null) {
			throw new JsonException(this.makePath(key) + " does not exist");
		}
		return value;
	}

	/**
	 * @return the value associated with the given key if present, otherwise a new {@link JsonNull}
	 */
	public JsonValue getOrJsonNull(String key) {
		JsonValue value = this.get(key);
		if (value != null)
			return value;

		JsonNull jsonNull = new JsonNull();
		this.setChildPath(key, jsonNull);
		return jsonNull;
	}

	/**
	 * Remove the value associated with the given key from this object.
	 * Since the value is no longer associated with this object, its path is cleared.
	 * @return the removed object, or null if it did not exist
	 */
	public JsonValue remove(String key) {
		JsonValue removed = this.entries.remove(key);
		if (removed != null) {
			removed.setPath(null);
			this.queried.remove(key);
		}
		return removed;
	}

	public boolean contains(String key) {
		return this.entries.containsKey(key);
	}

	public int size() {
		return this.entries.size();
	}

	public void forEach(BiConsumer<String, JsonValue> consumer) {
		this.entries.forEach(consumer);
	}

	/**
	 * @return a read-only view of all keys in this object
	 */
	public SequencedSet<String> keys() {
		return Collections.unmodifiableSequencedSet(this.entries.sequencedKeySet());
	}

	/**
	 * @return a read-only view of all values in this object
	 */
	public SequencedCollection<JsonValue> values() {
		return Collections.unmodifiableSequencedCollection(this.entries.sequencedValues());
	}

	/**
	 * @return a read-only view of all entries in this object
	 */
	public Set<Map.Entry<String, JsonValue>> entrySet() {
		return Collections.unmodifiableSequencedSet(this.entries.sequencedEntrySet());
	}

	/**
	 * @return a read-only view of the set of keys that have been accessed directly.
	 * This will not include keys that have been iterated over.
	 */
	public Set<String> queriedKeys() {
		return Collections.unmodifiableSet(this.queried);
	}

	/**
	 * Create a new set containing all keys within this object that have not been queried.
	 * This is equivalent to {@code keys - queriedKeys}. Since a new set is returned, you
	 * are free to modify it if you so desire.
	 */
	@Contract("->new")
	public Set<String> unusedKeys() {
		Set<String> keys = new HashSet<>(this.entries.keySet());
		keys.removeAll(this.queried);
		return keys;
	}

	@Override
	public JsonObject copy() {
		JsonObject copy = new JsonObject();
		this.forEach((k, v) -> copy.put(k, v.copy()));
		return copy;
	}

	@Override
	public JsonObject asObject() {
		return this;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof JsonObject that && that.entries.equals(this.entries);
	}

	@Override
	public int hashCode() {
		return this.entries.hashCode();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("{");
		int i = 0;
		for (Map.Entry<String, JsonValue> entry : this.entrySet()) {
			// increase indent of all lines
			String string = entry.getValue().toString().replace("\n", "\n\t");
			builder.append("\n\t").append('"').append(entry.getKey()).append("\": ").append(string);
			if (i != this.entries.size() - 1) {
				builder.append(',');
			}
			i++;
		}
		return builder.append("\n}").toString();
	}

	@Override
	public void setPath(String path) {
		super.setPath(path);
		this.forEach((key, value) -> {
			// reset path first
			value.setPath(null);
			this.setChildPath(key, value);
		});
	}

	private void setChildPath(String key, JsonValue value) {
		value.setPath(this.makePath(key));
	}

	private String makePath(String key) {
		return this.getPath() + '.' + key;
	}
}
