package shoppingmall.key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MemberKey implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(nullable = false)
    private String member;
}
