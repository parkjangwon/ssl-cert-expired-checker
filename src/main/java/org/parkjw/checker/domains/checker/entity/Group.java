package org.parkjw.sslcertificatechecker.domains.checker.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class Group {

	private List<String> recipients;

	private List<String> domains;
}
