package com.worklog.utils;

import java.util.Objects;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 
 * PasswordProtector - Utility class used for password hashing and check the hashed password.
 * 
 * @author Vasudevan Tamizharasan
 * @since 20-01-2026
 */

public class PasswordProtector {

	public static String getHashedPassword(String password) {
		Objects.requireNonNull(password, "password from user is null.");
		return BCrypt.hashpw(password, BCrypt.gensalt(10));
	}

	public static boolean checkPassword(String password, String hashedPassword) {
		Objects.requireNonNull(password, "password from user is null.");
		Objects.requireNonNull(hashedPassword, "password from db is null.");
		return BCrypt.checkpw(password, hashedPassword);
	}

	public static void main(String[] args) {
		System.out.println(getHashedPassword("Employee@123"));
	}
}
