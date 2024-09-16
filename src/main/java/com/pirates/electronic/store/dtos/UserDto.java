package com.pirates.electronic.store.dtos;

import com.pirates.electronic.store.validate.ImageNameValid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String userId;

    @Size(min=3, max=30, message = "Invalid Name!!")
    private String name;

    //@Email(message = "Invalid User Email!!")
    @Pattern(regexp = "\\w+([-+.']\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*", message = "Invalid User Email!!")
    @NotBlank(message = "Email is Required!!")
    private String email;

    @NotBlank(message = "Password is Required!!")
    private String password;

    @Size(min = 3, max = 6, message = "Invalid Gender Info!!")
    private String gender;

    @NotBlank(message = "Write Something about Yourself!!")
    private String about;

    @ImageNameValid
    private String imageName;
}
