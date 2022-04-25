package com.ryoko.rent.repository;

import com.ryoko.rent.model.PinCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PinCodeRepository extends JpaRepository<PinCode, Long> {
    Optional<PinCode> findByPinCodeAndUserEmail(Integer pinCode, String email);
    Optional<PinCode> findByUserEmail(String email);
}
