package br.com.profectum.configs;

import java.util.Arrays;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Wendel Ferreira de Mesquita
 * Após o início da construção do front-end, logo no consumo do cliente, foi visto que o mesmo acessava a base de dados repetidas vezes em um curto
 * espaço de tempo de forma desnecessária. Uma persistência deste tipo é necessária quando há um serviço de informação em tempo real
 * como um dashboard com uma time line, ou um sistema de notificações. Nesta aplicação, os dados não sofrem grandes alterações em um curto espaço de
 * tempo, logo a multipla atualização em um espaço de tempo curti não se vê necessário. Então, para solucionar este problema, foi usada a biblioteca
 * Cacheable do Spring Framework, visando diminuir o impacto na base de dados, e para deixar o acesso mais fluido.
 */

@Configuration
@EnableCaching
public class CacheableConfig {
	@Bean
    public CacheManager cacheManager() {
        SimpleCacheManager cacheManager = new SimpleCacheManager();
        cacheManager.setCaches(Arrays.asList( 
          new ConcurrentMapCache("disciplinas")));
        return cacheManager;
    }
}
