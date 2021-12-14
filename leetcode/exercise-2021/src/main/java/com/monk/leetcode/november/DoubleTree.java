package com.monk.leetcode.november;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树对象
 */
public class DoubleTree {

    private DoubleLinkNode root;

    /**
     * 往二叉树中添加值
     * @param value   值
     */
    public void add(Object value){
        DoubleLinkNode node = new DoubleLinkNode(value);
        if(null == root){
            root = node;
        }else{
            DoubleLinkNode currNode = root;
            while (true){
                if(compareObject(value, currNode.getValue())){
                    // 如果当前插入的值小余当前节点就放到左边
                    if(null == currNode.getLeft()){
                        currNode.setLeft(node);
                        break;
                    }else{
                        currNode = currNode.getLeft();
                    }
                }else{
                    // 如果当前插入的值小余当前节点就放到右边
                    if(null == currNode.getRight()){
                        currNode.setRight(node);
                        break;
                    }else{
                        currNode = currNode.getRight();
                    }
                }
            }
        }
    }

    /**
     * 遍历二叉树节点
     */
    public void loop(){
        showNode(root);
    }

    public void levelOrderTraversal(){
        levelOrderTraversal(root);
    }

    public void levelOrderTraversal(DoubleLinkNode node){
        if(null == node) return;;

        Queue<DoubleLinkNode> queue = new LinkedList<>();
        queue.offer(node);
        while (!queue.isEmpty()){
            DoubleLinkNode temp = queue.poll();
            System.out.println(temp.getValue());
            if(temp.getLeft() != null){
                queue.offer(temp.getLeft());
            }
            if(temp.getRight() != null){
                queue.offer(temp.getRight());
            }
        }
    }

    /**
     * 递归遍历二叉树节点
     * @param node
     */
    private void showNode(DoubleLinkNode node){

        // System.out.println(node.getValue());//前序打印

        if(null != node.getLeft()){
            showNode(node.getLeft());
        }
        // System.out.println(node.getValue());//中序打印

        if(null != node.getRight()){
            showNode(node.getRight());
        }
        System.out.println(node.getValue());//后序打印
    }

    private static boolean compareObject(Object obj1, Object obj2){
        return (int)obj1 < (int)obj2;
    }
}
