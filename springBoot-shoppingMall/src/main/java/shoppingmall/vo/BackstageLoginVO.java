package shoppingmall.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class BackstageLoginVO {
    
    /* 後台登入 */
    @ApiModelProperty(value = "用戶名", required = true)
    private String name;

    @ApiModelProperty(value = "密碼", required = true)
    private String password;

    @ApiModelProperty(value = "類別", required = true)
    private Integer web; // 0 Admin
}
