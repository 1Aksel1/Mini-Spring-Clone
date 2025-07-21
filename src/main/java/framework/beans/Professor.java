package framework.beans;

import framework.annotations.Autowired;
import framework.annotations.Bean;

@Bean(scope = "singleton")
public class Professor {

    private String name;
    private String surname;

    @Autowired(verbose = true)
    private Predmeti predmeti;


    public Professor() {

    }

    public Professor(String name, String surname) {
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
