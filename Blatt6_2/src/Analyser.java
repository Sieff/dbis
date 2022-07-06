import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Analyser {
    private CrossTable crossTable = new CrossTable();
    private int lineLength;

    public Analyser() {
        //Erzeuge Menü
        Menu branchMenu = new Menu("Select branch granulate...");
        branchMenu.addEntry("Shop", BranchGran.shop.ordinal());
        branchMenu.addEntry("City", BranchGran.city.ordinal());
        branchMenu.addEntry("Region", BranchGran.region.ordinal());
        branchMenu.addEntry("Country", BranchGran.country.ordinal());

        //Verarbeite Eingabe
        int branchResponse = branchMenu.show();
        crossTable.setBranchGran(BranchGran.values()[branchResponse]);

        Menu productMenu = new Menu("Select product granulate...");
        productMenu.addEntry("Article", BranchGran.shop.ordinal());
        productMenu.addEntry("Product group", BranchGran.city.ordinal());
        productMenu.addEntry("Product family", BranchGran.region.ordinal());

        //Verarbeite Eingabe
        int productResponse = productMenu.show();
        crossTable.setProductGran(ProductGran.values()[productResponse]);

        Menu timeMenu = new Menu("Select time granulate...");
        timeMenu.addEntry("Date", TimeGran.date.ordinal());
        timeMenu.addEntry("Day", TimeGran.day.ordinal());
        timeMenu.addEntry("Month", TimeGran.month.ordinal());
        timeMenu.addEntry("Quarter", TimeGran.quarter.ordinal());
        timeMenu.addEntry("Year", TimeGran.year.ordinal());

        //Verarbeite Eingabe
        int response = timeMenu.show();
        crossTable.setTimeGran(TimeGran.values()[response]);
        // ask user for granularity

        int numberOfRows = FormUtil.readInt("Set number of rows to display...");

        crossTable.setNumberOfRows(numberOfRows);
        analysis();

        while (true) {

            final int DRILL_UP = 0;
            final int DRILL_DOWN = 1;
            final int NEXT_PAGE = 2;
            final int PREVIOUS_PAGE = 3;
            final int AGGREGATE_COLUMN = 4;

            Menu analysisMenu = new Menu("Select Operation");
            analysisMenu.addEntry("Drill up", DRILL_UP);
            analysisMenu.addEntry("Drill down", DRILL_DOWN);
            analysisMenu.addEntry("Next page", NEXT_PAGE);
            analysisMenu.addEntry("Previous page", PREVIOUS_PAGE);
            analysisMenu.addEntry("Toggle aggregation for column", AGGREGATE_COLUMN);

            int analysisResponse = analysisMenu.show();

            switch (analysisResponse) {
                case DRILL_UP:
                    drillUp();
                    break;
                case DRILL_DOWN:
                    drillDown();
                    break;
                case NEXT_PAGE:
                    crossTable.nextPage();
                    break;
                case PREVIOUS_PAGE:
                    crossTable.previousPage();
                    break;
                case AGGREGATE_COLUMN:
                    aggregateColumn();
                    break;
            }
        }
    }

    private void drillUp() {
        final int BRANCH = 0;
        final int PRODUCT = 1;
        final int TIME = 2;

        Menu drillMenu = new Menu("Select Dimension");
        drillMenu.addEntry("Branch", BRANCH);
        drillMenu.addEntry("Product", PRODUCT);
        drillMenu.addEntry("Time", TIME);

        int analysisResponse = drillMenu.show();

        switch (analysisResponse) {
            case BRANCH:
                crossTable.setBranchGran(crossTable.getBranchGran().next());
                break;
            case PRODUCT:
                crossTable.setProductGran(crossTable.getProductGran().next());
                break;
            case TIME:
                crossTable.setTimeGran(crossTable.getTimeGran().next());
                break;
        }
        crossTable.setCurrentPage(0);
        analysis();
    }

    private void drillDown() {
        final int BRANCH = 0;
        final int PRODUCT = 1;
        final int TIME = 2;

        Menu drillMenu = new Menu("Select Dimension");
        drillMenu.addEntry("Branch", BRANCH);
        drillMenu.addEntry("Product", PRODUCT);
        drillMenu.addEntry("Time", TIME);

        int analysisResponse = drillMenu.show();

        switch (analysisResponse) {
            case BRANCH -> crossTable.setBranchGran(crossTable.getBranchGran().previous());
            case PRODUCT -> crossTable.setProductGran(crossTable.getProductGran().previous());
            case TIME -> crossTable.setTimeGran(crossTable.getTimeGran().previous());
        }
        crossTable.setCurrentPage(0);
        analysis();
    }

    private void aggregateColumn() {

        final int BRANCH = 0;
        final int PRODUCT = 1;
        final int TIME = 2;
        Menu aggregateMenu = new Menu("For which dimension do you want to toggle the aggregation?");
        aggregateMenu.addEntry("Branch", BRANCH);
        aggregateMenu.addEntry("Product", PRODUCT);
        aggregateMenu.addEntry("Time", TIME);

        int aggregateResponse = aggregateMenu.show();

        switch (aggregateResponse) {
            case BRANCH -> crossTable.setAggregateBranch(!crossTable.isAggregateBranch());
            case PRODUCT -> crossTable.setAggregateProduct(!crossTable.isAggregateProduct());
            case TIME -> crossTable.setAggregateTime(!crossTable.isAggregateTime());
        }
        crossTable.setCurrentPage(0);
        analysis();
    }

    private void analysis() {
        analysis(crossTable.getBranchGran(), crossTable.getProductGran(), crossTable.getTimeGran());
    }

    /**
     * Produces output that the manager can use.
     *  The desired granularity level
     *  of each dimension is given by the parameters; e.g. geo = "country" is the most general
     *  and geo = "shop" is the most fine-grained granularity level for the geographical dimension.
     *  *  @param branchGran
     *  admissible values: shop, city, region, country
     *  @param productGran
     *  admissible values: article , productGroup , productFamily
     *  @param timeGran
     *  admissible values: date, day, month, quarter, year
     */
    private void analysis(BranchGran branchGran, ProductGran productGran, TimeGran timeGran) {
        loadData(branchGran, productGran, timeGran);
        crossTable.printData();
    }

    private void loadData(BranchGran branchGran, ProductGran productGran, TimeGran timeGran) {
        // Hole Verbindung
        Connection con = DatabaseManager.getInstance().getConnection();

        try {
            crossTable.clearData();
            String updateSQL =
                    "SELECT b." + branchGran.toString() + " , p." + productGran.toString() + ", t." + timeGran.toString() + ", sum(sales) FROM dw_fact f, dw_branch b, dw_product p, dw_time t WHERE f.date = t.date and f.shop = b.shop and f.article = p.article GROUP BY CUBE (1, 2, 3) ORDER BY 1,2,3";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);
            ResultSet rs = pstmt.executeQuery();

            while ( rs.next() ) {
                Cuboid cuboid = new Cuboid();
                cuboid.setBranchLabel(rs.getString(branchGran.toString()));
                cuboid.setProductLabel(rs.getString(productGran.toString()));
                cuboid.setTimeLabel(rs.getString(timeGran.toString()));
                cuboid.setValue(rs.getInt("sum"));
                crossTable.add(cuboid);
            }

            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
