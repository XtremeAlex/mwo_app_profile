/**
 * 
 */
/**
 * @author XtremeAlex
 *
 */
package com.xa.mwo.login;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.xa.mwo.user.service.Utente;

public class LogIn {

	public void enableSSLSocket() throws KeyManagementException, NoSuchAlgorithmException {
		HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});

		SSLContext context = SSLContext.getInstance("TLS");
		context.init(null, new X509TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return new X509Certificate[0];
			}
		} }, new SecureRandom());
		HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
	}

	public Boolean logIn(Utente utente) throws IOException, KeyManagementException, NoSuchAlgorithmException {
		try {

			enableSSLSocket();

			Connection.Response loginForm = Jsoup.connect(utente.getUrlLogIn()).data("cookieexists", "false")
					.data("email", utente.getEmail(), "password", utente.getPassword()).method(Method.POST).execute();

			// TODO: COOKIES
			Map<String, String> loginCookies = loginForm.cookies();

			utente.setLoginCookies(loginCookies);

			return isLogged(utente);

		} catch (Exception e) {
			throw new RuntimeException();
		}
	}

	private Boolean isLogged(Utente utente) throws IOException {

		// Get page LogIn
		Document documentoStatisticheProfilo = Jsoup.connect(utente.getUrlProfile()).cookies(utente.getLoginCookies())
				.timeout(2000).get();

		Elements divsControllo = documentoStatisticheProfilo.select("li:nth-child(10)");

		Boolean result = true;
		if (divsControllo.text().equals("LOGIN")) {
			result = false;
		}
		return result;
	}
}
