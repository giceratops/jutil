package gh.giceratops.jutil.database;

import gh.giceratops.jutil.Suppliers;
import lombok.NonNull;

import java.sql.Connection;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class Database {

    private final DatabaseConnection connection;
    private final Executor executor;

    public Database(@NonNull final Properties props, @NonNull final Executor executor) {
        this.connection = new DatabaseConnection(props);
        this.executor = executor;
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

    public <O extends DatabaseObject<O>> CompletableFuture<O> load(@NonNull final O o) {
        return CompletableFuture.supplyAsync(Suppliers.unchecked(() -> o.load(this.connection())), executor);
    }

    public <O extends DatabaseObject<O>> CompletableFuture<O> save(@NonNull final O o) {
        return CompletableFuture.supplyAsync(Suppliers.unchecked(() -> o.save(this.connection())), executor);
    }

    public <O extends DatabaseObject<O>> CompletableFuture<O> delete(@NonNull final O o) {
        return CompletableFuture.supplyAsync(Suppliers.unchecked(() -> o.delete(this.connection())), executor);
    }
}
