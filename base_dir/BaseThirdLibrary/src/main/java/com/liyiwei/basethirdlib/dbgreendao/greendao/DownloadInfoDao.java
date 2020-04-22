package com.liyiwei.basethirdlib.dbgreendao.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.liyiwei.basethirdlib.dbgreendao.bean.DownloadInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DOWNLOAD_INFO".
*/
public class DownloadInfoDao extends AbstractDao<DownloadInfo, String> {

    public static final String TABLENAME = "DOWNLOAD_INFO";

    /**
     * Properties of entity DownloadInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Md5 = new Property(0, String.class, "md5", true, "MD5");
        public final static Property Url = new Property(1, String.class, "url", false, "URL");
        public final static Property FileName = new Property(2, String.class, "fileName", false, "FILE_NAME");
        public final static Property FilePath = new Property(3, String.class, "filePath", false, "FILE_PATH");
        public final static Property State = new Property(4, int.class, "state", false, "STATE");
        public final static Property Total = new Property(5, long.class, "total", false, "TOTAL");
        public final static Property Progress = new Property(6, long.class, "progress", false, "PROGRESS");
        public final static Property CurrSize = new Property(7, long.class, "currSize", false, "CURR_SIZE");
    }

    private DaoSession daoSession;


    public DownloadInfoDao(DaoConfig config) {
        super(config);
    }
    
    public DownloadInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DOWNLOAD_INFO\" (" + //
                "\"MD5\" TEXT PRIMARY KEY NOT NULL ," + // 0: md5
                "\"URL\" TEXT," + // 1: url
                "\"FILE_NAME\" TEXT," + // 2: fileName
                "\"FILE_PATH\" TEXT," + // 3: filePath
                "\"STATE\" INTEGER NOT NULL ," + // 4: state
                "\"TOTAL\" INTEGER NOT NULL ," + // 5: total
                "\"PROGRESS\" INTEGER NOT NULL ," + // 6: progress
                "\"CURR_SIZE\" INTEGER NOT NULL );"); // 7: currSize
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DOWNLOAD_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DownloadInfo entity) {
        stmt.clearBindings();
 
        String md5 = entity.getMd5();
        if (md5 != null) {
            stmt.bindString(1, md5);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
 
        String fileName = entity.getFileName();
        if (fileName != null) {
            stmt.bindString(3, fileName);
        }
 
        String filePath = entity.getFilePath();
        if (filePath != null) {
            stmt.bindString(4, filePath);
        }
        stmt.bindLong(5, entity.getState());
        stmt.bindLong(6, entity.getTotal());
        stmt.bindLong(7, entity.getProgress());
        stmt.bindLong(8, entity.getCurrSize());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DownloadInfo entity) {
        stmt.clearBindings();
 
        String md5 = entity.getMd5();
        if (md5 != null) {
            stmt.bindString(1, md5);
        }
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(2, url);
        }
 
        String fileName = entity.getFileName();
        if (fileName != null) {
            stmt.bindString(3, fileName);
        }
 
        String filePath = entity.getFilePath();
        if (filePath != null) {
            stmt.bindString(4, filePath);
        }
        stmt.bindLong(5, entity.getState());
        stmt.bindLong(6, entity.getTotal());
        stmt.bindLong(7, entity.getProgress());
        stmt.bindLong(8, entity.getCurrSize());
    }

    @Override
    protected final void attachEntity(DownloadInfo entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public DownloadInfo readEntity(Cursor cursor, int offset) {
        DownloadInfo entity = new DownloadInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // md5
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // url
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // fileName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // filePath
            cursor.getInt(offset + 4), // state
            cursor.getLong(offset + 5), // total
            cursor.getLong(offset + 6), // progress
            cursor.getLong(offset + 7) // currSize
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DownloadInfo entity, int offset) {
        entity.setMd5(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setUrl(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setFileName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setFilePath(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setState(cursor.getInt(offset + 4));
        entity.setTotal(cursor.getLong(offset + 5));
        entity.setProgress(cursor.getLong(offset + 6));
        entity.setCurrSize(cursor.getLong(offset + 7));
     }
    
    @Override
    protected final String updateKeyAfterInsert(DownloadInfo entity, long rowId) {
        return entity.getMd5();
    }
    
    @Override
    public String getKey(DownloadInfo entity) {
        if(entity != null) {
            return entity.getMd5();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DownloadInfo entity) {
        return entity.getMd5() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
