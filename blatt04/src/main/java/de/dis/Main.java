package main.java.de.dis;

public class Main {

    public static void main(String[] args) {
        //executeClientWrites();
        recover();
    }

    private static void recover() {
        RecoveryTool recoveryTool = new RecoveryTool();
        recoveryTool.performRecovery();
    }

    private static void executeClientWrites() {
        new Thread(() -> {
            Client client = new Client();
            client.executeTestWrite1();
        }).start();

        new Thread(() -> {
            Client client = new Client();
            client.executeTestWrite2();
        }).start();

        new Thread(() -> {
            Client client = new Client();
            client.executeTestWrite3();
        }).start();

        new Thread(() -> {
            Client client = new Client();
            client.executeTestWrite4();
        }).start();
    }

}
