package com.be.custom.service;

import com.be.base.core.BaseService;
import com.be.base.dto.ServerResponse;
import com.be.custom.dto.request.SaveUserDto;
import com.be.custom.dto.request.UpdateProfileRequest;
import com.be.custom.dto.response_api.UserResponseDto;
import com.be.custom.entity.UserEntity;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService extends BaseService<UserEntity, UserRepository> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public Optional<UserEntity> validateUser(String email, String password) {
        Optional<UserEntity> userOpt = repository.findByEmailAndIsDeletedFalse(email);
        if (userOpt.isEmpty()) {
            return Optional.empty();
        }
        UserEntity userEntity = userOpt.get();
        if (passwordEncoder.matches(password, userEntity.getPasswordHash())) {
            return userOpt;
        } else {
            return Optional.empty();
        }
    }

//    public Page<UserEntity> getPageAdmin(String keyword, Role roleFilter, Pageable pageable) {
//        List<Role> listRoleFilter;
//        if (roleFilter == null) {
//            listRoleFilter = List.of(Role.COMPANY_ADMIN, Role.COMPANY_STAFF);
//        } else if (Role.COMPANY_ADMIN == roleFilter) {
//            listRoleFilter = List.of(Role.COMPANY_ADMIN);
//        } else {
//            listRoleFilter = List.of(Role.COMPANY_STAFF);
//        }
//
//        return repository.getPageAdmin(keyword, listRoleFilter, pageable);
//    }

    public ServerResponse saveUser(SaveUserDto saveUserDto) {
        Optional<UserEntity> userToSaveOpt = fromSaveDto(saveUserDto);
        if (userToSaveOpt.isEmpty()) {
            return ServerResponse.ERROR;
        }
        try {
            UserEntity userEntity = userToSaveOpt.get();
            userRepository.save(userEntity);
            return ServerResponse.SUCCESS;
        } catch (DataIntegrityViolationException e) {
            LOGGER.error("exception when save user: {}", e.getMessage());
            return ServerResponse.ERROR;
        }
    }

    public Optional<UserEntity> fromSaveDto(SaveUserDto saveUserDto) {
        String username = saveUserDto.getEmail();
        if (StringUtils.isEmpty(username)) {
            LOGGER.info("username must not be null");
            return Optional.empty();
        }
        UserEntity userToSave;
        boolean isUpdate = saveUserDto.getId() != null;
        if (isUpdate) {
            Optional<UserEntity> userToSaveOpt = findUserById(saveUserDto.getId());
            if (userToSaveOpt.isEmpty()) {
                return Optional.empty();
            }
            userToSave = userToSaveOpt.get();
        } else {
            userToSave = new UserEntity();
            userToSave.setIsDeleted(false);
        }

        userToSave.setPasswordHash(passwordEncoder.encode(saveUserDto.getPassword()));
        userToSave.setFullName(saveUserDto.getName());
        userToSave.setEmail(username);
        userToSave.setEmail(saveUserDto.getEmail());
        userToSave.setPhoneNumber(saveUserDto.getPhone());
        userToSave.setRole(saveUserDto.getRole() == null ? Role.STUDENT : saveUserDto.getRole());
        return Optional.of(userToSave);
    }

    public ServerResponse getDetailUser(Long userId) {
        Optional<UserEntity> userEntityOpt = userRepository.findByIdAndIsDeletedFalse(userId);
        if (userEntityOpt.isEmpty()) {
            return ServerResponse.ERROR;
        }
        UserEntity userEntity = userEntityOpt.get();
        UserResponseDto userDto = new UserResponseDto();
        userDto.setId(userEntity.getId());
        userDto.setName(userEntity.getFullName());
        userDto.setUsername(userEntity.getEmail());
        userDto.setRole(userEntity.getRole());
        userDto.setEmail(userEntity.getEmail());
        userDto.setPhone(userEntity.getPhoneNumber());
        return ServerResponse.success(userDto);
    }

    public ServerResponse delete(Long userId) {
        Optional<UserEntity> userOpt = findUserById(userId);
        if (userOpt.isEmpty()) {
            return ServerResponse.ERROR;
        }
        UserEntity user = userOpt.get();
        user.setIsDeleted(true);
        user.setEmail(null);
        user.setUpdatedAt(new Date());
        repository.save(user);
        return ServerResponse.SUCCESS;
    }

    public ServerResponse resetPassword(Long userId) {
        Optional<UserEntity> userOpt = findUserById(userId);
        if (userOpt.isEmpty()) {
            return ServerResponse.ERROR;
        }
        UserEntity user = userOpt.get();
        user.setPasswordHash(passwordEncoder.encode(user.getEmail()));
        user.setUpdatedAt(new Date());
        repository.save(user);
        return ServerResponse.SUCCESS;
    }

    public ServerResponse changeStatus(Long userId) {
        Optional<UserEntity> userOpt = findUserById(userId);
        if (userOpt.isEmpty()) {
            return ServerResponse.ERROR;
        }
        UserEntity user = userOpt.get();
        user.setIsDeleted(!user.getIsDeleted());
        user.setUpdatedAt(new Date());
        repository.save(user);
        return ServerResponse.SUCCESS;
    }

    public Optional<UserEntity> findUserById(Long id) {
        return repository.findByIdAndIsDeletedFalse(id);
    }

    public boolean isUsernameExist(Long oldCompanyAdminId, String username) {
        UserEntity userEntity = repository.findByEmail(username);
        return userEntity != null && !userEntity.getId().equals(oldCompanyAdminId);
    }

    public List<UserEntity> getListStudentOfParent(Long parentId) {
        Optional<UserEntity> parentOpt = userRepository.findById(parentId);
        if (parentOpt.isEmpty() || parentOpt.get().getRole() != Role.PARENT) {
            return null;
        }

        return userRepository.getListStudentOfParent(parentId);
    }

    public List<UserEntity> getListPsychologist() {
        return userRepository.getListUserByRole(Role.PSYCHOLOGIST);
    }

    public ServerResponse updateProfile(Long userId, UpdateProfileRequest request, MultipartFile file) {
        UserEntity user = userRepository.findById(userId)
                .orElse(null);
        if (user == null) {
            return ServerResponse.ERROR;
        }
        try {
            String filePath = null;
            if (file != null && !file.isEmpty()) {
                filePath = saveFile(file);
            }
            if (request.getFullName() != null) user.setFullName(request.getFullName());
            if (request.getDateOfBirth() != null) user.setDateOfBirth(request.getDateOfBirth());
            if (request.getGender() != null) user.setGender(request.getGender());
            if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());
            if (filePath != null) user.setImageUrl(filePath);
            if (request.getAddress() != null) user.setAddress(request.getAddress());

            userRepository.save(user);
        } catch (Exception e) {
            LOGGER.info("Error when update profile {}", e.getMessage());
            return ServerResponse.ERROR;
        }
        return ServerResponse.SUCCESS;
    }

    private String saveFile(MultipartFile file) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File destinationFile = new File(UPLOAD_DIR + fileName);
        file.transferTo(destinationFile);
        return fileName;
    }

}
