package main.java.de.dis.data;

import java.sql.*;

public class PurchaseContract extends Contract {

    private int numberOfInstallments;
    private float interestRate;
    private int buyerId;
    private int houseId;

    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }

    public void setNumberOfInstallments(int numberOfInstallments) {
        this.numberOfInstallments = numberOfInstallments;
    }

    public float getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(float interestRate) {
        this.interestRate = interestRate;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getHouseId() {
        return houseId;
    }

    public void setHouseId(int houseId) {
        this.houseId = houseId;
    }

    @Override
    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {
            String insertSQL = "Insert INTO purchase_contract(date, place, number_of_installments, interest_rate, buyer_id, house_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setDate(1, getDateAsSQLDate());
            pstmt.setString(2, getPlace());
            pstmt.setInt(3, getNumberOfInstallments());
            pstmt.setFloat(4, getInterestRate());
            pstmt.setInt(5, getBuyerId());
            pstmt.setInt(6, getHouseId());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                setId(rs.getInt("contract_number"));
            }
            rs.close();
            pstmt.close();
            System.out.println("Vertrag mit ID " + getId() + " wurde erzeugt.");
            System.out.println();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static PurchaseContract loadByContract(Contract contract) {
        PurchaseContract purchaseContract = loadById(contract.getId());
        if (purchaseContract != null) {
            purchaseContract.getFromContract(contract);
            return purchaseContract;
        }
        return null;
    }

    public static PurchaseContract loadById(int id) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM purchase_contract WHERE contract_number = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                PurchaseContract purchaseContract = new PurchaseContract();
                purchaseContract.setId(rs.getInt("contract_number"));
                purchaseContract.setNumberOfInstallments(rs.getInt("number_of_installments"));
                purchaseContract.setInterestRate(rs.getFloat("interest_rate"));
                purchaseContract.setBuyerId(rs.getInt("buyer_id"));
                purchaseContract.setHouseId(rs.getInt("house_id"));
                rs.close();
                pstmt.close();
                return purchaseContract;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Anzahl der Ratenzahlungen: " + getNumberOfInstallments());
        System.out.println("Zinssatz: " + getInterestRate());
        System.out.println();
    }
}
