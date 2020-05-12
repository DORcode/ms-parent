package com.coin.msdict.config.dict;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.cache.support.AbstractCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.AbstractDataSource;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @ClassName DataSourceConfig
 * @Description: TODO
 * @Author kh
 * @Date 2020/5/7 8:20
 * @Version V1.0
 **/
@Component
public class DictDataSourceConfig {
    // 数据库jdbctemplate
    public static final ConcurrentMap<String, JdbcTemplate> JDBCSMAP = new ConcurrentHashMap<>();

    public static final ConcurrentMap<String, DataSource> SOURCEMAP = new ConcurrentHashMap<>();

    @Bean(name = "dsLocalorg")
    @ConfigurationProperties(prefix = "spring.datasource.local")
    public DataSource localorgDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean("localorgJdbcTemplate")
    public JdbcTemplate localorgJdbcTemplate(@Qualifier("dsLocalorg") DataSource dataSource) {
        SOURCEMAP.put("localOrg", dataSource);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        JDBCSMAP.put("localOrg", jdbcTemplate);
        return jdbcTemplate;
    }

//    @Bean
//    public void map(@Qualifier("localorgJdbcTemplate") JdbcTemplate jdbcTemplate) {
//        JDBCSMAP.put("localOrg", jdbcTemplate);
//    }

    public void init(String key, Map<String, String> map) {
        HikariDataSource source = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(map.get("driver"))
                .url(map.get("url"))
                .username(map.get("username"))
                .password(map.get("password"))
                .build();

        JdbcTemplate jdbcTemplate = new JdbcTemplate(source);

        close(key);

        SOURCEMAP.put(key, source);
        JDBCSMAP.put(key, jdbcTemplate);
    }

    public JdbcTemplate getJdbcTemplate(String key) {
        return JDBCSMAP.get(key);
    }

    public void remove(String key) {
        SOURCEMAP.remove(key);
        JDBCSMAP.remove(key);
    }

    public void close(String key) {
        try {
            SOURCEMAP.get(key).getConnection().close();
            JDBCSMAP.get(key).getDataSource().getConnection().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
