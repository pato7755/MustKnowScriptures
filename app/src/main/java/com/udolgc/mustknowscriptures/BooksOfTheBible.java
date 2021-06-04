package com.udolgc.mustknowscriptures;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbstuff.DatabaseHandler;
import utils.CheckInternetConnection;
import utils.UtilityManager;

public class BooksOfTheBible extends AppCompatActivity {

    int statusEnglish = 0;
    int statusFrench = 0;
    UtilityManager utilityManager = new UtilityManager();
    ListView listView;
    ListView othersListView;
    TextView booksLabelTextView;
    TextView othersLabelTextView;
    ProgressDialog progress;
    List<ScriptureEntity> listEnglish = new ArrayList<>();
    List<ScriptureEntity> listFrench = new ArrayList<>();
    Intent intent;
    List<HashMap<String, String>> aList;
    List<HashMap<String, String>> aList2;

    FirebaseAuth mAuth;
    FirebaseUser mCurrentUser;
    ProgressBar progressBar;

    CheckInternetConnection checkInternetConnection = new CheckInternetConnection();

    String[] booksEnglish = new String[]{
            "Genesis", "Exodus", "Leviticus", "Numbers", "Deuteronomy",
            "Joshua", /*"JUDG",*/ "Ruth", "1 Samuel", "2 Samuel",
            "1 Kings", "2 Kings", "1 Chronicles", "2 Chronicles", "Ezra",
            "Nehemiah", "Esther", "Job", "Psalm", "Proverbs",
            "Ecclesiastes", "Songs of Solomon", "Isaiah", "Jeremiah", "Lamentations",
            "Ezekiel", "Daniel", "Hosea", "Joel", "Amos",
            "Obadiah", "Jonah", "Micah", "Nahum", "Habakkuk",
            "Zephaniah", "Haggai", "Zechariah", "Malachi",
            "Matthew", "Mark", "Luke", "John", "Acts",
            "Romans", "1 Corinthians", "2 Corinthians", "Galatians", "Ephesians",
            "Philippians", "Colossians", "1 Thessalonians", "2 Thessalonians", "1 Timothy",
            "2 Timothy", "Titus", "Philemon", "Hebrews", "James",
            "1 Peter", "2 Peter", "1 John", "2 John", "3 John",
            "Jude", "Revelation"
    };

    String[] booksFrench = new String[]{
            "Genèse", "Exode", "Lévitique", "Nombres", "Deutéronome",
            "Josué", /*"JUDG",*/ "Ruth", "1 Samuel", "2 Samuel",
            "1 Rois", "2 Rois", "1 Chroniques", "2 Chroniques", "Esdras",
            "Néhémie", "Esther", "Job", "Psaumes", "Proverbes",
            "Ecclésiastes", "Cantique des Cantiques", "Esaïe", "Jérémie", "Lamentations",
            "Ézéchiel", "Daniel", "Osée", "Joël", "Amos",
            "Jonah", "Michée", "Habacuc",
            "Sophonie", "Aggée", "Zacharie", "Malachie",
            "Matthieu", "Marc", "Luc", "Jean", "Actes",
            "Romains", "1 Corinthiens", "2 Corinthiens", "Galates", "Éphésiens",
            "Philippiens", "Colossiens", "1 Thessaloniciens", "2 Thessaloniciens", "1 Timothée",
            "2 Timothée", "Tite", "Philémon", "Hébreux", "Jacques",
            "1 Pierre", "2 Pierre", "1 Jean", "2 Jean", "3 Jean",
            "Jude", "Apocalypse"
    };


    String[] othersEnglish = new String[]{
            "All", "Favourites"
    };

    String[] othersFrench = new String[]{
            "Tous", "Préféres"
    };

    DatabaseHandler dbHandler;
    public static List<ScriptureEntity> scriptureByBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books);

        initViews();

        dbHandler = new DatabaseHandler(BooksOfTheBible.this);

        if (!utilityManager.getBooleanSharedPreference(UtilityManager.SETUP_DONE)) {

            utilityManager.setPreferences(UtilityManager.LANGUAGE, UtilityManager.ENGLISH);

            if (!checkInternetConnection.isNetworkAvailable(BooksOfTheBible.this)) {
                showAlertDialog(getString(R.string.oops), getString(R.string.no_internet_connection), getString(R.string.ok));
            } else {
                authenticateUser();
            }

        } else {
            System.out.println("SETUP IS DONE");
        }

        populateMenus();

//        fetchScripturesTest();

    }

    public void initViews() {

        listView = findViewById(R.id.listview);
        othersListView = findViewById(R.id.others_listview);
        booksLabelTextView = findViewById(R.id.books_label_textview);
        othersLabelTextView = findViewById(R.id.others_label_textview);
        progressBar = findViewById(R.id.progress_bar);

    }


    private static boolean doesDatabaseExist(Context context, String databaseName) {
        File dbFile = context.getDatabasePath(databaseName);
        return dbFile.exists();
    }


    private void populateMenus() {

        aList = new ArrayList<>();
        aList2 = new ArrayList<>();

        booksLabelTextView.setText(utilityManager.getSharedPreference(UtilityManager.LANGUAGE).equals("English") ? R.string.books : R.string.livres);
        othersLabelTextView.setText(utilityManager.getSharedPreference(UtilityManager.LANGUAGE).equals("English") ? R.string.others : R.string.autres);

        for (String s : utilityManager.getSharedPreference(UtilityManager.LANGUAGE).equals("English") ? booksEnglish : booksFrench) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", s);
            aList.add(hm);
        }

        for (String other : utilityManager.getSharedPreference(UtilityManager.LANGUAGE).equals("English") ? othersEnglish : othersFrench) {
            HashMap<String, String> hm2 = new HashMap<String, String>();
            hm2.put("txt3", other);
            aList2.add(hm2);
        }

        String[] from = {"txt"};
        int[] to = {R.id.txt};


        String[] from3 = {"txt3"};
        int[] to3 = {R.id.txt3};

        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.gridview_layout, from, to);
        SimpleAdapter adapter2 = new SimpleAdapter(getBaseContext(), aList2, R.layout.gridview_layout3, from3, to3);

        listView.setAdapter(adapter);

        othersListView.setAdapter(adapter2);

        if (doesDatabaseExist(BooksOfTheBible.this, "SCRIPTURE_DB")) {
            System.out.println("DATABASE EXISTS !!!");
        } else {
            System.out.println("DATABASE DOES NOT EXIST");
        }


        listView.setOnItemClickListener((parent, view, position, id) -> {

            String book = (String) ((TextView) view.findViewById(R.id.txt)).getText();

            System.out.println("title: " + book);

            List<ScriptureEntity> scriptureList = dbHandler.getSelectedBook(book, utilityManager.getSharedPreference(UtilityManager.LANGUAGE));
            for (ScriptureEntity cn : scriptureList) {
                String log = "Id: " + cn.getId() +
                        " ,TITLE: " + cn.getTitle() +
                        " ,SCRIPTURE: " + cn.getScripture() +
                        " ,BOOK_NAME: " + cn.getBook();
                // Writing scriptures to log
                Log.d("Name: ", log);
            }

            if (!scriptureList.isEmpty()) {
                scriptureByBook = scriptureList;
                Intent intent = new Intent(BooksOfTheBible.this, MyListActivity.class);
                startActivity(intent);
            } else {

                showAlertDialog(getString(R.string.oops), getString(R.string.download_unsuccessful), getString(R.string.ok));

            }

        });


        othersListView.setOnItemClickListener((parent, view, position, id) -> {

            String book = (String) ((TextView) view.findViewById(R.id.txt3)).getText();

            System.out.println("title: " + book);

            if (book.equals("All") || book.equals("Tous")) {

                scriptureByBook = dbHandler.getAllScriptures(utilityManager.getSharedPreference(UtilityManager.LANGUAGE));
                if (!scriptureByBook.isEmpty()) {
                    Intent intent = new Intent(BooksOfTheBible.this, MyListActivity.class);
                    startActivity(intent);
                } else {

                    showAlertDialog(getString(R.string.oops), getString(R.string.download_unsuccessful), getString(R.string.ok));

                }

            } else if (book.equals("Favourites") || book.equals("Préféres")) {

                scriptureByBook = dbHandler.getFavouriteScriptures(utilityManager.getSharedPreference(UtilityManager.LANGUAGE));
                Intent intent = new Intent(BooksOfTheBible.this, MyListActivity.class);
                startActivity(intent);

            }
        });

    }

    private void fetchScripturesEnglish() {

        System.out.println("fetch scriptures");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {

            System.out.println("try");

            db.collection("English")
//                    .orderBy("createdAt")
                    .get()
                    .addOnCompleteListener(task -> {

                        System.out.println("oncomplete");

                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {

                                System.out.println(documentSnapshot.getData());
                                System.out.println("getId: " + documentSnapshot.getId());

                                String bookName = documentSnapshot.getId();

                                for (Map.Entry<String, Object> entry : documentSnapshot.getData().entrySet()) {

                                    System.out.println("entry.getkey(): " + entry.getKey());
                                    System.out.println("entry.getentry(): " + entry.getValue());

                                    listEnglish.add(new ScriptureEntity(entry.getKey(), entry.getValue().toString(), bookName, ""));

                                }

                            }
                            System.out.println("batch insert english");
                            dbHandler.batchInsertScriptures(listEnglish, UtilityManager.ENGLISH);
                            statusEnglish = 200;

                            System.out.println("done with english");
                        } else {

                            statusEnglish = 400;

                        }

                        try {

                            System.out.println("fetchScripturesFrench");
                            fetchScripturesFrench();

                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                            System.out.println("exception downloading french: " + e.getMessage());
                        }
                    });

        } catch (Exception ex) {
            progress.cancel();
            showAlertDialog(getString(R.string.oops), getString(R.string.something_went_wrong), getString(R.string.ok));
            System.out.println("exception: " + ex.getMessage());
        }

    }

    private void fetchScripturesFrench() {

        System.out.println("fetch scriptures french");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        try {

            System.out.println("try");

            db.collection("French")
//                    .orderBy("createdAt")
                    .get()
                    .addOnCompleteListener(task -> {

                        System.out.println("oncomplete");

                        if (task.isSuccessful()) {
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {

                                System.out.println(documentSnapshot.getData());
                                System.out.println(documentSnapshot.getId());

                                String bookName = documentSnapshot.getId();

                                for (Map.Entry<String, Object> entry : documentSnapshot.getData().entrySet()) {

                                    System.out.println("entry.getkey(): " + entry.getKey());
                                    System.out.println("entry.getentry(): " + entry.getValue());

                                    listFrench.add(new ScriptureEntity(entry.getKey(), entry.getValue().toString(), bookName, ""));

                                }

                            }
                            System.out.println("batch insert french");
                            dbHandler.batchInsertScriptures(listFrench, UtilityManager.FRENCH);
                            statusFrench = 200;

                        } else {

                            statusFrench = 400;

                        }
                        System.out.println("DONE DONE");

                        progress.cancel();

                        if (dbHandler.getScriptureCount(utilityManager.getSharedPreference(UtilityManager.LANGUAGE)) > 0) {

//                            progress.cancel();
                            Toast.makeText(BooksOfTheBible.this, "Downloaded scriptures successfully", Toast.LENGTH_LONG).show();
                            System.out.println("number of records: " + dbHandler.getScriptureCount(utilityManager.getSharedPreference(UtilityManager.LANGUAGE)));
                            utilityManager.setBooleanPreferences(UtilityManager.SETUP_DONE, true);

                        } else {

                            showRetryAlertDialog(getString(R.string.oops), getString(R.string.download_unsuccessful_dialog), getString(R.string.retry), getString(R.string.cancel));

                        }
                    });

        } catch (Exception ex) {
            System.out.println("exception: " + ex.getMessage());
            progress.cancel();
            showAlertDialog(getString(R.string.oops), getString(R.string.something_went_wrong), getString(R.string.ok));
        }


    }


//    private void fetchScripturesTest() {
//
//        System.out.println("fetch scriptures test");
//        List<ScriptureEntity> listTest = new ArrayList<>();
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        try {
//
//            System.out.println("try");
//
//            db.collection("French")
//                    .orderBy("timestamp")
//                    .get()
//                    .addOnCompleteListener(task -> {
//
//                        System.out.println("oncompletetest");
//
//                        if (task.isSuccessful()) {
//                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
//
////                                System.out.println(documentSnapshot.getData());
//                                System.out.println("getId: " + documentSnapshot.getId());
//
//                                String bookName = documentSnapshot.getId();
//
//                                for (Map.Entry<String, Object> entry : documentSnapshot.getData().entrySet()) {
//
//
//                                    System.out.println("entry.getkey(): " + entry.getKey());
////                                    System.out.println("entry.getentry(): " + entry.getValue());
//
//
//                                    listTest.add(new ScriptureEntity(entry.getKey(), entry.getValue().toString(), bookName, ""));
//
//                                }
//
//                            }
//                            System.out.println("batch insert test");
////                            System.out.println(listTest.removeIf(s -> s.equals("acbd"));
////                            dbHandler.batchInsertScriptures(listEnglish, UtilityManager.ENGLISH);
//                            statusEnglish = 200;
//
//                            System.out.println("done with test");
//                        } else {
//
//                            statusEnglish = 400;
//
//                        }
//
//
//                    });
//
//        } catch (Exception ex) {
//            progress.cancel();
//            showAlertDialog(getString(R.string.oops), getString(R.string.something_went_wrong), getString(R.string.ok));
//            System.out.println("exception: " + ex.getMessage());
//        }
//
//    }

    public class downloadScriptures extends AsyncTask<Void, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(BooksOfTheBible.this);
            progress.setCancelable(false);
            progress.setTitle("Please wait");
            progress.setMessage("Downloading scriptures...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                System.out.println("fetchScripturesEnglish");
                fetchScripturesEnglish();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception downloading english: " + e.getMessage());
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            if (e != null) {
                progress.cancel();
            } else {
                super.onPostExecute(result);

//                progress.cancel();


            }

        }

    }

    private void authenticateUser() {

        System.out.println("authenticateUser");

        progressBar.setVisibility(View.VISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();

        if (mCurrentUser == null) {

            mAuth.signInAnonymously().addOnCompleteListener(task -> {

                progressBar.setVisibility(View.INVISIBLE);

                if (task.isSuccessful()) {
                    System.out.println("task.isSuccessful()");
                    new downloadScriptures().execute();

                } else {

                    Toast.makeText(BooksOfTheBible.this, "Initial setup failed", Toast.LENGTH_LONG).show();

                }
            });
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            new downloadScriptures().execute();
        }

    }


    public void exitApp() {
        try {

            runOnUiThread(() -> {
                // TODO Auto-generated method stub
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(BooksOfTheBible.this);
                alertDialog.setTitle("Confirm exit");
                alertDialog.setMessage("Do you want to exit app?");

                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                BooksOfTheBible.super.onBackPressed();
                            }
                        });
                // Setting Negative "NO" Btn
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // Write your code here to execute after dialog

                                dialog.cancel();

                            }
                        });
                alertDialog.show();

            });

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume");

        populateMenus();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                intent = new Intent(BooksOfTheBible.this, AboutApp.class);
                startActivity(intent);
                break;
            case R.id.settings:
                intent = new Intent(BooksOfTheBible.this, Settings.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    public void showAlertDialog(String title, String message, String positiveButtonText) {

        Context context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, (dialogInterface, id) -> {
                    dialogInterface.dismiss();
                })
                .create()
                .show();

    }

    public void showRetryAlertDialog(String title, String message, String positiveButtonText, String negativeButtonText) {

        Context context = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveButtonText, (dialogInterface, id) -> {
                    new downloadScriptures().execute();
                    dialogInterface.dismiss();
                })
                .setNegativeButton(negativeButtonText, (dialogInterface, id) -> {
                    dialogInterface.dismiss();
                })
                .create()
                .show();

    }

    @Override
    public void onBackPressed() {
        exitApp();
    }
}
