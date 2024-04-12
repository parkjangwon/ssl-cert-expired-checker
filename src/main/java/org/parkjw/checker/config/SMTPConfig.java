package org.parkjw.checker.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class SMTPConfig {

	private final CheckerConfig config;

	@Bean
	public JavaMailSender javaMailSender() {

		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(config.getServer().getHost());
		mailSender.setPort(config.getServer().getPort());

		if (config.getServer().isAuthentication()) {
			mailSender.setUsername(config.getMail().getSender());
			mailSender.setPassword(config.getMail().getPassword());
		}

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", config.getServer().isAuthentication());
		props.put("mail.smtp.starttls.enable", config.getServer().isSsl());

		return mailSender;
	}
}