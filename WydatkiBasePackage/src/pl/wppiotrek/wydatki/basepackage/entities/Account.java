package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Account extends AccountShort implements Serializable {

	@SerializedName("name")
	private String name;

	@SerializedName("isVisibleForAll")
	private Boolean isVisibleForAll;

	@SerializedName("isSumInGlobalBalance")
	private Boolean isSumInGlobalBalance;

	@SerializedName("imageIndex")
	private Byte imageIndex;

	@SerializedName("ownerUserId")
	private int ownerId;

	public Account() {

	}

	public Account(int id) {
		super.setId(id);
	}

	public Account(int id, String name, Double balance, Boolean isActive) {
		super.setId(id);
		setName(name);
		this.setBalance(balance);
		super.setActive(isActive);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the isVisibleForAll
	 */
	public Boolean isVisibleForAll() {
		return isVisibleForAll;
	}

	/**
	 * @param isVisibleForAll
	 *            the isVisibleForAll to set
	 */
	public void setIsVisibleForAll(Boolean isVisibleForAll) {
		this.isVisibleForAll = isVisibleForAll;
	}

	/**
	 * @return the isSumInGlobalBalance
	 */
	public Boolean isSumInGlobalBalance() {
		return isSumInGlobalBalance;
	}

	/**
	 * @param isSumInGlobalBalance
	 *            the isSumInGlobalBalance to set
	 */
	public void setIsSumInGlobalBalance(Boolean isSumInGlobalBalance) {
		this.isSumInGlobalBalance = isSumInGlobalBalance;
	}

	public Byte getImageIndex() {
		return imageIndex;
	}

	public void setImageIndex(Byte imageIndex) {
		this.imageIndex = imageIndex;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

}
