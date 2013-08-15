package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;

public class Transaction extends BaseTransaction implements Serializable {

	private Category category;

	/**
	 * @return the category
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * @param category
	 *            the category to set
	 */
	public void setCategory(Category category) {
		this.category = category;
		if (this.category != null)
			super.categoryId = this.category.getId();
		else
			super.categoryId = 0;
	}

}
