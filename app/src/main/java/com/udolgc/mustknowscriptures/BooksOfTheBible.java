package com.udolgc.mustknowscriptures;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dbstuff.DatabaseHandler;

public class BooksOfTheBible extends AppCompatActivity {

    GridView gridView;
    ProgressDialog progress;

    String[] countries = new String[]{
            "GEN", "EXO", "LEV", "NUM", "DEU",
            "JOS", "JUD", "RUT", "1SAM", "2SAM",
            "1KIN", "2KIN", "1CHR", "2CHR", "EZR",
            "NEH", "EST", "JOB", "PSA", "PRO",
            "ECC", "SONG", "ISA", "JER", "LAM",
            "EZE", "DAN", "HOS", "JOE", "AMO",
            "OBA", "JON", "MIC", "NAH", "HAB",
            "ZEP", "HAG", "ZEC", "MAL", "MAT",
            "MAR", "LUK", "JOH", "ACT", "ROM",
            "1COR", "2COR", "GAL", "EPH", "PHI",
            "COL", "1THE", "2THE", "1TIM", "2TIM",
            "TIT", "PHI", "HEB", "JAM", "1PET",
            "2PET", "1JOH", "2JOH", "3JOH", "JUD",
            "REV", "ALL"
    };

    private ScriptureDAO mContactDAO;
    DatabaseHandler dbHandler;
    public static List<ScriptureEntity> scriptureByBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books);

        gridView = (GridView) findViewById(R.id.gridview);

        dbHandler = new DatabaseHandler(BooksOfTheBible.this);

        dbHandler.deleteAllScriptures();

        new uploadScriptures().execute();



        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < countries.length; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", countries[i]);
//            hm.put("flag", Integer.toString(flags[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"txt"};

        // Ids of views in listview_layout
        int[] to = {R.id.txt};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.gridview_layout, from, to);

        // Getting a reference to gridview of MainActivity
        final GridView gridView = (GridView) findViewById(R.id.gridview);

        // Setting an adapter containing images to the gridview
        gridView.setAdapter(adapter);

        if (doesDatabaseExist(BooksOfTheBible.this, "SCRIPTURE_DB")) {
            System.out.println("DATABASE EXISTS !!!");
        } else {
            System.out.println("DATABASE DOES NOT EXIST");
        }


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String book = (String) ((TextView) view.findViewById(R.id.txt)).getText();

                System.out.println("title: " + book);

                List<ScriptureEntity> scriptureList  = dbHandler.getSelectedBook(book);

                System.out.println("scriptureCount: " + dbHandler.getScriptureCount());

//                List<ScriptureEntity> contacts = dbHandler.getAllScriptures();

                for (ScriptureEntity cn : scriptureList) {
                    String log = "Id: " + cn.getId() + " ,TITLE: " + cn.getTitle() + " ,SCRIPTURE: " +
                            cn.getScripture();
                    // Writing Contacts to log
                    Log.d("Name: ", log);
                }

                scriptureByBook = scriptureList;

                Intent intent = new Intent(BooksOfTheBible.this, MyListActivity.class);
                startActivity(intent);
            }
        });

    }


    private static boolean doesDatabaseExist(Context context, String databaseName) {
        File dbFile = context.getDatabasePath(databaseName);
        return dbFile.exists();
    }


    public void insertScriptures() {
        dbHandler.addScripture(new ScriptureEntity());

        List<ScriptureEntity> valuesList = new ArrayList<>();


        valuesList.add(new ScriptureEntity("GEN 8:22", "While the earth remaineth, seedtime and harvest, and cold and heat, and summer and winter, and day and night shall not cease."));
        valuesList.add(new ScriptureEntity("GEN 32:28", "And he said, Thy name shall be called no more Jacob, but Israel: for as a prince hast thou power with God and with men, and hast prevailed."));
        valuesList.add(new ScriptureEntity("GEN 28:16-17", "And Jacob awaked out of his sleep, and he said, Surely the LORD is in this place; and I knew it not." +
                "And he was afraid, and said, How dreadful is this place! this is none other but the house of God, and this is the gate of heaven."));
        valuesList.add(new ScriptureEntity("GEN 45:7-8", "And God sent me before you to preserve you a posterity in the earth, and to save your lives by a great deliverance." +
                "So now it was not you that sent me hither, but God: and he hath made me a father to Pharaoh, and lord of all his house, and a ruler throughout all the land of Egypt."));
        valuesList.add(new ScriptureEntity("GEN 49:25", "Even by the God of thy father, who shall help thee; and by the Almighty, who shall bless thee with blessings of heaven above, blessings of the deep that lieth under, blessings of the breasts, and of the womb:"));

        valuesList.add(new ScriptureEntity("EXO 7:1", "And the LORD said unto Moses, See, I have made thee a god to Pharaoh: and Aaron thy brother shall be thy prophet."));
        valuesList.add(new ScriptureEntity("EXO 12:13", "And the blood shall be to you for a token upon the houses where ye are: and when I see the blood, I will pass over you, and the plague shall not be upon you to destroy you, when I smite the land of Egypt."));
        valuesList.add(new ScriptureEntity("EXO 15:26", "And said, If thou wilt diligently hearken to the voice of the LORD thy God, and wilt do that which is right in his sight, and wilt give ear to his commandments, and keep all his statutes, I will put none of these diseases upon thee, which I have brought upon the Egyptians: for I am the LORD that healeth thee."));
        valuesList.add(new ScriptureEntity("EXO 23:25-26", "And ye shall serve the LORD your God, and he shall bless thy bread, and thy water; and I will take sickness away from the midst of thee.\n" +
                "There shall nothing cast their young, nor be barren, in thy land: the number of thy days I will fulfil."));
        valuesList.add(new ScriptureEntity("EXO 33:14-15", "And he said, My presence shall go with thee, and I will give thee rest.\n" +
                "And he said unto him, If thy presence go not with me, carry us not up hence."));

        valuesList.add(new ScriptureEntity("LEV 19:32 (NASB)", "You shall rise up before the grayheaded and honor the aged, and you shall revere your God; I am the Lord."));

        valuesList.add(new ScriptureEntity("NUM 8:24", "This is it that belongeth unto the Levites: from twenty and five years old and upward they shall go in to wait upon the service of the tabernacle of the congregation:"));
        valuesList.add(new ScriptureEntity("NUM 14:24", "But my servant Caleb, because he had another spirit with him, and hath followed me fully, him will I bring into the land whereinto he went; and his seed shall possess it."));
        valuesList.add(new ScriptureEntity("NUM 11:17", "And I will come down and talk with thee there: and I will take of the spirit which is upon thee, and will put it upon them; and they shall bear the burden of the people with thee, that thou bear it not thyself alone."));
        valuesList.add(new ScriptureEntity("NUM 23:19", "God is not a man, that he should lie; neither the son of man, that he should repent: hath he said, and shall he not do it? or hath he spoken, and shall he not make it good?"));
        valuesList.add(new ScriptureEntity("NUM 23:23", "Surely there is no enchantment against Jacob, neither is there any divination against Israel: according to this time it shall be said of Jacob and of Israel, What hath God wrought!"));

        valuesList.add(new ScriptureEntity("DEU 4:5-6", "5 Behold, I have taught you statutes and judgments, even as the Lord my God commanded me, that ye should do so in the land whither ye go to possess it.\n" +
                "6 Keep therefore and do them; for this is your wisdom and your understanding in the sight of the nations, which shall hear all these statutes, and say, Surely this great nation is a wise and understanding people."));
        valuesList.add(new ScriptureEntity("DEU 6:5", "And thou shalt love the Lord thy God with all thine heart, and with all thy soul, and with all thy might."));
        valuesList.add(new ScriptureEntity("DEU 8:14", "Then thine heart be lifted up, and thou forget the Lord thy God, which brought thee forth out of the land of Egypt, from the house of bondage;"));
        valuesList.add(new ScriptureEntity("DEU 8:18", "But thou shalt remember the Lord thy God: for it is he that giveth thee power to get wealth, that he may establish his covenant which he sware unto thy fathers, as it is this day."));
        valuesList.add(new ScriptureEntity("DEU 29:29", "The secret things belong unto the Lord our God: but those things which are revealed belong unto us and to our children for ever, that we may do all the words of this law."));
        valuesList.add(new ScriptureEntity("DEU 30:19", "I call heaven and earth to record this day against you, that I have set before you life and death, blessing and cursing: therefore choose life, that both thou and thy seed may live:"));

        valuesList.add(new ScriptureEntity("JOS 1:8-9", "8 This book of the law shall not depart out of thy mouth; but thou shalt meditate therein day and night, that thou mayest observe to do according to all that is written therein: for then thou shalt make thy way prosperous, and then thou shalt have good success.\n" +
                "9 Have not I commanded thee? Be strong and of a good courage; be not afraid, neither be thou dismayed: for the Lord thy God is with thee whithersoever thou goest."));
        valuesList.add(new ScriptureEntity("JOS 1:7", " Only be thou strong and very courageous, that thou mayest observe to do according to all the law, which Moses my servant commanded thee: turn not from it to the right hand or to the left, that thou mayest prosper withersoever thou goest."));
        valuesList.add(new ScriptureEntity("JOS 13:1", "Now Joshua was old and stricken in years; and the Lord said unto him, Thou art old and stricken in years, and there remaineth yet very much land to be possessed."));
        valuesList.add(new ScriptureEntity("JOS 24:15", "And if it seem evil unto you to serve the Lord, choose you this day whom ye will serve; whether the gods which your fathers served that were on the other side of the flood, or the gods of the Amorites, in whose land ye dwell: but as for me and my house, we will serve the Lord."));

        valuesList.add(new ScriptureEntity("RUT 1:16-17", "16 And Ruth said, Intreat me not to leave thee, or to return from following after thee: for whither thou goest, I will go; and where thou lodgest, I will lodge: thy people shall be my people, and thy God my God:\n" +
                "17 Where thou diest, will I die, and there will I be buried: the Lord do so to me, and more also, if ought but death part thee and me."));
//        valuesList.add(new ScriptureEntity("", ""));
//        valuesList.add(new ScriptureEntity("", ""));
//        valuesList.add(new ScriptureEntity("", ""));
//        valuesList.add(new ScriptureEntity("", ""));
//        valuesList.add(new ScriptureEntity("",""));
//        valuesList.add(new ScriptureEntity("",""));
//        valuesList.add(new ScriptureEntity("",""));
//        valuesList.add(new ScriptureEntity("",""));
//        valuesList.add(new ScriptureEntity("",""));
//        valuesList.add(new ScriptureEntity("",""));


//        dbHandler.addScripture(new ScriptureEntity("Rom 3:23", "For all have sinned and fallen short of the glory of God"));
//        dbHandler.addScripture(new ScriptureEntity("Rom 6:23", "For the wages of sin is death but the gift of God is life in Christ Jesus our Lord"));

        dbHandler.batchInsertScriptures(valuesList);

    }


    public class uploadScriptures extends AsyncTask<Void, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progress = new ProgressDialog(BooksOfTheBible.this);
            progress.setCancelable(false);
            progress.setTitle("Please wait");
            progress.setMessage("Uploading scriptures...");
            progress.show();
        }

        @Override
        protected Void doInBackground(Void... params) {


            try {

                insertScriptures();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub

            if (e != null) {
                progress.cancel();
                //				new ResponseDialog(MyAccounts.this, "We found an error!", e.getMessage()).showDialog();
            } else {
                super.onPostExecute(result);

                progress.cancel();
                Toast.makeText(BooksOfTheBible.this, "Upload Successful", Toast.LENGTH_LONG).show();
//                Toast.makeText(BooksOfTheBible.this, "Upload Successful", Toast.LENGTH_LONG).show();
                System.out.println("number of records: " + dbHandler.getScriptureCount());


            }


        }

    }


}
