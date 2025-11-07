package fish.cichlidmc.tinyjson.test.tests;

import fish.cichlidmc.tinyjson.test.framework.Group;
import fish.cichlidmc.tinyjson.test.framework.Test;
import fish.cichlidmc.tinyjson.value.composite.JsonArray;
import fish.cichlidmc.tinyjson.value.composite.JsonObject;
import fish.cichlidmc.tinyjson.value.JsonValue;
import fish.cichlidmc.tinyjson.value.primitive.JsonBool;
import fish.cichlidmc.tinyjson.value.primitive.JsonNull;
import fish.cichlidmc.tinyjson.value.primitive.JsonNumber;

@Group("complex")
public class ComplexJsonTests {
	@Test
	public static final JsonValue NUMBERS = new JsonArray()
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

	@Test
	public static final JsonValue EVERYTHING = new JsonObject()
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

	@Test
	public static final JsonValue COMMENTS = new JsonObject()
			.put("test", new JsonNumber(1))
			.put("int", new JsonNumber(5))
			.put("object", new JsonObject()
					.put("bool", new JsonBool(true))
					.put("/*string//", "/*yes*///")
					.put("empty", new JsonNull())
			);

}
