import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DWFact {
    private String article;
    private String shop;
    private Date date;
    private int sales;
    private float revenue;

    private DWFact(String article, String shop, Date date, int sales, float revenue) {
        this.article = article;
        this.shop = shop;
        this.date = date;
        this.sales = sales;
        this.revenue = revenue;
    }

    public static DWFact from(String strToParse) {
        String[] parts = strToParse.split(";");
        String date = parts[0];
        String shop = parts[1];
        String article = parts[2];
        String sales = parts[3];
        String revenue = parts[4];
        return new DWFact(article, shop, parseDate(date), Integer.parseInt(sales), Float.parseFloat(revenue));
    }

    private static Date parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);
        LocalDate localDate = LocalDate.parse(date, formatter);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public void save() {
        // Hole Verbindung
        Connection con = DatabaseManager.getInstance().getConnection();

        try {
            String updateSQL = "INSERT INTO dw_fact(article, shop, date, sales, revenue) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);

            // Setze Anfrage Parameter
            pstmt.setString(1, getArticle());
            pstmt.setString(2, getShop());
            pstmt.setDate(3, new java.sql.Date(getDate().getTime()));
            pstmt.setInt(4, getSales());
            pstmt.setFloat(5, getRevenue());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public float getRevenue() {
        return revenue;
    }

    public void setRevenue(float revenue) {
        this.revenue = revenue;
    }
}
