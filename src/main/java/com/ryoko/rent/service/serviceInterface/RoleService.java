package com.ryoko.rent.service.serviceInterface;

import com.ryoko.rent.model.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> getById(Long id);

    Optional<Role> getByName(String roleName);

    Role getByIdThrowException(Long id);

    Role getByNameThrowException(String name);
}
