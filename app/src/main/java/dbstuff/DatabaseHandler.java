package dbstuff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.udolgc.mustknowscriptures.ScriptureEntity;

import java.util.ArrayList;
import java.util.List;

import utils.UtilityManager;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "SCRIPTURE_DB";
    private static final String SCRIPTURE_TABLE = "tb_scripture_";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "title";
    private static final String KEY_SCRIPTURE = "scripture";
    private static final String KEY_BOOK = "book";
    private static final String KEY_FAV = "favourite";

    UtilityManager utilityManager = new UtilityManager();

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //3rd argument to be passed is CursorFactory instance
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("onCreate databases");
        String CREATE_SCRIPTURE_TABLE_ENGLISH = "CREATE TABLE " + SCRIPTURE_TABLE.concat(UtilityManager.ENGLISH)+ "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_SCRIPTURE + " TEXT,"
                + KEY_BOOK + " TEXT,"
                + KEY_FAV + " TEXT" + ") ";
        String CREATE_SCRIPTURE_TABLE_FRENCH = "CREATE TABLE " + SCRIPTURE_TABLE.concat(UtilityManager.FRENCH)+ "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_SCRIPTURE + " TEXT,"
                + KEY_BOOK + " TEXT,"
                + KEY_FAV + " TEXT" + ") ";

        System.out.println(CREATE_SCRIPTURE_TABLE_ENGLISH);
        System.out.println(CREATE_SCRIPTURE_TABLE_FRENCH);

        try {
            db.execSQL(CREATE_SCRIPTURE_TABLE_ENGLISH);
        } catch (Exception exception){
            System.out.println("exception: " + exception.getMessage());
            FirebaseCrashlytics.getInstance().log("Failed to create " + CREATE_SCRIPTURE_TABLE_ENGLISH);
        }
        try {
            db.execSQL(CREATE_SCRIPTURE_TABLE_FRENCH);
        } catch (Exception exception){
            System.out.println("exception: " + exception.getMessage());
            FirebaseCrashlytics.getInstance().log("Failed to create " + CREATE_SCRIPTURE_TABLE_FRENCH);
        }
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + SCRIPTURE_TABLE.concat(UtilityManager.ENGLISH));
        db.execSQL("DROP TABLE IF EXISTS " + SCRIPTURE_TABLE.concat(UtilityManager.FRENCH));

        // Create tables again
        onCreate(db);
    }


    // code to add the new scripture
    public void addScripture(ScriptureEntity scripture, String language) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, scripture.getTitle());
        values.put(KEY_SCRIPTURE, scripture.getScripture());
        values.put(KEY_FAV, "");

        // Inserting Row
        db.insert(SCRIPTURE_TABLE.concat(language), null, values);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }


    public void batchInsertScriptures(List<ScriptureEntity> list, String language) {
        System.out.println("batch insert");
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            for (ScriptureEntity scripture : list) {
                values.put(KEY_NAME, scripture.getTitle());
                values.put(KEY_SCRIPTURE, scripture.getScripture());
                values.put(KEY_FAV, "");
                values.put(KEY_BOOK, scripture.getBook());
                db.insert(SCRIPTURE_TABLE.concat(language), null, values);
            }
            db.setTransactionSuccessful();
        } catch (Exception ex){
            System.out.println("insertion exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    // code to get all scriptures in a particular book
    public List<ScriptureEntity> getFavouriteScriptures(String language) {
        List<ScriptureEntity> scriptureList = new ArrayList<ScriptureEntity>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(SCRIPTURE_TABLE.concat(language),
                new String[]{KEY_ID, KEY_NAME, KEY_SCRIPTURE, KEY_FAV},
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
                scripture.setFavourite(cursor.getString(3));
//                scripture.setScripture(cursor.getString(3));
                // Adding scripture to list
                scriptureList.add(scripture);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return scripture list
        return scriptureList;
    }


    // code to get all scriptures in a particular book
    public List<ScriptureEntity> getSelectedBook(String book, String language) {
        List<ScriptureEntity> scriptureList = new ArrayList<ScriptureEntity>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(SCRIPTURE_TABLE.concat(language),
                new String[]{KEY_ID, KEY_NAME, KEY_SCRIPTURE, KEY_FAV},
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
                scripture.setFavourite(cursor.getString(3));
                // Adding scripture to list
                scriptureList.add(scripture);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return scripture list
        return scriptureList;
    }


    // code to get all scriptures in a list view
    public List<ScriptureEntity> getAllScriptures(String language) {
        List<ScriptureEntity> scriptureList = new ArrayList<ScriptureEntity>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + SCRIPTURE_TABLE.concat(language) + " WHERE " + KEY_NAME + " IS NOT NULL";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ScriptureEntity scripture = new ScriptureEntity();
                scripture.setId(Integer.parseInt(cursor.getString(0)));
                scripture.setTitle(cursor.getString(1));
                scripture.setScripture(cursor.getString(2));
                scripture.setFavourite(cursor.getString(3));
                // Adding scripture to list
                scriptureList.add(scripture);
            } while (cursor.moveToNext());
        }
        cursor.close();

        // return scripture list
        return scriptureList;
    }


    public List<ScriptureEntity> deleteAllScriptures(String language) {
        List<ScriptureEntity> scriptureList = new ArrayList<ScriptureEntity>();
        // Select All Query
        String selectQuery = "DELETE FROM " + SCRIPTURE_TABLE.concat(language);

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
        cursor.close();

        // return scripture list
        return scriptureList;
    }


    // code to update the single scripture
    public int addFavScripture(String title, String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        ScriptureEntity scriptureEntity = new ScriptureEntity();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, scriptureEntity.getTitle());
        values.put(KEY_SCRIPTURE, scriptureEntity.getScripture());
        values.put(KEY_FAV, scriptureEntity.getFavourite());
        System.out.println("KEY_FAV value is " + values.getAsString(KEY_FAV));

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_FAV, "YES");
        return db.update(SCRIPTURE_TABLE.concat(language), contentValues, KEY_NAME + "= ?", new String[]{title});
    }

    public int removeFavScripture(String title, String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        ScriptureEntity scriptureEntity = new ScriptureEntity();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, scriptureEntity.getTitle());
        values.put(KEY_SCRIPTURE, scriptureEntity.getScripture());
        values.put(KEY_FAV, scriptureEntity.getFavourite());

        ContentValues cv = new ContentValues();
        cv.put(KEY_FAV, "");
        return db.update(SCRIPTURE_TABLE.concat(language), cv, KEY_NAME + "= ?", new String[]{title});
    }


    // Deleting single scripture
    public void deleteScripture(ScriptureEntity scripture, String language) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(SCRIPTURE_TABLE.concat(language), KEY_ID + " = ?",
                new String[]{String.valueOf(scripture.getId())});
        db.close();
    }


    // Getting scripture Count
    public int getScriptureCount(String language) {
        String countQuery = "SELECT  * FROM " + SCRIPTURE_TABLE.concat(language);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();

        return count;
    }

}
