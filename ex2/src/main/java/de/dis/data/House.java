package main.java.de.dis.data;

import java.sql.*;

public class House extends Estate {
    private int floors;
    private float price;
    private boolean garden;

    public int getFloors() {
        return floors;
    }

    public void setFloors(int floors) {
        this.floors = floors;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public boolean getGarden() {
        return garden;
    }

    public void setGarden(boolean garden) {
        this.garden = garden;
    }

    public static House loadByEstate(Estate estate) {
        House house = loadById(estate.getId());
        if (house != null) {
            house.getFromEstate(estate);
            return house;
        }
        return null;
    }

    public static House loadById(int id) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM house WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                House ho = new House();
                ho.setId(rs.getInt("id"));
                ho.setCity(rs.getString("city"));
                ho.setPostal_Code(rs.getInt("postal_code"));
                ho.setStreet(rs.getString("street"));
                ho.setStreet_Nr(rs.getInt("street_nr"));
                ho.setSquare_Area(rs.getFloat("square_area"));
                ho.setAgent_Id(rs.getInt("agent_id"));
                ho.setFloors(rs.getInt("floors"));
                ho.setPrice(rs.getFloat("price"));
                ho.setGarden(rs.getBoolean("garden"));

                rs.close();
                pstmt.close();
                return ho;
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
            House existingEntity = House.loadById(getId());


            if (existingEntity != null) {
                // haus existiert schon, update machen

                String updateSQL = "UPDATE house SET city = ?, postal_code = ?, street = ?, street_nr = ?, square_area = ?, agent_id = ?, floors = ?, price = ?, garden = ? WHERE id = ?";
                PreparedStatement pstmt = con.prepareStatement(updateSQL);

                // Setze Anfrage Parameter
                pstmt.setString(1, getCity());
                pstmt.setInt(2, getPostal_Code());
                pstmt.setString(3, getStreet());
                pstmt.setInt(4, getStreet_Nr());
                pstmt.setFloat(5, getSquare_Area());
                pstmt.setInt(6, getAgent_Id());
                pstmt.setInt(7, getFloors());
                pstmt.setFloat(8, getPrice());
                pstmt.setBoolean(9, getGarden());
                pstmt.setInt(10, getId());
                pstmt.executeUpdate();

                pstmt.close();
                System.out.println("Haus mit ID " + getId() + " wurde bearbeitet.");
            } else {
                // Falls schon eine ID vorhanden ist, mache ein Update...
                String updateSQL = "INSERT INTO house(city, postal_code, street, street_nr, square_area, agent_id, floors, price, garden) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(updateSQL, Statement.RETURN_GENERATED_KEYS);

                // Setze Anfrage Parameter
                pstmt.setString(1, getCity());
                pstmt.setInt(2, getPostal_Code());
                pstmt.setString(3, getStreet());
                pstmt.setInt(4, getStreet_Nr());
                pstmt.setFloat(5, getSquare_Area());
                pstmt.setInt(6, getAgent_Id());
                pstmt.setInt(7, getFloors());
                pstmt.setFloat(8, getPrice());
                pstmt.setBoolean(9, getGarden());

                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    setId(rs.getInt("id"));
                }

                rs.close();
                pstmt.close();
                System.out.println("Haus mit ID " + getId() + " wurde erzeugt.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void printDetails() {
        super.printDetails();
        System.out.println("Stockwerke: " + getFloors());
        System.out.println("Preis: " + getPrice());
        System.out.println("Mit Garten: " + getGarden());
        System.out.println();
    }
}
