package com;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;

/**
 * @Author cuiwei
 * @Description
 * @Date 2019/10/24 16:42
 * @Version 1.0
 */
public class FawVkAakDataGenerotor {

    private static int count = 0;

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

    public static void main(String[] args) throws Exception {

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

        String inputFileHeadStr = "Prod_Type,Brand Indicator,OEM,Brand,Model_EN,Fuletype,BEV/PHEV,Segment,Sub-Segment (New Definition),Sub-Segment (Old Definition),Bodytype,Budget_Entry,Group_China,Group_Foreign,Oringinal,Sub-segment & Bodytype,厂商,品牌,中文名称,数量,年,月";
        String outputFileHeadStr = "是否进口(Prod_Type),品牌标志(Brand_Indicator),企业简称(OEM_ab),品牌简称(Brand_ab),车型简称(Model_ab),Fuletype(Fuletype),BEV/PHEV(BEV_PHEV),级别(Segment),子级别(Sub_Segment),,车身形式(BodyType),,Group_China(Group_China),Group_Foreign(Group_Foreign),国别(Oringinal),,厂商(OEM),品牌(Brand),车型(Model),数量(Sales),年(Year),月(Month)";
        StringBuilder tableHeader = new StringBuilder();

        List<String> inputFileHeadList = Arrays.asList(inputFileHeadStr.split(","));
        List<String> outputFileHeadList = Arrays.asList(outputFileHeadStr.split(","));
        int index = inputFileHeadList.indexOf("年");

        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < outputFileHeadList.size(); i++) {
            if (StringUtils.isNotBlank(outputFileHeadList.get(i))) {
                indexList.add(i);
                tableHeader.append(outputFileHeadList.get(i));
                if (i < outputFileHeadList.size() - 1) {
                    tableHeader.append(",");
                }
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

        StringBuilder writeSb;
        while ((str = in.readLine()) != null) {
            writeSb = new StringBuilder();
            String[] tempA = str.split(",");
            for (int i = 0; i < indexList.size(); i++) {
                writeSb.append(tempA[indexList.get(i)]);
                if (i < indexList.size() - 1) {
                    writeSb.append(",");
                }
            }
            symbolName = tempA[index];
            if (!fileNameSet.contains(symbolName)) {
                fileNameSet.add(symbolName);
                String filePath = String.format("%s\\%s年零售数据.csv", dirPath, symbolName);

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
                bw.write(tableHeader.toString());
                bw.newLine();

                fileMap.put(symbolName, bw);
            }


            bw = (BufferedWriter) fileMap.get(symbolName);
            bw.write(writeSb.toString());
            bw.newLine();
            count++;

            if (count % 10000 == 0) {
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
