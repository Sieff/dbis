package main.java.de.dis;

import java.util.concurrent.ThreadLocalRandom;

public class Client {

    public void executeTestWrite1() {
        PersistenceManager manager = PersistenceManager.getInstance();
        int transactionId = manager.beginTransaction();
        sleepRandomAmount();
        manager.write(transactionId, 0, "client1dataPage0");
        sleepRandomAmount();
        manager.write(transactionId, 1, "client1dataPage1");
        sleepRandomAmount();
        manager.write(transactionId, 1, "client1dataPage1Version1");
        sleepRandomAmount();
        manager.write(transactionId, 2, "client1dataPage2");
        sleepRandomAmount();
        manager.write(transactionId, 3, "client1dataPage3");
        sleepRandomAmount();
        manager.write(transactionId, 4, "client1dataPage4");
        sleepRandomAmount();
        manager.write(transactionId, 5, "client1dataPage5");
        sleepRandomAmount();
        manager.commit(transactionId);
    }

    public void executeTestWrite2() {
        PersistenceManager manager = PersistenceManager.getInstance();
        int transactionId = manager.beginTransaction();
        sleepRandomAmount();
        manager.write(transactionId, 10, "client2dataPage10");
        sleepRandomAmount();
        manager.write(transactionId, 11, "client2dataPage11");
        sleepRandomAmount();
        manager.write(transactionId, 11, "client2dataPage11Version1");
        sleepRandomAmount();
        manager.write(transactionId, 12, "client2dataPage12");
        sleepRandomAmount();
        manager.write(transactionId, 13, "client2dataPage13");
        sleepRandomAmount();
        manager.write(transactionId, 14, "client2dataPage14");
        sleepRandomAmount();
        manager.write(transactionId, 15, "client2dataPage15");
        sleepRandomAmount();
        manager.commit(transactionId);
    }

    public void executeTestWrite3() {
        PersistenceManager manager = PersistenceManager.getInstance();
        int transactionId = manager.beginTransaction();
        sleepRandomAmount();
        manager.write(transactionId, 20, "client3dataPage20");
        sleepRandomAmount();
        manager.write(transactionId, 21, "client3dataPage21");
        sleepRandomAmount();
        manager.write(transactionId, 21, "client3dataPage21Version1");
        sleepRandomAmount();
        manager.write(transactionId, 22, "client3dataPage22");
        sleepRandomAmount();
        manager.write(transactionId, 23, "client3dataPage23");
        sleepRandomAmount();
        manager.write(transactionId, 24, "client3dataPage24");
        sleepRandomAmount();
        manager.write(transactionId, 21, "client3dataPage21Version2");
        sleepRandomAmount();
        manager.write(transactionId, 22, "client3dataPage22Version1");
        sleepRandomAmount();
        manager.write(transactionId, 23, "client3dataPage23Version1");
        sleepRandomAmount();
        manager.write(transactionId, 24, "client3dataPage24Version1");
        sleepRandomAmount();
        manager.write(transactionId, 25, "client3dataPage25Version1");
        sleepRandomAmount();
        manager.commit(transactionId);
    }

    public void executeTestWrite4() {
        PersistenceManager manager = PersistenceManager.getInstance();
        int transactionId = manager.beginTransaction();
        sleepRandomAmount();
        manager.write(transactionId, 30, "client4dataPage30");
        sleepRandomAmount();
        manager.write(transactionId, 34, "client4dataPage34");
        sleepRandomAmount();
        manager.write(transactionId, 35, "client4dataPage35");
        sleepRandomAmount();
        manager.commit(transactionId);
    }

    private void sleepRandomAmount() {
        try {
            Thread.sleep(ThreadLocalRandom.current().nextInt(10, 51));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
