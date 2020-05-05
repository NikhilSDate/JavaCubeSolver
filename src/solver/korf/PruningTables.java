package solver.korf;

import solver.RubiksCube;

import java.io.*;
import java.util.*;

public class PruningTables {
    //final static int cornerCases=88179840;
    final static int cornerCases=88179840;
    final static int halfEdgeCases=42577920;
    final static int halfEdgeOrientationCases=64;
    final static int halfEdgePermutationCases=665280;

    final static int cornerOrientationCases=2187;
    final static int cornerPermutationCases=40320;
    public final static byte cornerState=0;
    public final static byte firstEdgeState=1;
    public final static  byte secondEdgeState=2;
    public byte[][] cornerTable=new byte[cornerOrientationCases][cornerPermutationCases];
    public byte[][] firstEdgeTable =new byte[halfEdgeOrientationCases][halfEdgePermutationCases];
    public byte[][] secondEdgeTable=new byte[halfEdgeOrientationCases][halfEdgePermutationCases];


    private static PruningTables pruningTables=new PruningTables();


    public PruningTables(){}
    public static PruningTables getSingletonInstance(){
        return pruningTables;
    }

    public void initializeCornerTable(){
        for(int i=0;i<cornerOrientationCases;i++){
            for(int j=0;j<cornerPermutationCases;j++){
                cornerTable[i][j]=-1;
            }
        }
    }
    public void initializeFirstEdgeTable(){
        for(int i=0;i<halfEdgeOrientationCases;i++){
            for(int j=0;j<halfEdgePermutationCases;j++){
                firstEdgeTable[i][j]=-1;
            }
        }
    }
    public void initializeSecondEdgeTable(){
        for(int i=0;i<halfEdgeOrientationCases;i++){
            for(int j=0;j<halfEdgePermutationCases;j++){
                secondEdgeTable[i][j]=-1;
            }
        }
    }
    public void initializeAllTables(){
        initializeCornerTable();
        initializeFirstEdgeTable();
        initializeSecondEdgeTable();
    }
    public void getAllTablesFromFile() throws IOException, ClassNotFoundException {
        getCornerTableFromFile();
        getFirstEdgeTableFromFile();
        getSecondEdgeTableFromFile();

    }
    public void fillCornerTable()  {
        //System.out.println(Arrays.toString(cornerTable)) ;


        Queue<CornerConfigurationNode> searchQueue =new LinkedList<CornerConfigurationNode>();
        CornerConfigurationNode startingNode=new CornerConfigurationNode();
        searchQueue.add(startingNode);
        cornerTable[startingNode.getOrientationHash()][startingNode.getPermutationHash()]=startingNode.distance;
        int numberCases=1;
        System.out.println(searchQueue.peek().toString());
        while((!searchQueue.isEmpty())&&(numberCases<=cornerCases)){
            CornerConfigurationNode currentNode=searchQueue.remove();

            System.out.println(numberCases);
            for(byte move: RubiksCube.allowedMovesMap.get(currentNode.previousMove)){
                CornerConfigurationNode newNode=currentNode.doMove(move);
                int orientationHash=newNode.getOrientationHash();
                int permutationHash=newNode.getPermutationHash();
                if(cornerTable[orientationHash][permutationHash]==-1){
                    cornerTable[orientationHash][permutationHash]=newNode.distance;
                    searchQueue.add(newNode);
                    numberCases++;



                }
            }

        }



    }
    public void fillFirstEdgeTable(){
        Queue<EdgeConfigurationNode> searchQueue=new LinkedList<>();
        EdgeConfigurationNode startingNode=new EdgeConfigurationNode();
        searchQueue.add(startingNode);
        firstEdgeTable[startingNode.getFirstOrientationHash()][startingNode.getFirstPermutationHash()]
                =startingNode.distance;
        int numberCases=1;
        while((!searchQueue.isEmpty())&&(numberCases<=halfEdgeCases)){
            EdgeConfigurationNode currentNode=searchQueue.remove();
            for(byte move:RubiksCube.allowedMovesMap.get(currentNode.previousMove)){
                EdgeConfigurationNode newNode=currentNode.doMove(move);
                int orientationHash=newNode.getFirstOrientationHash();
                int permutationHash=newNode.getFirstPermutationHash();
                if(firstEdgeTable[orientationHash][permutationHash]==-1){
                    firstEdgeTable[orientationHash][permutationHash]=newNode.distance;
                    searchQueue.add(newNode);
                    numberCases++;
                    System.out.println(numberCases);
                }
            }

        }


    }
    public void fillSecondEdgeTable(){
        Queue<EdgeConfigurationNode> searchQueue=new LinkedList<>();
        EdgeConfigurationNode startingNode=new EdgeConfigurationNode();
        searchQueue.add(startingNode);
        secondEdgeTable[startingNode.getSecondOrientationHash()][startingNode.getSecondPermutationHash()]
                =startingNode.distance;
        int numberCases=1;
        while((!searchQueue.isEmpty())&&(numberCases<=halfEdgeCases)){
            EdgeConfigurationNode currentNode=searchQueue.remove();
            for(byte move:RubiksCube.allowedMovesMap.get(currentNode.previousMove)){
                EdgeConfigurationNode newNode=currentNode.doMove(move);
                int orientationHash=newNode.getSecondOrientationHash();
                int permutationHash=newNode.getSecondPermutationHash();
                if(secondEdgeTable[orientationHash][permutationHash]==-1){
                    secondEdgeTable[orientationHash][permutationHash]=newNode.distance;
                    searchQueue.add(newNode);
                    numberCases++;
                    System.out.println(numberCases);
                }
            }

        }
    }
    public void writeCornerTableToFile() throws IOException {

        FileOutputStream fos=new FileOutputStream("corner_pruning_table.txt");
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(fos);
        objectOutputStream.writeObject(cornerTable);
        fos.close();
    }
    public void getCornerTableFromFile() throws IOException, ClassNotFoundException {
        FileInputStream fis=new FileInputStream("corner_pruning_table.txt");
        ObjectInputStream objectInputStream=new ObjectInputStream(fis);
        this.cornerTable=(byte[][])objectInputStream.readObject();
        objectInputStream.close();
        fis.close();
    }
    public void writeFirstEdgeTableToFile() throws IOException {
        FileOutputStream fos=new FileOutputStream("first_edge_pruning_table.txt");
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(fos);
        objectOutputStream.writeObject(firstEdgeTable);

        fos.close();
    }
    public void writeSecondEdgeTableToFile() throws IOException {
        FileOutputStream fos=new FileOutputStream("second_edge_pruning_table.txt");
        ObjectOutputStream objectOutputStream=new ObjectOutputStream(fos);
        objectOutputStream.writeObject(secondEdgeTable);

        fos.close();
    }
    public void getFirstEdgeTableFromFile() throws IOException, ClassNotFoundException {
        FileInputStream fis=new FileInputStream("first_edge_pruning_table.txt");
        ObjectInputStream objectInputStream=new ObjectInputStream(fis);
        this.firstEdgeTable=(byte[][])objectInputStream.readObject();
        objectInputStream.close();
        fis.close();
    }
    public void getSecondEdgeTableFromFile() throws IOException,ClassNotFoundException {
        FileInputStream fis=new FileInputStream("second_edge_pruning_table.txt");
        ObjectInputStream objectInputStream=new ObjectInputStream(fis);
        this.secondEdgeTable=(byte[][])objectInputStream.readObject();
        objectInputStream.close();
        fis.close();
    }
    public byte getCornerHeuristic(RubiksCube cube){
        return cornerTable[cube.getCornerOrientationHash()][cube.getCornerPermutationHash()];
    }
    public byte getFirstEdgeHeuristic(RubiksCube cube){
        return firstEdgeTable[cube.getFirstEdgeOrientationHash()][cube.getFirstEdgePermutationHash()];
    }
    public byte getSecondEdgeHeuristic(RubiksCube cube){
        return secondEdgeTable[cube.getSecondEdgeOrientationHash()][cube.getSecondEdgePermutationHash()];
    }


}
