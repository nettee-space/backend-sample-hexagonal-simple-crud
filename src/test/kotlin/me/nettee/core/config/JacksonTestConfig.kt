package me.nettee.core.config

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter

@TestConfiguration
class JacksonTestConfig {
    @Bean
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
                .registerModule(JavaTimeModule())
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
    }

    @Bean
    fun mappingJackson2HttpMessageConverter(objectMapper: ObjectMapper): MappingJackson2HttpMessageConverter {
        return MappingJackson2HttpMessageConverter(objectMapper)
    }
}