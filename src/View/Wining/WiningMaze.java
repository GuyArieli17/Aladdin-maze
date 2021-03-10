package View.Wining;

import ViewModel.MyViewModel;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class WiningMaze extends BorderPane {
    private static final String backgroundImag_path = "/Image/wining.jpg";
    private Button restartGameButton;
    private MyViewModel viewModel;
    public WiningMaze(MyViewModel viewModel,double width,double height){
        super();
        this.viewModel = viewModel;
        restartGameButton = new Button("Restart");
        restartGameButton.setMinSize(130,40);
        restartGameButton.setStyle("-fx-background-color: #8e44ad ;-fx-font-weight: bold; -fx-text-fill: #ecf0f1;");

        restartGameButton.setPadding(new Insets(3,20,3,20));
//        restartGameButton.setTextFill(Color.rgb(44, 62, 80));
        HBox buttonCont = new HBox(restartGameButton);
        buttonCont.setAlignment(Pos.CENTER);
        buttonCont.setPadding(new Insets(230,0,0,0));
        buttonCont.setSpacing(101);
        this.setCenter(buttonCont);
        //URL ul = getClass().getResource("/wining.jpg");
        Image imge = new Image(String.valueOf(getClass().getResource(backgroundImag_path)));
        BackgroundSize backgroundSize = new BackgroundSize(width, height, false,
                false, false, false);
        BackgroundImage myBI = new BackgroundImage(
                imge,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                new BackgroundSize(1.0, 1.0, true,
                        true, false, false));

        setBackground(new Background(myBI));
        setRestartGameButton();
    }

    public void setRestartGameButton(){
        this.restartGameButton.setOnAction(e->{viewModel.winingRestartButtonOnAction();});
    }

    public void updateSize(double width,double height){
        double mini = Math.min(height,width)/15;
        restartGameButton.setMinSize(mini,mini/3);
        this.restartGameButton.setStyle("-fx-background-color: #8e44ad ;-fx-font-weight: bold; -fx-text-fill: #ecf0f1;-fx-font-size: " + mini/3 + ";");
    }
}
