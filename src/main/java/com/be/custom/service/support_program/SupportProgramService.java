package com.be.custom.service.support_program;

import com.be.custom.entity.SupportProgramEntity;
import com.be.custom.repository.SupportProgramRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupportProgramService {

    private final SupportProgramRepository supportProgramRepository;

    public List<SupportProgramEntity> getAllSupportProgram() {
        return supportProgramRepository.findAllByIsDeletedFalse();
    }

    public Page<SupportProgramEntity> getPageSupportProgram(String keyword, Pageable pageable) {
        return supportProgramRepository.getPageSupportProgramAdmin(keyword, pageable);
    }

}
