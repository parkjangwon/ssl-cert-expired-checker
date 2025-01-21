package org.parkjw.checker.domains.smtp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parkjw.checker.config.CheckerConfig;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SMTPClient {

	private final CheckerConfig config;

	private final JavaMailSender mailSender;

	public void sendEmail(List<String> recipients, String domain, String date) {

		MimeMessageHelper helper;
		try {
			MimeMessage message = mailSender.createMimeMessage();
			String text = String.format("[%s] %s : %s", domain, config.getMail().getText(), date);

			helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(config.getMail().getSender());
			helper.setTo(recipients.toArray(new String[0]));
			helper.setSubject(text);
			helper.setText(text, true);

			if (log.isDebugEnabled()) {
				log.debug("[{}] Mail Send Start. content : [{}]", domain, text);
			}

			mailSender.send(message);
		} catch (MessagingException e) {
			log.debug("[{}] Mail Send Fail. error : [{}]", domain, e.getMessage());
		}

		if (log.isDebugEnabled()) {
			log.debug("[{}] Mail Send Complete", domain);
		}
	}
}
