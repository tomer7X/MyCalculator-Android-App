package com.example.myfirstapplication;

import java.util.Stack;

public class CalculateExpression {

    public static Double finalCalc(String expression){
        double[] nums = extractNumbers(expression);
        char[] opers = extractOperators(expression);
        return calculateExpression(nums, opers);
    }

    public static Double calculateExpression(double[] nums, char[] ops) {
        // Stack for numbers
        Stack<Double> numStack = new Stack<>();
        // Stack for operators (only '+', '-')
        Stack<Character> opStack = new Stack<>();

        // Push the first number onto the stack
        numStack.push(nums[0]);

        for (int i = 0; i < ops.length; i++) {
            char op = ops[i];
            if (op == '*' || op == '/') {
                // Perform multiplication or division immediately
                double num = numStack.pop();
                double nextNum = nums[i + 1];
                if(nextNum == 0 && op == '/') return null;
                double result = (op == '*') ? num * nextNum : num / nextNum;
                numStack.push(result);
            } else {
                // For '+' or '-', push the operator and next number onto their stacks
                opStack.push(op);
                numStack.push(nums[i + 1]);
            }
        }

        // Now process addition and subtraction
        double result = numStack.get(0);
        int opIndex = 0;

        for (int i = 1; i < numStack.size(); i++) {
            char op = opStack.get(opIndex++);
            double num = numStack.get(i);
            result = (op == '+') ? result + num : result - num;
        }

        return result;
    }

    // Function to extract numbers from the expression
    public static double[] extractNumbers(String expression) {
        StringBuilder numBuilder = new StringBuilder();
        double[] nums = new double[expression.length()];
        int numsIndex = 0;

        // Loop through each character of the expression
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            // If it's part of a number, append to numBuilder
            if (Character.isDigit(c) || c == '.' ||
                    (c == '-' && (i == 0 || expression.charAt(i - 1) == '+' || expression.charAt(i - 1) == '*' || expression.charAt(i - 1) == '/' || expression.charAt(i - 1) == '('))) {
                numBuilder.append(c);
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                if (numBuilder.length() > 0) {
                    nums[numsIndex++] = Double.parseDouble(numBuilder.toString());
                    numBuilder.setLength(0); // Reset the builder
                }
            }
        }

        // Add the last number after the loop
        if (numBuilder.length() > 0) {
            nums[numsIndex] = Double.parseDouble(numBuilder.toString());
        }

        // Return an array of numbers
        return java.util.Arrays.copyOf(nums, numsIndex + 1);
    }

    // Function to extract operators from the expression
    public static char[] extractOperators(String expression) {
        int opersCount = 0;
        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == '+' || expression.charAt(i) == '-' || expression.charAt(i) == '*' || expression.charAt(i) == '/') {
                // Make sure the minus sign is treated as part of a number and not an operator
                if (!(expression.charAt(i) == '-' && (i == 0 || expression.charAt(i - 1) == '+' || expression.charAt(i - 1) == '*' || expression.charAt(i - 1) == '/' || expression.charAt(i - 1) == '('))) {
                    opersCount++;
                }
            }
        }

        char[] opers = new char[opersCount];
        int opIndex = 0;
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            if (c == '+' || c == '-' || c == '*' || c == '/') {
                // Only add operators that are not part of negative numbers
                if (!(c == '-' && (i == 0 || expression.charAt(i - 1) == '+' || expression.charAt(i - 1) == '*' || expression.charAt(i - 1) == '/' || expression.charAt(i - 1) == '('))) {
                    opers[opIndex++] = c;
                }
            }
        }
        return opers;
    }
}