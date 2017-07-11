package com.china.example.cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 敏感词的redis缓存
 * Created by kakasun on 2017/7/10.
 */
@Repository
public class SensitiveCache {
    /**
     * redis Hash数据结构
     */
    @Resource
    RedisTemplate<String, Object> redisTemplate;

    final static String SENSITIVE_PRE = "Sensitive_";

    /**
     * 向redis中，添加敏感词
     * @param word
     */
    public void add(String word){
        word = word.trim();
        int l = word.length();
        Object o;
        String key;
        Map<String,String> value;
        for(int i = 0;i < l;i++){
            key = SENSITIVE_PRE + word.substring(0,i + 1);
            //该hash不存在或为空puthash
            if (!redisTemplate.hasKey(key)){
                if( i == l -1){
                    redisTemplate.opsForValue().set(key,"true");
                }else {
                    redisTemplate.opsForValue().set(key,"false");
                }
            }
        }
    }

    /**
     * 从redis中移除敏感词
     * @param word
     */
    public void remove(String word){
        word = word.trim();
        redisTemplate.delete(SENSITIVE_PRE+word);
    }

    /**
     * 检查从开始位置是否是敏感词
     * @param text
     * @param index
     * @return
     */
    public String checkOutSensitiveWord(String text,int index){
        int l = text.length();
        String key;
        String sub;
        String end;
        for(int i = index;i < l;i++){
            sub = text.substring(index,i + 1);
            key = SENSITIVE_PRE + sub;
            //key不存在返回null
            if (!redisTemplate.hasKey(key)){
                return null;
            }
            end = (String)redisTemplate.opsForValue().get(key);
            //该词语已经结束，则返回该敏感词
            if (end != null && "true".equalsIgnoreCase(end)){
                return sub;
            }
        }
        return null;
    }
}
