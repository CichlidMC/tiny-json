package fish.cichlidmc.tinyjson.test.def;

import fish.cichlidmc.tinyjson.TinyJson;
import fish.cichlidmc.tinyjson.test.Doohickey;
import fish.cichlidmc.tinyjson.test.framework.Group;
import fish.cichlidmc.tinyjson.test.framework.Resource;
import fish.cichlidmc.tinyjson.value.composite.JsonObject;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Group("doohickey")
public final class DoohickeyTests {
	@Test
	public void doohickey(@Resource String text) {
		JsonObject json = TinyJson.parseOrThrow(text).asObject();
		Doohickey doohickey = Doohickey.parse(json);

		assertEquals("Gerald", doohickey.name());
		assertEquals(-1, doohickey.i());
		assertEquals(101e-12, doohickey.d());
		assertEquals(new Doohickey.Thingy("Also Gerald", List.of(1d, 2d, 3d, 100e9, 12e-3)), doohickey.thingy());

		assertEquals(Set.of(), json.unusedKeys());
	}

	@Test
	public void doohickeyUnusedField(@Resource String text) {
		JsonObject json = TinyJson.parseOrThrow(text).asObject();
		Doohickey.parse(json);
		assertEquals(Set.of("unused"), json.unusedKeys());
	}
}
