package com.monk.leetcode.structure.hash;


import com.monk.leetcode.structure.tree.printer.BinaryTreeInfo;
import com.monk.leetcode.structure.tree.printer.BinaryTrees;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;

public class HashMap<K, V> implements Map<K, V> {

    private static final boolean RED = false;
    private static final boolean BLACK = true;
    private static final int DEFAULT_CAPACITY = 1 << 4;  //16
    private static final float DEFAULT_LOAD_FACTOR = 0.75f;

    private int size;

    private Node<K, V>[] tables;

    public HashMap() {
        tables = new Node[DEFAULT_CAPACITY];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        // 如果size为0,说明Map中没有元素,就没有必要去遍历tables清空了
        if (size == 0) return;

        for (int i = 0; i < tables.length; i++) {
            tables[i] = null;
        }

        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * 1. 先根据hash算出索引
     * 2. 将值放到tables[i]
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public V put(K key, V value) {

        resize();

        int index = index(key);
        Node<K, V> root = tables[index];
        if (root == null) {
            // 如果节点为空,说明这个桶内没有元素,直接将添加的值作为根节点
            root = createNode(key, value, null);
            tables[index] = root;
            size++;
            fixAfterPut(root);
            return null;
        }

        // 能来到这里说明,这个key对应的桶不为空,那么就需要根据红黑树的性质进行比较然后插入到红黑树上
        Node<K, V> node = root;
        Node<K, V> parent = root;
        Node<K, V> result = null;
        int cmp = 0;//比较结果
        K k1 = key;
        int h1 = hash(k1);
        boolean searched = false;// 是否已经搜索过这个key的一个标志位,用于在不知道两个key谁大谁小的时候,避免每一次都遍历
        do {
            parent = node;
            K k2 = node.key;
            int h2 = hash(k2);

            // 先比较两个key的hash值
            if (h1 > h2) {
                cmp = 1;
            } else if (h1 < h2) {
                cmp = -1;
            } else if (Objects.equals(k1, k2)) {//再通过equals比较两个key是否相等
                cmp = 0;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (cmp = ((Comparable) k1).compareTo(k2)) != 0) {
                /***
                 * 能进到这里来,说明key必然已经分清楚谁大谁小了,因为这里的if最后一个条件是不等于0
                 * 所以这里什么都不做,只是单纯的调用Comparable的比较方法尝试区分开谁大谁小
                 */
            } else if (searched) {
                // 如果经过进过hash值,equals方法的比较还不能判断出大小,
                // 并且已经遍历所有节点扫描过一遍了,还不能分清楚大小,那么就根据内存地址的hash值进行比较
                cmp = (System.identityHashCode(k1) - System.identityHashCode(k2)) > 0 ? 1 : -1;
            } else {
                /**
                 * 能进入到这里,说明这两个key已经无法通过hash值,equals以及comparable来比较大小,那么这里就
                 * 遍历当前这个桶中的所有元素来确定当前插入的key是否已经存在
                 */
                if ((node.left != null && (result = node(node.left, k1)) != null
                        || (node.right != null && (result = node(node.right, k1)) != null))) {
                    node = result;
                    cmp = 0;
                } else {
                    searched = true;
                    cmp = (System.identityHashCode(k1) - System.identityHashCode(k2)) > 0 ? 1 : -1;
                }
            }

            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            } else {
                // 当相等时,将old值取出返回出去,用新值覆盖old值
                V oldValue = node.value;
                node.value = value;
                node.key = key;
                node.hash = h1;//这里的hash值可以省略覆盖,因为节点都相等,那么hash固然相等和删除时不一样
                return oldValue;
            }
        } while (node != null);

        Node<K, V> newNode = createNode(key, value, parent);
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }
        size++;

        // 新添加节点的后序处理,给子类扩展
        fixAfterPut(newNode);
        return null;
    }

    /**
     * 扩容
     */
    private void resize() {
        if (size / tables.length <= DEFAULT_LOAD_FACTOR) return;

        Node<K, V>[] oldTables = tables;
        tables = new Node[oldTables.length << 1];

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < oldTables.length; i++) {
            if (oldTables[i] == null) continue;

            queue.offer(oldTables[i]);
            while (!queue.isEmpty()) {
                Node<K, V> temp = queue.poll();
                if (temp.left != null) queue.offer(temp.left);
                if (temp.right != null) queue.offer(temp.right);

                moveNode(temp);
            }

        }
    }

    /**
     * 扩容后的节点移动
     *
     * @param newNode
     */
    private void moveNode(Node<K, V> newNode) {
        newNode.parent = null;
        newNode.left = null;
        newNode.right = null;
        newNode.color = RED;

        int index = index(newNode);

        Node<K, V> root = tables[index];
        if (root == null) {
            // 如果节点为空,说明这个桶内没有元素,直接将添加的值作为根节点
            root = newNode;
            tables[index] = root;
            fixAfterPut(root);

            /**
             * 由于是转移Node,所以不需要size++
             */
            return;
        }

        // 能来到这里说明,这个key对应的桶不为空,那么就需要根据红黑树的性质进行比较然后插入到红黑树上
        Node<K, V> node = root;
        Node<K, V> parent = root;
        int cmp = 0;//比较结果
        K k1 = newNode.key;
        int h1 = newNode.hash;
        do {
            parent = node;
            K k2 = node.key;
            int h2 = node.hash;

            // 先比较两个key的hash值
            if (h1 > h2) {
                cmp = 1;
            } else if (h1 < h2) {
                cmp = -1;
                /**
                 * 因为是转移元素,再根据红黑树的性质,树上不可能存在两个相等的值,故在这里可以省略掉
                 */
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (cmp = ((Comparable) k1).compareTo(k2)) != 0) {
                /***
                 * 能进到这里来,说明key必然已经分清楚谁大谁小了,因为这里的if最后一个条件是不等于0
                 * 所以这里什么都不做,只是单纯的调用Comparable的比较方法尝试区分开谁大谁小
                 */
            } else {
                cmp = (System.identityHashCode(k1) - System.identityHashCode(k2)) > 0 ? 1 : -1;
            }

            /**
             * 因为是转移元素,再根据红黑树的性质,树上不可能存在两个相等的值,故在这里可以省略掉
             */
            if (cmp > 0) {
                node = node.right;
            } else if (cmp < 0) {
                node = node.left;
            }
        } while (node != null);

        newNode.parent = parent;
        if (cmp > 0) {
            parent.right = newNode;
        } else {
            parent.left = newNode;
        }

        // 新添加节点的后序处理,给子类扩展
        fixAfterPut(newNode);
    }

    @Override
    public V get(K key) {
        Node<K, V> node = node(key);
        return node == null ? null : node.value;
    }

    @Override
    public V remove(K key) {
        return remove(node(key));
    }

    private V remove(Node<K, V> node) {
        if (node == null) return null;

        Node<K, V> willNode = node;
        V oldValue = node.value;

        /**
         * 1. 先删除度为2的节点
         */
        if (node.hasTwoLeaf()) {
            // 1.1. 先找到当前节点的前驱or后继节点(这里自己定义,这里姑且定义为找到后继节点,因为大多数情况下都是找后继节点)
            Node<K, V> next = successor(node);

            // 1.2. 用后继节点的值覆盖原节点的值
            node.key = next.key;
            node.value = next.value;
            node.hash = next.hash;

            /**
             * <pre>
             * 1.3. 删除后继节点
             *      分析:
             *          这里有点类似于降维打击的意思,因为当前方法是删除一个节点,只是先删除度为2的节点,那么这里将度为2的节点降维成<2的节点,
             *          那么在当前方法中,当前行代码的后续会处理这种度<2的节点,交给后续的代码去删除这个后继节点
             * </pre>
             */
            node = next;
        }

        /**
         * 2. 处理度为1的节点
         */
        // 2.1. 先获取要删除节点的子节点
        Node<K, V> replacement = node.left != null ? node.left : node.right;
        int index = index(node);
        // 2.2. 将子节点的parent指针指向该节点的父节点
        if (replacement != null) {
            replacement.parent = node.parent;
            // 2.3. 完成子节点的替换
            if (node.parent == null) {
                // 如果要删除的节点度为1,切父节点为空,那么这个节点只可能是root节点,那么将root节点指向该节点的孩子节点(replacement)即可
                tables[index] = replacement;
            } else if (node.isLeftChild()) {
                // 如果要删除节点是出于父节点的左边,那就将父节点的左指针指向该节点的孩子节点(replacement)即可
                node.parent.left = replacement;
            } else {
                // 如果要删除节点是出于父节点的右边,那就将父节点的右指针指向该节点的孩子节点(replacement)即可
                node.parent.right = replacement;
            }

            fixAfterRemove(replacement);
        } else if (node.parent == null) {
            /**
             * <pre>
             * 3. 处理度为0的节点
             *      分析:
             *          1. 判断该节点是否是根节点
             *          2. 判断该叶子节点是处于父节点的左侧还是右侧
             *          3. 将父节点的左/右指针置空
             *
             * </pre>
             */
            // 3.1 如果要删除节点的父节点为空,那么该节点必然为root节点,而删除root节点,只需要要root置空即可
            tables[index] = null;
        } else {
            // 3.2 判断该叶子节点是处于父节点的左侧还是右侧
            if (node.isLeftChild()) {
                // 3.3 将父节点的左指针置空
                node.parent.left = null;
            } else {
                // 3.3 将父节点的右指针置空
                node.parent.right = null;
            }

            fixAfterRemove(node);
        }

        afterRemove(willNode, node);

        size--;

        return oldValue;
    }

    /**
     * 交给子类去扩展(在这里是留给自定义的LinkedHashMap子类去实现,主要是维护prev+next两个属性的值)
     * @param willNode  将要删除的节点
     * @param removedNode  实际删除的节点
     */
    protected void afterRemove(Node<K, V> willNode, Node<K, V> removedNode) {
        // 交给子类实现
    }

    @Override
    public boolean containsKey(K key) {
        return node(key) != null;
    }

    /**
     * 判断值是否存在,通过层序遍历,一个节点一个节点的进行比较
     *
     * @param value
     * @return
     */
    @Override
    public boolean containsValue(V value) {
        if (size == 0) return false;

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < tables.length; i++) {
            if (tables[i] == null) continue;
            queue.offer(tables[i]);
            while (!queue.isEmpty()) {
                Node<K, V> temp = queue.poll();
                if (Objects.equals(value, temp.value)) return true;

                if (temp.left != null) queue.offer(temp.left);
                if (temp.right != null) queue.offer(temp.right);
            }
        }
        return false;
    }

    @Override
    public void traversal(Visitor<K, V> visitor) {
        if (size == 0 || null == visitor) return;

        Queue<Node<K, V>> queue = new LinkedList<>();
        for (int i = 0; i < tables.length; i++) {
            if (tables[i] == null) continue;

            queue.offer(tables[i]);
            while (!queue.isEmpty()) {
                Node<K, V> temp = queue.poll();
                if (visitor.visit(temp.key, temp.value)) return;

                if (temp.left != null) queue.offer(temp.left);
                if (temp.right != null) queue.offer(temp.right);
            }
        }
    }

    /**
     * 打印HashMap的debug信息,将HashMap中每个桶的元素都打印输出出来
     */
    public void printDebugMap() {
        if (size == 0) return;

        for (int i = 0; i < tables.length; i++) {
            Node<K, V> root = tables[i];
            System.out.println("【index = " + i + "】");
            BinaryTrees.print(new BinaryTreeInfo() {
                @Override
                public Object root() {
                    return root;
                }

                @Override
                public Object left(Object node) {
                    return ((Node<K, V>) node).left;
                }

                @Override
                public Object right(Object node) {
                    return ((Node<K, V>) node).right;
                }

                @Override
                public Object string(Object node) {
                    return node;
                }
            });
            System.out.println("---------------------------------------------------");
        }
    }

    /**
     * 根据key算出对应的下标索引
     *
     * @param key
     * @return
     */
    private int index(K key) {
        return hash(key) & (tables.length - 1);
    }

    /**
     * 根据key计算key的hash值
     *
     * @param key
     * @return
     */
    private int hash(K key) {
        if (key == null) return 0;

        int hash = key.hashCode();
        return hash ^ (hash >>> 16);
    }

    /**
     * 根据node节点计算出节点的下标索引
     *
     * @param node
     * @return
     */
    private int index(Node node) {
        // 这里的&运算,好比是%取模运算,返回0到tables长度的索引
        return node.hash & (tables.length - 1);
    }


    /**
     * 创建节点(可以给子类去扩展)
     *
     * @param key
     * @param value
     * @param parent
     * @return
     */
    protected Node<K, V> createNode(K key, V value, Node<K, V> parent) {
        return new Node<>(key, value, parent);
    }

    /**
     * 根据key来找到对应的节点
     *
     * @param key
     * @return
     */
    private Node<K, V> node(K key) {
        Node<K, V> root = tables[index(key)];
        return root == null ? null : node(root, key);
    }

    /**
     * 根据key找到对应的节点
     *
     * @param node
     * @param k1
     * @return
     */
    private Node<K, V> node(Node<K, V> node, K k1) {
        int h1 = hash(k1);
        Node<K, V> result = null;//存储查找结果

        int cmp = 0;
        while (node != null) {
            K k2 = node.key;
            int h2 = node.hash;

            if (h1 > h2) {
                node = node.right;
            } else if (h1 < h2) {
                node = node.left;
            } else if (Objects.equals(k1, k2)) {
                return node;
            } else if (k1 != null && k2 != null
                    && k1.getClass() == k2.getClass()
                    && k1 instanceof Comparable
                    && (cmp = ((Comparable) k1).compareTo(k2)) != 0) {
                node = cmp > 0 ? node.right : node.left;
            } else if (node.right != null && (result = node(node.right, k1)) != null) {
                // 通过递归调用,在右边找到了就直接返回找到的node节点
                return result;
            } else {
                /**
                 * 这里实际上是一个优化后的写法,这里也可以像上面一样通过递归去查找左边的子树
                 * 但是递归的复杂度较高,这里通过while循环来找也是一样的
                 */
                node = node.left;
            }
        }

        return null;
    }

    /**
     * 修复添加节点后的红黑树特性
     *
     * @param node
     */
    protected void fixAfterPut(Node<K, V> node) {
        Node<K, V> parent = node.parent;

        // 如果父节点为空,则添加的是根节点,直接染成黑色返回即可(上溢到root节点的话,也视作是添加根节点)
        if (parent == null) {
            black(node);
            return;
        }

        // 如果父节点是黑色,就不用做处理
        if (isBlack(parent)) return;

        Node<K, V> uncle = parent.sibling();
        Node<K, V> grand = red(parent.parent);

        /**
         * 处理叔父节点是红色的情况
         * 1. 将parent和uncle节点染成黑色
         * 2. 将grand节点上溢
         */
        if (isRed(uncle)) {
            black(parent);
            black(uncle);

            // grand节点上溢(将grand节点当做是一个新添加的节点来递归添加,因为上溢可能会导致祖先节点出现上溢)
            fixAfterPut(grand);
            return;
        }

        /**
         *
         * 处理叔父节点不是红色的情况
         * <pre>
         *     分析: 当叔父节点不是红色的情况下,也分为4中情况(LL,LR,RL,RR),但是这四种情况和AVL树有些区别,
         *      1. LL : 将parent节点染成黑色,grand节点染成红色,再对grand节点进行右旋转
         *      2. LR : 将node节点(自己)染成黑色,grand节点染成红色,再对parent节点左旋转,grand节点进行右旋转
         *      3. RL : 将node节点(自己)染成黑色,grand节点染成红色,再对parent节点右旋转,grand节点进行左旋转
         *      4. RR : 将parent节点染成黑色,grand节点染成红色,再对grand节点进行左旋转
         *  注意:
         *      下述代码中,
         *      1. 由于都需要对grand节点进行染成红色,故在29行拿到grand节点的时候,就已经对其进行了染色操作
         *      2. 针对LL和LR情况,均需要对grand节点进行右旋转,故提出了公共代码放在了if-else外(Line:68)
         *      3. 针对RL和RR情况,都需要对grand节点进行左旋转,故也提出了公共代码放在了if-else外(Line:76)
         * </pre>
         */
        if (parent.isLeftChild()) {//L
            if (node.isLeftChild()) {//LL
                black(parent);
            } else {//LR
                black(node);
                rotateLeft(parent);
            }
            rotateRight(grand);
        } else {//R
            if (node.isLeftChild()) {//RL
                black(node);
                rotateRight(parent);
            } else {//RR
                black(parent);
            }
            rotateLeft(grand);
        }
    }

    /**
     * 左旋转
     * <pre>
     *     需要做的事情:
     *      1. grand.right = parent.left
     *      2. parent.left = grand
     *      3. 让parent作为当前子树的根节点
     *      4. 更新节点(child,parent,grand)的parent属性
     *      5. 更新节点的高度height属性
     * </pre>
     *
     * @param grand
     */
    private void rotateLeft(Node<K, V> grand) {
        Node<K, V> parent = grand.right;
        Node<K, V> child = parent.left;

        grand.right = child;
        parent.left = grand;

        afterRotate(grand, parent, child);
    }

    /**
     * 右旋转
     * <pre>
     *     需要做的事情:
     *      1. grand.left = parent.right
     *      2. parent.right = grand
     *      3. 让parent作为当前子树的根节点
     *      4. 更新节点(child,parent,grand)的parent属性
     *      5. 更新节点的高度height属性
     * </pre>
     *
     * @param grand
     */
    private void rotateRight(Node<K, V> grand) {
        Node<K, V> parent = grand.left;
        Node<K, V> child = parent.right;

        grand.left = child;
        parent.right = grand;

        afterRotate(grand, parent, child);
    }

    /**
     * 旋转操作后的公共操作
     * <pre>
     *      3. 让parent作为当前子树的根节点
     *      4. 更新节点(child,parent,grand)的parent属性
     *      5. 更新节点的高度height属性
     * </pre>
     *
     * @param grand
     * @param parent
     * @param child
     */
    private void afterRotate(Node<K, V> grand, Node<K, V> parent, Node<K, V> child) {
        // 1. 让parent作为当前子树的根节点
        parent.parent = grand.parent;

        //这里计算下当前树所在的桶所以,因为都是在同一棵树上,所以无论通过哪个节点计算出的下标索引都是一样的
        int index = index(grand);
        if (grand.isLeftChild()) {
            grand.parent.left = parent;
        } else if (grand.isRightChild()) {
            grand.parent.right = parent;
        } else {
            // 如果grand的父节点是空,那么说明,grand原来是root节点,那么就需要将root节点指向parent节点
            tables[index] = parent;
        }

        // 2. 更新节点的parent属性
        if (child != null) {
            child.parent = grand;
        }
        grand.parent = parent;
    }

    /**
     * 获取节点的后继节点(中序遍历)
     *
     * @param node
     * @return
     */
    protected Node<K, V> successor(Node<K, V> node) {
        if (node == null) return null;

        Node<K, V> suc = node.right;
        if (suc != null) {
            while (suc.left != null) {
                suc = suc.left;
            }
            return suc;
        }

        /**
         * 如果该节点的right为空,则它的前驱节点为parent.parent.(...).left,
         * 所以当父节点为空or当前节点不等于父节点的左边节点时,跳出循环
         */
        while (node.parent != null && !node.equals(node.parent.left)) {
            node = node.parent;
        }

        return node.parent;
    }

    /**
     * 修复删除后的红黑树特性
     *
     * @param node
     */
    private void fixAfterRemove(Node<K, V> node) {
        // 如果删除的节点是红色
        // 或者 用以取代删除节点的子节点是红色
        if (isRed(node)) {
            black(node);
            return;
        }

        Node<K, V> parent = node.parent;
        // 删除的是根节点
        if (parent == null) return;

        // 删除的是黑色叶子节点【下溢】
        // 判断被删除的node是左还是右
        boolean left = parent.left == null || node.isLeftChild();
        Node<K, V> sibling = left ? parent.right : parent.left;
        if (left) { // 被删除的节点在左边，兄弟节点在右边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateLeft(parent);
                // 更换兄弟
                sibling = parent.right;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    fixAfterRemove(parent);
                }
            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.right)) {
                    rotateRight(sibling);
                    sibling = parent.right;
                }

                color(sibling, colorOf(parent));
                black(sibling.right);
                black(parent);
                rotateLeft(parent);
            }
        } else { // 被删除的节点在右边，兄弟节点在左边
            if (isRed(sibling)) { // 兄弟节点是红色
                black(sibling);
                red(parent);
                rotateRight(parent);
                // 更换兄弟
                sibling = parent.left;
            }

            // 兄弟节点必然是黑色
            if (isBlack(sibling.left) && isBlack(sibling.right)) {
                // 兄弟节点没有1个红色子节点，父节点要向下跟兄弟节点合并
                boolean parentBlack = isBlack(parent);
                black(parent);
                red(sibling);
                if (parentBlack) {
                    fixAfterRemove(parent);
                }
            } else { // 兄弟节点至少有1个红色子节点，向兄弟节点借元素
                // 兄弟节点的左边是黑色，兄弟要先旋转
                if (isBlack(sibling.left)) {
                    rotateLeft(sibling);
                    sibling = parent.left;
                }

                color(sibling, colorOf(parent));
                black(sibling.left);
                black(parent);
                rotateRight(parent);
            }
        }
    }

    /**
     * 对传进来的节点进行染色
     *
     * @param node
     * @param color
     * @return
     */
    private Node<K, V> color(Node<K, V> node, boolean color) {
        if (node == null) return node;
        node.color = color;
        return node;
    }

    /**
     * 将节点染成红色
     *
     * @param node
     * @return
     */
    private Node<K, V> red(Node<K, V> node) {
        return color(node, RED);
    }

    /**
     * 将节点染成黑色
     *
     * @param node
     * @return
     */
    private Node<K, V> black(Node<K, V> node) {
        return color(node, BLACK);
    }

    /**
     * 返回传入节点的颜色(如果是空节点,返回的黑色)
     *
     * @param node
     * @return
     */
    private boolean colorOf(Node<K, V> node) {
        return node == null ? BLACK : node.color;
    }

    /**
     * 判断节点的颜色是否是红色
     *
     * @param node
     * @return
     */
    private boolean isRed(Node<K, V> node) {
        return colorOf(node) == RED;
    }

    /**
     * 判断节点的颜色是否是黑色
     *
     * @param node
     * @return
     */
    private boolean isBlack(Node<K, V> node) {
        return colorOf(node) == BLACK;
    }

    /**
     * 红黑树节点对象
     *
     * @param <K>
     * @param <V>
     */
    protected static class Node<K, V> {
        int hash;
        boolean color = RED;
        K key;
        V value;
        Node<K, V> left;
        Node<K, V> right;
        Node<K, V> parent;

        public Node(K key, V value, Node<K, V> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;

            int hash = key == null ? 0 : key.hashCode();
            this.hash = hash ^ (hash >>> 16);
        }

        /**
         * 是否是度为2的节点
         *
         * @return
         */
        public boolean hasTwoLeaf() {
            return left != null && right != null;
        }

        /**
         * 判断是否是叶子节点(叶子节点:既没有左节点,也没有右节点)
         *
         * @return
         */
        public boolean isLeaf() {
            return left == null && right == null;
        }

        /**
         * 是否是左节点
         *
         * @return
         */
        public boolean isLeftChild() {
            return parent != null && this == parent.left;
        }

        /**
         * 是否是右节点
         *
         * @return
         */
        public boolean isRightChild() {
            return parent != null && this == parent.right;
        }

        /**
         * 返回当前节点的兄弟节点
         *
         * @return
         */
        public Node<K, V> sibling() {
            if (isLeftChild()) {
                return parent.right;
            }

            if (isRightChild()) {
                return parent.left;
            }

            return null;
        }

        @Override
        public String toString() {
            return "Node_" + key + "_" + value;
        }

    }
}
