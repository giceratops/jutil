package gh.giceratops.jutil.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseObject<O extends DatabaseObject<O>> {

    O load(final Connection con) throws SQLException;

    O save(final Connection con) throws SQLException;

    O delete(final Connection con) throws SQLException;
}
