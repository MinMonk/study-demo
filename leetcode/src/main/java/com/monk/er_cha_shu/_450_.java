package com.monk.er_cha_shu;

/**
 * https://leetcode-cn.com/problems/delete-node-in-a-bst/
 *
 * 450. 删除二叉搜索树中的节点
 */
public class _450_ {

    public static void main(String[] args) {
        _450_ demo = new _450_();
        TreeNode root = TreeNode.buildData();
        root.preorderTraversal(root).forEach(e -> {System.out.print(e + ",");});
    }

    public TreeNode deleteNode(TreeNode root, int key) {
        if (root == null) return null;

        TreeNode keyNote = node(root, key);
        return root;
    }

    public TreeNode deleteNode(TreeNode root, TreeNode node) {
        if (node == null) return null;

        // 先处理度为2的节点
        if(node.hasTwoLeaf()){

        }

        // 再处理度<2的节点
        TreeNode replacement = node.left != null ? node.left : node.right;


        return root;
    }

    /**
     * 找当前节点的后继节点
     * @param node
     * @return
     */
    private TreeNode successor(TreeNode node){
        if(node == null) return null;

        TreeNode suc = node.right;
        if(suc != null){
            while (suc.left != null){
                suc = suc.left;
            }
            return suc;
        }

        //while (node.pa)
        return null;
    }

    /**
     * 根据key找到对应的节点
     * @param root
     * @param key
     * @return
     */
    private TreeNode node(TreeNode root, int key){
        if(key <=0 ) return null;

        TreeNode node = root;
        while (node != null){
            if(key == node.val) return node;

            if(key > node.val){
                node = node.right;
            }else{
                node = node.left;
            }
        }
        return node;
    }
}
