package fish.cichlidmc.tinyjson.value;

import fish.cichlidmc.tinyjson.JsonException;
import fish.cichlidmc.tinyjson.value.composite.JsonArray;
import fish.cichlidmc.tinyjson.value.composite.JsonObject;
import fish.cichlidmc.tinyjson.value.primitive.JsonBool;
import fish.cichlidmc.tinyjson.value.primitive.JsonNull;
import fish.cichlidmc.tinyjson.value.primitive.JsonNumber;
import fish.cichlidmc.tinyjson.value.primitive.JsonString;
import org.jspecify.annotations.Nullable;

/// Parent class to all JSON representations.
/// All JsonValues store a path to themselves for identification in errors. This means that one
/// JsonValue cannot be shared between multiple JsonObjects or JsonArrays and a copy must be made.
public abstract sealed class JsonValue permits JsonPrimitive, JsonArray, JsonObject {
	public static final String ROOT_PATH = "$";
	@Nullable
	private String path;

	/// Create a deep copy of this value and all children.
	/// The path of this value is not preserved, and all children are updated so that the copy is their root.
	public abstract JsonValue copy();

	/// Create a pretty-printed string representing this JSON value. This string is parseable back into a JsonValue.
	@Override
	public abstract String toString();

	// conversion to specific types. will throw JsonException if the type doesn't match

	public JsonArray asArray() 		{ throw this.typeError("an array");		}
	public JsonBool asBoolean() 	{ throw this.typeError("a boolean");	}
	public JsonNull asNull() 		{ throw this.typeError("null"); 		}
	public JsonNumber asNumber() 	{ throw this.typeError("a number"); 	}
	public JsonObject asObject() 	{ throw this.typeError("an object"); 	}
	public JsonString asString() 	{ throw this.typeError("a string"); 	}

	// utilities

	private JsonException typeError(String type) {
		if (this.hasPath()) {
			return new JsonException(this.path + " is not " + type);
		} else {
			return new JsonException("Not " + type);
		}
	}

	/// internal - do not call this directly. Paths are managed by JsonArray and JsonObject.
	public void setPath(@Nullable String path) {
		if (this.hasPath() && path != null) {
			throw new IllegalStateException("Cannot override path " + this.path + " with " + path);
		}

		this.path = path;
	}

	public final String getPath() {
		return this.path == null ? ROOT_PATH : this.path;
	}

	public final boolean hasPath() {
		return this.path != null;
	}
}
