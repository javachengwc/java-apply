package com.other.video;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

//mp4文件，编码方式mpeg4，转化为h264
public class Mp4 {

  public static void main(String [] args ) {
//
//    File file = new File(filePath);
//    Encoder encoder = new Encoder();
//    MultimediaInfo info = encoder.getInfo(file);
//    VideoInfo video = info.getVideo();
//    String decoder = video.getDecoder();
//    System.out.println(decoder);
    String inFilePath="D:\\tmp\\1.mp4";
    String videoType =info(inFilePath);
    System.out.println("--------------videoType:"+videoType);
    transfer(inFilePath,"D:\\tmp\\2.mp4");
    System.out.println("--------------end--------------");
  }

  public static void transfer(String infile,String outfile) {
    String videoCommend = "D:\\soft\\ffmpeg-4.3.1-2020-10-01-full_build\\bin\\ffmpeg.exe -i " + infile + " -vcodec h264 " + outfile;
    try {
      Runtime rt = Runtime.getRuntime();
      Process proc = rt.exec(videoCommend);
      InputStream stderr = proc.getErrorStream();
      InputStreamReader isr = new InputStreamReader(stderr);
      BufferedReader br = new BufferedReader(isr);
      String line = null;
      while ( (line = br.readLine()) != null)
        System.out.println(line);
      int exitVal = proc.waitFor();
      System.out.println("Process exitValue: " + exitVal);
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  public static String info(String infile) {
    String videoCommend = "D:\\soft\\ffmpeg-4.3.1-2020-10-01-full_build\\bin\\ffmpeg.exe -i " + infile ;
    String mpeg4 ="Video: mpeg4";
    String h264 ="Video: h264";
    String result="";
    try {
      Runtime rt = Runtime.getRuntime();
      Process proc = rt.exec(videoCommend);
      InputStream stderr = proc.getErrorStream();
      InputStreamReader isr = new InputStreamReader(stderr);
      BufferedReader br = new BufferedReader(isr);
      String line = null;
      while ( (line = br.readLine()) != null) {
        System.out.println(line);
        if(line.contains(mpeg4) ) {
          result="mpeg4";
        }
        if(line.contains(h264) ) {
          result="h264";
        }
      }

      int exitVal = proc.waitFor();
      System.out.println("Process exitValue: " + exitVal);
    } catch (Throwable t) {
      t.printStackTrace();
    }
    return result;
  }
}
