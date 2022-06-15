import com.opencsv.CSVReaderHeaderAware;

import java.io.File;
import java.util.List;
import java.io. * ;
import java.util.Map;
import java.util.Scanner;

public class DataExtractor {

    public DataExtractor() {
        extractDBData();
        extractCSVData();
    }

    private void extractDBData() {
        List<DWBranch> branches = DWBranch.extractAllBranches();
        branches.forEach(DWBranch::save);
        List<DWProduct> products = DWProduct.getAllProducts();
        products.forEach(DWProduct::save);
    }

    private void extractCSVData() {
        try {
            Map<String, String> values = new CSVReaderHeaderAware(new FileReader("sales.csv")).readMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int i = 0;
        /*
        File csvDataFile = new File("sales.csv");
        try {
            Scanner sc = new Scanner(csvDataFile);
            //Skipping Header line
            sc.next();
            while (sc.hasNext()) {
                DWFact fact = DWFact.from(sc.next());
                fact.save();
            }
            sc.close();
            //closes the scanner
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         */
    }



}
