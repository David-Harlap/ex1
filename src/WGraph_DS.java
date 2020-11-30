package ex1.src;



import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;


/**
 *The class WGraph_DS represents an unidirectional weighted graph.
 * each graph has Hashmap that contains all vertices in the graph.
 * each graph has Hashmap that contains another HashMap that presents for each node key it represent Hash map that contain the key of the neibor and the weight of the edge.
 * each graph has _edgeCount that presents the number of edge in the graph.
 * and also _modeCount that that presents the number of change that made in the graph..
 *
 */
public class WGraph_DS implements weighted_graph, java.io.Serializable {

    private int _modeCount=0;
    private HashMap<Integer, node_info> _vertex;
    private HashMap<Integer, HashMap<Integer,Double>> _edgeMap;
    private static int _edgeCount=0;


    /**
     * WGraph_DS has private class NodeInfo that represents an node in an unadjusted and weighted graph.
     *
     * Each node has its own key so it appears as FINAL.
     * Each node has a Tag value that is used by us when we want to find the shortest path.
     * Each node has info that you can write comments on each vertex.
     */
    private static class NodeInfo implements node_info , java.io.Serializable  {

        private final int _key;
        private double _tag = -1.0;
        private String _info="";

        /**
         * Creates an empty node with unique key.
         */

        public NodeInfo(int key){
            _key=key;
        }

        /**
         * copy Constructors
         * Used to make a deep copy of graph.
         * this constructors copy only the key number.
         *
         * @param node - key node
         */
        public NodeInfo(node_info node) {
            _key = node.getKey();

        }

        /**
         * @return the key (id) associated with this node.
         */
        @Override
        public int getKey() {
            return _key;
        }

        /**
         * This method return the remark (meta data) associated with this node.
         *
         * @return -the info include the node.
         */
        @Override
        public String getInfo() {
            return _info;
        }

        /**
         * Allows changing the remark (meta data) associated with this node.
         *
         * @param s - Represents the new Info
         */
        @Override
        public void setInfo(String s) {
            _info = s;

        }

        /**
         * This method return the tag value of this node.
         *
         * @return - the tag value of this node.
         */
        @Override
        public double getTag() {
            return _tag;
        }

        /**
         * Allow setting the "tag" value for temporal marking an node.
         *
         * @param t - the new value of the tag
         */
        @Override
        public void setTag(double t) {
            _tag = t;

        }
        ////////////////////////////////////////////////////////

        /**
         * i choose to override equals for the test class to check my project.
         * @param o
         * @return
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NodeInfo nodeInfo = (NodeInfo) o;
            return _key == nodeInfo._key &&
                    Double.compare(nodeInfo._tag, _tag) == 0 &&
                    Objects.equals(_info, nodeInfo._info);
        }

    }
    /** Constructor
     * Creates an empty graph.
     */
    public WGraph_DS() {
        _vertex = new HashMap<Integer, node_info>();
        _edgeMap  = new HashMap<Integer, HashMap<Integer, Double>>();
       _edgeCount = 0;
      // _modeCount=0;
    }
    /**
     * copy Constructors
     * this constructor copy all the node in the graph.with the help of copy Constructor of NodeInfo.
     * and after that the Constructor connect all the edge with they weight just like the original graph.
     * the _modeCount count one after one and can be diffrent from the original graph.
     * @param g - the graph we want to copy
     */
    public WGraph_DS(weighted_graph g) {
        _vertex = new HashMap<Integer, node_info>();
        _edgeMap=new HashMap<Integer, HashMap<Integer, Double>>();
        _edgeCount = 0;
        for (node_info i: g.getV()) {
            addNode(i.getKey());
        }

        for (node_info i : g.getV()) {
            int iKey = i.getKey();
            for (node_info j: g.getV(iKey)){
                int jKey = j.getKey();
                connect(iKey, jKey,getEdge(iKey,jKey));
            }
        }
    }

    /**
     *The method receive key and return the pointer of the relevant node by the receive key.
     * @param key - the node_id
     * @return pointer of the relevant node
     */
    @Override
    public node_info getNode(int key) {
        return _vertex.get(key);
    }
    /**
     * The method receive 2 key and check if they connect.
     * @param node1 - first key of node
     * @param node2 - second key of node
     * @return true if them connect and false if doesn't.
     */
    @Override
    public boolean hasEdge(int node1, int node2) {
        if (!_vertex.containsKey((node1)) || !(_vertex.containsKey(node2))|| (!_edgeMap.containsKey(node1)))
            return false;
        return _edgeMap.get(node1).containsKey(node2);

    }

    /**
     *  * The method receive 2 key check if they connect and return the weight of the edge.
     * @param node1 - first key of node
     * @param node2 - second key of node
     * @return the weight of the edge.
     */
    @Override
    public double getEdge(int node1, int node2) {
        if(!hasEdge(node1,node2))
            return -1;
        return _edgeMap.get(node1).get(node2);
    }
    /**
     * The method add the the key node to the _vertex.
     * At the same time it also inserts it into _edgeMap even though no other node is connected to it yet.
     * @param key - the node that we want to add.
     */
    @Override
    public void addNode(int key) {
        if(!_vertex.containsKey(key)) {
            _vertex.put(key, new NodeInfo(key));
            _edgeMap.put(key,new HashMap<Integer, Double>());
            _modeCount++;
        }
    }

    /**
     * The method receive 2 key and the weight between them and connect them each other.
     * if the edge already exsist the method replace the weight.
     * if the edge exsist Just like the original edge (including the weight) so the method will do nothing.
     * @param node1
     * @param node2
     * @param w
     */
    @Override
    public void connect(int node1, int node2, double w) {
        if((_vertex.containsKey(node1)) && (_vertex.containsKey(node2)) && (node1 != node2)){
            boolean tmp =hasEdge(node1,node2);
            if (tmp && _edgeMap.get(node1).get(node2) == w) {
                return;
            }
            if (!tmp) {
                _edgeCount++;
            }
            _edgeMap.get(node1).put(node2,w);
            _edgeMap.get(node2).put(node1,w);
           _modeCount++;
        }
    }
    /**
     * The method return collection of all the vertices in ths graph.
     *  i used The method values() of HashMap because it cost O(1).
     * @return collection that represents all the vertices in the graph
     */
    @Override
    public Collection<node_info> getV() {
        return _vertex.values();
    }

    /**
     * The method return collection that represent all the neighbor of the node 'key' in ths graph.
     *  *  i used The method  of HashMap
     * @param node_id - the node that i want to get is neighbors
     * @return - collection that represent the neighbor of the node 'key'
     */
    @Override
    public Collection<node_info> getV(int node_id) {
        LinkedList<node_info> ansList = new LinkedList<node_info>();
        if(_edgeMap.containsKey(node_id)) {
            for (Integer i : _edgeMap.get(node_id).keySet()) {
                ansList.add(_vertex.get(i));
            }
            return ansList;
        }
        return ansList;

    }
    /**
     * The method remove the node 'key' from the graph.
     * and also remove all the edge that was connect to 'key' node.
     * @param key - the id of the node we want to remove
     * @return the node_data that was remove
     */
    @Override
    public node_info removeNode(int key) {
         if(!_vertex.containsKey(key)) {
            return null;
        }
        for (Integer i : _edgeMap.get(key).keySet()) {
            _edgeMap.get(i).remove(key);
            _edgeCount--;
        }
        _modeCount++;
        _edgeMap.remove(key);
        return _vertex.remove(key);

    }
    /**
     * the method get 2 node check if both node are contain and if they are connect.
     * and if all true remove the edge between the node;
     * @param node1 first node
     * @param node2 second node
     */
    @Override
    public void removeEdge(int node1, int node2) {
        if(hasEdge(node1,node2)) {
            _edgeMap.get(node1).remove(node2);
            _edgeMap.get(node2).remove(node1);
            _edgeCount--;
            _modeCount++;
        }
    }
    /**
     *
     * @return the number of vertices (nodes) in the graph.
     */
    @Override
    public int nodeSize() {
        return _vertex.size();
    }

    /**
     *
     * @return the number of edges (unidirectional graph).
     */
    @Override
    public int edgeSize() {
        return _edgeCount;
    }

    /**
     *
     * @return the Mode Count - for testing changes in the graph.
     */
    @Override
    public int getMC() {
        return _modeCount;
    }


    /**
     * i override equals for the test class to check my project.
     * @param o
     * @return true if they equals and false if doesnt.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return Objects.equals(_vertex, wGraph_ds._vertex) &&
                Objects.equals(_edgeMap, wGraph_ds._edgeMap);
    }

}
