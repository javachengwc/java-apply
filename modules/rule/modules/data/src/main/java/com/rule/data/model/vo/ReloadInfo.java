package com.rule.data.model.vo;

import com.rule.data.model.SerService;

import java.util.Map;

public class ReloadInfo implements Comparable<ReloadInfo> {

    public long lastAcTime;

    public SerService po;

    public Map<String, Object> param;

    public long latency;

    public long createtime = 0;

    public ReloadInfo(long lastAcTime, SerService po, Map<String, Object> param, long latency, long createtime) {
        this.lastAcTime = lastAcTime;
        this.po = po;
        this.param = param;
        this.latency = latency;
        this.createtime = createtime;
    }

    @Override
    public int compareTo(ReloadInfo o) {
        
        long tmp = this.lastAcTime - o.lastAcTime;

        if (tmp > 0) {
            return -1;
        } else if (tmp == 0) {
            return 0;
        } else {
            return 1;
        }
    }
}
