package pl.wppiotrek.wydatki.basepackage.entities;

import com.google.gson.annotations.SerializedName;

public class Parameter extends ModelBase {

	@SerializedName("name")
	private String name;

	@SerializedName("defaultValue")
	private String defaultValue;

	private String value;

	@SerializedName("type")
	private int typeId;

	@SerializedName("dataSource")
	private String dataSource;

	public Parameter() {

	}

	public Parameter(int id, String name, boolean isActive,
			String defaultValue, int typeId) {
		super.setId(id);
		this.setName(name);
		super.setActive(isActive);
		this.setDefaultValue(defaultValue);
		this.setTypeId(typeId);
	}

	public Parameter(int id, String value) {
		super.setId(id);
		this.setValue(value);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @return the typeId
	 */
	public int getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *            the typeId to set
	 */
	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}
}
