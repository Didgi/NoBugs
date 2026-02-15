package practice12_final.fouth_task;

import org.junit.jupiter.api.BeforeEach;
import practice12_final.fourth_task.MovieService;

public class BaseTest {
    protected MovieService movieService;

    @BeforeEach
    public void setUp() {
        movieService = new MovieService();
    }
}
