import java.sql.*;
import java.util.*;

public class StudentCourseDAO {
    private Connection conn = DBConnection.getInstance().getConnection();

    public void assignCourse(int studentId, int courseId) throws Exception {
        PreparedStatement check = conn.prepareStatement(
                "SELECT COUNT(*) FROM StudentCourse WHERE StudentId=? AND CourseId=?");
        check.setInt(1, studentId);
        check.setInt(2, courseId);
        ResultSet rs = check.executeQuery();
        rs.next();
        if(rs.getInt(1) > 0) throw new Exception("Already assigned");

        PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO StudentCourse(StudentId, CourseId) VALUES (?, ?)");
        ps.setInt(1, studentId);
        ps.setInt(2, courseId);
        ps.executeUpdate();
    }

    public List<Course> getCoursesForStudent(int studentId) throws Exception {
        List<Course> list = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT c.Id, c.Name, c.Type FROM StudentCourse sc " +
                        "JOIN Course c ON sc.CourseId=c.Id WHERE sc.StudentId=?");
        ps.setInt(1, studentId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            Course c = CourseFactory.createCourse(rs.getString("Type"), rs.getString("Name"));
            c.setId(rs.getInt("Id"));
            list.add(c);
        }
        return list;
    }

    public boolean isStudentAssignedToCourse(int studentId, int courseId) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
                "SELECT COUNT(*) FROM StudentCourse WHERE StudentId=? AND CourseId=?"
        );
        ps.setInt(1, studentId);
        ps.setInt(2, courseId);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) return rs.getInt(1) > 0;
        return false;
    }

}