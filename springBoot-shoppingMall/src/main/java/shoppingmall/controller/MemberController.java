package shoppingmall.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shoppingmall.service.MemberService;
import shoppingmall.utils.BusinessLayerResponse;
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
}
