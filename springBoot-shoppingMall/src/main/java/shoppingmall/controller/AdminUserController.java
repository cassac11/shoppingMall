package shoppingmall.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shoppingmall.service.AdminUserService;
import shoppingmall.utils.BusinessLayerResponse;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@Api(tags = "管理員操作")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class AdminUserController {

    @Autowired
    private AdminUserService adminUserService;
    
    @ApiOperation("註冊管理員")
    @GetMapping("/createAdminUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BusinessLayerResponse<String> createAdminUser(HttpServletRequest request,
                                                         @RequestParam("adminName") String adminName,
                                                         @RequestParam("password")  String password)
    {
        return adminUserService.createAdminUser(request, adminName, password);
    }
}
