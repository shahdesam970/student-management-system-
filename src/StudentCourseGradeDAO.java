import java.sql.*;
import java.util.*;

public class StudentCourseGradeDAO {
    private Connection conn = DBConnection.getInstance().getConnection();

    // تعيين درجة جديدة للطالب في كورس
    public void setGrade(int studentId, int courseId, double grade) throws Exception {
        PreparedStatement ps = conn.prepareStatement(
                "MERGE INTO StudentCourseGrade AS target " +
                        "USING (SELECT ? AS StudentId, ? AS CourseId) AS source " +
                        "ON target.StudentId = source.StudentId AND target.CourseId = source.CourseId " +
                        "WHEN MATCHED THEN UPDATE SET Grade = ? " +
                        "WHEN NOT MATCHED THEN INSERT (StudentId, CourseId, Grade) VALUES (?, ?, ?);"
        );
        ps.setInt(1, studentId);
        ps.setInt(2, courseId);
        ps.setDouble(3, grade);
        ps.setInt(4, studentId);
        ps.setInt(5, courseId);
        ps.setDouble(6, grade);
        ps.executeUpdate();
    }

    // جلب درجات الطالب لكل كورس
    public Map<Integer, Double> getGradesForStudent(int studentId) throws Exception {
        Map<Integer, Double> grades = new HashMap<>();
        PreparedStatement ps = conn.prepareStatement(
                "SELECT CourseId, Grade FROM StudentCourseGrade WHERE StudentId=?"
        );
        ps.setInt(1, studentId);
        ResultSet rs = ps.executeQuery();
        while(rs.next()) {
            grades.put(rs.getInt("CourseId"), rs.getDouble("Grade"));
        }
        return grades;
    }
}