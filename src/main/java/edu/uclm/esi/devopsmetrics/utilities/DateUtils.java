package edu.uclm.esi.devopsmetrics.utilities;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

/**
*
* @author FcoCrespo
* 
*/
public class DateUtils {

	private DateUtils() {

	}

	public static Instant[] getDatesInstant(String begindate, String enddate) {

		String[] daymonthenddate = enddate.split(" ");

		
		daymonthenddate[1] = "23:59";
	    enddate = daymonthenddate[0] + " " + daymonthenddate[1];
		

		Instant[] instants = new Instant[2];

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm", Locale.US);
		ZoneId z = ZoneId.of("America/Toronto");

		LocalDateTime ldtBegin = LocalDateTime.parse(begindate, formatter);
		ZonedDateTime zdtBegin = ldtBegin.atZone(z);

		LocalDateTime ldtEnd = LocalDateTime.parse(enddate, formatter);
		ZonedDateTime zdtEnd = ldtEnd.atZone(z);

		instants[0] = zdtBegin.toInstant().minus(4, ChronoUnit.HOURS);
		instants[1] = zdtEnd.toInstant().minus(4, ChronoUnit.HOURS);

		return instants;

	}
}
