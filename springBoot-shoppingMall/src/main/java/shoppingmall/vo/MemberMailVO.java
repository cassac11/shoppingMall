package shoppingmall.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class MemberMailVO {

    @ApiModelProperty(value = "推送對象會員", required = true)
    private String member;
    
    @ApiModelProperty(value = "信件主題", required = true)
    private String subject;

    @ApiModelProperty(value = "信件內容", required = true)
    private String content;
}
