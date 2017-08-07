/**
 * 
 */
/**
 * @author XtremeAlex
 *
 */
package com.xa.mwo.user.service;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class AnalyzeSource {

	private Integer timeLow = 3000;
	private Integer timeLong = 30000;

	public AnalyzeSource() {
	}

	public Utente findAllInfo(Utente utente) throws Exception, KeyManagementException, NoSuchAlgorithmException {

		findInfo(utente);
		findBaseStatistics(utente);
		findMechAviable(utente);
		findMechStatisticsCurrent(utente);
		findWeaponStatisticsCurrent(utente);
		findMapsStatisticsCurrent(utente);
		findModeStatisticsCurrent(utente);

		return utente;

	}

	public Utente findInfo(Utente utente) throws Exception, KeyManagementException, NoSuchAlgorithmException {
		enableSSLSocket();

		Document htmlProfile = Jsoup.connect(utente.getUrlProfile()).cookies(utente.getLoginCookies()).timeout(timeLow)
				.get();

		JSONObject info = new JSONObject();

		info.put("Today_Date", new Date().toString());
		info.put("Today_Date_Json", new Date());

		Elements divsProfiloNome = htmlProfile.select("div.profilePilotName > h1");
		String nomePlayer = divsProfiloNome.text();
		info.put("Pilot_Name", nomePlayer);

		// Faction Image
		Element divsProfiloImg = htmlProfile.select("div.profileHouseImage > a > img").first();
		Element divsProfiloImgBandiera = htmlProfile
				.select("div.profileBox > table > tbody > tr:nth-child(5) > td:nth-child(2) > img").first();
		String urlPlayerImage = divsProfiloImg.absUrl("src");
		String nomeFazione = urlPlayerImage.substring(urlPlayerImage.indexOf("e/") + 2, urlPlayerImage.indexOf(".jpg"));
		info.put("Faction_Name", nomeFazione.replaceAll("-", " ").toUpperCase());
		info.put("Faction_Flag", urlPlayerImage);

		// Bandiera Image
		String urlPlayerImageBandiera = divsProfiloImgBandiera.absUrl("src");
		info.put("Country_Flag", urlPlayerImageBandiera);

		JSONObject infoProfileJson = new JSONObject();
		infoProfileJson.put("_info", info);

		JSONArray data;
		if (utente.getData() != null) {
			data = utente.getData();
		} else {
			data = new JSONArray();
		}

		data.add(infoProfileJson);
		utente.setData(data);

		return utente;

	}

	public Utente findMechAviable(Utente utente) throws Exception, KeyManagementException, NoSuchAlgorithmException {
		enableSSLSocket();

		Document htmlProfile = Jsoup.connect(utente.getUrlProfile()).cookies(utente.getLoginCookies()).timeout(timeLow)
				.get();

		Elements divsRicercaMech = htmlProfile.select("div.mechname");
		// Ricerca e Creazone di un ArrayList di tutti i mech
		List<String> listaMech = new ArrayList();
		for (Element div : divsRicercaMech) {
			if (div.ownText().equals("| | |"))
				break;
			listaMech.add(div.ownText());
		}
		// Giustifica array A-Z
		Collections.sort(listaMech);

		JSONObject accountMech = new JSONObject();
		for (String nomeMech : listaMech) {
			JSONObject dettaglioMech = new JSONObject();
			dettaglioMech.put("img",
					"https://mwomercs.com/static/img/theme/mechbay/" + nomeMech.toLowerCase() + ".png");
			accountMech.put(nomeMech, dettaglioMech);
		}

		JSONObject mechAvailableJson = new JSONObject();
		mechAvailableJson.put("mech_aviable", accountMech);

		JSONArray data;
		if (utente.getData() != null) {
			data = (JSONArray) utente.getData();
		} else {
			data = new JSONArray();
		}

		data.add(mechAvailableJson);
		utente.setData(data);

		return utente;
	}

	public Utente findBaseStatistics(Utente utente) throws Exception, KeyManagementException, NoSuchAlgorithmException {
		enableSSLSocket();

		Document htmlProfileStats = Jsoup.connect(utente.getUrlStats()).cookies(utente.getLoginCookies())
				.timeout(timeLow).get();
		Elements divsRicercaStats = htmlProfileStats.select("tbody");

		JSONObject baseStatisticsJson = new JSONObject();

		for (Element table : divsRicercaStats) {
			for (Element row : table.select("tr")) {
				JSONObject rowTableJson = new JSONObject();
				Elements tds = row.select("td");
				if (tds.size() == 1) {
					continue;
				} else {
					rowTableJson.put("Value", tds.get(1).text());
					baseStatisticsJson.put(tds.get(0).text(), rowTableJson);
				}
			}
		}

		JSONObject statsBase = new JSONObject();
		statsBase.put("base_statistics ", baseStatisticsJson);

		JSONArray data;
		if (utente.getData() != null) {
			data = (JSONArray) utente.getData();
		} else {
			data = new JSONArray();
		}

		data.add(statsBase);
		utente.setData(data);

		return utente;

	}

	public Utente findMechStatisticsCurrent(Utente utente)
			throws Exception, KeyManagementException, NoSuchAlgorithmException {
		enableSSLSocket();

		Document htmlProfileStats = Jsoup.connect(utente.getUrlStatsMech()).cookies(utente.getLoginCookies())
				.timeout(timeLong).get();

		JSONObject statisticheAccountMechStatsJson = new JSONObject();
		for (Element table : htmlProfileStats.select("table > tbody")) {
			for (Element row : table.select("tr")) {
				JSONObject jsonObject = new JSONObject();
				Elements tds = row.select("td");

				String nomeMech = tds.get(0).text();
				String matchesPlayed = tds.get(1).text();
				String wins = tds.get(2).text();
				String losses = tds.get(3).text();
				String ratioWL = tds.get(4).text();
				String kills = tds.get(5).text();
				String deaths = tds.get(6).text();
				String ratioKD = tds.get(7).text();
				String damageDone = tds.get(8).text();
				String xPEarned = tds.get(9).text();
				String timePlayed = tds.get(10).text();

				jsonObject.put("Nome-Mech", nomeMech);
				jsonObject.put("Matches-Played", matchesPlayed);
				jsonObject.put("Wins", wins);
				jsonObject.put("Losses", losses);
				jsonObject.put("Ratio-W/L", ratioWL);
				jsonObject.put("Kills", kills);
				jsonObject.put("Deaths", deaths);
				jsonObject.put("Ratio-K/D", ratioKD);
				jsonObject.put("Damage-Done", damageDone);
				jsonObject.put("XP-Earned", xPEarned);
				jsonObject.put("Time-Played", timePlayed);

				// Estrapolare Sigla Mech
				String nomeMechCompleto = tds.get(0).text();
				String[] spazio = nomeMechCompleto.split(" ");
				String siglaNomeMech = spazio[spazio.length - 1];

				statisticheAccountMechStatsJson.put(siglaNomeMech, jsonObject);

			}
		}

		JSONObject statsMech = new JSONObject();
		statsMech.put("mech_stats ", statisticheAccountMechStatsJson);

		JSONArray data;
		if (utente.getData() != null) {
			data = (JSONArray) utente.getData();
		} else {
			data = new JSONArray();
		}

		data.add(statsMech);
		utente.setData(data);

		return utente;

	}

	public Utente findWeaponStatisticsCurrent(Utente utente)
			throws Exception, KeyManagementException, NoSuchAlgorithmException {
		enableSSLSocket();

		Document htmlProfileStats = Jsoup.connect(utente.getUrlStatsWeapon()).cookies(utente.getLoginCookies())
				.timeout(timeLong).get();

		JSONObject statisticheAccountWeaponStatsJson = new JSONObject();
		for (Element table : htmlProfileStats.select("table > tbody")) {
			for (Element row : table.select("tr")) {
				JSONObject jsonObject = new JSONObject();
				Elements tds = row.select("td");

				String nameWeapon = tds.get(0).text();
				String matchesPlayed = tds.get(1).text();
				String fired = tds.get(2).text();
				String hit = tds.get(3).text();
				String accuracy = tds.get(4).text();
				String timeEquip = tds.get(5).text();
				String damageDone = tds.get(6).text();

				jsonObject.put("Name-Weapon", nameWeapon);
				jsonObject.put("Matches-Played", matchesPlayed);
				jsonObject.put("Fired", fired);
				jsonObject.put("Hit", hit);
				jsonObject.put("Accuracy", accuracy);
				jsonObject.put("Time-Equip", timeEquip);
				jsonObject.put("Damage-Done", damageDone);

				statisticheAccountWeaponStatsJson.put(nameWeapon, jsonObject);
			}
		}

		JSONObject statsWeapon = new JSONObject();
		statsWeapon.put("weapon_stats ", statisticheAccountWeaponStatsJson);

		JSONArray data;
		if (utente.getData() != null) {
			data = (JSONArray) utente.getData();
		} else {
			data = new JSONArray();
		}

		data.add(statsWeapon);
		utente.setData(data);

		return utente;

	}

	public Utente findMapsStatisticsCurrent(Utente utente)
			throws Exception, KeyManagementException, NoSuchAlgorithmException {
		enableSSLSocket();

		Document htmlProfileStats = Jsoup.connect(utente.getUrlStatsMap()).cookies(utente.getLoginCookies())
				.timeout(timeLong).get();

		JSONObject statisticheAccountMapsStatsJson = new JSONObject();
		for (Element table : htmlProfileStats.select("table > tbody")) {
			for (Element row : table.select("tr")) {
				JSONObject jsonObject = new JSONObject();
				Elements tds = row.select("td");

				String nameMap = tds.get(0).text();
				String totalMatchesPlayed = tds.get(1).text();
				String wins = tds.get(2).text();
				String losses = tds.get(3).text();
				String time = tds.get(4).text();

				jsonObject.put("Name-Map", nameMap);
				jsonObject.put("Total-Matches-Played", totalMatchesPlayed);
				jsonObject.put("Wins", wins);
				jsonObject.put("Losses", losses);
				jsonObject.put("Time", time);

				String stringNameForUrl = nameMap.replaceAll(" ", "-").toLowerCase();
				jsonObject.put("img", "https://mwomercs.com/static/img/game/maps/" + stringNameForUrl + ".jpg");

				statisticheAccountMapsStatsJson.put(nameMap, jsonObject);
			}
		}

		JSONObject statsMaps = new JSONObject();
		statsMaps.put("maps_statistics ", statisticheAccountMapsStatsJson);

		JSONArray data;
		if (utente.getData() != null) {
			data = (JSONArray) utente.getData();
		} else {
			data = new JSONArray();
		}

		data.add(statsMaps);
		utente.setData(data);

		return utente;
	}

	public Utente findModeStatisticsCurrent(Utente utente)
			throws Exception, KeyManagementException, NoSuchAlgorithmException {
		enableSSLSocket();

		Document htmlProfileStats = Jsoup.connect(utente.getUrlStatsMode()).cookies(utente.getLoginCookies())
				.timeout(timeLong).get();

		JSONObject statisticheAccountModeStatsJson = new JSONObject();
		for (Element table : htmlProfileStats.select("table > tbody")) {
			for (Element row : table.select("tr")) {
				JSONObject jsonObject = new JSONObject();
				Elements tds = row.select("td");

				String nameMode = tds.get(0).text();
				String totalMatchesPlayed = tds.get(1).text();
				String wins = tds.get(2).text();
				String losses = tds.get(3).text();
				String ratioWL = tds.get(4).text();
				String xPEarned = tds.get(5).text();
				String time = tds.get(6).text();

				jsonObject.put("Name-Mode", nameMode);
				jsonObject.put("Total-Matches-Played", totalMatchesPlayed);
				jsonObject.put("Wins", wins);
				jsonObject.put("Losses", losses);
				jsonObject.put("Ratio-W/L", ratioWL);
				jsonObject.put("XP-Earned", xPEarned);
				jsonObject.put("Time", time);

				statisticheAccountModeStatsJson.put(nameMode, jsonObject);
			}
		}

		JSONObject statsMode = new JSONObject();
		statsMode.put("mode_statistics ", statisticheAccountModeStatsJson);

		JSONArray data;
		if (utente.getData() != null) {
			data = (JSONArray) utente.getData();
		} else {
			data = new JSONArray();
		}

		data.add(statsMode);
		utente.setData(data);

		return utente;

	}

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

}
