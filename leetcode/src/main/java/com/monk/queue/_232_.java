package com.monk.queue;

import java.util.Stack;

/**
 *  https://leetcode-cn.com/problems/implement-queue-using-stacks/
 *
 *  232. 用栈实现队列
 */
public class _232_ {

    Stack<Integer> inStack = null;
    Stack<Integer> outStack = null;

    public _232_() {
        inStack = new Stack<Integer>();
        outStack = new Stack<Integer>();
    }

    public static void main(String[] args) {
        _232_ stack = new _232_();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        stack.push(4);
        while (!stack.empty()){
            System.out.println(stack.pop());
        }
    }

    public void push(int x) {
        inStack.push(x);
    }

    public int pop() {
        checkOutStack();
        return outStack.pop();
    }

    public int peek() {
        checkOutStack();
        return outStack.peek();
    }

    private void checkOutStack() {
        if (outStack.isEmpty()) {
            while (!inStack.isEmpty()) {
                outStack.push(inStack.pop());
            }
        }
    }

    public boolean empty() {
        return inStack.isEmpty() && outStack.isEmpty();
    }
}
