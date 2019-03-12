// Assignment 10
// Al-Saleh, Abdullah
// aalsaleh
// Wu, Guanting
// wukyle
// Zhen, Jialin
// jialinzhen

// represents the player
class Player {

  int x;
  int y;

  Vertex currentLoc;

  Player(Vertex currentLoc) {
    this.currentLoc = currentLoc;
  }

  // updates the player's position
  void updateVertex(Vertex v) {
    this.currentLoc = v;
    this.x = v.loc.x;
    this.y = v.loc.y;
  }
}
