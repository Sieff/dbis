package main.java.de.dis.data;

public interface DatabaseObject {
    /**
     * Saves the object to the database.
     */
    void save();

    /**
     * Deletes the object in the database.
     */
    void delete();

    /**
     * Prints details about the Object.
     */
    void printDetails();
}
