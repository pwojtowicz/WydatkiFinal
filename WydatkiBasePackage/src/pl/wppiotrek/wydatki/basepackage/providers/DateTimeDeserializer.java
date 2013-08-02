package pl.wppiotrek.wydatki.basepackage.providers;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

class DateTimeDeserializer implements JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		Date date = null;
		String dateString = json.toString().replace("\"", "");
		if (dateString.contains("T")) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd'T'HH:mm:ss");
			if (dateString.length() > 19)
				format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
			try {
				date = format.parse(dateString);

			} catch (ParseException e) {
				throw new JsonParseException(e);
			}
		}
		return date;
	}

}
