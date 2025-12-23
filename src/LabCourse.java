public class LabCourse extends Course {
    public LabCourse(String name) { super(new Builder().setName(name).setType("Lab")); }
}