public class CourseFactory {
    public static Course createCourse(String type, String name) {
        switch(type) {
            case "Core": return new CoreCourse(name);
            case "Elective": return new ElectiveCourse(name);
            case "Lab": return new LabCourse(name);
            default: throw new IllegalArgumentException("Invalid course type");
        }
    }
}
