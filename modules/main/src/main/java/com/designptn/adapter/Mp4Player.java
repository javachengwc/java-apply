package com.designptn.adapter;

//mp4播放器
public class Mp4Player implements AudioPlayer {

    @Override
    public void playVlc(String fileName) {
    }

    @Override
    public void playMp4(String fileName) {
        System.out.println("playMp4 fileName="+ fileName);
    }
}