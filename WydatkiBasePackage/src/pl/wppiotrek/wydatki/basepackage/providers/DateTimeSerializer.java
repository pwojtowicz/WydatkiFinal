package pl.wppiotrek.wydatki.basepackage.providers;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class DateTimeSerializer implements JsonSerializer<Date> {

	public JsonElement serialize(Date src, Type typeOfSrc,
			JsonSerializationContext context) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

		return new JsonPrimitive(df.format(src).toString());
	}
}
