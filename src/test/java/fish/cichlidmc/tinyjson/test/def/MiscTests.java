package fish.cichlidmc.tinyjson.test.def;

import fish.cichlidmc.tinyjson.TinyJson;
import org.junit.jupiter.api.Test;

public final class MiscTests {
	@Test
	public void checkModuleVersion() {
		TinyJson.class.getModule().getDescriptor().version().orElseThrow();
	}
}
