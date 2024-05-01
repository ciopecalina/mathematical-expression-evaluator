import java.util.Deque;
import java.util.LinkedList;
import java.util.Scanner;

class PostfixEvaluator {
    private String expression;
    private String postfix;
    private final Deque<Character> operatorStack;

    public PostfixEvaluator(String expression) throws Exception {
        this.expression = expression.trim().replaceAll("\\s+", "");
        this.operatorStack = new LinkedList<>();

        generateNotation();
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) throws Exception {
        this.expression = expression;
        generateNotation();
    }

    private void generateNotation() throws Exception {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        while (i < expression.length()) {
            char c = expression.charAt(i);
            if (Character.isDigit(c)) {
                sb.append(c);
            } else if (isOperator(c)) {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(' &&
                        (precedence(c) < precedence(operatorStack.peek()) ||
                                (precedence(c) == precedence(operatorStack.peek()) && isLeftAssociative(c)))) {
                    sb.append(operatorStack.pop());
                }
                operatorStack.push(c);
            } else if (c == '(') {
                operatorStack.push(c);
            } else if (c == ')') {
                while (!operatorStack.isEmpty() && operatorStack.peek() != '(') {
                    sb.append(operatorStack.pop());
                }
                if (operatorStack.isEmpty()) {
                    throw new Exception("Error: expression with wrong parentheses");
                }
                operatorStack.pop();
            }
            i++;
        }

        while (!operatorStack.isEmpty()) {
            char topOperator = operatorStack.pop();
            if (topOperator == '(') {
                throw new Exception("Error: expression with wrong parentheses");
            }
            sb.append(topOperator);
        }

        postfix = sb.toString();
    }

    private static boolean isOperator(char c) {
        if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^')
            return true;
        return false;
    }

    private int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return 0;
        }
    }

    public static int evaluateNotation(String postfix) throws Exception {
        Deque<Integer> operandStack = new LinkedList<>();
        int i = 0;
        int finalResult = 0;

        while (i < postfix.length()) {
            char c = postfix.charAt(i);
            if (Character.isDigit(c)) {
                operandStack.push(Character.getNumericValue(c));
            } else if (isOperator(c)) {
                int operand2 = operandStack.pop();
                int operand1 = operandStack.pop();
                int result = performOperation(operand1, operand2, c);
                operandStack.push(result);
            }
            i++;
        }

        finalResult = operandStack.pop();

        if (!operandStack.isEmpty()) {
            throw new Exception("Error: postfix expression is wrong");
        }

        return finalResult;
    }

    private static int performOperation(int operand1, int operand2, char operator) throws Exception {
        switch (operator) {
            case '+':
                return operand1 + operand2;
            case '-':
                return operand1 - operand2;
            case '*':
                return operand1 * operand2;
            case '/':
                if (operand2 == 0) {
                    throw new Exception("Division by zero");
                }
                return operand1 / operand2;
            case '^':
                return (int) Math.pow(operand1, operand2);
            default:
                throw new Exception("Invalid operator");
        }
    }

    private boolean isLeftAssociative(char c) {
        if (c == '^')
            return false;
        return true;
    }

    public String getPostfix() {
        return postfix;
    }
}

public class Main {
    public static final int POSTFIX_GENERATE = 1;
    public static final int POSTFIX_EVAL = 2;

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int exercise = scanner.nextInt();
        scanner.nextLine();
        String expression = scanner.nextLine();

        switch (exercise) {
            case POSTFIX_GENERATE:
                PostfixEvaluator e = new PostfixEvaluator(expression);
                System.out.println(e.getPostfix());
                break;

            case POSTFIX_EVAL:
                System.out.println(PostfixEvaluator.evaluateNotation(expression));
                break;
        }
    }
}
