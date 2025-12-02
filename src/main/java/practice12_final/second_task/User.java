package practice12_final.second_task;

import java.util.Objects;

public class User {
    private String name;
    private int age;
    private String email;

    public User(String name, int age, String email) throws InvalidUserException {
        if (UserValidator.nameValidation(name)) {
            if (UserValidator.ageValidation(age)) {
                if (UserValidator.emailValidation(email)) {
                    this.name = name;
                    this.age = age;
                    this.email = email;
                }
            }
        }
    }

    public void setName(String name) throws InvalidUserException {
        if (UserValidator.nameValidation(name)) {
            this.name = name;
        }
    }

    public void setAge(int age) throws InvalidUserException {
        if (UserValidator.ageValidation(age)) {
            this.age = age;
        }
    }

    public void setEmail(String email) throws InvalidUserException {
        if (UserValidator.emailValidation(email)) {
            this.email = email;
        }
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }
}
