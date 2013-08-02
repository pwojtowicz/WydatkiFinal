package pl.wppiotrek.wydatki.basepackage.entities;

import com.google.gson.annotations.SerializedName;

public class Project extends ModelBase {

	@SerializedName("name")
	private String name;

	@SerializedName("isVisibleForAll")
	private Boolean isVisibleForAll;

	@SerializedName("ownerUserId")
	private int ownerId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getIsVisibleForAll() {
		return isVisibleForAll;
	}

	public void setIsVisibleForAll(Boolean isVisibleForAll) {
		this.isVisibleForAll = isVisibleForAll;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

}
