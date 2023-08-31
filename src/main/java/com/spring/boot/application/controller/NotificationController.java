package com.spring.boot.application.controller;

import com.spring.boot.application.common.AbstractBaseController;
import com.spring.boot.application.common.auth.AuthorizeValidator;
import com.spring.boot.application.common.enums.NotificationFilter;
import com.spring.boot.application.common.enums.UserRole;
import com.spring.boot.application.common.utils.RestAPIResponse;
import com.spring.boot.application.controller.model.response.PagingResponse;
import com.spring.boot.application.services.notification.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiPath.NOTIFICATION_APIs)
public class NotificationController extends AbstractBaseController {
    final private NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Operation(summary = "markAsRead")
    @AuthorizeValidator({UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER})
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> markAsRead(
            @PathVariable(name = "id") String id
    ) {
        return responseUtil.successResponse(notificationService.markAsRead(id));
    }

    @Operation(summary = "markAllAsRead")
    @AuthorizeValidator({UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER})
    @RequestMapping(path = "/all/{companyId}", method = RequestMethod.PUT)
    public ResponseEntity<RestAPIResponse> markAllAsRead(
            @PathVariable String companyId
    ) {
        return responseUtil.successResponse(notificationService.markAllAsRead(companyId));
    }

    @Operation(summary = "getAllNotificationByCompanyId")
//    @AuthorizeValidator({UserRole.ADMIN, UserRole.COMPANY_ADMIN, UserRole.COMPANY_ADMIN_MEMBER, UserRole.COMPANY_MEMBER})
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<RestAPIResponse> getAllNotiByCompanyId(
            @RequestParam(name = "companyId") String companyId,
            @RequestParam NotificationFilter filter,
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return responseUtil.successResponse(
                new PagingResponse(notificationService.getAllNotificationByCompanyId(companyId, filter, pageNumber, pageSize))
        );
    }
}
