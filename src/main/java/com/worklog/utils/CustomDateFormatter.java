package com.worklog.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class CustomDateFormatter {
	
	private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public static String toLocalFormat(String date) {
		if (date != null) {
			return LocalDate.parse(date).format(formatter);
		}
		return date;

	}

	public static String toLocalFormat(String date, boolean isDateTime) {
		if (date != null) {
			DateTimeFormatter fromDbFormatter = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").optionalStart()
							.appendFraction(ChronoField.NANO_OF_SECOND, 1, 9, true).optionalEnd().toFormatter();
			LocalDateTime dbDate = LocalDateTime.parse(date, fromDbFormatter);
			return dbDate.format(formatter);
		}
		return date;
		}

}
