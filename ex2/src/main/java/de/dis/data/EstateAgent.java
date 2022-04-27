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
public class EstateAgent implements DatabaseObject {
	private int id;
	private String name;
	private String address;
	private String login;
	private String password;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public void setId(int id) {
		this.id = id;
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
	public static EstateAgent loadByLogin(String login) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate_agent WHERE login = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, login);

			return EstateAgent.load(pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Lädt einen Makler aus der Datenbank
	 * @param id ID des zu ladenden Maklers
	 * @return Makler-Instanz
	 */
	public static EstateAgent loadById(int id) {
		try {
			// Hole Verbindung
			Connection con = DbConnectionManager.getInstance().getConnection();

			// Erzeuge Anfrage
			String selectSQL = "SELECT * FROM estate_agent WHERE id = ?";
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setInt(1, id);
			return EstateAgent.load(pstmt);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static EstateAgent load(PreparedStatement statement) {
		try {
			// Führe Anfrage aus
			ResultSet rs = statement.executeQuery();
			if (rs.next()) {
				EstateAgent ts = new EstateAgent();
				ts.setId(rs.getInt("id"));
				ts.setName(rs.getString("name"));
				ts.setAddress(rs.getString("address"));
				ts.setLogin(rs.getString("login"));
				ts.setPassword(rs.getString("password"));

				rs.close();
				statement.close();
				return ts;
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
			EstateAgent existingEntity = EstateAgent.loadById(getId());


			if (existingEntity != null) {
				// makler existiert schon, update machen

				String updateSQL = "UPDATE estate_agent SET name = ?, address = ?, login = ?, password = ? WHERE id = ?";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getName());
				pstmt.setString(2, getAddress());
				pstmt.setString(3, getLogin());
				pstmt.setString(4, getPassword());
				pstmt.setInt(5, getId());
				pstmt.executeUpdate();

				pstmt.close();
				System.out.println("Makler mit dem Login " + getLogin() + " wurde bearbeitet.");
			} else {
				// Falls schon eine ID vorhanden ist, mache ein Update...
				String updateSQL = "INSERT INTO estate_agent(name, address, login, password) VALUES (?, ?, ?, ?)";
				PreparedStatement pstmt = con.prepareStatement(updateSQL);

				// Setze Anfrage Parameter
				pstmt.setString(1, getName());
				pstmt.setString(2, getAddress());
				pstmt.setString(3, getLogin());
				pstmt.setString(4, getPassword());

				pstmt.executeUpdate();

				pstmt.close();
				System.out.println("Makler mit dem Login "+ getLogin()+" wurde erzeugt.");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete() {
		Connection con = DbConnectionManager.getInstance().getConnection();
		try {
			String updateSQL = "DELETE estate_agent, estate FROM estate_agent left join estate on estate.agent_id = estate_agent.id WHERE estate_agent.id = ?";
			PreparedStatement pstmt = con.prepareStatement(updateSQL);
			pstmt.setInt(1, getId());

			// Führe Anfrage aus
			pstmt.executeUpdate();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void printDetails() {
		System.out.println("Ausgewählter Makler:");
		System.out.println("Name: " + getName());
		System.out.println("Adresse: " + getAddress());
		System.out.println("Login: " + getLogin());
		System.out.println("Passwort: " + getPassword());
		System.out.println();
	}
}
