package com.project.userservice.models;

import ch.qos.logback.core.joran.action.BaseModelAction;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseModel {
    private String name;
    private String email;
    private String hashedPassword;
    @ManyToMany
    private List<Role> roleList;
    private boolean isEmailVerified;
}
