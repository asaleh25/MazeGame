
// Assignment 10
// Al-Saleh, Abdullah
// aalsaleh
// Wu, Guanting
// wukyle
// Zhen, Jialin
// jialinzhen

import java.util.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

// represents the maze world
class MazeGameWorld extends World {

  static final int CELL_SIZE = 15;
  static final int MAZE_HEIGHT = 60;
  static final int MAZE_WIDTH = 100;
  // static final int WINDOW_WIDTH = MAZE_HEIGHT * CELL_SIZE;
  // static final int WINDOW_HEIGHT = MAZE_WIDTH * CELL_SIZE;

  // the height of the maze
  int height;
  // the width of the maze
  int width;

  // graph representing the initial maze
  Graph maze;

  // Random object
  Random rand = new Random();
  // maximum weight of an edge
  int maxWeight;
  // number of steps taken to solve maze
  int score;

  boolean enableBFS;
  boolean enableDFS;
  boolean isSolved;

  // All the edges of the graph
  ArrayList<Edge> workList;
  // All the edges in the minimum spanning tree
  ArrayList<Edge> edgesInTree = new ArrayList<Edge>();
  // All the edges not in the minimum spanning tree
  ArrayList<Edge> edgesNotInTree = new ArrayList<Edge>();

  // Hashmap of vertices and their representatives
  HashMap<Vertex, Vertex> representatives = new HashMap<Vertex, Vertex>();

  // workList for a depth-first search
  Stack<Vertex> dfsWorkList = new Stack<Vertex>();
  // workList for a breadth-first search
  Queue<Vertex> bfsWorkList = new LinkedList<Vertex>();
  // records the edge of the graphthat was to get from an already visited node
  HashMap<Vertex, Vertex> cameFromEdge = new HashMap<Vertex, Vertex>();

  // the player
  Player p;

  MazeGameWorld() {
    this.height = MAZE_HEIGHT;
    this.width = MAZE_WIDTH;

    this.init();
  }

  MazeGameWorld(int height, int width) {
    this.height = height;
    this.width = width;

    this.init();
  }

  // initializes necessary fields
  void init() {
    this.maxWeight = 1000;
    this.maze = new Graph(makeGraph());
    this.makeEdges();
    this.workList = createWorkList(this.maze);
    this.buildTree(this.representatives);
    this.dfsWorkList.push(maze.get(0));
    this.bfsWorkList.add(maze.get(0));
    this.cameFromEdge.clear();
    this.enableBFS = false;
    this.enableDFS = false;
    this.isSolved = false;
    this.p = new Player(this.maze.get(0));
    this.maze.get(0).playerAtVertex = true;
    this.maze.get(0).hasVisited = true;
  }

  // resets the world
  void resetWorld() {
    this.workList = new ArrayList<Edge>();
    this.edgesInTree = new ArrayList<Edge>();
    this.edgesNotInTree = new ArrayList<Edge>();
    this.representatives = new HashMap<Vertex, Vertex>();
    this.dfsWorkList = new Stack<Vertex>();
    this.bfsWorkList = new LinkedList<Vertex>();

    this.init();
  }

  // resets the world for the auto-solver
  void resetToSolve() {
    this.bfsWorkList = new LinkedList<Vertex>();
    this.dfsWorkList = new Stack<Vertex>();
    this.cameFromEdge.clear();

    for (Vertex v : this.maze.allVertices) {
      v.hasVisited = false;
      v.playerAtVertex = false;
    }

    this.p = new Player(this.maze.get(0));
    this.maze.get(0).playerAtVertex = true;
    this.maze.get(0).hasVisited = true;

    this.enableBFS = false;
    this.enableDFS = false;
    this.isSolved = false;

    this.score = 0;

    this.dfsWorkList.push(this.maze.get(0));
    this.bfsWorkList.add(this.maze.get(0));
  }

  // creates the initial graph representing the maze
  ArrayList<Vertex> makeGraph() {
    ArrayList<Vertex> temp = new ArrayList<Vertex>();
    for (int j = 0; j < this.height; j++) {
      for (int i = 0; i < this.width; i++) {
        temp.add(new Vertex(i, j));
      }
    }
    return temp;
  }

  // maps the location of the given vertex to its index in allVertices
  int vertexMap(Vertex given) {
    // return (given.loc.x + given.loc.y) + (given.loc.y * (this.width - 1));
    return given.loc.y * this.width + given.loc.x;
  }

  // creates an edge between vertex and its four neighbors
  void addNeighbors(Vertex given) {
    int vertexIndex = this.vertexMap(given);
    if (given.loc.y > 0) {
      int top = vertexIndex - this.width;
      given.addEdge(this.maze.get(top), rand.nextInt(maxWeight));
      given.top = this.maze.get(top);
    }
    if (given.loc.y < this.height - 1) {
      int bottom = vertexIndex + this.width;
      given.addEdge(this.maze.get(bottom), rand.nextInt(maxWeight));
      given.bottom = this.maze.get(bottom);
    }
    if (given.loc.x < this.width - 1) {
      int right = vertexIndex + 1;
      given.addEdge(this.maze.get(right), rand.nextInt(maxWeight));
      given.right = this.maze.get(right);
    }
    if (given.loc.x > 0) {
      int left = vertexIndex - 1;
      given.addEdge(this.maze.get(left), rand.nextInt(maxWeight));
      given.left = this.maze.get(left);
    }
  }

  // creates the edges of the graph
  void makeEdges() {
    for (Vertex curr : this.maze.allVertices) {
      this.addNeighbors(curr);
    }
  }

  // creates the workList
  ArrayList<Edge> createWorkList(Graph maze) {
    ArrayList<Edge> edges = new ArrayList<Edge>();
    for (Vertex currVert : maze.allVertices) {
      for (Edge currEdge : currVert.outEdges) {
        if (currVert.left == currEdge.to) {
          edges.add(currEdge);
        }
        if (currVert.top == currEdge.to) {
          edges.add(currEdge);
        }
      }
    }

    Collections.sort(edges);
    return edges;
  }

  // finds the vertex's representative based on key
  Vertex find(HashMap<Vertex, Vertex> rep, Vertex key) {
    if (rep.get(key).equals(key)) {
      return key;
    }
    else {
      return find(rep, rep.get(key));
    }
  }

  // sets representative
  void union(HashMap<Vertex, Vertex> rep, Vertex key, Vertex value) {
    rep.put(key, value);
  }

  // builds the minimum spanning tree using Kruskal's
  void buildTree(HashMap<Vertex, Vertex> rep) {
    // initialize every vertex's representative to itself
    for (int i = 0; i < this.maze.allVertices.size(); i++) {
      rep.put(maze.get(i), maze.get(i));
    }

    int i = 0;
    while (this.edgesInTree.size() < this.maze.allVertices.size() - 1) {
      Edge currentEdge = this.workList.get(i);
      // Edge inverseEdge = findInverse(this.workList, currentEdge);

      Vertex a = this.find(rep, currentEdge.from);
      Vertex b = this.find(rep, currentEdge.to);
      Vertex c = currentEdge.from;
      Vertex d = currentEdge.to;

      // if vertices are not already connected
      if (a.equals(b)) {
        this.edgesNotInTree.add(currentEdge);
        // this.edgesInTree.add(currentEdge);
        // this.edgesInTree.add(inverseEdge);
        // this.union(rep, this.find(rep, a), this.find(rep, b));
      }
      else {
        this.edgesInTree.add(currentEdge);
        this.union(rep, this.find(rep, a), this.find(rep, b));
        if (c.equals(d.bottom)) {
          c.blockedTop = false;
          d.blockedBottom = false;
        }
        if (c.equals(d.top)) {
          c.blockedBottom = false;
          d.blockedTop = false;
        }
        if (c.equals(d.right)) {
          c.blockedLeft = false;
          d.blockedRight = false;
        }
        if (c.equals(d.left)) {
          c.blockedRight = false;
          d.blockedLeft = false;
        }
        // this.edgesNotInTree.add(currentEdge);
        // this.edgesNotInTree.add(inverseEdge);
      }
      i++;
    }

    // add remaining edges to edges not in tree
    for (int j = i; j < this.workList.size(); j++) {
      this.edgesNotInTree.add(this.workList.get(j));
    }
  }

  // Calculates the window width
  int windowWidth() {
    return this.width * CELL_SIZE;
  }

  // Calculates the window height
  int windowHeight() {
    return this.height * CELL_SIZE;
  }

  // draw the maze
  public WorldScene makeScene() {
    WorldScene canvas = new WorldScene(this.windowWidth(), this.windowHeight());

    // background
    canvas.placeImageXY(
        new RectangleImage(this.windowWidth(), this.windowHeight(), OutlineMode.SOLID, Color.GRAY),
        this.windowWidth() / 2, this.windowHeight() / 2);

    // draw the vertices
    for (Vertex currV : this.maze.allVertices) {
      currV.drawNode(canvas);
    }

    // draw the edges
    for (Edge currE : this.edgesNotInTree) {
      if (currE.to.loc.x < currE.from.loc.x) {
        currE.drawHorizontal(canvas);
      }
      else {
        currE.drawVertical(canvas);
      }
    }
    // Edge top = currV.topEdge();
    // Edge left = currV.leftEdge();
    // if (edgesNotInTree.contains(top)) {
    // top.drawVertical(canvas);
    // }
    // if (edgesNotInTree.contains(left)) {
    // left.drawHorizontal(canvas);
    // }
    return canvas;
  }

  // draw the solution
  void reconstruct(HashMap<Vertex, Vertex> cameFromEdge, Vertex next) {
    isSolved = true;
    next.playerAtVertex = true;
    Vertex current = cameFromEdge.get(next);

    if (current.equals(maze.get(0))) {
      next.playerAtVertex = true;
    }
    else {
      reconstruct(cameFromEdge, current);
    }

  }

  // depth-first search
  void depthFirstSearch() {
    this.enableDFS = true;
    this.enableBFS = false;

    Vertex next = this.dfsWorkList.pop();

    if (cameFromEdge.containsValue(next)) {
      // do nothing
    }
    else if (next.equals(this.maze.get(this.maze.size() - 1))) {
      this.reconstruct(cameFromEdge, next);
    }
    else {
      this.score++;
      next.hasVisited = true;
      if (!next.blockedLeft && !next.left.hasVisited) {
        this.dfsWorkList.push(next.left);
        cameFromEdge.put(next.left, next);
      }
      if (!next.blockedRight && !next.right.hasVisited) {
        this.dfsWorkList.push(next.right);
        cameFromEdge.put(next.right, next);
      }
      if (!next.blockedTop && !next.top.hasVisited) {
        this.dfsWorkList.push(next.top);
        cameFromEdge.put(next.top, next);
      }
      if (!next.blockedBottom && !next.bottom.hasVisited) {
        this.dfsWorkList.push(next.bottom);
        cameFromEdge.put(next.bottom, next);
      }
    }
  }

  // breadth-first search
  void breadthFirstSearch() {
    this.enableBFS = true;
    this.enableDFS = false;

    Vertex next = this.bfsWorkList.remove();

    if (cameFromEdge.containsValue(next)) {
      // do nothing
    }
    else if (next.equals(this.maze.get(this.maze.size() - 1))) {
      this.reconstruct(cameFromEdge, next);
    }
    else {
      this.score++;
      next.hasVisited = true;
      if (!next.blockedLeft && !next.left.hasVisited) {
        this.bfsWorkList.add(next.left);
        cameFromEdge.put(next.left, next);
      }
      if (!next.blockedRight && !next.right.hasVisited) {
        this.bfsWorkList.add(next.right);
        cameFromEdge.put(next.right, next);
      }
      if (!next.blockedTop && !next.top.hasVisited) {
        this.bfsWorkList.add(next.top);
        cameFromEdge.put(next.top, next);
      }
      if (!next.blockedBottom && !next.bottom.hasVisited) {
        this.bfsWorkList.add(next.bottom);
        cameFromEdge.put(next.bottom, next);
      }
    }
  }

  public void onKeyEvent(String k) {
    if (k.equals("n")) {
      this.score = 0;
      this.resetWorld();
    }

    if (k.equals("d")) {
      this.resetToSolve();
      this.cameFromEdge.clear();
      this.depthFirstSearch();
    }

    if (k.equals("b")) {
      this.resetToSolve();
      this.cameFromEdge.clear();
      this.breadthFirstSearch();
    }

    if (k.equals("up") && !this.p.currentLoc.blockedTop) {
      if (!this.cameFromEdge.containsValue(p.currentLoc.top)) {
        this.cameFromEdge.put(p.currentLoc.top, p.currentLoc);
      }
      p.currentLoc.playerAtVertex = false;
      p.currentLoc.hasVisited = true;
      this.p.updateVertex(p.currentLoc.top);
      p.currentLoc.playerAtVertex = true;
      score++;
    }

    if (k.equals("left") && !this.p.currentLoc.blockedLeft) {
      if (!this.cameFromEdge.containsValue(p.currentLoc.left)) {
        this.cameFromEdge.put(p.currentLoc.left, p.currentLoc);
      }
      p.currentLoc.playerAtVertex = false;
      p.currentLoc.hasVisited = true;
      this.p.updateVertex(p.currentLoc.left);
      p.currentLoc.playerAtVertex = true;
      score++;
    }

    if (k.equals("down") && !this.p.currentLoc.blockedBottom) {
      if (!this.cameFromEdge.containsValue(p.currentLoc.bottom)) {
        this.cameFromEdge.put(p.currentLoc.bottom, p.currentLoc);
      }
      p.currentLoc.playerAtVertex = false;
      p.currentLoc.hasVisited = true;
      this.p.updateVertex(p.currentLoc.bottom);
      p.currentLoc.playerAtVertex = true;
      score++;
    }

    if (k.equals("right") && !this.p.currentLoc.blockedRight) {
      if (!this.cameFromEdge.containsValue(p.currentLoc.right)) {
        this.cameFromEdge.put(p.currentLoc.right, p.currentLoc);
      }
      p.currentLoc.playerAtVertex = false;
      p.currentLoc.hasVisited = true;
      this.p.updateVertex(p.currentLoc.right);
      p.currentLoc.playerAtVertex = true;
      score++;
    }

    else {
      return;
    }
  }

  // begins automatic solver using DFS or BFS
  public void onTick() {
    this.makeScene();
    if (this.enableDFS && !this.enableBFS && !this.dfsWorkList.isEmpty() && !isSolved) {
      this.depthFirstSearch();
    }
    
    else if (this.enableBFS && !this.enableDFS && !this.bfsWorkList.isEmpty() && !isSolved) {
      this.breadthFirstSearch();
    }

  }
}
