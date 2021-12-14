package com.monk.leetcode.structure.graph;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 图接口定义
 * @param <V>
 * @param <V>
 */
public interface Graph<V, E> {
    /**
     * 返回图中顶点的个数
     *
     * @return
     */
    int vertexSize();

    /**
     * 返回图中边的个数
     *
     * @return
     */
    int edgeSize();

    /**
     * 往图中添加顶点
     *
     * @param v 顶点
     */
    void addVertex(V v);

    /**
     * 往图中添加边
     *
     * @param from 起点
     * @param to   终点
     */
    void addEdge(V from, V to);

    /**
     * 往图中添加边
     *
     * @param from   起点
     * @param to     终点
     * @param weight 权重
     */
    void addEdge(V from, V to, E weight);

    /**
     * 删除顶点
     *
     * @param v 顶点
     */
    void removeVertex(V v);

    /**
     * 删除边
     *
     * @param from 起点
     * @param to   终点
     */
    void removeEdge(V from, V to);

    /**
     * 深度优先搜索
     *
     * @param begin 起点
     */
    void depthFirstSearch(V begin, Visitor<V> visitor);

    /**
     * 广度优先搜索
     *
     * @param begin 起点
     */
    void breadthFirstSearch(V begin, Visitor<V> visitor);

    /**
     * 拓扑排序(使用卡恩算法,Kahn于1962年提出的)
     * @return
     */
    List<V> topologicalSort();

    /**
     * 最小生成树
     *  有两种实现,均在实现类中实现: 1. Prim算法  2. Kruskal算法
     * @return
     */
    Set<EdgeInfo<V, E>> minSpanningTree();

    /**
     * 单源多点的最短路径(只显示权值)
     * @param begin  起点
     * @return
     */
    Map<V, E> shortestPath(V begin);

    /**
     * 单源最短路径(显示完整的路径信息)
     *  有两种实现,均在实现类中实现: 1. Dijkstra算法  2. Bellman-Ford算法
     * @param begin  起点
     * @return
     */
    Map<V, PathInfo<V, E>> shortestPathInfo(V begin);

    /**
     * 多源最短路径
     *
     * 实现算法: Floyd, 时间复杂度 O(V ^ 3)   V = 顶点的数量,理论上将效率比执行V次Dijkstra效率要好
     *
     * <pre>
     *     实现思路:
     *      假设存在 v1, v2(可以视为经过n个顶点) ,v3三个顶点, 循环比较 (v1到v3的权值) ? (v1到v2 + v2到v3的权值),如果v1->v2->v3的合计权值
     *      小于v1->v3的权值,就更新v1->v3的最短路径信息
     *
     * </pre>
     * @return
     */
    Map<V, Map<V, PathInfo<V, E>>> shortestPathInfo();


    /**
     * 打印图(测试方法,后期可以去掉这个接口)
     */
    @Deprecated
    void print();

    /**
     * 访问者对象
     * @param <V>
     */
    interface Visitor<V>{
        boolean visit(V v);
    }

    /**
     * 权重管理的接口
     * @param <E>
     */
    interface WeightManager<E>{
        /**
         * 比较两个权重的大小
         * @param e1
         * @param e2
         * @return
         */
        int compare(E e1, E e2);

        /**
         * 两个权重的想加
         * @param e1
         * @param e2
         * @return
         */
         E add(E e1, E e2);

        /**
         * 生成一个0的权值
         */
        E zero();
    }

    /**
     * 对外的边信息
     * @param <V> 顶点的值
     * @param <E>  权重
     */
    class EdgeInfo<V, E>{
        V from;
        V to;
        E weight;

        public EdgeInfo(V from, V to, E weight){
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "EdgeInfo [from=" + from + ", to=" + to + ", weight=" + weight + "]";
        }
    }

    /**
     * 对外暴露的到达顶点的路径信息
     * @param <V> 顶点的值
     * @param <E>  权重
     */
    class PathInfo<V, E>{
        private E weight;
        private List<EdgeInfo<V, E>> edgeInfos = new LinkedList<>();

        public PathInfo() {
        }

        public PathInfo(E weight) {
            this.weight = weight;
        }

        public E getWeight() {
            return weight;
        }

        public void setWeight(E weight) {
            this.weight = weight;
        }

        public List<EdgeInfo<V, E>> getEdgeInfos() {
            return edgeInfos;
        }

        public void setEdgeInfos(List<EdgeInfo<V, E>> edgeInfos) {
            this.edgeInfos = edgeInfos;
        }

        @Override
        public String toString() {
            return "PathInfo [weight=" + weight + ", edgeInfos=" + edgeInfos + "]";
        }
    }
}
