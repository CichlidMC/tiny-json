package fish.cichlidmc.tinyjson.parser.util;

import fish.cichlidmc.tinyjson.JsonException;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.function.Function;

import static fish.cichlidmc.tinyjson.parser.util.ParserUtils.CARRIAGE_RETURN;
import static fish.cichlidmc.tinyjson.parser.util.ParserUtils.EOF;
import static fish.cichlidmc.tinyjson.parser.util.ParserUtils.LINE_BREAK;

public final class ParseInput {
	private final PushbackReader reader;

	private int line = 1;
	private int col = 1;

	@Nullable
	private CommentState commentState;

	public ParseInput(Reader reader) {
		this.reader = new PushbackReader(reader, 64);
	}

	/// Read the next character, incrementing the parser location. Throws if the end of input is reached.
	/// Line breaks are unified into \n.
	public char next() throws IOException {
		char next = this.read();
		if (next == CARRIAGE_RETURN && this.peek() == LINE_BREAK) {
			// \r\n on windows
			// consume the \n too
			this.read();
			this.nextLine();
			return LINE_BREAK;
		} else if (next == CARRIAGE_RETURN || next == LINE_BREAK) {
			// \r on mac, \n on linux
			this.nextLine();
			return LINE_BREAK;
		} else {
			// not a line break
			this.col++;
			return next;
		}
	}

	private void nextLine() {
		this.line++;
		this.col = 1;
	}

	/// Skip past all whitespace at the start of the input, including comments.
	public void skipWhitespace() throws IOException {
		while (true) {
			int next = this.peek();

			// end line comments
			if (next == LINE_BREAK && this.commentState == CommentState.LINE) {
				this.commentState = null;
				this.next();
				continue;
			}

			// end block comments
			if (next == '*' && this.commentState == CommentState.BLOCK) {
				char[] next2 = this.peek(2);
				if (next2.length != 2) {
					this.next();
					continue;
				}

				if (next2[1] == '/') {
					this.commentState = null;
					this.next(2);
					continue;
				}
			}

			// starting comments
			if (next == '/' && this.commentState == null) {
				char[] next2 = this.peek(2);
				if (next2.length != 2) {
					this.next();
					continue;
				}

				if (next2[1] == '/') {
					this.commentState = CommentState.LINE;
					this.next(2);
					continue;
				} else if (next2[1] == '*') {
					this.commentState = CommentState.BLOCK;
					this.next(2);
					continue;
				}
			}

			// discard all content in comments
			if (this.commentState != null) {
				this.next();
				continue;
			}

			// finally actually check for whitespace
			if (Character.isWhitespace(next)) {
				this.next();
				continue;
			}

			// otherwise, end found
			break;
		}
	}

	/// Read the next non-whitespace character, throwing if EOF is reached.
	public char peekNonWhitespace() throws IOException {
		this.skipWhitespace();
		return this.peekOrThrow();
	}

	/// Read the next N characters into an array, throwing if EOF is reached.
	public char[] next(int n) throws IOException {
		char[] array = new char[n];
		for (int i = 0; i < n; i++) {
			array[i] = this.next();
		}
		return array;
	}

	/// Read the next character without consuming it. May be EOF.
	public int peek() throws IOException {
		int next = this.tryRead();
		if (next != EOF) {
			this.reader.unread(next);
		}
		return next;
	}

	/// Read the next character without consuming it, throwing if it's EOF.
	public char peekOrThrow() throws IOException {
		char next = this.read();
		this.reader.unread(next);
		return next;
	}

	/// Peek the next N characters into an array. May be shorter than expected due to EOF.
	public char[] peek(int n) throws IOException {
		char[] chars = new char[n];
		for (int i = 0; i < n; i++) {
			int read = this.tryRead();
			if (read == EOF) {
				// cut short
				char[] actualChars = Arrays.copyOf(chars, i);
				this.reader.unread(actualChars);
				return actualChars;
			}

			chars[i] = (char) read;
		}

		this.reader.unread(chars);
		return chars;
	}

	/// Try to read the next character. May be EOF.
	public int tryRead() throws IOException {
		return this.reader.read();
	}

	public Position pos() {
		return new Position(this.line, this.col);
	}

	/// Return a new JsonException to be thrown. Message may contain
	/// "${pos}" as a placeholder for the current input position.
	public JsonException error(Function<Position, String> messageFactory) {
		return new JsonException(messageFactory.apply(this.pos()));
	}

	/// Shortcut for error that just adds 'at ${pos}' to the end.
	public JsonException errorAt(String message) {
		return this.error(pos -> message + " at " + pos);
	}

	private char read() throws IOException {
		int next = this.tryRead();
		if (next == EOF) {
			throw this.errorAt("Unexpected EOF");
		}
		return (char) next;
	}

	public static final class Position {
		public final int line;
		public final int col;

		private Position(int line, int col) {
			this.line = line;
			this.col = col;
		}

		@Override
		public String toString() {
			return "line " + this.line + ", col. " + this.col;
		}
	}

	private enum CommentState {
		LINE, BLOCK
	}
}
