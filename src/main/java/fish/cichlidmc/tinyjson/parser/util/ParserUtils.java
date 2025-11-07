package fish.cichlidmc.tinyjson.parser.util;

public final class ParserUtils {
	public static final int EOF = -1;

	public static final char OBJ_START = '{';
	public static final char OBJ_END = '}';

	public static final char ARRAY_START = '[';
	public static final char ARRAY_END = ']';

	public static final char QUOTE = '"';

	public static final char LINE_BREAK = '\n';
	public static final char CARRIAGE_RETURN = '\r';

	public static final char NEGATIVE_SIGN = '-';

	public static final char[] TRUE = "true".toCharArray();
	public static final char[] FALSE = "false".toCharArray();
	public static final char[] NULL = "null".toCharArray();

	private ParserUtils() {}

	public static boolean charsEqual(char[] a, char[] b) {
		if (a.length != b.length)
			return false;

		for (int i = 0; i < a.length; i++) {
			if (a[i] != b[i]) {
				return false;
			}
		}

		return true;
	}

	public static boolean isNumber(char c) {
		return c >= '0' && c <= '9';
	}
}
