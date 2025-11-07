package fish.cichlidmc.tinyjson.test.def;

import fish.cichlidmc.tinyjson.TinyJson;
import fish.cichlidmc.tinyjson.test.framework.Group;
import fish.cichlidmc.tinyjson.test.framework.Resource;
import fish.cichlidmc.tinyjson.value.JsonValue;
import fish.cichlidmc.tinyjson.value.composite.JsonArray;
import fish.cichlidmc.tinyjson.value.composite.JsonObject;
import fish.cichlidmc.tinyjson.value.primitive.JsonBool;
import fish.cichlidmc.tinyjson.value.primitive.JsonNull;
import fish.cichlidmc.tinyjson.value.primitive.JsonNumber;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Group("complex")
public final class ComplexTests {
	@Test
	public void numbers(@Resource String text) {
		JsonValue expected = new JsonArray()
				.add(1)
				.add(2)
				.add(0)
				.add(0)
				.add(0.1)
				.add(0.00001)
				.add(0)
				.add(0)
				.add(1.5e9)
				.add(10_000e-3)
				.add(2.5e-1);

		Assertions.assertEquals(expected, TinyJson.parseOrThrow(text));
	}

	@Test
	public void everything(@Resource String text) {
		JsonValue expected = new JsonObject()
				.put("test", true)
				.putNull("test2")
				.put("test3: false", "true")
				.put("list", new JsonArray()
						.add(new JsonArray()
								.add("list2")
								.add(new JsonArray()
										.add("list3")
								)
								.add(new JsonObject()
										.put("veryNested", new JsonObject()
												.put("false", true)
										)
								)
						)
						.add(1e-20)
				);

		Assertions.assertEquals(expected, TinyJson.parseOrThrow(text));
	}

	@Test
	public void comments(@Resource String text) {
		JsonValue expected = new JsonObject()
				.put("test", new JsonNumber(1))
				.put("int", new JsonNumber(5))
				.put("object", new JsonObject()
						.put("bool", new JsonBool(true))
						.put("/*string//", "/*yes*///")
						.put("empty", new JsonNull())
				);

		Assertions.assertEquals(expected, TinyJson.parseOrThrow(text));
	}
}
