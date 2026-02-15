package practice_2;

public class Circle {

    double radius;

    Circle(double radius){
        this.radius = radius;
    }

    public double getRadius(){
        return this.radius;
    }

    public void setRadius(double radius){
        this.radius = radius;
    }

    public double calculateArea(){
        return  Math.PI * (this.radius * this.radius);
    }

    public double calculateCircumference(){
        return 2 * Math.PI * this.radius;
    }
}
