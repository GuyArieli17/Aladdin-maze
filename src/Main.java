import Model.IModel;
import Model.MyModel;
import View.MyViewController;
import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        //FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("View/MyView.fxml")) ;
        //Parent root = fxmlLoader.load();
        primaryStage.setTitle("Aladdin Search");
//        BorderPane borderPane = new BorderPane();
//        MenuBarMaze menuBarMaze = new MenuBarMaze();
//        borderPane.setTop(menuBarMaze);
//        MazeDisplayer mazeDisplayer = new MazeDisplayer();
//        borderPane.setCenter(new ZoomableScrollPane(mazeDisplayer));
//        borderPane.setLeft(new MazeInput());
       // mazeDisplayer.drawBoard(new Maze(3,2));



        IModel model = new MyModel();
        MyViewModel viewModel = new MyViewModel(model,primaryStage,700,450);
        MyViewController viewController = new MyViewController(viewModel,primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
