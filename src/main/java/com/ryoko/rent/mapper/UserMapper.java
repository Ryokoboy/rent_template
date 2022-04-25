package com.ryoko.rent.mapper;

import com.ryoko.rent.dto.responseDto.UserDtoResponse;
import com.ryoko.rent.model.User;
import org.apache.logging.log4j.util.Strings;
import kz.edu.astanait.diplomawork.mapper.security.RoleMapper;
import java.util.Objects;

public class UserMapper {

    public static UserDtoResponse userToDto(User user) {
        UserDtoResponse userDtoResponse = new UserDtoResponse();
        userDtoResponse.setId(user.getId());
        if(Strings.isNotBlank(user.getEmail())) userDtoResponse.setEmail(user.getEmail());
        if(Objects.nonNull(user.getRole())) userDtoResponse.setRole(RoleMapper.roleToDto(user.getRole()));
        if(Strings.isNotBlank(user.getName())) userDtoResponse.setName(user.getName());
        if(Strings.isNotBlank(user.getSurname())) userDtoResponse.setLastname(user.getSurname());
        if(Strings.isNotBlank(user.getPhone())) userDtoResponse.setPhone(user.getPhone());
        userDtoResponse.setActive(user.isActive());
        userDtoResponse.setLocked(user.isLocked());
        if(Objects.nonNull(user.getCreatedDate())) userDtoResponse.setCreatedDate(user.getCreatedDate());
        return userDtoResponse;
    }
}
