package gh.giceratops.jutil;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Yaml utility class.
 * Don't expose the underlying library.
 */
public class Yaml {

    private static final Yaml YAML = new Yaml();

    public static Yaml shared() {
        return YAML;
    }

    public static <O> O parse(final String yaml, final Class<O> oClass) {
        return YAML.asObject(yaml, oClass);
    }

    public static <O> O readResource(final String rsx, final Class<O> oClass) throws IOException {
        try (final var stream = Yaml.class.getClassLoader().getResourceAsStream(rsx)) {
            return YAML.asObject(stream, oClass);
        }
    }

    public static String stringify(final Object o) {
        return stringify(o, false);
    }

    public static String stringify(final Object o, final boolean printClass) {
        final var sb = new StringBuilder();
        if (printClass) {
            sb.append(o.getClass().getSimpleName()).append(" ");
        }
        return sb.append(YAML.asString(o)).toString();
    }

    private final YAMLMapper mapper;

    public Yaml() {
        this.mapper = YAMLMapper.builder()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .enable(SerializationFeature.INDENT_OUTPUT)
                .build();
    }

    public String asString(final Object o) {
        try {
            return this.mapper.writeValueAsString(o);
        } catch (final Exception e) {
            throw Exceptions.runtime(e, "Error parsing object (%s) into Yaml", o);
        }
    }

    public <O> O asObject(final String yaml, final Class<O> oClass) {
        try {
            return this.mapper.readValue(yaml, oClass);
        } catch (final Exception e) {
            throw Exceptions.runtime(e, "Error parsing string (%s) into object", yaml);
        }
    }

    public <O> O asObject(final byte[] yaml, final Class<O> oClass) {
        try {
            return this.mapper.readValue(yaml, oClass);
        } catch (final IOException e) {
            throw Exceptions.runtime(e, "Error parsing byte[] (%s) into object", Strings.hex(yaml));
        }
    }

    public <O> O asObject(final Reader reader, final Class<O> oClass) {
        try {
            return this.mapper.readValue(reader, oClass);
        } catch (final IOException e) {
            throw Exceptions.runtime(e, "Error parsing reader (%s) into object", reader);
        }
    }

    public <O> O asObject(final InputStream is, final Class<O> oClass) {
        try {
            return this.mapper.readValue(is, oClass);
        } catch (final IOException e) {
            throw Exceptions.runtime(e, "Error parsing reader (%s) into object", is);
        }
    }
}
