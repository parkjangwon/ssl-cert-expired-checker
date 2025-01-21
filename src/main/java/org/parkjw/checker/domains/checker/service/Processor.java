package org.parkjw.checker.domains.checker.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.parkjw.checker.config.CheckerConfig;
import org.parkjw.checker.domains.checker.entity.Group;
import org.parkjw.checker.domains.smtp.service.SMTPClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class Processor {

	private final CheckerService service;

	private final SMTPClient client;

	private final CheckerConfig checkerConfig;

	public void process() {
		checkerConfig.getEnableGroup().forEach(group -> {
			Group targetGroup = checkerConfig.getGroups().get(group);
			if (ObjectUtils.isEmpty(targetGroup)) {
				return;
			}

			for (String domain : targetGroup.getDomains()) {
				LocalDateTime expireDate = service.getSSLCertificateExpirationDate(domain);
				if (expireDate == null) {
					continue;
				}

				// Calculate the remaining days until the expiration date
				Duration duration = Duration.between(LocalDateTime.now(), expireDate);
				int day = Long.valueOf(duration.toDays()).intValue();

				// Send an email if the specified date condition is met
				if (targetGroup.getCondition().contains(day)) {
					client.sendEmail(targetGroup.getRecipients(), domain, expireDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
				}
			}
		});
	}

}
