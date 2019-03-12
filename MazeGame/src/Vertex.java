
// Assignment 10
// Al-Saleh, Abdullah
// aalsaleh
// Wu, Guanting
// wukyle
// Zhen, Jialin
// jialinzhen

import java.awt.Color;
import java.util.ArrayList;
import java.util.Objects;

//import tester.*;
import javalib.impworld.*;
//import java.awt.Color;
import javalib.worldimages.*;

class Vertex {
  // The position of the vertex. Also used as unique identifier
  Posn loc;

  // The edges from this vertex
  ArrayList<Edge> outEdges;
  
  // Neighboring nodes
  Vertex top;
  Vertex bottom;
  Vertex left;
  Vertex right;

  // is the node blocked from a cardinal direction
  boolean blockedLeft;
  boolean blockedRight;
  boolean blockedTop;
  boolean blockedBottom;

  // has the player visited this node
  boolean hasVisited;

  // is the player at this node
  boolean playerAtVertex;

  // Constructor
  Vertex(int x, int y) {
    this.loc = new Posn(x, y);
    this.outEdges = new ArrayList<Edge>();

    this.blockedBottom = true;
    this.blockedLeft = true;
    this.blockedRight = true;
    this.blockedTop = true;
    
    this.top = this;
    this.right = this;
    this.left = this;
    this.bottom = this;

    this.hasVisited = false;

    this.playerAtVertex = false;
  }

  // is this vertex at the same positon as the given vertex
  public boolean equals(Object other) {
    if (other instanceof Vertex) {
      Vertex given = (Vertex) other;
      return this.loc.x == given.loc.x && this.loc.y == given.loc.y;
    }
    else {
      return false;
    }
  }

  // overwritten hashcode function
  public int hashCode() {
    return Objects.hash(this.loc.x, this.loc.y);
  }

  // adds an endge to outEdges
  void addEdge(Vertex to, int weight) {
    outEdges.add(new Edge(this, to, weight));
    // if (to.loc.x >= 0 && this.loc.y >= 0) {
    // outEdges.add(new Edge(this, to, weight));
    // }
  }

  // gets the edge that connects the node to its top neighbor
  Edge topEdge() {
    if (this.loc.y > 0) {
      return this.outEdges.get(0);
    }
    else {
      return null;
    }
  }

  // gets the edge that connects the node to its left neighbor
  Edge leftEdge() {
    if (this.loc.x > 0) {
      return this.outEdges.get(this.outEdges.size() - 1);
    }
    else {
      return null;
    }
  }

  // returns the image for each node
  WorldImage nodeImage() {
    WorldImage canvas = new RectangleImage(MazeGameWorld.CELL_SIZE, MazeGameWorld.CELL_SIZE,
        OutlineMode.SOLID, Color.GRAY);

    if (this.hasVisited) {
      canvas = new RectangleImage(MazeGameWorld.CELL_SIZE, MazeGameWorld.CELL_SIZE,
          OutlineMode.SOLID, Color.BLUE);
    }
    if (this.loc.x == 0 && this.loc.y == 0) {
      canvas = new RectangleImage(MazeGameWorld.CELL_SIZE, MazeGameWorld.CELL_SIZE,
          OutlineMode.SOLID, Color.GREEN);
    }
    if (this.loc.x == MazeGameWorld.MAZE_WIDTH - 1 && this.loc.y == MazeGameWorld.MAZE_HEIGHT - 1) {
      canvas = new RectangleImage(MazeGameWorld.CELL_SIZE, MazeGameWorld.CELL_SIZE,
          OutlineMode.SOLID, Color.PINK);
    }
    if (this.playerAtVertex) {
      canvas = new RectangleImage(MazeGameWorld.CELL_SIZE, MazeGameWorld.CELL_SIZE,
          OutlineMode.SOLID, Color.RED);
    }

    return canvas;
  }

  // draws the nodes
  WorldScene drawNode(WorldScene canvas) {
    canvas.placeImageXY(this.nodeImage(),
        this.loc.x * MazeGameWorld.CELL_SIZE + MazeGameWorld.CELL_SIZE / 2 + 2,
        this.loc.y * MazeGameWorld.CELL_SIZE + MazeGameWorld.CELL_SIZE / 2 + 2);
    return canvas;
  }
}
