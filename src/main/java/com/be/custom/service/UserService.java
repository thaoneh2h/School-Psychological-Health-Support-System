package com.be.custom.service;

import com.be.base.core.BaseService;
import com.be.base.dto.ServerResponse;
import com.be.custom.dto.request.SaveUserDto;
import com.be.custom.dto.response_api.UserResponseDto;
import com.be.custom.entity.user.UserEntity;
import com.be.custom.enums.Role;
import com.be.custom.repository.UserRepository;
import com.be.custom.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends BaseService<UserEntity, UserRepository> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final BCryptPasswordEncoder passwordEncoder;

    public Optional<UserEntity> validateUser(String username, String password) {
        Optional<UserEntity> userOpt = repository.findByUsernameAndIsDeletedFalse(username);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        UserEntity userEntity = userOpt.get();
        if (passwordEncoder.matches(password, userEntity.getPassword()) && userEntity.isActive()) {
            return userOpt;
        } else {
            return Optional.empty();
        }
    }

    public Page<UserEntity> getPageAdmin(String keyword, Role roleFilter, Pageable pageable) {
        List<Role> listRoleFilter;
        if (roleFilter == null) {
            listRoleFilter = List.of(Role.COMPANY_ADMIN, Role.COMPANY_STAFF);
        } else if (Role.COMPANY_ADMIN == roleFilter) {
            listRoleFilter = List.of(Role.COMPANY_ADMIN);
        } else {
            listRoleFilter = List.of(Role.COMPANY_STAFF);
        }

        Page<UserEntity> result = repository.getPageAdmin(keyword, listRoleFilter, pageable);
        setCreatorAndUpdater(result);
        return result;
    }

    public ServerResponse saveUser(SaveUserDto saveUserDto) {
        Optional<UserEntity> userToSaveOpt = fromSaveDto(saveUserDto);
        if (userToSaveOpt.isEmpty()) {
            return ServerResponse.ERROR;
        }
        try {
            UserEntity userEntity = userToSaveOpt.get();
            save(userEntity);
            return ServerResponse.SUCCESS;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("exception when save user: {}", e.getMessage());
            return ServerResponse.ERROR;
        }
    }

    private Optional<UserEntity> fromSaveDto(SaveUserDto saveUserDto) {
        String username = saveUserDto.getUsername();
        if (StringUtils.isEmpty(username)) {
            LOGGER.info("username must not be null");
            return Optional.empty();
        }
        UserEntity userToSave;
        boolean isUpdate = saveUserDto.getId() != null;
        if (isUpdate) {
            Optional<UserEntity> userToSaveOpt = findAdminById(saveUserDto.getId());
            if (userToSaveOpt.isEmpty()) {
                return Optional.empty();
            }
            userToSave = userToSaveOpt.get();
        } else {
            userToSave = new UserEntity();
            userToSave.setActive(true);
        }

        userToSave.setPassword(passwordEncoder.encode(saveUserDto.getPassword()));
        userToSave.setName(saveUserDto.getName());
        userToSave.setUsername(username);
        userToSave.setEmail(saveUserDto.getEmail());
        userToSave.setPhone(saveUserDto.getPhone());
        userToSave.setRole(saveUserDto.getRole());
        return Optional.of(userToSave);
    }

    public ServerResponse getDetailUser(Long userId) {
        Optional<UserEntity> userEntityOpt = repository.findByIdAndIsDeletedFalse(userId);
        if (userEntityOpt.isEmpty()) {
            return ServerResponse.ERROR;
        }
        UserEntity userEntity = userEntityOpt.get();
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(userEntity.getId());
        userDto.setName(userEntity.getName());
        userDto.setUsername(userEntity.getUsername());
        userDto.setRole(userEntity.getRole());
        userDto.setEmail(userEntity.getEmail());
        userDto.setPhone(userEntity.getPhone());
        return ServerResponse.success(userDto);
    }

    public ServerResponse delete(Long userId) {
        Optional<UserEntity> userOpt = findAdminById(userId);
        if (userOpt.isEmpty()) {
            return ServerResponse.ERROR;
        }
        UserEntity user = userOpt.get();
        user.setDeleted(true);
        user.setOldUsername(user.getUsername());
        user.setUsername(null);
        user.setUpdatedTime(new Date());
        repository.save(user);
        return ServerResponse.SUCCESS;
    }

    public ServerResponse resetPassword(Long userId) {
        Optional<UserEntity> userOpt = findAdminById(userId);
        if (userOpt.isEmpty()) {
            return ServerResponse.ERROR;
        }
        UserEntity user = userOpt.get();
        user.setPassword(passwordEncoder.encode(user.getUsername()));
        user.setUpdatedTime(new Date());
        repository.save(user);
        return ServerResponse.SUCCESS;
    }

    public ServerResponse changeStatus(Long userId) {
        Optional<UserEntity> userOpt = findAdminById(userId);
        if (userOpt.isEmpty()) {
            return ServerResponse.ERROR;
        }
        UserEntity user = userOpt.get();
        user.setActive(!user.isActive());
        user.setUpdatedTime(new Date());
        repository.save(user);
        return ServerResponse.SUCCESS;
    }

    private Optional<UserEntity> findAdminById(Long id) {
        Optional<UserEntity> userOpt = repository.findByIdAndIsDeletedFalse(id);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        UserEntity user = userOpt.get();
        if (Role.SYSTEM_ADMIN == user.getRole()) {
            log.error("cannot get info of system admin account");
            return Optional.empty();
        }
        return userOpt;
    }

    public boolean isUsernameExist(Long oldCompanyAdminId, String username) {
        UserEntity userEntity = repository.findByUsername(username);
        return userEntity != null && !userEntity.getId().equals(oldCompanyAdminId);
    }

}
