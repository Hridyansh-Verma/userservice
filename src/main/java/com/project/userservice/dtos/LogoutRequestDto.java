package com.project.userservice.dtos;

import com.project.userservice.models.Token;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.TableOwner;

@Getter
@Setter
public class LogoutRequestDto {
    private String token;
}
