package org.parkjw.sslcertificatechecker.domains.checker.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.parkjw.sslcertificatechecker.config.CheckerConfig;
import org.parkjw.sslcertificatechecker.domains.checker.entity.Group;
import org.parkjw.sslcertificatechecker.domains.smtp.service.SMTPClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Processor {

	private final CheckerService service;

	private final SMTPClient client;

	private final CheckerConfig checkerConfig;

	public void process() {

		checkerConfig.getEnableGroup().forEach(group -> {
			Map<String, Group> groups = checkerConfig.getGroups();
			Group targetGroup = groups.get(group);
			if (ObjectUtils.isNotEmpty(targetGroup)) {
				targetGroup.getDomains().forEach(domain -> {
					LocalDateTime expirationDateTime = service.getSSLCertificateExpirationDate(domain);
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					String expirationDate = expirationDateTime.format(formatter);
					Instant now = Instant.now();
					Instant changeTimeInstant = expirationDateTime.atZone(ZoneId.systemDefault()).toInstant();
					Duration duration = Duration.between(changeTimeInstant, now);
					long daysUntilExpiration = Math.abs(duration.toDays());

					checkerConfig.getCondition().forEach(condition -> {
						if (condition > daysUntilExpiration) {
							client.sendEmail(targetGroup.getRecipients(), domain, expirationDate);
						}
					});

				});
			}
		});
	}
}
