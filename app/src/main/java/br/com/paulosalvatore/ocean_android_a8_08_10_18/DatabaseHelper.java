package br.com.paulosalvatore.ocean_android_a8_08_10_18;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private Context context;

	public DatabaseHelper(Context context) {
		super(context, "ocean_db", null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			db.execSQL("CREATE TABLE posicoes (id integer PRIMARY KEY, latitude double, longitude double, data_hora varchar(255));");
		} catch (SQLException e) {
			Log.e("BANCO_DADOS", "Erro ao criar banco de dados", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}
}
