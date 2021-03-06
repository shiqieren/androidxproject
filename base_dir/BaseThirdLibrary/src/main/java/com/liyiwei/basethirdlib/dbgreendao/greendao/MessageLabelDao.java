package com.liyiwei.basethirdlib.dbgreendao.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.liyiwei.basethirdlib.dbgreendao.bean.MessageLabel;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MESSAGE_LABEL".
*/
public class MessageLabelDao extends AbstractDao<MessageLabel, String> {

    public static final String TABLENAME = "MESSAGE_LABEL";

    /**
     * Properties of entity MessageLabel.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Name = new Property(0, String.class, "name", false, "NAME");
        public final static Property Color = new Property(1, String.class, "color", false, "COLOR");
        public final static Property Label = new Property(2, String.class, "label", true, "LABEL");
    }


    public MessageLabelDao(DaoConfig config) {
        super(config);
    }
    
    public MessageLabelDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MESSAGE_LABEL\" (" + //
                "\"NAME\" TEXT," + // 0: name
                "\"COLOR\" TEXT," + // 1: color
                "\"LABEL\" TEXT PRIMARY KEY NOT NULL );"); // 2: label
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MESSAGE_LABEL\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, MessageLabel entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(2, color);
        }
 
        String label = entity.getLabel();
        if (label != null) {
            stmt.bindString(3, label);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, MessageLabel entity) {
        stmt.clearBindings();
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(1, name);
        }
 
        String color = entity.getColor();
        if (color != null) {
            stmt.bindString(2, color);
        }
 
        String label = entity.getLabel();
        if (label != null) {
            stmt.bindString(3, label);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2);
    }    

    @Override
    public MessageLabel readEntity(Cursor cursor, int offset) {
        MessageLabel entity = new MessageLabel( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // name
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // color
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2) // label
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, MessageLabel entity, int offset) {
        entity.setName(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setColor(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLabel(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
     }
    
    @Override
    protected final String updateKeyAfterInsert(MessageLabel entity, long rowId) {
        return entity.getLabel();
    }
    
    @Override
    public String getKey(MessageLabel entity) {
        if(entity != null) {
            return entity.getLabel();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(MessageLabel entity) {
        return entity.getLabel() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
