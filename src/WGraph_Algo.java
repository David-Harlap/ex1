package ex1.src;

import java.io.*;
import java.util.*;

public class WGraph_Algo implements weighted_graph_algorithms ,java.io.Serializable {

    private weighted_graph _wGraph;
    private PriorityQueue<node_info> _queue;
    private Comparator<node_info> compereNode = new CompereNode();
    private HashMap<Integer, Integer> _parentHash;
    private int saveStatus_NodeStart;
    private int saveStatus_MC;
    private int saveStatus_connectCount;
    private boolean alreadyRun = false;

    private static class CompereNode implements Comparator<node_info> {

    public CompereNode(){}
    @Override
    public int compare(node_info o1, node_info o2) {
        int ans = 0;
        double tag1 = o1.getTag();
        double tag2 = o2.getTag();
        if(tag1<0 || tag2<0)
            throw new RuntimeException("ERR: the node can not add to the queue without change is default tag");
        if(tag1<tag2) {ans = -1;}
        if(tag1>tag2) {ans = 1;}
        return ans;
    }
    }

    public WGraph_Algo() {
        _wGraph = new WGraph_DS();
    }

    public WGraph_Algo(weighted_graph g) {
        _wGraph = g;
    }


    @Override
    public void init(weighted_graph g) {
        _queue = new PriorityQueue<node_info>(compereNode);
        _wGraph = g;
        _parentHash = new HashMap<>();
    }

    @Override
    public weighted_graph getGraph() {
        return _wGraph;
    }

    @Override
    public weighted_graph copy() {
        return new WGraph_DS(this._wGraph);
    }

    @Override
    public boolean isConnected() {
        if((_wGraph.getV().isEmpty() ) || (_wGraph.getV().size() == 1))
            return true;
        int sumConnect , temp = _wGraph.getV().iterator().next().getKey();
        if ((alreadyRun) && (saveStatus_MC==_wGraph.getMC()))
            sumConnect = saveStatus_connectCount;
        else
            sumConnect = dijkstra(temp);
        return (sumConnect == _wGraph.nodeSize());

    }

    @Override
    public double shortestPathDist(int src, int dest) {
        if(((!_wGraph.getV().contains(_wGraph.getNode(src))) ||
                (!_wGraph.getV().contains(_wGraph.getNode(dest)))))
                    return -1;
        if (src==dest) {return 0;}
        if ((saveStatus_NodeStart != src) || (saveStatus_MC!=_wGraph.getMC()))
            dijkstra(src);
        return _wGraph.getNode(dest).getTag();
    }

    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(shortestPathDist(src,dest)== -1)
            return null;
        Stack<node_info> path = new Stack<>();
        path.push(_wGraph.getNode(dest));
        int i = _parentHash.get(dest);

        while((_parentHash.get(i) != null))
        {
            path.push(_wGraph.getNode(i));
            i = _parentHash.get(i);
        }
        path.push(_wGraph.getNode(src));
        // change the stack to link list.
        LinkedList<node_info> shortPath = new LinkedList<>();
        int temp = path.size();
        for (int j = 0; j < temp ; j++) {
            shortPath.add(path.pop());
        }
        return shortPath;
    }


    @Override
    public boolean save(String file) {
        String filename = file +".ser";

        try
        {
            //Saving of object in a file
            FileOutputStream file1 = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream(file1);

            // Method for serialization of object
            out.writeObject(this.getGraph());

            out.close();
            file1.close();

            System.out.println("Object has been serialized");
            return  true;
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught");
        }
        return false;
    }

    @Override
    public boolean load(String file) {
        try
        {
            // Reading the object from a file
            FileInputStream file1 = new FileInputStream(file);
            ObjectInputStream in = new ObjectInputStream(file1);

            // Method for deserialization of object
            this._wGraph = (weighted_graph)in.readObject();

            in.close();
            file1.close();
/*
            System.out.println("Object has been deserialized ");
            System.out.println("a = " + object1.a);
            System.out.println("b = " + object1.b);

 */
            return true;
        }

        catch(IOException ex)
        {
            System.out.println("IOException is caught");
            return false;
        }

        catch(ClassNotFoundException ex)
        {
            System.out.println("ClassNotFoundException is caught");
            return false;
        }
    }

    private void reset(){
        for (node_info i : _wGraph.getV()) {
            i.setTag(-1.0);
        }
        _parentHash.clear();
    }

    /**
     *
     * @param start
     * @return
     */
    private int  dijkstra(int start){
        reset();
        int countConnected = 1;
        node_info node = _wGraph.getNode(start);
        _wGraph.getNode(start).setTag(0);
        _queue.add(node);
        _parentHash.put(start, null);

        while (!_queue.isEmpty()) {
            node = _queue.poll();
            for (node_info i : _wGraph.getV(node.getKey())) {
                double temp = node.getTag()+ _wGraph.getEdge(node.getKey(), i.getKey());
                if ((i.getTag() == -1.0))
                    countConnected++;
                    if ((i.getTag() == -1.0) || (temp < i.getTag())){
                        i.setTag(temp);
                        _queue.add(i);
                        _parentHash.put(i.getKey(), node.getKey());
                }
            }
        }
        alreadyRun = true;
        saveStatus_MC= _wGraph.getMC();
        saveStatus_NodeStart= start;
        saveStatus_connectCount = countConnected;
        return countConnected;
    }
/*
    private double  dijkstra(int start, int end){
        reset();
        node_info node = _wGraph.getNode(start);
        _wGraph.getNode(start).setTag(0);
        _queue.add(node);
        _parentHash.put(start, null);

        while (!_queue.isEmpty()) {
            node = _queue.poll();
            if(node == _wGraph.getNode(end))
                break;

            for (node_info i : _wGraph.getV(node.getKey())) {
                double temp = node.getTag()+ _wGraph.getEdge(node.getKey(), i.getKey());
                if ((i.getTag() == -1.0)|| (temp < i.getTag())){
                    i.setTag(temp);
                    _queue.add(i);
                    _parentHash.put(i.getKey(), node.getKey());
                }
            }
        }
        return node.getTag();

    }
*/
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return Objects.equals(_wGraph, that._wGraph);
    }


}

