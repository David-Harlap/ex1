package ex1.src;

import java.io.*;
import java.util.*;

/**
 * This class represents the "regular" Graph Theory algorithms.
 * this class implements weighted_graph_algorithms.
 * each WGraph_Algo has Wgraph that all the algorithm base on it.
 * each WGraph_Algo has {@link HashMap} that help us save the prev node in the short path.
 * each WGraph_Algo has {@link PriorityQueue} that help us to find the short path,
 * and also comparator that help us ad to the PriorityQueue.
 * saveStatus_connectCount,saveStatus_MC,saveStatus_NodeStart and alreadyRun They are values that used to save time
 *      Use them in case that the graph does not change then we will not have to run everything again.
 *      Will be explained more later.
 *
 * the class has:
 * 0. clone(); (copy)
 * 1. init(graph);
 * 2. isConnected();
 * 3. double shortestPathDist(int src, int dest);
 * 4. List<node_data> shortestPath(int src, int dest);
 * 5. Save(file);
 * 6. Load(file);
 */
public class WGraph_Algo implements weighted_graph_algorithms ,java.io.Serializable {

    private weighted_graph _wGraph;
    private PriorityQueue<node_info> _queue;
    private Comparator<node_info> compereNode = new CompereNode();
    private HashMap<Integer, Integer> _parentHash;
    private int saveStatus_NodeStart;
    private int saveStatus_MC;
    private int saveStatus_connectCount;
    private boolean alreadyRun = false;

    /**
     *In order for the priority queue to know what to put before what I had to implements comparator.
     */
    private static class CompereNode implements Comparator<node_info> {
        /**
         * Constructor to the comparator class that call {@link CompereNode}
         */
    public CompereNode(){}

        /**
         * override of the method compere that help us to add node to the queue.
         * @param o1 - first object from type node_info.
         * @param o2 - second object from type node_info
         * @return 0 if they equals, -1 if o1 bigger, and 1 if o2 bigger.
         * @throws RuntimeException to get inside the queue the node must to set is tag but if it doesn't happened it throw RuntimeException.
         */
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

    /**
     * Constructor
     * Creates an empty Graph_Algo.
     */
    public WGraph_Algo() {
        _wGraph = new WGraph_DS();
    }

    /**
     * Constructor
     * Creates an WGraph_Algo that the graph Initialized to the graph g.
     */
    public WGraph_Algo(weighted_graph g) {
        _wGraph = g;
    }

    /**
     * A method that initializes the graph
     * Always come before starting the algorithm
     * @param g the graph we want to init.
     */
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

    /**
     * making deep copy of this graph
     * @return the graph we want to copy.
     */
    @Override
    public weighted_graph copy() {
        return new WGraph_DS(this._wGraph);
    }

    /**
     * The method that check if all the graph is Connected.
     * the method used the method dijkstra.
     * the method check If there is a change in the graph since the last time they run dijkstra.
     *      if there really is no change then the method displays the relevant information from the previous run.
     *      When it comes to a graph of millions of vertices and tens of millions of edges it is very significant.
     * @return true if connect; false if not.
     */
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
    /**
     *the method get 2 node and return the weight-distance between the 2 node.
     * the method use solve(src) that scan the graph and set the 'tag' in each node to be the distance from src.
     * In the last stage the 'tag' value of dest will be the distance between the nodes.
     * if the nodes not connect the scan of solve will not arrive to dest and the 'tag' of dest will stay the Default -1.
     * @param src - start node
     * @param dest - end (target) node
     * @return - the number of node that take us arrive from src to end. return -1 if the path is not exist.
     */
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
    /**
     * the method get 2 nodes and returns the the shortest path between src to dest - as an ordered List of nodes:
     * src --  n1 --  n2 -- ...dest
     *
     * i choose  to implementaion with stack i push in the stack the dest node and after that push the prev node.
     * I do it with the help of the HashMap _parentHash that give me direct access to the prev node.
     * in the end i push the src node and finish to push all the path.
     *
     * I return linklist so the last level i need to add the node to the linklist
     * it easy because it already arranged as required because in stack it order LIFO (last in first out)

     * @param src - start node
     * @param dest - end (target) node
     * @return list that represent the path.
     */
    @Override
    public List<node_info> shortestPath(int src, int dest) {
        if(shortestPathDist(src,dest)== -1) //run dijkstra and check if the nodes are connect.
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

    /**
     * Saves this weighted (undirected) graph to the given file name.
     * i used {@link Serializable} to save and load.
     * i copy the code from   -  https://www.geeksforgeeks.org/serialization-in-java/
     * @param file - the file name (may include a relative path).
     * @return true - iff the file was successfully saved
     */
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

    /**
     * This method load a graph to this graph algorithm.
     * if the file was successfully loaded - the underlying graph
     * of this class will be changed (to the loaded one), in case the
     * graph was not loaded the original graph should remain "as is".
     *
     * @param file - file name
     * @return true - iff the graph was successfully loaded.
     */
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
    /**
     * private method that reset all the node tag to the default value -1.
     * and clear the Hashmap that help us save the prev node in the short path.
     * we used this method before we used the method dijkstra.
     */
    private void reset(){
        for (node_info i : _wGraph.getV()) {
            i.setTag(-1.0);
        }
        _parentHash.clear();
    }

    /**
     * this is the  private method that do all the algorithm to find the shortest path.
     * it start from the node 'start' and scan all the graph.
     * the method use the parameter 'tag' that contain in each node and mark the distance from the node to the start node.
     * the method use {@link PriorityQueue} and use the {@link CompereNode} to make sure that the node with the smallest weight (from the start node)
     *      is the one that will continue the algorithm and this is how we know every time that if a certain vertex is out of the queue
     *      it is necessarily with the shortest path so far.
     * the method save the prev node of each node in HashMap we will use it in the method 'shortestPath'.
     * the method count the number of the node that connect and return that, it will help us to in 'isConnected()'.
     * the metoud save the status of the graph So that in the future if not the graph will not drink then they will be able to save the unnecessary running of the method.
     *      it save the status in few parameter -
     *      saveStatus_connectCount - save the number of the connected node in this scan.
     *      saveStatus_MC - save the modecount of the graph in the  scan time.
     *      saveStatus_NodeStart - save the key of the start node. because if it start in diffrent node it Irrelevant any more.
     *      alreadyRun - save true brcause the method already run.
     *
     *
     * @param start - the node that start the scan.
     * @return the number of node are connected.
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

    /**
     * i override equals for the test class to check my project.
     * @param o
     * @return true if they equals and false if doesnt.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WGraph_Algo that = (WGraph_Algo) o;
        return Objects.equals(_wGraph, that._wGraph);
    }


}

