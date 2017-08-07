/**
 * 
 */
/**
 * @author XtremeAlex
 *
 */
package com.xa.mwo.user.service;

import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class Utente implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String urlLogIn = "https://mwomercs.com/do/login";
	private String urlProfile = "https://mwomercs.com/profile";
	private String urlStats = "https://mwomercs.com/profile/stats";
	private String urlStatsMech = "https://mwomercs.com/profile/stats?type=mech";
	private String urlStatsWeapon = "https://mwomercs.com/profile/stats?type=weapon";
	private String urlStatsMap = "https://mwomercs.com/profile/stats?type=map";
	private String urlStatsMode = "https://mwomercs.com/profile/stats?type=mode";

	private String email;
	private String password;
	private Map<String, String> loginCookies;

	private JSONArray data;

	public Utente() {
		super();
	}

	public Utente(String email, String password) {
		this.email = email;
		this.password = password;
	}

	public String getUrlLogIn() {
		return urlLogIn;
	}

	public void setUrlLogIn(String urlLogIn) {
		this.urlLogIn = urlLogIn;
	}

	public String getUrlProfile() {
		return urlProfile;
	}

	public void setUrlProfile(String urlProfile) {
		this.urlProfile = urlProfile;
	}

	public String getUrlStats() {
		return urlStats;
	}

	public void setUrlStats(String urlStats) {
		this.urlStats = urlStats;
	}

	public String getUrlStatsMech() {
		return urlStatsMech;
	}

	public void setUrlStatsMech(String urlStatsMech) {
		this.urlStatsMech = urlStatsMech;
	}

	public String getUrlStatsWeapon() {
		return urlStatsWeapon;
	}

	public void setUrlStatsWeapon(String urlStatsWeapon) {
		this.urlStatsWeapon = urlStatsWeapon;
	}

	public String getUrlStatsMap() {
		return urlStatsMap;
	}

	public void setUrlStatsMap(String urlStatsMap) {
		this.urlStatsMap = urlStatsMap;
	}

	public String getUrlStatsMode() {
		return urlStatsMode;
	}

	public void setUrlStatsMode(String urlStatsMode) {
		this.urlStatsMode = urlStatsMode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Map<String, String> getLoginCookies() {
		return loginCookies;
	}

	public void setLoginCookies(Map<String, String> loginCookies) {
		this.loginCookies = loginCookies;
	}

	public JSONArray getData() {
		return data;
	}

	public void setData(JSONArray data) {
		this.data = data;
	}

	// TODO: NO PASSWORD
	@Override
	public String toString() {
		return "Utente [urlLogIn=" + urlLogIn + ", urlProfile=" + urlProfile + ", urlStats=" + urlStats
				+ ", urlStatsMech=" + urlStatsMech + ", urlStatsWeapon=" + urlStatsWeapon + ", urlStatsMap="
				+ urlStatsMap + ", urlStatsMode=" + urlStatsMode + ", email=" + email + ", loginCookies=" + loginCookies
				+ ", data=" + data + "]";
	}

}
