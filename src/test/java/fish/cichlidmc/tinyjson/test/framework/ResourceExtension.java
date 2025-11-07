package fish.cichlidmc.tinyjson.test.framework;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public final class ResourceExtension implements ParameterResolver {
	@Override
	public boolean supportsParameter(ParameterContext param, ExtensionContext ext) throws ParameterResolutionException {
		return param.isAnnotated(Resource.class) && param.getParameter().getType() == String.class;
	}

	@Override
	public Object resolveParameter(ParameterContext param, ExtensionContext ext) throws ParameterResolutionException {
		Resource annotation = param.findAnnotation(Resource.class).orElseThrow();
		String path = determineFilePath(annotation, ext.getRequiredTestMethod());

		InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path);
		if (stream == null) {
			throw new ParameterResolutionException("Resource not found: " + path);
		}

		try (stream) {
			return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			throw new ParameterResolutionException("Failed to read resource at " + path, e);
		}
	}

	private static String determineFilePath(Resource resource, Method method) throws ParameterResolutionException {
		Optional<String> group = getGroup(method);
		String name = determineFileName(resource, method);
		return group.map(folder -> folder + '/' + name).orElse(name) + resource.extension();
	}

	private static String determineFileName(Resource resource, Method method) throws ParameterResolutionException {
		if (!resource.value().isBlank())
			return resource.value();

		String name = method.getName();
		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < name.length(); i++) {
			char c = name.charAt(i);
			if (Character.isUpperCase(c)) {
				builder.append('_').append(Character.toLowerCase(c));
			} else {
				builder.append(c);
			}
		}

		return builder.toString();
	}

	private static Optional<String> getGroup(Method method) {
		Group group = method.getDeclaringClass().getDeclaredAnnotation(Group.class);
		return group == null ? Optional.empty() : Optional.of(group.value());
	}
}
