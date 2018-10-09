package br.com.paulosalvatore.ocean_android_a8_08_10_18;

// Singleton

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
	private static DatabaseManager instancia;
	private DatabaseHelper helper;

	private boolean conexaoAberta = false;
	private SQLiteDatabase db;

	public static void inicializarInstancia(DatabaseHelper helper) {
		if (instancia == null) {
			instancia = new DatabaseManager();
			instancia.helper = helper;
		}
	}

	public static DatabaseManager getInstancia() {
		if (instancia == null) {
			throw new IllegalStateException("Declare 'inicializarInstancia()' primeiro.");
		}

		return instancia;
	}

	public void abrirConexao() {
		if (!conexaoAberta) {
			db = helper.getWritableDatabase();
			conexaoAberta = true;
		}
	}

	public void fecharConexao() {
		if (conexaoAberta) {
			db.close();
			conexaoAberta = false;
		}
	}

	public void criarPosicao(Posicao novaPosicao) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("latitude", novaPosicao.getLatitude());
		contentValues.put("longitude", novaPosicao.getLongitude());
		contentValues.put("dataHora", novaPosicao.getDataHora());

		abrirConexao();

		db.insert("posicoes", null, contentValues);

		fecharConexao();
	}

	public List<Posicao> obterPosicoes() {
		List<Posicao> posicoes = new ArrayList<Posicao>();

		String sql = "SELECT * FROM posicoes";

		abrirConexao();

		Cursor cursor = db.rawQuery(sql, null);

		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				double latitude = cursor.getDouble(cursor.getColumnIndex("latitude"));
				double longitude = cursor.getDouble(cursor.getColumnIndex("longitude"));
				String data_hora = cursor.getString(cursor.getColumnIndex("dataHora"));

				Posicao posicao = new Posicao(
						id,
						latitude,
						longitude,
						data_hora
				);

				posicoes.add(posicao);
			} while (cursor.moveToNext());
		}

		return posicoes;
	}

	public void editarPosicao(Posicao posicao) {
		String sql = "UPDATE posicoes SET latitude = '" + posicao.getLatitude() + "'," +
				"longitude = '" + posicao.getLongitude() + "'," +
				"data_hora = '" + posicao.getDataHora() + "' " +
				"WHERE (id = '" + posicao.getId() + "');";

		abrirConexao();

		db.execSQL(sql);

		fecharConexao();
	}

	public void removerPosicao(Posicao posicao) {
		String sql = "DELETE FROM posicoes WHERE (id = '" + posicao.getId() + "');";

		abrirConexao();

		db.execSQL(sql);

		fecharConexao();
	}

	public void limparPosicoes() {
		String sql = "DELETE FROM posicoes";

		abrirConexao();

		db.execSQL(sql);

		fecharConexao();
	}
}
