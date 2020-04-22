package com.liyiwei.basethirdlib.dbgreendao.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.liyiwei.basethirdlib.dbgreendao.bean.ExamCache;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EXAM_CACHE".
*/
public class ExamCacheDao extends AbstractDao<ExamCache, Long> {

    public static final String TABLENAME = "EXAM_CACHE";

    /**
     * Properties of entity ExamCache.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property PaperId = new Property(0, long.class, "paperId", true, "_id");
        public final static Property CurrentCount = new Property(1, int.class, "currentCount", false, "CURRENT_COUNT");
        public final static Property TakeMil = new Property(2, long.class, "takeMil", false, "TAKE_MIL");
        public final static Property Questions = new Property(3, String.class, "questions", false, "QUESTIONS");
    }


    public ExamCacheDao(DaoConfig config) {
        super(config);
    }
    
    public ExamCacheDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EXAM_CACHE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY NOT NULL ," + // 0: paperId
                "\"CURRENT_COUNT\" INTEGER NOT NULL ," + // 1: currentCount
                "\"TAKE_MIL\" INTEGER NOT NULL ," + // 2: takeMil
                "\"QUESTIONS\" TEXT);"); // 3: questions
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EXAM_CACHE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ExamCache entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getPaperId());
        stmt.bindLong(2, entity.getCurrentCount());
        stmt.bindLong(3, entity.getTakeMil());
 
        String questions = entity.getQuestions();
        if (questions != null) {
            stmt.bindString(4, questions);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ExamCache entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getPaperId());
        stmt.bindLong(2, entity.getCurrentCount());
        stmt.bindLong(3, entity.getTakeMil());
 
        String questions = entity.getQuestions();
        if (questions != null) {
            stmt.bindString(4, questions);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public ExamCache readEntity(Cursor cursor, int offset) {
        ExamCache entity = new ExamCache( //
            cursor.getLong(offset + 0), // paperId
            cursor.getInt(offset + 1), // currentCount
            cursor.getLong(offset + 2), // takeMil
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // questions
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ExamCache entity, int offset) {
        entity.setPaperId(cursor.getLong(offset + 0));
        entity.setCurrentCount(cursor.getInt(offset + 1));
        entity.setTakeMil(cursor.getLong(offset + 2));
        entity.setQuestions(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ExamCache entity, long rowId) {
        entity.setPaperId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ExamCache entity) {
        if(entity != null) {
            return entity.getPaperId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ExamCache entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
