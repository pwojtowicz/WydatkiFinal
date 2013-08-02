package pl.wppiotrek.wydatki.basepackage.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import com.google.gson.annotations.SerializedName;

public class ItemContainer<T> implements Serializable {

	@SerializedName("allItemsCount")
	int allItemsCount;

	@SerializedName("returmItemsCount")
	int returnItemsCount;

	@SerializedName("items")
	T[] items;

	public T[] getItemsArray() {
		return items;
	}

	public ArrayList<T> getItemsList() {
		return new ArrayList<T>(Arrays.asList(items));
	}

}
