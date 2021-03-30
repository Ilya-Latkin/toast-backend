package com.ngteam.toastapp.services.impl;

import com.ngteam.toastapp.services.MailService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassRelativeResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.SpringTemplateLoader;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    private final Template confirnMailTemplate;

    public MailServiceImpl() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        configuration.setDefaultEncoding("UTF-8");
        configuration.setTemplateLoader(
                new SpringTemplateLoader(new ClassRelativeResourceLoader(this.getClass()),
                        "/"));
        configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        try {
            this.confirnMailTemplate = configuration.getTemplate("mail/confirm_mail.ftlh");
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void sendConfirmEmail(String email, String code) {
        String mailText = getEmailText(code);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom(mailFrom);
            messageHelper.setTo(email);
            messageHelper.setSubject("Спасибо за регистрацию в Toast!");
            messageHelper.setText(mailText, true);
        };
        javaMailSender.send(messagePreparator);
    }

    private String getEmailText(String code) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("confirm_code", code);
        StringWriter writer = new StringWriter();
        try {
            confirnMailTemplate.process(attributes, writer);
        } catch (TemplateException | IOException e) {
            throw new IllegalStateException(e);
        }
        return writer.toString();
    }
}