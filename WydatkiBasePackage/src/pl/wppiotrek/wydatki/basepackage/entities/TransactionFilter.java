package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionFilter implements Serializable {

	private int startAt = 0;
	private int takeCount = 15;

	private Date dateFrom;
	private Date dateTo;

	private Double valueFrom;
	private Double valueTo;

	private Integer accountId;
	private Integer categoryId;

	private String note;

	/*
	 * Ustawia na starcie domyœlne wartoœæ pobieranie transakcji. Brak filtrów,
	 * pobieranie elemetów od pierwszego, pobieramy 15.
	 */
	public TransactionFilter() {
		this(0, 15);
	}

	public TransactionFilter(int startAt, int takeCount) {
		this.setStartAt(startAt);
		this.setTakeCount(takeCount);
	}

	public String getFilterString() {
		StringBuilder sb = new StringBuilder();

		sb.append("?startAt=" + getStartAt());
		sb.append("&takeCount=" + getTakeCount());

		if (getAccountId() != null && getAccountId() > 0)
			sb.append("&accountId=" + getAccountId());

		if (getNote() != null && getNote().length() > 0)
			sb.append("&note=" + getNote());

		if (getCategoryId() != null && getCategoryId() > 0)
			sb.append("&categoryId=" + getCategoryId());

		if (getValueFrom() != null && getValueFrom() > 0)
			sb.append("&valueFrom=" + getValueFrom());

		if (getValueTo() != null && getValueTo() > 0)
			sb.append("&valueTo=" + getValueTo());

		if (getDateFrom() != null)
			sb.append("&valueFrom=" + getStringFromDate(getDateFrom()));

		if (getDateTo() != null)
			sb.append("&dateTo=" + getStringFromDate(getDateTo()));

		return sb.toString();
	}

	private String getStringFromDate(Date source) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
		return df.format(source).toString();
	}

	public Date getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	public Date getDateTo() {
		return dateTo;
	}

	public void setDateTo(Date dateTo) {
		this.dateTo = dateTo;
	}

	public Double getValueFrom() {
		return valueFrom;
	}

	public void setValueFrom(Double valueFrom) {
		this.valueFrom = valueFrom;
	}

	public Double getValueTo() {
		return valueTo;
	}

	public void setValueTo(Double valueTo) {
		this.valueTo = valueTo;
	}

	public Integer getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public int getStartAt() {
		return startAt;
	}

	public void setStartAt(int startAt) {
		this.startAt = startAt;
	}

	public int getTakeCount() {
		return takeCount;
	}

	public void setTakeCount(int takeCount) {
		this.takeCount = takeCount;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public boolean isExtendFiltering() {
		if (getNote() != null && getNote().length() > 0)
			return true;

		if (getCategoryId() != null && getCategoryId() > 0)
			return true;

		if (getValueFrom() != null && getValueFrom() > 0)
			return true;

		if (getValueTo() != null && getValueTo() > 0)
			return true;

		if (getDateFrom() != null)
			return true;

		if (getDateTo() != null)
			return true;
		return false;
	}

}
