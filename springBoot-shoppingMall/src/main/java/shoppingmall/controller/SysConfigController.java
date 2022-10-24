package shoppingmall.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import shoppingmall.entity.SysConfig;
import shoppingmall.service.SysConfigService;
import shoppingmall.utils.BusinessLayerResponse;
import shoppingmall.vo.SysConfigList;

import java.util.List;

@RestController
@Api(tags = "系統設置")
@CrossOrigin(origins = "*")
@RequestMapping("/SysConfig")
public class SysConfigController {
    
    @Autowired
    private SysConfigService sysConfigService;
    
    @ApiOperation("新增設置")
    @GetMapping("/addItem")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BusinessLayerResponse<String> addItem(@RequestParam("id")      String id,
                                                 @RequestParam("content") String content)
    {
        return sysConfigService.addItem(id, content);
    }
    
    @ApiOperation("刪除設置")
    @GetMapping("/deleteItem")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BusinessLayerResponse<String> deleteItem(@RequestParam("id") String id)
    {
        return sysConfigService.deleteItem(id);
    }
    
    @ApiOperation("修改設置")
    @PostMapping("/updateList")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public BusinessLayerResponse<String> updateList(@RequestBody SysConfigList sysConfigList)
    {
        return sysConfigService.updateList(sysConfigList.getSysConfigs());
    }
    
    @ApiOperation("取得所有設置")
    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<SysConfig> getAll()
    {
        return sysConfigService.getAll();
    }
}
