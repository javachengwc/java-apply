package com.designptn.adapter;

//媒体播放适配器
public class MediaAdapter implements MediaPlayer {

    private AudioPlayer audioPlayer;

    public MediaAdapter(String audioType){
        if(audioType.equalsIgnoreCase("vlc") ){
            audioPlayer = new VlcPlayer();
        } else if (audioType.equalsIgnoreCase("mp4")){
            audioPlayer = new Mp4Player();
        }
    }

    public void play(String audioType, String fileName) {
        if(audioType.equalsIgnoreCase("vlc")){
            audioPlayer.playVlc(fileName);
        }else if(audioType.equalsIgnoreCase("mp4")){
            audioPlayer.playMp4(fileName);
        }
    }
}