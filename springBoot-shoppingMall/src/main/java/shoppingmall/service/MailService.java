package shoppingmall.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import shoppingmall.entity.Mail;
import shoppingmall.entity.MemberUser;
import shoppingmall.repository.MailRepository;
import shoppingmall.utils.BusinessLayerResponse;
import shoppingmall.vo.MemberMailVO;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

@Slf4j
@Service
public class MailService {
   
    @Autowired
    private SysConfigService sysConfigService;
    
    @Autowired
    private MailRepository mailRepository;
    
    @Autowired
    private MemberService memberService;
    
    public Mail save(Mail mail)
    {
        return mailRepository.save(mail);
    }
    
    // 寄件
    public boolean sendTo(Mail mail)
    {
        log.info("MailService ==> sendTo ... 發送信件： [" + mail.toString() + "]");

        if (!StringUtils.hasText(mail.getToEmailAddress()))
        {
            log.info("MailService ==> sendTo ... 未設定收件人地址！");
            return false;
        }
        if (!mail.getToEmailAddress().matches("\\w[-\\w.+]*@([A-Za-z0-9]{2,})[.]+[A-Za-z]{2,14}"))
        {
            log.info("MailService ==> sendTo ... 錯誤信箱格式：[" + mail.getToEmailAddress() + "]");
            return false;
        }

        String smtpMail = sysConfigService.getContent("mail_smtp_host");   /* 信箱smtp類型 */
        String port = sysConfigService.getContent("mail_smtp_port");       /* smtp port */
        String sender = sysConfigService.getContent("mail_sender_email");  /* DD888@tt186.com */
        String verificationCode = sysConfigService.getContent("mail_smtp_verification_code");

        log.info("MailService ==> sendTo ... 主機設置： [" + smtpMail + ":" + port + ", " + sender + "]");

        if (!StringUtils.hasText(smtpMail) || !StringUtils.hasText(port) 
            || !StringUtils.hasText(sender) || !StringUtils.hasText(verificationCode))
        {
            return false;
        }
        
        Properties props = new Properties();
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtpMail);  /* smtp.gmail.com */
        props.put("mail.smtp.port", port);      /* 587 */

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(sender, verificationCode); // 密碼是smtp驗證碼，不是信箱密碼
                    }
                });
        try
        {
            Message message = new MimeMessage(session);

            message.addHeader("X-Mailer","Microsoft Outlook Express 6.00.2900.2869");  // 防止成爲垃圾郵件，變成外部傳送
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail.getToEmailAddress())); /* 使用者信箱 */
            message.setSubject(mail.getSubject());
            message.setText(mail.getContent());

            Transport.send(message);
            save(mail);
        }
        catch (MessagingException ex)
        {
            log.info("MailService ==> sendTo ... 發送信件失敗！ Error： [" + ex + "]");
            ex.printStackTrace();
        }
        return true;
    }

    // 寄信至會員信箱
    public String sendMailToMember(MemberMailVO memberMailVO) throws Exception
    {
        MemberUser user = memberService.findByMember(memberMailVO.getMember());

        if (user == null)
            return "查無此會員";

        if (!StringUtils.hasText(user.getEmail()))
            return "會員未設置信箱地址";

        if (!user.getEmail().matches("\\w[-\\w.+]*@([A-Za-z0-9]{2,})[.]+[A-Za-z]{2,14}"))
        {
            log.info("MailService ==> sendMailToMember ... 錯誤信箱格式：[" + user.getEmail() + "]");
            return "錯誤信箱格式";
        }
        
        Mail mail = new Mail();
        mail.setContent(memberMailVO.getContent());
        mail.setSubject(memberMailVO.getSubject());
        mail.setToEmailAddress(user.getEmail());

        sendTo(mail);

        return null;
    }
    
    public BusinessLayerResponse<String> test()
    {
        MemberMailVO memberMailVO = new MemberMailVO();
        
        memberMailVO.setMember("iv02");
        memberMailVO.setSubject("測試");
        memberMailVO.setContent("https://google.com");
        
        try 
        {
            String mailToMember = sendMailToMember(memberMailVO);
            
            if (StringUtils.hasText(mailToMember))
                BusinessLayerResponse.error(mailToMember);
            
            return BusinessLayerResponse.ok("成功");
        }
        catch (Exception ex)
        {
            log.error("eee" + ex);
        }
        return BusinessLayerResponse.error("失敗");
    }
}
