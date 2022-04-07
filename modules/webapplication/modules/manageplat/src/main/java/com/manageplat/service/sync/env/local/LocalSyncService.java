package com.manageplat.service.sync.env.local;

import com.manageplat.service.sync.SyncService;
import com.manageplat.service.sync.SyncServiceFactory;

public class LocalSyncService extends SyncService {

    public LocalSyncService(){
        this.setDownloadService(SyncServiceFactory.getLocalSftpService());
        this.setUploadService(SyncServiceFactory.getLocalSftpService());
        this.setShellService(SyncServiceFactory.getLocalShellService());
    }
}