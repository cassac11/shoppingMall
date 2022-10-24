package shoppingmall.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import shoppingmall.entity.SysConfig;

import java.util.List;

@Data
@ApiModel
public class SysConfigList {
  
    @ApiModelProperty(value = "config array", required = true)
    List<SysConfig> sysConfigs;
}
