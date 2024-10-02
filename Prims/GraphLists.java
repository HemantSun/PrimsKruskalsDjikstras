// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }

    public boolean isEmpty() 
    {
        return N == 0;
    }

    public void siftUp( int k) 
    {
        int v = a[k];
        a[0] = 0; // initialize a[0] to be a sentinel
    
        while (dist[v] < dist[a[k / 2]]) 
        {
            a[k] = a[k / 2];
            hPos[a[k]] = k;
            k /= 2;
        }
        a[k] = v;
        hPos[v] = k;
    }

    public void siftDown( int k) 
    {
        int v, j;
       
        v = a[k];  

        while(k * 2 < N) 
        {
            j = k * 2;
            if(j < N && dist[ a[j] ] > dist[ a[j+1] ])
            {
                ++j; 
            }
            if(dist[v] <= dist[ a[j] ])
            {
                break;
            }
            hPos[ a[k] ] = j;
            a[k] = a[j];
            k = j;
        }
        hPos[v] = k;
        a[k] = v;
    }

    public void insert( int x) 
    {
        a[++N] = x;
        siftUp(N);
    }

    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N+1] = 0;  // put null node into empty spot
        
        a[1] = a[N--];
        siftDown(1);
        
        return v;
    }

}

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;
    
    // used for traversing graph
    private int[] visited;
    private int id;
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node newN;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
    
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        // create adjacency lists, initialised to sentinel node z       
        adj = new Node[V+1];        
        for(v = 1; v <= V; ++v)
            adj[v] = z;
        
        mst = new int[V+1];
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));   

            // write code to put edge into adjacency matrix
            newN = new Node();
            newN.vert = v;
            newN.wgt = wgt;
            newN.next = adj[u];
            adj[u] = newN;

            newN = new Node();
            newN.vert = u;
            newN.wgt = wgt;
            newN.next = adj[v];
            adj[v] = newN;
        }
    }
   
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
    
    // method to display the graph representation
    public void display() {
        int v;
        Node n;
        
        for(v=1; v<=V; ++v){
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    }

    public void MST_Prim(int s)
    {
        int v, u;
        int wgt, wgt_sum = 0;
        int[] dist, parent, hPos;
        Node t;
    
        // Initialize arrays for distances, parent nodes, and heap positions
        dist = new int[V + 1];
        parent = new int[V + 1];
        hPos = new int[V + 1];
    
        // Initialize distances to infinity and parent nodes to -1
        for (v = 1; v <= V; v++) {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }
    
        // Initialize distance of starting vertex to 0
        dist[s] = 0;
        mst[s] = s;
    
        // Create a heap with maximum size V and insert the starting vertex
        Heap h = new Heap(V, dist, hPos);
        h.insert(s);
    
        // Main loop of Prim's algorithm
        while (!h.isEmpty()) {
            // Remove vertex with minimum distance from heap
            v = h.remove();
            // Update the total weight of MST
            dist[v] = -dist[v];

            if (parent[v] != 0) {
                System.out.println("Vertex " + toChar(v) + " is connected to Vertex " + toChar(parent[v]) + " with edge weight = " + (-dist[v]));
                wgt_sum += dist[v];
            } else {
                System.out.println("Starting vertex: " + toChar(v));
            }
            
            mst[v] = parent[v];

            // Iterate over all adjacent vertices of v
            for (t = adj[v]; t != z; t = t.next) {
                u = t.vert;
                wgt = t.wgt;
    
                // If u is in the heap and the weight of edge (v, u) is less than
                // the current distance of u, update distance and parent
                if (wgt < dist[u]) {
                    dist[u] = wgt;
                    parent[u] = v;
                    if (hPos[u] == 0) {
                        h.insert(u);
                    } else {
                        h.siftUp(hPos[u]);
                    }
                }
                // If u is not in the heap, insert it; otherwise, adjust its position in the heap
            }
        }

        // Display the total weight of MST
        System.out.print("\n\nWeight of MST = " + -wgt_sum + "\n");

        showMST();
    }
    
    //Prints MST to screen
    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            for(int v = 1; v <= V; ++v)
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
            System.out.println("");
    }

    //DepthFirst Algorithm
    public void DF(int s) 
    {
        id = 0;
        visited = new int[V+1];
        System.out.println("");

        for(int j = 1; j<=V; j++)
        {
            visited[j] = 0;
        }
        dfVisit(0, s);
    }

    //How DFS is done
    private void dfVisit(int prev, int v)
    {
        Node n = adj[v];
        visited[v] = id++;
        System.out.println("Visiting Vertex [" + toChar(v) + "] from Vertex [" + toChar(prev) + "]");
        while(n != z)
        {
            if(visited[n.vert]==0)
            {
                dfVisit(v, n.vert);
            }
            n = n.next;
        }
    }

    //BreadthFirst Algorithm
    public void breadthFirst(int s) {
        Queue<Integer> q = new LinkedList<>();
        int[] parent = new int[V + 1];
        System.out.println();
    
        visited = new int[V + 1];
        visited[s] = 1;
        parent[s] = -1;
        q.add(s);
    
        while (!q.isEmpty()) {
            int v = q.poll();
            if (parent[v] != -1) {
                System.out.println("Visiting Vertex [" + toChar(v) + "] from Vertex [" + toChar(parent[v]) + "]");
            } else {
                System.out.println("Visiting Vertex [" + toChar(v) + "] (Starting vertex)");
            }
    
            for (Node n = adj[v]; n != z; n = n.next) {
                int u = n.vert;
                if (visited[u] == 0) {
                    q.add(u);
                    visited[u] = 1;
                    parent[u] = v;
                }
            }
        }
    }
    
    //Dijkstras Algorithm
    public void SPT_Dijkstra(int s) {
        int v, u, wgt;
        int[] dist, parent, hPos;
        Node c;

        dist = new int[V+1];
        parent = new int[V+1];
        hPos = new int[V+1];

        Heap pq = new Heap(V, dist, hPos);

        for(v = 1; v <= V; v++)
        {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        dist[s] = 0;

        pq.insert(s);

        while(!pq.isEmpty())
        {
            v = pq.remove();

            for (c = adj[v]; c != z; c = c.next)
            {
                u = c.vert;
                wgt = c.wgt;

                if (dist[v] + wgt < dist[u]) {
                    dist[u] = dist[v] + wgt;
                    parent[u] = v;

                    // Update the priority queue with the new distance
                    if (hPos[u] == 0) {
                        pq.insert(u);
                    } else {
                        pq.siftUp(hPos[u]);
                    }
                }
            }  
        }

        // Display the shortest path tree
        System.out.println("\nShortest Path Tree:");
        for (v = 1; v <= V; v++) {
            if (parent[v] != -1) {
                System.out.println("Vertex " + toChar(v) + " is connected to Vertex " + toChar(parent[v]) + " with edge weight = " + dist[v]);
            } else {
                System.out.println("Vertex " + toChar(v) + " is unreachable");
            }
        }
    }
}

//Main function which calls ALgorithm Methods, Reads in Graph, and chooses starting vertex
public class GraphLists {
    public static void main(String[] args) throws IOException
    {
        String fname;
        Scanner scanned = new Scanner(System.in);

        System.out.print("\nInput name of file with graph definition: ");
        fname = scanned.nextLine();

        System.out.print("\nEnter the vertex you want to start at (I.E A = 1, B = 2 etc): ");
        int s = scanned.nextInt();
        
        Graph g = new Graph(fname);
       
        g.display();

        System.out.print("\nDFS using Recursion:");

        g.DF(s);

        System.out.print("\nBFS using a queue:");

        g.breadthFirst(s);
        
        System.out.print("\nPrims Algorithm:\n");

        g.MST_Prim(s);

        System.out.print("\nDijkstras Algorithm:\n");

        g.SPT_Dijkstra(s);
        
        scanned.close();       
    }
}
