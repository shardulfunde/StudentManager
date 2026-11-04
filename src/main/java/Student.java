
public class Student {
    private int id;
    private String name;
    private double marks;

    Student(int id,String name,double marks){
        this.id=id;
        this.name=name;
        setMarks(marks);

    }

    Student(String name,double marks){
        this.name=name;
        setMarks(marks);
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public double getMarks(){
        return this.marks;
    }

    public void setMarks(double marks){
        if(marks>=0 && marks<=100){
            this.marks=marks;
        }else{
            throw new IllegalArgumentException("Marks should be between 0 and 100");
        }
    }
}
