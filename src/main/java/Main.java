import java.sql.SQLException;

public class Main {

    public static void showHelp(){
        String help= """
                        Student Manager CLI
                        
                        Usage:
                          student-manager <command> [arguments]
                        
                        Available commands:
                        
                          list
                              Display all students.
                        
                          add --name <name> --marks <marks>
                              Add a new student.
                        
                          get <id>
                              Retrieve a student by ID.
                        
                          update <id> --marks <marks>
                              Update a student's marks.
                        
                          delete <id>
                              Delete a student by ID.
                        
                          help
                              Display this help message.
                        
                        Examples:
                          student-manager list
                          student-manager add --name "Rahul Funde" --marks 85.5
                          student-manager get 3
                          student-manager update 3 --marks 91
                          student-manager delete 3""";
        System.out.println(help);
    }

    public static void main(String[] args) throws SQLException {
        if(args.length==0){
            showHelp();
            return;
        }

        String command=args[0];
        StudentDAO dao=new StudentDAO();

        switch (command){
            case "list":
                for(Student student:dao.getAllStudents()){
                    System.out.println(
                            student.getId()+" | "+
                            student.getName()+" | "+
                            student.getMarks()
                    );
                }
                break;

            case "get":
                if(args.length<2){
                    System.out.println("Usage: student-manager get <id>");
                    return;
                }

                int id=Integer.parseInt(args[1]);
                Student student=dao.getStudentById(id);

                if(student==null){
                    System.out.println("Student not found: "+id);
                }else{
                    System.out.println("ID: "+student.getId()+" | "+
                            "Name: "+student.getName()+" | "+
                            "Marks: "+student.getMarks());
                }

                break;

            case "add":
                if(args.length!=5){
                    System.out.println("Usage: student-manager add --name <Student_Name> --marks <Student_Marks>");
                    return;
                }
                String name=null;
                Double marks=null;
                for(int i=1;i<args.length;i+=2){
                    switch (args[i]){
                        case "--name":
                            name=args[i+1];
                            break;

                        case "--marks":
                            marks=Double.parseDouble(args[i+1]);
                            break;

                        default:
                            System.out.println("Invalid Argument :"+args[i]);
                            return;
                    }

                }
                if(name!=null && marks!=null){
                    Student student1=new Student(name,marks);
                    int id1=dao.addStudent(student1);
                    System.out.println("Student added with id: "+id1);
                }else{
                    System.out.println("Invalid marks or name");
                }
                break;

            case "delete":
                if(args.length!=2){
                    System.out.println("Usage: student-manager delete <Student_id>");
                    return;
                }

                int id2=Integer.parseInt(args[1]);
                dao.deleteStudent(id2);
                System.out.println("Student deleted with ID: "+id2);
                break;

            case "update":
                if(args.length!=4){
                    System.out.println("Usage: student-manager update <Student_id> --marks <new_marks>");
                    return;
                }
                int id3=Integer.parseInt(args[1]);
                if(args[2].equals("--marks")){
                    double newMarks=Double.parseDouble(args[3]);
                    dao.updateMarks(id3,newMarks);
                    System.out.println("Updated marks of student with ID: "+id3+" to "+newMarks);
                }else{
                    System.out.println("Usage: java Main update <Student_id> --marks <new_marks>");
                    return;
                }
                break;

            case "help":
                showHelp();
                break;

            default:
                System.out.println("Unknown command: "+command);
                return;
        }
    }
}

