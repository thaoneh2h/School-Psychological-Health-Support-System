package com.be.base.email;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.stream.Collectors;

@Service
@Slf4j
public class EmailService {

    @Value("${email.receiver}")
    private String receiver;

    @Autowired
    private JavaMailSender javaMailSender;

    /**
     * sendEmail
     *
     * @param messageTemplate
     */
    public void sendEmail(final EmailTemplate messageTemplate) {
        new Thread(() -> {
            try {
                log.info("Sending email {} to {} ", messageTemplate.getSubject(), messageTemplate.getReceiver());
                MimeMessage mimeMessage = javaMailSender.createMimeMessage();

                MimeMessageHelper mailMsg = new MimeMessageHelper(mimeMessage);

                mailMsg.setFrom(new InternetAddress("vinhnh2597@gmail.com", "Vinh", "utf-8"));
                mailMsg.setTo(messageTemplate.getReceiver());
                mailMsg.setSubject(messageTemplate.getSubject());
                mailMsg.setText(messageTemplate.getContent(), true);

                javaMailSender.send(mimeMessage);
            } catch (MessagingException | UnsupportedEncodingException e) {
                log.error("Error when sending email : ", e);
            }
        }).start();
    }

    /**
     * sendEmail
     *
     * @param receiver
     * @param subject
     * @param content
     */
    public void sendEmail(String receiver, String subject, String content) {
        EmailTemplate emailMessage = new EmailTemplate();

        emailMessage.setReceiver(receiver);
        emailMessage.setSubject(subject);
        emailMessage.setContent(content);

        sendEmail(emailMessage);
    }

    /**
     * sendEmail
     *
     * @param subject
     * @param content
     */
    public void sendEmail(String subject, String content) {
        EmailTemplate emailMessage = new EmailTemplate();

        emailMessage.setReceiver(receiver);
        emailMessage.setSubject(subject);
        emailMessage.setContent(content);

        sendEmail(emailMessage);
    }

    public void sendErrorReport(String errorMessage, HttpServletRequest request) {
        String sb = "URL is: " + request.getRequestURL().toString() + "\n" +
                getRequestInfo(request) + "\n" +
                "ERROR MESSAGE IS: " + "\n" + errorMessage;
        DateFormatter dateFormatter = new DateFormatter("dd-MM-yyyy HH:mm");
        String subject = " BASE_API | " + dateFormatter.print(new Date(), request.getLocale());
        sendEmail(subject, sb);
    }


    private String getRequestInfo(HttpServletRequest request) {
        return "METHOD: " + request.getMethod() + "\n" +
                "Content-Type: " + request.getContentType() + "\n" +
                "REQUEST PARAM: " + "\n" + getRequestParams(request) +
                "REQUEST BODY: " + getRequestBody(request);
    }

    private String getRequestParams(HttpServletRequest request) {
        StringBuilder params = new StringBuilder();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            params.append(paramName).append(":").append(request.getParameter(paramName));
            params.append("\n");

        }
        return params.toString();
    }

    private String getRequestBody(HttpServletRequest request) {
        String requestData = "";
        try {
            requestData = request.getReader().lines().collect(Collectors.joining());
        } catch (IOException e) {
            log.info("Cannot get body!", e);
        }
        return requestData;
    }
}
