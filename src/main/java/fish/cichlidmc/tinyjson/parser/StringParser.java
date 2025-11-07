package fish.cichlidmc.tinyjson.parser;

import static fish.cichlidmc.tinyjson.parser.util.ParserUtils.CARRIAGE_RETURN;
import static fish.cichlidmc.tinyjson.parser.util.ParserUtils.LINE_BREAK;
import static fish.cichlidmc.tinyjson.parser.util.ParserUtils.QUOTE;

import java.io.IOException;
import java.util.Arrays;

import fish.cichlidmc.tinyjson.parser.util.ParseInput;
import fish.cichlidmc.tinyjson.parser.util.ParserUtils;
import fish.cichlidmc.tinyjson.value.primitive.JsonString;

public final class StringParser {
	public static final char BACKSLASH = '\\';

	private StringParser() {}

	static JsonString parse(ParseInput input) throws IOException {
		input.next(); // discard opening "

		StringBuilder builder = new StringBuilder();
		while (input.peek() != ParserUtils.QUOTE) {
			char next = input.next();
			if (next == LINE_BREAK) {
				throw input.error(pos -> "Found linebreak interrupting string on line " + (pos.line - 1));
			} else if (next != BACKSLASH) {
				builder.append(next);
				continue;
			}

			// escapes
			char escaped = input.next();
			if (escaped == QUOTE) {
				builder.append(QUOTE);
			} else if (escaped == BACKSLASH) {
				builder.append(BACKSLASH);
			} else if (escaped == '/') {
				// json lets you escape forward slashes too
				builder.append('/');
			} else if (escaped == 'b') {
				builder.append('\b');
			} else if (escaped == 'f') {
				builder.append('\f');
			} else if (escaped == 'n') {
				builder.append(LINE_BREAK);
			} else if (escaped == 'r') {
				builder.append(CARRIAGE_RETURN);
			} else if (escaped == 't') {
				builder.append('\t');
			} else if (escaped == 'u') {
				// escaped unicode character
				char[] hex = input.next(4);
				if (hex.length != 4) {
					throw input.errorAt("Unicode escape sequence is not 4 characters long: '" + Arrays.toString(hex) + '\'');
				}
				for (char c : hex) {
					if (Character.digit(c, 16) == -1) {
						throw input.errorAt("Invalid hex character in unicode escape: '" + c + '\'');
					}
				}
				char decoded = (char) Integer.parseInt(new String(hex), 16);
				builder.append(decoded);
			} else {
				throw input.errorAt("Unknown escape sequence '" + escaped + '\'');
			}
		}

		// consume closing "
		input.next();

		return new JsonString(builder.toString());
	}
}
