package com.easy1staking.jpgstore.jpgstorejavaclient.config;

import com.bloxbean.cardano.client.plutus.spec.*;
import com.bloxbean.cardano.client.plutus.spec.serializers.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(ConstrPlutusData.class, new ConstrDataJsonSerializer());
        module.addSerializer(BigIntPlutusData.class,new BigIntDataJsonSerializer());
        module.addSerializer(BytesPlutusData.class,new BytesDataJsonSerializer());
        module.addSerializer(ListPlutusData.class,new ListDataJsonSerializer());
        module.addSerializer(MapPlutusData.class,new MapDataJsonSerializer());
        objectMapper.registerModule(module);
        return objectMapper;
    }


}
