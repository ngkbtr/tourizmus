package ngkbtr.storage.dao;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class RedisDao {
    private final RedissonClient client;

    public RedisDao() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        client = Redisson.create(config);
    }

    public <T> void save(String group, String key, T object){
        client.getMap(group).put(key, object);
    }

    public <T> void saveTTL(String group, String key, T object, long ttl, TimeUnit timeUnit){
        client.getMapCache(group).put(key, object, ttl, timeUnit);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String group, String key){
        return (T)client.getMap(group).get(key);
    }

    @SuppressWarnings("unchecked")
    public <T> T getTTL(String group, String key){
        return (T)client.getMapCache(group).get(key);
    }

    public <K, V> Map<K, V> getAllGroup(String group){
        return client.getMap(group);
    }

    public <K, V> Map<K, V> getAllTTL(String group){
        return client.getMapCache(group);
    }

    public void remove(String group, String key){
        client.getMap(group).remove(key);
    }

    public void removeTTL(String group, String key){
        client.getMapCache(group).remove(key);
    }
}
