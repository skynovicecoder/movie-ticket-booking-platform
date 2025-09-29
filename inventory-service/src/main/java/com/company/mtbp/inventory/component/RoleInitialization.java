package com.company.mtbp.inventory.component;

import com.company.mtbp.inventory.entity.Role;
import com.company.mtbp.inventory.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class RoleInitialization {

    private final RoleRepository roleRepository;

    @PostConstruct
    public void initRoles() {
        if (roleRepository.count() == 0) {
            Role customerRole = Role.builder().name("ADMIN").build();
            Role adminRole = Role.builder().name("CUSTOMER").build();
            Role theatreOwnerRole = Role.builder().name("THEATRE_OWNER").build();

            List<Role> roles = List.of(customerRole, adminRole, theatreOwnerRole);
            roleRepository.saveAll(roles);

            log.info("Default roles inserted: CUSTOMER, ADMIN, THEATRE_OWNER");
        }
    }
}