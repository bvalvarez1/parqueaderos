package ec.loja.config;

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
            createCache(cm, ec.loja.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, ec.loja.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, ec.loja.domain.User.class.getName());
            createCache(cm, ec.loja.domain.Authority.class.getName());
            createCache(cm, ec.loja.domain.User.class.getName() + ".authorities");
            createCache(cm, ec.loja.domain.Catalogue.class.getName());
            createCache(cm, ec.loja.domain.Catalogue.class.getName() + ".items");
            createCache(cm, ec.loja.domain.ItemCatalogue.class.getName());
            createCache(cm, ec.loja.domain.Institution.class.getName());
            createCache(cm, ec.loja.domain.SystemParameters.class.getName());
            createCache(cm, ec.loja.domain.SystemParameterInstitution.class.getName());
            createCache(cm, ec.loja.domain.Person.class.getName());
            createCache(cm, ec.loja.domain.Functionality.class.getName());
            createCache(cm, ec.loja.domain.Functionality.class.getName() + ".childrens");
            createCache(cm, ec.loja.domain.AuthorityFunctionality.class.getName());
            createCache(cm, ec.loja.domain.Contract.class.getName());
            createCache(cm, ec.loja.domain.Contract.class.getName() + ".horaries");
            createCache(cm, ec.loja.domain.Horary.class.getName());
            createCache(cm, ec.loja.domain.Record.class.getName());
            createCache(cm, ec.loja.domain.Tariff.class.getName());
            createCache(cm, ec.loja.domain.Tariff.class.getName() + ".tarifs");
            createCache(cm, ec.loja.domain.TariffVehicleType.class.getName());
            createCache(cm, ec.loja.domain.UserAuthority.class.getName());
            createCache(cm, ec.loja.domain.UserAuthorityInstitution.class.getName());
            createCache(cm, ec.loja.domain.Receipt.class.getName());
            createCache(cm, ec.loja.domain.RecordTicket.class.getName());
            createCache(cm, ec.loja.domain.Institution.class.getName() + ".places");
            createCache(cm, ec.loja.domain.Place.class.getName());
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
