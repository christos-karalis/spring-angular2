package com.neurocom.crud.config;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.neurocom.crud.domain.User;
import com.neurocom.crud.repository.UserRepository;
import com.neurocom.crud.search.AdvancedSearchController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.spi.EvaluationContextExtension;
import org.springframework.data.repository.query.spi.EvaluationContextExtensionSupport;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.data.util.AnnotatedTypeScanner;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Comparator;
import java.util.Properties;

@Configuration
@Import(RepositoryRestMvcConfiguration.class)
@EnableJpaRepositories(basePackages = "com.neurocom.crud.repository")
@ComponentScan(basePackages = {"com.neurocom.crud.search", "com.neurocom.crud.handler"})
@EnableTransactionManagement
public class RestDataConfig  extends RepositoryRestMvcConfiguration {

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.import_files", "initial_data.sql");
        properties.setProperty("hibernate.hbm2ddl.auto", "create");
        return properties;
    }

    @Bean
    public DataSource dataSource() {
        EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
        return builder.setType(EmbeddedDatabaseType.DERBY).build();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setShowSql(true);
        vendorAdapter.setGenerateDdl(false);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("com.neurocom.crud.domain");
        factory.setDataSource(dataSource());

        Properties jpaProperties = hibernateProperties();
        factory.setJpaProperties(jpaProperties);

        factory.afterPropertiesSet();


        return factory.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {

        JpaTransactionManager txManager = new JpaTransactionManager();
        txManager.setEntityManagerFactory(entityManagerFactory());
        return txManager;
    }


    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {

        return new RepositoryRestConfigurerAdapter() {

            @Override
            public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
                config.exposeIdsFor(new AnnotatedTypeScanner(Entity.class).
                        findTypes("com.neurocom.crud.domain").toArray(new Class[0]));
                config.setMaxPageSize(25);
//                Comparator c = Comparator.comparing(User::getUsername);
//                config.withEntityLookup().//
//                        forRepository(UserRepository.class)
//                        .withIdMapping(User::getUsername);
            }

            @Override
            public void configureJacksonObjectMapper(ObjectMapper objectMapper) {
                objectMapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            }


        };
    }

//    @Override
//    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
//
//        config.withCustomEntityLookup().//
//                forRepository(UserRepository.class, User::getUsername, UserRepository::findByUsername);
//    }

    @Bean
    EvaluationContextExtension securityExtension() {
        return new EvaluationContextExtensionSupport() {
            @Override
            public String getExtensionId() {
                return "security";
            }

            @Override
            public SecurityExpressionRoot getRootObject() {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                return new SecurityExpressionRoot(authentication) {};
            }
        };
    }

}