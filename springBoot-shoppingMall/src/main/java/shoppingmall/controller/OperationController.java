package shoppingmall.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import shoppingmall.service.OperationService;
import shoppingmall.utils.BusinessLayerResponse;
import shoppingmall.vo.BackstageLoginVo;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Api(tags = "營運工具類")
@RestController
@RequestMapping("/operation")
@CrossOrigin(origins = "*")
public class OperationController {
  
    @Autowired
    private OperationService operationService;
    
    @ApiOperation("登入")
    @PostMapping(path = "/getToken")
    public BusinessLayerResponse<String> getToken(HttpServletRequest request,
                                                  @RequestBody BackstageLoginVo backstageLoginVo)
    {
        return operationService.getToken(request, backstageLoginVo);
    }
}
