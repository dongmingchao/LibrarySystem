package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class Main extends Application {
    public Parent root;
    private Controller controller;
    Login getUserData;
    Stage mainStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        root = FXMLLoader.load(getClass().getResource("login.fxml"));
        primaryStage.setTitle("图书馆理系统-登陆");
        primaryStage.setScene(new Scene(root, 472, 339));
        primaryStage.show();
        mainStage = primaryStage;
        controller=new Controller();
        controller.app = this;
        getUserData = getLogin();
    }

    public Login getLogin(){
        Login login = new Login();
        login.setParentPane((AnchorPane) root);
        Login.controller = controller;
        login.init();
        return login;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
