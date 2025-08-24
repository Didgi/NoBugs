package practice_2;

public class Point {

    double x;
    double y;

    Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    public void setX(double x){
        this.x = x;
    }

    public void print(){
        System.out.println("Значение коориданты Х " + this.x + " и значение координаты Y " + this.y);
    }
}
