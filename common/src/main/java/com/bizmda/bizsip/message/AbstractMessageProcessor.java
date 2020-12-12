package com.bizmda.bizsip.message;

import com.bizmda.bizsip.common.BizException;
import com.bizmda.bizsip.config.AbstractServerAdaptorConfig;

public abstract class AbstractMessageProcessor {
    private AbstractServerAdaptorConfig serverAdaptorConfig;
    public abstract Object pack(Object inMessage) throws BizException;
    public abstract Object unpack(Object inMessage) throws BizException;

    public void init(AbstractServerAdaptorConfig serverAdaptorConfig) throws BizException {
        this.serverAdaptorConfig = serverAdaptorConfig;
    }
}
