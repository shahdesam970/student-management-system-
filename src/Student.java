public abstract class Student implements Cloneable {
    protected int id;
    protected String name;
    protected String type;

    public Student(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }

    public void setId(int id) { this.id = id; }

    @Override
    public Student clone() {
        try {
            return (Student) super.clone();
        } catch (CloneNotSupportedException ex) {
            return new UndergraduateStudent(this.name); // default fallback
        }
    }
}