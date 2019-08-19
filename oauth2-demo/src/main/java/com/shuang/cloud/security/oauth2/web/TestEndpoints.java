package com.shuang.cloud.security.oauth2.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.store.redis.JdkSerializationStrategy;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStoreSerializationStrategy;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


 /**
  * @Author shuangyc
  * @Email: shuangyuan.chen.ext@nokia-sbell.com
  * @Date 2019/8/17 12:07
  * @Classname TestEndpoints
  **/
@RestController
public class TestEndpoints {
    private static final Logger logger = LoggerFactory.getLogger(TestEndpoints.class);

    @Autowired
    private RedisTemplate redisTemplate;
    private static final String ACCESS = "access:";
    private static final String AUTH_TO_ACCESS = "auth_to_access:";
    private static final String AUTH = "auth:";
    private static final String REFRESH_AUTH = "refresh_auth:";
    private static final String ACCESS_TO_REFRESH = "access_to_refresh:";
    private static final String REFRESH = "refresh:";
    private static final String REFRESH_TO_ACCESS = "refresh_to_access:";
    private static final String CLIENT_ID_TO_ACCESS = "client_id_to_access:";
    private static final String UNAME_TO_ACCESS = "uname_to_access:";
    private RedisTokenStoreSerializationStrategy serializationStrategy = new JdkSerializationStrategy();
    private byte[] serializedKey;

    @GetMapping("/product/{id}")
    public String getProduct(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "product id : " + id;
    }

    @GetMapping("/order/{id}")
    public String getOrder(@PathVariable String id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "order id : " + id;
    }

    @GetMapping("/deserialize/access")
    @ResponseBody
    public Object getAccess(@RequestParam String key) {

        serializedKey = serializationStrategy.serialize(ACCESS + key);
        RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
        byte[] bytes;
        try {
            bytes = conn.get(serializedKey);
        } finally {
            conn.close();
        }
        return serializationStrategy.deserialize(bytes, OAuth2AccessToken.class);
    }
    @GetMapping("/deserialize/auth_to_access")
    @ResponseBody
    public Object getAuth_to_access(@RequestParam String key) {
        serializedKey = serializationStrategy.serialize(AUTH_TO_ACCESS + key);
        RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
        byte[] bytes;
        try {
            bytes = conn.get(serializedKey);
        } finally {
            conn.close();
        }
        return serializationStrategy.deserialize(bytes, OAuth2AccessToken.class);
    }
    @GetMapping("/deserialize/client_id_to_access")
    @ResponseBody
    public Object getClient_id_to_access(@RequestParam String key) {
        serializedKey = serializationStrategy.serialize(CLIENT_ID_TO_ACCESS + key);
        RedisConnection conn = redisTemplate.getConnectionFactory().getConnection();
        byte[] bytes;
        List list = new ArrayList<byte[]>();
        List list2 = new ArrayList<OAuth2AccessToken>();
        try {
            Long size = conn.lLen(serializedKey);
            logger.info("size : {}",size);
            list = conn.lRange(serializedKey,0,size);
        } finally {
            conn.close();
        }
        list.forEach( b->{
            list2.add(serializationStrategy.deserialize((byte[]) b, OAuth2AccessToken.class));
        });
        return list2;
    }



}
