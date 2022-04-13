package main.java.de.dis.data;

public interface DatabaseObject {
    /**
     * Saves the object to the database.
     */
    public void save();

    /**
     * Deletes the object in the database.
     */
    public void delete();

    public void printDetails();
}
