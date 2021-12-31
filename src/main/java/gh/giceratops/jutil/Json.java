package gh.giceratops.jutil;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;

import java.io.IOException;
import java.io.Reader;

/**
 * Json utility class.
 * Don't expose the underlying library.
 */
public class Json {

    private static final Json JSON = new Json();

    public static <O> O parse(final String json, final Class<O> oClass) {
        return JSON.asObject(json, oClass);
    }

    public static String stringify(final Object o) {
        return stringify(o, false);
    }

    public static String stringify(final Object o, final boolean printClass) {
        return (printClass ? o.getClass().getSimpleName() + " " : "")
                + JSON.asString(o);
    }

    private final ObjectMapper mapper;

    public Json() {
        this(JsonMapper.builder()
                .addModule(new ParameterNamesModule())
                .addModule(new Jdk8Module())
                .addModule(new JavaTimeModule())
                .build()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
                .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
                .enable(SerializationFeature.INDENT_OUTPUT)
        );
    }

    private Json(final ObjectMapper mapper) {
        this.mapper = mapper;
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
}
