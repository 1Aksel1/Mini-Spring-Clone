package framework.beans;

import framework.annotations.Autowired;
import framework.annotations.Bean;
import framework.annotations.Qualifier;

@Bean(scope = "singleton")
@Qualifier(value = "StudentInterfaceImplementation")
public class StudentInterfaceImplementation implements StudentInterface{

    @Autowired(verbose = true)
    private StudentskiServis studentskiServis;

    public StudentInterfaceImplementation() {

    }

    @Override
    public void run() {

    }

}
