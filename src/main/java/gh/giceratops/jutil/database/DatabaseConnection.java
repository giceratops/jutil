package gh.giceratops.jutil.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

public class DatabaseConnection extends ThreadLocal<Connection> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseConnection.class);

    private final Properties properties;
    private final Set<Connection> pool;

    private boolean closed;

    public DatabaseConnection(Properties properties) {
        this.properties = Objects.requireNonNull(properties);
        this.pool = new HashSet<>();
        this.closed = false;
    }

    @Override
    protected Connection initialValue() {
        LOGGER.debug("Opening new DB connection [{}] on thread {}", this, Thread.currentThread());

        try {
            final var url = this.properties.getProperty("url");
            final var connection = DriverManager.getConnection(url, this.properties);
            this.pool.add(connection);
            return connection;
        } catch (final SQLException sqle) {
            throw new RuntimeException("Error opening initial connection for " + this, sqle);
        }
    }

    @Override
    public Connection get() {
        if (this.closed) {
            throw new IllegalStateException(this + " is closed");
        }

        return super.get();
    }

    @Override
    public void remove() {
        final var connection = super.get();
        this.close(connection);
        this.pool.remove(connection);
        super.remove();
    }

    public void close() {
        this.closed = true;
        this.pool.forEach(this::close);
        this.pool.clear();
    }

    private void close(final Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (final SQLException sqle) {
                LOGGER.warn("Can not close connection " + this, sqle);
            }
        }
    }

    @Override
    public String toString() {
        return String.format(
                "%s :: %s@%s (%d connections - %s)",
                this.getClass().getSimpleName(),
                this.properties.get("user"),
                this.properties.getProperty("url"),
                this.pool.size(),
                this.closed ? "closed" : "open"
        );
    }
}
