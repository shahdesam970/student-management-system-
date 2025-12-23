import java.sql.*;
import java.util.*;

public class StudentDAO {
    private Connection conn = DBConnection.getInstance().getConnection();

    public void insert(Student s) throws Exception {
        if(s.getName() == null || s.getName().trim().isEmpty())
            throw new Exception("Student name required");
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Student(Name, Type) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, s.getName());
        ps.setString(2, s.getType());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()) s.setId(rs.getInt(1));
    }

    public List<Student> getAll() throws Exception {
        List<Student> list = new ArrayList<>();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Student");
        while(rs.next()) {
            Student s = StudentFactory.createStudent(rs.getString("Type"), rs.getString("Name"));
            s.setId(rs.getInt("Id"));
            list.add(s);
        }
        return list;
    }
}