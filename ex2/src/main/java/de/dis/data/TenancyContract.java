package main.java.de.dis.data;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TenancyContract extends Contract {

    private Date startDate;
    private int durationMonths;
    private float additionalCosts;
    private int renterId;
    private int apartmentId;

    public Date getStartDate() {
        return startDate;
    }

    public java.sql.Date getStartDateAsSQLDate() {
        return new java.sql.Date(getStartDate().getTime());
    }

    public String getPrettyStartDate() {
        String pattern = "dd.MM.yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(getStartDate());
    }

    public void setStartDate(Date date) {
        this.startDate = date;
    }

    public int getDurationMonths() {
        return durationMonths;
    }

    public void setDurationMonths(int durationInMonths) {
        this.durationMonths = durationInMonths;
    }

    public float getAdditionalCosts() {
        return additionalCosts;
    }

    public void setAdditionalCosts(float additionalCosts) {
        this.additionalCosts = additionalCosts;
    }

    public int getRenterId() {
        return renterId;
    }

    public void setRenterId(int renterId) {
        this.renterId = renterId;
    }

    public int getApartmentId() {
        return apartmentId;
    }

    public void setApartmentId(int apartmentId) {
        this.apartmentId = apartmentId;
    }

    @Override
    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {
            String insertSQL = "Insert INTO tenancy_contract(date, place, start_date, duration_months, additional_costs) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setDate(1, getDateAsSQLDate());
            pstmt.setString(2, getPlace());
            pstmt.setDate(3, getStartDateAsSQLDate());
            pstmt.setInt(4, getDurationMonths());
            pstmt.setFloat(5, getAdditionalCosts());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                setId(rs.getInt("contract_number"));
            }
            rs.close();
            pstmt.close();
            System.out.println("Vertrag mit ID " + getId() + " wurde erzeugt.");

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static TenancyContract loadByContract(Contract contract) {
        TenancyContract tenancyContract = loadById(contract.getId());
        if (tenancyContract != null) {
            tenancyContract.getFromContract(contract);
            return tenancyContract;
        }
        return null;
    }

    public static TenancyContract loadById(int id) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM tenancy_contract WHERE contract_number = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                TenancyContract tenancyContract = new TenancyContract();
                tenancyContract.setId(rs.getInt("contract_number"));
                tenancyContract.setAdditionalCosts(rs.getFloat("additional_costs"));
                tenancyContract.setDurationMonths(rs.getInt("duration_months"));

                rs.close();
                pstmt.close();
                return tenancyContract;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void sign(int renterId, int apartmentId) {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            String updateSQL = "INSERT INTO rent(contract_number, renter_id, apartment_id) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);

            // Setze Anfrage Parameter
            pstmt.setInt(1, getId());
            pstmt.setInt(2, renterId);
            pstmt.setInt(3, apartmentId);

            pstmt.executeUpdate();

            System.out.println("Der Vertrag mit der ID " + getId() + " wurde unterschrieben.");
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Anfangsdatum: " + getPrettyStartDate());
        System.out.println("Laufzeit: " + getDurationMonths());
        System.out.println("Zusätzliche Kosten: " + getAdditionalCosts());
    }
}
