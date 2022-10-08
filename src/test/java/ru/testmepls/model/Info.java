package ru.testmepls.model;

import java.util.List;

public class Info {
    public String name;
    public String surname;
    public int age;
    public boolean isActor;
    public List<String> address;
    public Mother mother;

    public static class Mother {
        public String name;
        public boolean isActress;
    }

}
