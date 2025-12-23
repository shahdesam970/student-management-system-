import java.sql.*;
import java.util.*;

public class CourseDAO {
    private Connection conn = DBConnection.getInstance().getConnection();

    public void insert(Course c) throws Exception {
        if(c.getName() == null || c.getName().trim().isEmpty())
            throw new Exception("Course name required");
        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Course(Name, Type) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, c.getName());
        ps.setString(2, c.getType());
        ps.executeUpdate();
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()) c.setId(rs.getInt(1));
    }

    public List<Course> getAll() throws Exception {
        List<Course> list = new ArrayList<>();
        ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Course");
        while(rs.next()) {
            Course c = CourseFactory.createCourse(rs.getString("Type"), rs.getString("Name"));
            c.setId(rs.getInt("Id"));
            list.add(c);
        }
        return list;
    }
}