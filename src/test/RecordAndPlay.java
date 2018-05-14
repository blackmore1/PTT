package test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;  
public class RecordAndPlay {  
    static volatile boolean stop=false;  
    public static void main(String[] args) {  
        Play();  
    }  
    //播放音频文件  
    public static void Play() {  
  
        try {  
            AudioFormat audioFormat =  
//                    new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100F,  
//                    8, 1, 1, 44100F, false);  
             new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100F, 16, 2, 4,  
             44100F, true);  
            DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat); 
            TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(info);  
            targetDataLine.open(audioFormat);  
            info = new DataLine.Info(SourceDataLine.class, audioFormat);  
            targetDataLine.start();  
            int len = 0;  
            final int bufSize=4*100;  
            byte[] buffer = new byte[bufSize];
            OutputStream bos = new FileOutputStream(new File("./test.wav"));
            while (len != -1) {  
                //System.in.read();  
            	len = targetDataLine.read(buffer, 0, bufSize);
                bos.write(buffer, 0, len);
                System.out.println(len);
            }  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}
