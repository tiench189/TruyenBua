package com.td.truyenbuavd.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.td.truyenbuavd.model.TDCategory;

public class DatabaseHelper extends SQLiteOpenHelper implements TableHelper {

	public DatabaseHelper(Context context) {
		super(context, "TruyenBua", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREAT_CATEGORY = "CREATE TABLE " + TABLE_CATEGORY + "(" + KEY_ID
				+ " INTEGER," + KEY_NAME + " TEXT," + KEY_CURRENT_ID
				+ " INTEGER)";
		db.execSQL(CREAT_CATEGORY);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
	}

	public void insertCategory(TDCategory category) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(KEY_ID, category.id);
		values.put(KEY_NAME, category.name);
		values.put(KEY_CURRENT_ID, category.currentId);
		db.insert(TABLE_CATEGORY, null, values);
		db.close();
	}

	public void updateCategory(TDCategory category) {
		SQLiteDatabase db = this.getWritableDatabase();
		String filter = KEY_ID + "=" + category.id;
		ContentValues values = new ContentValues();
		values.put(KEY_CURRENT_ID, category.currentId);
		db.update(TABLE_CATEGORY, values, filter, null);
		db.close();
	}

	public boolean checkCategory(TDCategory category) {
		boolean check = false;
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT * FROM " + TABLE_CATEGORY + " WHERE " + KEY_ID
				+ " =" + category.id;
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			check = true;
		} else {
			check = false;
		}
		return check;
	}

	public List<TDCategory> getAllCategory() {
		List<TDCategory> listCategory = new ArrayList<TDCategory>();
		SQLiteDatabase db = this.getReadableDatabase();
		String query = "SELECT * FROM " + TABLE_CATEGORY;
		Cursor cursor = db.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			do {
				listCategory.add(new TDCategory(cursor.getInt(0), cursor
						.getString(1), cursor.getInt(2)));
			} while (cursor.moveToNext());
		}
		db.close();
		return listCategory;
	}

}
