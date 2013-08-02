package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class OperationResult implements Serializable {

	@SerializedName("isSuccess")
	public boolean isSuccess;

	public OperationResult(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
}
