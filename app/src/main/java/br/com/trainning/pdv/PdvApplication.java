package br.com.trainning.pdv;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import se.emilsjolander.sprinkles.Migration;
import se.emilsjolander.sprinkles.Sprinkles;

/**
 * Created by android on 05/03/2016.
 */
public class PdvApplication extends Application {

    @Override
    public void onCreate()  {
        super.onCreate();

        Sprinkles sprinkles = Sprinkles.init(getApplicationContext());

        sprinkles.addMigration(new Migration() {
            @Override
            protected void onPreMigrate() {
                // do nothing
            }

            @Override
            protected void doMigration(SQLiteDatabase db) {
                db.execSQL(
                        "CREATE TABLE produto (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                "descricao TEXT,"+
                                "unidade TEXT,"+
                                "preco REAL,"+
                                "codigo_barra TEXT,"+
                                "foto TEXT,"+
                                "latitude REAL,"+
                                "longitude REAL"+
                                ")"
                );
                db.execSQL(
                        "CREATE TABLE item (" +
                                "id INTEGER PRIMARY KEY AUTOINCREMENT,"+
                                "id_compra INTEGER,"+
                                "id_produto TEXT,"+
                                "quantidade INTEGER"+
                                ")"
                );
            }

            @Override
            protected void onPostMigrate() {
                // do nothing
            }
        });
    }

}
