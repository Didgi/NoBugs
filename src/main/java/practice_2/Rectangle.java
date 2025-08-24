package practice_2;

public class Rectangle {
    double width;
    double height;

    Rectangle(double widht, double height){
        this.width = widht;
        this.height = height;
    }

    public double getWidth(){
        return this.width;
    }

    public double getHeight(){
        return this.height;
    }

    public void setWidth(double newWidth) {
        this.width = newWidth;
    }

    public double calculateArea(){
        return this.width * this.height;
    }
}
