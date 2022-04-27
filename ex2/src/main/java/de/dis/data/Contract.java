package main.java.de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Contract implements DatabaseObject {

    private int id;
    private Date date;
    private String place;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public java.sql.Date getDateAsSQLDate() {
        return new java.sql.Date(getDate().getTime());
    }

    public String getPrettyDate() {
        String pattern = "dd.MM.yyyy";
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(getDate());
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public static Contract loadById(int id) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM contract WHERE contract_number = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Contract contract = new Contract() {};
                contract.setId(rs.getInt("id"));
                contract.setDate(rs.getDate("date"));
                contract.setPlace(rs.getString("place"));

                rs.close();
                pstmt.close();
                return contract;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save() {
        throw new RuntimeException("Funktion nicht implementiert");
    }

    @Override
    public void delete() {
        throw new RuntimeException("Funktion nicht implementiert");
    }

    @Override
    public void printDetails() {
        System.out.println("Vertragsnummer: " + getId());
        System.out.println("Datum: " + getPrettyDate());
        System.out.println("Ort: " + getPlace());
    }

    public static List<Contract> getAll() {
        List<Contract> contractList = new ArrayList<>();
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM contract";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {

                Contract contract = new Contract() {};
                contract.setId(rs.getInt("contract_number"));
                contract.setDate(rs.getDate("date"));
                contract.setPlace(rs.getString("place"));

                contractList.add(contract);
            }
            rs.close();
            pstmt.close();
            return contractList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getFromContract(Contract contract) {
        setId(contract.getId());
        setDate(contract.getDate());
        setPlace(contract.getPlace());
    }

}
