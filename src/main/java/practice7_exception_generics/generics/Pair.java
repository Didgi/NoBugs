package practice7_exception_generics.generics;

public class Pair<K, V> {

    private K valueK;

    private V valueV;

    public K getValueK() {
        return valueK;
    }

    public V getValueV() {
        return valueV;
    }

    public void setValueK(K valueK) {
        this.valueK = valueK;
    }

    public void setValueV(V valueV) {
        this.valueV = valueV;
    }
}
