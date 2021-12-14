package com.monk.leetcode.structure.graph;

import com.monk.leetcode.structure.graph.test.GraphTools;

import java.util.Map;

public class TestGraph {

    private static Graph.WeightManager<Double> weightManager = new Graph.WeightManager<Double>() {
        @Override
        public int compare(Double e1, Double e2) {
            return e1.compareTo(e2);
        }

        @Override
        public Double add(Double e1, Double e2) {
            return e1 + e2;
        }

        @Override
        public Double zero() {
            return 0.0;
        }
    };

    public static void main(String[] args) {
        // testForAddEdge(new ListGraph<>());
        // testForRemove(new ListGraph<>());

        testForBFS(GraphTools.undirectedGraph(GraphTools.BFS_01), "A");
        testForBFS(GraphTools.directedGraph(GraphTools.BFS_02), 2);
        testForBFS(GraphTools.undirectedGraph(GraphTools.BFS_03), 1);
        testForBFS(GraphTools.directedGraph(GraphTools.BFS_04), 1);

        testForDFS(GraphTools.undirectedGraph(GraphTools.DFS_01), 1);
        testForDFS(GraphTools.directedGraph(GraphTools.DFS_02), "a");

        testTopologicalSort(GraphTools.directedGraph(GraphTools.TOPO));

        testShortestPath();

        testShortestPathInfo();

        testShortestPathInfoForBellmanFord(GraphTools.NEGATIVE_WEIGHT1, "A");
        testShortestPathInfoForBellmanFord(GraphTools.NEGATIVE_WEIGHT2, 0);

        testShortestPathInfoForFloyd(GraphTools.SP);
    }

    /**
     * 测试多源多点最短路径
     *
     * @param data
     */
    private static void testShortestPathInfoForFloyd(Object[][] data) {
        Graph<Object, Double> graph = GraphTools.directedGraph(data, weightManager);
        Map<Object, Map<Object, Graph.PathInfo<Object, Double>>> pathInfo = graph.shortestPathInfo();
        if (null == pathInfo) return;
        pathInfo.forEach((Object key, Map<Object, Graph.PathInfo<Object, Double>> pathInfoMap) -> {
            System.out.println("from -> " + key);
            pathInfoMap.forEach((Object vertex, Graph.PathInfo<Object, Double> path) -> {
                System.out.println(vertex + "_" + path);
            });
        });
        System.out.println("=======================================");
    }

    /**
     * 测试最短路径计算(针对Bellman-Ford算法)
     */
    private static void testShortestPathInfoForBellmanFord(Object[][] data, Object begin) {
        Graph<Object, Double> graph = GraphTools.directedGraph(data, weightManager);
        Map<Object, Graph.PathInfo<Object, Double>> pathInfo = graph.shortestPathInfo(begin);
        if (null == pathInfo) {
            System.out.println("=======================================");
            return;
        }
        pathInfo.forEach((Object key, Graph.PathInfo<Object, Double> value) -> {
            System.out.println(key + "_" + value);
        });
        System.out.println("=======================================");
    }

    /**
     * 测试最短路径计算
     */
    private static void testShortestPathInfo() {
        Graph<Object, Double> graph = GraphTools.directedGraph(GraphTools.SP, weightManager);
        Map<Object, Graph.PathInfo<Object, Double>> a = graph.shortestPathInfo("A");
        a.forEach((Object key, Graph.PathInfo<Object, Double> value) -> {
            System.out.println(key + "_" + value);
        });
        System.out.println("=======================================");
    }

    /**
     * 测试最短路径计算
     */
    private static void testShortestPath() {
        Graph<Object, Double> graph = GraphTools.directedGraph(GraphTools.SP, weightManager);
        Map<Object, Double> a = graph.shortestPath("A");
        a.forEach((Object key, Double value) -> {
            System.out.println(key + "_" + value);
        });
        System.out.println("=======================================");
    }

    /**
     * 测试拓扑排序
     */
    private static void testTopologicalSort(Graph<Object, Double> graph) {
        System.out.println(graph.topologicalSort());
        System.out.println("=======================================");
    }

    /**
     * 测试广度优先搜索
     *
     * @param graph 图
     * @param begin 起点
     */
    private static void testForBFS(Graph<Object, Double> graph, Object begin) {
        graph.breadthFirstSearch(begin, (Object obj) -> {
            System.out.print(obj + ", ");
            return false;
        });
        System.out.println("");
    }

    /**
     * 测试深度优先搜索
     *
     * @param graph 图
     * @param begin 起点
     */
    private static void testForDFS(Graph<Object, Double> graph, Object begin) {
        graph.depthFirstSearch(begin, (Object obj) -> {
            System.out.print(obj + ", ");
            return false;
        });
        System.out.println("");
    }

    /**
     * 测试添加边方法
     *
     * @param graph
     */
    private static void testForAddEdge(Graph<String, Integer> graph) {
        graph.addEdge("V0", "V1");
        graph.addEdge("V1", "V0");

        graph.addEdge("V0", "V2");
        graph.addEdge("V2", "V0");

        graph.addEdge("V0", "V3");
        graph.addEdge("V3", "V0");

        graph.addEdge("V1", "V2");
        graph.addEdge("V2", "V1");

        graph.addEdge("V2", "V3");
        graph.addEdge("V3", "V2");

        graph.print();
    }

    /**
     * 测试删除顶点,删除边方法
     *
     * @param graph
     */
    private static void testForRemove(Graph<String, Integer> graph) {
        graph.addEdge("V1", "V0", 9);
        graph.addEdge("V1", "V2", 3);
        graph.addEdge("V2", "V0", 2);
        graph.addEdge("V2", "V3", 5);
        graph.addEdge("V3", "V4", 1);
        graph.addEdge("V0", "V4", 6);

        graph.removeEdge("V0", "V4");
        graph.removeVertex("V0");

        graph.print();
    }
}
