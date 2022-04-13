package main.java.de.dis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

/**
 * Kleine Helferklasse zum Einlesen von Formulardaten
 */
public class FormUtil {
	/**
	 * Liest einen String vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Zeile
	 */
	public static String readString(String label) {
		String ret = null;
		BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));

		try {
			System.out.print(label+": ");
			ret = stdin.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/**
	 * Liest einen Integer vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Integer
	 */
	public static int readInt(String label) {
		int ret = 0;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);

			try {
				ret = Integer.parseInt(line);
				finished = true;
			} catch (NumberFormatException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}

		return ret;
	}

	/**
	 * Liest eine Gleitkommazahl vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesene Gleitkommazahl
	 */
	public static float readFloat(String label) {
		float ret = 0.0f;
		boolean finished = false;

		while(!finished) {
			String line = readString(label);

			try {
				ret = Float.parseFloat(line);
				ret = (float) (Math.round(ret * Math.pow(10, 2)) / Math.pow(10, 2));
				finished = true;
			} catch (NumberFormatException | ClassCastException e) {
				System.err.println("Ungültige Eingabe: Bitte geben Sie eine Zahl an!");
			}
		}

		return ret;
	}


	/** Liest einen boolean in der Form "j" oder "n" vom standard input ein
	 * @param label Zeile, die vor der Eingabe gezeigt wird
	 * @return eingelesener Boolean
	 */
	public static boolean readBoolean(String label) {
		while(true) {
			String line = readString(label);

			if (line.toLowerCase(Locale.ROOT).equals("j")) {
				return true;
			}
			if (line.toLowerCase(Locale.ROOT).equals("n")) {
				return false;
			}
			System.err.println("Ungültige Eingabe: Eingabe war weder j noch n!");
		}
	}
}
