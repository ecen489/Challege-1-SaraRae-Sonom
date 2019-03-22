package com.example.cameraapp;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

public class DatabaseInterface {
    private SQLiteDatabase database;
    private Context context;
    private final String databaseTitle = "photos";

    private final String CREATE_SQL = "CREATE TABLE " + this.databaseTitle + " (" + "id INTEGER PRIMARY KEY AUTOINCREMENT," + " image TEXT)";

    private final String DROP_TABLE_SQL = "DROP TABLE IF EXISTS " + this.databaseTitle;

    public DatabaseInterface() {}

    public DatabaseInterface(Context context){
        this.context = context; this.database = context.openOrCreateDatabase(this.databaseTitle, Context.MODE_PRIVATE, null);
        createDatabase();
    }

    private void createDatabase() {
        try {
            this.database.execSQL(this.DROP_TABLE_SQL);
            this.database.execSQL(this.CREATE_SQL);
        }
        catch (SQLiteException ex_ception) {
            System.out.println("Could not create table."); System.out.println(ex_ception.getMessage());
            ex_ception.printStackTrace();
        }
    }

    public void loadImageToDatabase(String img64) throws SQLiteException {
        String insert = "INSERT INTO " + this.databaseTitle + "(image) VALUES ('" + img64 + "')";
        database.execSQL(insert);
    }

    public String retrievePhoto(String imageID) throws SQLiteException {
        String select = "SELECT image FROM " + this.databaseTitle + " WHERE id=" + imageID;
        Cursor cr = database.rawQuery(select, null);

        String returnedImg = "Error.";

        if(cr.moveToFirst()) {
            do { returnedImg = cr.getString(cr.getColumnIndex("image"));
            } while (cr.moveToNext());
            cr.close();
        }
        return returnedImg;
    }

    public String fromBmapTo64(Bitmap bmap) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream(); bmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        byte[] byteArray2 = byteArray.toByteArray();

        String img64 = Base64.encodeToString(byteArray2, Base64.DEFAULT);
        return img64;
    }

    public Bitmap from64toBmap(String img64) {
        byte[] data = Base64.decode(img64, Base64.DEFAULT);
        Bitmap bmap; BitmapFactory.Options op_tion = new BitmapFactory.Options();
        op_tion.inMutable = true;
        bmap = BitmapFactory.decodeByteArray(data, 0, data.length, op_tion);
        return bmap;
    }
}
