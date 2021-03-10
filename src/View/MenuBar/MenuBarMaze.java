package View.MenuBar;

import ViewModel.MyViewModel;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;


public class MenuBarMaze extends MenuBar {

    /**Menu menu**/
    private Menu fileMenu;
    private Menu optionMenu;
    private Menu helpMenu;
    private Menu aboutMenu;
    private Menu exitMenu;
    /**Menu item (Sub menu)**/
    private MenuItem newItem_File;
    private MenuItem continueItem_File;
    private MenuItem saveItem_File;
    private MenuItem loadItem_File;
    private MenuItem propertiesItem_Options;
    private Label exit_label;
    private Label about_label;
    private Label help_label;
    private  Label continueLebel;
    private Menu continueMenu;
    private MyViewModel myViewModel;
    private BooleanProperty booleanProperty = new SimpleBooleanProperty(false);

    public MenuBarMaze(MyViewModel viewModel){
        super();
        this.myViewModel = viewModel;
        this.createFileMenu();
        this.createOptionMenu();
        this.createAboutMenu();
        this.createHelpMenu();
        this.createExitMenu();
        this.createContinueMenu();
        setStyle("-fx-background-color: #ecf0f1");


    }

    private void createFileMenu(){
        this.fileMenu = new Menu("File");
        newItem_File = new MenuItem("New");
        saveItem_File = new MenuItem("Save");
        loadItem_File = new MenuItem("Load");
//        continueItem_File = new MenuItem("Continue");
        fileMenu.getItems().add(newItem_File);
        fileMenu.getItems().add(saveItem_File);
        fileMenu.getItems().add(loadItem_File);

        getMenus().add(fileMenu);
    }

    private void createOptionMenu(){
        this.optionMenu = new Menu("Options");

        propertiesItem_Options = new MenuItem("Properties");

        optionMenu.getItems().add(propertiesItem_Options);

        getMenus().add(optionMenu);
    }

    private void createExitMenu(){
        this.exitMenu = new Menu();
        this.exit_label = new Label("Exit");
        this.exitMenu.setGraphic(exit_label);
        getMenus().add(exitMenu);
    }

    private void createHelpMenu(){
        this.helpMenu = new Menu();
        this.help_label = new Label("Help");
        helpMenu.setGraphic(help_label);
        getMenus().add(helpMenu);
    }

    private void createAboutMenu(){
        this.aboutMenu = new Menu();
        this.about_label = new Label("About");
        aboutMenu.setGraphic(about_label);
        getMenus().add(aboutMenu);
    }

    private void createContinueMenu(){
        this.continueMenu = new Menu();
        this.continueLebel = new Label("Continue");
        continueMenu.setGraphic(continueLebel);
        getMenus().add(continueMenu);
        this.continueMenu.visibleProperty().bind(this.booleanProperty);
    }

    public void setOnActionNewItem_File(){
        this.newItem_File.setOnAction(e->myViewModel.fileNew());
    }

    public void setOnActionSaveItem_File(){
        this.saveItem_File.setOnAction(e->myViewModel.fileSave());
    }

    public void setOnActionLoadItem_File(){
        this.loadItem_File.setOnAction(e->myViewModel.fileLoad());
    }

    public void setContinueMenu_File(){ this.continueLebel.setOnMouseClicked(e->myViewModel.setOnContinue()); }

    public void setOnActionPropertiesItem_Options(){
        this.propertiesItem_Options.setOnAction(e->myViewModel.optionProperties());
    }

    public void setOnActionExit(){this.exit_label.setOnMouseClicked(e->myViewModel.exitGame());}

    public void setOnActionHelp(){this.help_label.setOnMouseClicked(e->myViewModel.help());}

    public void setOnActionAbout(){ this.about_label.setOnMouseClicked(e->myViewModel.about());}

    public void updateContinueMenu(boolean toHideContinue){
//        System.out.println("toHIde: " + !toHideContinue);
        this.booleanProperty.set(true);//        this.continueMenu.setVisible(true);
//        this.continueMenu.setVisible(!toHideContinue);
    }


}
