package org.example.rgybackend.Utils;

import java.time.LocalDate;
import java.util.List;

import org.example.rgybackend.Model.DiaryModel;
import org.example.rgybackend.Model.EmotionModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class CacheUtil {
    @Autowired
    private CacheManager cacheManager;

    public void evictPsyProfileCache(String psyid) {
        Cache cache = cacheManager.getCache("psyprofile");
        if(cache != null) {
            cache.evict(psyid);
        }
    }

    public void evictIntimateUsersCache(String userid) {
        Cache cache = cacheManager.getCache("intimateUsers");
        if(cache != null) {
            cache.evict(userid);
        }
    }

    public void putEmotionsToCache(String userid, LocalDate date, List<EmotionModel> emotions) {
        Cache cache = cacheManager.getCache("userEmotions");
        if(cache != null) {
            cache.put(userid + "_" + date, emotions);
        }
    }

    public void evictEmotionsCache(String userid, LocalDate date) {
        Cache cache = cacheManager.getCache("userEmotions");
        if(cache != null) {
            cache.evict(userid + "_" + date);
        }
    }

    @SuppressWarnings("unchecked")
    public List<EmotionModel> getEmotionsFromCache(String userid, LocalDate date) {
        Cache cache = cacheManager.getCache("userEmotions");
        if(cache != null) {
            Cache.ValueWrapper wrapper = cache.get(userid + "_" + date);
            if(wrapper != null) {
                return (List<EmotionModel>) wrapper.get();
            }
        }
        return null;
    }

    public void putDiariesToCache(String userid, LocalDate date, List<DiaryModel> diaries) {
        Cache cache = cacheManager.getCache("userDiaries");
        if(cache != null) {
            cache.put(userid + "_" + date, diaries);
        }
    }

    public void evictDiariesCache(String userid, LocalDate date) {
        Cache cache = cacheManager.getCache("userDiaries");
        if(cache != null) {
            cache.evict(userid + "_" + date);
        }
    }

    @SuppressWarnings("unchecked")
    public List<DiaryModel> getDiariesFromCache(String userid, LocalDate date) {
        Cache cache = cacheManager.getCache("userDiaries");
        if(cache != null) {
            Cache.ValueWrapper wrapper = cache.get(userid + "_" + date);
            if(wrapper != null) {
                return (List<DiaryModel>) wrapper.get();
            }
        }
        return null;
    }

    public void putAllEmotionsToCache(LocalDate date, List<EmotionModel> emotions) {
        Cache cache = cacheManager.getCache("allEmotions");
        if(cache != null) {
            cache.put(date, emotions);
        }
    }

    public void evictAllEmotionsCache(LocalDate date) {
        Cache cache = cacheManager.getCache("allEmotions");
        if(cache != null) {
            cache.evict(date);
        }
    }

    @SuppressWarnings("unchecked")
    public List<EmotionModel> getAllEmotionsFromCache(LocalDate date) {
        Cache cache = cacheManager.getCache("allEmotions");
        if(cache != null) {
            Cache.ValueWrapper wrapper = cache.get(date);
            if(wrapper != null) {
                return (List<EmotionModel>) wrapper.get();
            }
        }
        return null;
    }
}
