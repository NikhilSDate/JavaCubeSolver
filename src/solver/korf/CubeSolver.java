package solver.korf;

import solver.RubiksCube;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Stack;



public class CubeSolver {
    final static int SOLVED=-1;
    int counter=0;


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        RubiksCube.initializeAllowedMovesMaps();
        ArrayList<Byte> current=new ArrayList<>();
        ArrayList<Integer> possibleCases= new ArrayList<Integer>(Arrays.asList(0,1,2,3,4,5,6,7,8,9,10,11));
        EdgeConfigurationNode.generatePermutationHashes(current,possibleCases);
        PruningTables tables=PruningTables.getSingletonInstance();
        tables.initializeAllTables();
        tables.getAllTablesFromFile();
        RubiksCube cube=new RubiksCube();
        cube.doAlgorithm(new byte[]{RubiksCube.R,RubiksCube.U, RubiksCube.R_PRIME,RubiksCube.F_PRIME,
                RubiksCube.R,RubiksCube.U2, RubiksCube.R_PRIME,RubiksCube.U_PRIME, RubiksCube.F_PRIME, RubiksCube.U2, RubiksCube.D,
                RubiksCube.B_PRIME,RubiksCube.R2, RubiksCube.U2, RubiksCube.F2, RubiksCube.L2, RubiksCube.B_PRIME

        });
        CubeSolver solver=new CubeSolver();
        ArrayList<String> solution=solver.getSolutionString(solver.ida_star(cube));
        System.out.println(solution);





    }
    public Stack<RubiksCubeNode> ida_star(RubiksCube state){
        int bound=state.getHeuristic();
        Stack<RubiksCubeNode> path=new Stack<RubiksCubeNode>();
        RubiksCubeNode startingNode=new RubiksCubeNode(state);
        path.push(startingNode);
        while(true){
            System.out.println(bound);
            int t=search(path,0,bound);
            if(t==SOLVED){
                return path;
            }
            if(t==Integer.MAX_VALUE){
                return null;
            }
            bound=t;
        }
    }
    public int search(Stack<RubiksCubeNode> path, int g, int bound){
        RubiksCubeNode node=path.lastElement();
        int f=g+node.rubiksCube.getHeuristic();
        if(f>bound){
            return f;
        }if(node.rubiksCube.isSolved()){
            return SOLVED;
        }
        int min=Integer.MAX_VALUE;
        for(byte move:RubiksCube.allowedMovesMap.get(node.previousMove)){
            RubiksCubeNode newNode=node.doMove(move);
            counter++;
            if(counter%1000000==0){
                System.out.println(counter);
            }
            if(!path.contains(newNode)){
                path.push(newNode);
                int t=search(path,g+1,bound);
                if(t==SOLVED){
                    return SOLVED;
                }
                if(t<min){
                    min=t;
                }
                path.pop();
            }
        }
        return min;

    }
    public ArrayList<String> getSolutionString(Stack<RubiksCubeNode> solution){
        ArrayList<String> solutionList=new ArrayList<>();
        while(!solution.isEmpty()){
            solutionList.add(RubiksCube.getMoveString(solution.pop().previousMove));
        }
        solutionList.remove(solutionList.size()-1);
        Collections.reverse(solutionList);
        return solutionList;


    }

}
