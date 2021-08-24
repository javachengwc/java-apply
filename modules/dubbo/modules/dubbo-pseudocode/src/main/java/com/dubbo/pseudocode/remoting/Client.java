package com.dubbo.pseudocode.remoting;

import org.apache.dubbo.common.Resetable;
import org.apache.dubbo.remoting.Channel;
import org.apache.dubbo.remoting.Endpoint;
import org.apache.dubbo.remoting.IdleSensible;
import org.apache.dubbo.remoting.RemotingException;

public interface Client extends Endpoint, Channel, Resetable, IdleSensible {

    void reconnect() throws RemotingException;

    @Deprecated
    void reset(org.apache.dubbo.common.Parameters parameters);
}
