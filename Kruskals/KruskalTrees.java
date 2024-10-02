// Kruskal's Minimum Spanning Tree Algorithm
// Union-find implemented using union by rank, and path compression

import java.io.*;
import java.util.Scanner;
 
class Edge {
    public int u, v, wgt;

    public Edge() {
        u = 0;
        v = 0;
        wgt = 0;
    }

    public Edge( int x, int y, int w) {
        // missing lines
        u = x;
        v = y;
        wgt = w;
    }
    
    public void show() {
        System.out.print("Edge " + toChar(u) + "--" + wgt + "--" + toChar(v) + "\n") ;
    }
    
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
}

//Heap class with siftdown and remove implementations
class Heap
{
	private int[] h;
    int N, Nmax;
    Edge[] edge;


    public Heap(int _N, Edge[] _edge) {
        int i;
        Nmax = N = _N;
        h = new int[N+1];
        edge = _edge;
       
        // initially just fill heap array with 
        // indices of edge[] array.
        for (i = 0; i <= N; ++i) 
            h[i] = i;
           
        // Then convert h[] into a heap
        // from the bottom up.
        for (i = N / 2; i > 0; --i)
            siftDown(i);
    }
    
    private void siftDown(int k) {
        int e, j;
    
        e = h[k];
        while (k <= N / 2) {
            j = k + k;
            if (j < N && edge[h[j]].wgt > edge[h[j + 1]].wgt)
                j++;
            if (edge[e].wgt <= edge[h[j]].wgt)
                break;
            h[k] = h[j];
            k = j;
        }
        h[k] = e;
    }

    public int remove() {
        h[0] = h[1];
        h[1] = h[N--];
        siftDown(1);
        return h[0];
    }
}

/****************************************************
*
*       UnionFind partition to support union-find operations
*       Implemented simply using Discrete Set Trees
*
*****************************************************/

class UnionFindSets {
    private int[] treeParent;
    private int[] rank;
    private int N;

    public UnionFindSets(int V) {
        N = V;
        treeParent = new int[V + 1];
        rank = new int[V + 1];
        for (int i = 1; i <= V; i++) {
            treeParent[i] = i;
            rank[i] = 0;
        }
    }

    public int findSet(int vertex) {
        if (vertex != treeParent[vertex]) {
            // Path compression
            treeParent[vertex] = findSet(treeParent[vertex]);
        }
        return treeParent[vertex];
    }

    public void union(int set1, int set2) {
        int root1 = findSet(set1);
        int root2 = findSet(set2);

        // Union by rank
        if (rank[root1] < rank[root2]) {
            treeParent[root1] = root2;
        } else if (rank[root1] > rank[root2]) {
            treeParent[root2] = root1;
        } else {
            treeParent[root2] = root1;
            rank[root1]++;
        }
    }

    public void showTrees() {
        int i;
        for (i = 1; i <= N; ++i)
            System.out.print(toChar(i) + "->" + toChar(treeParent[i]) + "  ");
        System.out.print("\n");
    }

    public void showSets() {
        int u, root;
        int[] shown = new int[N + 1];
        for (u = 1; u <= N; ++u) {
            root = findSet(u);
            if (shown[root] != 1) {
                showSet(root);
                shown[root] = 1;
            }
        }
        System.out.print("\n");
    }

    private void showSet(int root) {
        int v;
        System.out.print("Set{");
        for (v = 1; v <= N; ++v)
            if (findSet(v) == root)
                System.out.print(toChar(v) + " ");
        System.out.print("}  ");
    }

    private char toChar(int u) {
        return (char) (u + 64);
    }
}

//initializes graph and reads in edges and vertexs
class Graph 
{ 
    private int V, E;
    private Edge[] edge;
    private Edge[] mst;        

    public Graph(String graphFile) throws IOException
    {
        int u, v;
        int w, e;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create edge array
        edge = new Edge[E+1];   
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            w = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + w + ")--" + toChar(v));                         
             
            // create Edge object
            Edge edges = new Edge(u, v, w);
			edge[e] = edges;
        }
    }

/**********************************************************
*
*       Kruskal's minimum spanning tree algorithm
*
**********************************************************/
public Edge[] MST_Kruskal() 
{
    int ei, i = 0;
    Edge e;
    int uSet, vSet;
    UnionFindSets partition;
    
    // create edge array to store MST
    // Initially it has no edges.
    mst = new Edge[V-1];

    // priority queue for indices of array of edges
    Heap h = new Heap(E, edge);

    // create partition of singleton sets for the vertices
    partition = new UnionFindSets(V);

    // Keep selecting edges until MST is formed or all edges are processed
    for (int temp = 0; temp < E; ++temp) {
        ei = h.remove(); // Get the index of the edge with the minimum weight
        e = edge[ei]; // Get the edge itself

        // Find the set representatives of the vertices of the edge
        uSet = partition.findSet(e.u);
        vSet = partition.findSet(e.v);

        // If the edge doesn't form a cycle, add it to the MST
        if (uSet != vSet) {
            mst[i++] = e;
            partition.union(uSet, vSet);

            partition.showSets();
            partition.showTrees();
        }
    }
    
    return mst;
}

    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }

    public void showMST()
    {
        System.out.print("\nMinimum spanning tree build from following edges:\n");
        for(int e = 0; e < V-1; ++e) {
            mst[e].show(); 
        }
        System.out.println();
    }

} // end of Graph class
    
// test code
class KruskalTrees {
    public static void main(String[] args) throws IOException
    {
        String fname;
        Scanner scanned = new Scanner(System.in);

        System.out.print("\nInput name of file with graph definition: ");
        fname = scanned.nextLine();

        Graph g = new Graph(fname);

        System.out.println();

        g.MST_Kruskal();

        g.showMST();

        scanned.close();
    }
}    


