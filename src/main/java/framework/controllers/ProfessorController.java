package framework.controllers;

import framework.annotations.*;
import framework.beans.Professor;
import framework.beans.ProfessorInterface;
import framework.response.JsonResponse;
import framework.response.RequestNotValidResponse;
import framework.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class ProfessorController {

    private static List<Professor> professors = new ArrayList<>();

    @Autowired(verbose = true)
    private Professor professor;

    @Qualifier(value = "professorImpl")
    @Autowired(verbose = true)
    private ProfessorInterface professorInterface;

    static {
        professors.add(new Professor("milan","milanovic"));
        professors.add(new Professor("janko","jankovic"));
        professors.add(new Professor("ranko","rankovic"));
    }


    @GET
    @Path(path = "/professors")
    public Response getProfessors(){

        return new JsonResponse(professors);

    }

    @POST
    @Path(path = "/professors")
    public Response createProfessor(Map<String,String> parameters){

        String name = parameters.get("name");
        String surname = parameters.get("surname");

        if(parameters.isEmpty() || (name == null || name == "") || (surname == null || surname == "") )
            return new RequestNotValidResponse();

        Professor newProfessor = new Professor(name,surname);
        professors.add(newProfessor);

        return new JsonResponse(newProfessor);

    }

    @GET
    @Path(path = "/firstProfessor")
    public Response getFirstProfessor(){

        return new JsonResponse(professors.get(0));

    }


}
