package sample;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Admin extends User{
    TextField setBookName;
    TextField setBookAuthor;
    TextField setBookPublish;
    TextField setBookNumber;
    Button add;

    Admin(String name, String pass,ArrayList<Book> hadBooks) {
        super(name, pass,hadBooks);
        fileName = "signedAdmin";
    }

    void addBooks(List<Book> books){
        Store.getAddedBooks().addAll(books);
    }
    void removeBooks(List<Book> books){
        Store.getAddedBooks().removeAll(books);
    }
    void signBooks(List<Book> books){

    }

    private void addBook(ActionEvent event){
        Integer number;
        try {
            number = Integer.valueOf(setBookNumber.getText());
            Book book = new Book(setBookName.getText(),setBookAuthor.getText(),setBookPublish.getText(),number);
            controller.showAll.add(book);
            Store.getAddedBooks().add(book);
            setBookName.setText("");
            setBookAuthor.setText("");
            setBookPublish.setText("");
            setBookNumber.setText("");
            FXtools.showTip("添加成功！",event);
        }catch (NumberFormatException e){
            FXtools.showTip("数量选项请输入一个数字！",event);
        }
    }

    void envInit(){
        FXtools.setFav("img/admin.jpg",controller.myfav);
        Tab manage = new Tab("管理");
        GridPane managePane = new GridPane();
        managePane.setVgap(20);
        setBookName = new TextField();
        setBookName.setPromptText("书名");
        setBookAuthor = new TextField();
        setBookAuthor.setPromptText("作者");
        setBookPublish = new TextField();
        setBookPublish.setPromptText("出版社");
        setBookNumber = new TextField();
        setBookNumber.setPromptText("数量");
        add = new Button("添加这本书");
        add.setOnAction(this::addBook);
        managePane.add(setBookName,0,0);
        managePane.add(setBookAuthor,0,1);
        managePane.add(setBookPublish,0,2);
        managePane.add(setBookNumber,0,3);
        managePane.add(add,0,4);
        manage.setContent(managePane);
        managePane.setPadding(new Insets(30));
        controller.right.getTabs().add(manage);
    }

    @Override
    public String toString() {
        return name + " " + pass ;
    }
}
