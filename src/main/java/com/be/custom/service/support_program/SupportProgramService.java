package com.be.custom.service.support_program;

import com.be.base.core.BaseService;
import com.be.base.dto.ServerResponse;
import com.be.custom.dto.request.SaveSupportProgramReq;
import com.be.custom.entity.SupportProgramEntity;
import com.be.custom.entity.UserEntity;
import com.be.custom.repository.SupportProgramRepository;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupportProgramService extends BaseService<SupportProgramEntity, SupportProgramRepository> {

    private final SupportProgramRepository supportProgramRepository;
    private final UserService userService;

    public List<SupportProgramEntity> getAllSupportProgram() {
        return supportProgramRepository.findAllByIsDeletedFalse();
    }

    public Page<SupportProgramEntity> getPageSupportProgram(String keyword, Pageable pageable) {
        return supportProgramRepository.getPageSupportProgramClient(keyword, pageable);
    }

    public Page<SupportProgramEntity> getPageSupportProgramAdmin(String keyword, Pageable pageable) {
        return supportProgramRepository.getPageSupportProgramAdmin(keyword, pageable);
    }

    @Transactional
    public ServerResponse saveOrUpdateSupportProgram(SaveSupportProgramReq supportProgramReq, Long userId) {
        UserEntity user = userService.findById(userId);
        if (user == null) {
            return ServerResponse.error("Not found user update");
        }
        Long supportProgramId = supportProgramReq.getId();
        SupportProgramEntity supportProgramEntity = new SupportProgramEntity();
        if (supportProgramId != null) {
            supportProgramEntity = supportProgramRepository.getById(supportProgramId);
            if (supportProgramEntity.getId() == null) {
                return ServerResponse.error("Not found support program");
            }
        }
        supportProgramEntity.setTitle(supportProgramReq.getTitle());
        supportProgramEntity.setDescription(supportProgramReq.getDescription());
        supportProgramEntity.setCategory(supportProgramReq.getCategory());
        supportProgramEntity.setUpdatedBy(user);
        supportProgramEntity.setUpdatedAt(new Date());

        save(supportProgramEntity);
        return ServerResponse.SUCCESS;
    }

    @Transactional
    public ServerResponse changeStatusSupportProgram(Long supportProgramId, Long userId) {
        UserEntity user = userService.findById(userId);
        Optional<SupportProgramEntity> supportProgramEntityOpt = repository.findById(supportProgramId);
        if (user == null || supportProgramEntityOpt.isEmpty()) {
            return ServerResponse.error("Not found entity in database to change status");
        }

        SupportProgramEntity supportProgramEntity = supportProgramEntityOpt.get();
        boolean currentStatus = supportProgramEntity.getIsDeleted();
        supportProgramEntity.setUpdatedAt(new Date());
        supportProgramEntity.setUpdatedBy(user);
        supportProgramEntity.setIsDeleted(!currentStatus);

        save(supportProgramEntity);
        return ServerResponse.SUCCESS;
    }
}
