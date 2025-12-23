public class Course {
    private int id;
    private String name;
    private String type;

    Course(Builder builder) {
        this.name = builder.name;
        this.type = builder.type;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public void setId(int id) { this.id = id; }

    public static class Builder {
        private String name;
        private String type;

        public Builder setName(String name) { this.name = name; return this; }
        public Builder setType(String type) { this.type = type; return this; }
        public Course build() { return new Course(this); }
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Course)) return false;
        Course c = (Course) obj;
        return this.name.equals(c.name) && this.type.equals(c.type);
    }
}