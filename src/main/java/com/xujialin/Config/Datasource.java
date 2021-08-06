package com.xujialin.Config;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author XuJiaLin
 * @date 2021/8/3 19:34
 */
@Configuration
public class Datasource {

    /**
     * 配置SQL数据源
     * @return
     * @throws SQLException
     */
    @ConfigurationProperties("spring.datasource")
    @Bean
    public DataSource dataSource() throws SQLException {
        return new DruidDataSource();
    }

}
