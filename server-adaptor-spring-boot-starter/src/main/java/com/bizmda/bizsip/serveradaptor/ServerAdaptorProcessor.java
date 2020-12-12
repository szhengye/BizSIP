package com.bizmda.bizsip.serveradaptor;

import com.bizmda.bizsip.common.BizException;
import com.bizmda.bizsip.common.BizResultEnum;
import com.bizmda.bizsip.config.AbstractServerAdaptorConfig;
import com.bizmda.bizsip.config.BizSipConfig;
import com.bizmda.bizsip.message.AbstractMessageProcessor;
import com.bizmda.bizsip.serveradaptor.config.ServerAdaptorConfig;
import com.bizmda.bizsip.serveradaptor.protocol.AbstractServerProtocolProcessor;

public class ServerAdaptorProcessor {

    private AbstractMessageProcessor messageProcessor;
    private AbstractServerProtocolProcessor protocolProcessor;

    public ServerAdaptorProcessor(AbstractServerAdaptorConfig serverAdaptorConfig) throws BizException {
        String messageType = serverAdaptorConfig.getMessageType();
        String clazzName = BizSipConfig.messageTypeMap.get(messageType);
        if (clazzName == null) {
            throw new BizException(BizResultEnum.SERVER_NO_MESSAGE_PROCESSOR_ERROR);
        }

        try {
            this.messageProcessor = (AbstractMessageProcessor)Class.forName(clazzName).newInstance();
        } catch (InstantiationException e) {
            throw new BizException(BizResultEnum.SERVER_MESSAGE_CREATE_ERROR,e);
        } catch (IllegalAccessException e) {
            throw new BizException(BizResultEnum.SERVER_MESSAGE_CREATE_ERROR,e);
        } catch (ClassNotFoundException e) {
            throw new BizException(BizResultEnum.SERVER_MESSAGE_CREATE_ERROR,e);
        }

        this.messageProcessor.init(serverAdaptorConfig);

        String protocolType = serverAdaptorConfig.getProtocol().getType();

        clazzName = BizSipConfig.protocolTypeMap.get(protocolType);
        if (clazzName == null) {
            throw new BizException(BizResultEnum.SERVER_NO_PROTOCOL_PROCESSOR_ERROR);
        }

        try {
            this.protocolProcessor = (AbstractServerProtocolProcessor)Class.forName(clazzName).newInstance();
        } catch (InstantiationException e) {
            throw new BizException(BizResultEnum.SERVER_PROTOCOL_CREATE_ERROR,e);
        } catch (IllegalAccessException e) {
            throw new BizException(BizResultEnum.SERVER_PROTOCOL_CREATE_ERROR,e);
        } catch (ClassNotFoundException e) {
            throw new BizException(BizResultEnum.SERVER_PROTOCOL_CREATE_ERROR,e);
        }

        this.protocolProcessor.init(serverAdaptorConfig);
    }

    public Object process(Object inMessage) throws BizException {
        Object message = this.messageProcessor.pack(inMessage);
        message = this.protocolProcessor.process(message);
        message = this.messageProcessor.unpack(message);
        return message;
    }

//    public AbstractServerAdaptorConfig getServerAdaptorConfigById(String serverId) {
//        if (this.serverAdaptorConfigMapping == null) {
//            this.serverAdaptorConfigMapping = new ServerAdaptorConfigMapping(this.configPath);
//        }
//        return this.serverAdaptorConfigMapping.getServerAdaptorConfig(serverId);
//    }
}
