public class Analyser {

    public Analyser() {
        // ask user for granularity
        new CrossTable();
    }

    /**
     * Produces output that the manager can use.
     *  The desired granularity level
     *  of each dimension is given by the parameters; e.g. geo = "country" is the most general
     *  and geo = "shop" is the most fine-grained granularity level for the geographical dimension.
     *  @param product
     *  admissible values: article , productGroup , productFamily
     *  @param time
     *  admissible values: date, day, month, quarter, year
     *  @param geo
     *  admissible values: shop, city, region, country
     *  @throws java.sql.SQLException
     */
    private static void analysis(String geo, String time, String product) {

    }
}
