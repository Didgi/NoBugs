package other_m;

import java.util.Random;

public class CoopUser extends Users{
    private int id;
    private String companyName;
    public CoopUser(String companyName, String name) {
        super(name);
        this.id = new Random().nextInt(100);;
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "Корпоративный с " +
                "id=" + id +
                ", именем компании" + companyName + " и именем " + getName();
    }
}
