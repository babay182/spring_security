package web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@ComponentScan("web")
@PropertySource("classpath:db.properties")
@EnableTransactionManagement
@EnableWebMvc
public class SpringConfig implements WebMvcConfigurer {

    private final ApplicationContext applicationContext;

    @Autowired
    public SpringConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Autowired
    private Environment env;

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("db.driver"));
        dataSource.setUrl(env.getProperty("db.url"));
        dataSource.setUsername(env.getProperty("db.username"));
        dataSource.setPassword(env.getProperty("db.password"));
        return dataSource;
    }

    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(applicationContext);
        templateResolver.setPrefix("/WEB-INF/pages/");
        templateResolver.setSuffix(".html");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());
        templateEngine.setEnableSpringELCompiler(true);
        return templateEngine;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        registry.viewResolver(resolver);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean getEntityManager(){
        System.out.println("Entity manager initializing");
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(getDataSource());


        Properties prop = new Properties();
        prop.setProperty("hibernate.connection.useUnicode", "true");
        prop.setProperty("hibernate.connection.characterEncoding", "UTF-8");
        prop.setProperty("hibernate.connection.charSet", "UTF-8");
        prop.setProperty("hibernate.use_nationalized_character_data", "true");
        prop.setProperty("dialect", "org.hibernate.dialect.MySQL5Dialect");
        prop.setProperty("hibernate.hbm2ddl.auto", "create");
        prop.setProperty("hibernate.show_sql", "true");
        prop.setProperty("hibernate.format_sql", "false");
        prop.setProperty("hibernate.generate_statistics", "false");
        prop.setProperty("hibernate.use_sql_comments", "false");
        prop.setProperty("connection.provider_class", "org.hibernate.service.jdbc.connections.internal.C3P0ConnectionProvider");
        prop.setProperty("hibernate.c3p0.min_size", "5");
        prop.setProperty("hibernate.c3p0.max_size", "20");
        prop.setProperty("hibernate.c3p0.acquire_increment", "5");
        prop.setProperty("hibernate.c3p0.timeout", "1800");
        em.setJpaProperties(prop);

        em.setPackagesToScan("ru/model");

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        return em;

       /* LocalContainerEntityManagerFactoryBean localContainerEntityManagerFactoryBean =
                new LocalContainerEntityManagerFactoryBean();
        localContainerEntityManagerFactoryBean.setDataSource(getDataSource());
        localContainerEntityManagerFactoryBean.setPackagesToScan("ru/model");
        localContainerEntityManagerFactoryBean.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        localContainerEntityManagerFactoryBean.setJpaProperties(getHibernateProperties());

        return localContainerEntityManagerFactoryBean;*/
    }

    public Properties getHibernateProperties(){

        try {
            Properties properties = new Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties");
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException("no db.properties", e);
        }
    }

    @Bean
    public JpaTransactionManager getJpaTransactionManager(){
        /*JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        Properties props=new Properties();
        props.put("entityManagerFactory", getEntityManager());
        jpaTransactionManager.setJpaProperties(props);
        return jpaTransactionManager;*/

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(getEntityManager().getObject());

        return transactionManager;
    }
}