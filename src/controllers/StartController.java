package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @Author 郭小柒w
 * @Date 2022/6/24 17:26
 * @Description 开始界面逻辑控制
 **/
public class StartController {
    @FXML
    private AnchorPane rootStage; // 父窗口面板

    /**
     * 手动生成按钮点击事件
     */
    public void onManualClick() {
        try {
            // 加载手动输入界面布局文件
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/input.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            // 设置stage
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.getIcons().add(new Image("/images/maze.png"));
            stage.setScene(scene);
            // 设置父窗体
            stage.initOwner(rootStage.getScene().getWindow());
            // 设置除当前窗体外其他窗体均不可编辑
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 自动生成按钮点击事件
     */
    public void onAutoClick() {
        try {
            // 加载迷宫主界面布局文件
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            // 获取Controller
            MenuController controller = loader.getController();
            // 进行迷宫初始化操作
            controller.initialize(new int[42][42], MenuController.AUTO, null);
            // 设置Stage
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.getIcons().add(new Image("/images/maze.png"));
            stage.setScene(scene);
            // 设置父窗体
            stage.initOwner(rootStage.getScene().getWindow());
            // 设置除当前窗体外其他窗体均不可编辑
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        // TODO: 如有需要初始化的内容，请在此方法内完成
    }
}
