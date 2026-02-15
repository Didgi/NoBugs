package practice6_collections.additionalTasks.seventh;

import java.util.Stack;

public class BracketsSequence {

    private Stack<Character> brackets;

    public BracketsSequence(){
        brackets = new Stack<>();
    }

    public boolean checkBracketsSequence(String sequence){
        final char[] charArray = sequence.toCharArray();
        for (char symbol:
             charArray) {
            if (openingBrackets(symbol)){
                brackets.push(symbol);
            }
            else if (closingBrackets(symbol)){
                if (brackets.isEmpty()) {
                    return false;
                }
                if (!pairBrackets(symbol, brackets.pop())){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean openingBrackets(char symbol){
        return symbol == '[' || symbol == '{' || symbol == '(';
    }

    private boolean closingBrackets(char symbol){
        return symbol == ']' || symbol == '}' || symbol == ')';
    }

    private boolean pairBrackets(char symbol, char lastSymbol){
        return (symbol == ']' && lastSymbol == '[') || (symbol == '}' && lastSymbol == '{') || (symbol == ')' && lastSymbol == '(');
    }

}
