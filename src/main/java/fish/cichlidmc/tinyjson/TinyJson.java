package fish.cichlidmc.tinyjson;

import fish.cichlidmc.tinyjson.parser.ValueParser;
import fish.cichlidmc.tinyjson.parser.util.ParseInput;
import fish.cichlidmc.tinyjson.value.JsonValue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;

/// Primary interface for TinyJson. Provides several methods for parsing JSON from various sources:
/// - Strings
/// - Files
/// - Paths
/// - URIs
/// - URLs
/// - InputStreams
/// - Readers
public final class TinyJson {
	private TinyJson() {}

	// String input

	public static JsonValue parse(String string) throws IOException {
		return parse(new StringReader(string));
	}

	public static JsonValue parseOrThrow(String string) {
		return parseOrThrow(new StringReader(string));
	}

	// File input

	public static JsonValue parse(File file) throws IOException {
		return parse(file.toPath());
	}

	public static JsonValue parseOrThrow(File file) {
		return parseOrThrow(file.toPath());
	}

	// Path input

	public static JsonValue parse(Path file) throws IOException {
		return parse(Files.newBufferedReader(file));
	}

	public static JsonValue parseOrThrow(Path file) {
		try {
			return parse(Files.newBufferedReader(file));
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	// URI input

	public static JsonValue fetch(URI uri) throws IOException {
		return fetch(uri.toURL());
	}

	public static JsonValue fetchOrThrow(URI uri) {
		try {
			return fetchOrThrow(uri.toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	// URL input

	public static JsonValue fetch(URL url) throws IOException {
		URLConnection connection = url.openConnection();
		connection.connect();
		return parse(connection.getInputStream());
	}

	public static JsonValue fetchOrThrow(URL url) {
		try {
			return fetch(url);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}

	// InputStream input

	public static JsonValue parse(InputStream stream) throws IOException {
		return parse(new InputStreamReader(stream));
	}

	public static JsonValue parseOrThrow(InputStream stream) {
		return parseOrThrow(new InputStreamReader(stream));
	}

	// Reader input

	public static JsonValue parse(Reader reader) throws IOException {
		return ValueParser.parse(new ParseInput(reader));
	}

	public static JsonValue parseOrThrow(Reader reader) {
		try {
			return parse(reader);
		} catch (IOException e) {
			throw new UncheckedIOException(e);
		}
	}
}
