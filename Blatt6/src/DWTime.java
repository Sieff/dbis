import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DWTime {

    private Date date;
    private int year;
    private int quarter;
    private int month;
    private int day;

    private DWTime(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        this.date = date;
        this.year = calendar.get(Calendar.YEAR);
        this.month = calendar.get(Calendar.MONTH) + 1;
        this.quarter = this.month / 4 + 1;
        this.day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static DWTime from(String strToParse) {
        String[] parts = strToParse.split(";");
        String dateString = parts[0];
        Date date = parseDate(dateString);
        return new DWTime(date);
    }

    public static void insertBatch(List<DWTime> times) {

        // Hole Verbindung
        Connection con = DatabaseManager.getInstance().getConnection();

        try {
            String updateSQL = "INSERT INTO dw_time(date, year , month ,quarter, day) VALUES (?, ?, ?, ?, ?) ON CONFLICT (date) DO NOTHING";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);

            for (DWTime time : times) {
                // Setze Anfrage Parameter
                pstmt.setDate(1, new java.sql.Date(time.getDate().getTime()));
                pstmt.setInt(2, time.getYear());
                pstmt.setInt(3, time.getMonth());
                pstmt.setInt(4, time.getQuarter());
                pstmt.setInt(5, time.getDay());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void save() {
        // Hole Verbindung
        Connection con = DatabaseManager.getInstance().getConnection();

        try {
            String updateSQL = "INSERT INTO dw_time(date, year , month ,quarter, day) VALUES (?, ?, ?, ?, ?) ON CONFLICT (date) DO NOTHING";
            PreparedStatement pstmt = con.prepareStatement(updateSQL);

            // Setze Anfrage Parameter
            pstmt.setDate(1, new java.sql.Date(getDate().getTime()));
            pstmt.setInt(2, getYear());
            pstmt.setInt(3, getMonth());
            pstmt.setInt(4, getQuarter());
            pstmt.setInt(5, getDay());

            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    private static Date parseDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.ENGLISH);
        LocalDate localDate = LocalDate.parse(date, formatter);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

}
