package main.java.de.dis;

import main.java.de.dis.data.EstateAgent;

/**
 * Hauptklasse
 */
public class Main {
	/**
	 * Startet die Anwendung
	 */
	public static void main(String[] args) {
		showMainMenu();
	}

	/**
	 * Zeigt das Hauptmenü
	 */
	public static void showMainMenu() {
		//Menüoptionen
		final int MENU_ESTATE_AGENT = 0;
		final int MENU_ESTATE = 1;
		final int QUIT = 2;

		//Erzeuge Menü
		Menu mainMenu = new Menu("Hauptmenü");
		mainMenu.addEntry("Makler-Verwaltung", MENU_ESTATE_AGENT);
		mainMenu.addEntry("Objekt-Verwaltung", MENU_ESTATE);
		mainMenu.addEntry("Beenden", QUIT);

		//Verarbeite Eingabe
		while(true) {
			int response = mainMenu.show();

			switch(response) {
				case MENU_ESTATE_AGENT:
					validateUser();
					break;
				case MENU_ESTATE:
					validateAgent();
					break;
				case QUIT:
					return;
			}
		}
	}

	private static void validateUser() {
		if ("PW".equals(FormUtil.readString("Passwort"))) {
			EstateAgentMenuHandler.showMaklerMenu();
		} else {
			System.out.println("Falsches Passwort");
		}
	}

	private static void validateAgent() {
		String agentLogin = FormUtil.readString("Makler-Login");
		EstateAgent agent = EstateAgent.loadByLogin(agentLogin);
		if (agent != null) {
			while (true) {
				String password = FormUtil.readString("Passwort");
				if (password.equals(agent.getPassword())) {
					EstateMenuHandler.showEstateMenu(agent);
					return;
				} else {
					System.out.println("Passwort falsch!");
				}
			}
		} else {
			System.out.println("Makler konnte nicht gefunden werden");
		}
	}
}
