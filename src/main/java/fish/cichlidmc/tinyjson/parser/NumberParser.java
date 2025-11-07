package fish.cichlidmc.tinyjson.parser;

import static fish.cichlidmc.tinyjson.parser.util.ParserUtils.EOF;
import static fish.cichlidmc.tinyjson.parser.util.ParserUtils.isNumber;

import java.io.IOException;

import fish.cichlidmc.tinyjson.parser.util.ParseInput;
import fish.cichlidmc.tinyjson.value.primitive.JsonNumber;

public final class NumberParser {
	public static final char PLUS = '+';
	public static final char MINUS = '-';
	public static final char DECIMAL = '.';

	private NumberParser() {}

	static JsonNumber parse(ParseInput input) throws IOException {
		StringBuilder builder = new StringBuilder();
		builder.append(readDigitString(input, false, true));

		int peek = input.peek();
		if (peek == DECIMAL) {
			input.next(); // discard decimal point
			builder.append('.');
			builder.append(readDigitString(input, false, false));

			// update peek to check for exponent
			peek = input.peek();
		}

		if (isExponentMarker(peek)) {
			input.next(); // discard letter E
			builder.append('e');
			builder.append(readDigitString(input, true, true));
		}

		// technically the JSON spec specifies a number as unlimited in size
		// in practice, JavaScript limits it to a double, so I will do the same
		// hitting this limit is user error
		String string = builder.toString();
		try {
			double d = Double.parseDouble(string);
			// special case
			if (d == -0)
				d = 0;
			return new JsonNumber(d);
		} catch (NumberFormatException e) {
			throw input.errorAt("Failed to parse number '" + string + '\'');
		}
	}

	private static String readDigitString(ParseInput input, boolean plusAllowed, boolean minusAllowed) throws IOException {
		StringBuilder builder = new StringBuilder();
		char first = input.next();
		if (!isValidFirstChar(first, plusAllowed, minusAllowed)) {
			throw input.errorAt("Number component starts with illegal character '" + first + '\'');
		}
		builder.append(first);
		int next = input.peek();
		while (next != EOF && isNumber((char) next)) {
			builder.append(input.next());
			next = input.peek();
		}
		return builder.toString();
	}

	private static boolean isExponentMarker(int i) {
		return 'e' == i || 'E' == i; // o
	}

	private static boolean isValidFirstChar(char c, boolean plusAllowed, boolean minusAllowed) {
		if (isNumber(c))
			return true;

		if (c == PLUS)
			return plusAllowed;

		if (c == MINUS)
			return minusAllowed;

		return false;
	}
}
