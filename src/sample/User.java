package sample;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class User {
    String name;
    String pass;
    Controller controller;
    TreeTableView message;
    ArrayList<Book> hadBooks;
    TreeItem<Book> outOfDate = new TreeItem<Book>(new Book("已过期", "", "", 0));
    TreeItem<Book> inDate = new TreeItem<Book>(new Book("未过期", "", "", 0));
    TreeItem<Book> all = new TreeItem<Book>(new Book("全部", "", "", 0));


    public void setController(Controller controller) {
        this.controller = controller;
    }

    public User(String name, String pass, ArrayList<Book> hadBooks) {
        this.name = name;
        this.pass = pass;
        this.hadBooks = hadBooks;
    }

    void envInit() {
        FXtools.setFav("img/users.jpg", controller.myfav);
    }

    void initPersonTable() {
        message = ((TreeTableView) controller.right.getTabs().get(1).getContent());
        outOfDate.setExpanded(true);
        inDate.setExpanded(true);
        all.setExpanded(true);
        message.setRoot(all);
        all.getChildren().addAll(outOfDate, inDate);
        hadBooks.forEach(this::addToTable);
        TreeTableColumn<Book, String> bookName = (TreeTableColumn<Book, String>) message.getColumns().get(0);
        TreeTableColumn<Book, String> bookAuthor = (TreeTableColumn<Book, String>) message.getColumns().get(1);
        TreeTableColumn<Book, String> bookPublish = (TreeTableColumn<Book, String>) message.getColumns().get(2);
        TreeTableColumn<Book, String> bookNumber = (TreeTableColumn<Book, String>) message.getColumns().get(3);
        TreeTableColumn<Book, String> bookTime = (TreeTableColumn<Book, String>) message.getColumns().get(4);
        TreeTableColumn<Book, HBox> bookOperator = (TreeTableColumn<Book, HBox>) message.getColumns().get(5);
        bookName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getName()));
        bookAuthor.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getAuthor()));
        bookPublish.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->
                new ReadOnlyStringWrapper(param.getValue().getValue().getPublish()));
        bookNumber.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) ->
                new ReadOnlyStringWrapper(String.valueOf(param.getValue().getValue().getNumber())));
        bookTime.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, String> param) -> {
            if (param.getValue().getValue().borrowTime != null)
                return new ReadOnlyStringWrapper(param.getValue().getValue().borrowTime.toString());
            else return new SimpleStringProperty();
        });
        bookOperator.setCellValueFactory((TreeTableColumn.CellDataFeatures<Book, HBox> param) -> param.getValue().getValue().personLiteProperty());
    }

    private void addToTable(Book book){
        if (book.borrowTime.isAfter(LocalDate.now()))
            inDate.getChildren().add(new TreeItem<>(book));
        else outOfDate.getChildren().add(new TreeItem<>(book));
    }

    private void removeFromTable(Book book){
        if (book.borrowTime.isAfter(LocalDate.now()))
            inDate.getChildren().remove(inDate.getChildren().stream().filter(each -> each.getValue().equals(book)).findFirst().orElse(null));
        else outOfDate.getChildren().remove(outOfDate.getChildren().stream().filter(each -> each.getValue().equals(book)).findFirst().orElse(null));
    }

    public void borrow(List<Book> books) {
        hadBooks.addAll(books);
    }

    public void borrow(Book book) {
        addToTable(book);
        hadBooks.add(book);
    }

    public void back(Book book){
        hadBooks.remove(book);
        removeFromTable(book);
    }

    public void saveBooks(){
        File file = new File("signedUser");
        try {
            PrintWriter out = new PrintWriter("signedUser.bak");
            Scanner in = new Scanner(file);
            while(in.hasNextLine()){
                String line = in.nextLine();
                String[] each = line.split(" ");
                if (each[0].equals(name)){
                    out.print(line+" <Books> ");
                    hadBooks.forEach(i -> out.print(i+" "));
                    out.println("</Books>");
                }else out.println(line);
            }
            in.close();
            out.close();
            Files.delete(Paths.get("signedUser"));
            new File("signedUser.bak").renameTo(file);
        } catch (IOException e) {
            System.out.println("文件写入出错");
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return name + " " + pass;
    }
}
