package com.boothu.service;

import com.boothu.model.vo.DataVo;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DataService {

    List<DataVo> addData(MultipartFile file);
}
