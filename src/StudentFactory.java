public class StudentFactory {
    public static Student createStudent(String type, String name) {
        switch(type) {
            case "Undergraduate": return new UndergraduateStudent(name);
            case "Graduate": return new GraduateStudent(name);
            case "PartTime": return new PartTimeStudent(name);
            default: throw new IllegalArgumentException("Invalid student type");
        }
    }
}
