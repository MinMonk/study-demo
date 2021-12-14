package com.monk.leetcode.november;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Demo15 {

    /**
     * 运算符号优先级Map
     */
    static Map<String, Integer> operatorLevel = new HashMap<>();

    /**
     * 运算符栈
     */
    static MyStack operatorStack = new MyStack();

    /**
     * 数字栈
     */
    static MyStack numberStack = new MyStack();

    /**
     * 初始化运算符优先级别
     */
    static{
        operatorLevel.put("(", 0);
        operatorLevel.put(")", 0);
        operatorLevel.put("+", 1);
        operatorLevel.put("-", 1);
        operatorLevel.put("*", 2);
        operatorLevel.put("/", 2);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        if(StringUtils.isBlank(input)){
            System.out.println("请输入非空公式...");
            return;
        }

        MyQueue queue = spiltValueToQueue(input);
        while (queue.getSize() > 0){
            String data = queue.deQueue().toString();
            if(!operatorLevel.containsKey(data)){
                // 如果不是操作符,就存放到数字栈中
                numberStack.push(Integer.parseInt(data));
                continue;
            }

            // 如果操作符栈中没有值就直接存放到操作符栈中
            if(operatorStack.getSize() <= 0){
                operatorStack.push(data);
                continue;
            }

            // 如果是左括号就直接入栈
            if("(".equals(data)){
                operatorStack.push(data);
                continue;
            }

            // 如果是右括号,就依次循环运算符号栈中的符号进行运算,直到遇到左括号为止
            if(")".equals(data)) {
                while (!"(".equals(operatorStack.peek().toString())) {
                    reallyOperation();
                }
                operatorStack.pop();
                continue;
            }

            /**
             * 如果当前运算法的优先级别<=栈顶的运算符级别,那么就进行运算
             * 直到当前运算法能够入栈(当前运算符大于栈顶的运算符)
             */
            while(operatorStack.getSize() > 0 && !getOperatorLevel(data, operatorStack.peek().toString())){
                reallyOperation();
            }

            // 如果当前运算符大于栈顶的运算符级别,那么就直接入栈
            operatorStack.push(data);
        }

        // 如果队列中的数据处理完,还需要将运算符栈中的符号和数字栈中的数据进行运算
        while (operatorStack.getSize() > 0){
            reallyOperation();
        }

        System.out.println(numberStack.pop());

    }

    /**
     * 取出运算符栈 和 数字栈中的数据进行真实运算
     */
    private static void reallyOperation() {
        int num1 = Integer.parseInt(numberStack.pop().toString());
        int num2 = Integer.parseInt(numberStack.pop().toString());
        // 这里注意栈的出栈顺序
        int result = operation(num2,num1,operatorStack.pop().toString());
        // 将运算的结果存放到数字栈中
        numberStack.push(result);
    }

    /**
     * 返回两个运算符的优先级
     * @param o1 运算符1
     * @param o2 运算符2
     * @return  o1 > o2
     */
    private static boolean getOperatorLevel(String o1, String o2){
        return operatorLevel.get(o1) > operatorLevel.get(o2);
    }

    /**
     * 将输入的值切割成一个个的字符存放到队列中
     * @param input  输入的文本值
     * @return  存放运算公式的队列
     */
    private static MyQueue spiltValueToQueue(String input) {
        MyQueue queue = new MyQueue();
        char[] chars = input.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            queue.enQueue(chars[i]);
        }
        return queue;
    }

    /**
     * 运算方法
     * @param num1  数字1
     * @param num2  数字2
     * @param operator  运算符号
     */
    public static int operation(int num1, int num2, String operator){
        switch (operator){
            case "+":
                return num1 + num2;
            case "-":
                return num1 - num2;
            case "*":
                return num1 * num2;
            case "/":
                return num1 / num2;
            default:
                return 0;
        }
    }

    /**
     * 测试自定义栈,队列是否有问题
     */
    public static void testStackAndQueue(){
        MyStack data = new MyStack();
        MyQueue queue = new MyQueue();
        for (int i = 0; i < 5; i++) {
            data.push(i);
            queue.enQueue(i);
        }

        while (data.getSize() > 0){
            System.out.println(data.pop());
        }

        while (queue.getSize() > 0){
            System.out.println(queue.deQueue());
        }
    }
}
