import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataExtractor {

    public DataExtractor() {
        ScriptRunner sr = new ScriptRunner(DatabaseManager.getInstance().getConnection(), false, true);
        try {
            sr.runScript(new FileReader("dw-schema.sql"));
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        extractDBData();
        extractCSVData();
        try {
            DatabaseManager.getInstance().getConnection().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void extractDBData() {
        System.out.println("Extracting Database Data");
        System.out.println("Extracting Branches");
        List<DWBranch> branches = DWBranch.extractAllBranches();
        System.out.println("Storing Branches in Data Warehouse");
        branches.forEach(DWBranch::save);
        System.out.println("Extracting Products");
        List<DWProduct> products = DWProduct.getAllProducts();
        System.out.println("Storing Products in Data Warehouse");
        products.forEach(DWProduct::save);
    }

    private void extractCSVData() {
        System.out.println("Extracting CSV Data");
        List<DWFact> facts = new ArrayList<>();
        List<DWTime> times = new ArrayList<>();
        System.out.println("Reading CSV file");
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("sales.csv"), StandardCharsets.ISO_8859_1))) {
            String line;

            br.readLine();
            System.out.println("Store 5000 entries in list for batch insert");
            while ((line = br.readLine()) != null) {
                // process the line.
                DWTime time = DWTime.from(line);
                times.add(time);
                DWFact fact = DWFact.from(line);
                if (fact != null) {
                    facts.add(fact);
                }
                if (facts.size() == 5000) {
                    System.out.println("Batch list full, perform insert");
                    insertBatchData(times, facts);
                }
            }
            insertBatchData(times, facts);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insertBatchData(List<DWTime> times, List<DWFact> facts) {
        System.out.println("Batch insert Times");
        DWTime.insertBatch(times);
        System.out.println("Batch insert Facts");
        DWFact.insertBatch(facts);
        System.out.println("Clear Batch lists");
        facts.clear();
        times.clear();
        System.out.println("inserted Batch");
    }

}
