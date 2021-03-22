package gh.giceratops.jutil.properties;

import gh.giceratops.jutil.Arrays;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class OverrideProperties extends Properties {

    @Serial private static final long serialVersionUID = -6669961611246943001L;

    public OverrideProperties() {
    }

    public OverrideProperties(final ClassLoader ctx, final String rsx) throws IOException {
        this.load(ctx, rsx);
    }

    public OverrideProperties(final Class<?> ctx, final String rsx) throws IOException {
        this(ctx.getClassLoader(), rsx);
    }

    public OverrideProperties(final String fileName) throws IOException {
        this(new File(fileName));
    }

    public OverrideProperties(final File serverFile) throws IOException {
        this.load(serverFile);
    }

    public OverrideProperties(final ResultSet rs, final String... ignore) throws SQLException {
        this.load(rs, ignore);
    }

    private static String overrideName(final String base) {
        final StringBuilder sb = new StringBuilder(base);
        final int i = sb.lastIndexOf(".");
        if (i < 0) {
            sb.append(".override");
        } else {
            sb.insert(i, ".override");
        }
        return sb.toString();
    }

    @Override
    public void load(final InputStream inStream) {
        throw new RuntimeException("Unsupported method");
    }

    @Override
    public void load(final Reader reader) {
        throw new RuntimeException("Unsupported method");
    }

    public final OverrideProperties load(final String fileName) throws IOException {
        return this.load(new File(fileName));
    }

    public final synchronized OverrideProperties load(final ClassLoader ctx, final String rsx) throws IOException {
        final var serverStream = ctx.getResourceAsStream(rsx);
        final var localStream = ctx.getResourceAsStream(overrideName(rsx));

        if (serverStream != null) {
            try (serverStream) {
                if (super.defaults == null) {
                    super.defaults = new Properties();
                }
                super.defaults.load(serverStream);
            }
        }
        if (localStream != null) {
            try (localStream) {
                super.load(localStream);
            }
        }
        return this;
    }

    public final synchronized OverrideProperties load(final File serverFile) throws IOException {
        final File localFile = new File(serverFile.getParentFile(), overrideName(serverFile.getName()));
        if (!serverFile.exists() && !localFile.exists()) {
            throw new FileNotFoundException("Both server and local file do not exist for " + serverFile);
        }

        if (serverFile.exists()) {
            try (final FileInputStream fis = new FileInputStream(serverFile)) {
                if (super.defaults == null) {
                    super.defaults = new Properties();
                }
                super.defaults.load(fis);
            }
        }
        if (localFile.exists()) {
            try (final FileInputStream fis = new FileInputStream(localFile)) {
                super.load(fis);
            }
        }
        return this;
    }

    public final synchronized OverrideProperties load(final ResultSet rs, final String... ignore) throws SQLException {
        final var meta = rs.getMetaData();
        final int columnCount = meta.getColumnCount();

        for (int i = 1; i <= columnCount; i++) {
            final String name = meta.getColumnName(i);
            if (!Arrays.contains(ignore, name)) {
                this.setProperty(name, String.valueOf(rs.getObject(name)));
            }
        }
        return this;
    }

    public void store(final String file, final String comments) throws IOException {
        try (final FileOutputStream fos = new FileOutputStream(file)) {
            super.store(fos, comments);
        }
    }

    public <T> T get(final OverrideProperty<T> prop) {
        try {
            if (prop.isPresent()) {
                return prop.get();
            }
            return prop.fromString(super.getProperty(prop.key()));
        } catch (final RuntimeException re) {
            return prop.def();
        }
    }

    public <T> void set(final OverrideProperty<T> prop, final T value) {
        super.setProperty(prop.key(), prop.toString(value));
        prop.clear();
    }

    public boolean getBooleanProperty(final String key) {
        final String value = super.getProperty(key);
        if (value == null) {
            throw new NullPointerException();
        }
        return Boolean.parseBoolean(value);
    }

    public boolean getBooleanProperty(final String key, final boolean def) {
        try {
            return this.getBooleanProperty(key);
        } catch (final RuntimeException ignore) {
            return def;
        }
    }

    public void setBooleanProperty(final String key, final boolean val) {
        super.setProperty(key, String.valueOf(val));
    }

    public int getIntProperty(final String key) {
        final String value = super.getProperty(key);
        if (value == null) {
            throw new NullPointerException();
        }
        return Integer.parseInt(value);
    }

    public int getIntProperty(final String key, final int def) {
        try {
            return getIntProperty(key);
        } catch (final RuntimeException ignore) {
            return def;
        }
    }

    public void setIntProperty(final String key, final int val) {
        super.setProperty(key, String.valueOf(val));
    }
}
