package main.java.de.dis.data;

import java.sql.*;

public class Apartment extends Estate {
    private int floor;
    private float rent;
    private int rooms;
    private boolean balcony;
    private boolean built_in_kitchen;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public float getRent() {
        return rent;
    }

    public void setRent(float rent) {
        this.rent = rent;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public boolean getBalcony() {
        return balcony;
    }

    public void setBalcony(boolean balcony) {
        this.balcony = balcony;
    }

    public boolean getBuilt_In_Kitchen() {
        return built_in_kitchen;
    }

    public void setBuilt_In_Kitchen(boolean built_in_kitchen) {
        this.built_in_kitchen = built_in_kitchen;
    }

    public static Apartment loadByEstate(Estate estate) {
        Apartment apartment = loadById(estate.getId());
        if (apartment != null) {
            apartment.getFromEstate(estate);
            return apartment;
        }
        return null;
    }

    public static Apartment loadById(int id) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM apartment WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Apartment ap = new Apartment();
                ap.setFloor(rs.getInt("floor"));
                ap.setRent(rs.getFloat("rent"));
                ap.setRooms(rs.getInt("rooms"));
                ap.setBalcony(rs.getBoolean("balcony"));
                ap.setBuilt_In_Kitchen(rs.getBoolean("built_in_kitchen"));

                rs.close();
                pstmt.close();
                return ap;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void save() {
        // Hole Verbindung
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {
            // FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
            Apartment existingEntity = Apartment.loadById(getId());


            if (existingEntity != null) {
                // apartment existiert schon, update machen

                String updateSQL = "UPDATE apartment SET city = ?, postal_code = ?, street = ?, street_nr = ?, square_area = ?, agent_id = ?, floor = ?, rent = ?, rooms = ?, balcony = ?, built_in_kitchen = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Setze Anfrage Parameter
                pstmt.setString(1, getCity());
                pstmt.setInt(2, getPostal_Code());
                pstmt.setString(3, getStreet());
                pstmt.setInt(4, getStreet_Nr());
                pstmt.setFloat(5, getSquare_Area());
                pstmt.setInt(6, getAgent_Id());
                pstmt.setInt(7, getFloor());
                pstmt.setFloat(8, getRent());
                pstmt.setInt(9, getRooms());
                pstmt.setBoolean(10, getBalcony());
                pstmt.setBoolean(11, getBuilt_In_Kitchen());
                pstmt.setInt(12, getId());
                pstmt.executeUpdate();

                pstmt.close();
                System.out.println("Apartment mit ID " + getId() + " wurde bearbeitet.");
                System.out.println();
            } else {
                // Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQL = "INSERT INTO apartment(city, postal_code, street, street_nr, square_area, agent_id, floor, rent, rooms, balcony, built_in_kitchen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(updateSQL, Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrage Parameter
                pstmt.setString(1, getCity());
                pstmt.setInt(2, getPostal_Code());
                pstmt.setString(3, getStreet());
                pstmt.setInt(4, getStreet_Nr());
                pstmt.setFloat(5, getSquare_Area());
                pstmt.setInt(6, getAgent_Id());
                pstmt.setInt(7, getFloor());
                pstmt.setFloat(8, getRent());
                pstmt.setInt(9, getRooms());
                pstmt.setBoolean(10, getBalcony());
                pstmt.setBoolean(11, getBuilt_In_Kitchen());

                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt("id"));
                }

                rs.close();
                pstmt.close();
                System.out.println("Apartment mit ID " + getId() + " wurde erzeugt.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Miete: " + getRent());
        System.out.println("Räume: " + getRooms());
        System.out.println("Mit Balkon: " + getBalcony());
        System.out.println("Mit Einbauküche: " + getBuilt_In_Kitchen());
        System.out.println();
    }

    @Override
    public void delete() {
        super.delete();

        Connection con = DbConnectionManager.getInstance().getConnection();
        try {
            String updateSQL = "with delete_rent as (DELETE FROM rent WHERE apartment_id = ? returning contract_number as cn) delete from contract where contract_number in (select cn from delete_rent)";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);
            pstmt.setInt(1, getId());

            // Führe Anfrage aus
            pstmt.executeUpdate();
            System.out.println("Vertrag mit Apartment " + getId() + " wurde gelöscht.");
            System.out.println();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
