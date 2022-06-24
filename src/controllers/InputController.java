package controllers;

import application.Route;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * @Author 郭小柒w
 * @Date 2022/6/24 17:31
 * @Description 手动输入界面逻辑控制
 **/
public class InputController {
    @FXML
    private AnchorPane rootStage; // 父窗口面板
    @FXML
    private TextField ix, iy, ox, oy; // 出入口坐标文本框
    @FXML
    private Label row, col; // 行列坐标方向标签
    @FXML
    private TextArea edit;  // 迷宫输入文本域

    /**
     * 确认按钮点击事件
     */
    public void onConfirmClick() {
        try {
            // 获取输入的迷宫地图信息
            String text = edit.getText();
            int[][] map = new int[42][42];
            // 根据输入生成地图
            String[] rows = text.split("\n");
            for (int i = 0; i < rows.length; ++i) {
                for (int j = 0; j < rows[0].length(); ++j) {
                    char c = rows[i].charAt(j);
                    // 字符转换为整数
                    map[i][j] = c - '0';
                }
            }
            // 获取并创建出入口坐标
            Route[] inout = {
                    new Route(Integer.parseInt(ix.getText()), Integer.parseInt(iy.getText())),
                    new Route(Integer.parseInt(ox.getText()), Integer.parseInt(oy.getText()))
            };
            // 关闭输入窗口
            Stage parentStage = (Stage) rootStage.getScene().getWindow();
            parentStage.close();
            // 加载迷宫主界面布局文件
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/fxmls/menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            // 获取Controller
            MenuController controller = loader.getController();
            // 进行迷宫初始化操作
            controller.initialize(map, MenuController.MANUAL, inout);
            // 设置Stage
            Stage stage = new Stage();
            stage.setResizable(false);
            stage.getIcons().add(new Image("/images/maze.png"));
            stage.setScene(scene);
            // 设置除当前窗体外其他窗体均不可编辑
            stage.initModality(Modality.WINDOW_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 输入界面初始化
     */
    public void initialize() {
        // 横坐标
        row.setText("0\t1\t2\t3\t4\t5\t6\t ... \t37\t38\t39");
        // 纵坐标
        col.setText("0\n\n1\n\n2\n\n3\n\n4\n\n5\n.\n.\n.\n\n37\n\n38\n\n39");
    }
}
