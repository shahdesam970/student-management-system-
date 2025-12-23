import java.util.*;

public class CourseRegistration {
    private static CourseRegistration instance;
    private Map<Integer, List<Course>> registrations;

    private CourseRegistration() {
        registrations = new HashMap<>();
    }

    public static CourseRegistration getInstance() {
        if (instance == null) {
            instance = new CourseRegistration();
        }
        return instance;
    }

    public void registerCourse(Student student, Course course) {
        registrations.computeIfAbsent(student.getId(), k -> new ArrayList<>());
        List<Course> courses = registrations.get(student.getId());
        if (!courses.contains(course)) {
            courses.add(course);
        }
    }

    public List<Course> getCoursesForStudent(int studentId) {
        return registrations.getOrDefault(studentId, new ArrayList<>());
    }
}
