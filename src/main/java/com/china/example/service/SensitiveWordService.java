package com.china.example.service;

import com.china.example.cache.SensitiveCache;
import com.china.example.dao.entity.SensitiveWord;
import com.china.example.dao.mapper.SensitiveWordMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by kakasun on 2017/7/10.
 */
@Service
public class SensitiveWordService {

    /**
     * 敏感词redis缓存
     */
    @Resource
    SensitiveCache sensitiveCache;
    /**
     * redisTemplate
     */
    @Resource
    RedisTemplate<String,Object> redisTemplate;

    /**
     * 敏感词持久层
     */
    @Resource
    SensitiveWordMapper sensitiveWordMapper;

    /**
     * 保存敏感词
     * @param sensitiveWord
     */
    @Transactional
    public void saveSensitiveWord(SensitiveWord sensitiveWord){
        try {
            sensitiveWordMapper.insertSensitiveWord(sensitiveWord);
        }catch (Exception e){
            //敏感词如果重复保存，违反唯一键约束，则对该敏感词进行更新
            sensitiveWordMapper.updateSensitiveWordByWord(sensitiveWord);
        }
        //将新添加的敏感词放入redis
        redisTemplate.multi();
        sensitiveCache.add(sensitiveWord.getWord());
        redisTemplate.exec();
    }

    /**
     *
     * @param ids
     */
    @Transactional
    public void removeSensitiveWord(long[] ids) {
        List<SensitiveWord> words = sensitiveWordMapper.findByIds(ids);
        if (words == null || words.size() == 0) {
            return;
        }
        sensitiveWordMapper.deleteByIds(ids);
        redisTemplate.multi();
        for (SensitiveWord sw : words) {
            sensitiveCache.remove(sw.getWord());
        }
        redisTemplate.exec();
    }

    /**
     * 查出文中所有的敏感词
     * @return
     */
    public Set<String> checkOutAllSensitiveWord(String text,boolean isHtml){
        Set<String> set = new TreeSet<String>();
        if (text == null || text.trim().length() == 0){
            return set;
        }
        if (isHtml){
            text = text.replaceAll("</?[^<>]+>","");
        }
        text = text.replaceAll("[\r\n]+","");
        int l = text.length();
        String word = null;
        for(int i = 0;i < l;i++){
            word = sensitiveCache.checkOutSensitiveWord(text,i);
            if(word != null){
                set.add(word);
            }
        }
        return set;
    }

}
