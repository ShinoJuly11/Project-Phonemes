import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Database{

    public void connectDatabase(){
        String url = "jdbc:sqlite:notes.db";

        try (var conn = DriverManager.getConnection(url)) {
            if (conn != null) {
                var meta = conn.getMetaData();
                System.out.println("The driver name is " + meta.getDriverName());
                System.out.println("A new database has been created.");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void createTable(){
        var url = "jdbc:sqlite:notes.db";

        // SQL statement for creating a new table
        var sql = "CREATE TABLE IF NOT EXISTS notes("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "	fileName TEXT NOT NULL,"
                + " alias text NULL,"
                + " offset INTEGER NOT NULL,"    // reason why its integer not a float is because im tuning it against the
                + " overlap INTEGER NOT NULL,"   // frames not the time in microseconds
                + " consonant INTEGER NOT NULL,"
                + " preuttrance INTEGER NOT NULL,"
                + " cutoff INTEGER NOT NULL,"
                + "	comment TEXT NULL"
                + ");";

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement()) {
            // create a new table
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }


    public void insertPhoneme(Phoneme phoneme){
        String url = "jdbc:sqlite:notes.db";

        String sql = "INSERT INTO"
                   + " notes(fileName,alias,offset,overlap,consonant,preuttrance,cutoff,comment)"
                   + " VALUES(?,?,?,?,?,?,?,?)";

        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, phoneme.getFileName());
                pstmt.setString(2, phoneme.getAlias());
                pstmt.setInt(3, phoneme.getOffset());
                pstmt.setInt(4, phoneme.getOverlap());
                pstmt.setInt(5, phoneme.getConsonant());
                pstmt.setInt(6, phoneme.getPreuttrance());
                pstmt.setInt(7, phoneme.getCutoff());
                pstmt.setString(8, phoneme.getComment());
                pstmt.executeUpdate();

                System.out.println(phoneme.getFileName() + " is saved!");
            }

        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public void updatePhoneme(Phoneme phoneme, phonemeStrings randText, String newString){
        var url = "jdbc:sqlite:notes.db";
        String sql = null;

        switch (randText){
            case FILENAME -> sql = "UPDATE notes SET fileName = ? WHERE fileName = ?";
            case ALIAS -> sql = "UPDATE notes SET alias = ? WHERE alias = ?";
            case COMMENT -> sql = "UPDATE notes SET comment = ? WHERE comment = ?";
        }


        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            // set the parameters
            pstmt.setString(1, newString);
            //pstmt.setInt(3, id);

        switch (randText){
            case FILENAME -> pstmt.setString(2, phoneme.getFileName());
            case ALIAS -> pstmt.setString(2, phoneme.getAlias());
            case COMMENT -> pstmt.setString(2, phoneme.getComment());
        }
            // update
            pstmt.executeUpdate();

        switch (randText){
            case FILENAME -> phoneme.setFileName(newString);
            case ALIAS -> phoneme.setAlias(newString);
            case COMMENT -> phoneme.setComment(newString);
        }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

        public void updatePhoneme(Phoneme phoneme, phonemeInts randText, int newInt){
        var url = "jdbc:sqlite:notes.db";
        String sql = null;

        switch (randText){
            case OFFSET -> sql = "UPDATE notes SET offset = ? WHERE offset = ?";
            case OVERLAP -> sql = "UPDATE notes SET overlap = ? WHERE overlap = ?";
            case CONSONANT -> sql = "UPDATE notes SET consonant = ? WHERE consonant = ?";
            case CUTOFF -> sql = "UPDATE notes SET cutoff = ? WHERE cutoff = ?";
            case PREUTTRANCE -> sql = "UPDATE notes SET preutturance = ? WHERE preutturance = ?";
            default -> System.err.println("inserted phonemeInts is not there");
        }


        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {
            // set the parameters
            pstmt.setInt(1, newInt);

        switch (randText){
            case OFFSET -> pstmt.setInt(2, phoneme.getOffset());
            case OVERLAP -> pstmt.setInt(2, phoneme.getOverlap());
            case CONSONANT -> pstmt.setInt(2, phoneme.getConsonant());
            case CUTOFF -> pstmt.setInt(2, phoneme.getCutoff());
            case PREUTTRANCE -> pstmt.setInt(2, phoneme.getPreuttrance());
        }
            // update
            pstmt.executeUpdate();

            switch (randText){
            case OFFSET -> phoneme.setOffset(newInt);
            case OVERLAP -> phoneme.setOverlap(newInt);
            case CONSONANT -> phoneme.setConsonant(newInt);
            case CUTOFF -> phoneme.setCutoff(newInt);
            case PREUTTRANCE -> phoneme.setPreuttrance(newInt);

        }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }

    public List<Phoneme> selectAllPhoneme(){

        var url = "jdbc:sqlite:notes.db";
        var sql = "SELECT fileName,alias,offset,overlap,consonant,preuttrance,cutoff,comment FROM notes";
        List<Phoneme> phonemeArray = new ArrayList<>();

        try (var conn = DriverManager.getConnection(url);
             var stmt = conn.createStatement();
             var rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                        Phoneme phoneme = new Phoneme();

                        phoneme.setFileName(rs.getString("fileName"));
                        phoneme.setAlias(rs.getString("alias"));
                        phoneme.setOffset(rs.getInt("offset"));
                        phoneme.setOverlap(rs.getInt("overlap"));
                        phoneme.setConsonant(rs.getInt("consonant"));
                        phoneme.setPreuttrance(rs.getInt("preuttrance"));
                        phoneme.setCutoff(rs.getInt("cutoff"));
                        phoneme.setComment(rs.getString("comment"));

                        phonemeArray.add(phoneme);
            }

            return phonemeArray;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return phonemeArray;
        }
        

    }

    // DO THIS NEXT 22/7/2025

    public void deleteData(phonemeStrings type, String searchName){
        var url = "jdbc:sqlite:notes.db";
        
        String sql;

        switch (type){
            case FILENAME -> sql = "DELETE FROM notes WHERE fileName = ?";
            case ALIAS -> sql = "DELETE FROM notes WHERE alias = ?";
            case COMMENT -> sql = "DELETE FROM notes WHERE comment = ?";
            default -> sql = null;
        }

        try (var conn = DriverManager.getConnection(url);
             var pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, searchName);

            // execute the delete statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}  