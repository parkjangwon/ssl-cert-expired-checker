package org.parkjw.checker.domains.checker.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Slf4j
@Component
@RequiredArgsConstructor
public class CheckerService {
	public LocalDateTime getSSLCertificateExpirationDate(String domain) {
		try {
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(null, new TrustManager[]{new X509TrustManager() {

				public X509Certificate[] getAcceptedIssuers() {

					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {

				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {

				}
			}}, null);

			URL url = new URL("https://" + domain);
			HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
			connection.setSSLSocketFactory(sslContext.getSocketFactory());

			connection.connect();
			X509Certificate[] certs = (X509Certificate[]) connection.getServerCertificates();
			connection.disconnect();

			if (certs != null && certs.length > 0) {
				X509Certificate cert = certs[0];
				Instant expirationInstant = cert.getNotAfter().toInstant();
				LocalDateTime expirationDateTime = expirationInstant.atZone(ZoneId.systemDefault()).toLocalDateTime();
				return expirationDateTime;
			}

			log.warn("Failed to retrieve certificate information.");
		} catch (IOException e) {
			log.error("Failed to retrieve domain information.", e);
		} catch (Exception e) {
			log.error("Failed to retrieve certificate information.", e);
		}

		return null;
	}
}
