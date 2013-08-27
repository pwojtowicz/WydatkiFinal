package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class AccountShort extends ModelBase implements Serializable {
	@SerializedName("balance")
	private Double balance;

	@SerializedName("lastUpdateDateTime")
	private Date lastActionDate;

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Date getLastActionDate() {
		return lastActionDate;
	}

	public void setLastActionDate(Date lastActionDate) {
		this.lastActionDate = lastActionDate;
	}

	public void setBalance(String string) {
		string = string.replace(",", ".").replace(" ", "");
		this.balance = Double.parseDouble(string);
	}
}
