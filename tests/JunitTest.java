package ex1.tests;



import ex1.src.*;
import org.junit.jupiter.api.Test;



import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class JunitTest {

    private static Random _rnd = null;

    /**
     *
     */
    @Test
    void bigGraph(){
        weighted_graph g1 = new WGraph_DS();
        for (int i = 0; i <1000000 ; i++) {
            g1.addNode(i);
        }
        assertEquals(1000000,g1.nodeSize());



    //    long startTime = System.nanoTime();
    //    graph_creator(1000000,10000000,1);
    //    System.out.println("it take: "+ ((System.nanoTime()-startTime)/1000000000.0)+ " secoend");
    }

    /**
     *
     */
    @Test
    void nodeTest(){
        weighted_graph g1 = new WGraph_DS();
        weighted_graph_algorithms ga1 = new WGraph_Algo();
        ga1.init(g1);
        g1.addNode(0);
        g1.getNode(0).setTag(22);
        g1.addNode(0);

        assertEquals(1, g1.nodeSize()); // check
        assertEquals(22 ,g1.getNode(0).getTag());// check if new node override node with same key.
        assertTrue(ga1.isConnected()); // graph with 1 node always need to be connected.
    }

    @Test
    void hasEdge(){
        weighted_graph g2 = new WGraph_DS();
        g2.addNode(0);
        g2.addNode(1);
        g2.addNode(2);
        g2.addNode(3);
        g2.connect(0,1,1.1);
        g2.connect(0,2,1.1);
        g2.connect(0,3,1.1);
        assertEquals(7,g2.getMC()); // check getMC()
        g2.connect(0,1,1.1);

        assertEquals(7,g2.getMC()); // the mode count dont change if the edge exsist.
        assertFalse(g2.hasEdge(0,0));//     There is no edge between a vertex and itself and therefore should be false.
        assertFalse(g2.hasEdge(0,5)); // Should be false because node number 5 does not exist*/
        assertEquals(1.1,g2.getEdge(0,1));// check getEDGE().*/

        g2.connect(0,1,2.2);
        assertEquals(2.2,g2.getEdge(0,1)); // check if connect override currect the whight.*/
    }

    @Test
    void remove(){
        weighted_graph g3 = new WGraph_DS();
        g3.addNode(0);
        g3.addNode(1);
        g3.addNode(2);
        g3.addNode(3);
        g3.connect(0,1,1);
        g3.connect(0,2,1.1);
        g3.connect(0,3,1.2);

        g3.removeEdge(1,1);
        assertEquals(3,g3.edgeSize()); //the edge doesnt exsist. should do nothing.
        g3.removeEdge(0,3);
        assertEquals(2,g3.edgeSize()); // check if removeEdge work.
        g3.connect(0,3,0.1);

        //remove node
        g3.removeNode(5);
        assertEquals(3,g3.edgeSize()); // check if remove not exsust node change.
        assertEquals(9,g3.getMC());
        g3.removeNode(0);
        assertEquals(0, g3.edgeSize()); //
    }

    @Test
    void getV(){
        weighted_graph g4 = new WGraph_DS();
        weighted_graph_algorithms ga4 = new WGraph_Algo();
        ga4.init(g4);
        assertEquals(0,g4.getV().size());// G4 isempty graph so the suze of getV should be 0;
        g4.addNode(0);
        g4.addNode(1);
        g4.connect(0,1,1.1);
        assertEquals(0,g4.getV(3).size());// node 3 is doesnt exsist in G4 so the size of getV(3) should be 0;
        assertEquals(1,g4.getV(0).size());

    }

    /////////////////// algo \\\\\\\\\\\\\\\\\\\\\\\
    @Test
    void startAlgo(){
        weighted_graph g = new WGraph_DS();
        weighted_graph_algorithms ga = new WGraph_Algo();
        assertEquals(ga.getGraph(),g);
        ga.init(g);
        g.addNode(0);
        assertEquals(ga.getGraph(),g); // check if the ga Automatically syncs

        // copy check
        weighted_graph copyG = new WGraph_DS();
        copyG = ga.copy();
        assertEquals(ga.getGraph(),copyG);

        copyG.addNode(2);
        assertNotEquals(ga.getGraph(),copyG);
        g.addNode(2);
       // assertNotEquals(g,copyG);//Although both graphs have 2 node having the same key still the graphs themselves are not equal.

        weighted_graph g1 = new WGraph_DS();
        weighted_graph_algorithms ga1 = new WGraph_Algo();
        ga1.init(g1);
        g1.addNode(1);
        g1.addNode(2);
        weighted_graph copyG1 = ga1.copy();

        assertFalse(ga1.isConnected());
        g1.addNode(3);
        assertEquals(3,g1.getV().size());
        assertEquals(2,copyG1.getV().size());

        ga1.getGraph().removeNode(1); //g1 change but copy dont change.
        assertEquals(2,g1.getV().size());
        assertEquals(2,copyG1.getV().size());

        copyG1.addNode(4);//copy chaage
        assertEquals(3,copyG1.getV().size());
        assertEquals(2,g1.getV().size());


    }

    @Test
    void isConnect(){
        weighted_graph g = new WGraph_DS();
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g);
        assertTrue(ga.isConnected()); // EMPTY graph shuold be connect
        g.addNode(0);
        assertTrue(ga.isConnected()); // one node graph need to be connect
        g.addNode(1);
        assertFalse(ga.isConnected()); // 2 node 0 edge need to be not connect.
        g.addNode(2);
        g.addNode(3);
        g.addNode(4);
        g.addNode(5);
        g.addNode(6);
        g.connect(0,1,0.2);
        g.connect(0,2,0.2);
        g.connect(0,3,0.2);
        g.connect(0,4,0.2);
        g.connect(0,5,0.2);
        g.connect(0,6,0.2);
        assertTrue(ga.isConnected()); // all node connect to node 0 so the graph is connect.
        ga.getGraph().removeEdge(0,1);
        assertFalse(ga.getGraph().hasEdge(0,1));
        // This line and the line below check that there is no difference whether the graph is reached via getGraph or via g directly.
        assertFalse(g.hasEdge(0,1));
        g.removeNode(0);
        assertFalse(ga.isConnected());


        weighted_graph g1 = new WGraph_DS();
        weighted_graph_algorithms ga1 = new WGraph_Algo();
        ga1.init(g1);
        for (int i = 0; i <10 ; i++) {
            g1.addNode(i);
        }
        g1.connect(0,1,1);
        g1.connect(2,1,1);
        g1.connect(2,3,1);
        g1.connect(3,4,1);

        g1.connect(4,5,1);
        g1.connect(5,6,1);

        g1.connect(6,7,1);
        g1.connect(8,1,1);

        assertFalse(ga1.isConnected()); // node 9 not conect.
        g1.connect(9,5,1);
        assertTrue(ga1.isConnected()); // connect node 9.
    }

    @Test
    void ShortPath() {
        weighted_graph g = new WGraph_DS();
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g);

        assertEquals(-1, ga.shortestPathDist(0, 0)); //empty graph return -1
        for (int i = 0; i < 10; i++) {
            g.addNode(i);
        }
        assertEquals(0, ga.shortestPathDist(0, 0));
        g.connect(0, 1, 1);
        g.connect(2, 1, 1);
        g.connect(2, 3, 1);
        g.connect(3, 4, 1);
        assertEquals(4, ga.shortestPathDist(0, 4));
        StringBuilder st = new StringBuilder();
        for (node_info i : ga.shortestPath(0, 4)) {
            String temp = String.valueOf(i.getKey());
            st.append(temp);
        }
        assertEquals("01234", st.toString()); // the only way in the graph is 0 >1>2>3>4
    }


    @Test
    void short_1(){
    weighted_graph g = new WGraph_DS();
    weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g);
        for (int i = 0; i < 10; i++) {
            g.addNode(i);
        }

        g.connect(0,1,1);
        g.connect(2,1,1);
        g.connect(2,3,1);
        g.connect(3,4,1);
        g.connect(0,5,0.5);
        g.connect(1,5,0.1);
        g.connect(5,6,4);
        g.connect(2,6,1);
        g.connect(5,9,2);
        g.connect(9,7,1);
         assertEquals(1.1,ga.shortestPathDist(5,2));
         assertEquals(3.1,ga.shortestPathDist(9,2));
         assertEquals(-1,ga.shortestPathDist(5,8));
    }

    @Test
    void save_load(){
        weighted_graph g = graph_creator(10,30,1);
        weighted_graph_algorithms ga = new WGraph_Algo();
        ga.init(g);
        String str = "myGraph_ex1";
        ga.save(str);

        weighted_graph_algorithms ga1 = new WGraph_Algo();
        if (ga1.load(str))
                 assertEquals(ga.getGraph(),ga1.getGraph());


    }








    public static weighted_graph graph_creator(int v_size, int e_size, int seed) {
        weighted_graph g = new WGraph_DS();
        _rnd = new Random(seed);
        for (int i = 0; i < v_size; i++) {
            g.addNode(i);
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while (g.edgeSize() < e_size) {
            int a = nextRnd(0, v_size);
            int b = nextRnd(0, v_size);
            int i = nodes[a];
            int j = nodes[b];
            double w = _rnd.nextDouble();
            g.connect(i, j, w);
        }
        return g;
    }

    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }

    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_info> V = g.getV();
        node_info[] nodes = new node_info[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }
    }
