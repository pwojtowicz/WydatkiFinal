package pl.wppiotrek.wydatki.basepackage.singletons;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.entities.Category;
import pl.wppiotrek.wydatki.basepackage.entities.ModelBase;
import pl.wppiotrek.wydatki.basepackage.entities.Parameter;
import pl.wppiotrek.wydatki.basepackage.entities.Project;
import android.util.SparseArray;

public class SingletonLoadedWebContent {

	private static volatile SingletonLoadedWebContent instance = null;
	private boolean wasContentLoadedAtStart;

	private SparseArray<Account> accounts = new SparseArray<Account>();
	private SparseArray<Category> categories = new SparseArray<Category>();
	private SparseArray<Parameter> parameters = new SparseArray<Parameter>();
	private SparseArray<Project> projects = new SparseArray<Project>();

	public static SingletonLoadedWebContent getInstance() {
		if (instance == null) {
			synchronized (SingletonLoadedWebContent.class) {
				if (instance == null) {
					instance = new SingletonLoadedWebContent();
				}
			}
		}
		return instance;
	}

	private SingletonLoadedWebContent() {
		setContentLoadedAtStart(false);
	}

	public void setAccounts(ArrayList<Account> items) {
		accounts.clear();
		if (items != null)
			for (Account item : items) {
				accounts.put(item.getId(), item);
			}
	}

	public void setCategories(ArrayList<Category> items) {
		categories.clear();
		if (items != null)
			for (Category item : items) {
				categories.put(item.getId(), item);
			}
	}

	public void setParameters(ArrayList<Parameter> items) {
		parameters.clear();
		if (items != null)
			for (Parameter item : items) {
				parameters.put(item.getId(), item);
			}
	}

	public void setProjects(ArrayList<Project> items) {
		projects.clear();
		if (items != null)
			for (Project item : items) {
				projects.put(item.getId(), item);
			}
	}

	public ArrayList<Account> getAccounts() {
		ArrayList<Account> items = new ArrayList<Account>();
		for (int i = 0; i < accounts.size(); i++) {
			items.add(accounts.valueAt(i));
		}
		return items;
	}

	public ArrayList<Category> getCategories() {
		ArrayList<Category> items = new ArrayList<Category>();
		for (int i = 0; i < categories.size(); i++) {
			items.add(categories.valueAt(i));
		}
		return items;
	}

	public ArrayList<Parameter> getParameters() {
		ArrayList<Parameter> items = new ArrayList<Parameter>();
		for (int i = 0; i < parameters.size(); i++) {
			items.add(parameters.valueAt(i));
		}
		return items;
	}

	public ArrayList<Project> getProjects() {
		ArrayList<Project> items = new ArrayList<Project>();
		for (int i = 0; i < projects.size(); i++) {
			items.add(projects.valueAt(i));
		}
		return items;
	}

	public boolean isContentLoadedAtStart() {
		return wasContentLoadedAtStart;
	}

	public void setContentLoadedAtStart(boolean wasContentLoadedAtStart) {
		this.wasContentLoadedAtStart = wasContentLoadedAtStart;
	}

	public void insertItemToLoadedContent(ModelBase item) {
		if (item instanceof Account) {
			accounts.put(item.getId(), (Account) item);
		} else if (item instanceof Category) {
			categories.put(item.getId(), (Category) item);
		} else if (item instanceof Parameter) {
			parameters.put(item.getId(), (Parameter) item);
		} else if (item instanceof Project) {
			projects.put(item.getId(), (Project) item);
		} else
			try {
				throw new Exception("Unsupported object");
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	public Category getCategoryById(int categoryId) {
		return categories.get(categoryId, null);
	}

	public Parameter getParameterById(int id) {
		return parameters.get(id, null);
	}

	public Account getAccountById(int id) {
		return accounts.get(id, null);
	}

}
