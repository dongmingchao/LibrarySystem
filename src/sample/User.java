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
    int id;
    Controller controller;
    TreeTableView message;
    ArrayList<Book> hadBooks;
    TreeItem<Book> outOfDate = new TreeItem<Book>(new Book(-1,"已过期", "", "", 0));
    TreeItem<Book> inDate = new TreeItem<Book>(new Book(-1,"未过期", "", "", 0));
    TreeItem<Book> all = new TreeItem<Book>(new Book(-1,"全部", "", "", 0));
    String fileName = "signedUser";


    public void setController(Controller controller) {
        this.controller = controller;
    }

    public User(int id,String name, String pass, ArrayList<Book> hadBooks) {
        this.name = name;
        this.pass = pass;
        this.hadBooks = hadBooks;
        this.id = id;
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
        all.getValue().number.set(all.getValue().number.get()+book.number.get());
        if (book.borrowTime.isAfter(LocalDate.now())) {
            inDate.getChildren().add(new TreeItem<>(book));
            inDate.getValue().number.set(inDate.getValue().number.get()+book.number.get());
        } else {
            outOfDate.getChildren().add(new TreeItem<>(book));
            outOfDate.getValue().number.set(outOfDate.getValue().number.get()+book.number.get());
        }
    }

    private void removeFromTable(Book book){
        TreeItem<Book> what;
        if (book.borrowTime.isAfter(LocalDate.now())) {
            what = inDate.getChildren().stream().filter(each -> each.getValue().equals(book)).findFirst().orElse(null);
            if (what==null) return;
            inDate.getChildren().remove(what);
            inDate.getValue().number.set(inDate.getValue().number.get()-what.getValue().number.get());
        }
        else {
            what = outOfDate.getChildren().stream().filter(each -> each.getValue().equals(book)).findFirst().orElse(null);
            if (what==null) return;
            outOfDate.getChildren().remove(what);
            outOfDate.getValue().number.set(outOfDate.getValue().number.get()-what.getValue().number.get());
        }
        all.getValue().number.set(all.getValue().number.get()-what.getValue().number.get());
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

    public void saveBooksSQL(){
        if (hadBooks==null) return;
        StringBuilder fin = new StringBuilder();
        hadBooks.forEach(each -> {
            fin.append(each);
            fin.append(" ");
        });
        SQL.update("UPDATE admin SET books=? where id=?",new Object[]{fin.substring(0,fin.length()-1),id});
    }

    public void saveBooks(){
        File file = new File(fileName);
        try {
            PrintWriter out = new PrintWriter(fileName+".bak");
            Scanner in = new Scanner(file);
            while(in.hasNextLine()){
                String line = in.nextLine();
                ArrayList<String> each = new ArrayList<>();
                each.addAll(Arrays.asList(line.split(" ")));
                if (each.get(0).equals(name)){
                    if (each.indexOf("<Books>")==-1){
                        out.print(line+" ");
                    }else {
                        for (int i = 0; i < each.indexOf("<Books>"); i++) {
                            out.print(each.get(i) + " ");
                        }
                    }
                    out.print("<Books> ");
                    hadBooks.forEach(i -> out.print(i+" "));
                    out.println("</Books>");
                }else out.println(line);
            }
            in.close();
            out.close();
            Files.delete(Paths.get(fileName));
            new File(fileName+".bak").renameTo(file);
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
