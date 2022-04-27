package main.java.de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class Estate implements DatabaseObject {
    private int id;
    private String city;
    private int postal_Code;
    private String street;
    private int street_Number;
    private float square_area;
    private int agent_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getPostal_Code() {
        return postal_Code;
    }

    public void setPostal_Code(int postal_Code) {
        this.postal_Code = postal_Code;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getStreet_Nr() {
        return street_Number;
    }

    public void setStreet_Nr(int street_Number) {
        this.street_Number = street_Number;
    }

    public float getSquare_Area() {
        return square_area;
    }

    public void setSquare_Area(float square_area) {
        this.square_area = square_area;
    }

    public int getAgent_Id() {
        return agent_id;
    }

    public void setAgent_Id(int agent_id) {
        this.agent_id = agent_id;
    }

    public static Estate loadById(int id) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM estate WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Estate estate = new Estate() {};
                estate.setId(rs.getInt("id"));
                estate.setCity(rs.getString("city"));
                estate.setPostal_Code(rs.getInt("postal_code"));
                estate.setStreet(rs.getString("street"));
                estate.setStreet_Nr(rs.getInt("street_nr"));
                estate.setSquare_Area(rs.getFloat("square_area"));
                estate.setAgent_Id(rs.getInt("agent_id"));

                rs.close();
                pstmt.close();
                return estate;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save() {}

    @Override
    public void delete() {
        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            String updateSQL = "DELETE FROM estate WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);
            pstmt.setInt(1, getId());

            // Führe Anfrage aus
            pstmt.executeUpdate();
            System.out.println("Objekt mit der ID " + getId() + " wurde gelöscht.");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void printDetails() {
        System.out.println("Ausgewähltes Objekt:");
        System.out.println("Straße: " + getStreet());
        System.out.println("Hausnummer: " + getStreet_Nr());
        System.out.println("Stadt: " + getCity());
        System.out.println("Postleitzahl: " + getPostal_Code());
        System.out.println("Quadratmeter: " + getSquare_Area());
    }

    public void getFromEstate(Estate estate) {
        setId(estate.getId());
        setAgent_Id(estate.getAgent_Id());
        setCity(estate.getCity());
        setPostal_Code(estate.getPostal_Code());
        setStreet(estate.getStreet());
        setStreet_Nr(estate.getStreet_Nr());
        setSquare_Area(estate.getSquare_Area());
    }
}
