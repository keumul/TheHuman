package by.sapegina.springblog.service;

import by.sapegina.springblog.entity.Email;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class MailService {

    private final JavaMailSender mailSender;
    private final MailContentBuilder mailContentBuilder;

//    void sendMail(Email email){
//        MimeMessagePreparator messagePreparator = mimeMessage -> {
//            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
//            messageHelper.setFrom()
//        }
//    }
}
