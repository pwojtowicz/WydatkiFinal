package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.SerializedName;

public class BaseTransaction extends ModelBase implements Serializable {

	@SerializedName("minusId")
	protected int accMinus;

	@SerializedName("plusId")
	protected int accPlus;

	@SerializedName("note")
	protected String note;

	@SerializedName("value")
	protected Double value;

	@SerializedName("actionDate")
	protected Date date;

	@SerializedName("categoryId")
	private int categoryId;

	@SerializedName("projectId")
	protected int projectId;

	@SerializedName("parameters")
	private Parameter[] parameters;

	@SerializedName("objectHash")
	private String objectHash;

	/**
	 * @return the accMinus
	 */
	public int getAccMinus() {
		return accMinus;
	}

	public int getCategoryId() {
		return categoryId;
	}

	/**
	 * @param accMinus
	 *            the accMinus to set
	 */
	public void setAccMinus(int accMinus) {
		this.accMinus = accMinus;
	}

	/**
	 * @return the accPlus
	 */
	public int getAccPlus() {
		return accPlus;
	}

	/**
	 * @param accPlus
	 *            the accPlus to set
	 */
	public void setAccPlus(int accPlus) {
		this.accPlus = accPlus;
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the value
	 */
	public Double getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(Double value) {
		this.value = value;
	}

	/**
	 * @return the date
	 */
	public java.util.Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(java.util.Date date) {
		this.date = date;
	}

	public int getProjectId() {
		return projectId;
	}

	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	public Parameter[] getParameters() {
		return parameters;
	}

	public void setParameters(Parameter[] parameters) {
		this.parameters = parameters;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getObjectHash() {
		return objectHash;
	}

	public void setObjectHash(String objectHash) {
		this.objectHash = objectHash;
	}

}
