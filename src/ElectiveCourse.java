public class ElectiveCourse extends Course {
    public ElectiveCourse(String name) { super(new Builder().setName(name).setType("Elective")); }
}