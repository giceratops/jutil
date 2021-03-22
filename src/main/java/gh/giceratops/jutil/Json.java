package gh.giceratops.jutil;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class Json {

    protected static final ObjectMapper MAPPER = new ObjectMapper()
            .setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE)
            .setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY)
            .enable(SerializationFeature.INDENT_OUTPUT)
            .setDateFormat(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"));

    public static <O> O parse(final byte[] json, final Class<O> oClass) {
        try {
            return MAPPER.readValue(json, oClass);
        } catch (final IOException e) {
            throw Exceptions.runtime(e, "Error parsing byte[] (%s) into object", Strings.hex(json));
        }
    }

    public static <O> O parse(final String json, final Class<O> oClass) {
        try {
            return MAPPER.readValue(json, oClass);
        } catch (final JsonProcessingException e) {
            throw Exceptions.runtime(e, "Error parsing JSON (%s) into object", json);
        }
    }

    public static String stringify(final Object o) {
        try {
            return MAPPER.writeValueAsString(o);
        } catch (final JsonProcessingException e) {
            throw Exceptions.runtime(e, "Error parsing object (%s) into JSON", o);
        }
    }
}
