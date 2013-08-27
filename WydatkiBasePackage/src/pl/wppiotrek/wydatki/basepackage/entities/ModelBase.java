package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ModelBase implements Serializable {

	@SerializedName("id")
	private int id;

	@SerializedName("isActive")
	private boolean isActive;

	public ModelBase cloneBase() {
		ModelBase clone = new ModelBase();
		clone.setId(this.getId());
		clone.setActive(this.isActive());
		return clone;
	}

	public ModelBase() {

	}

	public ModelBase(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
}
