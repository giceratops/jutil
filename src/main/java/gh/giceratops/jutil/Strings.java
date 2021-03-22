package gh.giceratops.jutil;

import java.net.InetAddress;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class Strings {

    protected Strings() {
    }

    public static String urlEncode(final String s) {
        return URLEncoder.encode(s, StandardCharsets.UTF_8);
    }

    public static boolean nullOrEmpty(final String s) {
        return s == null || s.isEmpty();
    }

    public static String hex(final int i) {
        return String.format("%02X", i & 0xFF);
    }

    public static String hexShortLE(final short value) {
        return hexLE(value, Short.SIZE);
    }

    public static String hexIntLE(final int value) {
        return hexLE(value, Integer.SIZE);
    }

    public static String hexLongLE(final long value) {
        return hexLE(value, Long.SIZE);
    }

    private static String hexLE(final long value, final int size) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i += 8) {
            sb.append(hex((byte) (value >>> i))).append(" ");
        }
        return sb.toString().trim();
    }

    public static String hex(final byte[] bytes) {
        if (Arrays.isEmpty(bytes)) {
            return "[]";
        }

        final StringBuilder sb = new StringBuilder(bytes.length * 3 + 1).append("[");
        for (final byte b : bytes) {
            sb.append(hex(b)).append(" ");
        }
        sb.setCharAt(sb.length() - 1, ']');

        return sb.toString();
    }

    public static String enumName(final Enum<?> e) {
        return enumName(e, 2);
    }

    public static String enumName(final Enum<?> e, final int minimum) {
        if (e == null) {
            return Objects.toString(null);
        }
        final String name = e.name();
        final int length = name.length();
        final StringBuilder sb = new StringBuilder(length + 1);
        int start = 0, stop;
        do {
            stop = name.indexOf("_", start);
            if (stop == -1) {
                stop = length;
            }

            if (stop - start <= minimum) {
                sb.append(name, start, stop);
            } else {
                sb.append(name.charAt(start));
                sb.append(name.substring(start + 1, stop).toLowerCase());
            }
            sb.append(" ");

            start = stop + 1;
        } while (stop != length);

        return sb.substring(0, length);
    }

    public static String padStart(final int i, final int minLength, final char padChar) {
        return padStart(String.valueOf(i), minLength, padChar);
    }

    public static String padStart(final String string, final int minLength, final char padChar) {
        int length = string == null ? 0 : string.length();
        if (length >= minLength) {
            return string == null ? "" : string;
        }
        final StringBuilder sb = new StringBuilder(minLength);
        for (; length < minLength; length++) {
            sb.append(padChar);
        }
        sb.append(string);
        return sb.toString();
    }

    public static String padEnd(final int i, final int minLength, final char padChar) {
        return padEnd(String.valueOf(i), minLength, padChar);
    }

    public static String padEnd(final String string, final int minLength, final char padChar) {
        int length = string == null ? 0 : string.length();
        if (length >= minLength) {
            return string == null ? "" : string;
        }
        final StringBuilder sb = new StringBuilder(minLength);
        sb.append(string);
        for (; length < minLength; length++) {
            sb.append(padChar);
        }
        return sb.toString();
    }

    public static String join(final String[] elements, final String joiner) {
        return join(elements, joiner, "");
    }

    public static String join(final String[] elements, final String joiner, final String prePostJoin) {
        return join(elements, joiner, prePostJoin, prePostJoin);
    }

    public static String join(final String[] elements, final String joiner, final String preJoin, final String postJoin) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            final String element = elements[i];
            sb.append(preJoin);
            sb.append(element);
            sb.append(postJoin);

            if (i != elements.length - 1) {
                sb.append(joiner);
            }
        }
        return sb.toString();
    }

    public static String join(final String element, final int count, final String joiner) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(element);

            if (i != count - 1) {
                sb.append(joiner);
            }
        }
        return sb.toString();
    }

    public static String format(final String format, final Map<String, String> values) {
        if (values == null) {
            return format;
        }
        return format(format, values.entrySet().stream(), Map.Entry::getKey, Map.Entry::getValue);
    }

    public static String format(final String format, final Stream<Pair<String, String>> stream) {
        return format(format, stream, Pair::left, Pair::right);
    }

    public static String format(final String format, final Pair<String, String> pair) {
        return format(format, Stream.of(pair), Pair::left, Pair::right);
    }

    @SafeVarargs
    public static String format(final String format, final Pair<String, String>... pairs) {
        return format(format, Stream.of(pairs), Pair::left, Pair::right);
    }

    public static <E> String format(final String format, final Stream<E> stream, final Function<E, String> key,
            final Function<E, String> value) {
        if (format == null || stream == null || key == null || value == null) {
            return format;
        }

        return stream.reduce(format, (s, e) -> s.replace(String.format("{%s}", key.apply(e)), value.apply(e)),
                (s, s2) -> s);
    }

    public static boolean formatHasValue(final String format, final String formatted, final String key, final String value) {
        System.out.printf("%s %s %s %s%n", format, formatted, key, value);
        final var replace = String.format("{%s}", key);
        if (!format.contains(replace)) {
            return false;
        }
        final var length = formatted.length();
        final var start = format.indexOf(replace);
        final var end = start + value.length();
        if (length < start || length < end) {
            return false;
        }
        return formatted.subSequence(start, end).equals(value);
    }

    public static boolean formatHasValue(final String format, final String formatted, final Pair<String, String> map) {
        return formatHasValue(format, formatted, map.left(), map.right());
    }

    public static boolean formatHasValue(final String format, final String formatted, final Map<String, String> map) {
        if (map == null) {
            return true;
        }

        for (final var entry : map.entrySet()) {
            if (!formatHasValue(format, formatted, entry.getKey(), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    public static byte[] ipToBytes(final String ip) {
        try {
            return InetAddress.getByName(ip).getAddress();
        } catch (final Exception e) {
            return new byte[] { 0, 0, 0, 0 };
        }
    }
}
