
// Assignment 10
// Al-Saleh, Abdullah
// aalsaleh
// Wu, Guanting
// wukyle
// Zhen, Jialin
// jialinzhen

//import java.util.ArrayList;
//import tester.*;

import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// represents an edge in the graph
class Edge implements Comparable<Edge> {
  // the vertex this edge extends from
  Vertex from;
  // the vertex this edge extends to
  Vertex to;
  // the weight of this edge
  int weight;

  // Constructor
  Edge(Vertex from, Vertex to, int weight) {
    this.from = from;
    this.to = to;
    this.weight = weight;
  }

  // Overwritten compareTo method from Comparable
  // returns the difference of the weight in this edge and given edge
  public int compareTo(Edge o) {
    return this.weight - o.weight;
  }

  // returns image for a horizontal edge
  LineImage horizontalLineImage() {
    return new LineImage(new Posn(0, MazeGameWorld.CELL_SIZE), Color.BLACK);
  }

  // returns image for a vertical edge
  LineImage verticalLineImage() {
    return new LineImage(new Posn(MazeGameWorld.CELL_SIZE, 0), Color.BLACK);
  }

  // draws the horizontal edge
  WorldScene drawHorizontal(WorldScene canvas) {
    canvas.placeImageXY(this.horizontalLineImage(), this.from.loc.x * MazeGameWorld.CELL_SIZE,
        this.from.loc.y * MazeGameWorld.CELL_SIZE + (MazeGameWorld.CELL_SIZE / 2) + 1);
    return canvas;
  }

  // draws the vertical edge
  WorldScene drawVertical(WorldScene canvas) {
    canvas.placeImageXY(this.verticalLineImage(),
        (this.from.loc.x * MazeGameWorld.CELL_SIZE) + (MazeGameWorld.CELL_SIZE / 2) + 1,
        this.from.loc.y * MazeGameWorld.CELL_SIZE);
    return canvas;
  }

}
