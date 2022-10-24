package shoppingmall.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.net.URL;
import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Mail {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    
	private String toEmailAddress;  // 寄信地址
	private String subject;         // 信件主題
	private String content;         // 信件內容
	private URL url;                // 附件地址
	private String fileName;        // 附件檔名
	
    @CreatedDate
    private Date createDate;        // 建立日期
    
    @LastModifiedDate
    private Date modifyTime;        // 最後更新時間

}
