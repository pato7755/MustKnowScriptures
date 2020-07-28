package dbstuff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.udolgc.mustknowscriptures.ScriptureEntity;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SCRIPTURE_DB";
    private static final String SCRIPTURE_TABLE = "tb_scripture";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "title";
    private static final String KEY_SCRIPTURE = "scripture";
    private static final String KEY_FAV = "favourite";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SCRIPTURE_TABLE = "CREATE TABLE " + SCRIPTURE_TABLE + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_SCRIPTURE + " TEXT,"
                + KEY_FAV + " TEXT" + ") ";
        db.execSQL(CREATE_SCRIPTURE_TABLE);

    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SCRIPTURE_TABLE);

        // Create tables again
        onCreate(db);
    }


    // code to add the new contact
    public void addScripture(ScriptureEntity contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getTitle());
        values.put(KEY_SCRIPTURE, contact.getScripture());

        // Inserting Row
        db.insert(SCRIPTURE_TABLE, null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    public void batchInsertScriptures(List<ScriptureEntity> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (ScriptureEntity scripture : list) {
                values.put(KEY_NAME, scripture.getTitle());
                values.put(KEY_SCRIPTURE, scripture.getScripture());
                db.insert(SCRIPTURE_TABLE, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }


    // code to get all scriptures in a particular book
    public List<ScriptureEntity> getFavouriteScriptures() {
        List<ScriptureEntity> scriptureList = new ArrayList<ScriptureEntity>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(SCRIPTURE_TABLE,
                new String[]{KEY_ID, KEY_NAME, KEY_SCRIPTURE},
                KEY_FAV + " =?",
                new String[]{"YES"},
                null,
                null,
                null,
                null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScriptureEntity scripture = new ScriptureEntity();
                scripture.setId(Integer.parseInt(cursor.getString(0)));
                scripture.setTitle(cursor.getString(1));
                scripture.setScripture(cursor.getString(2));
//                scripture.setScripture(cursor.getString(3));
                // Adding scripture to list
                scriptureList.add(scripture);
            } while (cursor.moveToNext());
        }

        // return scripture list
        return scriptureList;
    }


    // code to get all scriptures in a particular book
    public List<ScriptureEntity> getSelectedBook(String book) {
        List<ScriptureEntity> scriptureList = new ArrayList<ScriptureEntity>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(SCRIPTURE_TABLE,
                new String[]{KEY_ID, KEY_NAME, KEY_SCRIPTURE},
                KEY_NAME + " LIKE?",
                new String[]{book + "%"},
                null,
                null,
                null,
                null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScriptureEntity scripture = new ScriptureEntity();
                scripture.setId(Integer.parseInt(cursor.getString(0)));
                scripture.setTitle(cursor.getString(1));
                scripture.setScripture(cursor.getString(2));
                // Adding scripture to list
                scriptureList.add(scripture);
            } while (cursor.moveToNext());
        }

        // return scripture list
        return scriptureList;
    }


    // code to get all scriptures in a list view
    public List<ScriptureEntity> getAllScriptures() {
        List<ScriptureEntity> contactList = new ArrayList<ScriptureEntity>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SCRIPTURE_TABLE + " WHERE " + KEY_NAME + " IS NOT NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScriptureEntity contact = new ScriptureEntity();
                contact.setId(Integer.parseInt(cursor.getString(0)));
                contact.setTitle(cursor.getString(1));
                contact.setScripture(cursor.getString(2));
                // Adding scripture to list
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }


    // code to get all contacts in a list view
    public List<ScriptureEntity> deleteAllScriptures() {
        List<ScriptureEntity> scriptureList = new ArrayList<ScriptureEntity>();
        // Select All Query
        String selectQuery = "DELETE FROM " + SCRIPTURE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScriptureEntity scripture = new ScriptureEntity();
                scripture.setId(Integer.parseInt(cursor.getString(0)));
                scripture.setTitle(cursor.getString(1));
                scripture.setScripture(cursor.getString(2));
                // Adding scripture to list
                scriptureList.add(scripture);
            } while (cursor.moveToNext());
        }

        // return contact list
        return scriptureList;
    }


    // code to update the single scripture
    public int addFavScripture(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        ScriptureEntity scriptureEntity = new ScriptureEntity();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, scriptureEntity.getTitle());
        values.put(KEY_SCRIPTURE, scriptureEntity.getScripture());
        values.put(KEY_FAV, scriptureEntity.getFavourite());

//        String sql = "UPDATE "+SCRIPTURE_TABLE +" SET " + KEY_FAV+ " = '" + "YES" + "' WHERE "+KEY_NAME+ " = "+title;

        ContentValues cv = new ContentValues();
        cv.put(KEY_FAV, "YES");
        return db.update(SCRIPTURE_TABLE, cv, KEY_NAME + "= ?", new String[]{title});

        // updating row
//        return db.update(SCRIPTURE_TABLE, values, KEY_ID + " = ?",
//                new String[]{String.valueOf(scriptureEntity.getId())});
    }


    // Deleting single scripture
    public void deleteScripture(ScriptureEntity contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SCRIPTURE_TABLE, KEY_ID + " = ?",
                new String[]{String.valueOf(contact.getId())});
        db.close();
    }


    // Getting scripture Count
    public int getScriptureCount() {
        String countQuery = "SELECT  * FROM " + SCRIPTURE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

}
