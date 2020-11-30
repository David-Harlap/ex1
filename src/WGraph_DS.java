package ex1.src;



import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;


public class WGraph_DS implements weighted_graph, java.io.Serializable {

    private int _modeCount=0;
    private HashMap<Integer, node_info> _vertex;
    private HashMap<Integer, HashMap<Integer,Double>> _edgeMap;
    private static int _edgeCount=0;



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
         * this constructors copy only the key number. and do not copy the neighbor collection
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

    public WGraph_DS() {
        _vertex = new HashMap<Integer, node_info>();
        _edgeMap  = new HashMap<Integer, HashMap<Integer, Double>>();
       _edgeCount = 0;
      // _modeCount=0;
    }


    public WGraph_DS(weighted_graph g) {
        _vertex = new HashMap<Integer, node_info>();
        _edgeMap=new HashMap<Integer, HashMap<Integer, Double>>();
        _edgeCount = 0;
     //   _modeCount=0;
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
      //  _modeCount = g.getMC();//????????????? needed??
    }


    @Override
    public node_info getNode(int key) {
        return _vertex.get(key);
    }

    @Override
    public boolean hasEdge(int node1, int node2) {
        if (!_vertex.containsKey((node1)) || !(_vertex.containsKey(node2))|| (!_edgeMap.containsKey(node1)))
            return false;
        return _edgeMap.get(node1).containsKey(node2);

    }

    @Override
    public double getEdge(int node1, int node2) {
        if(!hasEdge(node1,node2))
            return -1;
        return _edgeMap.get(node1).get(node2);
    }

    @Override
    public void addNode(int key) {
        if(!_vertex.containsKey(key)) {
            _vertex.put(key, new NodeInfo(key));
            _edgeMap.put(key,new HashMap<Integer, Double>());
            _modeCount++;
        }
    }

    /**
     * the method used put that add new pair to adj and if it wxsist put replace the value w.
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

    @Override
    public Collection<node_info> getV() {
        return _vertex.values();
    }

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

    @Override
    public void removeEdge(int node1, int node2) {
        if(hasEdge(node1,node2)) {
            _edgeMap.get(node1).remove(node2);
            _edgeMap.get(node2).remove(node1);
            _edgeCount--;
            _modeCount++;
        }
    }

    @Override
    public int nodeSize() {
        return _vertex.size();
    }

    @Override
    public int edgeSize() {
        return _edgeCount;
    }

    @Override
    public int getMC() {
        return _modeCount;
    }
    public HashMap<Integer, HashMap<Integer,Double>> getHash(){
        return _edgeMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_DS wGraph_ds = (WGraph_DS) o;
        return Objects.equals(_vertex, wGraph_ds._vertex) &&
                Objects.equals(_edgeMap, wGraph_ds._edgeMap);
    }

}
