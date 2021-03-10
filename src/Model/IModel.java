package Model;

import algorithms.mazeGenerators.Maze;

import java.io.File;
import java.util.Observer;

public interface IModel {
    public void generateBoard(int rows, int columns);
    public void updateCharacterLocation(String direction);
    public void assignObserver(Observer o);
    public void solveMaze();
    public Maze getMaze();
    public void updateMaze(Maze maze);
    public void openFileAndUpdate(File file);
    public void saveFile(File file);
}
