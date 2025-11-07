package fish.cichlidmc.tinyjson.test;

import fish.cichlidmc.tinyjson.value.JsonValue;
import fish.cichlidmc.tinyjson.value.composite.JsonObject;
import fish.cichlidmc.tinyjson.value.primitive.JsonNumber;

import java.util.List;
import java.util.stream.Collectors;

public record Doohickey(String name, int i, double d, Thingy thingy) {
	public static Doohickey parse(JsonObject json) {
		String name = json.getOrThrow("name").asString().value();
		int i = json.getOrThrow("i").asNumber().strictValue().intValue();
		double d = json.getOrThrow("d").asNumber().value();
		Thingy thingy = Thingy.parse(json.getOrThrow("thingy").asObject());
		return new Doohickey(name, i, d, thingy);
	}

	public record Thingy(String name, List<Double> numbers) {
		public static Thingy parse(JsonObject json) {
				String name = json.getOrThrow("name").asString().value();
				List<Double> numbers = json.getOrThrow("numbers").asArray().stream()
						.map(JsonValue::asNumber)
						.map(JsonNumber::value)
						.collect(Collectors.toList());
				return new Thingy(name, numbers);
			}
		}
}
