package main.java.de.dis;

import main.java.de.dis.data.Apartment;
import main.java.de.dis.data.Estate;
import main.java.de.dis.data.EstateAgent;
import main.java.de.dis.data.House;

public class EstateMenuHandler {

    private static EstateAgent agent;

    public static void showEstateMenu(EstateAgent agent) {
        EstateMenuHandler.agent = agent;

        //Menüoptionen
        final int NEW_ESTATE = 0;
        final int EDIT_ESTATE = 1;
        final int DELETE_ESTATE = 2;
        final int BACK = 3;

        //Objektverwaltungsmenü
        Menu estateMenu = new Menu("Objekt-Verwaltung");
        estateMenu.addEntry("Neues Objekt", NEW_ESTATE);
        estateMenu.addEntry("Objekt bearbeiten", EDIT_ESTATE);
        estateMenu.addEntry("Objekt löschen", DELETE_ESTATE);
        estateMenu.addEntry("Zurück zum Hauptmenü", BACK);

        //Verarbeite Eingabe
        while (true) {
            int response = estateMenu.show();

            switch (response) {
                case NEW_ESTATE:
                    newEstate();
                    break;
                case EDIT_ESTATE:
                    editEstate();
                    break;
                case DELETE_ESTATE:
                    deleteEstate();
                    break;
                case BACK:
                    return;
            }
        }
    }

    private static void newEstate() {

        String street = FormUtil.readString("Straße");
        int number = FormUtil.readInt("Hausnummer");
        String city = FormUtil.readString("In welcher Stadt");
        int postalCode = FormUtil.readInt("Postleitzahl");
        float square_area = FormUtil.readFloat("Quadratmeter");

        final int NEW_APARTMENT = 0;
        final int NEW_HOUSE = 1;
        //Objektverwaltungsmenü

        Menu estateCreationMenu = new Menu("Objekt-Erstellung");

        System.out.println("Was für ein Object soll erstellt werden?");

        estateCreationMenu.addEntry("Apartment", NEW_APARTMENT);
        estateCreationMenu.addEntry("Haus", NEW_HOUSE);

        //Verarbeite Eingabe
        int response = estateCreationMenu.show();
        switch (response) {
            case NEW_APARTMENT -> newApartment(street, number, city, postalCode, square_area);
            case NEW_HOUSE -> newHouse(street, number, city, postalCode, square_area);
        }
    }


    private static void newApartment(String street, int number, String city, int postalCode, float square_area) {
        Apartment apartment = new Apartment();
        apartment.setStreet(street);
        apartment.setStreet_Nr(number);
        apartment.setCity(city);
        apartment.setPostal_Code(postalCode);
        apartment.setSquare_Area(square_area);

        apartment.setBalcony(FormUtil.readBoolean("Mit Balkon (j/n)"));
        apartment.setFloor(FormUtil.readInt("Welches Stockwerk"));
        apartment.setRooms(FormUtil.readInt("Anzahl der Räume (Ganze Zahl)"));
        apartment.setAgent_Id(agent.getId());
        apartment.save();
    }

    private static void newHouse(String street, int number, String city, int postalCode, float square_area) {
        House house = new House();
        house.setStreet(street);
        house.setStreet_Nr(number);
        house.setCity(city);
        house.setPostal_Code(postalCode);
        house.setSquare_Area(square_area);

        house.setGarden(FormUtil.readBoolean("Mit Garten (j/n)"));
        house.setPrice(FormUtil.readFloat("Kaufpreis (Kommazahl)"));
        house.setFloors(FormUtil.readInt("Anzahl Stockwerke"));
        house.setAgent_Id(agent.getId());
        house.save();
    }


    private static void editEstate() {
        int id = FormUtil.readInt("ID des Objekts");

        Estate estate = Estate.loadById(id);

        if (estate != null) {

            if (estate.getAgent_Id() == agent.getId()) {

                Apartment apartment = Apartment.loadByEstate(estate);
                if (apartment != null) {
                    editApartment(apartment);
                    return;
                }

                House house = House.loadByEstate(estate);
                if (house != null) {
                    editHouse(house);
                    return;
                }
            } else {
                System.out.println("Kein Zugriff auf Objekt mit der ID " + id + " möglich.");
            }
        } else {
            System.out.println("Objekt mit der ID " + id + " wurde nicht gefunden.");
        }

    }

    private static void editApartment(Apartment apartment) {

        //Menüoptionen
        final int STREET = 0;
        final int STREET_NR = 1;
        final int CITY = 2;
        final int POSTAL_CODE = 3;
        final int SQUARE_AREA = 4;
        final int FLOOR = 5;
        final int RENT = 6;
        final int ROOMS = 7;
        final int BALCONY = 8;
        final int BUILT_IN_KITCHEN = 9;
        final int ABORT = 10;

        Menu editMenu = new Menu("Welches Attribut soll bearbeitet werden?");
        editMenu.addEntry("Straße", STREET);
        editMenu.addEntry("Hausnummer", STREET_NR);
        editMenu.addEntry("Stadt", CITY);
        editMenu.addEntry("Postleitzahl", POSTAL_CODE);
        editMenu.addEntry("Quadratmeter", SQUARE_AREA);
        editMenu.addEntry("Stockwerk", FLOOR);
        editMenu.addEntry("Miete", RENT);
        editMenu.addEntry("Räume", ROOMS);
        editMenu.addEntry("Mit Balkon", BALCONY);
        editMenu.addEntry("Mit Einbauküche", BUILT_IN_KITCHEN);
        editMenu.addEntry("Zurück zum Objektmenü", ABORT);

        //Verarbeite Eingabe
        while (true) {
            apartment.printDetails();
            int response = editMenu.show();

            switch (response) {
                case STREET:
                    apartment.setStreet(FormUtil.readString("Neue Straße"));
                    break;
                case STREET_NR:
                    apartment.setStreet_Nr(FormUtil.readInt("Neue Hausnummer"));
                    break;
                case CITY:
                    apartment.setCity(FormUtil.readString("Neue Stadt"));
                    break;
                case POSTAL_CODE:
                    apartment.setPostal_Code(FormUtil.readInt("Neue Postleitzahl"));
                    break;
                case SQUARE_AREA:
                    apartment.setSquare_Area(FormUtil.readFloat("Neue Quadratmeteranzahl"));
                    break;
                case FLOOR:
                    apartment.setFloor(FormUtil.readInt("Neues Stockwerk"));
                    break;
                case RENT:
                    apartment.setRent(FormUtil.readFloat("Neue Miete"));
                    break;
                case ROOMS:
                    apartment.setRooms(FormUtil.readInt("Neue Anzahl an Räumen"));
                    break;
                case BALCONY:
                    apartment.setBalcony(FormUtil.readBoolean("Hat Balkon (j/n)"));
                    break;
                case BUILT_IN_KITCHEN:
                    apartment.setBuilt_In_Kitchen(FormUtil.readBoolean("Hat Einbauküche (j/n)"));
                case ABORT:
                    return;
            }
            apartment.save();
        }
    }

    private static void editHouse(House house) {

        //Menüoptionen
        final int STREET = 0;
        final int STREET_NR = 1;
        final int CITY = 2;
        final int POSTAL_CODE = 3;
        final int SQUARE_AREA = 4;

        final int FLOORS = 5;
        final int PRICE = 6;
        final int GARDEN = 7;

        final int ABORT = 10;

        Menu editMenu = new Menu("Welches Attribut soll bearbeitet werden?");
        editMenu.addEntry("Straße", STREET);
        editMenu.addEntry("Hausnummer", STREET_NR);
        editMenu.addEntry("Stadt", CITY);
        editMenu.addEntry("Postleitzahl", POSTAL_CODE);
        editMenu.addEntry("Quadratmeter", SQUARE_AREA);
        editMenu.addEntry("Stockwerke", FLOORS);
        editMenu.addEntry("Preis", PRICE);
        editMenu.addEntry("Mit Garten", GARDEN);
        editMenu.addEntry("Zurück zum Objektmenü", ABORT);

        //Verarbeite Eingabe
        while (true) {
            house.printDetails();
            int response = editMenu.show();

            switch (response) {
                case STREET:
                    house.setStreet(FormUtil.readString("Neue Straße"));
                    break;
                case STREET_NR:
                    house.setStreet_Nr(FormUtil.readInt("Neue Hausnummer"));
                    break;
                case CITY:
                    house.setCity(FormUtil.readString("Neue Stadt"));
                    break;
                case POSTAL_CODE:
                    house.setPostal_Code(FormUtil.readInt("Neue Postleitzahl"));
                    break;
                case SQUARE_AREA:
                    house.setSquare_Area(FormUtil.readFloat("Neue Quadratmeteranzahl"));
                    break;
                case FLOORS:
                    house.setFloors(FormUtil.readInt("Neues Anzahl Stockwerke"));
                    break;
                case PRICE:
                    house.setPrice(FormUtil.readFloat("Neuer Preis"));
                    break;
                case GARDEN:
                    house.setGarden(FormUtil.readBoolean("Mit Garten (j/n)"));
                    break;
                case ABORT:
                    return;
            }
            house.save();
        }
    }

    private static void deleteEstate() {
        int id = FormUtil.readInt("ID des Objektes");
        Estate estate = Estate.loadById(id);
        //Menüoptionen
        final int DELETE = 0;
        final int ABORT = 1;

        if (estate != null) {

            if (estate.getAgent_Id() == agent.getId()) {

                //lösch menu
                Menu maklerMenu = new Menu("Soll das Objekt mit der ID " + estate.getId() + " wirklich gelöscht werden?");
                maklerMenu.addEntry("Löschen", DELETE);
                maklerMenu.addEntry("NICHT löschen", ABORT);

                //Verarbeite Eingabe
                while (true) {
                    int response = maklerMenu.show();

                    switch (response) {
                        case DELETE:
                            estate.delete();
                        case ABORT:
                            return;
                    }
                }
            } else {
                System.out.println("Kein Zugriff auf Objekt mit der ID " + id + " möglich.");
            }
        } else {
            System.out.println("Objekt mit der ID " + id + " wurde nicht gefunden.");
        }
    }

}