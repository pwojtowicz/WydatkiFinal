package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;

public class Transaction extends BaseTransaction implements Serializable {

	private Category category;

	public Transaction() {

	}

	public Transaction(BaseTransaction loadTransaction) {
		super.setId(loadTransaction.getId());
		super.setAccMinus(loadTransaction.getAccMinus());
		super.setAccPlus(loadTransaction.getAccPlus());
		super.setDate(loadTransaction.getDate());
		super.setNote(loadTransaction.getNote());
		super.setProjectId(loadTransaction.getProjectId());
		super.setValue(loadTransaction.getValue());
		super.setCategoryId(loadTransaction.getCategoryId());
	}

	public BaseTransaction toBaseTransaction() {
		BaseTransaction bt = new BaseTransaction();
		bt.setId(this.getId());
		bt.setAccMinus(this.getAccMinus());
		bt.setAccPlus(this.getAccPlus());
		bt.setDate(this.getDate());
		bt.setNote(this.getNote());
		bt.setProjectId(this.getProjectId());
		bt.setValue(this.getValue());
		bt.setCategoryId(this.getCategoryId());
		if (this.getCategory() != null)
			bt.setParameters(this.getCategory().getAttributes());
		return bt;
	}

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
			super.setCategoryId(this.category.getId());
		else
			super.setCategoryId(0);
	}

	public void setCategory(Category category, Parameter[] parameters) {
		this.category = category;
		if (this.category.getAttributes() != null && parameters != null)
			for (Parameter parameter : this.category.getAttributes()) {
				for (Parameter tmp : parameters) {
					if (tmp.getId() == parameter.getId())
						parameter.setValue(tmp.getValue());
				}
			}

	}

}
