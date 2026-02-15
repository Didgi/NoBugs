package practice12_final.third_task;

import org.junit.jupiter.api.BeforeEach;

public class BaseTest {
    protected GradeService<Integer> gradeService;
    protected GradeService<Double> gradeServiceDouble;

    @BeforeEach
    public void setUp() {
        gradeService = new GradeService<>();
        gradeServiceDouble = new GradeService<>();
    }
}
