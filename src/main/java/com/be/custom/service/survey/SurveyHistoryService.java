package com.be.custom.service.survey;

import com.be.base.core.BaseService;
import com.be.custom.entity.SurveyHistoryEntity;
import com.be.custom.entity.UserEntity;
import com.be.custom.repository.SurveyHistoryRepository;
import com.be.custom.repository.UserRepository;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyHistoryService extends BaseService<SurveyHistoryEntity, SurveyHistoryRepository> {

    private final UserService userService;
    private final UserRepository userRepository;

    public List<SurveyHistoryEntity> getListHistorySurveyByListStudentId(List<Long> listStudentId) {
        if (listStudentId.isEmpty()) {
            return null;
        }

        return repository.getListHistorySurveyByListStudentId(listStudentId);
    }

    public List<SurveyHistoryEntity> getListHistorySurveyFromParent(Long parentId) {
        List<Long> listStudentId = userRepository.getListStudentIdOfParent(parentId);
        if (listStudentId.isEmpty()) {
            return null;
        }

        return getListHistorySurveyByListStudentId(listStudentId);
    }
}
