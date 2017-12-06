package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class Controller {
    Main app;

    StringProperty my_identity = new SimpleStringProperty();
    StringProperty my_username = new SimpleStringProperty();
    public AnchorPane left;
    public Label username;
    public Label identity;
    public Label mess2;
    public ImageView myfav;

    private void lookupAll(){
        left = (AnchorPane)((SplitPane) app.root).getItems().get(0);
        username = (Label) left.lookup("#username");
        identity = (Label) left.lookup("#myidentity");
        myfav = (ImageView) left.lookup("#myfav");
    }

    private void setFav(String url){
        try {
            myfav.setImage(new Image(new FileInputStream(Paths.get(url).toFile())));
        } catch (FileNotFoundException e) {
            System.out.println("文件未找到");
            e.printStackTrace();
        }
    }


    void init(){
        lookupAll();
        identity.setText(my_identity.get());
        username.setText(my_username.get());
//        System.out.println(identity.getText());
//        System.out.println(identity.getText().equals("管理员"));
        if (my_identity.get().equals("管理员")) setFav("img/admin.jpg");
        else setFav("img/users.jpg");
    }
}
