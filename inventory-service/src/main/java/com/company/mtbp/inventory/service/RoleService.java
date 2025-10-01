package com.company.mtbp.inventory.service;

import com.company.mtbp.inventory.dto.RoleDTO;
import com.company.mtbp.inventory.entity.Role;
import com.company.mtbp.inventory.exception.BadRequestException;
import com.company.mtbp.inventory.exception.ResourceNotFoundException;
import com.company.mtbp.inventory.pagedto.PageResponse;
import com.company.mtbp.inventory.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public RoleDTO createRole(RoleDTO roleDTO) {
        if (roleRepository.findByName(roleDTO.getName()).isPresent()) {
            throw new BadRequestException("Role already exists: " + roleDTO.getName());
        }
        Role role = Role.builder().name(roleDTO.getName()).build();
        Role saved = roleRepository.save(role);
        return toDTO(saved);
    }

    public RoleDTO getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + id));
        return toDTO(role);
    }

    public PageResponse<RoleDTO> getAllRoles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Role> rolePage = roleRepository.findAll(pageable);

        List<RoleDTO> dtos = rolePage.getContent()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                dtos,
                rolePage.getNumber(),
                rolePage.getSize(),
                rolePage.getTotalElements(),
                rolePage.getTotalPages(),
                rolePage.isLast()
        );
    }

    public RoleDTO patchRole(Long id, Map<String, Object> updates) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + id));

        updates.forEach((key, value) -> {
            try {
                Field field = Role.class.getDeclaredField(key);
                field.setAccessible(true);
                field.set(role, value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new BadRequestException("Invalid field: " + key);
            }
        });

        return toDTO(roleRepository.save(role));
    }

    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Role not found with id " + id);
        }
        roleRepository.deleteById(id);
    }

    private RoleDTO toDTO(Role role) {
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}
