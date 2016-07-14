package com.framework.phvtDatabase;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.framework.phvtCommon.AppState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Phatvt-FrameWork
 *
 * @author PhatVan ヴァン  タン　ファット
 * @since: 11/18/15
 *
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    //TAG
    private static String TAG = DatabaseHelper.class.getName();
    //destination path (location) of our database on device
    private static String DB_PATH = Environment.getExternalStorageDirectory().getPath() + "/your_folder/";
    //database name
    private static String DB_NAME = "your_db_name.sqlite";
    //system sqlite databse
    private SQLiteDatabase mDatabase;
    //context activity
    private final Context mContext;


    /**
     * Database Constructor
     * @param context
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null,1);
        this.mContext = context;
    }


    /**
     * DatabaseHelper Contructor
     * @param context
     * @param database_name
     * @param databse_path
     * @param databse_version
     */
    public DatabaseHelper(Context context, String database_name, String databse_path, int databse_version) {
        super(context, DB_NAME, null,databse_version);
        this.mContext = context;
        this.DB_NAME = database_name;
        this.DB_PATH = databse_path;
    }

    /**
     * DatabaseHelper Contructor
     * @param context
     * @param database_name
     * @param databse_folder
     * @param databse_version
     * @param factory
     */

    public DatabaseHelper(Context context, String database_name, String databse_folder, int databse_version, SQLiteDatabase.CursorFactory factory) {
        super(context, DB_NAME, factory,databse_version);
        this.mContext = context;
        this.DB_NAME = database_name;
        this.DB_PATH =  Environment.getExternalStorageDirectory().getPath() + "/" + databse_folder + "/";;
    }

    /**
     * Create Database
     * Including: copydatabse
     * @throws IOException
     */
    public void createDatabase() throws IOException {
        boolean mDatabaseExist = checkDatabase();
        File dbFile = new File(DB_PATH);
        if(!mDatabaseExist) {
            dbFile.mkdirs();
            try {
                copyDatabase();
                Log.e(TAG, "createDatabase database created");
                AppState.isCreateDatabase = true;
            } catch (IOException mIOException) {
                throw new Error("ErrorCopyingDatabase");
            }
        } else {
            AppState.isCreateDatabase = false;
        }
    }


    /**
     * Check exist database
     * Check that the database exists here: /mnt/sdcard/Documents/databse_name.sqlite
     *
     * @return true-false
     */
    private boolean checkDatabase() {
        File dbFile = new File( DB_PATH + DB_NAME );
        Log.v("dbFile", dbFile + " " + dbFile.exists());
        return dbFile.exists();
    }


    /**
     * Copy the database from assets folder
     * From ASSET to MNT Card
     *
     * @throws IOException
     */
    private void copyDatabase() throws IOException {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while((mLength = mInput.read(mBuffer))>0) {
            mOutput.write(mBuffer,0,mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    /***
     * OPEN databse
     * @return true-false can be open
     * @throws SQLException
     */

    public boolean openDatabase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        if(mDatabase==null || mDatabase.isOpen()==false){
            mDatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        }
        return mDatabase != null;
    }


    /**
     * Open databse
     * @return: void
     */
    public void open(){
        if(mDatabase==null || mDatabase.isOpen()==false){
            String mPath = DB_PATH + DB_NAME;
            mDatabase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        }
    }


    /**
     * Close Databse
     */
    @Override
    public synchronized void close() {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
        super.close();
    }


    /**
     * getDatabaseConnection
     * @return SQLiteDatabse
     */
    public SQLiteDatabase getDatabaseConnection(){
        return mDatabase;
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }


    /**
     * get Databse Name
     * @return String
     *
     */
    public static String getDatabseName(){
        return DB_NAME;
    }


    /**
     * Processing databse for first load and more
     * It's need copy & replace if needed
     * or
     */

}
