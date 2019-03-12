
// Assignment 10
// Al-Saleh, Abdullah
// aalsaleh
// Wu, Guanting
// wukyle
// Zhen, Jialin
// jialinzhen

import java.util.ArrayList;
//import tester.*;
//import javalib.impworld.*;
//import java.awt.Color;
//import javalib.worldimages.*;

// represents a graph
class Graph {
  // a list of all the vertices in the graph
  ArrayList<Vertex> allVertices;

  // Constructor
  Graph(ArrayList<Vertex> allVertices) {
    this.allVertices = allVertices;
  }

  // adds vertex to the graph;
  void addVertex(int x, int y) {
    this.allVertices.add(new Vertex(x, y));
  }

  // returns the size of the graph, mainly for testing purposes
  int size() {
    return this.allVertices.size();
  }

  // returns the vertex at the given index, mostly for testing purposes
  Vertex get(int index) {
    return this.allVertices.get(index);
  }
}
