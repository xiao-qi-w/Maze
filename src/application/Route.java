package application;

/**
 * @Author 郭小柒w
 * @Date 2022/6/24 17:31
 * @Description 路径信息类，包括 当前坐标 和 上一坐标的指针
 **/
public class Route {
    private int x;
    private int y;
    private Route pre;

    public Route() {}

    public Route(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Route(int x, int y, Route pre) {
        this.x = x;
        this.y = y;
        this.pre = pre;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Route getPre() {
        return pre;
    }

    public void setPre(Route pre) {
        this.pre = pre;
    }

}