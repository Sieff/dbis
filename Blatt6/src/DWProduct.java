import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;

public class DWProduct {

    private String article;
    private float price;
    private String productGroup;
    private String productFamily;
    private String productCategory;

    public static List<DWProduct> getAllProducts() {
        // Hole Verbindung
        Connection con = DatabaseManager.getInstance().getConnection();

        List<DWProduct> listOfProducts = new LinkedList<>();

        try {

            String updateSQL =
                    "SELECT a.name as article, a.price, pg.name as product_group, pf.name as product_family, pc.name as product_category FROM article a, productfamily pf, productgroup pg, productcategory pc " +
                            "WHERE a.productgroupid = pg.productgroupid and pg.productfamilyid = pf.productfamilyid and " +
                            "pf.productcategoryid = pc.productcategoryid";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);
            ResultSet rs = pstmt.executeQuery();

            while ( rs.next() ) {
                DWProduct product = new DWProduct();
                product.setArticle(rs.getString("article"));
                float price =  rs.getFloat("price");
                BigDecimal bd = new BigDecimal(Float.toString(price));
                bd = bd.setScale(2, RoundingMode.HALF_UP);
                product.setPrice(bd.floatValue());
                product.setProductGroup(rs.getString("product_group"));
                product.setProductFamily(rs.getString("product_family"));
                product.setProductCategory(rs.getString("product_category"));
                listOfProducts.add(product);
            }

            pstmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfProducts;
    }

    public void save() {
        // Hole Verbindung
        Connection con = DatabaseManager.getInstance().getConnection();

        try {
            String updateSQL = "INSERT INTO dw_product(product_category, product_family, product_group, article, price) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(updateSQL, Statement.RETURN_GENERATED_KEYS);

            // Setze Anfrage Parameter
            pstmt.setString(1, getProductCategory());
            pstmt.setString(2, getProductFamily());
            pstmt.setString(3, getProductGroup());
            pstmt.setString(4, getArticle());
            pstmt.setFloat(5, getPrice());

            pstmt.executeUpdate();

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getProductFamily() {
        return productFamily;
    }

    public void setProductFamily(String productFamily) {
        this.productFamily = productFamily;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getArticle() {
        return article;
    }

    public void setArticle(String article) {
        this.article = article;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
}
