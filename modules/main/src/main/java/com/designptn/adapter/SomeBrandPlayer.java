package com.designptn.adapter;

public class SomeBrandPlayer implements MediaPlayer {

    private MediaAdapter mediaAdapter;

    @Override
    public void play(String audioType, String fileName) {

        //播放mp3,内置支持
        if(audioType.equalsIgnoreCase("mp3")){
            System.out.println("playMp3 fileName="+ fileName);
        } else if(audioType.equalsIgnoreCase("vlc")
                || audioType.equalsIgnoreCase("mp4")){
            mediaAdapter = new MediaAdapter(audioType);
            mediaAdapter.play(audioType, fileName);
        } else{
            System.out.println("Invalid media. "+audioType + " format not supported");
        }
    }

    public static void main(String args []) {
        //某个品牌的播放器
        SomeBrandPlayer player = new SomeBrandPlayer();

        player.play("mp3", "aa.mp3");
        player.play("mp4", "bb.mp4");
        player.play("vlc", "cc.vlc");
        player.play("avi", "dd.avi");
    }
}