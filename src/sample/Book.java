package sample;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

public class Book {
    StringProperty name;
    StringProperty author;
    StringProperty publish;
    IntegerProperty number;

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getAuthor() {
        return author.get();
    }

    public StringProperty authorProperty() {
        return author;
    }

    public void setAuthor(String author) {
        this.author.set(author);
    }

    public String getPublish() {
        return publish.get();
    }

    public StringProperty publishProperty() {
        return publish;
    }

    public void setPublish(String publish) {
        this.publish.set(publish);
    }

    public int getNumber() {
        return number.get();
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public ObjectProperty personLiteProperty() {
        HBox res = new HBox();
        Button borrow = new Button("还");
        Button look = new Button("申请延期");
        borrow.setOnAction(this::back);
        res.setSpacing(10);
        res.getChildren().addAll(borrow, look);
        return new SimpleObjectProperty<HBox>(res);
    }

    private void back(ActionEvent event){
        numberAdd();
        Controller.holder.back(this);
        FXtools.showTip("还书成功！",event);
    }

    public ObjectProperty allLiteProperty() {
        HBox res = new HBox();
        Button borrow = new Button("借");
        Button look = new Button("查看详细");
        borrow.setOnAction(this::borrowPane);
        res.setSpacing(10);
        res.getChildren().addAll(borrow, look);
        return new SimpleObjectProperty<HBox>(res);
    }

    public void setBorrowTime(LocalDate borrowTime) {
        this.borrowTime = borrowTime;
    }

    LocalDate borrowTime;
    int id;

    public Book(int id,String name, String author, String publish, Integer number) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
        this.author = new SimpleStringProperty(author);
        this.publish = new SimpleStringProperty(publish);
        this.number = new SimpleIntegerProperty(number);
    }

    public Book(int id,String name, String author, String publish, Integer number, LocalDate borrowTime) {
        this(id,name,author,publish,number);
        this.borrowTime = borrowTime;
    }

    @Override
    public String toString() {
        return id+","+name.get() + "," + author.get() + "," + publish.get() + "," + number.get() + "," + borrowTime;
    }

    private Button cancel;
    private CheckBox agree;
    private ChoiceBox<Integer> choice;
    private Button confirm;
    private DatePicker date;
    private Parent borrowRoot;
    private Stage borrowStage;

    private void lookupAll() {
        confirm = (Button) borrowRoot.lookup("#confirm");
        cancel = (Button) borrowRoot.lookup("#cancel");
        choice = (ChoiceBox<Integer>) borrowRoot.lookup("#number");
        agree = (CheckBox) borrowRoot.lookup("#agree");
        date = (DatePicker) borrowRoot.lookup("#date");
    }

    private void init() {
        lookupAll();
        initChoice();
        initConfirm();
        cancel.setOnAction(event -> borrowStage.hide());
    }

    private void numberSub(){
        number.setValue(number.get()-choice.getValue());
        if (number.get()==0) Controller.showAll.remove(this);
    }

    void numberAdd(){
        if (Controller.showAll.contains(this)){
            Book inlist = Controller.showAll.get(Controller.showAll.indexOf(this));
            inlist.number.setValue(inlist.number.get()+number.get());
        }else Controller.showAll.add(this);
    }

    public void initConfirm() {
        confirm.setOnAction(event -> {
            if (!agree.isSelected()){
                FXtools.showTip("请同意协议！",agree);
                return;
            }
            numberSub();
            Controller.holder.borrow(new Book(id,name.get(),author.get(),publish.get(),choice.getValue(),date.getValue()));
            FXtools.showTip("借书成功！",jumpReason);
            borrowStage.hide();
        });
    }

    private void initChoice(){
        ObservableList<Integer> allowNumber = FXCollections.observableArrayList();
        for (int i=number.get();i>0;i--) {
            allowNumber.add(i);
            choice.setValue(i);
        }
        choice.setItems(allowNumber);
    }

    private ActionEvent jumpReason;
    private void borrowPane(ActionEvent event) {
        jumpReason = event;
        borrowStage = new Stage();
        borrowStage.setTitle("借书-《" + name.get()+"》");
        try {
            borrowRoot = FXMLLoader.load(getClass().getResource("borrowPane.fxml"));
            borrowStage.setScene(new Scene(borrowRoot));
            borrowStage.show();
            init();
        } catch (IOException e) {
            FXtools.showTip("界面丢失", event);
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (!name.get().equals(book.name.get())) return false;
        if (!author.get().equals(book.author.get())) return false;
        return publish.get().equals(book.publish.get());
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + publish.hashCode();
        return result;
    }
}
