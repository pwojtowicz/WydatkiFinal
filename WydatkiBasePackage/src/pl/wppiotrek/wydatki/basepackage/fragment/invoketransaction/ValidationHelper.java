package pl.wppiotrek.wydatki.basepackage.fragment.invoketransaction;

import pl.wppiotrek.wydatki.basepackage.entities.BaseTransaction;

public class ValidationHelper {
	boolean isValid = false;
	String errorMessage;
	BaseTransaction item;

	public boolean isValid() {
		if (item != null) {

			if (item.getValue() == 0.0) {
				errorMessage = "Nie podano kwoty, lub podana kwota jest zerowa.";
				isValid = false;
			} else if ((item.getAccMinus() == 0 || item.getAccPlus() == 0)
					&& item.getCategoryId() <= 0) {
				errorMessage = "Nie wybrano kategorii dla transakcji.";
				isValid = false;
			}

		}

		return isValid;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public BaseTransaction getItem() {
		return item;
	}
}
