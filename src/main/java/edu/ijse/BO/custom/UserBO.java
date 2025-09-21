package edu.ijse.BO.custom;

import edu.ijse.BO.SuperBO;
import edu.ijse.dto.UserDto;

import java.util.List;

public interface UserBO extends SuperBO {

    boolean saveUser(UserDto user);
    boolean updateUser(UserDto user);
    boolean deleteUser(int id);
    List<UserDto> getAllUsers();
    UserDto login(String username, String password);

}
