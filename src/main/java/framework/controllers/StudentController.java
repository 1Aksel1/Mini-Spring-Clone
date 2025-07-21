package framework.controllers;

import framework.annotations.*;
import framework.beans.Student;
import framework.response.JsonResponse;
import framework.response.RequestNotValidResponse;
import framework.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class StudentController {

    private static List<Student> students = new ArrayList<>();

    @Autowired(verbose = true)
    private Student student;

    static {
        students.add(new Student("pera","peric"));
        students.add(new Student("mika","mikic"));
        students.add(new Student("zika","zikic"));
    }

    @GET
    @Path(path = "/students")
    public Response getStudents(){

        return new JsonResponse(students);

    }

    @POST
    @Path(path = "/students")
    public Response createStudent(Map<String,String> parameters) {

        String name = parameters.get("name");
        String surname = parameters.get("surname");

        if(parameters.isEmpty() || (name == null || name == "") || (surname == null || surname == "") )
            return new RequestNotValidResponse();

        Student newStudent = new Student(name,surname);
        students.add(newStudent);

        return new JsonResponse(newStudent);

    }

    @GET
    @Path(path = "/firstStudent")
    public Response getFirstStudent(){

        return new JsonResponse(students.get(0));

    }


}
