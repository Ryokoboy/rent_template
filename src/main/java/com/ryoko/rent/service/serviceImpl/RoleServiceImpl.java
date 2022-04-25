package com.ryoko.rent.service.serviceImpl;

import com.ryoko.rent.exception.ExceptionDescription;
import com.ryoko.rent.exception.domain.CustomNotFoundException;
import com.ryoko.rent.model.Role;
import com.ryoko.rent.repository.RoleRepository;
import com.ryoko.rent.service.serviceInterface.RoleService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Log4j2
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<Role> getById(Long id) {
        return this.roleRepository.findById(id);
    }

    @Override
    public Optional<Role> getByName(String roleName) {
        return this.roleRepository.findByRoleName(roleName);
    }

    @Override
    public Role getByIdThrowException(Long id) {
        return this.getById(id).orElseThrow(
                () -> new CustomNotFoundException(String.format(ExceptionDescription.CustomNotFoundException, "role", "id", id)));
    }

    @Override
    public Role getByNameThrowException(String name) {
        return this.getByName(name).orElseThrow(
                () -> new CustomNotFoundException(String.format(ExceptionDescription.CustomNotFoundException, "role", "name", name)));
    }
}
