import org.postgresql.core.SqlCommand;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {
    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(
                System.getenv("DB_URL"),
                System.getenv("DB_USER"),
                System.getenv("DB_PASSWORD")
        );
    }

    public int addStudent(Student student) throws SQLException{
        Connection con=getConnection();
        String sql="""
                     INSERT INTO students (name,marks)
                     VALUES (?,?)
                     RETURNING id""";
        PreparedStatement ps=con.prepareStatement(sql);
        ps.setString(1,student.getName());
        ps.setDouble(2,student.getMarks());

        try(ResultSet rs=ps.executeQuery()){
            if(rs.next()){
                return rs.getInt("id");
            }
        }
        throw new SQLException("Student insert returned no id");
    }

    public List<Student> getAllStudents() throws SQLException{
        List<Student> students=new ArrayList<>();
        String sql="SELECT id,name,marks FROM students";

        try(Connection con=getConnection();
            Statement stmt=con.createStatement();
            ResultSet rs=stmt.executeQuery(sql)){
            while(rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("name");
                double marks=rs.getDouble("marks");

                Student student=new Student(id,name,marks);
                students.add(student);
            }
        }catch (SQLException e){
            throw new SQLException("Something broke!");
        }

        return students;
    }

    public Student getStudentById(int id) throws SQLException{
        String sql="SELECT id,name,marks FROM students WHERE id=?";

        try(Connection con=getConnection();
            PreparedStatement ps=con.prepareStatement(sql);){
            ps.setInt(1,id);

            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    int id1=rs.getInt("id");
                    String name=rs.getString("name");
                    double marks=rs.getDouble("marks");

                    return new Student(id1,name,marks);
                }
            }
        }
        return null;
    }

    public void updateMarks(int id,double newMarks) throws SQLException{

        if(newMarks<0 || newMarks>100){
            throw new IllegalArgumentException("Marks should be between 0 and 100");
        }

        String sql= """
                
                UPDATE students
                SET marks=?
                WHERE id=?""";
        try(Connection con=getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
            ps.setDouble(1,newMarks);
            ps.setInt(2,id);

            int rowsAffected=ps.executeUpdate();

            if(rowsAffected==0){
                throw new IllegalArgumentException("No student exists with id:"+id);
            }

        }
    }

    public void deleteStudent(int id) throws SQLException{
        String sql= """
                DELETE FROM students
                WHERE id=?""";

        try(Connection con=getConnection();
            PreparedStatement ps=con.prepareStatement(sql)){
            ps.setInt(1,id);

            int rowsAffected=ps.executeUpdate();

            if(rowsAffected==0){
                throw new IllegalArgumentException("No student exists with id:"+id);
            }
        }
    }
}
