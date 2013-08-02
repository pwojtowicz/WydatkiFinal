package pl.wppiotrek.wydatki.basepackage.repositories;

import pl.wppiotrek.wydatki.basepackage.database.DataBaseManager;
import android.os.Bundle;

public abstract class BaseRepository<T> {
	
	protected final DataBaseManager dbm;
	protected boolean useTransaction;
	protected String baseTable;

	public BaseRepository(DataBaseManager dbm, boolean useTransaction, String baseTable) {
		if (dbm != null)
			this.dbm = dbm;
		else
			this.dbm = DataBaseManager.getInstance();
		this.useTransaction = useTransaction;
		if(baseTable==null){
			throw new NoSuchFieldError("NO BASE TABLE");
		}
		this.baseTable=baseTable;
		
	}
	
	public abstract boolean create(T item);
	
	public abstract T readAll(Bundle extras);
	
	public abstract T readById(int itemId,Bundle extras);
	
	public abstract boolean update(T item);
	
	public abstract boolean delete(T item, Bundle extras);

}
