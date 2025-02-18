package me.nettee.core.jpa.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@Configuration
@EnableJpaAuditing
@EntityScan(basePackages = "me.nettee")
@EnableJpaRepositories(basePackages = "me.nettee")
public class JpaConfig {

}
