
// Assignment 10
// Al-Saleh, Abdullah
// aalsaleh
// Wu, Guanting
// wukyle
// Zhen, Jialin
// jialinzhen

import java.util.*;
import tester.*;
//import javalib.impworld.*;
//import java.awt.Color;
//import javalib.worldimages.*;

class ExamplesMazeGameWorld {
  // instantiation of Vertices
  Vertex v1;
  Vertex v2;
  Vertex v3;
  Vertex v4;

  // instantiation of MazeGameWorld
  MazeGameWorld m1;
  MazeGameWorld m2;

  // instatation of a representatives HashMap
  HashMap<Vertex, Vertex> testRepresentatives;

  // initializes data for testing
  void initData() {
    v1 = new Vertex(4, 5);
    v2 = new Vertex(4, 5);
    v3 = new Vertex(5, 4);
    v4 = new Vertex(4, 20);

    m1 = new MazeGameWorld();
    m2 = new MazeGameWorld(4, 4);

    testRepresentatives = new HashMap<Vertex, Vertex>();

    testRepresentatives.put(v1, v1);
    testRepresentatives.put(v3, v4);
    testRepresentatives.put(v4, v1);
  }

  // run the game
  void testMazeGame(Tester t) {
    MazeGameWorld game = new MazeGameWorld(10, 10);
    game.bigBang(game.windowWidth() + 1, game.windowHeight() + 1, 0.1);
  }

  // test for sameVertex
  void testSameVertex(Tester t) {
    this.initData();

    t.checkExpect(v1.equals(v2), true);
    t.checkExpect(v1.equals(v3), false);
    t.checkExpect(v1.equals(v4), false);
  }

  // test for makeGraph
  void testMakeGraph(Tester t) {
    this.initData();

    t.checkExpect(m1.maze.size(), 6000);
    t.checkExpect(m2.maze.size(), 16);

    t.checkExpect(m2.maze.get(2), new Vertex(2, 0));
    t.checkExpect(m1.maze.get(100), new Vertex(0, 1));
  }

  // test for addEdge
  void testAddEdge(Tester t) {
    this.initData();

    t.checkExpect(v1.outEdges.size(), 0);

    v1.addEdge(v3, 1);

    t.checkExpect(v1.outEdges.size(), 1);
    t.checkExpect(v1.outEdges.get(0), new Edge(v1, v3, 1));

    v1.addEdge(v4, 5);
    t.checkExpect(v1.outEdges.size(), 2);
    t.checkExpect(v1.outEdges.get(1), new Edge(v1, v4, 5));

    // v1.addEdge(new Vertex(-3, 5), 1);

    // t.checkExpect(v1.outEdges.size(), 2);
  }

  // tests for vertexMap
  void testMapVertex(Tester t) {
    this.initData();

    t.checkExpect(m2.vertexMap(new Vertex(3, 3)), 15);

    t.checkExpect(m2.maze.get(m2.vertexMap(new Vertex(3, 3))), new Vertex(3, 3));
  }

  // tests for addNeighbors
  void testAddNeighbors(Tester t) {
    this.initData();

    // t.checkExpect(m2.maze.get(3).outEdges.size(), 0);

    // m2.addNeighbors(m2.maze.get(3));

    t.checkExpect(m2.maze.get(3).outEdges.size(), 2);
    // t.checkExpect(m2.maze.get(3).outEdges.size(), 2);
    t.checkExpect(m2.maze.get(3), new Vertex(3, 0));
    // t.checkExpect(m2.maze.get(3).outEdges.get(0).to, new Vertex(3, 1));
    // t.checkExpect(m2.maze.get(3).outEdges.get(1).to, new Vertex(2, 0));

    t.checkRange(m2.maze.get(3).outEdges.get(0).weight, 0, 1000);

    t.checkExpect(m2.maze.get(4).top, new Vertex(0, 0));
    t.checkExpect(m2.maze.get(0).bottom, new Vertex(0, 1));
  }

  // tests for makeEdges
  void testMakeEdges(Tester t) {
    this.initData();

    // t.checkExpect(m2.maze.get(0).outEdges.size(), 2);
    t.checkExpect(m2.maze.get(6).outEdges.size(), 4);

    // test to ensure no singular vertex has degree greater than 2
    // since every node is only aware of its top and left neighbor
    int currMax = m2.maze.get(0).outEdges.size();
    for (int i = 1; i < m2.maze.size(); i++) {
      if (m2.maze.get(i).outEdges.size() > currMax) {
        currMax = m2.maze.get(i).outEdges.size();
      }
    }

    t.checkExpect(currMax, 4);

    // m2.makeEdges();

    // t.checkExpect(m2.maze.get(0).outEdges.size(), 2);
    // t.checkExpect(m2.maze.get(6).outEdges.size(), 4);
  }

  // tests for createWorkList
  void testCreateWorkList(Tester t) {
    this.initData();

    t.checkExpect(m2.workList.size(), 24);
    t.checkExpect(m2.workList.get(0).weight < m2.workList.get(1).weight, true);
    t.checkExpect(m1.workList.size(), 11840);
    t.checkExpect(m1.workList.get(5).weight <= m1.workList.get(6).weight, true);
  }

  // tests for find
  void testFind(Tester t) {
    this.initData();

    t.checkExpect(m1.find(testRepresentatives, v1), v1);
    t.checkExpect(m1.find(testRepresentatives, v3), v1);
  }

  // tests for union
  void testUnion(Tester t) {
    this.initData();

    m1.union(testRepresentatives, v4, new Vertex(12, 43));

    t.checkExpect(testRepresentatives.get(v4), new Vertex(12, 43));
  }

  // tests for windowWidth
  // void testWindowWidth(Tester t) {
  // this.initData();
  //
  // t.checkExpect(m1.windowWidth(), 500);
  // t.checkExpect(m2.windowWidth(), 20);
  // }

  // tests for windowHeight
  // void testWindowHeight(Tester t) {
  // this.initData();
  //
  // t.checkExpect(m1.windowHeight(), 300);
  // t.checkExpect(m2.windowHeight(), 20);
  // }
  //
  // tests for buildTree
  void testBuildTree(Tester t) {
    this.initData();

    t.checkExpect(m2.edgesInTree.size(), 15);
    t.checkExpect(m2.edgesNotInTree.size(), 9);
    t.checkExpect(m1.edgesInTree.size(), 5999);
    t.checkExpect(m1.edgesNotInTree.size(), 5841);
  }

  // tests for onKeyEvent
  void testOnKeyEvent(Tester t) {
    this.initData();

    t.checkExpect(m1.score, 0);
    m1.onKeyEvent("up");
    t.checkExpect(m1.score, 0);
  }

}
