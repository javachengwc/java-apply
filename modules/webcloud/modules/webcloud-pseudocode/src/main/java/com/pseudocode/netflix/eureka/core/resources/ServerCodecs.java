package com.pseudocode.netflix.eureka.core.resources;

import com.pseudocode.netflix.eureka.client.appinfo.EurekaAccept;
import com.pseudocode.netflix.eureka.core.registry.Key;

public interface ServerCodecs {

    CodecWrapper getFullJsonCodec();

    CodecWrapper getCompactJsonCodec();

    CodecWrapper getFullXmlCodec();

    CodecWrapper getCompactXmlCodecr();

    EncoderWrapper getEncoder(Key.KeyType keyType, boolean compact);

    EncoderWrapper getEncoder(Key.KeyType keyType, EurekaAccept eurekaAccept);
}

