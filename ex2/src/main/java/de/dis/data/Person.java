package main.java.de.dis.data;

import java.sql.*;

public class Person {

    private int id;
    private String firstName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    private String lastName;
    private String address;

    public static Person loadById(int id) {
        try {
            // Hole Verbindung
            Connection con = DbConnectionManager.getInstance().getConnection();

            // Erzeuge Anfrage
            String selectSQL = "SELECT * FROM person WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(selectSQL);
            pstmt.setInt(1, id);

            // Führe Anfrage aus
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Person person = new Person() {};
                person.setId(rs.getInt("id"));
                person.setAddress(rs.getString("address"));
                person.setFirstName(rs.getString("first_name"));
                person.setLastName(rs.getString("name"));

                rs.close();
                pstmt.close();
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void save() {
        Connection con = DbConnectionManager.getInstance().getConnection();

        try {
            String insertSQL = "Insert INTO person (first_name, name, address) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, getFirstName());
            pstmt.setString(2, getLastName());
            pstmt.setString(3, getAddress());

            pstmt.executeUpdate();

            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                setId(rs.getInt("id"));
            }
            rs.close();
            pstmt.close();
            System.out.println("Person mit ID " + getId() + " wurde erzeugt.");
            System.out.println();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
