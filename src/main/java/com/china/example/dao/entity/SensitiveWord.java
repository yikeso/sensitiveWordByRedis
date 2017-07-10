package com.china.example.dao.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by kakasun on 2017/7/10.
 */
public class SensitiveWord implements Serializable {

    private static final long serialVersionUID = 5238287808739898209L;
    /**
     * id
     */
    Long sys_id;
    /**
     * 敏感词
     */
    String word;
    /**
     * 创建时间
     */
    Timestamp create_time;
    /**
     * 上次修改时间
     */
    Timestamp last_update = new Timestamp(System.currentTimeMillis());
    /**
     * 是否删除，0表示未删除，1表示已删除
     */
    int del;
    /**
     * 类别，0表示敏感词
     */
    int category = 0;

    /**
     * 敏感事由
     */
    String explain_word = "";

    public Long getSys_id() {
        return sys_id;
    }

    public void setSys_id(Long sys_id) {
        this.sys_id = sys_id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }

    public Timestamp getLast_update() {
        return last_update;
    }

    public void setLast_update(Timestamp last_update) {
        this.last_update = last_update;
    }

    public int getDel() {
        return del;
    }

    public void setDel(int del) {
        this.del = del;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getExplain() {
        return explain_word;
    }

    public void setExplain(String explain) {
        this.explain_word = explain;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SensitiveWord that = (SensitiveWord) o;

        return sys_id.equals(that.sys_id);
    }

    @Override
    public int hashCode() {
        return sys_id.hashCode();
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("SensitiveWord{");
        sb.append("sys_id=").append(sys_id);
        sb.append(", word='").append(word).append('\'');
        sb.append(", create_time=").append(create_time);
        sb.append(", last_update=").append(last_update);
        sb.append(", del=").append(del);
        sb.append(", category=").append(category);
        sb.append(", explain='").append(explain_word).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
