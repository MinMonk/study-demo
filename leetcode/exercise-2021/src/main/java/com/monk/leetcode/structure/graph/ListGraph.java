package com.monk.leetcode.structure.graph;

import com.monk.leetcode.structure.union.GenericUnionFind;

import java.util.*;

/**
 * 邻接表
 *
 * @param <V>
 * @param <E>
 */
public class ListGraph<V, E> implements Graph<V, E> {

    /**
     * 内部类 -- >顶点
     *
     * @param <V> 顶点
     * @param <E> 边
     */
    private static class Vertex<V, E> {
        /**
         * 顶点上的值
         */
        V value;

        /**
         * 顶点上进来的边
         */
        Set<Edge<V, E>> inEdges = new HashSet<>();

        /**
         * 顶点上出去的边
         */
        Set<Edge<V, E>> outEdges = new HashSet<>();

        public Vertex(V value) {
            this.value = value;
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return Objects.equals(value, ((Vertex<V, E>) obj).value);
        }

        @Override
        public String toString() {
            return value == null ? "null" : value.toString();
        }
    }

    /**
     * 内部类 -- >边
     *
     * @param <V> 顶点
     * @param <E> 边
     */
    private static class Edge<V, E> {

        /**
         * 权重
         */
        E weight;

        /**
         * 起点
         */
        Vertex<V, E> from;

        /**
         * 终点
         */
        Vertex<V, E> to;

        public Edge(Vertex<V, E> from, Vertex<V, E> to) {
            this.from = from;
            this.to = to;
        }

        public Edge(Vertex<V, E> from, Vertex<V, E> to, E weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public EdgeInfo<V, E> info() {
            return new EdgeInfo(from, to, weight);
        }

        @Override
        public int hashCode() {
            return from.hashCode() * 31 + to.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            Edge<V, E> vertex = (Edge<V, E>) obj;
            return Objects.equals(from, vertex.from) && Objects.equals(to, vertex.to);
        }

        @Override
        public String toString() {
            return "Edge [from=" + from + ", to=" + to + ", weight=" + weight + "]";
        }
    }

    /**
     * 储存图中所有的顶点
     */
    private Map<V, Vertex<V, E>> vertices = new HashMap<>();

    /**
     * 储存图中所有的边
     */
    private Set<Edge<V, E>> edges = new HashSet<>();


    private WeightManager weightManager;

    public ListGraph() {

    }

    public ListGraph(WeightManager<E> weightManager) {
        this.weightManager = weightManager;
    }

    private Comparator<Edge<V, E>> comparator = (Edge<V, E> e1, Edge<V, E> e2) -> {
        return weightManager.compare(e1, e2);
    };

    @Override
    public int vertexSize() {
        return vertices.size();
    }

    @Override
    public int edgeSize() {
        return edges.size();
    }

    @Override
    public void addVertex(V v) {
        // 如果这个顶点已经存在就直接返回
        if (vertices.containsKey(v)) return;

        vertices.put(v, new Vertex<>(v));
    }

    /**
     * 确定顶点是否存在,不存在则创建这个顶点
     *
     * @param v 顶点上的值
     */
    private Vertex<V, E> ensureVertex(V v) {
        Vertex<V, E> vertex = vertices.get(v);
        if (vertex == null) {
            // 如果顶点不存在,就创建这个顶点并添加到顶点的集合(vertices)中
            vertex = new Vertex<>(v);
            vertices.put(v, vertex);
        }
        return vertex;
    }

    @Override
    public void addEdge(V from, V to) {
        addEdge(from, to, null);
    }

    @Override
    public void addEdge(V from, V to, E weight) {
        // 先判断顶点是否存在
        Vertex<V, E> fromVertex = ensureVertex(from);
        Vertex<V, E> toVertex = ensureVertex(to);

        // 判断边是否存在
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex, weight);

        // 如果已经存在这条边,先删除这个边,再重新添加这个边,已达到覆盖的效果
        if (edges.remove(edge)) {
            fromVertex.outEdges.remove(edge);
            toVertex.inEdges.remove(edge);
        }

        // 重新添加这个边
        fromVertex.outEdges.add(edge);
        toVertex.inEdges.add(edge);
        edges.add(edge);
    }

    @Override
    public void removeVertex(V v) {
        // 先判断删除的顶点是否存在,不存在就直接返回,不需要做任何事
        Vertex<V, E> vertex = vertices.get(v);
        if (null == vertex) return;

        // 删除这个顶点上出去的边
        for (Iterator<Edge<V, E>> iterator = vertex.outEdges.iterator(); iterator.hasNext(); ) {
            Edge<V, E> edge = iterator.next();
            // 从这个边所到达的顶点中的进来的边集合中删除这个边
            edge.to.inEdges.remove(edge);

            // 从outEdges集合中删除自己
            iterator.remove();

            // 从全局变量边的集合(edges)中删除这条边
            edges.remove(edge);
        }

        // 删除这个顶点上进来的边
        for (Iterator<Edge<V, E>> iterator = vertex.inEdges.iterator(); iterator.hasNext(); ) {
            Edge<V, E> edge = iterator.next();
            // 从这个边所到达的顶点中的出去的边集合中删除这个边
            edge.to.outEdges.remove(edge);

            // 从outEdges集合中删除自己
            iterator.remove();

            // 从全局变量边的集合(edges)中删除这条边
            edges.remove(edge);
        }
    }

    @Override
    public void removeEdge(V from, V to) {
        // 先判断要删除边所在的顶点是否存在,不存在就直接返回,不需要做任何事
        Vertex<V, E> fromVertex = vertices.get(from);
        if (null == fromVertex) return;

        Vertex<V, E> toVertex = vertices.get(to);
        if (null == toVertex) return;

        // 在确定边是否存在,存在才删除这个边
        Edge<V, E> edge = new Edge<>(fromVertex, toVertex);
        if (edges.remove(edge)) {
            fromVertex.outEdges.remove(edge);
            toVertex.inEdges.remove(edge);
        }
    }

    @Override
    public void depthFirstSearch(V begin, Visitor<V> visitor) {
        if (visitor == null) return;
        // 先判断入参所在的顶点是否存在,不存在就直接返回,不需要进行遍历
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (null == beginVertex) return;

        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        Stack<Vertex<V, E>> stack = new Stack<>();
        stack.push(beginVertex);
        if (visitor.visit(beginVertex.value)) return;
        visitedVertices.add(beginVertex);

        while (!stack.isEmpty()) {
            Vertex<V, E> vertex = stack.pop();

            // 遍历这个起点全部的边
            for (Edge<V, E> edge : vertex.outEdges) {
                // 如果这条边的终点已经遍历过,就跳过这个顶点
                if (visitedVertices.contains(edge.to)) continue;

                // 这个from实际上就是自己,把自己加入栈的目的是为了让程序能够回退到自己
                stack.push(edge.from);
                // 将这个没有遍历的顶点入队,并添加到已经访问的顶点集合(visitedVertices)中
                stack.push(edge.to);
                if (visitor.visit(edge.to.value)) return;
                visitedVertices.add(edge.to);

                // 遍历到一条边就退出循环,将这条边走到底,然后再从栈顶弹出一个元素继续遍历
                break;
            }
        }
    }

    /**
     * 递归方式实现
     *
     * @param begin 起点
     */
    public void depthFirstSearch2(V begin) {
        // 先判断入参所在的顶点是否存在,不存在就直接返回,不需要进行遍历
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (null == beginVertex) return;

        /**
         * 创建一个已经遍历过的顶点集合用来存储已经遍历过的顶点,避免重复遍历进入死循环
         */
        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        depthFirstSearch(beginVertex, visitedVertices);
    }

    private void depthFirstSearch(Vertex<V, E> vertex, Set<Vertex<V, E>> visitedVertices) {
        System.out.print(vertex.value + ", ");
        visitedVertices.add(vertex);

        // 遍历这个起点全部的边
        for (Edge<V, E> edge : vertex.outEdges) {
            // 如果这条边的终点已经遍历过,就跳过这个顶点
            if (visitedVertices.contains(edge.to)) continue;

            // 将这个没有遍历的顶点入队,并添加到已经访问的顶点集合(visitedVertices)中
            depthFirstSearch(edge.to, visitedVertices);
        }
    }

    @Override
    public void breadthFirstSearch(V begin, Visitor<V> visitor) {
        if (visitor == null) return;
        // 先判断入参所在的顶点是否存在,不存在就直接返回,不需要进行遍历
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (null == beginVertex) return;

        /**
         * 创建一个已经遍历过的顶点集合用来存储已经遍历过的顶点,避免重复遍历进入死循环
         */
        Set<Vertex<V, E>> visitedVertices = new HashSet<>();
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        queue.offer(beginVertex);
        visitedVertices.add(beginVertex);

        while (!queue.isEmpty()) {
            Vertex<V, E> vertex = queue.poll();
            if (visitor.visit(vertex.value)) return;

            // 遍历这个起点全部的边
            for (Edge<V, E> edge : vertex.outEdges) {
                // 如果这条边的终点已经遍历过,就跳过这个顶点
                if (visitedVertices.contains(edge.to)) continue;

                // 将这个没有遍历的顶点入队,并添加到已经访问的顶点集合(visitedVertices)中
                queue.offer(edge.to);
                visitedVertices.add(edge.to);
            }
        }
    }

    @Override
    public List<V> topologicalSort() {
        // 如果图中压根就没有顶点,就没有必要进行拓扑排序了
        if (vertices.size() == 0) return null;

        List<V> sortedList = new ArrayList<>();

        // 初始化队列以及所有顶点的入度Map
        Queue<Vertex<V, E>> queue = new LinkedList<>();
        Map<Vertex<V, E>, Integer> inMap = new HashMap<>();
        Iterator<Vertex<V, E>> vertexIterator = vertices.values().iterator();
        while (vertexIterator.hasNext()) {
            Vertex<V, E> vertex = vertexIterator.next();
            int inDegree = vertex.inEdges.size();
            if (inDegree == 0) {
                // 将入度为0的顶点放入队列,不为
                queue.offer(vertex);
            } else {
                // 将入度不为0的放入入度Map中储存起来
                inMap.put(vertex, inDegree);
            }
        }

        while (!queue.isEmpty()) {
            Vertex<V, E> ver = queue.poll();
            // 将队列中的顶点(能进入到队列中的顶点都是入度为0的顶点)加入到结果集中
            sortedList.add(ver.value);

            // 遍历这个顶点出去的边,找到边的终点,将它的入度减一
            for (Edge<V, E> edge : ver.outEdges) {
                // 拿到终点的度
                int degree = inMap.get(edge.to) - 1;
                if (degree == 0) {
                    // 入度等于0的时候就需要入队
                    queue.offer(edge.to);
                } else {
                    // 不为0的时候,更新inMap中的度
                    inMap.put(edge.to, degree);
                }
            }
        }

        return sortedList;
    }

    @Override
    public Set<EdgeInfo<V, E>> minSpanningTree() {
        // 如果图中压根就没有顶点,就没有必要生成最小树了
        if (vertices.size() == 0) return null;

        // 随机选择一个算法生成最小生成树
        return Math.random() > 0.5 ? prim() : kruskal();
    }

    /**
     * prim算法实现
     * 实现思路:
     * 随机选择一个顶点,从这个顶点开始横切,权重最小的那个边必然属于最小生成树
     *
     * @return
     */
    private Set<EdgeInfo<V, E>> prim() {
        Set<EdgeInfo<V, E>> result = new HashSet<>();
        Set<Vertex<V, E>> addedVertex = new HashSet<>();

        // 先随机从图中获取一个顶点,开始横切
        Iterator<Vertex<V, E>> vertexIterator = vertices.values().iterator();
        Vertex<V, E> vertex = vertexIterator.next();
        addedVertex.add(vertex);//记录这个顶点已经添加过了

        // 利用最小二叉堆从顶点的边中获取权重最小的边
        MinHeap<Edge<V, E>> minHeap = new MinHeap<>(vertex.outEdges, comparator);
        // 当堆为空或者已经添加的顶点数 == 这个图中的顶点数就退出循环
        while (!minHeap.isEmpty() && addedVertex.size() < vertices.size()) {
            // 从堆中拿到权重最小的边
            Edge<V, E> edge = minHeap.remove();
            // 如果在已添加的顶点集合中存在to这个顶点,说明这两个顶点间已经选择了权重最小的边
            if (addedVertex.contains(edge.to)) continue;
            // 记录这个顶点已经添加过了
            addedVertex.add(edge.to);

            // 将其加到最小生成树的集合
            result.add(edge.info());

            // 将to这个顶点的边全部添加到最小堆中,在下一次while循环的时候重新选择出最小的边
            minHeap.addAll(edge.to.outEdges);
        }

        return result;
    }


    /**
     * kruskal算法实现
     * 实现思路:
     * 对图中所有的边进行排序,按照从小到大的顺序依次添加到最小生成树中,但是添加到第三个顶点的时候,就需要
     * 注意了,要进行判断,对于即将添加进来的这个顶点是否会和已存在的顶点之间构成环,如果构成换就不能选择这条边,
     * 即使他是最小的权重
     * 利用并查集(UnionFind)来判断即将添加的顶点于与已存在的顶点是否构成环
     *
     * @return
     */
    private Set<EdgeInfo<V, E>> kruskal() {
        Set<EdgeInfo<V, E>> result = new HashSet<>();

        // 初始化并查集,将每一个顶点都初始化为一个单独的集合
        GenericUnionFind<Vertex<V, E>> unionFind = new GenericUnionFind<>();
        for (Vertex<V, E> vertex : vertices.values()) {
            unionFind.makeSet(vertex);
        }

        MinHeap<Edge<V, E>> minHeap = new MinHeap<>(edges, comparator);
        // 当最小堆为空 或 已经选择的边 < 顶点数量 - 1 时退出循环
        while (!minHeap.isEmpty() && result.size() < vertices.size() - 1) {
            Edge<V, E> edge = minHeap.remove();
            // 如果要添加的终点于现有的顶点会构成换,就跳过,不选择这条边
            if (unionFind.isSame(edge.from, edge.to)) continue;

            // 将最小的边添加到结果集中
            result.add(edge.info());

            // 如果不够成环,就将from和to合并成一个集合
            unionFind.union(edge.from, edge.to);
        }

        return result;
    }

    @Override
    public Map<V, E> shortestPath(V begin) {
        // 先判断入参所在的顶点是否存在,不存在就直接返回
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (null == beginVertex) return null;

        Map<V, E> shortestPath = new HashMap<>();

        // 初始化begin这个顶点的边信息
        Map<Vertex<V, E>, E> paths = new HashMap<>();
        for (Edge<V, E> edge : beginVertex.outEdges) {
            paths.put(edge.to, edge.weight);
        }

        while (!paths.isEmpty()) {
            // 找出最小权重的边,添加到最短路径集合中
            Map.Entry<Vertex<V, E>, E> minEntry = getMinPath(paths);

            // 即将离开桌面的那个顶点(权重最小的那个顶点也就是edge.to)
            Vertex<V, E> minVertex = minEntry.getKey();

            // 将这个顶点添加到最短路径集合中
            shortestPath.put(minVertex.value, minEntry.getValue());

            // 执行上述步骤,说明这个顶点已经被拎起来了,那么就将其从待选择路径集合中删除
            paths.remove(minVertex);

            // 对这个拎起来的顶点的边进行松弛操作
            for (Edge<V, E> edge : minVertex.outEdges) {
                // 如果这个边的顶点已经离开了桌面,就没有必要进行松弛操作
                if (shortestPath.containsKey(edge.to.value)) continue;

                // 计算新的权值 = begin -> edge.from的权值 + 现在这条边的权值
                E newWeight = (E) weightManager.add(minEntry.getValue(), edge.weight);

                // 从待选择的集合中拿到之前维护进去的权值(begin --> edge.to)
                E oldWeight = paths.get(edge.to);

                // 如果old权值不存在 or 新计算的权值小余以前的权值,那么就需要更新对应的权值
                if (oldWeight == null || weightManager.compare(newWeight, oldWeight) < 0) {
                    paths.put(edge.to, newWeight);
                }
            }
        }

        /**
         * 上面的for循环会对到起点的操作也会进行松弛操作,并添加到最短路径的集合中,而这
         * 个顶点的信息是不需要的,所以这里手动删除一下. 当然也可以在for循环下的第一个if判断后
         * 跟上判断( || begin.euqals(edge.to)),如果松弛的顶点是begin起点,就不需要进行
         * 松弛操作,直接跳过. 但是放到租后删除是一个优化,因为如果边很多,放在if里,每遍历一条边
         * 都需要进行一次判断,而到起点的边相对于边的总数来说肯定是少的,所以让他进行松弛操作,
         * 然后在返回之前,删除这个起点的最短路径即可
         */
        shortestPath.remove(begin);
        return shortestPath;
    }

    /**
     * 从路径集合中找出最小权值的路径
     *
     * @param paths 待离开桌面的路径集合
     * @return 最小权值的路径
     */
    private Map.Entry<Vertex<V, E>, E> getMinPath(Map<Vertex<V, E>, E> paths) {
        Iterator<Map.Entry<Vertex<V, E>, E>> iterator = paths.entrySet().iterator();
        Map.Entry<Vertex<V, E>, E> minEntry = iterator.next();
        while (iterator.hasNext()) {
            Map.Entry<Vertex<V, E>, E> entry = iterator.next();
            if (weightManager.compare(entry.getValue(), minEntry.getValue()) < 0) {
                minEntry = entry;
            }
        }
        return minEntry;
    }

    @Override
    public Map<V, PathInfo<V, E>> shortestPathInfo(V begin) {
        // 随机选择一个算法算出单源多点最短路径
        // return Math.random() > 0.5 ? dijkstra(begin) : bellmanFord(begin);
        return bellmanFord(begin);
    }

    /**
     * Bellman-Ford实现的 单源多点最短路径
     * <p>
     * 使用前提:  支持负权变,并且还能检测出负权环
     * <pre>
     *     实现思路:
     *     对所有的边进行vertices.size - 1次松弛操作
     * </pre>
     *
     * @param begin 起点
     * @return
     */
    private Map<V, PathInfo<V, E>> bellmanFord(V begin) {
        // 先判断入参所在的顶点是否存在,不存在就直接返回
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (null == beginVertex) return null;

        // 初始化begin这个顶点的边信息
        Map<V, PathInfo<V, E>> shortestPath = new HashMap<>();
        shortestPath.put(begin, new PathInfo<V, E>((E) weightManager.zero()));

        // 进行n-1次循环
        for (int i = 0; i < vertices.size() - 1; i++) {
            // 对每一条边进行松弛
            for (Edge<V, E> edge : edges) {
                // 拿到起点(begin)到这个边的from(edge.from)的距离
                PathInfo<V, E> fromPathInfo = shortestPath.get(edge.from.value);
                // 如果begin -> edge.from的最短路径信息不存在,说明这个点之前不存在直接达到的路径,故没有必要进行松弛操作
                if (null == fromPathInfo) continue;

                relaxForBellmanFord(shortestPath, edge, fromPathInfo);
            }
        }

        // 再来一次松弛,如果还能松弛成功,就说明存在负权环
        for (Edge<V, E> edge : edges) {
            // 拿到起点(begin)到这个边的from(edge.from)的距离
            PathInfo<V, E> fromPathInfo = shortestPath.get(edge.from.value);
            // 如果begin -> edge.from的最短路径信息不存在,说明这个点之前不存在直接达到的路径,故没有必要进行松弛操作
            if (null == fromPathInfo) continue;

            if (relaxForBellmanFord(shortestPath, edge, fromPathInfo)) {
                System.out.println("有负权环.");
                return null;
            }
        }

        // 删除起点
        shortestPath.remove(begin);
        return shortestPath;
    }

    /**
     * 松弛操作
     *
     * @param path     最短路径集合
     * @param edge     松弛的边
     * @param fromPath 到要松弛边的起点(begin -> edge.from)的路径信息
     * @return
     */
    private boolean relaxForBellmanFord(Map<V, PathInfo<V, E>> path, Edge<V, E> edge, PathInfo<V, E> fromPath) {
        E newWeight = (E) weightManager.add(fromPath.getWeight(), edge.weight);
        PathInfo<V, E> oldPath = path.get(edge.to.value);

        // 如果新路径的权值 > old路径的权值,就没必要进行松弛操作
        if (oldPath != null && weightManager.compare(newWeight, oldPath.getWeight()) >= 0) return false;

        if (null == oldPath) {
            // 如果odlPath==null,说明这个点目前还没有计算出最短路径,不可以直接到达
            oldPath = new PathInfo<>();
            path.put(edge.to.value, oldPath);
        } else {
            oldPath.getEdgeInfos().clear();
        }

        oldPath.setWeight(newWeight);
        oldPath.getEdgeInfos().addAll(fromPath.getEdgeInfos());
        oldPath.getEdgeInfos().add(edge.info());

        return true;
    }

    /**
     * dijkstra实现的 单源多点最短路径
     * <p>
     * 使用前提:  !!! 不能存在负权边 !!!
     * <pre>
     *     实现思路:
     *     模拟生活中的拎石头的例子,从起点出发,依次找出outEdges中最小的权值的边,然后进行松弛操作,从而找到最短路径
     * </pre>
     *
     * @param begin 起点
     * @return
     */
    private Map<V, PathInfo<V, E>> dijkstra(V begin) {
        // 先判断入参所在的顶点是否存在,不存在就直接返回
        Vertex<V, E> beginVertex = vertices.get(begin);
        if (null == beginVertex) return null;

        Map<V, PathInfo<V, E>> shortestPath = new HashMap<>();

        // 初始化begin这个顶点的边信息
        Map<Vertex<V, E>, PathInfo<V, E>> paths = new HashMap<>();
        paths.put(beginVertex, new PathInfo<V, E>((E) weightManager.zero()));

        /**
         * 注释下面一段代码,是以为实际上下面的初始化操作,实际上就是一次松弛操作,而松弛操作完全可以放在下面的while循环之中
         * 去实现,只需要在初始化数据的时候维护下begin -> begin存在一条权重为0的数据即可
         */
        // for (Edge<V, E> edge : beginVertex.outEdges) {
        //     PathInfo<V, E> pathInfo = new PathInfo<>();
        //     pathInfo.setWeight(edge.weight);
        //     pathInfo.getEdgeInfos().add(edge.info());
        //     paths.put(edge.to, pathInfo);
        // }

        while (!paths.isEmpty()) {
            // 找出最小权重的边,添加到最短路径集合中
            Map.Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = getMinPath2(paths);

            // 即将离开桌面的那个顶点(权重最小的那个顶点也就是edge.to)
            Vertex<V, E> minVertex = minEntry.getKey();

            // 将这个顶点添加到最短路径集合中
            shortestPath.put(minVertex.value, minEntry.getValue());

            // 执行上述步骤,说明这个顶点已经被拎起来了,那么就将其从待选择路径集合中删除
            paths.remove(minVertex);

            // 对这个拎起来的顶点的边进行松弛操作
            for (Edge<V, E> edge : minVertex.outEdges) {
                // 如果这个边的顶点已经离开了桌面,就没有必要进行松弛操作
                if (shortestPath.containsKey(edge.to.value)) continue;

                relaxForDijkstra(minEntry.getValue(), edge, paths);
            }
        }

        /**
         * 上面的for循环会对到起点的操作也会进行松弛操作,并添加到最短路径的集合中,而这
         * 个顶点的信息是不需要的,所以这里手动删除一下. 当然也可以在for循环下的第一个if判断后
         * 跟上判断( || begin.euqals(edge.to)),如果松弛的顶点是begin起点,就不需要进行
         * 松弛操作,直接跳过. 但是放到租后删除是一个优化,因为如果边很多,放在if里,每遍历一条边
         * 都需要进行一次判断,而到起点的边相对于边的总数来说肯定是少的,所以让他进行松弛操作,
         * 然后在返回之前,删除这个起点的最短路径即可
         */
        shortestPath.remove(begin);
        return shortestPath;
    }

    /**
     * 松弛操作
     *
     * @param fromPath 边的最短路径信息
     * @param edge     松弛的边
     * @param paths    待选择的点
     */
    private void relaxForDijkstra(PathInfo<V, E> fromPath, Edge<V, E> edge, Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        // 计算新的权值 = begin -> edge.from的权值 + 现在这条边的权值
        E newWeight = (E) weightManager.add(fromPath.getWeight(), edge.weight);

        // 从待选择的集合中拿到之前维护进去的权值(begin --> edge.to)
        PathInfo<V, E> oldPathInfo = paths.get(edge.to);

        // 如果old路径信息存在 且 新路径的权值 > old的权值,就跳过
        if (oldPathInfo != null && weightManager.compare(newWeight, oldPathInfo.getWeight()) > 0) return;

        // 如果不存在old路径信息,说明那当前这条路径就是最短路径
        if (oldPathInfo == null) {
            oldPathInfo = new PathInfo<>();

            // 不要忘记了,将选择的最短路径添加到paths集合中去
            paths.put(edge.to, oldPathInfo);
        } else {
            // 如果存在old路径,需要将old路径先√, 再将新的路径添加进去
            oldPathInfo.getEdgeInfos().clear();
        }

        oldPathInfo.setWeight(newWeight);
        oldPathInfo.getEdgeInfos().addAll(fromPath.getEdgeInfos());
        oldPathInfo.getEdgeInfos().add(edge.info());
    }

    /**
     * 从路径集合中找出最小权值的路径
     *
     * @param paths 待离开桌面的路径集合
     * @return 最小权值的路径
     */
    private Map.Entry<Vertex<V, E>, PathInfo<V, E>> getMinPath2(Map<Vertex<V, E>, PathInfo<V, E>> paths) {
        Iterator<Map.Entry<Vertex<V, E>, PathInfo<V, E>>> iterator = paths.entrySet().iterator();
        Map.Entry<Vertex<V, E>, PathInfo<V, E>> minEntry = iterator.next();
        while (iterator.hasNext()) {
            Map.Entry<Vertex<V, E>, PathInfo<V, E>> entry = iterator.next();
            if (weightManager.compare(entry.getValue().getWeight(), minEntry.getValue().getWeight()) < 0) {
                minEntry = entry;
            }
        }
        return minEntry;
    }

    @Override
    public Map<V, Map<V, PathInfo<V, E>>> shortestPathInfo() {
        Map<V, Map<V, PathInfo<V, E>>> pathInfos = new HashMap<>();

        // 初始化数据,将每一条边都构建到路径信息集合中去
        for (Edge<V, E> edge : edges) {
            Map<V, PathInfo<V, E>> map = pathInfos.get(edge.from.value);
            if (null == map) {
                map = new HashMap<>();
                pathInfos.put(edge.from.value, map);
            }

            // 将边的信息转换为路径信息,添加到路径集合中
            PathInfo<V, E> pathInfo = new PathInfo(edge.weight);
            pathInfo.getEdgeInfos().add(edge.info());
            map.put(edge.to.value, pathInfo);
        }

        vertices.forEach((V v2, Vertex<V, E> vertex1) -> {
            vertices.forEach((V v1, Vertex<V, E> vertex2) -> {
                vertices.forEach((V v3, Vertex<V, E> vertex3) -> {
                    // 在遍历的时候,是有可能遍历到的顶点是同一个顶点,如果是同一个顶点,就没有必要进行下述的松弛操作了
                    if (v1.equals(v2) || v1.equals(v3) || v2.equals(v3)) return;

                    //获取v1 -> v2的路径信息
                    PathInfo<V, E> path12 = getPathInfo(v1, v2, pathInfos);
                    if (null == path12) return;

                    //获取v2 -> v3的路径信息
                    PathInfo<V, E> path23 = getPathInfo(v2, v3, pathInfos);
                    if (null == path23) return;

                    //获取v1 -> v3的路径信息
                    PathInfo<V, E> path13 = getPathInfo(v1, v3, pathInfos);
                    E newWeight = (E) weightManager.add(path12.getWeight(), path23.getWeight());

                    // 如果新算出来的路径权值 > old路径的权值,就没有必要进行松弛操作
                    if (null != path13 && weightManager.compare(newWeight, path13.getWeight()) >= 0) return;

                    // 如果不存在old路径信息,说明那当前这条路径就是最短路径
                    if (path13 == null) {
                        path13 = new PathInfo<>();

                        // 不要忘记了,将选择的最短路径添加到paths集合中去
                        pathInfos.get(v1).put(v3, path13);
                    } else {
                        // 如果存在old路径,需要将old路径先清空, 再将新的路径添加进去
                        path13.getEdgeInfos().clear();
                    }

                    path13.setWeight(newWeight);
                    path13.getEdgeInfos().addAll(path12.getEdgeInfos());
                    path13.getEdgeInfos().addAll(path23.getEdgeInfos());
                });
            });
        });

        return pathInfos;
    }

    /**
     * 从pathInfos集合中获取路径信息
     *
     * @param from      起点
     * @param to        终点
     * @param pathInfos 路径信息集合
     * @return
     */
    private PathInfo<V, E> getPathInfo(V from, V to, Map<V, Map<V, PathInfo<V, E>>> pathInfos) {
        Map<V, PathInfo<V, E>> fromMap = pathInfos.get(from);
        return fromMap == null ? null : fromMap.get(to);
    }

    @Override
    public void print() {
        System.out.println("[顶点]-------------------");
        vertices.forEach((V v, Vertex<V, E> vertex) -> {
            System.out.println(v);
            System.out.println("out-----------");
            System.out.println(vertex.outEdges);
            System.out.println("in-----------");
            System.out.println(vertex.inEdges);
        });

        System.out.println("[边]-------------------");
        edges.forEach((Edge<V, E> edge) -> {
            System.out.println(edge);
        });
    }
}
