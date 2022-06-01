package main.java.de.dis;

public class Page {
    private String data;
    private int lsn;
    private int pageId;

    public Page(int pageId, int lsn, String data) {
        this.lsn = lsn;
        this.data = data;
        this.pageId = pageId;
    }

    public int getPageId() {
        return pageId;
    }

    public int getLsn() {
        return lsn;
    }

    public String getData() {
        return data;
    }
}
