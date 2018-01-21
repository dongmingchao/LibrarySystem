package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class Login {
    public Button sign;
    private AnchorPane parentPane;
    public static Controller controller;
    public TextField account;
    public Button confirm;
    public PasswordField password;
    public ChoiceBox<String> identity;

    private ObservableList<String> identityList = FXCollections.observableArrayList();

    public void lookupAll() {
        identity = (ChoiceBox<String>) parentPane.lookup("#identity");
        account = (TextField) parentPane.lookup("#account");
        password = (PasswordField) parentPane.lookup("#password");
        confirm = (Button) parentPane.lookup("#confirm");
        sign = (Button) parentPane.lookup("#sign");
    }

    void init() {
        lookupAll();
        identityList.addAll("用户", "管理员");
        identity.setItems(identityList);
        confirm.setOnAction(this::toLogin);
        sign.setOnAction(this::toSign);
//        if (!backLastLogin("signedAdmin"))
//            backLastLogin("signedUser");
    }

//    private void backLastLogin() {
//        if (new File("signedAdmin").exists()) {
//            try {
//                Files.readAllLines(Paths.get("signedAdmin")).forEach(line -> {
//                    String[] each = line.split(" ");
//                    if (each.length > 2 && each[2].equals("Last-Login:")) {

//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else if (new File("signedUser").exists()) {
//            try {
//                Files.readAllLines(Paths.get("signedUser")).forEach(line -> {
//                    String[] each = line.split(" ");
//                    if (each[2].equals("Last-Login:")) {
//                        account.setText(each[0]);
//                        password.setText(each[1]);
//                        identity.setValue("用户");
//                    }
//                });
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    private boolean backLastLogin(String fileName) {
        boolean found = false;
        File file = new File(fileName);
        try {
            PrintWriter out = new PrintWriter(fileName + ".bak");
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] each = line.split(" ");
                if (each.length > 2 && each[2].equals("Last-Login:")) {
                    out.print(each[0] + " " + each[1]);
                    for (int i = 3; i < each.length; i++) {
                        out.print(" "+each[i]);
                    }
                    out.println();
                    account.setText(each[0]);
                    password.setText(each[1]);
                    if (fileName.equals("signedAdmin"))
                        identity.setValue("管理员");
                    else identity.setValue("用户");
                    found = true;
                } else out.println(line);
            }
            in.close();
            out.close();
            Files.delete(Paths.get(fileName));
            new File(fileName + ".bak").renameTo(file);
            return found;
        } catch (FileNotFoundException ignored) {
        } catch (IOException e) {
            System.out.println("文件使用出错");
            e.printStackTrace();
        }
        return false;
    }

    private void hideTip(Event event) {
        Tooltip tip = ((Control) event.getSource()).getTooltip();
        if (tip != null) tip.hide();
    }

    void setParentPane(AnchorPane parentPane) {
        this.parentPane = parentPane;
    }

    private boolean passNotInput(ActionEvent event) {
        boolean res = true;
        if (identity.getValue() == null) {
            FXtools.showTip("请选择您的身份", identity);
            res = false;
        }
        if (account.getText().equals("")) {
            FXtools.showTip("请输入您的账号", account);
            res = false;
        }
        if (password.getText().equals("")) {
            FXtools.showTip("请输入您的密码", password);
            res = false;
        }
        return !res;
    }

    private void toLogin(ActionEvent event) {
        if (passNotInput(event)) return;
        String level = "user";
        if (identity.getValue().equals("管理员")) level = "admin";
        User person = Store.isSignedSQL(account.getText(), level);
        if (person == null) {
            FXtools.showTip("您还没有注册!", event);
            return;
        } else {
            if (!person.pass.equals(password.getText())) {
                FXtools.showTip("密码错误", event);
                return;
            } else Controller.holder = person;
        }
//        rememberLogin(account.getText(), level);
        controller.app.mainStage.hide();
        try {
            SplitPane mainPane = FXMLLoader.load(getClass().getResource("main.fxml"));
            controller.app.root = mainPane;
            Stage main = new Stage();
            main.setTitle("图书管理系统");
            main.setScene(new Scene(mainPane));
            controller.app.mainStage = main;
            main.show();
            controller.my_username.set(account.getText());
            controller.my_identity.set(identity.getValue());
            controller.init();
        } catch (IOException e) {
            System.out.println("界面丢失");
            e.printStackTrace();
        }
    }

    private void rememberLogin(String name, String level) {
        String fileName;
        if (level.equals("admin")) fileName = "signedAdmin";
        else fileName = "signedUser";
        File file = new File(fileName);
        try {
            PrintWriter out = new PrintWriter(fileName + ".bak");
            Scanner in = new Scanner(file);
            while (in.hasNextLine()) {
                String line = in.nextLine();
                String[] each = line.split(" ");
                if (each[0].equals(name)) {
                    if (each.length >2 && !each[2].equals("Last-Login:")) {
                        out.print(each[0]+" "+each[1]+" Last-Login:");
                        for (int i = 2; i < each.length; i++) {
                            out.print(" "+each[i]);
                        }
                        out.println();
                    }else out.println(line);
                } else out.println(line);
            }
            in.close();
            out.close();
            Files.delete(Paths.get(fileName));
            new File(fileName + ".bak").renameTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void toSign(ActionEvent event) {
        if (passNotInput(event)) return;
        if (identity.getValue().equals("管理员")) {
            FXtools.showTip(Store.signSQL(account.getText(), password.getText(), "admin"), event);
        } else {
            FXtools.showTip(Store.signSQL(account.getText(), password.getText(), "user"), event);
        }
    }
}
