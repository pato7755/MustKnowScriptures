package com.udolgc.mustknowscriptures;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    GridView gridView2;
    GridView gridView3;
    ProgressDialog progress;


    String[] oldTestament = new String[]{
            "GEN", "EXO", "LEV", "NUM", "DEU",
            "JOS", "JUDG", "RUT", "1SAM", "2SAM",
            "1KIN", "2KIN", "1CHR", "2CHR", "EZR",
            "NEH", "EST", "JOB", "PSA", "PRO",
            "ECC", "SONG", "ISA", "JER", "LAM",
            "EZE", "DAN", "HOS", "JOEL", "AMO",
            "OBA", "JON", "MIC", "NAH", "HAB",
            "ZEP", "HAG", "ZEC", "MAL"
    };

    String[] newTestament = new String[]{
            "MAT", "MAR", "LUK", "JOH", "ACT",
            "ROM", "1COR", "2COR", "GAL", "EPH",
            "PHILI", "COL", "1THE", "2THE", "1TIM",
            "2TIM", "TIT", "PHILE", "HEB", "JAM",
            "1PET", "2PET", "1JOH", "2JOH", "3JOH",
            "JUDE", "REV"
    };

    String[] others = new String[]{
            "ALL", "FAV"
    };

    DatabaseHandler dbHandler;
    public static List<ScriptureEntity> scriptureByBook;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.books);

        gridView = (GridView) findViewById(R.id.gridview);
        gridView2 = (GridView) findViewById(R.id.gridview2);
        gridView3 = (GridView) findViewById(R.id.gridview3);

        dbHandler = new DatabaseHandler(BooksOfTheBible.this);

//        dbHandler.deleteAllScriptures();

        SharedPreferences preferences = getSharedPreferences("myprefs", MODE_PRIVATE);
        if (preferences.getBoolean("firstRun", true)) {

            new uploadScriptures().execute();

            preferences.edit().putBoolean("firstRun", false).apply();

        }

        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        List<HashMap<String, String>> aList2 = new ArrayList<HashMap<String, String>>();
        List<HashMap<String, String>> aList3 = new ArrayList<HashMap<String, String>>();

        for (String s : oldTestament) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", s);
            aList.add(hm);
        }

        for (String s : newTestament) {
            HashMap<String, String> hm2 = new HashMap<String, String>();
            hm2.put("txt2", s);
            aList2.add(hm2);
        }

        for (String other : others) {
            HashMap<String, String> hm3 = new HashMap<String, String>();
            hm3.put("txt3", other);
            aList3.add(hm3);
        }

        String[] from = {"txt"};
        int[] to = {R.id.txt};

        String[] from2 = {"txt2"};
        int[] to2 = {R.id.txt2};

        String[] from3 = {"txt3"};
        int[] to3 = {R.id.txt3};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), aList, R.layout.gridview_layout, from, to);
        SimpleAdapter adapter2 = new SimpleAdapter(getBaseContext(), aList2, R.layout.gridview_layout2, from2, to2);
        SimpleAdapter adapter3 = new SimpleAdapter(getBaseContext(), aList3, R.layout.gridview_layout3, from3, to3);

        final GridView gridView = (GridView) findViewById(R.id.gridview);
        gridView.setAdapter(adapter);

        final GridView gridView2 = (GridView) findViewById(R.id.gridview2);
        gridView2.setAdapter(adapter2);

        final GridView gridView3 = (GridView) findViewById(R.id.gridview3);
        gridView3.setAdapter(adapter3);

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

                List<ScriptureEntity> scriptureList = dbHandler.getSelectedBook(book);
                for (ScriptureEntity cn : scriptureList) {
                    String log = "Id: " + cn.getId() + " ,TITLE: " + cn.getTitle() + " ,SCRIPTURE: " +
                            cn.getScripture();
                    // Writing scriptures to log
                    Log.d("Name: ", log);
                }

                scriptureByBook = scriptureList;
                Intent intent = new Intent(BooksOfTheBible.this, MyListActivity.class);
                startActivity(intent);
//                }
            }
        });

        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String book = (String) ((TextView) view.findViewById(R.id.txt2)).getText();

                System.out.println("title: " + book);


                List<ScriptureEntity> scriptureList = dbHandler.getSelectedBook(book);
                for (ScriptureEntity cn : scriptureList) {
                    String log = "Id: " + cn.getId() + " ,TITLE: " + cn.getTitle() + " ,SCRIPTURE: " +
                            cn.getScripture();
                    // Writing scriptures to log
                    Log.d("Name: ", log);
                }

                scriptureByBook = scriptureList;
                Intent intent = new Intent(BooksOfTheBible.this, MyListActivity.class);
                startActivity(intent);

            }
        });


        gridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String book = (String) ((TextView) view.findViewById(R.id.txt3)).getText();

                System.out.println("title: " + book);

                if (book.equals("ALL")) {
                    scriptureByBook = dbHandler.getAllScriptures();
                    Intent intent = new Intent(BooksOfTheBible.this, MyListActivity.class);
                    startActivity(intent);

                } else if (book.equals("FAV")) {
                    scriptureByBook = dbHandler.getFavouriteScriptures();
                    Intent intent = new Intent(BooksOfTheBible.this, MyListActivity.class);
                    startActivity(intent);

                }
            }
        });

    }


//    public void verifyStoragePermissions(Activity activity) {
//        // Check if we have read or write permission
//        int writePermission = ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        if (writePermission != PackageManager.PERMISSION_GRANTED
//            /*|| phoneStatePermission != PackageManager.PERMISSION_GRANTED*/) {
//            // We don't have permission so prompt the user
//            ActivityCompat.requestPermissions(
//                    activity,
//                    PERMISSIONS_STORAGE,
//                    REQUEST_EXTERNAL_STORAGE
//
//            );
//        } else {
//            try {
//                File sd = Environment.getExternalStorageDirectory();
//                File data = Environment.getDataDirectory();
//
//
//                if (sd.canWrite()) {
//                    String currentDBPath = "/data/" + "com.udolgc.mustknowscriptures" + "/databases/SCRIPTURE_DB";
//                    String backupDBPath = "scripture_db_dump.db";
//                    File currentDB = new File(data, currentDBPath);
//                    File backupDB = new File(sd, backupDBPath);
//                    FileChannel source=null;
//                    FileChannel destination=null;
//
//                    try {
//                        source = new FileInputStream(currentDB).getChannel();
//                        destination = new FileOutputStream(backupDB).getChannel();
//                        destination.transferFrom(source, 0, source.size());
//                        source.close();
//                        destination.close();
//                        Toast.makeText(BooksOfTheBible.this, "DB Exported!", Toast.LENGTH_LONG).show();
//                    } catch(IOException e) {
//                        e.printStackTrace();
//                        System.out.println(e.getMessage());
//                    }
//                } else {
//                    System.out.println("cannot write");
//                }
//            } catch (Exception e) {
//                System.out.println("exception occured");
//            }
//        }
//    }


    private static boolean doesDatabaseExist(Context context, String databaseName) {
        File dbFile = context.getDatabasePath(databaseName);
        return dbFile.exists();
    }


    public void insertScriptures() {
//        dbHandler.addScripture(new ScriptureEntity());

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

        // JUDGES

        // RUTH
        valuesList.add(new ScriptureEntity("RUT 1:16-17", "16 And Ruth said, Intreat me not to leave thee, or to return from following after thee: for whither thou goest, I will go; and where thou lodgest, I will lodge: thy people shall be my people, and thy God my God:\n" +
                "17 Where thou diest, will I die, and there will I be buried: the Lord do so to me, and more also, if ought but death part thee and me."));

        // 1SAMUEL
        valuesList.add(new ScriptureEntity("1SAM 12:6", "And Samuel said unto the people, It is the LORD that advanced Moses and Aaron, and that brought your fathers up out of the land of Egypt."));
        valuesList.add(new ScriptureEntity("1SAM 12:23", "Moreover as for me, God forbid that I should sin against the LORD in ceasing to pray for you: but I will teach you the good and the right way:"));
        valuesList.add(new ScriptureEntity("1SAM 15:22-23", "22 And Samuel said, Hath the Lord as great delight in burnt offerings and sacrifices, as in obeying the voice of the Lord? Behold, to obey is better than sacrifice, and to hearken than the fat of rams.\n" +
                "23 For rebellion is as the sin of witchcraft, and stubbornness is as iniquity and idolatry. Because thou hast rejected the word of the Lord, he hath also rejected thee from being king."));
        valuesList.add(new ScriptureEntity("1SAM 16:7", "But the LORD said unto Samuel, Look not on his countenance, or on the height of his stature; because I have refused him: for the LORD seeth not as man seeth; for man looketh on the outward appearance, but the LORD looketh on the heart"));
        valuesList.add(new ScriptureEntity("1SAM 16:15-16", "15 And Saul's servants said unto him, Behold now, an evil spirit from God troubleth thee. " +
                "16 Let our lord now command thy servants, which are before thee, to seek out a man, who is a cunning player on an harp: and it shall come to pass, when the evil spirit from God is upon thee, that he shall play with his hand, and thou shalt be well."));
        valuesList.add(new ScriptureEntity("1SAM 16:23", "And it came to pass, when the evil spirit from God was upon Saul, that David took an harp, and played with his hand: so Saul was refreshed, and was well, and the evil spirit departed from him."));

        // 2SAMUEL
        valuesList.add(new ScriptureEntity("2SAM 1:20", "Tell it not in Gath, publish it not in the streets of Askelon; lest the daughters of the Philistines rejoice, lest the daughters of the uncircumcised triumph."));

        // 1KINGS
        valuesList.add(new ScriptureEntity("1KIN 3:9", "Give therefore thy servant an undertanding heart to jugde thy people, that I may discern between good and bad: for who is able to judge this thy so great a people?"));

        // 2KINGS
        valuesList.add(new ScriptureEntity("2KIN 3:14-15", "14 And Elisha said, As the LORD of hosts liveth, before whom I stand, surely, were it not that I regard the presence of Jehoshaphat the king of Judah, I would not look toward thee, nor see thee. " +
                "15 But now bring me a minstrel. And it came to pass, when the minstrel played, that the hand of the Lord came upon him."));
        valuesList.add(new ScriptureEntity("2KIN 6:5", "But as one was felling a beam, the axe head fell into the water: and he cried, and said, Alas, master! for it was borrowed."));

        // 1CHRONICLES
        valuesList.add(new ScriptureEntity("1CHR 12:32", "And of the children of Issachar, which were men that had understanding of the times, to know what Israel ought to do; the heads of them were two hundred; and all their brethren were at their commandment."));

        // 2CHRONICLES
        valuesList.add(new ScriptureEntity("2CHR 20:20", "And they rose early in the morning, and went forth into the wilderness of Tekoa: and as they went forth, Jehoshaphat stood and said, Hear me, O Judah, and ye inhabitants of Jerusalem; Believe in the LORD your God, so shall ye be established; believe his prophets, so shall ye prosper."));
        valuesList.add(new ScriptureEntity("2CHR 7:14", "If my people, which are called by my name, shall humble themselves, and pray, and seek my face, and turn from their wicked ways; then will I hear from heaven, and will forgive their sin, and will heal their land."));

        // EZRA
        valuesList.add(new ScriptureEntity("EZR 7:10", "For Ezra had prepared his heart to seek the law of the LORD, and to do it, and to teach in Israel statutes and judgments."));

        // NEHEMIAH
        valuesList.add(new ScriptureEntity("NEH 2:20", "Then answered I them, and said unto them, The God of heaven, he will prosper us; therefore we his servants will arise and build: but ye have no portion, nor right, nor memorial, in Jerusalem."));

        // ESTHER
        valuesList.add(new ScriptureEntity("EST 4:14", "For if thou altogether holdest thy peace at this time, then shall there enlargement and deliverance arise to the Jews from another place; but thou and thy father's house shall be destroyed: and who knoweth whether thou art come to the kingdom for such a time as this?"));

        // JOB
        valuesList.add(new ScriptureEntity("JOB 1:10", "Hast not thou made an hedge about him, and about his house, and about all that he hath on every side? thou hast blessed the work of his hands, and his substance is increased in the land."));
        valuesList.add(new ScriptureEntity("JOB 3:25", "For the thing which I greatly feared is come upon me, and that which I was afraid is come unto me."));
        valuesList.add(new ScriptureEntity("JOB 7:1", "For the thing which I greatly feared is come upon me, and that which I was afraid is come unto me."));
        valuesList.add(new ScriptureEntity("JOB 14:1-2", "1 Man that is born of a woman is of few days, and full of trouble.\n" +
                "2 He cometh forth like a flower, and is cut down: he fleeth also as a shadow, and continueth not."));
        valuesList.add(new ScriptureEntity("JOB 14:5", "Seeing his days are determined, the number of his months are with thee, thou hast appointed his bounds that he cannot pass;"));
        valuesList.add(new ScriptureEntity("JOB 29:4-6", "4 As I was in the days of my youth, when the secret of God was upon my tabernacle;\n" +
                "5 When the Almighty was yet with me, when my children were about me;\n" +
                "6 When I washed my steps with butter, and the rock poured me out rivers of oil;"));

        // PSALMS
        valuesList.add(new ScriptureEntity("PSA 23:3", "He restoreth my soul: he leadeth me in the paths of righteousness for his name's sake."));
        valuesList.add(new ScriptureEntity("PSA 34:8-10", "8 O taste and see that the Lord is good: blessed is the man that trusteth in him.\n" +
                "9 O fear the Lord, ye his saints: for there is no want to them that fear him.\n" +
                "10 The young lions do lack, and suffer hunger: but they that seek the Lord shall not want any good thing."));
        valuesList.add(new ScriptureEntity("PSA 37:23", "The steps of a good man are ordered by the Lord: and he delighteth in his way."));
        valuesList.add(new ScriptureEntity("PSA 42:7", "Deep calleth unto deep at the noise of thy waterspouts: all thy waves and thy billows are gone over me."));
        valuesList.add(new ScriptureEntity("PSA 84:7", "They go from strength to strength, every one of them in Zion appeareth before God."));
        valuesList.add(new ScriptureEntity("PSA 84:11", "For the Lord God is a sun and shield: the Lord will give grace and glory: no good thing will he withhold from them that walk uprightly."));
        valuesList.add(new ScriptureEntity("PSA 91:7-8", "7 A thousand shall fall at thy side, and ten thousand at thy right hand; but it shall not come nigh thee.\n" +
                "8 Only with thine eyes shalt thou behold and see the reward of the wicked."));
        valuesList.add(new ScriptureEntity("PSA 92:13", "Those that be planted in the house of the Lord shall flourish in the courts of our God."));
        valuesList.add(new ScriptureEntity("PSA 103:1-3", "1 Bless the Lord, O my soul: and all that is within me, bless his holy name.\n" +
                "2 Bless the Lord, O my soul, and forget not all his benefits:\n" +
                "3 Who forgiveth all thine iniquities; who healeth all thy diseases;"));
        valuesList.add(new ScriptureEntity("PSA 109:6-12", "6 Set thou a wicked man over him: and let Satan stand at his right hand.\n" +
                "7 When he shall be judged, let him be condemned: and let his prayer become sin.\n" +
                "8 Let his days be few; and let another take his office.\n" +
                "9 Let his children be fatherless, and his wife a widow.\n" +
                "10 Let his children be continually vagabonds, and beg: let them seek their bread also out of their desolate places.\n" +
                "11 Let the extortioner catch all that he hath; and let the strangers spoil his labour.\n" +
                "12 Let there be none to extend mercy unto him: neither let there be any to favour his fatherless children."));
        valuesList.add(new ScriptureEntity("PSA 119:9", "Wherewithal shall a young man cleanse his way? by taking heed thereto according to thy word."));
        valuesList.add(new ScriptureEntity("PSA 119:67", "Before I was afflicted I went astray: but now have I kept thy word."));
        valuesList.add(new ScriptureEntity("PSA 119:99", "I have more understanding than all my teachers: for thy testimonies are my meditation."));
        valuesList.add(new ScriptureEntity("PSA 119:105", "Thy word is a lamp unto my feet, and a light unto my path."));
        valuesList.add(new ScriptureEntity("PSA 119:130", "The entrance of thy words giveth light; it giveth understanding unto the simple."));
        valuesList.add(new ScriptureEntity("PSA 127:1", "Except the Lord build the house, they labour in vain that build it: except the Lord keep the city, the watchman waketh but in vain."));
        valuesList.add(new ScriptureEntity("PSA 127:3", "Lo, children are an heritage of the Lord: and the fruit of the womb is his reward."));
        valuesList.add(new ScriptureEntity("PSA 131:1", "Lord, my heart is not haughty, nor mine eyes lofty: neither do I exercise myself in great matters, or in things too high for me."));

        // PROVERBS
        valuesList.add(new ScriptureEntity("PRO 3:5-6", "5 Trust in the Lord with all thine heart; and lean not unto thine own understanding.\n" +
                "6 In all thy ways acknowledge him, and he shall direct thy paths."));
        valuesList.add(new ScriptureEntity("PRO 3:9-10", "9 Honour the Lord with thy substance, and with the firstfruits of all thine increase:\n" +
                "10 So shall thy barns be filled with plenty, and thy presses shall burst out with new wine."));
        valuesList.add(new ScriptureEntity("PRO 4:20-23", "20 My son, attend to my words; incline thine ear unto my sayings.\n" +
                "21 Let them not depart from thine eyes; keep them in the midst of thine heart.\n" +
                "22 For they are life unto those that find them, and health to all their flesh.\n" +
                "23 Keep thy heart with all diligence; for out of it are the issues of life."));
        valuesList.add(new ScriptureEntity("PRO 9:8", "Reprove not a scorner, lest he hate thee: rebuke a wise man, and he will love thee."));
        valuesList.add(new ScriptureEntity("PRO 11:1", "A false balance is abomination to the Lord: but a just weight is his delight."));
        valuesList.add(new ScriptureEntity("PRO 14:12", "There is a way which seemeth right unto a man, but the end thereof are the ways of death."));
        valuesList.add(new ScriptureEntity("PRO 15:1", "A soft answer turneth away wrath: but grievous words stir up anger."));
        valuesList.add(new ScriptureEntity("PRO 16:25", "There is a way that seemeth right unto a man, but the end thereof are the ways of death."));
        valuesList.add(new ScriptureEntity("PRO 22:1", "A good name is rather to be chosen than great riches, and loving favour rather than silver and gold."));
        valuesList.add(new ScriptureEntity("PRO 22:13", "The slothful man saith, There is a lion without, I shall be slain in the streets."));
        valuesList.add(new ScriptureEntity("PRO 22:29", "Seest thou a man diligent in his business? he shall stand before kings; he shall not stand before mean men."));
        valuesList.add(new ScriptureEntity("PRO 24:3-4", "3 Through wisdom is an house builded; and by understanding it is established:\n" +
                "4 And by knowledge shall the chambers be filled with all precious and pleasant riches."));
        valuesList.add(new ScriptureEntity("PRO 29:4", "The king by judgment establisheth the land: but he that receiveth gifts overthroweth it."));
        valuesList.add(new ScriptureEntity("PRO 29:18", "Where there is no vision, the people perish: but he that keepeth the law, happy is he."));

        // ECCLESIASTES
        valuesList.add(new ScriptureEntity("ECC 1:9-10", "9 The thing that hath been, it is that which shall be; and that which is done is that which shall be done: and there is no new thing under the sun.\n" +
                "10 Is there any thing whereof it may be said, See, this is new? it hath been already of old time, which was before us."));
        valuesList.add(new ScriptureEntity("ECC 3:1", "To every thing there is a season, and a time to every purpose under the heaven:"));
        valuesList.add(new ScriptureEntity("ECC 3:11", "He hath made every thing beautiful in his time: also he hath set the world in their heart, so that no man can find out the work that God maketh from the beginning to the end."));
        valuesList.add(new ScriptureEntity("ECC 4:8", "There is one alone, and there is not a second; yea, he hath neither child nor brother: yet is there no end of all his labour; neither is his eye satisfied with riches; neither saith he, For whom do I labour, and bereave my soul of good? This is also vanity, yea, it is a sore travail."));
        valuesList.add(new ScriptureEntity("ECC 4:13", "Better is a poor and a wise child than an old and foolish king, who will no more be admonished."));
        valuesList.add(new ScriptureEntity("ECC 5:4", "When thou vowest a vow unto God, defer not to pay it; for he hath no pleasure in fools: pay that which thou hast vowed."));
        valuesList.add(new ScriptureEntity("ECC 5:18", "Behold that which I have seen: it is good and comely for one to eat and to drink, and to enjoy the good of all his labour that he taketh under the sun all the days of his life, which God giveth him: for it is his portion."));
        valuesList.add(new ScriptureEntity("ECC 7:1", "A good name is better than precious ointment; and the day of death than the day of one's birth."));
        valuesList.add(new ScriptureEntity("ECC 9:9", "Live joyfully with the wife whom thou lovest all the days of the life of thy vanity, which he hath given thee under the sun, all the days of thy vanity: for that is thy portion in this life, and in thy labour which thou takest under the sun."));
        valuesList.add(new ScriptureEntity("ECC 9:10", "Whatsoever thy hand findeth to do, do it with thy might; for there is no work, nor device, nor knowledge, nor wisdom, in the grave, whither thou goest."));
        valuesList.add(new ScriptureEntity("ECC 9:14-16", "14 There was a little city, and few men within it; and there came a great king against it, and besieged it, and built great bulwarks against it:\n" +
                "15 Now there was found in it a poor wise man, and he by his wisdom delivered the city; yet no man remembered that same poor man.\n" +
                "16 Then said I, Wisdom is better than strength: nevertheless the poor man's wisdom is despised, and his words are not heard."));
        valuesList.add(new ScriptureEntity("ECC 10:16-17", "16 Woe to thee, O land, when thy king is a child, and thy princes eat in the morning!\n" +
                "17 Blessed art thou, O land, when thy king is the son of nobles, and thy princes eat in due season, for strength, and not for drunkenness!"));
        valuesList.add(new ScriptureEntity("ECC 12:1", "Remember now thy Creator in the days of thy youth, while the evil days come not, nor the years draw nigh, when thou shalt say, I have no pleasure in them;"));
        valuesList.add(new ScriptureEntity("ECC 12:9", "And moreover, because the preacher was wise, he still taught the people knowledge; yea, he gave good heed, and sought out, and set in order many proverbs."));
        valuesList.add(new ScriptureEntity("ECC 12:13", "Let us hear the conclusion of the whole matter: Fear God, and keep his commandments: for this is the whole duty of man."));

        // SONGS OF SOLOMON
        valuesList.add(new ScriptureEntity("SONG 1:3", "Because of the savour of thy good ointments thy name is as ointment poured forth, therefore do the virgins love thee."));
        valuesList.add(new ScriptureEntity("SONG 1:4", "Draw me, we will run after thee: the king hath brought me into his chambers: we will be glad and rejoice in thee, we will remember thy love more than wine: the upright love thee."));
        valuesList.add(new ScriptureEntity("SONG 8:4", "I charge you, O daughters of Jerusalem, that ye stir not up, nor awake my love, until he please."));

        // ISAIAH
        valuesList.add(new ScriptureEntity("ISA 1:3", "The ox knoweth his owner, and the ass his master's crib: but Israel doth not know, my people doth not consider."));
        valuesList.add(new ScriptureEntity("ISA 1:18", "Come now, and let us reason together, saith the Lord: though your sins be as scarlet, they shall be as white as snow; though they be red like crimson, they shall be as wool."));
        valuesList.add(new ScriptureEntity("ISA 1:19", "If ye be willing and obedient, ye shall eat the good of the land:"));
        valuesList.add(new ScriptureEntity("ISA 2:2-3", "2 And it shall come to pass in the last days, that the mountain of the Lord's house shall be established in the top of the mountains, and shall be exalted above the hills; and all nations shall flow unto it.\n" +
                "3 And many people shall go and say, Come ye, and let us go up to the mountain of the Lord, to the house of the God of Jacob; and he will teach us of his ways, and we will walk in his paths: for out of Zion shall go forth the law, and the word of the Lord from Jerusalem."));
        valuesList.add(new ScriptureEntity("ISA 3:10", "Say ye to the righteous, that it shall be well with him: for they shall eat the fruit of their doings."));
        valuesList.add(new ScriptureEntity("ISA 3:12", "As for my people, children are their oppressors, and women rule over them. O my people, they which lead thee cause thee to err, and destroy the way of thy paths."));
        valuesList.add(new ScriptureEntity("ISA 5:13", "Therefore my people are gone into captivity, because they have no knowledge: and their honourable men are famished, and their multitude dried up with thirst."));
        valuesList.add(new ScriptureEntity("ISA 6:10", "Make the heart of this people fat, and make their ears heavy, and shut their eyes; lest they see with their eyes, and hear with their ears, and understand with their heart, and convert, and be healed.\n"));
        valuesList.add(new ScriptureEntity("ISA 7:14", "Therefore the Lord himself shall give you a sign; Behold, a virgin shall conceive, and bear a son, and shall call his name Immanuel."));
        valuesList.add(new ScriptureEntity("ISA 9:7", "Of the increase of his government and peace there shall be no end, upon the throne of David, and upon his kingdom, to order it, and to establish it with judgment and with justice from henceforth even for ever. The zeal of the Lord of hosts will perform this."));
        valuesList.add(new ScriptureEntity("ISA 9:8", "The Lord sent a word into Jacob, and it hath lighted upon Israel."));
        valuesList.add(new ScriptureEntity("ISA 10:27", "And it shall come to pass in that day, that his burden shall be taken away from off thy shoulder, and his yoke from off thy neck, and the yoke shall be destroyed because of the anointing."));
        valuesList.add(new ScriptureEntity("ISA 11:1-3", "1 And there shall come forth a rod out of the stem of Jesse, and a Branch shall grow out of his roots:\n" +
                "2 And the spirit of the Lord shall rest upon him, the spirit of wisdom and understanding, the spirit of counsel and might, the spirit of knowledge and of the fear of the Lord;\n" +
                "3 And shall make him of quick understanding in the fear of the Lord: and he shall not judge after the sight of his eyes, neither reprove after the hearing of his ears:"));
        valuesList.add(new ScriptureEntity("ISA 14:9", "Hell from beneath is moved for thee to meet thee at thy coming: it stirreth up the dead for thee, even all the chief ones of the earth; it hath raised up from their thrones all the kings of the nations"));
        valuesList.add(new ScriptureEntity("ISA 26:18", "We have been with child, we have been in pain, we have as it were brought forth wind; we have not wrought any deliverance in the earth; neither have the inhabitants of the world fallen."));
        valuesList.add(new ScriptureEntity("ISA 28:9-11", "9 Whom shall he teach knowledge? and whom shall he make to understand doctrine? them that are weaned from the milk, and drawn from the breasts.\n" +
                "10 For precept must be upon precept, precept upon precept; line upon line, line upon line; here a little, and there a little:\n" +
                "11 For with stammering lips and another tongue will he speak to this people."));
        valuesList.add(new ScriptureEntity("ISA 30:1", "Woe to the rebellious children, saith the Lord, that take counsel, but not of me; and that cover with a covering, but not of my spirit, that they may add sin to sin:"));
        valuesList.add(new ScriptureEntity("ISA 30:21", "And thine ears shall hear a word behind thee, saying, This is the way, walk ye in it, when ye turn to the right hand, and when ye turn to the left."));
        valuesList.add(new ScriptureEntity("ISA 33:6", "And wisdom and knowledge shall be the stability of thy times, and strength of salvation: the fear of the Lord is his treasure."));
        valuesList.add(new ScriptureEntity("ISA 37:3", "And they said unto him, Thus saith Hezekiah, This day is a day of trouble, and of rebuke, and of blasphemy: for the children are come to the birth, and there is not strength to bring forth."));
        valuesList.add(new ScriptureEntity("ISA 40:6-8", "6 The voice said, Cry. And he said, What shall I cry? All flesh is grass, and all the goodliness thereof is as the flower of the field:\n" +
                "7 The grass withereth, the flower fadeth: because the spirit of the Lord bloweth upon it: surely the people is grass.\n" +
                "8 The grass withereth, the flower fadeth: but the word of our God shall stand for ever.\n"));
        valuesList.add(new ScriptureEntity("ISA 40:31", "But they that wait upon the Lord shall renew their strength; they shall mount up with wings as eagles; they shall run, and not be weary; and they shall walk, and not faint."));
        valuesList.add(new ScriptureEntity("ISA 42:19", "Who is blind, but my servant? or deaf, as my messenger that I sent? who is blind as he that is perfect, and blind as the Lord's servant?"));
        valuesList.add(new ScriptureEntity("ISA 43:2", "When thou passest through the waters, I will be with thee; and through the rivers, they shall not overflow thee: when thou walkest through the fire, thou shalt not be burned; neither shall the flame kindle upon thee."));
        valuesList.add(new ScriptureEntity("ISA 48:17-19", "17 Thus saith the Lord, thy Redeemer, the Holy One of Israel; I am the Lord thy God which teacheth thee to profit, which leadeth thee by the way that thou shouldest go.\n" +
                "18 O that thou hadst hearkened to my commandments! then had thy peace been as a river, and thy righteousness as the waves of the sea:\n" +
                "19 Thy seed also had been as the sand, and the offspring of thy bowels like the gravel thereof; his name should not have been cut off nor destroyed from before me."));
        valuesList.add(new ScriptureEntity("ISA 48:21", "And they thirsted not when he led them through the deserts: he caused the waters to flow out of the rock for them: he clave the rock also, and the waters gushed out."));
        valuesList.add(new ScriptureEntity("ISA 50:1-2", "1 Thus saith the Lord, Where is the bill of your mother's divorcement, whom I have put away? or which of my creditors is it to whom I have sold you? Behold, for your iniquities have ye sold yourselves, and for your transgressions is your mother put away.\n" +
                "2 Wherfore, when I came, was there no man? when I called, was there none to answer? Is my hand shortened at all, that it cannot redeem? or have I no power to deliver? behold, at my rebuke I dry up the sea, I make the rivers a wilderness: their fish stinketh, because there is no water, and dieth for thirst.\n"));
        valuesList.add(new ScriptureEntity("ISA 51:1-2", "1 Hearken to me, ye that follow after righteousness, ye that seek the Lord: look unto the rock whence ye are hewn, and to the hole of the pit whence ye are digged.\n" +
                "2 Look unto Abraham your father, and unto Sarah that bare you: for I called him alone, and blessed him, and increased him."));
        valuesList.add(new ScriptureEntity("ISA 52:7", "How beautiful upon the mountains are the feet of him that bringeth good tidings, that publisheth peace; that bringeth good tidings of good, that publisheth salvation; that saith unto Zion, Thy God reigneth!"));
        valuesList.add(new ScriptureEntity("ISA 53:4,6", "4 Surely he hath borne our griefs, and carried our sorrows: yet we did esteem him stricken, smitten of God, and afflicted.\n" +
                "\n" +
                "6 All we like sheep have gone astray; we have turned every one to his own way; and the Lord hath laid on him the iniquity of us all.\n"));
        valuesList.add(new ScriptureEntity("ISA 54:1-3", "1 Sing, O barren, thou that didst not bear; break forth into singing, and cry aloud, thou that didst not travail with child: for more are the children of the desolate than the children of the married wife, saith the Lord.\n" +
                "2 Enlarge the place of thy tent, and let them stretch forth the curtains of thine habitations: spare not, lengthen thy cords, and strengthen thy stakes;\n" +
                "3 For thou shalt break forth on the right hand and on the left; and thy seed shall inherit the Gentiles, and make the desolate cities to be inhabited."));
        valuesList.add(new ScriptureEntity("ISA 54:17", "No weapon that is formed against thee shall prosper; and every tongue that shall rise against thee in judgment thou shalt condemn. This is the heritage of the servants of the Lord, and their righteousness is of me, saith the Lord"));
        valuesList.add(new ScriptureEntity("ISA 55:7-9", "7 Let the wicked forsake his way, and the unrighteous man his thoughts: and let him return unto the Lord, and he will have mercy upon him; and to our God, for he will abundantly pardon.\n" +
                "8 For my thoughts are not your thoughts, neither are your ways my ways, saith the Lord.\n" +
                "9 For as the heavens are higher than the earth, so are my ways higher than your ways, and my thoughts than your thoughts.\n"));
        valuesList.add(new ScriptureEntity("ISA 55:10-11", "10 For as the rain cometh down, and the snow from heaven, and returneth not thither, but watereth the earth, and maketh it bring forth and bud, that it may give seed to the sower, and bread to the eater:\n" +
                "11 So shall my word be that goeth forth out of my mouth: it shall not return unto me void, but it shall accomplish that which I please, and it shall prosper in the thing whereto I sent it.\n"));
        valuesList.add(new ScriptureEntity("ISA 58:12", "And they that shall be of thee shall build the old waste places: thou shalt raise up the foundations of many generations; and thou shalt be called, The repairer of the breach, The restorer of paths to dwell in."));
        valuesList.add(new ScriptureEntity("ISA 58:8-9", "8 Then shall thy light break forth as the morning, and thine health shall spring forth speedily: and thy righteousness shall go before thee; the glory of the Lord shall be thy rearward.\n" +
                "9 Then shalt thou call, and the Lord shall answer; thou shalt cry, and he shall say, Here I am. If thou take away from the midst of thee the yoke, the putting forth of the finger, and speaking vanity;\n"));
        valuesList.add(new ScriptureEntity("ISA 59:1-2", "1 Behold, the Lord's hand is not shortened, that it cannot save; neither his ear heavy, that it cannot hear:\n" +
                "2 But your iniquities have separated between you and your God, and your sins have hid his face from you, that he will not hear.\n"));
        valuesList.add(new ScriptureEntity("ISA 60:1", "Arise, shine; for thy light is come, and the glory of the Lord is risen upon thee."));
        valuesList.add(new ScriptureEntity("ISA 64:6", "But we are all as an unclean thing, and all our righteousnesses are as filthy rags; and we all do fade as a leaf; and our iniquities, like the wind, have taken us away."));
        valuesList.add(new ScriptureEntity("ISA 66:7", "Before she travailed, she brought forth; before her pain came, she was delivered of a man child."));

        // JEREMIAH
        valuesList.add(new ScriptureEntity("JER 1:5", "Before I formed thee in the belly I knew thee; and before thou camest forth out of the womb I sanctified thee, and I ordained thee a prophet unto the nations."));
        valuesList.add(new ScriptureEntity("JER 1:10", "See, I have this day set thee over the nations and over the kingdoms, to root out, and to pull down, and to destroy, and to throw down, to build, and to plant."));
        valuesList.add(new ScriptureEntity("JER 3:15", "And I will give you pastors according to mine heart, which shall feed you with knowledge and understanding."));
        valuesList.add(new ScriptureEntity("JER 6:14", "They have healed also the hurt of the daughter of my people slightly, saying, Peace, peace; when there is no peace."));
        valuesList.add(new ScriptureEntity("JER 6:16", "Thus saith the Lord, Stand ye in the ways, and see, and ask for the old paths, where is the good way, and walk therein, and ye shall find rest for your souls. But they said, We will not walk therein."));
        valuesList.add(new ScriptureEntity("JER 8:11", "For they have healed the hurt of the daughter of my people slightly, saying, Peace, peace; when there is no peace."));
        valuesList.add(new ScriptureEntity("JER 8:20", "The harvest is past, the summer is ended, and we are not saved."));
        valuesList.add(new ScriptureEntity("JER 8:22", "Is there no balm in Gilead; is there no physician there? why then is not the health of the daughter of my people recovered?"));
        valuesList.add(new ScriptureEntity("JER 10:21", "For the pastors are become brutish, and have not sought the Lord: therefore they shall not prosper, and all their flocks shall be scattered"));
        valuesList.add(new ScriptureEntity("JER 17:9", "The heart is deceitful above all things, and desperately wicked: who can know it?"));
        valuesList.add(new ScriptureEntity("JER 17:14", "Heal me, O Lord, and I shall be healed; save me, and I shall be saved: for thou art my praise."));
        valuesList.add(new ScriptureEntity("JER 18:2", "Arise, and go down to the potter's house, and there I will cause thee to hear my words."));
        valuesList.add(new ScriptureEntity("JER 22:21", "I spake unto thee in thy prosperity; but thou saidst, I will not hear. This hath been thy manner from thy youth, that thou obeyedst not my voice."));
        valuesList.add(new ScriptureEntity("JER 23:2", "Therefore thus saith the Lord God of Israel against the pastors that feed my people; Ye have scattered my flock, and driven them away, and have not visited them: behold, I will visit upon you the evil of your doings, saith the Lord."));
        valuesList.add(new ScriptureEntity("JER 29:11", "For I know the thoughts that I think toward you, saith the Lord, thoughts of peace, and not of evil, to give you an expected end."));
        valuesList.add(new ScriptureEntity("JER 29:13", "And ye shall seek me, and find me, when ye shall search for me with all your heart"));
        valuesList.add(new ScriptureEntity("JER 33:3", "Call unto me, and I will answer thee, and shew thee great and mighty things, which thou knowest not."));
        valuesList.add(new ScriptureEntity("JER 48:11", "Moab hath been at ease from his youth, and he hath settled on his lees, and hath not been emptied from vessel to vessel, neither hath he gone into captivity: therefore his taste remained in him, and his scent is not changed."));
        valuesList.add(new ScriptureEntity("JER 51:20", "Thou art my battle axe and weapons of war: for with thee will I break in pieces the nations, and with thee will I destroy kingdoms;"));


        // LAMENTATIONS
        valuesList.add(new ScriptureEntity("LAM 3:22-23", "22 It is of the Lord's mercies that we are not consumed, because his compassions fail not.\n" +
                "23 They are new every morning: great is thy faithfulness."));
        valuesList.add(new ScriptureEntity("LAM 3:37", "Who is he that saith, and it cometh to pass, when the Lord commandeth it not?"));

        // EZEKIEL
        valuesList.add(new ScriptureEntity("EZE 2:2", "And the spirit entered into me when he spake unto me, and set me upon my feet, that I heard him that spake unto me."));
        valuesList.add(new ScriptureEntity("EZE 3:22-24", "22 And the hand of the Lord was there upon me; and he said unto me, Arise, go forth into the plain, and I will there talk with thee.\n" +
                "23 Then I arose, and went forth into the plain: and, behold, the glory of the Lord stood there, as the glory which I saw by the river of Chebar: and I fell on my face.\n" +
                "24 Then the spirit entered into me, and set me upon my feet, and spake with me, and said unto me, Go, shut thyself within thine house.\n"));
        valuesList.add(new ScriptureEntity("EZE 34:2", "Son of man, prophesy against the shepherds of Israel, prophesy, and say unto them, Thus saith the Lord God unto the shepherds; Woe be to the shepherds of Israel that do feed themselves! should not the shepherds feed the flocks?"));
        valuesList.add(new ScriptureEntity("EZE 37:3", "And he said unto me, Son of man, can these bones live? And I answered, O Lord God, thou knowest"));

        // DANIEL
        valuesList.add(new ScriptureEntity("DAN 1:17", "As for these four children, God gave them knowledge and skill in all learning and wisdom: and Daniel had understanding in all visions and dreams."));
        valuesList.add(new ScriptureEntity("DAN 1:20", "And in all matters of wisdom and understanding, that the king inquired of them, he found them ten times better than all the magicians and astrologers that were in all his realm."));
        valuesList.add(new ScriptureEntity("DAN 11:32", "And such as do wickedly against the covenant shall he corrupt by flatteries: but the people that do know their God shall be strong, and do exploits."));
        valuesList.add(new ScriptureEntity("DAN 5:11-12", "11 There is a man in thy kingdom, in whom is the spirit of the holy gods; and in the days of thy father light and understanding and wisdom, like the wisdom of the gods, was found in him; whom the king Nebuchadnezzar thy father, the king, I say, thy father, made master of the magicians, astrologers, Chaldeans, and soothsayers;\n" +
                "12 Forasmuch as an excellent spirit, and knowledge, and understanding, interpreting of dreams, and shewing of hard sentences, and dissolving of doubts, were found in the same Daniel, whom the king named Belteshazzar: now let Daniel be called, and he will shew the interpretation.\n" +
                "\n"));
        valuesList.add(new ScriptureEntity("DAN 11:32B", "...the people that do know their God shall be strong, and do exploits."));
        valuesList.add(new ScriptureEntity("DAN 12:3", "And they that be wise shall shine as the brightness of the firmament; and they that turn many to righteousness as the stars for ever and ever."));

        // HOSEA
        valuesList.add(new ScriptureEntity("HOS 4:6", "My people are destroyed for lack of knowledge: because thou hast rejected knowledge, I will also reject thee, that thou shalt be no priest to me: seeing thou hast forgotten the law of thy God, I will also forget thy children."));
        valuesList.add(new ScriptureEntity("HOS 10:12", "Sow to yourselves in righteousness, reap in mercy; break up your fallow ground: for it is time to seek the Lord, till he come and rain righteousness upon you."));
        valuesList.add(new ScriptureEntity("HOS 12:13", "And by a prophet the Lord brought Israel out of Egypt, and by a prophet was he preserved."));

        // JOEL
        valuesList.add(new ScriptureEntity("JOEL 2:1", "Blow ye the trumpet in Zion, and sound an alarm in my holy mountain: let all the inhabitants of the land tremble: for the day of the Lord cometh, for it is nigh at hand;"));
        valuesList.add(new ScriptureEntity("JOEL 2:7", "They shall run like mighty men; they shall climb the wall like men of war; and they shall march every one on his ways, and they shall not break their ranks:"));
        valuesList.add(new ScriptureEntity("JOEL 2:25-26", "25 And I will restore to you the years that the locust hath eaten, the cankerworm, and the caterpiller, and the palmerworm, my great army which I sent among you.\n" +
                "26 And ye shall eat in plenty, and be satisfied, and praise the name of the Lord your God, that hath dealt wondrously with you: and my people shall never be ashamed.\n"));
        valuesList.add(new ScriptureEntity("JOEL 2:28-29", "28 And it shall come to pass afterward, that I will pour out my spirit upon all flesh; and your sons and your daughters shall prophesy, your old men shall dream dreams, your young men shall see visions:\n" +
                "29 And also upon the servants and upon the handmaids in those days will I pour out my spirit."));

        // AMOS
        valuesList.add(new ScriptureEntity("AMO 3:3", "Can two walk together, except they be agreed?"));
        valuesList.add(new ScriptureEntity("AMO 4:12", "Therefore thus will I do unto thee, O Israel: and because I will do this unto thee, prepare to meet thy God, O Israel."));
        valuesList.add(new ScriptureEntity("AMO 6:1", "Woe to them that are at ease in Zion, and trust in the mountain of Samaria, which are named chief of the nations, to whom the house of Israel came!"));

        // JONAH
        valuesList.add(new ScriptureEntity("JON 1:1-2", "1 Now the word of the Lord came unto Jonah the son of Amittai, saying,\n" +
                "2 Arise, go to Nineveh, that great city, and cry against it; for their wickedness is come up before me."));
        valuesList.add(new ScriptureEntity("JON 2:2", "And said, I cried by reason of mine affliction unto the Lord, and he heard me; out of the belly of hell cried I, and thou heardest my voice."));
        valuesList.add(new ScriptureEntity("JON 4:11", "And should not I spare Nineveh, that great city, wherein are more than sixscore thousand persons that cannot discern between their right hand and their left hand; and also much cattle?"));

        // MICAH
        valuesList.add(new ScriptureEntity("MIC 4:1", "But in the last days it shall come to pass, that the mountain of the house of the Lord shall be established in the top of the mountains, and it shall be exalted above the hills; and people shall flow unto it."));
        valuesList.add(new ScriptureEntity("MIC 4:2", "And many nations shall come, and say, Come, and let us go up to the mountain of the Lord, and to the house of the God of Jacob; and he will teach us of his ways, and we will walk in his paths: for the law shall go forth of Zion, and the word of the Lord from Jerusalem."));
        valuesList.add(new ScriptureEntity("MIC 6:8", "He hath shewed thee, O man, what is good; and what doth the Lord require of thee, but to do justly, and to love mercy, and to walk humbly with thy God?"));
        valuesList.add(new ScriptureEntity("MIC 7:8", "Rejoice not against me, O mine enemy: when I fall, I shall arise; when I sit in darkness, the Lord shall be a light unto me.\n"));

        // HABAKKUK
        valuesList.add(new ScriptureEntity("HAB 2:1", "I will stand upon my watch, and set me upon the tower, and will watch to see what he will say unto me, and what I shall answer when I am reproved."));
        valuesList.add(new ScriptureEntity("HAB 2:2", "And the Lord answered me, and said, Write the vision, and make it plain upon tables, that he may run that readeth it."));
        valuesList.add(new ScriptureEntity("HAB 2:3", "For the vision is yet for an appointed time, but at the end it shall speak, and not lie: though it tarry, wait for it; because it will surely come, it will not tarry."));
        valuesList.add(new ScriptureEntity("HAB 2:4", "Behold, his soul which is lifted up is not upright in him: but the just shall live by his faith."));
        valuesList.add(new ScriptureEntity("HAB 3:17-18", "17 Although the fig tree shall not blossom, neither shall fruit be in the vines; the labour of the olive shall fail, and the fields shall yield no meat; the flock shall be cut off from the fold, and there shall be no herd in the stalls:\n" +
                "18 Yet I will rejoice in the Lord, I will joy in the God of my salvation."));

        // ZEPHANIAH
        valuesList.add(new ScriptureEntity("ZEP 3:17", "The Lord thy God in the midst of thee is mighty; he will save, he will rejoice over thee with joy; he will rest in his love, he will joy over thee with singing."));

        // HAGGAI
        valuesList.add(new ScriptureEntity("HAG 1:6", "Ye have sown much, and bring in little; ye eat, but ye have not enough; ye drink, but ye are not filled with drink; ye clothe you, but there is none warm; and he that earneth wages earneth wages to put it into a bag with holes."));
        valuesList.add(new ScriptureEntity("HAG 1:9", "Ye looked for much, and, lo, it came to little; and when ye brought it home, I did blow upon it. Why? saith the Lord of hosts. Because of mine house that is waste, and ye run every man unto his own house."));

        // ZECHARIAH
        valuesList.add(new ScriptureEntity("ZEC 1:5", "Your fathers, where are they? and the prophets, do they live for ever?"));
        valuesList.add(new ScriptureEntity("ZEC 1:17", "Cry yet, saying, Thus saith the Lord of hosts; My cities through prosperity shall yet be spread abroad; and the Lord shall yet comfort Zion, and shall yet choose Jerusalem."));
        valuesList.add(new ScriptureEntity("ZEC 3:1-2", "1 And he shewed me Joshua the high priest standing before the angel of the Lord, and Satan standing at his right hand to resist him.\n" +
                "2 And the Lord said unto Satan, The Lord rebuke thee, O Satan; even the Lord that hath chosen Jerusalem rebuke thee: is not this a brand plucked out of the fire?\n"));
        valuesList.add(new ScriptureEntity("ZEC 4:6", "Then he answered and spake unto me, saying, This is the word of the Lord unto Zerubbabel, saying, Not by might, nor by power, but by my spirit, saith the Lord of hosts."));
        valuesList.add(new ScriptureEntity("ZEC 5:4", "I will bring it forth, saith the Lord of hosts, and it shall enter into the house of the thief, and into the house of him that sweareth falsely by my name: and it shall remain in the midst of his house, and shall consume it with the timber thereof and the stones thereof."));
        valuesList.add(new ScriptureEntity("ZEC 10:1", "Ask ye of the Lord rain in the time of the latter rain; so the Lord shall make bright clouds, and give them showers of rain, to every one grass in the field."));

        // MALACHI
        valuesList.add(new ScriptureEntity("MAL 1:6", "A son honoureth his father, and a servant his master: if then I be a father, where is mine honour? and if I be a master, where is my fear? saith the Lord of hosts unto you, O priests, that despise my name. And ye say, Wherein have we despised thy name?"));
        valuesList.add(new ScriptureEntity("MAL 2:7", "For the priest's lips should keep knowledge, and they should seek the law at his mouth: for he is the messenger of the Lord of hosts"));
        valuesList.add(new ScriptureEntity("MAL 2:14", "Yet ye say, Wherefore? Because the Lord hath been witness between thee and the wife of thy youth, against whom thou hast dealt treacherously: yet is she thy companion, and the wife of thy covenant."));
        valuesList.add(new ScriptureEntity("MAL 3:8-10", "8 Will a man rob God? Yet ye have robbed me. But ye say, Wherein have we robbed thee? In tithes and offerings.\n" +
                "9 Ye are cursed with a curse: for ye have robbed me, even this whole nation.\n" +
                "10 Bring ye all the tithes into the storehouse, that there may be meat in mine house, and prove me now herewith, saith the Lord of hosts, if I will not open you the windows of heaven, and pour you out a blessing, that there shall not be room enough to receive it.\n"));
        valuesList.add(new ScriptureEntity("MAL 3:17", "And they shall be mine, saith the Lord of hosts, in that day when I make up my jewels; and I will spare them, as a man spareth his own son that serveth him."));
        valuesList.add(new ScriptureEntity("MAL 3:18", "Then shall ye return, and discern between the righteous and the wicked, between him that serveth God and him that serveth him not."));
        valuesList.add(new ScriptureEntity("MAL 4:2", "But unto you that fear my name shall the Sun of righteousness arise with healing in his wings; and ye shall go forth, and grow up as calves of the stall."));

        // MATTHEW
        valuesList.add(new ScriptureEntity("MAT 7:1-3", "1 Judge not, that ye be not judged.\n" +
                "\n" +
                "2 For with what judgment ye judge, ye shall be judged: and with what measure ye mete, it shall be measured to you again.\n" +
                "\n" +
                "3 And why beholdest thou the mote that is in thy brother's eye, but considerest not the beam that is in thine own eye?"));
        valuesList.add(new ScriptureEntity("MAT 6:33", "But seek ye first the kingdom of God, and his righteousness; and all these things shall be added unto you."));
        valuesList.add(new ScriptureEntity("MAT 7:13", "Enter ye in at the strait gate: for wide is the gate, and broad is the way, that leadeth to destruction, and many there be which go in thereat:"));
        valuesList.add(new ScriptureEntity("MAT 7:24-27", "24 Therefore whosoever heareth these sayings of mine, and doeth them, I will liken him unto a wise man, which built his house upon a rock:\n" +
                "25 And the rain descended, and the floods came, and the winds blew, and beat upon that house; and it fell not: for it was founded upon a rock.\n" +
                "26 And every one that heareth these sayings of mine, and doeth them not, shall be likened unto a foolish man, which built his house upon the sand:\n" +
                "27 And the rain descended, and the floods came, and the winds blew, and beat upon that house; and it fell: and great was the fall of it"));
        valuesList.add(new ScriptureEntity("MAT 8:17", "That it might be fulfilled which was spoken by Esaias the prophet, saying, Himself took our infirmities, and bare our sicknesses."));
        valuesList.add(new ScriptureEntity("MAT 12:34", "O generation of vipers, how can ye, being evil, speak good things? for out of the abundance of the heart the mouth speaketh."));
        valuesList.add(new ScriptureEntity("MAT 12:43-45", "43 When the unclean spirit is gone out of a man, he walketh through dry places, seeking rest, and findeth none.\n" +
                "44 Then he saith, I will return into my house from whence I came out; and when he is come, he findeth it empty, swept, and garnished.\n" +
                "45 Then goeth he, and taketh with himself seven other spirits more wicked than himself, and they enter in and dwell there: and the last state of that man is worse than the first. Even so shall it be also unto this wicked generation.\n"));
        valuesList.add(new ScriptureEntity("MAT 28:19-20", "19 Go ye therefore, and teach all nations, baptizing them in the name of the Father, and of the Son, and of the Holy Ghost:\n" +
                "20 Teaching them to observe all things whatsoever I have commanded you: and, lo, I am with you alway, even unto the end of the world. Amen."));

        // MARK
        valuesList.add(new ScriptureEntity("MAR 11:22-26", "22 And Jesus answering saith unto them, Have faith in God.\n" +
                "23 For verily I say unto you, That whosoever shall say unto this mountain, Be thou removed, and be thou cast into the sea; and shall not doubt in his heart, but shall believe that those things which he saith shall come to pass; he shall have whatsoever he saith.\n" +
                "24 Therefore I say unto you, What things soever ye desire, when ye pray, believe that ye receive them, and ye shall have them.\n" +
                "25 And when ye stand praying, forgive, if ye have ought against any: that your Father also which is in heaven may forgive you your trespasses.\n" +
                "26 But if ye do not forgive, neither will your Father which is in heaven forgive your trespasses.\n"));
        valuesList.add(new ScriptureEntity("MAR 16:15-18", "15 And he said unto them, Go ye into all the world, and preach the gospel to every creature.\n" +
                "16 He that believeth and is baptized shall be saved; but he that believeth not shall be damned.\n" +
                "17 And these signs shall follow them that believe; In my name shall they cast out devils; they shall speak with new tongues;\n" +
                "18 .They shall take up serpents; and if they drink any deadly thing, it shall not hurt them; they shall lay hands on the sick, and they shall recover."));

        // LUKE
        valuesList.add(new ScriptureEntity("LUK 4:18", "The Spirit of the Lord is upon me, because he hath anointed me to preach the gospel to the poor; he hath sent me to heal the brokenhearted, to preach deliverance to the captives, and recovering of sight to the blind, to set at liberty them that are bruised,"));
        valuesList.add(new ScriptureEntity("LUK 6:38", "Give, and it shall be given unto you; good measure, pressed down, and shaken together, and running over, shall men give into your bosom. For with the same measure that ye mete withal it shall be measured to you again."));
        valuesList.add(new ScriptureEntity("LUK 11:21-22", "21 When a strong man armed keepeth his palace, his goods are in peace:\n" +
                "22 But when a stronger than he shall come upon him, and overcome him, he taketh from him all his armour wherein he trusted, and divideth his spoils."));
        valuesList.add(new ScriptureEntity("LUK 12:15", "And he said unto them, Take heed, and beware of covetousness: for a man's life consisteth not in the abundance of the things which he possesseth."));
        valuesList.add(new ScriptureEntity("LUK 14:23", "And the lord said unto the servant, Go out into the highways and hedges, and compel them to come in, that my house may be filled."));
        valuesList.add(new ScriptureEntity("LUK 14:26-27", "26 If any man come to me, and hate not his father, and mother, and wife, and children, and brethren, and sisters, yea, and his own life also, he cannot be my disciple.\n" +
                "27 And whosoever doth not bear his cross, and come after me, cannot be my disciple."));
        valuesList.add(new ScriptureEntity("LUK 16:10-11", "10 He that is faithful in that which is least is faithful also in much: and he that is unjust in the least is unjust also in much.\n" +
                "11 If therefore ye have not been faithful in the unrighteous mammon, who will commit to your trust the true riches?"));
        valuesList.add(new ScriptureEntity("LUK 16:16", "The law and the prophets were until John: since that time the kingdom of God is preached, and every man presseth into it."));

        // JOHN
        valuesList.add(new ScriptureEntity("JOH 1:12", "But as many as received him, to them gave he power to become the sons of God, even to them that believe on his name:"));
        valuesList.add(new ScriptureEntity("JOH 3:6-7", "6 That which is born of the flesh is flesh; and that which is born of the Spirit is spirit.\n" +
                "7 Marvel not that I said unto thee, Ye must be born again."));
        valuesList.add(new ScriptureEntity("JOH 3:16-17", "16 For God so loved the world, that he gave his only begotten Son, that whosoever believeth in him should not perish, but have everlasting life.\n" +
                "17 For God sent not his Son into the world to condemn the world; but that the world through him might be saved."));
        valuesList.add(new ScriptureEntity("JOH 4:24", "God is a Spirit: and they that worship him must worship him in spirit and in truth.\n"));
        valuesList.add(new ScriptureEntity("JOH 6:63", "It is the spirit that quickeneth; the flesh profiteth nothing: the words that I speak unto you, they are spirit, and they are life."));
        valuesList.add(new ScriptureEntity("JOH 10:10", "The thief cometh not, but for to steal, and to kill, and to destroy: I am come that they might have life, and that they might have it more abundantly."));
        valuesList.add(new ScriptureEntity("JOH 14:1", "Let not your heart be troubled: ye believe in God, believe also in me."));
        valuesList.add(new ScriptureEntity("JOH 14:6", "Jesus saith unto him, I am the way, the truth, and the life: no man cometh unto the Father, but by me."));
        valuesList.add(new ScriptureEntity("JOH 14:11-12", "11 Believe me that I am in the Father, and the Father in me: or else believe me for the very works' sake.\n" +
                "12 Verily, verily, I say unto you, He that believeth on me, the works that I do shall he do also; and greater works than these shall he do; because I go unto my Father."));
        valuesList.add(new ScriptureEntity("JOH 14:21", "He that hath my commandments, and keepeth them, he it is that loveth me: and he that loveth me shall be loved of my Father, and I will love him, and will manifest myself to him."));
        valuesList.add(new ScriptureEntity("JOH 15:7", "If ye abide in me, and my words abide in you, ye shall ask what ye will, and it shall be done unto you."));
        valuesList.add(new ScriptureEntity("JOH 15:16", "Ye have not chosen me, but I have chosen you, and ordained you, that ye should go and bring forth fruit, and that your fruit should remain: that whatsoever ye shall ask of the Father in my name, he may give it you."));
        valuesList.add(new ScriptureEntity("JOH 16:7-9", "7 Nevertheless I tell you the truth; It is expedient for you that I go away: for if I go not away, the Comforter will not come unto you; but if I depart, I will send him unto you.\n" +
                "8 And when he is come, he will reprove the world of sin, and of righteousness, and of judgment:\n" +
                "9 Of sin, because they believe not on me;"));
        valuesList.add(new ScriptureEntity("JOH 16:23-24", "23 And in that day ye shall ask me nothing. Verily, verily, I say unto you, Whatsoever ye shall ask the Father in my name, he will give it you.\n" +
                "24 Hitherto have ye asked nothing in my name: ask, and ye shall receive, that your joy may be full."));
        valuesList.add(new ScriptureEntity("JOH 17:17", "Sanctify them through thy truth: thy word is truth."));
        valuesList.add(new ScriptureEntity("JOH 18:9", "That the saying might be fulfilled, which he spake, Of them which thou gavest me have I lost none."));

        // ACTS
        valuesList.add(new ScriptureEntity("ACT 1:8", "But ye shall receive power, after that the Holy Ghost is come upon you: and ye shall be witnesses unto me both in Jerusalem, and in all Judaea, and in Samaria, and unto the uttermost part of the earth."));
        valuesList.add(new ScriptureEntity("ACT 2:4", "And they were all filled with the Holy Ghost, and began to speak with other tongues, as the Spirit gave them utterance."));
        valuesList.add(new ScriptureEntity("ACT 2:41-42", "41 Then they that gladly received his word were baptized: and the same day there were added unto them about three thousand souls.\n" +
                "42 And they continued stedfastly in the apostles' doctrine and fellowship, and in breaking of bread, and in prayers."));
        valuesList.add(new ScriptureEntity("ACT 4:12", "Neither is there salvation in any other: for there is none other name under heaven given among men, whereby we must be saved."));
        valuesList.add(new ScriptureEntity("ACT 4:4", "Howbeit many of them which heard the word believed; and the number of the men was about five thousand."));
        valuesList.add(new ScriptureEntity("ACT 5:14", "And believers were the more added to the Lord, multitudes both of men and women.)"));
        valuesList.add(new ScriptureEntity("ACT 6:4", "But we will give ourselves continually to prayer, and to the ministry of the word.\n"));
        valuesList.add(new ScriptureEntity("ACT 10:38", "How God anointed Jesus of Nazareth with the Holy Ghost and with power: who went about doing good, and healing all that were oppressed of the devil; for God was with him."));
        valuesList.add(new ScriptureEntity("ACT 13:44", "And the next sabbath day came almost the whole city together to hear the word of God."));
        valuesList.add(new ScriptureEntity("ACT 20:28", "Take heed therefore unto yourselves, and to all the flock, over the which the Holy Ghost hath made you overseers, to feed the church of God, which he hath purchased with his own blood"));

        // ROMANS There is therefore now no condemnation to them which are in Christ Jesus, who walk not after the flesh, but after the Spirit.
        valuesList.add(new ScriptureEntity("ROM 1:16", "For I am not ashamed of the gospel of Christ: for it is the power of God unto salvation to every one that believeth; to the Jew first, and also to the Greek."));
        valuesList.add(new ScriptureEntity("ROM 3:23", "For all have sinned, and come short of the glory of God;"));
        valuesList.add(new ScriptureEntity("ROM 6:23", "For the wages of sin is death; but the gift of God is eternal life through Jesus Christ our Lord."));
        valuesList.add(new ScriptureEntity("ROM 8:1", "There is therefore now no condemnation to them which are in Christ Jesus, who walk not after the flesh, but after the Spirit."));
        valuesList.add(new ScriptureEntity("ROM 8:6", "For to be carnally minded is death; but to be spiritually minded is life and peace."));
        valuesList.add(new ScriptureEntity("ROM 8:14", "For as many as are led by the Spirit of God, they are the sons of God."));
        valuesList.add(new ScriptureEntity("ROM 8:26", "Likewise the Spirit also helpeth our infirmities: for we know not what we should pray for as we ought: but the Spirit itself maketh intercession for us with groanings which cannot be uttered."));
        valuesList.add(new ScriptureEntity("ROM 8:28", "And we know that all things work together for good to them that love God, to them who are the called according to his purpose."));
        valuesList.add(new ScriptureEntity("ROM 10:17", "So then faith cometh by hearing, and hearing by the word of God."));
        valuesList.add(new ScriptureEntity("ROM 10:9-10", "9 That if thou shalt confess with thy mouth the Lord Jesus, and shalt believe in thine heart that God hath raised him from the dead, thou shalt be saved.\n" +
                "10 For with the heart man believeth unto righteousness; and with the mouth confession is made unto salvation."));
        valuesList.add(new ScriptureEntity("ROM 11:29", "For the gifts and calling of God are without repentance."));
        valuesList.add(new ScriptureEntity("ROM 12:1-2", "1 I beseech you therefore, brethren, by the mercies of God, that ye present your bodies a living sacrifice, holy, acceptable unto God, which is your reasonable service.\n" +
                "2 And be not conformed to this world: but be ye transformed by the renewing of your mind, that ye may prove what is that good, and acceptable, and perfect, will of God."));
        valuesList.add(new ScriptureEntity("ROM 13:8", "Owe no man any thing, but to love one another: for he that loveth another hath fulfilled the law."));
        valuesList.add(new ScriptureEntity("ROM 14:17", "For the kingdom of God is not meat and drink; but righteousness, and peace, and joy in the Holy Ghost."));

        // 1CORINTHIANS
        valuesList.add(new ScriptureEntity("1COR 3:1", "And I, brethren, could not speak unto you as unto spiritual, but as unto carnal, even as unto babes in Christ."));
        valuesList.add(new ScriptureEntity("1COR 3:16", "Know ye not that ye are the temple of God, and that the Spirit of God dwelleth in you?"));
        valuesList.add(new ScriptureEntity("1COR 6:12", "All things are lawful unto me, but all things are not expedient: all things are lawful for me, but I will not be brought under the power of any."));
        valuesList.add(new ScriptureEntity("1COR 7:28", "But and if thou marry, thou hast not sinned; and if a virgin marry, she hath not sinned. Nevertheless such shall have trouble in the flesh: but I spare you."));
        valuesList.add(new ScriptureEntity("1COR 9:7", "Who goeth a warfare any time at his own charges? who planteth a vineyard, and eateth not of the fruit thereof? or who feedeth a flock, and eateth not of the milk of the flock?"));
        valuesList.add(new ScriptureEntity("1COR 10:12", "Wherefore let him that thinketh he standeth take heed lest he fall."));
        valuesList.add(new ScriptureEntity("1COR 10:13", "There hath no temptation taken you but such as is common to man: but God is faithful, who will not suffer you to be tempted above that ye are able; but will with the temptation also make a way to escape, that ye may be able to bear it."));
        valuesList.add(new ScriptureEntity("1COR 10:23", "All things are lawful for me, but all things are not expedient: all things are lawful for me, but all things edify not."));
        valuesList.add(new ScriptureEntity("1COR 11:31", "For if we would judge ourselves, we should not be judged."));
        valuesList.add(new ScriptureEntity("1COR 12:7", "But the manifestation of the Spirit is given to every man to profit withal."));
        valuesList.add(new ScriptureEntity("1COR 13:4-7", "4 Charity suffereth long, and is kind; charity envieth not; charity vaunteth not itself, is not puffed up,\n" +
                "5 Doth not behave itself unseemly, seeketh not her own, is not easily provoked, thinketh no evil;\n" +
                "6 Rejoiceth not in iniquity, but rejoiceth in the truth;\n" +
                "7 Beareth all things, believeth all things, hopeth all things, endureth all things.\n"));
        valuesList.add(new ScriptureEntity("1COR 14:1-2", "1 Follow after charity, and desire spiritual gifts, but rather that ye may prophesy.\n" +
                "2 For he that speaketh in an unknown tongue speaketh not unto men, but unto God: for no man understandeth him; howbeit in the spirit he speaketh mysteries."));
        valuesList.add(new ScriptureEntity("1COR 14:3-4", "3 But he that prophesieth speaketh unto men to edification, and exhortation, and comfort.\n" +
                "4 He that speaketh in an unknown tongue edifieth himself; but he that prophesieth edifieth the church."));
        valuesList.add(new ScriptureEntity("1COR 14:10", "There are, it may be, so many kinds of voices in the world, and none of them is without signification"));
        valuesList.add(new ScriptureEntity("1COR 14:14-15", "14 For if I pray in an unknown tongue, my spirit prayeth, but my understanding is unfruitful.\n" +
                "15 What is it then? I will pray with the spirit, and I will pray with the understanding also: I will sing with the spirit, and I will sing with the understanding also."));
        valuesList.add(new ScriptureEntity("1COR 15:33", "Be not deceived: evil communications corrupt good manners."));
        valuesList.add(new ScriptureEntity("1COR 15:58", "Therefore, my beloved brethren, be ye stedfast, unmoveable, always abounding in the work of the Lord, forasmuch as ye know that your labour is not in vain in the Lord."));

        // 2CORINTHIANS
        valuesList.add(new ScriptureEntity("2COR 2:14", "Now thanks be unto God, which always causeth us to triumph in Christ, and maketh manifest the savour of his knowledge by us in every place."));
        valuesList.add(new ScriptureEntity("2COR 3:17", "Now the Lord is that Spirit: and where the Spirit of the Lord is, there is liberty."));
        valuesList.add(new ScriptureEntity("2COR 4:4", "In whom the god of this world hath blinded the minds of them which believe not, lest the light of the glorious gospel of Christ, who is the image of God, should shine unto them."));
        valuesList.add(new ScriptureEntity("2COR 4:13", "We having the same spirit of faith, according as it is written, I believed, and therefore have I spoken; we also believe, and therefore speak;"));
        valuesList.add(new ScriptureEntity("2COR 4:16-17", "16 For which cause we faint not; but though our outward man perish, yet the inward man is renewed day by day.\n" +
                "17 For our light affliction, which is but for a moment, worketh for us a far more exceeding and eternal weight of glory;"));
        valuesList.add(new ScriptureEntity("2COR 4:18", "While we look not at the things which are seen, but at the things which are not seen: for the things which are seen are temporal; but the things which are not seen are eternal"));
        valuesList.add(new ScriptureEntity("2COR 5:7", "(For we walk by faith, not by sight:)"));
        valuesList.add(new ScriptureEntity("2COR 5:10-11", "10 For we must all appear before the judgment seat of Christ; that every one may receive the things done in his body, according to that he hath done, whether it be good or bad.\n" +
                "11 Knowing therefore the terror of the Lord, we persuade men; but we are made manifest unto God; and I trust also are made manifest in your consciences.\n"));
        valuesList.add(new ScriptureEntity("2COR 5:14-15", "14 For the love of Christ constraineth us; because we thus judge, that if one died for all, then were all dead:\n" +
                "15 And that he died for all, that they which live should not henceforth live unto themselves, but unto him which died for them, and rose again."));
        valuesList.add(new ScriptureEntity("2COR 5:17", "Therefore if any man be in Christ, he is a new creature: old things are passed away; behold, all things are become new."));
        valuesList.add(new ScriptureEntity("2COR 6:14-15", "14 Be ye not unequally yoked together with unbelievers: for what fellowship hath righteousness with unrighteousness? and what communion hath light with darkness?\n" +
                "15 And what concord hath Christ with Belial? or what part hath he that believeth with an infidel?\n"));
        valuesList.add(new ScriptureEntity("2COR 8:9", "For ye know the grace of our Lord Jesus Christ, that, though he was rich, yet for your sakes he became poor, that ye through his poverty might be rich."));
        valuesList.add(new ScriptureEntity("2COR 9:6-7", "6 But this I say, He which soweth sparingly shall reap also sparingly; and he which soweth bountifully shall reap also bountifully.\n" +
                "7 Every man according as he purposeth in his heart, so let him give; not grudgingly, or of necessity: for God loveth a cheerful giver"));
        valuesList.add(new ScriptureEntity("2COR 9:8", "And God is able to make all grace abound toward you; that ye, always having all sufficiency in all things, may abound to every good work:"));
        valuesList.add(new ScriptureEntity("2COR 10:12", "For we dare not make ourselves of the number, or compare ourselves with some that commend themselves: but they measuring themselves by themselves, and comparing themselves among themselves, are not wise."));
        valuesList.add(new ScriptureEntity("2COR 12:9-10", "9 And he said unto me, My grace is sufficient for thee: for my strength is made perfect in weakness. Most gladly therefore will I rather glory in my infirmities, that the power of Christ may rest upon me.\n" +
                "10 Therefore I take pleasure in infirmities, in reproaches, in necessities, in persecutions, in distresses for Christ's sake: for when I am weak, then am I strong.\n"));
        valuesList.add(new ScriptureEntity("2COR 13:5", "Examine yourselves, whether ye be in the faith; prove your own selves. Know ye not your own selves, how that Jesus Christ is in you, except ye be reprobates?"));

        // GALATIANS
        valuesList.add(new ScriptureEntity("GAL 1:10", "For do I now persuade men, or God? or do I seek to please men? for if I yet pleased men, I should not be the servant of Christ."));
        valuesList.add(new ScriptureEntity("GAL 2:18", "For if I build again the things which I destroyed, I make myself a transgressor"));
        valuesList.add(new ScriptureEntity("GAL 2:20", "I am crucified with Christ: nevertheless I live; yet not I, but Christ liveth in me: and the life which I now live in the flesh I live by the faith of the Son of God, who loved me, and gave himself for me."));
        valuesList.add(new ScriptureEntity("GAL 3:13-14", "13 Christ hath redeemed us from the curse of the law, being made a curse for us: for it is written, Cursed is every one that hangeth on a tree:\n" +
                "14 That the blessing of Abraham might come on the Gentiles through Jesus Christ; that we might receive the promise of the Spirit through faith."));
        valuesList.add(new ScriptureEntity("GAL 3:28", "There is neither Jew nor Greek, there is neither bond nor free, there is neither male nor female: for ye are all one in Christ Jesus."));
        valuesList.add(new ScriptureEntity("GAL 4:16", "Am I therefore become your enemy, because I tell you the truth?"));
        valuesList.add(new ScriptureEntity("GAL 4:18", "But it is good to be zealously affected always in a good thing, and not only when I am present with you."));
        valuesList.add(new ScriptureEntity("GAL 4:19", "My little children, of whom I travail in birth again until Christ be formed in you,"));
        valuesList.add(new ScriptureEntity("GAL 4:28", "Now we, brethren, as Isaac was, are the children of promise"));
        valuesList.add(new ScriptureEntity("GAL 5:1", "Stand fast therefore in the liberty wherewith Christ hath made us free, and be not entangled again with the yoke of bondage."));
        valuesList.add(new ScriptureEntity("GAL 5:19-21", "19 Now the works of the flesh are manifest, which are these; Adultery, fornication, uncleanness, lasciviousness,\n" +
                "20 Idolatry, witchcraft, hatred, variance, emulations, wrath, strife, seditions, heresies,\n" +
                "21 Envyings, murders, drunkenness, revellings, and such like: of the which I tell you before, as I have also told you in time past, that they which do such things shall not inherit the kingdom of God.\n"));
        valuesList.add(new ScriptureEntity("GAL 5:22-23", "22 But the fruit of the Spirit is love, joy, peace, longsuffering, gentleness, goodness, faith,\n" +
                "23 Meekness, temperance: against such there is no law."));
        valuesList.add(new ScriptureEntity("GAL 5:9", "A little leaven leaveneth the whole lump."));
        valuesList.add(new ScriptureEntity("GAL 6:1", "Brethren, if a man be overtaken in a fault, ye which are spiritual, restore such an one in the spirit of meekness; considering thyself, lest thou also be tempted."));
        valuesList.add(new ScriptureEntity("GAL 6:6", "6 Let him that is taught in the word communicate unto him that teacheth in all good things."));
        valuesList.add(new ScriptureEntity("GAL 6:7", "Be not deceived; God is not mocked: for whatsoever a man soweth, that shall he also reap."));
        valuesList.add(new ScriptureEntity("GAL 6:8", "For he that soweth to his flesh shall of the flesh reap corruption; but he that soweth to the Spirit shall of the Spirit reap life everlasting."));
        valuesList.add(new ScriptureEntity("GAL 6:9-10", "9 And let us not be weary in well doing: for in due season we shall reap, if we faint not.\n" +
                "10 As we have therefore opportunity, let us do good unto all men, especially unto them who are of the household of faith.\n"));

        // EPHESIANS
        valuesList.add(new ScriptureEntity("EPH 1:17", "That the God of our Lord Jesus Christ, the Father of glory, may give unto you the spirit of wisdom and revelation in the knowledge of him:"));
        valuesList.add(new ScriptureEntity("EPH 2:1-2", "1 And you hath he quickened, who were dead in trespasses and sins;\n" +
                "2 Wherein in time past ye walked according to the course of this world, according to the prince of the power of the air, the spirit that now worketh in the children of disobedience:"));
        valuesList.add(new ScriptureEntity("EPH 2:10", "For we are his workmanship, created in Christ Jesus unto good works, which God hath before ordained that we should walk in them"));
        valuesList.add(new ScriptureEntity("EPH 3:20-21", "20 Now unto him that is able to do exceeding abundantly above all that we ask or think, according to the power that worketh in us,\n" +
                "21 Unto him be glory in the church by Christ Jesus throughout all ages, world without end. Amen."));
        valuesList.add(new ScriptureEntity("EPH 4:11-12", "11 And he gave some, apostles; and some, prophets; and some, evangelists; and some, pastors and teachers;\n" +
                "12 For the perfecting of the saints, for the work of the ministry, for the edifying of the body of Christ:"));
        valuesList.add(new ScriptureEntity("EPH 4:14", "That we henceforth be no more children, tossed to and fro, and carried about with every wind of doctrine, by the sleight of men, and cunning craftiness, whereby they lie in wait to deceive;"));
        valuesList.add(new ScriptureEntity("EPH 4:26", "Be ye angry, and sin not: let not the sun go down upon your wrath:"));
        valuesList.add(new ScriptureEntity("EPH 4:27", "Neither give place to the devil."));
        valuesList.add(new ScriptureEntity("EPH 4:28", "Let him that stole steal no more: but rather let him labour, working with his hands the thing which is good, that he may have to give to him that needeth.\n"));
        valuesList.add(new ScriptureEntity("EPH 4:29", "Let no corrupt communication proceed out of your mouth, but that which is good to the use of edifying, that it may minister grace unto the hearers."));
        valuesList.add(new ScriptureEntity("EPH 4:30", "And grieve not the holy Spirit of God, whereby ye are sealed unto the day of redemption."));
        valuesList.add(new ScriptureEntity("EPH 5:1-2", "1 Be ye therefore followers of God, as dear children;\n" +
                "2 And walk in love, as Christ also hath loved us, and hath given himself for us an offering and a sacrifice to God for a sweetsmelling savour."));
        valuesList.add(new ScriptureEntity("EPH 5:3", "But fornication, and all uncleanness, or covetousness, let it not be once named among you, as becometh saints;"));
        valuesList.add(new ScriptureEntity("EPH 5:16", "Redeeming the time, because the days are evil."));
        valuesList.add(new ScriptureEntity("EPH 5:18-19", "18 And be not drunk with wine, wherein is excess; but be filled with the Spirit;\n" +
                "19 Speaking to yourselves in psalms and hymns and spiritual songs, singing and making melody in your heart to the Lord;\n"));
        valuesList.add(new ScriptureEntity("EPH 5:22", "Wives, submit yourselves unto your own husbands, as unto the Lord."));
        valuesList.add(new ScriptureEntity("EPH 5:31-32", "31 For this cause shall a man leave his father and mother, and shall be joined unto his wife, and they two shall be one flesh.\n" +
                "32 This is a great mystery: but I speak concerning Christ and the church."));
        valuesList.add(new ScriptureEntity("EPH 6:2-3", "2 Honour thy father and mother; (which is the first commandment with promise;)\n" +
                "3 That it may be well with thee, and thou mayest live long on the earth."));
        valuesList.add(new ScriptureEntity("EPH 6:10", "Finally, my brethren, be strong in the Lord, and in the power of his might."));
        valuesList.add(new ScriptureEntity("EPH 6:11", "Put on the whole armour of God, that ye may be able to stand against the wiles of the devil."));
        valuesList.add(new ScriptureEntity("EPH 6:12", "For we wrestle not against flesh and blood, but against principalities, against powers, against the rulers of the darkness of this world, against spiritual wickedness in high places."));
        valuesList.add(new ScriptureEntity("EPH 6:13", "Wherefore take unto you the whole armour of God, that ye may be able to withstand in the evil day, and having done all, to stand."));
        valuesList.add(new ScriptureEntity("EPH 6:18", "Praying always with all prayer and supplication in the Spirit, and watching thereunto with all perseverance and supplication for all saints;"));

        // PHILIPPIANS
        valuesList.add(new ScriptureEntity("PHILI 1:21", "For to me to live is Christ, and to die is gain."));
        valuesList.add(new ScriptureEntity("PHILI 1:28:29", "28 And in nothing terrified by your adversaries: which is to them an evident token of perdition, but to you of salvation, and that of God.\n" +
                "29 For unto you it is given in the behalf of Christ, not only to believe on him, but also to suffer for his sake;"));
        valuesList.add(new ScriptureEntity("PHILI 2:3", "Let nothing be done through strife or vainglory; but in lowliness of mind let each esteem other better than themselves. "));
        valuesList.add(new ScriptureEntity("PHILI 2:10", "That at the name of Jesus every knee should bow, of things in heaven, and things in earth, and things under the earth;"));
        valuesList.add(new ScriptureEntity("PHILI 2:12", "Wherefore, my beloved, as ye have always obeyed, not as in my presence only, but now much more in my absence, work out your own salvation with fear and trembling."));
        valuesList.add(new ScriptureEntity("PHILI 2:14", "Do all things without murmurings and disputings:"));
        valuesList.add(new ScriptureEntity("PHILI 2:20-21", "20 For I have no man likeminded, who will naturally care for your state.\n" +
                "21 For all seek their own, not the things which are Jesus Christ's."));
        valuesList.add(new ScriptureEntity("PHILI 3:10", "That I may know him, and the power of his resurrection, and the fellowship of his sufferings, being made conformable unto his death;"));
        valuesList.add(new ScriptureEntity("PHILI 4:6", "Be careful for nothing; but in every thing by prayer and supplication with thanksgiving let your requests be made known unto God."));
        valuesList.add(new ScriptureEntity("PHILI 4:8", "Finally, brethren, whatsoever things are true, whatsoever things are honest, whatsoever things are just, whatsoever things are pure, whatsoever things are lovely, whatsoever things are of good report; if there be any virtue, and if there be any praise, think on these things."));
        valuesList.add(new ScriptureEntity("PHILI 4:13", "I can do all things through Christ which strengtheneth me."));
        valuesList.add(new ScriptureEntity("PHILI 4:19", "But my God shall supply all your need according to his riches in glory by Christ Jesus."));

        // COLOSSIANS
        valuesList.add(new ScriptureEntity("COL 1:9", "For this cause we also, since the day we heard it, do not cease to pray for you, and to desire that ye might be filled with the knowledge of his will in all wisdom and spiritual understanding;"));
        valuesList.add(new ScriptureEntity("COL 1:10", "That ye might walk worthy of the Lord unto all pleasing, being fruitful in every good work, and increasing in the knowledge of God;"));
        valuesList.add(new ScriptureEntity("COL 1:13", "Who hath delivered us from the power of darkness, and hath translated us into the kingdom of his dear Son:"));
        valuesList.add(new ScriptureEntity("COL 3:16", "Let the word of Christ dwell in you richly in all wisdom; teaching and admonishing one another in psalms and hymns and spiritual songs, singing with grace in your hearts to the Lord."));

        // 1THESSALONIANS
        valuesList.add(new ScriptureEntity("1THE 3:3", "That no man should be moved by these afflictions: for yourselves know that we are appointed thereunto."));
        valuesList.add(new ScriptureEntity("1THE 4:3-4", "3 For this is the will of God, even your sanctification, that ye should abstain from fornication:\n" +
                "4 That every one of you should know how to possess his vessel in sanctification and honour;"));
        valuesList.add(new ScriptureEntity("1THE 4:16-18", "16 For the Lord himself shall descend from heaven with a shout, with the voice of the archangel, and with the trump of God: and the dead in Christ shall rise first:\n" +
                "17 Then we which are alive and remain shall be caught up together with them in the clouds, to meet the Lord in the air: and so shall we ever be with the Lord.\n" +
                "18 Wherefore comfort one another with these words.\n"));
        valuesList.add(new ScriptureEntity("1THE 5:2-3", "2 For yourselves know perfectly that the day of the Lord so cometh as a thief in the night.\n" +
                "3 For when they shall say, Peace and safety; then sudden destruction cometh upon them, as travail upon a woman with child; and they shall not escape."));
        valuesList.add(new ScriptureEntity("1THE 5:16", "Rejoice evermore."));
        valuesList.add(new ScriptureEntity("1THE 5:17", "Pray without ceasing."));
        valuesList.add(new ScriptureEntity("1THE 5:18", "In every thing give thanks: for this is the will of God in Christ Jesus concerning you."));
        valuesList.add(new ScriptureEntity("1THE 5:19-20", "19 Quench not the Spirit.\n" +
                "20 Despise not prophesyings."));
        valuesList.add(new ScriptureEntity("1THE 5:22", "Abstain from all appearance of evil."));
        valuesList.add(new ScriptureEntity("1THE 5:23", "And the very God of peace sanctify you wholly; and I pray God your whole spirit and soul and body be preserved blameless unto the coming of our Lord Jesus Christ."));
        valuesList.add(new ScriptureEntity("1THE 5:24", "Faithful is he that calleth you, who also will do it."));

        // 2THESSALIONS
        valuesList.add(new ScriptureEntity("2THE 2:11-12", "11 And for this cause God shall send them strong delusion, that they should believe a lie:\n" +
                "12 That they all might be damned who believed not the truth, but had pleasure in unrighteousness."));
        valuesList.add(new ScriptureEntity("2THE 3:1", "Finally, brethren, pray for us, that the word of the Lord may have free course, and be glorified, even as it is with you:"));
        valuesList.add(new ScriptureEntity("2THE 3:5", "And the Lord direct your hearts into the love of God, and into the patient waiting for Christ."));

        // 1TIMOTHY
        valuesList.add(new ScriptureEntity("1TIM 1:19-20", "19 Holding faith, and a good conscience; which some having put away concerning faith have made shipwreck:\n" +
                "20 Of whom is Hymenaeus and Alexander; whom I have delivered unto Satan, that they may learn not to blaspheme."));
        valuesList.add(new ScriptureEntity("1TIM 2:1-2", "1 I exhort therefore, that, first of all, supplications, prayers, intercessions, and giving of thanks, be made for all men;\n" +
                "2 For kings, and for all that are in authority; that we may lead a quiet and peaceable life in all godliness and honesty."));
        valuesList.add(new ScriptureEntity("1TIM 2:5", "For there is one God, and one mediator between God and men, the man Christ Jesus;"));
        valuesList.add(new ScriptureEntity("1TIM 3:1", "This is a true saying, If a man desire the office of a bishop, he desireth a good work."));
        valuesList.add(new ScriptureEntity("1TIM 3:2", "A bishop then must be blameless, the husband of one wife, vigilant, sober, of good behaviour, given to hospitality, apt to teach;"));
        valuesList.add(new ScriptureEntity("1TIM 3:7", "Moreover he must have a good report of them which are without; lest he fall into reproach and the snare of the devil."));
        valuesList.add(new ScriptureEntity("1TIM 4:14-15", "14 Neglect not the gift that is in thee, which was given thee by prophecy, with the laying on of the hands of the presbytery.\n" +
                "15 Meditate upon these things; give thyself wholly to them; that thy profiting may appear to all."));
        valuesList.add(new ScriptureEntity("1TIM 4:16", "Take heed unto thyself, and unto the doctrine; continue in them: for in doing this thou shalt both save thyself, and them that hear thee."));
        valuesList.add(new ScriptureEntity("1TIM 5:14", "I will therefore that the younger women marry, bear children, guide the house, give none occasion to the adversary to speak reproachfully."));
        valuesList.add(new ScriptureEntity("1TIM 6:6-8", "6 But godliness with contentment is great gain.\n" +
                "7 For we brought nothing into this world, and it is certain we can carry nothing out.\n" +
                "8 And having food and raiment let us be therewith content."));
        valuesList.add(new ScriptureEntity("1TIM 6:10", "For the love of money is the root of all evil: which while some coveted after, they have erred from the faith, and pierced themselves through with many sorrows."));
        valuesList.add(new ScriptureEntity("1TIM 6:12", "Fight the good fight of faith, lay hold on eternal life, whereunto thou art also called, and hast professed a good profession before many witnesses."));

        // 2TIMOTHY
        valuesList.add(new ScriptureEntity("2TIM 2:1-2", "Thou therefore, my son, be strong in the grace that is in Christ Jesus.\n" +
                "2 And the things that thou hast heard of me among many witnesses, the same commit thou to faithful men, who shall be able to teach others also."));
        valuesList.add(new ScriptureEntity("2TIM 2:3-4", "3 Thou therefore endure hardness, as a good soldier of Jesus Christ.\n" +
                "4 No man that warreth entangleth himself with the affairs of this life; that he may please him who hath chosen him to be a soldier."));
        valuesList.add(new ScriptureEntity("2TIM 2:15", "Study to shew thyself approved unto God, a workman that needeth not to be ashamed, rightly dividing the word of truth."));
        valuesList.add(new ScriptureEntity("2TIM 2:16-17", "16 But shun profane and vain babblings: for they will increase unto more ungodliness.\n" +
                "17 And their word will eat as doth a canker: of whom is Hymenaeus and Philetus;"));
        valuesList.add(new ScriptureEntity("2TIM 2:19", "Nevertheless the foundation of God standeth sure, having this seal, The Lord knoweth them that are his. And, Let every one that nameth the name of Christ depart from iniquity."));
        valuesList.add(new ScriptureEntity("2TIM 2:20", "But in a great house there are not only vessels of gold and of silver, but also of wood and of earth; and some to honour, and some to dishonour."));
        valuesList.add(new ScriptureEntity("2TIM 2:23", "But foolish and unlearned questions avoid, knowing that they do gender strifes."));
        valuesList.add(new ScriptureEntity("2TIM 4:10", "For Demas hath forsaken me, having loved this present world, and is departed unto Thessalonica; Crescens to Galatia, Titus unto Dalmatia."));

        // TITUS
        valuesList.add(new ScriptureEntity("TIT 2:11-12", "11 For the grace of God that bringeth salvation hath appeared to all men,\n" +
                "12 Teaching us that, denying ungodliness and worldly lusts, we should live soberly, righteously, and godly, in this present world;"));
        valuesList.add(new ScriptureEntity("TIT 2:13-14", "13 Looking for that blessed hope, and the glorious appearing of the great God and our Saviour Jesus Christ;\n" +
                "14 Who gave himself for us, that he might redeem us from all iniquity, and purify unto himself a peculiar people, zealous of good works."));

        // PHILEMON
        valuesList.add(new ScriptureEntity("PHILE 6", "That the communication of thy faith may become effectual by the acknowledging of every good thing which is in you in Christ Jesus"));
        valuesList.add(new ScriptureEntity("PHILE 19", "I Paul have written it with mine own hand, I will repay it: albeit I do not say to thee how thou owest unto me even thine own self besides."));

        // HEBREWS
        valuesList.add(new ScriptureEntity("HEB 2:3", "How shall we escape, if we neglect so great salvation; which at the first began to be spoken by the Lord, and was confirmed unto us by them that heard him;"));
        valuesList.add(new ScriptureEntity("HEB 4:2", "For unto us was the gospel preached, as well as unto them: but the word preached did not profit them, not being mixed with faith in them that heard it."));
        valuesList.add(new ScriptureEntity("HEB 4:16", "Let us therefore come boldly unto the throne of grace, that we may obtain mercy, and find grace to help in time of need."));
        valuesList.add(new ScriptureEntity("HEB 5:7", "Who in the days of his flesh, when he had offered up prayers and supplications with strong crying and tears unto him that was able to save him from death, and was heard in that he feared;"));
        valuesList.add(new ScriptureEntity("HEB 5:12-14", "12 For when for the time ye ought to be teachers, ye have need that one teach you again which be the first principles of the oracles of God; and are become such as have need of milk, and not of strong meat.\n" +
                "13 For every one that useth milk is unskilful in the word of righteousness: for he is a babe.\n" +
                "14 But strong meat belongeth to them that are of full age, even those who by reason of use have their senses exercised to discern both good and evil."));
        valuesList.add(new ScriptureEntity("HEB 6:1-2", "1 Therefore leaving the principles of the doctrine of Christ, let us go on unto perfection; not laying again the foundation of repentance from dead works, and of faith toward God,\n" +
                "2 Of the doctrine of baptisms, and of laying on of hands, and of resurrection of the dead, and of eternal judgment."));
        valuesList.add(new ScriptureEntity("HEB 6:12", "That ye be not slothful, but followers of them who through faith and patience inherit the promises"));
        valuesList.add(new ScriptureEntity("HEB 9:22", "And almost all things are by the law purged with blood; and without shedding of blood is no remission"));
        valuesList.add(new ScriptureEntity("HEB 9:27", "And as it is appointed unto men once to die, but after this the judgment:"));
        valuesList.add(new ScriptureEntity("HEB 10:25", "Not forsaking the assembling of ourselves together, as the manner of some is; but exhorting one another: and so much the more, as ye see the day approaching."));
        valuesList.add(new ScriptureEntity("HEB 11:1", "Now faith is the substance of things hoped for, the evidence of things not seen."));
        valuesList.add(new ScriptureEntity("HEB 11:6", "But without faith it is impossible to please him: for he that cometh to God must believe that he is, and that he is a rewarder of them that diligently seek him."));
        valuesList.add(new ScriptureEntity("HEB 12:1-2", "1 Wherefore seeing we also are compassed about with so great a cloud of witnesses, let us lay aside every weight, and the sin which doth so easily beset us, and let us run with patience the race that is set before us,\n" +
                "2 Looking unto Jesus the author and finisher of our faith; who for the joy that was set before him endured the cross, despising the shame, and is set down at the right hand of the throne of God.\n" +
                "\n"));
        valuesList.add(new ScriptureEntity("HEB 12:6", "For whom the Lord loveth he chasteneth, and scourgeth every son whom he receiveth."));
        valuesList.add(new ScriptureEntity("HEB 12:14-15", "14 Follow peace with all men, and holiness, without which no man shall see the Lord:\n" +
                "15 Looking diligently lest any man fail of the grace of God; lest any root of bitterness springing up trouble you, and thereby many be defiled;"));
        valuesList.add(new ScriptureEntity("HEB 13:1", "Let brotherly love continue."));
        valuesList.add(new ScriptureEntity("HEB 13:4-5", "4 Marriage is honourable in all, and the bed undefiled: but whoremongers and adulterers God will judge.\n" +
                "5 Let your conversation be without covetousness; and be content with such things as ye have: for he hath said, I will never leave thee, nor forsake thee."));
        valuesList.add(new ScriptureEntity("HEB 13:8", "Jesus Christ the same yesterday, and to day, and for ever."));
        valuesList.add(new ScriptureEntity("HEB 13:17", "Obey them that have the rule over you, and submit yourselves: for they watch for your souls, as they that must give account, that they may do it with joy, and not with grief: for that is unprofitable for you."));

        // JAMES
        valuesList.add(new ScriptureEntity("JAM 1:5", "If any of you lack wisdom, let him ask of God, that giveth to all men liberally, and upbraideth not; and it shall be given him."));
        valuesList.add(new ScriptureEntity("JAM 1:8", "A double minded man is unstable in all his ways"));
        valuesList.add(new ScriptureEntity("JAM 1:13-14", "13 Let no man say when he is tempted, I am tempted of God: for God cannot be tempted with evil, neither tempteth he any man:\n" +
                "14 But every man is tempted, when he is drawn away of his own lust, and enticed."));
        valuesList.add(new ScriptureEntity("JAM 1:17", "Every good gift and every perfect gift is from above, and cometh down from the Father of lights, with whom is no variableness, neither shadow of turning."));
        valuesList.add(new ScriptureEntity("JAM 2:10", "For whosoever shall keep the whole law, and yet offend in one point, he is guilty of all."));
        valuesList.add(new ScriptureEntity("JAM 2:17", "Even so faith, if it hath not works, is dead, being alone."));
        valuesList.add(new ScriptureEntity("JAM 3:17", "But the wisdom that is from above is first pure, then peaceable, gentle, and easy to be intreated, full of mercy and good fruits, without partiality, and without hypocrisy."));
        valuesList.add(new ScriptureEntity("JAM 4:4", "Ye adulterers and adulteresses, know ye not that the friendship of the world is enmity with God? whosoever therefore will be a friend of the world is the enemy of God."));
        valuesList.add(new ScriptureEntity("JAM 4:7", "Submit yourselves therefore to God. Resist the devil, and he will flee from you."));
        valuesList.add(new ScriptureEntity("JAM 4:10", "Humble yourselves in the sight of the Lord, and he shall lift you up."));
        valuesList.add(new ScriptureEntity("JAM 5:13-15", "13 Is any among you afflicted? let him pray. Is any merry? let him sing psalms.\n" +
                "14 Is any sick among you? let him call for the elders of the church; and let them pray over him, anointing him with oil in the name of the Lord:\n" +
                "15 And the prayer of faith shall save the sick, and the Lord shall raise him up; and if he have committed sins, they shall be forgiven him"));
        valuesList.add(new ScriptureEntity("JAM 5:16", "Confess your faults one to another, and pray one for another, that ye may be healed. The effectual fervent prayer of a righteous man availeth much."));

        // 1PETER
        valuesList.add(new ScriptureEntity("1PET 2:2", "As newborn babes, desire the sincere milk of the word, that ye may grow thereby:"));
        valuesList.add(new ScriptureEntity("1PET 2:9", "But ye are a chosen generation, a royal priesthood, an holy nation, a peculiar people; that ye should shew forth the praises of him who hath called you out of darkness into his marvellous light:"));
        valuesList.add(new ScriptureEntity("1PET 2:22-23", "22 Who did no sin, neither was guile found in his mouth:\n" +
                "23 Who, when he was reviled, reviled not again; when he suffered, he threatened not; but committed himself to him that judgeth righteously:"));
        valuesList.add(new ScriptureEntity("1PET 2:24", "Who his own self bare our sins in his own body on the tree, that we, being dead to sins, should live unto righteousness: by whose stripes ye were healed"));

        // 2PETER
        valuesList.add(new ScriptureEntity("2PET 1:4", "Whereby are given unto us exceeding great and precious promises: that by these ye might be partakers of the divine nature, having escaped the corruption that is in the world through lust"));
        valuesList.add(new ScriptureEntity("2PET 1:5-8", "5 And beside this, giving all diligence, add to your faith virtue; and to virtue knowledge;\n" +
                "6 And to knowledge temperance; and to temperance patience; and to patience godliness;\n" +
                "7 And to godliness brotherly kindness; and to brotherly kindness charity.\n" +
                "8 For if these things be in you, and abound, they make you that ye shall neither be barren nor unfruitful in the knowledge of our Lord Jesus Christ."));
        valuesList.add(new ScriptureEntity("2PET 1:9-10", "9 But he that lacketh these things is blind, and cannot see afar off, and hath forgotten that he was purged from his old sins.\n" +
                "10 Wherefore the rather, brethren, give diligence to make your calling and election sure: for if ye do these things, ye shall never fall:"));
        valuesList.add(new ScriptureEntity("2PET 1:20-21", "20 Knowing this first, that no prophecy of the scripture is of any private interpretation.\n" +
                "21 For the prophecy came not in old time by the will of man: but holy men of God spake as they were moved by the Holy Ghost."));
//        valuesList.add(new ScriptureEntity("2PET 5:9", "9 But he that lacketh these things is blind, and cannot see afar off, and hath forgotten that he was purged from his old sins.\n" +
//                "10 Wherefore the rather, brethren, give diligence to make your calling and election sure: for if ye do these things, ye shall never fall:"));

        // 1JOHN
        valuesList.add(new ScriptureEntity("1JOH 1:1", "That which was from the beginning, which we have heard, which we have seen with our eyes, which we have looked upon, and our hands have handled, of the Word of life;"));
        valuesList.add(new ScriptureEntity("1JOH 1:7", "But if we walk in the light, as he is in the light, we have fellowship one with another, and the blood of Jesus Christ his Son cleanseth us from all sin."));
        valuesList.add(new ScriptureEntity("1JOH 1:8-9", "8 If we say that we have no sin, we deceive ourselves, and the truth is not in us.\n" +
                "9 If we confess our sins, he is faithful and just to forgive us our sins, and to cleanse us from all unrighteousness."));
        valuesList.add(new ScriptureEntity("1JOH 2:14", "I have written unto you, fathers, because ye have known him that is from the beginning. I have written unto you, young men, because ye are strong, and the word of God abideth in you, and ye have overcome the wicked one."));
        valuesList.add(new ScriptureEntity("1JOH 2:17", "And the world passeth away, and the lust thereof: but he that doeth the will of God abideth for ever"));
        valuesList.add(new ScriptureEntity("1JOH 3:17-18", "17 But whoso hath this world's good, and seeth his brother have need, and shutteth up his bowels of compassion from him, how dwelleth the love of God in him?\n" +
                "18 My little children, let us not love in word, neither in tongue; but in deed and in truth."));
        valuesList.add(new ScriptureEntity("1JOH 4:4", "Ye are of God, little children, and have overcome them: because greater is he that is in you, than he that is in the world."));
        valuesList.add(new ScriptureEntity("1JOH 4:7", "Beloved, let us love one another: for love is of God; and every one that loveth is born of God, and knoweth God.\n"));
        valuesList.add(new ScriptureEntity("1JOH 5:4", "For whatsoever is born of God overcometh the world: and this is the victory that overcometh the world, even our faith."));
        valuesList.add(new ScriptureEntity("1JOH 5:14-15", "14 And this is the confidence that we have in him, that, if we ask any thing according to his will, he heareth us:\n" +
                "15 And if we know that he hear us, whatsoever we ask, we know that we have the petitions that we desired of him."));

        // 2JOHN
        valuesList.add(new ScriptureEntity("2JOH 10-11", "10 If there come any unto you, and bring not this doctrine, receive him not into your house, neither bid him God speed:\n" +
                "11 For he that biddeth him God speed is partaker of his evil deeds."));

        // 3JOHN
        valuesList.add(new ScriptureEntity("3JOH 2", "Beloved, I wish above all things that thou mayest prosper and be in health, even as thy soul prospereth."));
        valuesList.add(new ScriptureEntity("3JOH 4", "I have no greater joy than to hear that my children walk in truth."));

        // JUDE
        valuesList.add(new ScriptureEntity("JUDE 19", "These be they who separate themselves, sensual, having not the Spirit."));
        valuesList.add(new ScriptureEntity("JUDE 20", "But ye, beloved, building up yourselves on your most holy faith, praying in the Holy Ghost,"));

        // REVELATIONS
        valuesList.add(new ScriptureEntity("REV 2:4-5", "4 Nevertheless I have somewhat against thee, because thou hast left thy first love.\n" +
                "5 Remember therefore from whence thou art fallen, and repent, and do the first works; or else I will come unto thee quickly, and will remove thy candlestick out of his place, except thou repent."));
        valuesList.add(new ScriptureEntity("REV 3:15-17", "15 I know thy works, that thou art neither cold nor hot: I would thou wert cold or hot.\n" +
                "16 So then because thou art lukewarm, and neither cold nor hot, I will spue thee out of my mouth.\n" +
                "17 Because thou sayest, I am rich, and increased with goods, and have need of nothing; and knowest not that thou art wretched, and miserable, and poor, and blind, and naked:\n"));
        valuesList.add(new ScriptureEntity("REV 3:20", "Behold, I stand at the door, and knock: if any man hear my voice, and open the door, I will come in to him, and will sup with him, and he with me."));
        valuesList.add(new ScriptureEntity("REV 5:12", "Saying with a loud voice, Worthy is the Lamb that was slain to receive power, and riches, and wisdom, and strength, and honour, and glory, and blessing."));
        valuesList.add(new ScriptureEntity("REV 10:1-2", "1 And I saw another mighty angel come down from heaven, clothed with a cloud: and a rainbow was upon his head, and his face was as it were the sun, and his feet as pillars of fire:\n" +
                "2 And he had in his hand a little book open: and he set his right foot upon the sea, and his left foot on the earth,"));
        valuesList.add(new ScriptureEntity("REV 10:10-11", "10 And I took the little book out of the angel's hand, and ate it up; and it was in my mouth sweet as honey: and as soon as I had eaten it, my belly was bitter.\n" +
                "11 And he said unto me, Thou must prophesy again before many peoples, and nations, and tongues, and kings."));
        valuesList.add(new ScriptureEntity("REV 18:2-3", "2 And he cried mightily with a strong voice, saying, Babylon the great is fallen, is fallen, and is become the habitation of devils, and the hold of every foul spirit, and a cage of every unclean and hateful bird.\n" +
                "3 For all nations have drunk of the wine of the wrath of her fornication, and the kings of the earth have committed fornication with her, and the merchants of the earth are waxed rich through the abundance of her delicacies.\n"));
        valuesList.add(new ScriptureEntity("REV 20:8", "And shall go out to deceive the nations which are in the four quarters of the earth, Gog and Magog, to gather them together to battle: the number of whom is as the sand of the sea."));
        valuesList.add(new ScriptureEntity("REV 20:12", "And I saw the dead, small and great, stand before God; and the books were opened: and another book was opened, which is the book of life: and the dead were judged out of those things which were written in the books, according to their works."));
        valuesList.add(new ScriptureEntity("REV 20:15", "And whosoever was not found written in the book of life was cast into the lake of fire."));
        valuesList.add(new ScriptureEntity("REV 21:4", "And God shall wipe away all tears from their eyes; and there shall be no more death, neither sorrow, nor crying, neither shall there be any more pain: for the former things are passed away."));
        valuesList.add(new ScriptureEntity("REV 22:3", "And there shall be no more curse: but the throne of God and of the Lamb shall be in it; and his servants shall serve him:"));

//        valuesList.add(new ScriptureEntity("", ""));


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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                Intent intent = new Intent(BooksOfTheBible.this, AboutApp.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
