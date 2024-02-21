package com.project.userservice.dtos;


import com.project.userservice.models.Role;
import com.project.userservice.models.User;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter

public class UserDto {
    private String name;
    private String email;
    private List<Role> roles;
    private boolean isEmailVerified;
    public static UserDto from(User user) {
        if (user == null) return null;

        UserDto userDto = new UserDto();
        userDto.email = user.getEmail();
        userDto.name = user.getName();
        userDto.roles =user.getRoleList();
        userDto.isEmailVerified = user.isEmailVerified();

        return userDto;
    }
}
