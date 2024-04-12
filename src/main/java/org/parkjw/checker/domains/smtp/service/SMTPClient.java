package org.parkjw.sslcertificatechecker.domains.smtp.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.parkjw.sslcertificatechecker.config.CheckerConfig;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SMTPClient {

	private final CheckerConfig config;

	private final JavaMailSender mailSender;

	public void sendEmail(List<String> recipients, String domain, String date) {

		StringBuilder textBuilder = new StringBuilder();
		textBuilder.append("[");
		textBuilder.append(domain);
		textBuilder.append("] ");
		textBuilder.append(config.getMail().getSubject() + " : ");
		textBuilder.append(date);
		String text = textBuilder.toString();
		log.info("text : " + text);

		log.info("  >> Mail Send Start");
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = null;

		try {
			helper = new MimeMessageHelper(message, true, "UTF-8");
			helper.setFrom(config.getMail().getSender());
			helper.setTo(recipients.toArray(new String[0]));
			helper.setSubject(text);
			helper.setText(text, true);
			mailSender.send(message);

		} catch (MessagingException e) {
			log.error("  >> Mail Send Fail. {}", e.getMessage());
		}

		log.info("  >> Mail Send Complete");
	}
}
