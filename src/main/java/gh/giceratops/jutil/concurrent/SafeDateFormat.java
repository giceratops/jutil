package gh.giceratops.jutil.concurrent;

import java.text.*;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Stream;

public class SafeDateFormat extends DateFormat {

    private final ThreadLocal<DateFormat[]> threadLocal;

    public SafeDateFormat(final String... formats) {
        if (gh.giceratops.jutil.Arrays.isEmpty(formats)) {
            throw new IllegalArgumentException("Formats cannot be empty");
        }

        this.threadLocal = ThreadLocal.withInitial(() ->
                Stream.of(formats)
                        .map(SimpleDateFormat::new)
                        .toArray(DateFormat[]::new)
        );
    }

    @Override
    public StringBuffer format(final Date date, final StringBuffer toAppendTo, final FieldPosition fieldPosition) {
        return this.threadLocal.get()[0].format(date, toAppendTo, fieldPosition);
    }

    @Override
    public Date parse(final String source, final ParsePosition pos) {
        return this.threadLocal.get()[0].parse(source, pos);
    }

    public Date parse(final String source) throws ParseException {
        for (final var sdf : this.threadLocal.get()) {
            try {
                return sdf.parse(source);
            } catch (final ParseException ignore) {
            }
        }
        throw new ParseException("Couldn't parse " + source + " as any of " + Arrays.toString(this.threadLocal.get()), 0);
    }


}
