package Model;

import IO.MyDecompressorInputStream;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.AMazeGenerator;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;
import algorithms.mazeGenerators.Position;
import algorithms.search.*;
import javafx.stage.FileChooser;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class MyModel extends Observable implements  IModel{

    private AMazeGenerator mazeGenerator;//the Generator
    private Position characterPosition;//Psotion of the player
    private Maze maze;//the maze we use
    private Solution solution;//Solution of the maze
    private ASearchingAlgorithm mazeSolver;//genarate solution
    private ServerStrategySolveSearchProblem server;
    private FileChooser fileChooser;

    private static final int mazeGeneratingServer_ip = 5400;
    private static final int solveSearchProblemServer_ip = 5401;
    private Server mazeGeneratingServer = new Server(mazeGeneratingServer_ip,1000, new ServerStrategyGenerateMaze());
    private Server solveSearchProblemServer =  new Server(solveSearchProblemServer_ip, 1000, new ServerStrategySolveSearchProblem());
    private boolean hasWon;

    /**Generate my Model**/
    public MyModel() {
        maze = null;
        solution=null;
        mazeGenerator = new MyMazeGenerator();
        mazeSolver = new BestFirstSearch();
        hasWon = false;
        solveSearchProblemServer.start();
        mazeGeneratingServer.start();
        this.fileChooser = new FileChooser();
        this.fileChooser.setInitialDirectory(new File("C:\\"));
    }

    /**Assign Observer to this**/
    @Override
    public void assignObserver(Observer o) {
        this.addObserver(o);
    }

    /**Solve the Maze**/
    @Override
    public void solveMaze() {
        //Solving maze
        if(maze==null)return;
        ISearchable searchableMaze = new SearchableMaze(this.maze);
        ISearchingAlgorithm searchingAlgorithm = new BreadthFirstSearch();
        this.solution= commuteWithSolveSearchProblemServer();//searchingAlgorithm.solve(searchableMaze);
        setChanged();
        notifyObservers(this.solution);
    }

    /**Return the Maze**/
    @Override
    public Maze getMaze() { return maze; }

    @Override
    public void updateMaze(Maze maze) { this.maze = maze; }


    /**Generate Maze by size(row,columns)**/
    @Override
    public void generateBoard(int rows, int columns) {
        this.hasWon = false;
        Maze maze = commuteWithGenerateServer(rows,columns);
        if (maze==null){ return;}
        //Maze maze =  mazeGenerator.generate(rows,columns);
        this.maze=maze;
        int rowChar=maze.getStartPosition().getRowIndex();
        int colChar=maze.getStartPosition().getColumnIndex();
        this.characterPosition = new Position(rowChar,colChar);
        setChanged();
        notifyObservers(this.maze);

    }

    /**update the location of the player**/
    @Override
    public void updateCharacterLocation (String direction){
        System.out.println("FUCK YOU ALL");
        System.out.flush();
        /*
            direction = 1 -> Up
            direction = 2 -> Down
            direction = 3 -> Left
            direction = 4 -> Right
            direction = 5 -> RightUp
            direction = 6 -> RightDown
            direction = 7 -> LeftUp
            direction = 8 -> leftDown
         */
        Position nextStep = null;
        if (this.maze == null || (this.maze != null && hasWon))return;
        int rowChar = this.characterPosition.getRowIndex(),
                colChar = this.characterPosition.getColumnIndex();
        switch (direction) {
            case "DownLeft":
                rowChar++;
                colChar--;
                break;
            case "Down":
                rowChar++;
                break;
            case "DownRight":
                rowChar++;
                colChar++;
                break;
            case "Left":
                colChar--;
                break;
            case "Right":
                colChar++;
                break;
            case "UpLeft":
                rowChar--;
                colChar--;
                break;
            case "Up":
                rowChar--;
                break;
            case "UpRight":
                rowChar--;
                colChar++;
                break;

        }
        nextStep = new Position(rowChar, colChar);
        if (this.maze.isPointOnMaze(nextStep) && this.maze.isPathPoint(nextStep)) {
            this.characterPosition = nextStep;
            if (this.characterPosition.equals(this.maze.getGoalPosition())){/**WE HAVE WON **/
                hasWon = true;
                setChanged();
                notifyObservers(hasWon);
            }
            else{
                setChanged();
                notifyObservers(nextStep);
            }

        }
    }

    /**Connect to server and return a maze using the server**/
    private Maze commuteWithGenerateServer(int rows,int columns)  {
        Maze maze= null;
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getLocalHost(),mazeGeneratingServer_ip);
            InputStream inFromServer = socket.getInputStream();
            OutputStream outToServer = socket.getOutputStream();
            try {
                ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                toServer.flush();
                int[] mazeDimensions = new int[]{rows,columns};
                toServer.writeObject(mazeDimensions); //send maze dimensions to server
                toServer.flush();
                byte[] compressedMaze = (byte[]) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                InputStream is = new MyDecompressorInputStream(new ByteArrayInputStream(compressedMaze));
                byte[] decompressedMaze = new byte[100000/*CHANGE SIZE ACCORDING TO YOU MAZE SIZE*/]; //allocating byte[] for the decompressed maze -
                is.read(decompressedMaze); //Fill decompressedMaze with bytes
                maze = new Maze(decompressedMaze);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maze;
    }

    /**Connect to server and return a Solution to the server**/
    private Solution commuteWithSolveSearchProblemServer(){
        Solution mazeSolution = null;
        try {
            Socket socket = new Socket(InetAddress.getLocalHost(), solveSearchProblemServer_ip);
            InputStream inFromServer = socket.getInputStream();
            OutputStream outToServer = socket.getOutputStream();
            try {
                ObjectOutputStream toServer = new ObjectOutputStream(outToServer);
                ObjectInputStream fromServer = new ObjectInputStream(inFromServer);
                toServer.flush();
                Maze maze = this.maze;
                System.out.println("START POINT " + this.maze.getStartPosition().toString());
                toServer.writeObject(maze); //send maze to server
                toServer.flush();
                mazeSolution = (Solution) fromServer.readObject(); //read generated maze (compressed with MyCompressor) from server
                ArrayList<AState> mazeSolutionSteps = mazeSolution.getSolutionPath();
                for (int i = 0; i < mazeSolutionSteps.size(); i++) {
                    System.out.println(String.format("%s. %s", i,
                            mazeSolutionSteps.get(i).toString()));
                }
                System.out.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mazeSolution;
    }

    public void openFileAndUpdate(File file){
        try{
            fileChooser.setInitialDirectory(file.getParentFile());
            FileInputStream fileOutputStream = new FileInputStream(file.getAbsoluteFile());
            ObjectInputStream objectInputStream = new ObjectInputStream(fileOutputStream);
            Object object = objectInputStream.readObject();
            objectInputStream.close(); fileOutputStream.close();
            this.maze = (Maze)object;
            int rowChar=maze.getStartPosition().getRowIndex();
            int colChar=maze.getStartPosition().getColumnIndex();
            this.characterPosition = new Position(rowChar,colChar);
            this.solution = null;
            setChanged();
            notifyObservers(this.maze);
        } catch (Exception e) {
            setChanged();
            if (e instanceof NullPointerException){
                notifyObservers(new Exception("You Have to Select A File"));
            }else{
                notifyObservers(e);
            }
        }
    }

    public void saveFile(File file){
        try {
            fileChooser.setInitialDirectory(file.getParentFile());
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(maze);
            objectOutputStream.flush(); objectOutputStream.close(); fileOutputStream.close();
        }
        catch (Exception e){
            setChanged();
            if (e instanceof NullPointerException){
                notifyObservers(new Exception("You Have to Select A folder to save in"));
            }else{
                notifyObservers(e);
            }
        }

    }


}
