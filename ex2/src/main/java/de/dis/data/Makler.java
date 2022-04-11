package main.java.de.dis.data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Makler-Bean
 *
 * create table estate_agent (
 * name varchar (255),
 * address varchar (255),
 * login varchar (255) primary key,
 * password varchar (255)
 * );
 */
public class Makler {
	private String name;
	private String address;
	private String login;
	private String password;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Lädt einen Makler aus der Datenbank
	 * @param login ID des zu ladenden Maklers
	 * @return Makler-Instanz
	 */
	public static Makler load(String login) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate_agent WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, login);

			// Führe Anfrage aus
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				Makler ts = new Makler();
				ts.setName(rs.getString("name"));
				ts.setAddress(rs.getString("address"));
				ts.setLogin(rs.getString("login"));
				ts.setPassword(rs.getString("password"));

				rs.close();
				pstmt.close();
				return ts;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Speichert den Makler in der Datenbank. Ist noch keine ID vergeben
	 * worden, wird die generierte Id von der DB geholt und dem Model übergeben.
	 */
	public void save() {
		// Hole Verbindung
		Connection con = DbConnectionManager.getInstance().getConnection();

		try {
			// FC<ge neues Element hinzu, wenn das Objekt noch keine ID hat.
			Makler existingEntity = Makler.load(this.login);


			if (existingEntity != null) {
				// makler existiert schon, update machen

				String updateSQL = "UPDATE estate_agent SET name = ?, address = ?, login = ?, password = ? WHERE login = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getName());
				pstmt.setString(2, getAddress());
				pstmt.setString(3, getLogin());
				pstmt.setString(4, getPassword());
				pstmt.setInt(5, getLogin());
				pstmt.executeUpdate();

				pstmt.close();
				System.out.println("Makler mit der Login "+getLogin()+" wurde bearbeitet.");
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "INSERT estate_agent set name = ?, address = ?, login = ?, password = ? WHERE login = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getName());
				pstmt.setString(2, getAddress());
				pstmt.setString(3, getLogin());
				pstmt.setString(4, getPassword());
				pstmt.setString(5, getLogin());

				pstmt.executeUpdate();

				pstmt.close();
				System.out.println("Makler mit dem Login "+ this.getLogin()+" wurde erzeugt.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Speichert den Makler in der Datenbank. Ist noch keine ID vergeben
	 * worden, wird die generierte Id von der DB geholt und dem Model übergeben.
	 */
	public void delete() {
		Connection con = DbConnectionManager.getInstance().getConnection();
		try {
			String updateSQL = "DELETE FROM estate_agent WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);
			pstmt.setString(1, getLogin()););
			pstmt.setString(1, login);

			// Führe Anfrage aus
			pstmt.executeQuery();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
