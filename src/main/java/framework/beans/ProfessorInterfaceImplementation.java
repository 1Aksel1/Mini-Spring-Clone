package framework.beans;

import framework.annotations.Qualifier;
import framework.annotations.Service;

@Service
@Qualifier(value = "professorImpl")
public class ProfessorInterfaceImplementation implements ProfessorInterface{

    public ProfessorInterfaceImplementation() {

    }

    @Override
    public void run() {

    }

}
