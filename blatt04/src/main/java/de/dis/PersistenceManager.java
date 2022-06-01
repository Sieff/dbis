package main.java.de.dis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class PersistenceManager {

    private static final PersistenceManager instance = new PersistenceManager();
    private Hashtable<Integer, Operation> buffer = new Hashtable<>();
    private int lastTransactionId = -1;
    private int lsn = -1;

    private PersistenceManager() {
        File pagesDirectory = new File("pages");
        File logDirectory = new File("logs.txt");
        if (!pagesDirectory.exists()) {
            pagesDirectory.mkdir();
        }
        if (!logDirectory.exists()) {
            try {
                logDirectory.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        loadValues();
        System.out.println("Objekt gebildet...");
    }

    public static PersistenceManager getInstance() {
        return instance;
    }

    /**
     * starts a new transaction.
     * The persistence manager creates a unique transaction ID and returns it to the client.
     * @return the transactionId of the created transaction
     */
    public int beginTransaction() {
        System.out.println("Begin of Transaction");
        return ++lastTransactionId;
    }

    synchronized public void write(int transactionId, int pageId, String data) {
        Operation operation = new Operation(transactionId, pageId, data);
        buffer.put(pageId, operation);
        writeLogEntry(operation);
        checkIfBufferCanBeCleared();
        System.out.println("Write operation successful");
    }

    private void writeLogEntry(Operation o) {
        try {
            FileWriter logWriter = new FileWriter("logs.txt", true);
            logWriter.write(o.toLogEntry(++lsn));
            logWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkIfBufferCanBeCleared() {
        System.out.println("Current buffer size: " + buffer.size());
        if (buffer.size() > 5) {
            List<Operation> entriesToRemove = new ArrayList<>();
            Collection<Operation> values = buffer.values();
            for (Operation bufferEntry: values) {
                if (isTransactionCommitted(bufferEntry.getTransactionId())) {
                    entriesToRemove.add(bufferEntry);
                    writeToDatabase(bufferEntry);
                    System.out.println("Cleared bufferEntry with transactionId: " + bufferEntry.getTransactionId());
                }
            }
            entriesToRemove.forEach(entry -> buffer.remove(entry.getPageId()));
        }
    }

    private boolean isTransactionCommitted(int transactionId) {
        File logFile = new File("logs.txt");
        try {
            List<String> content = Files.readAllLines(logFile.toPath());
            for (String logEntry : content) {
                String[] values = logEntry.split(",");
                if (Integer.parseInt(values[1]) == transactionId) {
                    if (values[2].equals("EOT")) {
                        return true;
                    }
                }
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void writeToDatabase(Operation objectToWrite) {
        try {
            String fileName = "pages/" + objectToWrite.getPageId() + ".txt";
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter pageWriter = new FileWriter(fileName);
            pageWriter.write(lsn + "," + objectToWrite.getData());
            pageWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * commits the transaction specified by the given transaction ID.
     * @param transactionId
     */
    public void commit(int transactionId) {
        try {
            FileWriter logWriter = new FileWriter("logs.txt", true);
            logWriter.write(++lsn + "," + transactionId + ",EOT\n");
            logWriter.close();
            System.out.println("Transaction with ID " + transactionId + " committed");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadValues() {
        File logFile = new File("logs.txt");
        try {
            List<String> content = Files.readAllLines(logFile.toPath());
            for (String logEntry : content) {
                String[] values = logEntry.split(",");
                int localTransactionId = Integer.parseInt(values[1]);
                if (localTransactionId > lastTransactionId) {
                    lastTransactionId = localTransactionId;
                }
                int localLSN = Integer.parseInt(values[0]);
                if (localLSN > lsn) {
                    lsn = localLSN;
                }
            }
            System.out.println("Highest TransactionId: " + lastTransactionId);
            System.out.println("Highest LSN: " + lsn);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
