package other_m;

public class CheckPrimeNumber {
    public static void main(String[] args) {
        System.out.println(isPrime(12));
    }

    public static boolean isPrime(int digital){
        if (digital <= 0) return false;
        for (int i = 2; i * i <= digital; i++){
            if (digital % 2 == 0) return false;
        }
        return true;
    }
}
