package com.template.monolith.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories
public class RedisConfig {
	
	@Value("${redis.password}")
	private String redisPassword;
	
	@Bean
	public JedisConnectionFactory connectionFactory() {
		JedisConnectionFactory jedis = new JedisConnectionFactory();
		jedis.setHostName("localhost");
		jedis.setPort(6379);
		jedis.setPassword(redisPassword);
		return jedis;
	}
	
	@Bean
	RedisTemplate<Object, Object> redisTemplate(){
		RedisTemplate<Object, Object> redis = new RedisTemplate<>();
		redis.setConnectionFactory(connectionFactory());
		return redis;
	}
}
