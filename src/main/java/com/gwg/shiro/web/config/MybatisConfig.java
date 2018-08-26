package com.gwg.shiro.web.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import tk.mybatis.spring.mapper.MapperScannerConfigurer;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * MyBatis基础配置
 * @author Administrator
 *
 */
@Configuration
public class MybatisConfig {

	private static final Logger logger = LoggerFactory.getLogger(MybatisConfig.class);


	/**
	 * 读取资源文件
	 */
	@Bean
	@Order(Integer.MIN_VALUE)//优先执行
	public PropertyPlaceholderConfigurer propertyPlaceholderConfigurer(){
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer.setLocation(new ClassPathResource("config.properties"));
		return propertyPlaceholderConfigurer;
	}

    /**
     * 插件配置
     */
    @Bean
    public Interceptor[] pageInterceptor(){
        PageInterceptor[] interceptorArr = new PageInterceptor[1];
        //分页插件配置
        PageInterceptor pageInterceptor = new PageInterceptor();
        Properties properties = new Properties();
        properties.setProperty("helperDialect", "mysql");
        pageInterceptor.setProperties(properties);
        interceptorArr[0] = pageInterceptor;
        return interceptorArr;
    }


	/**
	 * 生成一个名字为 sqlSessionFactory 的bean
	 * mybatis的sqlSessionFactory配置
	 * @param dataSource
	 */
	@Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception{
    	SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
    	bean.setDataSource(dataSource);
    	bean.setTypeAliasesPackage("com.gwg.shiro.web.model");
    	bean.setPlugins(pageInterceptor());
    	ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    	try {
			bean.setMapperLocations(resolver.getResources("classpath*:com/gwg/shiro/web/mapper/*.xml"));
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return bean.getObject();
    }


 /*   @Bean
    public SqlSessionTemplate getSqlSessionTemplate(SqlSessionFactory sqlSessionFactory){
    	return new SqlSessionTemplate(sqlSessionFactory);
    }
    */

	/**
	 * 生成一个名字为mapperScannerConfigurer的bean
	 * mapper接口扫描包
	 */
    @Bean
    public MapperScannerConfigurer mapperScannerConfigurer(){
		MapperScannerConfigurer configurer = new MapperScannerConfigurer();
		configurer.setSqlSessionFactoryBeanName("sqlSessionFactory");
		configurer.setBasePackage("com.gwg.shiro.web.mapper");
		return configurer;
	}


}
