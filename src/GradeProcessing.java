import java.util.List;

public class GradeProcessing {
    private static GradeProcessing instance;

    private GradeProcessing() {}

    public static GradeProcessing getInstance() {
        if (instance == null) {
            instance = new GradeProcessing();
        }
        return instance;
    }

    public double normalize(double grade) {
        if (grade < 0) return 0;
        if (grade > 100) return 100;
        return grade;
    }
}
