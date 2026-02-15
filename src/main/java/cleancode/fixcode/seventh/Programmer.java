package cleancode.fixcode.seventh;

class Programmer implements Workable {
    @Override
    public void work() {
        System.out.println("Программист пишет код");
    }
}

//Задача: Разделите интерфейс на отдельные специализированные интерфейсы.
