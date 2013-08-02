package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Category extends ModelBase implements Serializable {

	@SerializedName("name")
	private String name;

	@SerializedName("isPositive")
	private Boolean isPositive;

	@SerializedName("parentId")
	private int parentId;

	@SerializedName("parameters")
	private Parameter[] parameters;

	@SerializedName("RN")
	private String rn;

	private String description = "";

	private String lvl = "";

	public Category() {

	}

	public Category(int id, String name, boolean isActive) {
		super.setId(id);
		super.setActive(isActive);
		this.setName(name);

	}

	public Category(int id) {
		super.setId(id);
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
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
	 * @return the attributes
	 */
	public Parameter[] getAttributes() {
		return parameters;
	}

	/**
	 * @param attributes
	 *            the attributes to set
	 */
	public void setAttributes(Parameter[] parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return the rn
	 */
	public String getRn() {
		return rn;
	}

	/**
	 * @param rn
	 *            the rn to set
	 */
	public void setRn(String rn) {
		this.rn = rn;
		if (rn != null) {
			String[] items = rn.split("\\.");
			if (items.length > 1) {
				for (int i = 0; i < items.length - 1; i++) {
					lvl += "--";
				}
			}
		}
	}

	private boolean checkDescription = false;

	public void setDescription() {
		// if (!checkDescription) {
		// checkDescription = true;
		// AndroidGlobals globals = AndroidGlobals.getInstance();
		//
		// if (attributes != null && attributes.length > 0) {
		// StringBuilder sb = new StringBuilder();
		// sb.append("[");
		// boolean isFirst = true;
		// for (Parameter item : attributes) {
		// Parameter p = globals.getParameterById(item.getId());
		// if (!isFirst)
		// sb.append(", ");
		//
		// sb.append(p.getName());
		//
		// if (isFirst)
		// isFirst = false;
		// }
		// sb.append("]");
		// this.description = sb.toString();
		// } else
		// this.description = "";
		// }
	}

	public String getParameters() {
		return this.description;
	}

	public String getLvl() {
		return lvl;
	}

	public void setLvl(String lvl) {
		this.lvl = lvl;
	}

	public Boolean isPositive() {
		return isPositive;
	}

	public void setIsPositive(Boolean isPositive) {
		this.isPositive = isPositive;
	}

}
