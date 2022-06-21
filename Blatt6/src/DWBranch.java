import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class DWBranch {

    private String country;
    private String region;
    private String city;
    private String shop;

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public static List<DWBranch> extractAllBranches() {
        // Hole Verbindung
        Connection con = DatabaseManager.getInstance().getConnection();

        List<DWBranch> listOfBranches = new LinkedList<>();

        try {

            String updateSQL =
                    "SELECT s.name as shop, ci.name as city, c.name as country, r.name as region FROM shop s, city ci, region r, country c WHERE s.cityid = ci.cityid AND ci.regionid = r.regionid AND r.countryid = c.countryid";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);
            ResultSet rs = pstmt.executeQuery();

            while ( rs.next() ) {
                DWBranch branch = new DWBranch();
                branch.setShop(rs.getString("shop"));
                branch.setCity(rs.getString("city"));
                branch.setRegion(rs.getString("region"));
                branch.setCountry(rs.getString("country"));
                listOfBranches.add(branch);
            }

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listOfBranches;
    }

    public void save() {
        // Hole Verbindung
        Connection con = DatabaseManager.getInstance().getConnection();

        try {
            // Falls schon eine ID vorhanden ist, mache ein Update...
            String updateSQL = "INSERT INTO dw_branch(country, region, city, shop) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);

            // Setze Anfrage Parameter
            pstmt.setString(1, getCountry());
            pstmt.setString(2, getRegion());
            pstmt.setString(3, getCity());
            pstmt.setString(4, getShop());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
