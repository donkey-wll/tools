package com;

import java.io.*;
import java.util.Scanner;

/**
 * 接口文档处理
 */
public class InterfaceDocGenerator {

    private final static String titleSymbol = "<h5>";

    /**
     * <p>
     * 读取控制台内容
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        StringBuilder help = new StringBuilder();
        help.append("请输入" + tip + "：");
        System.out.println(help.toString());
        if (scanner.hasNext()) {
            String ipt = scanner.next();
            return ipt;
        }
        return null;
    }

    public static void main(String[] args) {
//        String[] fileNameArr = new String[]{"勘察", "设计", "桩基", "质量"};

//        for (int i = 0; i < fileNameArr.length; i++) {
//            String fileName = fileNameArr[i];

        String filePath = scanner("文件路径");
        if (filePath == null || "".equals(filePath)) {
            System.out.println("请输入文件路径");
        } else {
            try {
//                InputStream is = new FileInputStream(String.format("E:\\Work\\JavaProject\\test\\src\\fileDir\\%s.html", fileName));
                InputStream is = new FileInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is);
                //读取文件内容
                byte[] b = new byte[bis.available()];
                bis.read(b);
                String str = new String(b);
                str = str.replaceFirst("style=\"width:800px; margin: 0 auto", "style=\"width:553px; margin: 0 auto");
                str = str.replaceAll("\t", "").replaceAll("\r", "").replaceAll("\n", "");
                str = str.replaceAll("</h4>                 <h5>", "").replaceAll("<h4>", "<h5>");
                str = str.replaceAll("<td colspan=\"4\">\\*/\\*</td>", "<td colspan=\"4\">Map</td>");
                str = str.replaceAll("<td>请求类型</td>                        <td colspan=\"4\"></td>", "<td>请求类型</td><td colspan=\"4\">application/json</td>");
                str = str.replaceAll("<td>请求类型</td>                        <td colspan=\"4\"/>", "<td>请求类型</td><td colspan=\"4\">application/json</td>");


                String[] strArr = str.split(titleSymbol);
                StringBuilder ssb = new StringBuilder(strArr[0]);
                int count = 0;
                for (int j = 1; j < strArr.length; j++) {
                    String content = strArr[j];
                    String title = content.split("</h5>")[0];

                    if (title.contains("承诺制")) {
                        continue;
                    }
                    count++;

                    if (title.toLowerCase().contains("controller")) {
                        System.out.println(title);
                        int controllerIndex = title.toLowerCase().indexOf("controller");
                        String tempTitle = title.substring(controllerIndex + "controller".length());
                        content = content.replaceAll(title, tempTitle);
                        title = tempTitle;
                        System.out.println("\n\n" + title);
                    }

                    content = content.replaceAll("<td>接口描述</td>                        <td colspan=\"4\">null</td>", String.format("<td>接口描述</td>                        <td colspan=\"4\">%s</td>", title));

                    String returnValue;
                    if (title.contains("删除") || title.contains("修改") || title.contains("工作流") || title.contains("退回")) {
                        returnValue = "无";
                    } else if (title.contains("保存")) {
                        returnValue = "id";
                    } else if (title.contains("导出")) {
                        returnValue = "pdf";
                    } else if (title.contains("对比")) {
                        returnValue = String.format("%s%s", title, "信息");
                    } else {
                        returnValue = title;
                    }
                    content = content.replaceAll("<td class=\"bg\">返回值</td>                        <td colspan=\"4\"></td>", String.format("<td class=\"bg\">返回值</td>                        <td colspan=\"4\">%s</td>", returnValue));

                    ssb.append(String.format("%s（%s） %s", titleSymbol, count, content));
                }

//                OutputStream ot = new FileOutputStream(String.format("E:\\Work\\JavaProject\\test\\src\\fileDir\\%s%s.html", fileName, "-改"));
                OutputStream ot = new FileOutputStream(String.format("%s%s", filePath, "new"));
                byte[] otB = ssb.toString().getBytes();
                ot.write(otB);
                ot.flush();
                ot.close();
                bis.close();//关闭流(关闭bis就可以了)
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                //系统强制解决的问题：文件没有找到
                e.printStackTrace();
            } catch (IOException e) {
                //文件读写异常
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        }
    }


}
