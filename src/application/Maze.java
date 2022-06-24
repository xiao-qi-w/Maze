package application;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * @Author 郭小柒w
 * @Date 2022/6/24 17:26
 * @Description 迷宫类，包含迷宫的创建、寻路、寻优和遍历等功能
 **/
public class Maze {
    public static final int WALL = 0;  // 墙
    public static final int ROUTE = 1; // 路
    public static final int RIGHT = 2; // 正确的路
    private Random random;
    // 迷宫边长
    private int L;
    // 迷宫复杂程度
    private int rank;
    // 出口
    private int inX, inY, outX, outY;
    // 迷宫地图数据
    private int[][] map;
    // 方向数组
    private int[][] dirs;
    // 路径信息栈
    private ArrayList<LinkedList<Route>> stacks;

    // 构造函数
    public Maze(int L, int rank) {
        this.L = L;
        this.rank = rank;
        this.inX = 2;
        this.inY = 1;
        this.outX = L - 3;
        this.outY = L - 2;
        this.random = new Random();
        this.dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    }

    // Getter and Setter
    public int getInX() {
        return inX;
    }

    public void setInX(int inX) {
        this.inX = inX;
    }

    public int getInY() {
        return inY;
    }

    public void setInY(int inY) {
        this.inY = inY;
    }

    public int getOutX() {
        return outX;
    }

    public void setOutX(int outX) {
        this.outX = outX;
    }

    public int getOutY() {
        return outY;
    }

    public void setOutY(int outY) {
        this.outY = outY;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public ArrayList<LinkedList<Route>> getStack() {
        return stacks;
    }

    public void setStack(ArrayList<LinkedList<Route>> stacks) {
        this.stacks = stacks;
    }

    // 修饰迷宫地图
    public void initMap() {
        //最外围层设为路径的原因，为了防止挖路时挖出边界，同时为了保护迷宫主体外的一圈墙体被挖穿
        for (int i = 0; i < L; i++) {
            map[i][0] = 1;
            map[0][i] = 1;
            map[i][L - 1] = 1;
            map[L - 1][i] = 1;
        }
        // 创造迷宫, (2, 2)为起点
        CreateMaze(inX, inY + 1);
        // 画迷宫的入口和出口
        for (int i = L - 3; i >= 0; i--) {
            if (map[i][L - 3] == 1) {
                map[i][L - 2] = 1;
                this.outX = i;
                break;
            }
        }
        map[inX][inY] = map[outX][outY] = 1;
        // 制造环路
        for (int i = 10; i < 31; i++) {
            map[i][10] = 1;
            map[10][i] = 1;
            map[i][30] = 1;
            map[30][i] = 1;
        }
        // 创建迷宫时会打乱方向顺序，这里还原方向数组
        dirs = new int[][]{{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    }

    // 构造迷宫地图
    public void CreateMaze(int x, int y) {
        map[x][y] = ROUTE;
        int i, j;
        // 随机打乱方向顺序
        for (i = 0; i < 4; i++) {
            int r = random.nextInt(4);
            int temp = dirs[0][0];
            dirs[0][0] = dirs[r][0];
            dirs[r][0] = temp;
            temp = dirs[0][1];
            dirs[0][1] = dirs[r][1];
            dirs[r][1] = temp;
        }
        //向四个方向开挖
        for (i = 0; i < 4; i++) {
            int dx = x;
            int dy = y;
            //控制挖的距离，由rank来调整大小
            int range = 1 + random.nextInt(rank);
            while (range > 0) {
                //计算出将要访问到的坐标
                dx += dirs[i][0];
                dy += dirs[i][1];
                //排除掉回头路
                if (map[dx][dy] == ROUTE) {
                    break;
                }
                //判断是否挖穿路径
                int count = 0, k;
                for (j = dx - 1; j < dx + 2; j++) {
                    for (k = dy - 1; k < dy + 2; k++) {
                        //abs(j - dx) + abs(k - dy) == 1 确保只判断九宫格的四个特定位置
                        if (Math.abs(j - dx) + Math.abs(k - dy) == 1 && map[j][k] == ROUTE) {
                            count++;
                        }
                    }
                }
                //count大于1表明墙体会被挖穿，停止
                if (count > 1)
                    break;
                //确保不会挖穿时，前进
                range -= 1;
                map[dx][dy] = ROUTE;
            }
            //没有挖穿危险，以此为节点递归
            if (range <= 0) {
                CreateMaze(dx, dy);
            }
        }
    }

    // DFS寻找可行路径
    public void findWay(boolean[][] visit, int x, int y) {
        for (int k = 0; k < 4; ++k) {
            int nx = x + dirs[k][0];
            int ny = y + dirs[k][1];
            if (nx < 2 || nx > L - 3 || ny < 1 || ny > L - 2 || visit[nx][ny] || map[nx][ny] != ROUTE)
                continue;
            //来到新位置后, 进行标记
            map[nx][ny] = RIGHT;
            visit[nx][ny] = true;
            if (nx == outX && ny == outY) {
                //走到出口则结束搜索, 记录路径并返回
                LinkedList<Route> stack = new LinkedList<>();
                for (int i = 0; i < L; ++i) {
                    for (int j = 0; j < L; ++j) {
                        if (map[i][j] > 1)
                            stack.push(new Route(i, j));
                    }
                }
                stacks.add(stack);
                return;
            } else {
                //否则进行下一层递归
                findWay(visit, nx, ny);
            }
            // 不正确的路径需要还原
            map[nx][ny] = ROUTE;
        }
    }

    // BFS寻找最优路径
    public void findBestWay() {
        // 辅助队列
        LinkedList<Route> queue = new LinkedList<>();
        // 放入起点
        queue.offer(new Route(inX, inY));
        // 访问标记，用于判断当前坐标是否曾走到过
        boolean[][] visit = new boolean[L][L];
        visit[inX][inY] = true;
        // 队列不为空 且 未找到终点
        while (!queue.isEmpty() && !visit[outX][outY]) {
            Route route = queue.poll();
            int cx = route.getX(), cy = route.getY();
            // 继续寻找
            for (int i = 0; i < 4; i++) {
                // 计算将要到达的坐标
                int nx = cx + dirs[i][0];
                int ny = cy + dirs[i][1];
                // 判断可行性
                if (nx > 1 && nx < L - 2 && ny > 0 && ny < L - 1 && map[nx][ny] == ROUTE && !visit[nx][ny]) {
                    visit[nx][ny] = true;
                    Route next = new Route(nx, ny, route);
                    queue.offer(next);
                    // 找到终点
                    if (nx == outX && ny == outY) {
                        LinkedList<Route> stack = new LinkedList<>();
                        for (Route p = next; p != null; p = p.getPre()) {
                            stack.push(p);
                        }
                        stacks.add(stack);
                        break;
                    }
                }
            }
        }
    }

    // DFS遍历全部可行路径
    public void findAllWay(int x, int y) {
        for (int k = 0; k < 4; ++k) {
            int nx = x + dirs[k][0];
            int ny = y + dirs[k][1];
            if (nx < 2 || nx > L - 3 || ny < 1 || ny > L - 2 || map[nx][ny] != ROUTE)
                continue;
            //来到新位置后,设置当前值为可行路径
            map[nx][ny] = RIGHT;
            if (nx == outX && ny == outY) {
                //走到出口则结束搜索，记录路径并返回
                LinkedList<Route> stack = new LinkedList<>();
                for (int i = 0; i < L; ++i) {
                    for (int j = 0; j < L; ++j) {
                        if (map[i][j] > 1)
                            stack.push(new Route(i, j));
                    }
                }
                stacks.add(stack);
            } else {
                //否则进行下一层递归
                findAllWay(nx, ny);
            }
            map[nx][ny] = ROUTE;
        }
    }
}