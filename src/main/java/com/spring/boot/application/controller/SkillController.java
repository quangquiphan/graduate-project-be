package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.request.skill.SkillRequest;
import com.spring.boot.application.services.skill.SkillService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.SKILL_APIs)
public class SkillController extends AbstractBaseController {
    final private SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @Operation(summary = "addSkill")
    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN})
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<RestAPIResponse> addSkill(
            @RequestBody SkillRequest addSkill
    ) {
        return responseUtil.successResponse(skillService.addSkill(addSkill));
    }

    @Operation(summary = "updateSkill")
    @AuthorizeValidator(UserRole.ADMIN)
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> updateSkill(
            @PathVariable(name = "id") String id,
            @RequestBody SkillRequest addSkill
    ) {
        return responseUtil.successResponse(skillService.updateSkill(id, addSkill));
    }

    @Operation(summary = "getSkill")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getSkill(
            @PathVariable(name = "id") String id
    ) {
        return responseUtil.successResponse(skillService.getSkillById(id));
    }

    @Operation(summary = "getAllSkill")
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAllSkill() {
        return responseUtil.successResponse(skillService.getAllSkill());
    }

    @Operation(summary = "deleteSkill")
    @AuthorizeValidator(UserRole.ADMIN)
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<RestAPIResponse> deleteSkill(
            @PathVariable(name = "id") String id
    ) {
        return responseUtil.successResponse(skillService.deleteSkill(id));
    }
}
