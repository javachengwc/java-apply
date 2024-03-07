package com.otd.boot.plat.service;

import com.otd.boot.plat.model.entity.Storage;

public interface StorageService {

    Storage queryByStorageCode(String storageCode);
}
