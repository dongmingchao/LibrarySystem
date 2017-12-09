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
    public void saveBooks(){
        File file = new File("signedAdmin");
        try {
            PrintWriter out = new PrintWriter("signedAdmin.bak");
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
            Files.delete(Paths.get("signedAdmin"));
            new File("signedAdmin.bak").renameTo(file);
        } catch (IOException e) {
            System.out.println("文件写入出错");
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return name + " " + pass ;
    }
}
