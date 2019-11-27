package com;

import java.io.*;
import java.util.*;

/**
 * 一汽大众数据保险数据文件处理
 */
public class FawVkInsuranceDataGenerotor {

    private static int count = 0;

    //表头
    private static final String TABLE_HEADER = "年(Year),月(Month),是否进口(Prod_Type),省份(Province),城市(City)," +
            "大区(RSD),车身形式(BodyType),级别(Segment),排量(Displacement),厢数(Compartment_Number)," +
            "是否乘用车(PCrec),子级别(Sub_Segment),系别(DEPT),企业简称(OEM_ab),厂商(OEM)," +
            "品牌简称(Brand_ab),品牌(Brand),车型简称(Model_ab),车型(Model),新能源类型(New_Energy_Type)," +
            "销量(Sales)";

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

    public static void main(String[] args) throws IOException {

        String inputDirPath = scanner("文件夹路径");
        String inputFilePath = scanner("文件名称");
        String outputDirName = scanner("存放文件夹名称");

        //读取文件(字符流)
        String sourceFileFullPath = inputDirPath.concat("\\").concat(inputFilePath);
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFileFullPath)));

        String dirPath = inputDirPath.concat("\\").concat(outputDirName);
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            try {
                dirFile.mkdir();
            } catch (Exception ex) {

            }
        }

        //读取数据
        //循环取出数据
        Set<String> fileNameSet = new HashSet<>();
        Map<String, Object> fileMap = new HashMap<>();

        BufferedWriter bw;
        in.readLine();
        String str;
        String symbolName;
        int index;
        StringBuilder writeSb;

        while ((str = in.readLine()) != null) {
            if (str.contains("Beetle,甲壳虫") && !str.contains("Beetle,甲壳虫(旧)")) {
                System.out.println(str);
                str = str.replace("甲壳虫", "甲壳虫(旧)");
            }

            writeSb = new StringBuilder(str);

            index = str.indexOf(',', str.indexOf(',') + 1);
            symbolName = str.substring(0, index);
            if (!fileNameSet.contains(symbolName)) {
                fileNameSet.add(symbolName);
                String[] symbolNameArr = symbolName.split(",");
                String filePath = String.format("%s\\%s年%s月保险数据.csv", dirPath, symbolNameArr[0], symbolNameArr[1]);

                File file = new File(filePath);
                if (file.exists()) {
                    //文件存在先删除
                    file.delete();
                }
                try {
                    file.createNewFile();
                } catch (Exception ex) {

                }

                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filePath), "GB2312"));
                bw.write(TABLE_HEADER);
                bw.newLine();

                fileMap.put(symbolName, bw);
            }


            bw = (BufferedWriter) fileMap.get(symbolName);
            bw.write(writeSb.toString());
            bw.newLine();
            count++;

            if (count % 100000 == 0) {
                System.out.println("已完成" + count + "条");
            }
        }

        for (Object obj : fileMap.values()) {
            bw = (BufferedWriter) obj;
            //释放缓存，关闭文件流
            bw.flush();
            bw.close();
        }

        //关闭文件流
        in.close();
    }
}
