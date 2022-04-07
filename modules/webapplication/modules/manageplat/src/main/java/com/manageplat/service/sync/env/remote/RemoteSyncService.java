package com.manageplat.service.sync.env.remote;

import com.manageplat.service.sync.SyncService;
import com.manageplat.service.sync.SyncServiceFactory;

public class RemoteSyncService extends SyncService {

    public RemoteSyncService() {
        this.setDownloadService(SyncServiceFactory.getLocalSftpService());
        this.setUploadService(SyncServiceFactory.getRemoteSftpService());
        this.setShellService(SyncServiceFactory.getRemoteShellService());
    }
}