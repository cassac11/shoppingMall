package shoppingmall.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ApiModel
public class MemberRegisterVO {
	
	@ApiModelProperty(value = "暱稱", required = true)
	private String nickName;
	
	@ApiModelProperty(value = "註冊名", required = true)
	private String member;
	
	@ApiModelProperty(value = "用戶密碼", required = true)
	private String password;
	
	@ApiModelProperty(value = "用戶信箱", required = true)
	private String email;
	
	@ApiModelProperty(value = "用戶手機", required = true)
	private String mobile;	

	@ApiModelProperty(value = "區域碼", required = true, example = "886")
	private String areaCode;

	@ApiModelProperty(value = "性別", required = true)
	private Integer sex;
}
