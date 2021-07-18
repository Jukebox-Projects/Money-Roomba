package com.moneyroomba.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cache.JCacheManagerCustomizer;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.*;
import tech.jhipster.config.JHipsterProperties;
import tech.jhipster.config.cache.PrefixedKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private GitProperties gitProperties;
    private BuildProperties buildProperties;
    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        JHipsterProperties.Cache.Ehcache ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration =
            Eh107Configuration.fromEhcacheCacheConfiguration(
                CacheConfigurationBuilder
                    .newCacheConfigurationBuilder(Object.class, Object.class, ResourcePoolsBuilder.heap(ehcache.getMaxEntries()))
                    .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                    .build()
            );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, com.moneyroomba.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, com.moneyroomba.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, com.moneyroomba.domain.User.class.getName());
            createCache(cm, com.moneyroomba.domain.Authority.class.getName());
            createCache(cm, com.moneyroomba.domain.User.class.getName() + ".authorities");
            createCache(cm, com.moneyroomba.domain.PersistentToken.class.getName());
            createCache(cm, com.moneyroomba.domain.User.class.getName() + ".persistentTokens");
            createCache(cm, com.moneyroomba.domain.UserDetails.class.getName());
            createCache(cm, com.moneyroomba.domain.UserDetails.class.getName() + ".wallets");
            createCache(cm, com.moneyroomba.domain.UserDetails.class.getName() + ".categories");
            createCache(cm, com.moneyroomba.domain.UserDetails.class.getName() + ".events");
            createCache(cm, com.moneyroomba.domain.UserDetails.class.getName() + ".transactions");
            createCache(cm, com.moneyroomba.domain.UserDetails.class.getName() + ".userDetails");
            createCache(cm, com.moneyroomba.domain.UserDetails.class.getName() + ".targetContacts");
            createCache(cm, com.moneyroomba.domain.UserDetails.class.getName() + ".sourceContacts");
            createCache(cm, com.moneyroomba.domain.Wallet.class.getName());
            createCache(cm, com.moneyroomba.domain.Wallet.class.getName() + ".transactions");
            createCache(cm, com.moneyroomba.domain.Transaction.class.getName());
            createCache(cm, com.moneyroomba.domain.ScheduledTransaction.class.getName());
            createCache(cm, com.moneyroomba.domain.ScheduledTransaction.class.getName() + ".schedulePatterns");
            createCache(cm, com.moneyroomba.domain.SchedulePattern.class.getName());
            createCache(cm, com.moneyroomba.domain.Attachment.class.getName());
            createCache(cm, com.moneyroomba.domain.Category.class.getName());
            createCache(cm, com.moneyroomba.domain.Category.class.getName() + ".categories");
            createCache(cm, com.moneyroomba.domain.Category.class.getName() + ".transactions");
            createCache(cm, com.moneyroomba.domain.Invoice.class.getName());
            createCache(cm, com.moneyroomba.domain.License.class.getName());
            createCache(cm, com.moneyroomba.domain.Currency.class.getName());
            createCache(cm, com.moneyroomba.domain.Currency.class.getName() + ".transactions");
            createCache(cm, com.moneyroomba.domain.Currency.class.getName() + ".scheduledTransactions");
            createCache(cm, com.moneyroomba.domain.Currency.class.getName() + ".wallets");
            createCache(cm, com.moneyroomba.domain.Currency.class.getName() + ".invoices");
            createCache(cm, com.moneyroomba.domain.Notification.class.getName());
            createCache(cm, com.moneyroomba.domain.Event.class.getName());
            createCache(cm, com.moneyroomba.domain.SystemSetting.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }

    @Autowired(required = false)
    public void setGitProperties(GitProperties gitProperties) {
        this.gitProperties = gitProperties;
    }

    @Autowired(required = false)
    public void setBuildProperties(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return new PrefixedKeyGenerator(this.gitProperties, this.buildProperties);
    }
}
