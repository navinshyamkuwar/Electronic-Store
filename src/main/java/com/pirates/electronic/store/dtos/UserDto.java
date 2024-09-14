package com.pirates.electronic.store.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String userId;
    private String name;
    private String email;
    private String password;
    private String gender;
    private String about;
    private String imageName;
}
