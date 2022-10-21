package shoppingmall.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class MemberLogin {

    @ApiModelProperty(value = "註冊名",required = true)
    private String member;
    
    @ApiModelProperty(value = "用戶密碼",required = true)
    private String password;
	
}
