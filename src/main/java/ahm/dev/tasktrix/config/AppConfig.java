package ahm.dev.tasktrix.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableTransactionManagement
@EnableConfigurationProperties({JwtProperties.class, CorsProperties.class})
public class AppConfig {

}
