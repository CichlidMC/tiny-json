import org.jspecify.annotations.NullMarked;

@NullMarked
open module fish.cichlidmc.tinyjson {
	requires static transitive org.jspecify;

	exports fish.cichlidmc.tinyjson;
	exports fish.cichlidmc.tinyjson.value;
	exports fish.cichlidmc.tinyjson.value.composite;
	exports fish.cichlidmc.tinyjson.value.primitive;
}
