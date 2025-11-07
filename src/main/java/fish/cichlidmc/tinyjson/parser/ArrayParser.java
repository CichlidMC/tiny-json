package fish.cichlidmc.tinyjson.parser;

import static fish.cichlidmc.tinyjson.parser.util.ParserUtils.ARRAY_END;

import java.io.IOException;

import fish.cichlidmc.tinyjson.parser.util.ParseInput;
import fish.cichlidmc.tinyjson.value.composite.JsonArray;

public final class ArrayParser {
	public static final char COMMA = ',';

	private ArrayParser() {}

	static JsonArray parse(ParseInput input) throws IOException {
		input.next(); // discard opening [

		JsonArray array = new JsonArray();
		boolean first = true;
		while (input.peekNonWhitespace() != ARRAY_END) {
			if (!first) {
				// for all values except the first one, check for a preceding comma.
				if (input.next() != COMMA) {
					throw input.errorAt("Expected comma before value");
				}
				// check for ARRAY_END again in case of trailing comma
				if (input.peekNonWhitespace() == ARRAY_END) {
					throw input.errorAt("Expected value after comma");
				}
			}

			first = false;

			array.add(ValueParser.parse(input));
		}

		// consume closing ]
		input.next();

		return array;
	}
}
