package com.util.image;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.BigDecimal;

/**
 * 图片比较,相似度
 */
public class ImageCompare {

    //转成像素
    public static String[][] trans2Pixel(File file) {
        BufferedImage bImage = null;
        try {
            bImage = ImageIO.read(file);
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        int width = bImage.getWidth();
        int height = bImage.getHeight();
        int x = bImage.getMinX();
        int y = bImage.getMinY();
        String[][] list = new String[width][height];
        for (int i = x; i < width; i++) {
            for (int j = y; j < height; j++) {
                int point = bImage.getRGB(i, j);
                int r= (point & 0xff0000) >> 16;
                int b = (point & 0xff00) >> 8;
                int g= (point & 0xff);
                list[i][j] = r + "," + b + "," + g;

            }
        }
        return list;

    }

    //比较像素点
    public static Integer [] comparePixel(String[][] pixel1 ,String[][] pixel2 ) {
        int same = 0;
        int no = 0;
        int imax=pixel1.length;
        System.out.println("=============comparePixel imax="+imax);
        //遍历pixel1每行
        for(int i=0;i<pixel1.length;i++) {
            String line1[]= pixel1[i];
            int jmax=line1.length;
            System.out.println("-----comparePixel jmax="+jmax+",....i="+i);
            for (int j = 0; j < jmax; j++) {
                try {
                    String[] value1 = pixel1[i][j].split(",");
                    String[] value2 = pixel2[i][j].split(",");
                    int nmax = value1.length;
                    if (value2.length > nmax) {
                        nmax = value2.length;
                    }
                    int diff=0;
                    for (int n = 0; n < nmax; n++) {
                        diff+=Math.abs(Integer.parseInt(value1[n]) - Integer.parseInt(value2[n]));
                    }
                    if (diff <= 50) {
                        //相似
                        same++;
                        System.out.println("------same point i="+i+",j="+j);
                        printStrArray(value1);
                        printStrArray(value2);
                    } else {
                        //不相似
                        no++;
                    }
                } catch(Exception e) {
                    //有一些像素点坐标 ,另一张图可能没那么大的坐标
                    continue;
                }
            }
        }
        return new Integer []{same,no};
    }

    public static void compareImage(String imagePath1, String imagePath2){

        File image1=new File(imagePath1);
        File image2=new File(imagePath2);
        String[][] pixel1 = trans2Pixel(image1);
        for(int a=0;a<pixel1.length;a++) {
            String cur[]= pixel1[a];
            System.out.println(a+" "+cur.length+" "+ cur[0]+".........");
        }
        String[][] pixel2 = trans2Pixel(image2);
        System.out.println("...........................pixel1.lenth="+pixel1.length);
        System.out.println("...........................pixel2.lenth="+pixel2.length);
        Integer [] b1=comparePixel(pixel1,pixel2);
        System.out.println("/////////////////////////////////////////////////////");
        Integer [] b2=comparePixel(pixel2,pixel1);
        int same=b1[0]+b2[0];
        int no=b1[1]+b2[1];
        int total=same+no;
        String samePercent="0";
        if(no == 0){
            samePercent="100";
        }else {
            System.out.println(same +" "+no);
            BigDecimal s = new BigDecimal(total+"");
            BigDecimal t = new BigDecimal(same+"");
            BigDecimal rt= t.divide(s,10,BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            samePercent = rt.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
        }
        System.out.println("两图相似度:" + samePercent+ "%, 相似像素数:" + same + ",不相似像素数:" + no );
    }

    private static void printStrArray(String [] ay) {
        StringBuffer buffer = new StringBuffer();
        for(int i=0;i<ay.length;i++) {
            buffer.append(ay[i]).append(" ");
        }
        String rt=buffer.toString();
        System.out.println(rt);
    }

    public static void main(String[] args){

        compareImage("E:\\aa.png", "E:\\bb.png");
    }
}
