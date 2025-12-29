package cleancode.fixcode.sixth;

class Penguin extends NonflyingBird {
    @Override
    public void walk() {
        System.out.println("Пингвин ходит покачиваясь");
    }
}
//Задача: Перепроектируйте код так, чтобы классы-наследники не нарушали поведение базового класса.

