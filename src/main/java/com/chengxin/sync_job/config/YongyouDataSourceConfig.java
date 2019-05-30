package com.chengxin.sync_job.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Mr.Song
 * @create 2019-05-22 15:55
 * @desc 用友数据源配置
 **/
@Configuration
@MapperScan(basePackages = "com.chengxin.sync_job.dao.yongyou", sqlSessionTemplateRef  = "yongyouSqlSessionTemplate")
public class YongyouDataSourceConfig {
    @Bean(name = "yongyouDataSource")
    @ConfigurationProperties(prefix="spring.datasource.yongyou")
    public DataSource primaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "yongyouSqlSessionFactory")
    public SqlSessionFactory yongyouSqlSessionFactory(@Qualifier("yongyouDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:**/mapper/yongyou/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "yongyouTransactionManager")
    public DataSourceTransactionManager yongyouTransactionManager(@Qualifier("yongyouDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "yongyouSqlSessionTemplate")
    public SqlSessionTemplate yongyouSqlSessionTemplate(@Qualifier("yongyouSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
