package org.smigo.config;

import com.jolbox.bonecp.BoneCPDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import javax.sql.DataSource;

@Configuration
@DevProfile
public class DevelopmentConfiguration {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Bean(name = "dataSource")
    public DataSource getDataSource() {
        log.debug("Get BoneCPDataSource from profile dev");
        BoneCPDataSource boneCPDataSource = new BoneCPDataSource();
        boneCPDataSource.setDriverClass("com.mysql.jdbc.Driver");
        boneCPDataSource.setJdbcUrl("jdbc:mysql://smigo.org/nosslin2_dbtest");
        boneCPDataSource.setUsername("nosslin2_dbusert");
        boneCPDataSource.setPassword("To4[n=GGkp2l");
        boneCPDataSource.setIdleConnectionTestPeriodInMinutes(5);
        boneCPDataSource.setIdleMaxAgeInMinutes(5);
        boneCPDataSource.setMinConnectionsPerPartition(1);
        boneCPDataSource.setPartitionCount(1);
        boneCPDataSource.setAcquireIncrement(1);
        boneCPDataSource.setStatementsCacheSize(100);
        boneCPDataSource.setMaxConnectionsPerPartition(2);
        return boneCPDataSource;
    }

    @Bean
    public HostEnvironmentInfo hostEnvironmentInfo() {
        return new HostEnvironmentInfo(EnvironmentProfile.DEVELOPMENT, true, "/home/nosslin2/");
    }

    @Bean
    public MessageSource messageSource() {
        log.debug("getMessageSource");
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("messages", "classpath:messages");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(1);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


}
