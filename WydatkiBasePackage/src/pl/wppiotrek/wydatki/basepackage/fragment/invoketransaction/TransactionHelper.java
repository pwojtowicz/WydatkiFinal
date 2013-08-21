package pl.wppiotrek.wydatki.basepackage.fragment.invoketransaction;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;

public class TransactionHelper implements Serializable {

	BaseTransaction transaction = new BaseTransaction();

	int accMinusPosition = -1;
	int accPlusPosition = -1;
	int categoryPosition = -1;
	int projektPosition = -1;
	String value = "";

	int year;
	int month;
	int day;
	int hour;
	int minute;

	public void setDate(Date date) {
		if (date == null)
			date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());

		year = cal.get(Calendar.YEAR);
		month = cal.get(Calendar.MONTH);
		day = cal.get(Calendar.DAY_OF_MONTH);

		hour = cal.get(Calendar.HOUR_OF_DAY);
		minute = cal.get(Calendar.MINUTE);
	}
}
