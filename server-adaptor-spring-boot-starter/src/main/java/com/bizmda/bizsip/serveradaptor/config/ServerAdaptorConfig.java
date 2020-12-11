package com.bizmda.bizsip.serveradaptor.config;

import com.bizmda.bizsip.common.BizException;
import com.bizmda.bizsip.config.AbstractServerAdaptorConfig;
import com.bizmda.bizsip.config.ServerAdaptorConfigMapping;
import com.bizmda.bizsip.serveradaptor.ServerAdaptorProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ServerAdaptorConfig {
    @Value("${bizsip.config-path}")
    private String configPath;

    @Bean
    @ConditionalOnProperty(name = "bizsip.config-path", matchIfMissing = false)
    public ServerAdaptorConfigMapping serverAdaptorMapping() {
        return new ServerAdaptorConfigMapping(this.configPath);
    }

    @Autowired
    private ServerAdaptorConfigMapping serverAdaptorConfigMapping;

    public AbstractServerAdaptorConfig getServerAdaptorConfig(String serverId) {
        return this.serverAdaptorConfigMapping.getServerAdaptorConfig(serverId);
    }

    public ServerAdaptorProcessor getServerAdaptorProcessor(String serverId) throws BizException {
        return new ServerAdaptorProcessor(this.serverAdaptorConfigMapping.getServerAdaptorConfig(serverId));
    }
}
