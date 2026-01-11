package other_m;

public class B {
    B b = new B();

    public int check(){
        return (true ? null : 0);
    }

    public static void main(String[] args) {
//        B b = new B();
//        b.check();
        String ba = "123";
        final StringBuffer reverse = new StringBuffer(ba).reverse();
        System.out.println(reverse);
        final String substring = ba.substring(1,2);
        System.out.println(substring);
    }
}
