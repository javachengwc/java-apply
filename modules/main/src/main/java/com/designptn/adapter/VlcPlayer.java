package com.designptn.adapter;

//音乐播放器
public class VlcPlayer implements AudioPlayer {

    @Override
    public void playVlc(String fileName) {
        System.out.println("playVlc fileName="+ fileName);
    }

    @Override
    public void playMp4(String fileName) {
    }
}

