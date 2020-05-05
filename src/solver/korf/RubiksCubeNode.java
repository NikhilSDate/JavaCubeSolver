package solver.korf;

import solver.RubiksCube;

public class RubiksCubeNode {
    RubiksCube rubiksCube;
    byte previousMove;
    byte distance;
    byte heuristic;

    public RubiksCubeNode(){
        this.rubiksCube=new RubiksCube();
        this.distance=0;
        this.previousMove=RubiksCube.I;
        this.heuristic=0;

    }
    public RubiksCubeNode(byte[] scramble){
        RubiksCube cube=new RubiksCube();
        cube.doAlgorithm(scramble);
        this.rubiksCube=cube;//TODO CLONE
        this.distance=0;
        this.heuristic=cube.getHeuristic();
        this.previousMove=RubiksCube.I;
    }
    public RubiksCubeNode(RubiksCube cube){
        this.rubiksCube=cube;
        this.previousMove=RubiksCube.I;
        this.distance=0;
        this.heuristic=cube.getHeuristic();
    }

    public RubiksCubeNode doMove(byte move){
        RubiksCubeNode node=this.clone();
        node.rubiksCube.doMove(move);
        node.distance=(byte)(this.distance+1);
        node.heuristic=node.rubiksCube.getHeuristic();
        node.previousMove=move;
        return node;

    }
    @Override
    public RubiksCubeNode clone(){
        RubiksCubeNode node=new RubiksCubeNode();
        node.rubiksCube=this.rubiksCube.clone();
        node.previousMove=this.previousMove;
        node.distance=this.distance;
        node.heuristic=this.heuristic;
        return node;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RubiksCubeNode that = (RubiksCubeNode) o;
        return previousMove == that.previousMove &&
                distance == that.distance &&
                heuristic == that.heuristic &&
                rubiksCube.equals(that.rubiksCube);
    }


}
