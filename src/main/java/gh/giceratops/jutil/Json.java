package gh.giceratops.jutil;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * Json utility class.
 * Don't expose the underlying library.
 */
public class Json {

    private static final Json JSON = new Json();

    public static Json shared() {
        return JSON;
    }

    public static <O> O parse(final String json, final Class<O> oClass) {
        return JSON.asObject(json, oClass);
    }

    public static String stringify(final Object o) {
        return stringify(o, false);
    }

    public static String stringify(final Object o, final boolean printClass) {
        final var sb = new StringBuilder();
        if (printClass) {
            sb.append(o.getClass().getSimpleName()).append(" ");
        }
        return sb.append(JSON.asString(o)).toString();
    }

    private final ObjectMapper mapper;

    public Json() {
        this.mapper = JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .enable(SerializationFeature.INDENT_OUTPUT);
    }

    public byte[] asBytes(final Object o) {
        try {
            return this.mapper.writeValueAsBytes(o);
        } catch (final JsonProcessingException e) {
            throw Exceptions.runtime(e, "Error parsing object (%s) into JSON", o);
        }
    }

    public String asString(final Object o) {
        try {
            return this.mapper.writeValueAsString(o);
        } catch (final JsonProcessingException e) {
            throw Exceptions.runtime(e, "Error parsing object (%s) into JSON", o);
        }
    }

    public <O> O asObject(final String json, final Class<O> oClass) {
        try {
            return this.mapper.readValue(json, oClass);
        } catch (final JsonProcessingException e) {
            throw Exceptions.runtime(e, "Error parsing string (%s) into object", json);
        }
    }

    public <O> O asObject(final byte[] json, final Class<O> oClass) {
        try {
            return this.mapper.readValue(json, oClass);
        } catch (final IOException e) {
            throw Exceptions.runtime(e, "Error parsing byte[] (%s) into object", Strings.hex(json));
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
