package framework.beans;

import framework.annotations.Autowired;
import framework.annotations.Bean;
import framework.annotations.Qualifier;

@Bean(scope = "prototype")
public class Student {

    private String name;
    private String surname;

    @Autowired(verbose = true)
    private Predmeti predmeti;


    @Qualifier(value = "StudentInterfaceImplementation")
    @Autowired(verbose = true)
    private StudentInterface studentInterface;


    public Student() {

    }

    public Student(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

}
