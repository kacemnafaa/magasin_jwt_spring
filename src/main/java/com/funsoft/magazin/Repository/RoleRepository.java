package com.funsoft.magazin.Repository;

import com.funsoft.magazin.model.Erole;
import com.funsoft.magazin.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface  RoleRepository extends JpaRepository<Role,Long>{
    Optional<Role> findByName(Erole name);
}
