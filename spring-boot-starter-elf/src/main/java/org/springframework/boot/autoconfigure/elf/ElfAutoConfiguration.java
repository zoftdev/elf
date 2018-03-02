package org.springframework.boot.autoconfigure.elf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ElfProperties.class) // 
public class ElfAutoConfiguration {

    @Autowired ElfProperties elfProperties;

    String name="x";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Bean
    public Tmp test(){
        logger.info("name: {} elfpropname {}",name,elfProperties.getName());
        return new Tmp();
    }


    public static class Tmp{

    }

}
