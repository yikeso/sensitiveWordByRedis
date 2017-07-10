package com.china.example.config;

import com.github.pagehelper.PageHelper;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Created by kakasun on 2017/7/10.
 */
@MapperScan(basePackages = "com.com.china.*.dao.mapper")
public class MybatisConfig {
    @Autowired
    private Environment env;//springboot 的环境变量

    @Bean
    @Primary
    public DataSource dataSource() throws Exception {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass(env.getProperty("jdbc.driverClassName"));
        dataSource.setJdbcUrl(env.getProperty("jdbc.url"));
        dataSource.setUser(env.getProperty("jdbc.username"));
        dataSource.setPassword(env.getProperty("jdbc.password"));
        dataSource.setMaxPoolSize(Integer.parseInt(env.getProperty("datasource.max-active")));
        dataSource.setMaxIdleTime(Integer.parseInt(env.getProperty("datasource.max-idle")));
        dataSource.setInitialPoolSize(Integer.parseInt(env.getProperty("datasource.initial-size")));
        return dataSource;
    }

    /**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource ds) throws Exception {
        SqlSessionFactoryBean fb = new SqlSessionFactoryBean();
        fb.setDataSource(ds);// 指定数据源(这个必须有，否则报错)
        // 下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
/*        fb.setTypeAliasesPackage(env.getProperty("mybatis.typeAliasesPackage"));// 指定基包
        fb.setMapperLocations(
                new PathMatchingResourcePatternResolver().getResources(env.getProperty("mybatis.mapperLocations")));*/
        PageHelper pageHelper = new PageHelper();
        Properties prop = new Properties();
        prop.setProperty("offsetAsPageNum", "true");
        prop.setProperty("rowBoundsWithCount", "true");
        prop.setProperty("reasonable", "true");
        pageHelper.setProperties(prop);
        fb.setPlugins(new Interceptor[]{pageHelper});
        return fb.getObject();
    }

    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }
}
