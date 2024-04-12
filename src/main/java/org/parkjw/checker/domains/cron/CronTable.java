package org.parkjw.checker.domains.cron;

import lombok.RequiredArgsConstructor;
import org.parkjw.checker.domains.checker.service.Processor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CronTable {

	private final Processor processor;

	@Scheduled(cron = "${checker.cron}")
	public void cron() {

		processor.process();
	}
}
