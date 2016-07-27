package org.apache.diana.redis.key;

import com.google.gson.Gson;
import org.apache.diana.api.key.BucketManager;
import org.apache.diana.api.key.BucketManagerFactory;
import redis.clients.jedis.JedisPool;

import java.util.*;

class RedisKeyValueEntityManagerFactory implements BucketManagerFactory {

    private final JedisPool jedisPool;

    RedisKeyValueEntityManagerFactory(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }


    @Override
    public BucketManager getBucketManager(String bucketName) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        return new RedisKeyValueEntityManager(bucketName, new Gson(), jedisPool.getResource());
    }

    @Override
    public <T> List<T> getList(String bucketName, Class<T> clazz) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(clazz, "Class type is required");
        return new RedisList<T>(jedisPool.getResource(), clazz, bucketName);
    }

    @Override
    public <T> Set<T> getSet(String bucketName, Class<T> clazz) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(clazz, "Class type is required");
        return new RedisSet<T>(jedisPool.getResource(), clazz, bucketName);
    }

    @Override
    public <T> Queue<T> getQueue(String bucketName, Class<T> clazz) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(clazz, "Class type is required");
        return new RedisQueue<T>(jedisPool.getResource(), clazz, bucketName);
    }

    @Override
    public <K, V> Map<K, V> getMap(String bucketName, Class<K> keyValue, Class<V> valueValue) {
        Objects.requireNonNull(bucketName, "bucket name is required");
        Objects.requireNonNull(valueValue, "Class type is required");
        return new RedisMap<>(jedisPool.getResource(), keyValue, valueValue, bucketName);
    }

    @Override
    public void close() throws Exception {
        jedisPool.close();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("RedisKeyValueEntityManagerFactory{");
        sb.append("jedisPool=").append(jedisPool);
        sb.append('}');
        return sb.toString();
    }
}