package other_m;

// Класс, представляющий ресурс
class Resource implements AutoCloseable {
    private final String name;

    public Resource(String name) {
        this.name = name;
        System.out.println("open " + name);
    }

    @Override
    public void close() {
        System.out.println("close " + name);
        throw new RuntimeException("close " + name);
    }
}

// Основной класс с методом main
public class TryWithResources {
    public static void main(String[] args) {
        try (Resource r1 = new Resource("R1");
             Resource r2 = new Resource("R2")) {

            System.out.println("body");
            throw new RuntimeException("body");
        } catch (RuntimeException e) {
            System.out.println("caught: " + e.getMessage());
        }
    }
}

