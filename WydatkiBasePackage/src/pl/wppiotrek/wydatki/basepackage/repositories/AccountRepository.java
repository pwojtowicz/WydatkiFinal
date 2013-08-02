package pl.wppiotrek.wydatki.basepackage.repositories;

import java.util.ArrayList;
import java.util.Date;

import pl.wppiotrek.wydatki.basepackage.database.DataBaseHelper;
import pl.wppiotrek.wydatki.basepackage.database.DataBaseManager;
import pl.wppiotrek.wydatki.basepackage.entities.Account;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;

public class AccountRepository extends BaseRepository<Account> {

	private String readColumns = "ID,Name, Balance,LastActionDate, IsActive, IsSumInGlobalBalance, ImageIndex";

	private final String INSERT_TO_ACCOUNT = "INSERT INTO "
			+ DataBaseHelper.TABLE_ACCOUNTS
			+ "(Name, Balance,LastActionDate, IsActive, IsSumInGlobalBalance, ImageIndex)  Values(?,?,?,?,?,?)";

	private boolean doNotOpenAndCloseDB = false;

	public AccountRepository() {
		this(null, false);
	}

	public AccountRepository(DataBaseManager dbm, boolean useTransaction) {
		super(dbm, useTransaction, DataBaseHelper.TABLE_ACCOUNTS);
	}

	@Override
	public boolean create(Account item) {
		if (item != null) {
			try {
				dbm.checkIsOpen();

				SQLiteStatement insertStmt = dbm.getDataBase()
						.compileStatement(INSERT_TO_ACCOUNT);
				insertStmt.bindString(1, item.getName());
				insertStmt.bindDouble(2, item.getBalance());
				insertStmt.bindLong(3, item.getLastActionDate().getTime());
				insertStmt.bindLong(4, item.isActive() ? 1 : 0);
				insertStmt.bindLong(5, item.isSumInGlobalBalance() ? 1 : 0);
				insertStmt.bindLong(6, item.getImageIndex());

				return insertStmt.executeInsert() > 0;
			} finally {
				dbm.close();
			}
		}
		return false;
	}

	@Override
	public Account readAll(Bundle extras) {
		dbm.checkIsOpen();

		ArrayList<Account> list = new ArrayList<Account>();
		Cursor cursor = dbm.getDataBase().query(super.baseTable,
				new String[] { readColumns }, null, null, null, null, "Name");
		if (cursor.moveToFirst()) {
			do {
				list.add(readAccountFromCursor(cursor));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		dbm.close();
		return null;
	}

	private Account readAccountFromCursor(Cursor cursor) {
		Account a = new Account();
		a.setId(cursor.getInt(0));
		a.setName(cursor.getString(1));
		a.setBalance(cursor.getDouble(2));
		a.setLastActionDate(new Date(cursor.getLong(3)));
		a.setActive(cursor.getLong(4) == 1 ? true : false);
		a.setIsSumInGlobalBalance(cursor.getLong(5) == 1 ? true : false);
		a.setImageIndex((byte) cursor.getLong(6));
		// if (WydatkiGlobals.getInstance().isLocalVersion())
		a.setIsVisibleForAll(true);
		return a;
	}

	@Override
	public Account readById(int itemId, Bundle extras) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean update(Account item) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(Account item, Bundle extras) {
		// TODO Auto-generated method stub
		return false;
	}

}
