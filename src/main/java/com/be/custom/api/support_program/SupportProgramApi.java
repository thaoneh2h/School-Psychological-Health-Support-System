package com.be.custom.api.support_program;

import com.be.custom.entity.SupportProgramEntity;
import com.be.custom.enums.Role;
import com.be.custom.service.support_program.SupportProgramService;
import com.be.custom.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/support-program")
@RequiredArgsConstructor
public class SupportProgramApi {

    private final SupportProgramService supportProgramService;

    @GetMapping("/get-all-support-program")
    public ResponseEntity<List<SupportProgramEntity>> getAllSupportProgram() {
        return ResponseEntity.ok(supportProgramService.getAllSupportProgram());
    }

    @PreAuthorize("@authorizationService.isLogin()")
    @GetMapping("/get-page-support-program")
    public ResponseEntity<Page<SupportProgramEntity>> getPageSupportProgram(@RequestParam int page,
                                                                            @RequestParam int size,
                                                                            @RequestParam(defaultValue = "updatedAt") String sortField,
                                                                            @RequestParam(defaultValue = "desc") String sortDir,
                                                                            @RequestParam(required = false) Role role,
                                                                            @RequestParam(required = false) String keyword) {
        Pageable pageable = PageUtils.from(sortDir, sortField, page, size);
        return ResponseEntity.ok(supportProgramService.getPageSupportProgram(keyword, pageable));
    }
}
