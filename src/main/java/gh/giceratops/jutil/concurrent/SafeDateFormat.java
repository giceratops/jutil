package gh.giceratops.jutil.concurrent;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

public class SafeDateFormat extends ThreadLocal<DateFormat[]> {

    private final String[] formats;

    public SafeDateFormat(final String... formats) {
        this.formats = formats;
    }

    @Override
    protected DateFormat[] initialValue() {
        return Stream.of(this.formats)
                .map(SimpleDateFormat::new)
                .toArray(DateFormat[]::new);
    }

    public String format(final Date date) {
        return this.get()[0].format(date);
    }

    public Date parse(final String source) throws ParseException {
        for (final var sdf : this.get()) {
            try {
                return sdf.parse(source);
            } catch (final ParseException ignore) {
            }
        }
        throw new ParseException("Couldn't parse " + source + " as any of " + Arrays.toString(this.get()), 0);
    }
}
