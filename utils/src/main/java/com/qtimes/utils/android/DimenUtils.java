package com.qtimes.utils.android;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

/**
 * Author: JackHou
 * Date: 2020/3/17.
 * 快速生成适配工具类
 */
public class DimenUtils {
    //文件保存的路径  是在该项目下根路径下创建  比如该项目创建的路径是E:\project3.0\，
    // 则保存的文件路径是E:\project3.0\DimenDemo\app\src\main\res\values-1920x1080\dimen.xml
    private final static String rootPath = "app/src/main/res/values-{0}x{1}";


    private final static float dw = 768f;//默认布局的宽
    private final static float dh = 1280f;//默认布局的高

    private final static String WTemplate = "<dimen name=\"x{0}\">{1}px</dimen>\n";
    private final static String HTemplate = "<dimen name=\"y{0}\">{1}px</dimen>\n";

    public static void main(String[] args) {
        makeString(600, 1024);
    }

    //获取dimen.xml的文本内容
    public static void makeString(int w, int h) {
        System.out.println("1111111111");
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
        sb.append("<resources>");

        //遍历获取一系列宽的值
        float cellw = w / dw;//宽的比例
        for (int i = 0; i < dw; i++) {
            sb.append(WTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellw * i) + ""));
        }

        sb.append(WTemplate.replace("{0}", dw + "").replace("{1}", w + ""));

        //遍历获取一系列高的值
        float cellh = h / dh;//高的比例
        for (int i = 0; i < dh; i++) {
            sb.append(HTemplate.replace("{0}", i + "").replace("{1}",
                    change(cellh * i) + ""));
        }

        sb.append(HTemplate.replace("{0}", dh + "").replace("{1}", h + ""));
        sb.append("</resources>");

        makeFile(w, h, sb.toString());
    }

    //创建文件并写入内容
    private static void makeFile(int w, int h, String text) {
        System.out.println("22222222222222");
        String path = rootPath.replace("{0}", h + "").replace("{1}", w + "");
        File rootFile = new File(path);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        File file = new File(path, "dimens.xml");
        System.out.println("333333333333333:" + file.getAbsolutePath());
        try {
            PrintWriter pw = new PrintWriter(new FileOutputStream(file));
            pw.println(text);
            pw.close();
            System.out.println("4444444444444444");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("5555555555555555");
        }
    }


    public static float change(float a) {
        int temp = (int) (a * 100);
        return temp / 100f;
    }
}
