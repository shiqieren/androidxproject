package com.liyiwei.basethirdlib;

import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;


import com.liyiwei.basethirdlib.roomwcdb.DBHelper;
import com.liyiwei.basethirdlib.roomwcdb.Word;
import com.liyiwei.basethirdlib.roomwcdb.WordRepository;
import com.liyiwei.basethirdlib.roomwcdb.entity.AppDatabase;
import com.liyiwei.basethirdlib.roomwcdb.entity.User;
import com.liyiwei.basethirdlib.roomwcdb.entity.UserDao;
import com.tencent.wcdb.database.SQLiteCipherSpec;
import com.tencent.wcdb.database.SQLiteDatabase;
import com.tencent.wcdb.database.SQLiteException;
import com.tencent.wcdb.database.SQLiteOpenHelper;
import com.tencent.wcdb.repair.RepairKit;
import com.tencent.wcdb.support.CancellationSignal;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Random;

import hongyi.basethirdlib.R;

public class BaseRoomWCDBActivity extends AppCompatActivity {
    private static final String TAG = "BaseNetWorkActivity";
    WordRepository mRepository ;

    //初始化修复-取消修复
    private SQLiteDatabase mDB;
    private final SQLiteOpenHelper mDBHelper = new DBHelper(this);
    private RepairKit mRepair;
    private CancellationSignal mCancellationSignal;
    private Button mCancelButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Extract test database from assets.从db'文件启动修复任务
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                File dbFile = getDatabasePath(DBHelper.DATABASE_NAME);

                if (!dbFile.exists()) {
                    dbFile.getParentFile().mkdirs();

                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        byte[] buffer = new byte[1024];
                        in = getAssets().open(DBHelper.DATABASE_NAME);
                        out = new FileOutputStream(dbFile);
                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } finally {
                        if (in != null) try { in.close(); } catch (IOException e) {}
                        if (out != null) try { out.close(); } catch (IOException e) {}
                    }
                }
                return null;
            }
        }.execute();


        setContentView(R.layout.roomwcdb_activity);


        ////////////////////////////////增删改查//////////////////////////

         mRepository = new WordRepository(BaseThirdApplication.getInstance());

        ((Button)findViewById(R.id.button_save)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Word word = new Word("insert");
                mRepository.insert(word);
            }
        });

        ((Button)findViewById(R.id.button_delete)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            }
        });

        ((Button)findViewById(R.id.button_updata)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

            }
        });

        ((Button)findViewById(R.id.button_select)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                List<Word> mAllWords = mRepository.getAllWords();
            }
        });


        ////////////////////////////////修复//////////////////////////

        findViewById(R.id.btn_init_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, SQLiteException>() {
                    @Override
                    protected void onPreExecute() {
//                        mAdapter.changeCursor(null);
                    }

                    @Override
                    protected SQLiteException doInBackground(Void... params) {
                        if (mDB != null && mDB.isOpen()) {
                            mDBHelper.close();
                            mDB = null;
                        }

                        try {
                            mDBHelper.setWriteAheadLoggingEnabled(true);
                            mDB = mDBHelper.getWritableDatabase();

                            // After successfully opened the database, backup its master info.
                            RepairKit.MasterInfo.save(mDB, mDB.getPath() + "-mbak", DBHelper.PASSPHRASE);
                        } catch (SQLiteException e) {
                            // Failed to open database, probably due to corruption.
                            mDB = null;
                            return e;
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(SQLiteException e) {
                        if (e == null) {
                            // Database is successfully opened, query and refresh ListView.
                            Cursor cursor = mDB.rawQuery("SELECT rowid as _id, a, b FROM t1;",
                                    null);
//                            mAdapter.changeCursor(cursor);

                            Toast.makeText(BaseRoomWCDBActivity.this, "Database is successfully opened.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Database could not be opened, show toast.
                            Toast.makeText(BaseRoomWCDBActivity.this, "Database cannot be opened, exception: "
                                    + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
            }
        });

        findViewById(R.id.btn_corrupt_db).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Exception>() {
                    @Override
                    protected void onPreExecute() {
//                        mAdapter.changeCursor(null);
                    }

                    @Override
                    protected Exception doInBackground(Void... params) {
                        if (mDB != null && mDB.isOpen()) {
                            mDBHelper.close();
                            mDB = null;
                        }

                        // Write random noise to the first page to corrupt the database.
                        RandomAccessFile raf = null;
                        try {
                            File dbFile = getDatabasePath(DBHelper.DATABASE_NAME);
                            raf = new RandomAccessFile(dbFile, "rw");
                            byte[] buffer = new byte[1024];
                            new Random().nextBytes(buffer);
                            raf.seek(0);
                            raf.write(buffer);
                        } catch (IOException e) {
                            return e;
                        } finally {
                            if (raf != null) try { raf.close(); } catch (IOException e) {}
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Exception e) {
                        if (e == null) {
                            Toast.makeText(BaseRoomWCDBActivity.this, "Database is now CORRUPTED!",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(BaseRoomWCDBActivity.this, "Unable to overwrite database: "
                                    + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }.execute();
            }
        });

        mCancelButton = (Button) findViewById(R.id.btn_repair_cancel);
        findViewById(R.id.btn_repair_db).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, SQLiteException>() {
                    @Override
                    protected void onPreExecute() {
//                        mAdapter.changeCursor(null);
                        mCancellationSignal = new CancellationSignal();
                        mCancelButton.setEnabled(true);
                    }

                    @Override
                    protected SQLiteException doInBackground(Void... params) {
                        if (mDB != null && mDB.isOpen()) {
                            mDBHelper.close();
                            mDB = null;
                        }

                        RepairKit.MasterInfo master = null;
                        File dbFile = getDatabasePath(DBHelper.DATABASE_NAME);
                        File masterFile = new File(dbFile.getPath() + "-mbak");
                        File newDbFile = getDatabasePath(DBHelper.DATABASE_NAME + "-recover");

                        if (masterFile.exists()) {
                            try {
                                master = RepairKit.MasterInfo.load(masterFile.getPath(),
                                        DBHelper.PASSPHRASE, null);
                            } catch (SQLiteException e) {
                                // Could not load master info. Maybe backup file itself corrupted?
                            }
                        }

                        mRepair = null;
                        try {
                            mRepair = new RepairKit(
                                    dbFile.getPath(),       // corrupted database file
                                    DBHelper.PASSPHRASE,    // passphrase to the database
                                    DBHelper.CIPHER_SPEC,   // cipher spec to the database
                                    master                  // backup master info just loaded
                            );

                            if (newDbFile.exists())
                                newDbFile.delete();

                            SQLiteDatabase newDb = SQLiteDatabase.openOrCreateDatabase(newDbFile,
                                    DBHelper.PASSPHRASE, DBHelper.CIPHER_SPEC, null,
                                    DBHelper.ERROR_HANDLER);
                            //更新最新版本wcdb
//                            mRepair.setCallback(new RepairKit.Callback() {
//                                @Override
//                                public int onProgress(String table, int root, Cursor cursor) {
//                                    Log.d(TAG, String.format("table: %s, root: %d, count: %d",
//                                            table, root, cursor.getColumnCount()));
//                                    return RepairKit.RESULT_OK;
//                                }
//                            });
//                            int result = mRepair.output(newDb, 0);
//                            if (result != RepairKit.RESULT_OK && result != RepairKit.RESULT_CANCELED) {
//                                throw new SQLiteException("Repair returns failure.");
//                            }

                            newDb.setVersion(DBHelper.DATABASE_VERSION);
                            newDb.close();
                            mRepair.release();
                            mRepair = null;

                            if (!dbFile.delete() || !newDbFile.renameTo(dbFile))
                                throw new SQLiteException("Cannot rename database.");
                        } catch (SQLiteException e) {
                            return e;
                        } finally {
                            if (mRepair != null) {
                                mRepair.release();
                                mRepair = null;
                            }
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(SQLiteException e) {
                        if (e == null) {
                            Toast.makeText(BaseRoomWCDBActivity.this, "Repair succeeded.", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(BaseRoomWCDBActivity.this, "Repair failed: "
                                    + e.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        mCancelButton.setEnabled(false);
                        mCancellationSignal = null;
                    }
                }.execute();
            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCancellationSignal != null) {
                    mCancellationSignal.cancel();
                }
            }
        });

    }


    //////////////////////////////////////room 持久化 模板 start////////////////////
//     static final SQLiteCipherSpec sCipherSpec = new SQLiteCipherSpec()
//            .setPageSize(4096)
//            .setKDFIteration(64000);
//
//    private AppDatabase mAppDB = Room.databaseBuilder(this, AppDatabase.class, "app-db")
//            .allowMainThreadQueries()
//            .openHelperFactory(new WCDBOpenHelperFactory()
//                    .passphrase("passphrase".getBytes())
//                    .cipherSpec(sCipherSpec)
//                    .writeAheadLoggingEnabled(true)
//                    .asyncCheckpointEnabled(true)
//            )
//            .addCallback(new RoomDatabase.Callback() {
//                @Override
//                public void onOpen(@NonNull SupportSQLiteDatabase db) {
//                    SQLiteDatabase realDb = ((WCDBDatabase) db).getInnerDatabase();
//
//                    realDb.addExtension(MMFtsTokenizer.EXTENSION);
//                }
//            })
//            .build();
//    private UserDao mUsers= mAppDB.userDao();
//
//    User user = new User();
//    user.firstName = "John";
//    user.lastName = "He";
//        mUsers.insert(user);
//
//    user.firstName = "Sanhua";
//    user.lastName = "Zhang";
//        mUsers.insert(user);
//
//    List<User> userList = mUsers.getAll();

    //////////////////////////////////////room 持久化 end////////////////////
}

