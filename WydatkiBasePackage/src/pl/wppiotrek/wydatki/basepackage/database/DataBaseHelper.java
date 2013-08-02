package pl.wppiotrek.wydatki.basepackage.database;

import java.util.ArrayList;

import pl.wppiotrek.wydatki.basepackage.entities.Account;
import pl.wppiotrek.wydatki.basepackage.repositories.AccountRepository;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "wydatki.db";
	private static final int DATABASE_VERSION = 1;

	public static final String TABLE_ACCOUNTS = "Account";
	public static final String TABLE_CATERORIES = "Category";

	public static final String TABLE_CATERORY_PARAMETERS = "CategoryParameter";

	public static final String TABLE_PARAMETERS = "Parameter";
	public static final String TABLE_PROJECTS = "Project";

	public static final String TABLE_TRANSACTION = "Transactions";
	public static final String TABLE_TRANSACTION_PARAMETERS = "TransactionsParameter";

	public static final String TABLE_CACHE = "Cache";

	private static final String CREATE_CACHE = "CREATE TABLE "
			+ TABLE_CACHE
			+ " (cacheId INTEGER PRIMARY KEY , userLogin VARCHAR, uri VARCHAR, postTAG VARCHAR, eTAG VARCHAR, response VARCHAR, timestamp DATETIME)";

	private static final String CREATE_ACCOUNTS = "create table "
			+ TABLE_ACCOUNTS
			+ "(ID integer primary key autoincrement, Name text not null, Balance REAL,LastActionDate NUMERIC, IsActive NUMERIC, IsSumInGlobalBalance NUMERIC, ImageIndex INTEGER);";

	private static final String CREATE_CATEGOIRES = "create table "
			+ TABLE_CATERORIES
			+ "(ID integer primary key autoincrement, Name text not null, IsActive NUMERIC, isPositive NUMERIC, ParentId INTEGER);";

	private static final String CREATE_PARAMETERS = "create table "
			+ TABLE_PARAMETERS
			+ "(ID integer primary key autoincrement, Name text not null, TypeId INTEGER, DefaultValue TEXT, IsActive NUMERIC, DataSource TEXT);";

	private static final String CREATE_PROJECTS = "create table "
			+ TABLE_PROJECTS
			+ "(ID integer primary key autoincrement, Name text not null, IsActive NUMERIC);";

	private static final String CREATE_CATERORY_PARAMETERS = "create table "
			+ TABLE_CATERORY_PARAMETERS + "(catId INTEGER, parId INTEGER);";

	private static final String CREATE_TRANSACTION = "create table "
			+ TABLE_TRANSACTION
			+ "(ID INTEGER primary key autoincrement, AccPlus INTEGER, AccMinus INTEGER, Value REAL, ActionDate NUMERIC, Note TEXT, CategoryId INTEGER, ProjectId INTEGER);";

	private static final String CREATE_TRANSACTION_PARAMETERS = "create table "
			+ TABLE_TRANSACTION_PARAMETERS
			+ "(TransactionId INTEGER, ParameterId INTEGER, Value TEXT);";

	public DataBaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		SQLiteDatabase db = getWritableDatabase();
		db.needUpgrade(DATABASE_VERSION);
		db.close();
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(CREATE_ACCOUNTS);
		database.execSQL(CREATE_CATEGOIRES);
		database.execSQL(CREATE_PARAMETERS);
		database.execSQL(CREATE_PROJECTS);
		database.execSQL(CREATE_CATERORY_PARAMETERS);

		database.execSQL(CREATE_TRANSACTION_PARAMETERS);
		database.execSQL(CREATE_TRANSACTION);

		database.execSQL(CREATE_CACHE);

	}

	public void onFirstRun() {
		ArrayList<Account> items = new ArrayList<Account>();

		items.add(new Account(1, "Gotówka Milena", 12.23, true));
		items.add(new Account(2, "Gotówka Piotrek", 123.23, true));
		items.add(new Account(3, "ING", 3921.12, true));
		items.add(new Account(4, "W domu", 132.22, true));

		AccountRepository repo = new AccountRepository();
		for (Account account : items) {
			repo.create(account);
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACCOUNTS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATERORY_PARAMETERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATERORIES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARAMETERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROJECTS);

		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION_PARAMETERS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTION);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CACHE);
		onCreate(db);
	}
}
