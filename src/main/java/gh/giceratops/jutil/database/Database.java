package gh.giceratops.jutil.database;

import gh.giceratops.jutil.Suppliers;

import java.sql.Connection;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Database {

    private final DatabaseConnection connection;
    private final Executor executor;

    public Database(final Properties props, final Executor executor) {
        this.connection = new DatabaseConnection(Objects.requireNonNull(props));
        this.executor = Objects.requireNonNull(executor);
    }

    public Connection connection() {
        return this.connection.get();
    }

    public void shutdown() throws InterruptedException {
        this.connection.close();
        if (this.executor instanceof ExecutorService) {
            ((ExecutorService) this.executor).shutdown();
            ((ExecutorService) this.executor).awaitTermination(5, TimeUnit.MINUTES);
        }
    }

    public <O extends DatabaseObject<O>> CompletableFuture<O> load(final O o) {
        return CompletableFuture.supplyAsync(Suppliers.unchecked(() -> o.load(this.connection())), executor);
    }

    public <O extends DatabaseObject<O>> CompletableFuture<O> save(final O o) {
        return CompletableFuture.supplyAsync(Suppliers.unchecked(() -> o.save(this.connection())), executor);
    }

    public <O extends DatabaseObject<O>> CompletableFuture<O> delete(final O o) {
        return CompletableFuture.supplyAsync(Suppliers.unchecked(() -> o.delete(this.connection())), executor);
    }
}
