package com.be.custom.service.program_registration;

import com.be.base.core.BaseService;
import com.be.base.dto.ServerResponse;
import com.be.custom.entity.ProgramRegistrationEntity;
import com.be.custom.entity.SupportProgramEntity;
import com.be.custom.entity.UserEntity;
import com.be.custom.enums.StatusProgramRegistration;
import com.be.custom.repository.SupportProgramRegistrationRepository;
import com.be.custom.repository.SupportProgramRepository;
import com.be.custom.repository.UserRepository;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProgramRegistrationService extends BaseService<ProgramRegistrationEntity, SupportProgramRegistrationRepository> {

    private final SupportProgramRegistrationRepository programRegistrationRepository;
    private final UserService userService;
    private final SupportProgramRepository supportProgramRepository;

    public List<ProgramRegistrationEntity> getAllProgramRegistrationByUserId(Long userId) {
        return programRegistrationRepository.findAllByUserIdAndIsDeletedFalse(userId);
    }

    @Transactional
    public ServerResponse saveProgramRegistration(Long userId, Long programId) {
        UserEntity userEntity = userService.findById(userId);
        if (userEntity == null) {
            return ServerResponse.ERROR;
        }

        boolean saveSuccess = registerProgram(userEntity, programId);

        return saveSuccess ? ServerResponse.SUCCESS : ServerResponse.ERROR;
    }

    @Transactional
    public ServerResponse saveProgramRegistrationFromParent(Long parentId, Long studentId, Long programId) {
        List<UserEntity> listStudentOfParent = userService.getListStudentOfParent(parentId);
        if (listStudentOfParent.isEmpty()) {
            return ServerResponse.ERROR;
        }

        UserEntity student = listStudentOfParent.stream()
                .filter(item -> Objects.equals(item.getId(), studentId))
                .findFirst().orElse(null);

        if (student == null) {
            return ServerResponse.ERROR;
        }

        boolean saveSuccess = registerProgram(student, programId);

        return saveSuccess ? ServerResponse.SUCCESS : ServerResponse.ERROR;
    }

    private boolean registerProgram(UserEntity student, Long programId) {
        Optional<SupportProgramEntity> supportProgramEntityOpt = supportProgramRepository.findById(programId);
        if (supportProgramEntityOpt.isEmpty()) {
            return false;
        }

        ProgramRegistrationEntity programRegistrationEntity = new ProgramRegistrationEntity();
        programRegistrationEntity.setProgram(supportProgramEntityOpt.get());
        programRegistrationEntity.setUser(student);
        programRegistrationEntity.setStatus(StatusProgramRegistration.REGISTERED);
        programRegistrationEntity.setIsDeleted(false);
        programRegistrationEntity.setRegisteredAt(new Date());
        programRegistrationEntity.setUpdatedAt(new Date());

        save(programRegistrationEntity);
        return true;
    }

}
