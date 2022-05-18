package main.java.de.dis;

public class Operation {
    private int transactionId;
    private int pageId;
    private String data;

    public Operation(int transactionId, int pageId, String data) {
        this.transactionId = transactionId;
        this.pageId = pageId;
        this.data = data;
    }

    public int getPageId() {
        return pageId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getData() {
        return data;
    }

    public String toLogEntry(int lsn) {
        return lsn + "," + transactionId + "," + pageId + "," + data + "\n";
    }
}
