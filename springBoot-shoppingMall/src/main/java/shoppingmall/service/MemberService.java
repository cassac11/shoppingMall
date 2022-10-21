package shoppingmall.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shoppingmall.entity.MemberUser;
import shoppingmall.key.MemberKey;
import shoppingmall.repository.MemberRepository;
import shoppingmall.utils.BusinessLayerResponse;
import shoppingmall.utils.EnDecoderUtil;
import shoppingmall.vo.MemberLogin;
import shoppingmall.vo.MemberRegisterVO;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
public class MemberService {
    
    @Autowired
    private MemberRepository memberRepository;
    
    @Autowired
    private SessionManagementService sessionManagementService;
    
    public MemberUser findByMember(String member)
    {
        Optional<MemberUser> memberUser = memberRepository.findById(new MemberKey(member));

        return memberUser.orElse(null);
    }
    
    public MemberUser saveFlush(MemberUser memberUser)
    {
        return memberRepository.saveAndFlush(memberUser);
    }
    
    // 用戶註冊
    public BusinessLayerResponse<String> register(HttpServletRequest request, MemberRegisterVO memberRegisterVO)
    {
        String member = memberRegisterVO.getMember();
        
        if (!StringUtils.hasText(member))
        {
            return BusinessLayerResponse.error("用戶名稱不可為空!");
        }

        if (member.contains("$") || member.contains("-"))
        {
            return BusinessLayerResponse.error("請勿使用特殊字元!");
        }

        if (!StringUtils.hasText(memberRegisterVO.getPassword()))
        {
            return BusinessLayerResponse.error("密碼不可為空!");
        }
        
        if (!StringUtils.hasText(memberRegisterVO.getEmail())) 
        {
            return BusinessLayerResponse.error("信箱不可為空!");
        }
      
        if (!memberRegisterVO.getEmail().matches("\\w[-\\w.+]*@([A-Za-z0-9]{2,})[.]+[A-Za-z]{2,14}"))
        {
            return BusinessLayerResponse.error("會員信箱格式錯誤!");
        }

        if (!StringUtils.hasText(memberRegisterVO.getMobile()))
        {
            return BusinessLayerResponse.error("手機不可為空!");
        }
        
        if (memberRegisterVO.getSex() == null) 
        {
            return BusinessLayerResponse.error("性別不可為空!");
        }
        
        if (!StringUtils.hasText(memberRegisterVO.getNickName()))
        {
            return BusinessLayerResponse.error("暱稱不可為空!");
        }
        
        log.info("MemberService ==> register ... 檢查會員是否已經存在 [" + member + "]");
    
        String trimMember = StringUtils.trimWhitespace(member);
        
        MemberUser memberUser = findByMember(trimMember);

        if (memberUser != null)
        {
            log.info("MemberService ==> register ... 會員已經存在！");
            return BusinessLayerResponse.error("用戶名稱已經存在，請更換用戶名稱！");
        }
        
        try 
        {
            log.info("MemberService ==> register ... 建立新會員");
            
            String trimPassword = StringUtils.trimWhitespace(memberRegisterVO.getPassword());

            MemberUser user = new MemberUser();

            user.setKey(new MemberKey(trimMember));
            user.setAreaCode(memberRegisterVO.getAreaCode());
            user.setMobile(memberRegisterVO.getMobile());
            user.setEmail(memberRegisterVO.getEmail());
            user.setNickName(memberRegisterVO.getNickName());
            user.setSex(memberRegisterVO.getSex());
            user.setPassword(EnDecoderUtil.md5Encrypt(trimPassword));
            user.setRoles(Collections.singletonList("ROLE_USER"));
            
            user.setIsOnline(false);
            user.setEnable(true);

            saveFlush(user);
            
            log.info("MemberService ==> register ... 註冊成功 : [" + trimMember + "]");
            
            return BusinessLayerResponse.ok("註冊成功");
        }
        catch (Exception ex)
        {
            log.error("MemberService ==> register ... Exception : [" + ex + "]");
            
            return BusinessLayerResponse.error("註冊失敗!");
        }
    }
    
    public BusinessLayerResponse<String> login(HttpServletRequest request, MemberLogin memberLogin)
    {
        // 帳號登出
        String vToken = sessionManagementService.removeLoggedUser(request, "member_" + memberLogin.getMember(), 1);
        
        if (!StringUtils.hasText(memberLogin.getMember()))
        {
            return BusinessLayerResponse.error("帳號密碼錯誤!");
        }
        
        if (!StringUtils.hasText(memberLogin.getPassword()))
        {
            return BusinessLayerResponse.error("帳號密碼錯誤!");
        }

        MemberUser member = findByMember(memberLogin.getMember());
        
        if (member == null)
        {
            log.info("MemberService ==> login ... 會員登入失敗：未輸入帳號");
            
            return BusinessLayerResponse.error("登入失敗!");
        }
    }
}
