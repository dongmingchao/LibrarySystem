package sample;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.WindowEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class Controller {
    Main app;

    StringProperty my_identity = new SimpleStringProperty();
    StringProperty my_username = new SimpleStringProperty();
    static ObservableList<Book> showAll;

    public AnchorPane left;
    public TabPane right;
    public Label username;
    public Label identity;
    public Label mess2;
    public ImageView myfav;
    public TableView<Book> showBooks;

    static User holder;

    private void lookupAll(){
        left = (AnchorPane)((SplitPane) app.root).getItems().get(0);
        username = (Label) left.lookup("#username");
        identity = (Label) left.lookup("#identity");
        myfav = (ImageView) left.lookup("#myfav");
        right = (TabPane) ((SplitPane) app.root).getItems().get(1);
        showBooks = (TableView<Book>) right.getTabs().get(0).getContent();
    }

    void init(){
        lookupAll();
        identity.setText(my_identity.get());
        username.setText(my_username.get());
        holder.setController(this);
        holder.envInit();
        holder.initPersonTable();
        Store.importBooks();
        showAll = FXCollections.observableArrayList(Store.importBooks());
        tableViewInit();
        this.app.mainStage.setOnCloseRequest(this::closeAndSave);
    }

    private void closeAndSave(WindowEvent event){
        Store.saveBooks();
        holder.saveBooks();
    }

    private void tableViewInit(){
        showBooks.setItems(showAll);
        showBooks.getColumns().get(0).setCellValueFactory(new PropertyValueFactory("name"));
        showBooks.getColumns().get(1).setCellValueFactory(new PropertyValueFactory("author"));
        showBooks.getColumns().get(2).setCellValueFactory(new PropertyValueFactory("publish"));
        showBooks.getColumns().get(3).setCellValueFactory(new PropertyValueFactory("number"));
        showBooks.getColumns().get(4).setCellValueFactory(new PropertyValueFactory("allLite"));
    }
}
