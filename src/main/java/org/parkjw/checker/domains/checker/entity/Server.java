package org.parkjw.sslcertificatechecker.domains.checker.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Server {

	private String host;

	private int port;

	private boolean ssl;

	private boolean authentication;
}
