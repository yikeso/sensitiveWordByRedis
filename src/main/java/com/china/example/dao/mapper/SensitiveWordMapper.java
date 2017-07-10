package com.china.example.dao.mapper;

import com.china.example.dao.entity.SensitiveWord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 敏感词持久层
 * Created by kakasun on 2017/7/10.
 */
public interface SensitiveWordMapper {

    /**
     * 查找出所有正在使用中的敏感词
     * @return
     */
    @Select("SELECT word FROM pec_sensitive_words " +
            "WHERE del = 0 AND category = 0 ORDER BY sys_id desc")
    List<String> findAllUsedSensitiveWord();

    /**
     * 插入敏感词
     * @param sensitiveWord
     */
    @Insert("Insert Into pec_sensitive_words (word,category," +
            "last_update,explain_word) " +
            "Values(#{word,jdbcType = varchar},#{category,jdbcType = INTEGER}," +
            "#{last_update},#{explain_word,jdbcType = VARCHAR})" )
    @Options(useGeneratedKeys = true, keyProperty = "sys_id", keyColumn = "sys_id")
    void insertSensitiveWord(SensitiveWord sensitiveWord);

    /**
     * 根据敏感词更新
     * @param sensitiveWord
     */
    @Update("UPDATE pec_sensitive_words SET del = #{del},category = #{category}," +
            "last_update = #{last_update},explain_word = #{explain_word} WHERE word = #{word}")
    void updateSensitiveWordByWord(SensitiveWord sensitiveWord);

    /**
     * 根据id查找敏感词
     * @param ids
     * @return
     */
    @Select("<script> " +
            "SELECT sys_id,word,create_time,last_update,del,category,explain_word " +
            "FROM pec_sensitive_words WHERE sys_id IN " +
            "<foreach collection='array' item='id' index='i' " +
            " open='(' close=')' separator=','> " +
            " #{id} " +
            " </foreach> " +
            "</script>")
    List<SensitiveWord> findByIds(long[] ids);

    /**
     * 根据id删除
     * @param ids
     */
    @Update("DELETE FROM pec_sensitive_words WHERE sys_id IN " +
            "<foreach collection='array' item='id' index='i' " +
            " open='(' close=')' separator=','> " +
            " #{id} " +
            " </foreach> " +
            "</script>")
    void deleteByIds(long[] ids);
}
