package main.java.de.dis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecoveryTool {

    public void performRecovery() {
        List<String> logEntries = loadAllLogEntries();
        List<Integer> winnerTransactionIds = new ArrayList<>();
        for (String logEntry : logEntries) {
            String[] values = logEntry.split(",");
            if (values[2].equals("EOT")) {
                winnerTransactionIds.add(Integer.parseInt(values[1]));
            }
        }
        for (String logEntry : logEntries) {
            String[] values = logEntry.split(",");
            if (winnerTransactionIds.contains(Integer.parseInt(values[1])) &&
                    !values[2].equals("EOT")) {
                int logLsn = Integer.parseInt(values[0]);
                int pageId = Integer.parseInt(values[2]);
                Page page = loadPage(pageId);
                if (page != null) {
                    if (logLsn > page.getLsn()) {
                        // write page
                        writePage(pageId, logLsn, values[3]);
                    }
                } else {
                    writePage(pageId, logLsn, values[3]);
                }
            }
        }
    }

    private Page loadPage(int pageId) {
        File pageFile = new File("pages/" + pageId + ".txt");
        try {
            String content = Files.readAllLines(pageFile.toPath()).get(0);
            String[] values = content.split(",");
            return new Page(pageId, Integer.parseInt(values[0]), values[1]);
        } catch (IOException e) {
            // page does not exist yet, needs to be created in recovery
            System.out.println("Page with id " + pageId + " does not exist, create...");
        }
        return null;
    }

    private void writePage(int pageId, int newLsn, String newData) {
        try {
            String fileName = "pages/" + pageId + ".txt";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter pageWriter = new FileWriter(fileName);
            pageWriter.write(newLsn + "," + newData);
            pageWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> loadAllLogEntries() {
        File logFile = new File("logs.txt");

        try {
            return Files.readAllLines(logFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

}
