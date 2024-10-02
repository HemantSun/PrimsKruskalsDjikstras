// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.Scanner;
import java.util.Queue;
import java.util.LinkedList;

//Removed heap, mst, spt code etc for the Matrix code.

class GraphMtx {
    private int V, E;
    private int[][] adjMatrix; // adjacency matrix
    private int[] visited;

    public GraphMtx(String graphFile) throws IOException {
        FileReader fr = new FileReader(graphFile);
        BufferedReader reader = new BufferedReader(fr);

        String splits = " +";
        String line = reader.readLine();
        String[] parts = line.split(splits);

        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);

        adjMatrix = new int[V + 1][V + 1];
        
        for (int i = 1; i <= V; i++) {
            for (int j = 1; j <= V; j++) {
                adjMatrix[i][j] = 0;
            }
        }

        for (int e = 1; e <= E; ++e) {
            line = reader.readLine();
            parts = line.split(splits);
            int u = Integer.parseInt(parts[0]);
            int v = Integer.parseInt(parts[1]);
            int wgt = Integer.parseInt(parts[2]);

            adjMatrix[u][v] = wgt;
            adjMatrix[v][u] = wgt;
        }
    }

    // convert vertex into char for pretty printing
    private char toChar(int u) {  
        return (char)(u + 64);
    }

    public void display() {
        System.out.println();
        System.out.println("Adjacency Matrix:");
        for (int i = 1; i <= V; i++) {
            for (int j = 1; j <= V; j++) {
                System.out.print(adjMatrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void DF(int prev, int s) {
        System.out.println();
        visited = new int[V + 1];
        dfVisit(prev, s);
        System.out.println();
    }

    private void dfVisit(int prev, int v) {
        visited[v] = 1;
        System.out.println("Visiting node [" + toChar(v) + "] from node [" + toChar(prev) + "]");

        for (int i = 1; i <= V; i++) {
            if (adjMatrix[v][i] != 0 && visited[i] == 0) {
                dfVisit(v, i);
            }
        }
    }

    public void breadthFirst(int s) {
        visited = new int[V + 1];
        Queue<Integer> queue = new LinkedList<>();
        System.out.println();

        visited[s] = 1;
        queue.add(s);

        while (!queue.isEmpty()) {
            int v = queue.poll();
            System.out.println("Currently visiting [" + toChar(v) + "]");

            for (int i = 1; i <= V; i++) {
                if (adjMatrix[v][i] != 0 && visited[i] == 0) {
                    visited[i] = 1;
                    queue.add(i);
                }
            }
        }

        System.out.println();
    }
}

public class GraphMtxMain {
    public static void main(String[] args) throws IOException {
        String fname;
        Scanner scanned = new Scanner(System.in);

        System.out.print("\nEnter File name of Graph - Contains Vertices and Edges: ");
        fname = scanned.nextLine();

        System.out.print("\nEnter the vertex you want to start at (I.E A = 1, B = 2 etc): ");
        int s = scanned.nextInt();

        GraphMtx g = new GraphMtx(fname);

        g.display();

        System.out.print("\nDFS using Recursion (Matrix Representation):");

        g.DF(0, s);

        System.out.print("\nBFS using a queue (Matrix Representation):");

        g.breadthFirst(s);

        scanned.close();
    }
}
