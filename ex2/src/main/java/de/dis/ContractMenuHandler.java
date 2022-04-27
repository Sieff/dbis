package main.java.de.dis;

import main.java.de.dis.data.*;

import java.util.Date;
import java.util.List;

public class ContractMenuHandler {

    private static EstateAgent agent;

    public static void showContractsMenu(EstateAgent agent) {
        ContractMenuHandler.agent = agent;


        final int NEW_CONTRACT = 0;
        final int INSERT_PERSON = 1;
        final int VIEW_ALL_CONTRACTS = 2;
        final int BACK = 3;

        Menu contractsMenu = new Menu("Vertragsverwaltung");
        contractsMenu.addEntry("Neuen Vertrag erstellen", NEW_CONTRACT);
        contractsMenu.addEntry("Füge Person hinzu", INSERT_PERSON);
        contractsMenu.addEntry("Vertragsübersicht", VIEW_ALL_CONTRACTS);
        contractsMenu.addEntry("Zurück zum Hauptmenü", BACK);

        while (true) {
            int response = contractsMenu.show();

            switch (response) {
                case NEW_CONTRACT:
                    newContract();
                    break;
                case INSERT_PERSON:
                    createPerson();
                    break;
                case VIEW_ALL_CONTRACTS:
                    viewAllContracts();
                    break;
                case BACK:
                    return;
            }
        }
    }

    public static void newContract() {
        final int TENANCY_CONTRACT = 0;
        final int SELL_CONTRACT = 1;

        String place = FormUtil.readString("Ort");
        Date date = FormUtil.readDate("Datum (Format: DD.MM.YYYY)");
        int personId = FormUtil.readInt("Id des Unterschreibenden");
        Person person = Person.loadById(personId);
        if (person != null) {
            System.out.println("Was für ein Vertrag soll erstellt werden?");
            Menu contractCreationMenu = new Menu("Objekt-Erstellung");
            contractCreationMenu.addEntry("Mietvertrag", TENANCY_CONTRACT);
            contractCreationMenu.addEntry("Kaufvertrag", SELL_CONTRACT);
            int response = contractCreationMenu.show();
            switch (response) {
                case TENANCY_CONTRACT -> newTenancyContact(place, date, personId);
                case SELL_CONTRACT -> newPurchaseContract(place, date, personId);
            }
        } else {
            System.out.println("Person existiert nicht.");
        }
    }

    public static void newTenancyContact(String place, Date date, int renterId) {
        TenancyContract tenancyContract = new TenancyContract();
        tenancyContract.setDate(date);
        tenancyContract.setPlace(place);
        tenancyContract.setStartDate(FormUtil.readDate("Startdatum (Format: DD.MM.YYYY)"));
        tenancyContract.setDurationMonths(FormUtil.readInt("Laufzeit"));
        tenancyContract.setAdditionalCosts(FormUtil.readFloat("Zusätzliche Kosten"));
        tenancyContract.setRenterId(renterId);
        boolean finished = false;
        while(!finished) {
            int apartmentId = FormUtil.readInt("Id des Apartments, welches vermietet werden soll");
            Estate estate = Estate.loadById(apartmentId);
            if (estate != null) {
                if (estate.getAgent_Id() == agent.getId()) {
                    Apartment apartment = Apartment.loadByEstate(estate);
                    if (apartment != null) {
                        tenancyContract.setApartmentId(apartmentId);
                        tenancyContract.save();
                        finished = true;
                    } else {
                        System.err.println("Apartment mit der ID " + apartmentId + " existiert nicht.");
                    }
                } else {
                    System.out.println("Kein Zugriff auf Objekt mit der ID " + apartmentId + " möglich.");
                }
            } else {
                System.err.println("Objekt mit der ID " + apartmentId + " existiert nicht.");
            }
        }
    }

    public static void newPurchaseContract(String place, Date date, int buyerId) {
        PurchaseContract purchaseContract = new PurchaseContract();
        purchaseContract.setDate(date);
        purchaseContract.setPlace(place);
        purchaseContract.setNumberOfInstallments(FormUtil.readInt("Anzahl an Raten"));
        purchaseContract.setInterestRate(FormUtil.readFloat("Zinssatz"));
        purchaseContract.setBuyerId(buyerId);
        boolean finished = false;
        while(!finished) {
            int houseId = FormUtil.readInt("Id des Hauses, welches verkauft werden soll");
            Estate estate = Estate.loadById(houseId);
            if (estate != null) {
                if (estate.getAgent_Id() == agent.getId()) {
                    House house = House.loadByEstate(estate);
                    if (house != null) {
                        purchaseContract.setHouseId(house.getId());
                        purchaseContract.save();
                        finished = true;
                    } else {
                        System.err.println("Haus mit der ID " + houseId + " existiert nicht.");
                    }
                } else {
                    System.out.println("Kein Zugriff auf Objekt mit der ID " + houseId + " möglich.");
                }
            } else {
                System.err.println("Objekt mit der ID " + houseId + " existiert nicht.");
            }
        }
    }

    public static void createPerson() {
        Person person = new Person();
        person.setFirstName(FormUtil.readString("Vorname"));
        person.setLastName(FormUtil.readString("Nachname"));
        person.setAddress(FormUtil.readString("Adresse"));
        person.save();
    }

    public static void viewAllContracts() {
        List<Contract> contractList = Contract.getAll();
        if (contractList != null) {
            for (Contract contract : contractList) {
                PurchaseContract purchaseContract = PurchaseContract.loadByContract(contract);
                if (purchaseContract != null) {
                    if (checkOwnership(purchaseContract.getHouseId())) {
                        purchaseContract.printDetails();
                        continue;
                    }
                }

                TenancyContract tenancyContract = TenancyContract.loadByContract(contract);
                if (tenancyContract != null) {
                    if (checkOwnership(tenancyContract.getApartmentId())) {
                        tenancyContract.printDetails();
                        continue;
                    }
                }
            }
            FormUtil.readString("Drücke die Enter-Taste um zurückzugehen.");
        } else {
            System.err.println("Es ist ein Fehler aufgetreten.");
        }
    }

    private static boolean checkOwnership(int estateId) {
        Estate estate = Estate.loadById(estateId);
        return estate != null && estate.getAgent_Id() == agent.getId();
    }

}
