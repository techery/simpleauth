package io.github.spoiq_auth_module.model;

import java.io.Serializable;

public class User implements Serializable{

    private final String email;
    private final String first_name;
    private final String last_name;
    private final String password;
    private final String password_confirmation;

    public User(String email, String first_name, String last_name, String password, String password_confirmation) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
        this.password = password;
        this.password_confirmation = password_confirmation;
    }
}
