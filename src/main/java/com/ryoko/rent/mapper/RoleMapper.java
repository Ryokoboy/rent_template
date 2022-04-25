package kz.edu.astanait.diplomawork.mapper.security;

import com.ryoko.rent.dto.responseDto.RoleDtoResponse;
import com.ryoko.rent.model.Role;
import org.apache.logging.log4j.util.Strings;

public class RoleMapper {

    public static RoleDtoResponse roleToDto(Role role) {
        RoleDtoResponse roleDtoResponse = new RoleDtoResponse();
        roleDtoResponse.setId(role.getId());
        if(Strings.isNotBlank(role.getRoleName())) roleDtoResponse.setRoleName(role.getRoleName());
        return roleDtoResponse;
    }
}
