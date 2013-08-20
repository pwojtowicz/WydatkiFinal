package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class MultiOperationResult implements Serializable {

	@SerializedName("isSuccess")
	public boolean isSuccess;

	@SerializedName("results")
	private ObjectResult[] objectResults;

	public MultiOperationResult(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public ObjectResult[] getObjectResults() {
		return objectResults;
	}

	public void setObjectResults(ObjectResult[] objectResults) {
		this.objectResults = objectResults;
	}

	public class ObjectResult implements Serializable {
		@SerializedName("objectId")
		private int objectId;

		@SerializedName("objectHash")
		private String objectHash;

		public int getObjectId() {
			return objectId;
		}

		public void setObjectId(int objectId) {
			this.objectId = objectId;
		}

		public String getObjectHash() {
			return objectHash;
		}

		public void setObjectHash(String objectHash) {
			this.objectHash = objectHash;
		}
	}

}
