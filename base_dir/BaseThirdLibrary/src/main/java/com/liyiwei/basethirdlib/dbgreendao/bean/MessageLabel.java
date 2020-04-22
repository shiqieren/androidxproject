package com.liyiwei.basethirdlib.dbgreendao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

@Entity
public class MessageLabel implements IPartShadow{


    /**
     * name : 通知
     * color : #66BFFF
     * label : NOTICE
     */

    private String name;
    private String color;
    @Id
    private String label;

    @Generated(hash = 1405233616)
    public MessageLabel(String name, String color, String label) {
        this.name = name;
        this.color = color;
        this.label = label;
    }

    @Generated(hash = 517530409)
    public MessageLabel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
