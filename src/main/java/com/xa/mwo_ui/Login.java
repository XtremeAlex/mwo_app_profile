/**
 * 
 */
/**
 * @author XtremeAlex
 *
 */
package com.xa.mwo_ui;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.inject.Inject;

import com.xa.mwo.login.LogIn;
import com.xa.mwo.user.service.Utente;

public class Login {

	public static boolean authenticate(Utente utente) {

		LogIn controllaLog = new LogIn();

		try {
			if (controllaLog.logIn(utente)) {
				return true;
			}
		} catch (KeyManagementException | NoSuchAlgorithmException | IOException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}


}