package main.java.de.dis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Locale;

public class RunnableOperation implements Runnable {

    public final char readwrite;
    public final String op;
    public final Connection c;

    public RunnableOperation(Connection connection, char readwrite, String operation) {

        this.readwrite = readwrite;
        this.op = operation;
        this.c = connection;
    }

    public void run(){
        System.out.println(Thread.currentThread().getName()+"sql = " + preparedOperation());
        Statement st;
        try {
            Statement lock = c.createStatement();
            lock.executeQuery(getLockOperation());
            st = c.createStatement();
            if (readwrite == 'r') {
                ResultSet rs = st.executeQuery(preparedOperation());
                while (rs.next())
                    System.out.println(rs.getString("name"));
            } else if (readwrite == 'w') {
                st.execute(preparedOperation());
            } else if (readwrite == 'c') {
                c.commit();
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private String preparedOperation() {
        String rtn = op.strip();
        rtn = !rtn.endsWith(";") ? rtn + ";" : rtn;
        rtn = switch (readwrite) {
            case 'r' -> op.replace(";", " FOR SHARE;");
            case 'w' -> op.replace(";", " FOR UPDATE;");
            default -> rtn;
        };
        return rtn;
    }

    private String getLockOperation() {
        if (readwrite == 'r') {
            return getLockOperationForShare();
        } else if (readwrite == 'w') {
            return getLockOperationForUpdate();
        }
        return "";
    }

    private String getLockOperationForShare() {
        String[] parts = op.toLowerCase(Locale.ROOT).split("from");
        String tableName = parts[1].split("where")[0].strip();
        String predicate = parts[1].split("where")[1].split(";")[0].strip();
        return "SELECT FROM " + tableName + " WHERE " + predicate + " FOR SHARE;";
    }

    private String getLockOperationForUpdate() {
        String tablename = op.toLowerCase(Locale.ROOT).split("set")[0].split("update")[1];
        String predicate = op.toLowerCase(Locale.ROOT).split("where")[1].split(";")[0];
        return "SELECT FROM" + tablename + "WHERE" + predicate + "FOR UPDATE";
    }
}
