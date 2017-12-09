package sample;

import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

public class FXtools {
    public static void showTip(String tipText, ButtonBase control) {
        if (control.getTooltip()!=null && control.getTooltip().isShowing()) return;
        Tooltip tip = new Tooltip(tipText);
        control.setTooltip(tip);
        control.setOnAction(e -> tip.hide());
        Point2D p = control.localToScene(control.getWidth() + 10, control.getHeight() - 7);
        tip.show(control.getScene().getWindow(), p.getX() + control.getScene().getWindow().getX(), p.getY() + control.getScene().getWindow().getY());
    }

    public static void showTip(String tipText, ChoiceBox control) {
        if (control.getTooltip()!=null && control.getTooltip().isShowing()) return;
        Tooltip tip = new Tooltip(tipText);
        control.setTooltip(tip);
        control.setOnAction(e -> tip.hide());
        Point2D p = control.localToScene(control.getWidth() + 10, control.getHeight() - 7);
        tip.show(control.getScene().getWindow(), p.getX() + control.getScene().getWindow().getX(), p.getY() + control.getScene().getWindow().getY());
    }

    public static void showTip(String tipText, TextField control) {
        if (control.getTooltip()!=null && control.getTooltip().isShowing()) return;
        Tooltip tip = new Tooltip(tipText);
        control.setTooltip(tip);
        control.setOnKeyTyped(e -> tip.hide());
        Point2D p = control.localToScene(control.getWidth() + 10, control.getHeight() - 7);
        tip.show(control.getScene().getWindow(), p.getX() + control.getScene().getWindow().getX(), p.getY() + control.getScene().getWindow().getY());
    }

    public static void showTip(String tipText, ActionEvent event) {
        Tooltip tip = new Tooltip(tipText);
        tip.setAutoHide(true);
        Control control = (Control) event.getSource();
        if (control.getTooltip()!=null && control.getTooltip().isShowing()) return;
        control.setTooltip(tip);
        tip.show(control.getScene().getWindow());
        Point2D p = control.localToScene(0, control.getHeight() * 2);
        tip.setAnchorX(p.getX() + control.getWidth() / 2 - tip.getWidth() / 2 + 9 + control.getScene().getWindow().getX());
        tip.setAnchorY(p.getY() + control.getScene().getWindow().getY());
    }

    public static void setFav(String url, ImageView imageView) {
        try {
            imageView.setImage(new Image(new FileInputStream(Paths.get(url).toFile())));
        } catch (FileNotFoundException e) {
            System.out.println("文件未找到");
            e.printStackTrace();
        }
    }
}
