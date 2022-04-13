package main.java.de.dis;

import main.java.de.dis.data.EstateAgent;

public class EstateAgentMenuHandler {

    /**
     * Zeigt die Maklerverwaltung
     */
    public static void showMaklerMenu() {
        //Menüoptionen
        final int NEW_MAKLER = 0;
        final int EDIT_MAKLER = 1;
        final int DELETE_MAKLER = 2;
        final int BACK = 3;

        //Maklerverwaltungsmenü
        Menu maklerMenu = new Menu("Makler-Verwaltung");
        maklerMenu.addEntry("Neuer Makler", NEW_MAKLER);
        maklerMenu.addEntry("Makler bearbeiten", EDIT_MAKLER);
        maklerMenu.addEntry("Makler löschen", DELETE_MAKLER);
        maklerMenu.addEntry("Zurück zum Hauptmenü", BACK);

        //Verarbeite Eingabe
        while(true) {
            int response = maklerMenu.show();

            switch(response) {
                case NEW_MAKLER:
                    newMakler();
                    break;
                case EDIT_MAKLER:
                    editMakler();
                    break;
                case DELETE_MAKLER:
                    deleteMakler();
                    break;
                case BACK:
                    return;
            }
        }
    }

    /**
     * Legt einen neuen Makler an, nachdem der Benutzer
     * die entsprechenden Daten eingegeben hat.
     */
    private static void newMakler() {
        EstateAgent m = new EstateAgent();

        m.setName(FormUtil.readString("Name"));
        m.setAddress(FormUtil.readString("Adresse"));
        m.setLogin(FormUtil.readString("Login"));
        m.setPassword(FormUtil.readString("Passwort"));
        m.save();
    }

    private static void editMakler() {
        String login = FormUtil.readString("Login");
        EstateAgent estate_agent = EstateAgent.loadByLogin(login);

        if (estate_agent != null) {
            //Menüoptionen
            final int NAME = 0;
            final int ADDRESS = 1;
            final int LOGIN = 2;
            final int PASSWORD = 3;
            final int ABORT = 4;

            estate_agent.printDetails();

            Menu editMenu = new Menu("Welches Attribut soll bearbeitet werden?");
            editMenu.addEntry("Name", NAME);
            editMenu.addEntry("Address", ADDRESS);
            editMenu.addEntry("Login", LOGIN);
            editMenu.addEntry("Passwort", PASSWORD);
            editMenu.addEntry("Zurück zum Maklermenü", ABORT);

            //Verarbeite Eingabe
            while (true) {
                int response = editMenu.show();

                switch (response) {
                    case NAME:
                        estate_agent.setName(FormUtil.readString("Neuer Name"));
                        break;
                    case ADDRESS:
                        estate_agent.setAddress(FormUtil.readString("Neue Addresse"));
                        break;
                    case LOGIN:
                        estate_agent.setLogin(FormUtil.readString("Neuer Login"));
                        break;
                    case PASSWORD:
                        estate_agent.setPassword(FormUtil.readString("Neues Passwort"));
                        break;
                    case ABORT:
                        return;
                }
                estate_agent.save();
            }

        } else {
            System.out.println("Makler mit dem Login " + login + " wurde nicht gefunden.");
        }

    }

    private static void deleteMakler() {
        String login = FormUtil.readString("Login");
        EstateAgent estate_agent = EstateAgent.loadByLogin(login);
        //Menüoptionen
        final int DELETE = 0;
        final int ABORT = 1;

        if (estate_agent != null) {

            //lösch menu
            Menu maklerMenu = new Menu("Soll der Makler mit dem Login " + estate_agent.getLogin() + " wirklich gelöscht werden?");
            maklerMenu.addEntry("Löschen", DELETE);
            maklerMenu.addEntry("NICHT löschen", ABORT);

            //Verarbeite Eingabe
            while (true) {
                int response = maklerMenu.show();

                switch (response) {
                    case DELETE:
                        estate_agent.delete();
                    case ABORT:
                        return;
                }
            }
        } else {
            System.out.println("Makler mit dem Login " + login + " wurde nicht gefunden.");
        }
    }

}
