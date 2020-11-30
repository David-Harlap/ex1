# ex1 - Weighted Graph
Assignments 1 in OOP course in Ariel University. 

The Assignments was to make undirectional weighted graph.  My graph should support a large number of nodes (over 10^6, with average degree of 10).
It was given us 3 interfaces and we needed to write 2 class Wgraph and WGraph_Algo.


### Wgraph  class 
 This graph has private inner class 'NodeInfo' that represent node in the graph. each node has unique key, tag and info (Srting that can represent something). 
 
  in the graph we have some methods like Adds and deletes vertices from the graph.
                                         Connects vertices. And more ..
  
  ### WGraph_Algo
  the ALGO class Represents the algorithms that can be made using a weighted graph.
                 
 Some of the methods in the class are:
 Make a deep copy between graphs, check if the graph is connected, and most importantly look for the shortest way between different vertices.
 
 The algorithm I used is dijkstra, you can see a short explanation of it in the "wiki" tab.
 
 In addition, you can save and load graphs through the class.
 I did it with the help of implements Serializable

I used 2 websites to write the project:

https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm

https://www.geeksforgeeks.org/serialization-in-java/
