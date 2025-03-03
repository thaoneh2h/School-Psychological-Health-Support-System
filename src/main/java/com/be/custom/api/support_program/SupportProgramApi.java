package com.be.custom.api.support_program;

import com.be.base.dto.ServerResponse;
import com.be.custom.common.security.CustomUserDetails;
import com.be.custom.dto.request.SaveSupportProgramReq;
import com.be.custom.entity.SupportProgramEntity;
import com.be.custom.enums.Role;
import com.be.custom.service.support_program.SupportProgramService;
import com.be.custom.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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
                                                                            @RequestParam(required = false) String keyword) {
        Pageable pageable = PageUtils.from(sortDir, sortField, page, size);
        return ResponseEntity.ok(supportProgramService.getPageSupportProgram(keyword, pageable));
    }

    @PreAuthorize("@authorizationService.isSystemAdmin()")
    @GetMapping("/get-page-support-program-admin")
    public ResponseEntity<Page<SupportProgramEntity>> getPageSupportProgramForAdmin(@RequestParam int page,
                                                                                    @RequestParam int size,
                                                                                    @RequestParam(defaultValue = "updatedAt") String sortField,
                                                                                    @RequestParam(defaultValue = "desc") String sortDir,
                                                                                    @RequestParam(required = false) String keyword) {
        Pageable pageable = PageUtils.from(sortDir, sortField, page, size);
        return ResponseEntity.ok(supportProgramService.getPageSupportProgramAdmin(keyword, pageable));
    }

    @PostMapping("/save-support-program")
    @PreAuthorize("@authorizationService.isSystemAdmin()")
    public ResponseEntity<ServerResponse> saveSupportProgram(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @RequestBody SaveSupportProgramReq supportProgramReq) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(supportProgramService.saveOrUpdateSupportProgram(supportProgramReq, userId));
    }

    @PostMapping("/change-status-support-program")
    @PreAuthorize("@authorizationService.isSystemAdmin()")
    public ResponseEntity<ServerResponse> ChangeStatusSupportProgram(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                     @RequestBody Long supportProgramId) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(supportProgramService.changeStatusSupportProgram(supportProgramId, userId));
    }

    @GetMapping("/get-detail-support-program")
    @PreAuthorize("@authorizationService.isLogin()")
    public ResponseEntity<SupportProgramEntity> getDetailSupportProgram(@RequestParam Long supportProgramId) {
        return ResponseEntity.ok(supportProgramService.findById(supportProgramId));
    }
}
