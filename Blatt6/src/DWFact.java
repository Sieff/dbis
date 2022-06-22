import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
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
        try {
            int salesAsInt = Integer.parseInt(sales);
            float revenueAsFloat = Float.parseFloat(revenue.replace(",", "."));
            return new DWFact(article, shop, parseDate(date), salesAsInt, revenueAsFloat);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        System.out.println("Faulty data set detected, discard. Data: " + strToParse);
        return null;
    }

    private static Date parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);
        LocalDate localDate = LocalDate.parse(date, formatter);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public static void insertBatch(List<DWFact> facts) {

        // Hole Verbindung
        Connection con = DatabaseManager.getInstance().getConnection();

        try {
            String updateSQL = "INSERT INTO dw_fact(article, shop, date, sales, revenue) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);

            for (DWFact fact : facts) {
                // Setze Anfrage Parameter
                pstmt.setString(1, fact.getArticle());
                pstmt.setString(2, fact.getShop());
                pstmt.setDate(3, new java.sql.Date(fact.getDate().getTime()));
                pstmt.setInt(4, fact.getSales());
                pstmt.setFloat(5, fact.getRevenue());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException e) {
            System.out.println("Error when executing batch insert");
            BatchUpdateException exception = (BatchUpdateException) e;
            int[] array = exception.getUpdateCounts();
            int index = 0;
            while (array[index] == 1) {
                index++;
            }
            System.out.println("Index is: " + index +", faulty data set expected in the next 200 entries.");
            System.out.println("Skip the next 200 and do them one by one.");
            facts.subList(index, index + 200).forEach(DWFact::save);
            System.out.println("Continue with batch insert after removing the 200.");
            insertBatch(facts.subList(index + 200, facts.size()));
        }
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
