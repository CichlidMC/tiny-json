package fish.cichlidmc.tinyjson.test;

import java.io.IOException;
import java.net.URL;

import fish.cichlidmc.tinyjson.TinyJson;
import fish.cichlidmc.tinyjson.test.deserialize.Doohickey;
import fish.cichlidmc.tinyjson.test.framework.TestRunner;
import fish.cichlidmc.tinyjson.test.tests.ComplexJsonTests;
import fish.cichlidmc.tinyjson.test.tests.SimpleJsonTests;
import fish.cichlidmc.tinyjson.value.composite.JsonObject;

public class Main {
	public static void main(String[] args) throws IOException {
		TestRunner.of(
				SimpleJsonTests.class,
				ComplexJsonTests.class
		).run();

//		Files.walkFileTree(Paths.get("data"), new SimpleFileVisitor<Path>() {
//			@Override
//			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//				if (file.getFileName().toString().endsWith(".json")) {
//					TinyJson.parseOrThrow(file);
//				}
//				return FileVisitResult.CONTINUE;
//			}
//		});

		parseDoohickey("doohickey.json");
		parseDoohickey("wrong_doohickey.json");

		JsonObject json = new JsonObject()
				.put("int", 1000)
				.put("float", 0.5);

		String expected = """
				{
					"int": 1000,
					"float": 0.5
				}""";

		if (!expected.equals(json.toString())) {
			throw new RuntimeException("Bad number formatting: " + json);
		}
	}

	private static void parseDoohickey(String name) {
		URL url = Main.class.getClassLoader().getResource(name);
		JsonObject parsed = TinyJson.fetchOrThrow(url).asObject();
		Doohickey doohickey = Doohickey.parse(parsed);
		System.out.println(doohickey);
		parsed.forEach((key, value) -> {
			if (!parsed.accessedFields().contains(key)) {
				System.out.println("Unused field: " + key);
			}
		});
	}
}
