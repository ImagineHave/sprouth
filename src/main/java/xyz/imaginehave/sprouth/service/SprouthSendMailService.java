package xyz.imaginehave.sprouth.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import xyz.imaginehave.sprouth.entity.SprouthVerificationToken;
import xyz.imaginehave.sprouth.properties.MailProperties;

@Service
@Slf4j
public class SprouthSendMailService {
    private final MailProperties mailProperties;
    private final Configuration templates;

    @Autowired
    SprouthSendMailService(MailProperties mailProperties, Configuration templates){
        this.mailProperties = mailProperties;
        this.templates = templates;
    }

    public boolean sendVerificationMail(SprouthVerificationToken sprouthVerificationToken) {
        String subject = "Please verify your email";
        String body = "";
        try {
            Template t = templates.getTemplate("email-verification.ftl");
            Map<String, String> map = new HashMap<>();
            
            log.info(mailProperties.getVerificationapi());
            
            map.put("VERIFICATION_URL", mailProperties.getVerificationapi() + sprouthVerificationToken.getToken());
            map.put("USER_NAME", sprouthVerificationToken.getUser().getUsername());
            body = FreeMarkerTemplateUtils.processTemplateIntoString(t, map);
        } catch (Exception ex) {
        	log.error(ex.getMessage());
        }
        return sendMail(sprouthVerificationToken.getUser().getEmail(), subject, body);
    }

    private boolean sendMail(String toEmail, String subject, String body) {
        try {
        	
            Properties props = System.getProperties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.port", mailProperties.getPort());
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.required", "true");

            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(mailProperties.getEmail(), mailProperties.getName()));
            msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            msg.setSubject(subject);
            msg.setContent(body, "text/html");

            Transport transport = session.getTransport();
            transport.connect(mailProperties.getHost(), mailProperties.getUsername(), mailProperties.getPassword());
            transport.sendMessage(msg, msg.getAllRecipients());
            return true;
        } catch (Exception ex) {
        	log.error(ex.getMessage());
        }
        return false;
    }
}
