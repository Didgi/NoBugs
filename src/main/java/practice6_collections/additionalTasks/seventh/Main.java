package practice6_collections.additionalTasks.seventh;

public class Main {
    public static void main(String[] args) {
        BracketsSequence bracketsSequence = new BracketsSequence();
        String example1 = "[]";
        String example2 = "[()]";
        String example3 = "[({})]";
        String example4 = "]";
        String example5 = "[({})])";
        System.out.println(bracketsSequence.checkBracketsSequence(example1));
        System.out.println(bracketsSequence.checkBracketsSequence(example2));
        System.out.println(bracketsSequence.checkBracketsSequence(example3));
        System.out.println(bracketsSequence.checkBracketsSequence(example4));
        System.out.println(bracketsSequence.checkBracketsSequence(example5));

    }
}
