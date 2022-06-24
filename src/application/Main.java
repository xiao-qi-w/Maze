package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;

/**
 * @Author 郭小柒w
 * @Date 2022/6/24 17:26
 * @Description 程序运行入口，主类
 **/
public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			// 加载布局文件
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(getClass().getResource("/fxmls/start.fxml"));
			Parent root = loader.load();
			// 设置窗体内容和标题
			Scene scene = new Scene(root);
			stage.setResizable(false);
			stage.setTitle("迷宫鼠");
			stage.getIcons().add(new Image("/images/maze.png"));
			stage.setScene(scene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
