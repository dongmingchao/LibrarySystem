package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class Login {
    private AnchorPane parentPane;
    public Controller controller;
    public TextField account;
    public Button confirm;
    public PasswordField password;
    public ChoiceBox<String> identity;
    public String user;
    public String passwd;

    private ObservableList<String> identityList = FXCollections.observableArrayList();

    public void lookupAll(){
        identity = (ChoiceBox<String>)parentPane.lookup("#identity");
        account = (TextField) parentPane.lookup("#account");
        password = (PasswordField) parentPane.lookup("#password");
        confirm = (Button) parentPane.lookup("#confirm");
    }

    void init(){
        lookupAll();
        identityList.addAll("用户","管理员");
        System.out.println(identity);
        identity.setItems(identityList);
        confirm.setOnAction(this::toLogin);
        identity.setOnAction(this::hideTip);
        account.setOnAction(this::hideTip);
        password.setOnAction(this::hideTip);
    }

    private void hideTip(ActionEvent event){
        Tooltip tip = ((ChoiceBox) event.getSource()).getTooltip();
        if (tip!=null) tip.hide();
    }

    void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }

    private void toLogin(ActionEvent event){
        if (identity.getValue()==null) {
            showTip("请选择您的身份",identity);
            return;
        }
        if (account.getText().equals("")) {
            showTip("请输入您的账号",account);
            return;
        }
        if (password.getText().equals("")) {
            showTip("请输入您的密码",password);
            return;
        }
        user=account.getText();
        passwd = password.getText();
        System.out.println(user+","+passwd+identity.getValue());
        controller.app.mainStage.hide();
    }

    private void showTip(String tipText, Control control){
        Tooltip tip = new Tooltip(tipText);
        control.setTooltip(tip);
        Point2D p = control.localToScene(control.getWidth()+10, control.getHeight()-7);
        tip.show(controller.app.mainStage, p.getX()+control.getScene().getWindow().getX(),p.getY()+control.getScene().getWindow().getY());
    }
}
