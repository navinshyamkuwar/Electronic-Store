package com.pirates.electronic.store.services;

import com.pirates.electronic.store.dtos.PageableResponse;
import com.pirates.electronic.store.dtos.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserDto userDto);
    UserDto updateUser(UserDto userDto, String userId);
    void deleteUser(String userId);
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortyDir);
    UserDto getUserById(String userId);
    UserDto getUserByEmail(String email);
    List<UserDto> searchUser(String keyword);

}
