package shoppingmall.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import shoppingmall.entity.MemberUser;
import shoppingmall.service.MemberService;
import shoppingmall.utils.BusinessLayerResponse;
import shoppingmall.vo.MemberLogin;
import shoppingmall.vo.MemberRegisterVO;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "會員相關")
@CrossOrigin(origins = "*")
@RequestMapping("/member")
public class MemberController {
    
    @Autowired
    private MemberService memberService;
    
    @ApiOperation("會員註冊")
    @PostMapping("/register")
    public BusinessLayerResponse<String> register(HttpServletRequest request, 
                                                  @RequestBody MemberRegisterVO registerVO)
    {
        return memberService.register(request, registerVO);
    }

    @ApiOperation("會員登入")
    @PostMapping("/login")
    public BusinessLayerResponse<String> login(HttpServletRequest request,
                                               @RequestBody MemberLogin memberLogin)
    {
        return memberService.login(request, memberLogin);
    }
    
    @ApiOperation("會員登出")
    @GetMapping("/logout")
    @PreAuthorize("hasRole('ROLE_USER')")
    public BusinessLayerResponse<String> logout(HttpServletRequest request,
                                                @AuthenticationPrincipal MemberUser member)
    {
        return memberService.logout(request, member);
    }
}
