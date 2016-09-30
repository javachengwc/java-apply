package com.mountain.zookeeper;

import org.apache.zookeeper.Watcher;

public interface StateListener {

   public void stateChanged(Watcher.Event.KeeperState state);

   public void reconnected();
}
