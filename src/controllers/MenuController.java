package controllers;

import application.Maze;
import application.Route;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * @Author 郭小柒w
 * @Date 2022/6/24 17:26
 * @Description 迷宫功能主界面逻辑控制
 **/
public class MenuController {
    public static final int AUTO = 0;   // 自动生成
    public static final int MANUAL = 1; // 手动生成
    @FXML
    private GridPane grid;          // 网格布局
    @FXML
    private Label explain, in, out; // 栈内方向解释、出入口标签
    @FXML
    private TextArea stack_info;    // 栈内信息文本域

    private int L;    // GridPane 或 迷宫 边长
    private Maze maze;// 迷宫对象
    private int[][] map;  // 迷宫地图数据
    private Random random;// 用于RGB生成的随机数
    private ObservableList<Node> rects;// 获取GridPane包含的网格元素集合
    private ArrayList<LinkedList<Route>> stacks;// 路径信息栈集合

    /**
     * 还原按钮点击事件
     */
    public void onRestoreClick() {
        restore();
    }

    /**
     * 遍历按钮点击事件
     */
    public void onAllClick() {
        // 清空之前的信息
        stacks.clear();
        // 遍历所有可行路径
        map[2][1] = 2;
        maze.findAllWay(maze.getInX(), maze.getInY());
        final IntegerProperty i = new SimpleIntegerProperty(0);
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.millis(2000),
                        event -> {
                            // 每隔2秒显示一条正确路径
                            printRoute(i.get());
                            // 每隔1.5秒就还原地图，准备展示下一条路径
                            PauseTransition pause = new PauseTransition(Duration.millis(1500));
                            pause.setOnFinished(e -> restore());
                            pause.play();
                            i.set(i.get() + 1);
                        }
                )
        );
        timeline.setCycleCount(stacks.size());
        timeline.play();
    }

    /**
     * 寻路按钮点击事件
     */
    public void onSearchClick() {
        // 清空之前的信息
        stacks.clear();
        // 寻路
        boolean[][] visit = new boolean[L][L];
        visit[maze.getInX()][maze.getInY()] = true;
        maze.findWay(visit, maze.getInX(), maze.getInY());
        // 显示正确路径
        printRoute(0);
    }

    /**
     * 寻优按钮点击事件
     */
    public void onBestClick() {
        // 清空之前的信息
        stacks.clear();
        // 寻优
        maze.findBestWay();
        // 显示正确路径
        printRoute(0);
    }


    /**
     * 界面初始化
     *
     * @param map   迷宫基础地图
     * @param type  生成方式
     * @param inout 出入口坐标
     */
    public void initialize(int[][] map, int type, Route[] inout) {
        // 说明栈的方向
        explain.setText("栈\n顶\n\n/\\\n |\n |\n |\n |\n |\n |\n |\n |\n |\n\n栈\n底");
        this.map = map;
        this.L = map.length;
        this.random = new Random();
        maze = new Maze(L, 5);
        // 地图赋值并根据生成方式创建迷宫
        maze.setMap(map);
        if (type == AUTO) {
            maze.initMap();
        } else {
            maze.setInX(inout[0].getX());
            maze.setInY(inout[0].getY());
            maze.setOutX(inout[1].getX());
            maze.setOutY(inout[1].getY());
        }
        // 展示出入口坐标
        in.setText("入口: (" + maze.getInX() + ", " + maze.getInY() + ")");
        out.setText("出口: (" + maze.getOutX() + ", " + maze.getOutY() + ")");
        // 栈赋值
        this.stacks = new ArrayList<>();
        maze.setStack(stacks);
        // 填充图形
        for (int i = 0; i < L; ++i) {
            for (int j = 0; j < L; ++j) {
                Rectangle rect = new Rectangle();
                rect.setHeight(10);
                rect.setWidth(10);
                if (map[i][j] == Maze.WALL) {
                    // 黑色为墙
                    rect.setFill(Color.BLACK);
                } else {
                    map[i][j] = 1;
                    // 白色为路
                    rect.setFill(Color.WHITE);
                }
                grid.add(rect, j, i);
            }
        }
        this.rects = grid.getChildren();
    }

    /**
     * 重新设置地图颜色
     */
    public void restore() {
        for (int i = 0; i < L; ++i) {
            for (int j = 0; j < L; ++j) {
                Rectangle rect = (Rectangle) rects.get(i * L + j);
                if (map[i][j] == Maze.WALL) {
                    // 黑色为墙
                    rect.setFill(Color.BLACK);
                } else {
                    map[i][j] = 1;
                    // 白色为路
                    rect.setFill(Color.WHITE);
                }
            }
        }
    }

    /**
     * 绘制给定的第i条路径
     *
     * @param i
     */
    public void printRoute(int i) {
        // 随机生成一种颜色
        Color color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        // 栈内数据信息缓存文本
        StringBuffer buffer = new StringBuffer();
        // 获取要绘制的路径信息栈
        LinkedList<Route> stack = stacks.get(i);
        while (!stack.isEmpty()) {
            Route route = stack.pop();
            int x = route.getX(), y = route.getY();
            // 记录栈内坐标信息
            buffer.append('(').append(x).append(',').append(y).append(')').append('\n');
            // 坐标（x, y）处的矩形颜色填充为color
            Rectangle rect = (Rectangle) rects.get(x * L + y);
            rect.setFill(color);
        }
        stack_info.setText(buffer.toString());
    }
}