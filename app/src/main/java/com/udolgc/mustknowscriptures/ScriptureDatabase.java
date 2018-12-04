package com.udolgc.mustknowscriptures;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

@Database(entities = ScriptureEntity.class, version = 1)
public abstract class ScriptureDatabase extends RoomDatabase {

    public  abstract ScriptureDAO getScriptureDAO();
    private Context context;

    public Context getContext() {
        return context;
    }





    public class doDatabaseOperations extends AsyncTask<Void, Void, Void> {

        private Exception e = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {

            try {

                ScriptureDatabase db = Room.databaseBuilder(getContext(), ScriptureDatabase.class, "scripture_db")
                        .build();
//                List<ScriptureEntity> scriptures = db.scriptureDAO.getAll();

                ScriptureDAO contactDAO = db.getScriptureDAO();

//Inserting a contact
                ScriptureEntity scripture = new ScriptureEntity();
                scripture.setId(1);
                scripture.setTitle("Rom 3:23");
                scripture.setScripture("For all have sinned and fallen short of the glory of God");
                contactDAO.insert(scripture);


//Fetching all contacts
                List<ScriptureEntity> contact = contactDAO.getAll();
                System.out.println("contact: " + contact.toString());

//Getting a single contact and updating it
//                Contact contact = contactDAO.getContactWithId("1234567890");
//
//                contact.setFirstName("Kevin");
//                contact.setLastName("Brew");
//
//                contactDAO.update(contact);


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            //			Toast.makeText(Settings.this, mb_response.trim(), Toast.LENGTH_LONG).show();


        }
    }


}
