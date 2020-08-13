package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.io.*;
import javax.swing.JFileChooser;

public class Main {


    public static void main(String[] args) {
        try {

            //指定的txt文件名称
            String txtName = "1.txt";
            //获取当前程序目录路径
            String golbPath = null;
            //记录记事本中是否空行 如果存在空行则进行替换json文件处理逻辑
            //文件相对路径
            String luj = null;
            //文本相对路径
            String txtPath = System.getProperty("user.dir") + File.separator + txtName;
            //旧内容
            String srcStr = null;
            //要替换的内容
            String replaceStr = null;
            // 读取json路径
            String temp = null;
            //输出到控制面板的信息
            String strPut = null;
            //读取txt路径
            File txtFile = new File(txtPath);
            //默认配置相对路径
            golbPath = System.getProperty("user.dir");
            //控制台输入
            Scanner scn = new Scanner(System.in);
            //文件流读取
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(txtFile), "GBK");
            BufferedReader bf = new BufferedReader(inputReader);
            //获取所有的文件路径
            List<String> dirList = new ArrayList<String>();
            // 按行读取字符串
            String txt = null;
            luj = "";
            //路径和文本内容存储
            HashMap<String, String> cfgDic = new HashMap<String, String>();
            //得到每个文件名
            List<String> infoList = new ArrayList<String>();
            //存储替换字符信息
            List<String> rep = new ArrayList<String>();
            String nice = null;
            //判断文件是否为空
            while ((txt = bf.readLine()) != null) {
                if (txt.isEmpty()){
                    System.out.println("文件格式不正确！！");
                    return;
                }
                //判断文件中是否带有空格
                if (txt.contains(" ")) {
                    //判断文件不为空
                    if (!txt.isEmpty()) {
                        strPut = txt.split(" ")[0];
                        System.out.println(strPut);
                        srcStr = txt.split(" ")[1];
                        replaceStr = scn.next();
                        if (luj.indexOf("\uFEFF") != -1) {
                            int index = luj.indexOf("\uFEFF");
                            temp = luj.substring(0, index) + luj.substring(index + 1);
                        } else {
                            temp = luj;
                        }
                        //判断如果得到的路径中包含特殊字符进行切割
                        File fileExists = new File(temp);
                        if (fileExists.exists() && fileExists.isFile()) {
                            //编码格式
                            String encoding = "UTF-8";
                            File file = new File(temp);
                            Long filelength = file.length();
                            //将文件转为byte
                            byte[] filecontent = new byte[filelength.intValue()];
                            FileInputStream c = new FileInputStream(file);
                            //读取字节
                            c.read(filecontent);
                            c.close();
                            //获取文件中的内容转为字符串
                            String replaceData= new String(filecontent, encoding);
                            //先替换文件内容
                            replaceData = replaceData.replaceAll(srcStr, replaceStr);
                            rep.add(replaceData);
                            if (rep.size() > 0) {
                                nice = rep.get(0).replaceAll(srcStr, replaceStr);
                            }
                            //存储要修改的文件路径及文件信息
                            cfgDic.put(infoList.get(0), nice);
                            //如果key存在 则修改value
                            if (cfgDic.containsKey(infoList.get(0))) {
                                cfgDic.put(infoList.get(0), nice);
                            }
                        } else {
                            System.out.println(temp+"文件不存在");
                            return;
                        }
                    }
                } else {
                    try{
                        if (txt.contains(".")) {
                            rep.clear();//每次处理下一个文件时提前清空
                            infoList.clear();
                            luj = dirList.get(0) + File.separator + txt;
                            infoList.add(txt);
                            dirList.add(luj);
                        } else {
                            luj = golbPath + File.separator + txt;
                            dirList.add(luj);
                        }
                    }
                    catch (Exception ex){
                            System.out.println("文件格式错误");
                            return;
                    }
                }
            }
            createDialog(dirList.get(0), cfgDic);
            bf.close();
            inputReader.close();//关闭
            //控制台程序完成后停留
            System.in.read();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    /**
     * 创建弹出文件选择框
     *
     * @oldPathDir 旧文件地址
     */
    public static void createDialog(String oldPathDir, HashMap<String, String> hashMap) {
        try {
            //声明弹出
            JFileChooser fileChooser = new JFileChooser();
            //设置title
            fileChooser.setDialogTitle("选择文件夹");
            //设置弹窗选择的类型为文件夹
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            //弹出选择控件
            fileChooser.showOpenDialog(null);
            //获取选择的文件
            File nullFile = fileChooser.getSelectedFile();
            String strcount=nullFile.toString();
            String compareStr="\\";
            int index=0;
            int indexStart=0;
            int compareStrLength=strcount.length();
            while (true){
                int tm = strcount.indexOf(compareStr,indexStart);
                if( tm >= 0){
                    index ++;
                    //  没查找一次就从新计算下次开始查找的位置
                    indexStart = tm+compareStrLength;
                }else{
                    //直到没有匹配结果为止
                    break;
                }
            }
            if (index<=1){
                System.out.println("请选择文件夹");
                createDialog(oldPathDir, hashMap);
            }
            if (nullFile != null) {
                String newPathDir = fileChooser.getSelectedFile().getAbsolutePath();
                //获取新的文件路径
                //判断旧文件路径是否是文件否则就是目录
                if ((new File(oldPathDir)).isFile()) {
                    copyFile(new File(oldPathDir), new File(newPathDir));
                } else if ((new File(oldPathDir)).isDirectory()) {
                    copyDirectory(oldPathDir, newPathDir);
                }
                if (hashMap.size()>0){
                    contentWrite(hashMap,newPathDir);
                }

            } else {
                System.out.println("重新选择需要copy的文件路径，1重新选择，0取消copy文件操作");
                Scanner scn = new Scanner(System.in);
                int number = scn.nextInt();
                if (number == 1) {
                    createDialog(oldPathDir, hashMap);
                } else {
                    return;
                }
            }
        } catch (Exception ex) {
            System.out.println("选择文件弹出错误：" + ex.toString());
        }
    }

    /**
     * @param hashMap  需要修改的文件 及对应的内容
     * @param dirfile  选定新的文件路径
     */
    private  static  void  contentWrite(HashMap<String,String> hashMap,String dirfile){
        BufferedWriter fw;
        //判断字典中是否含有数据
            for (String key : hashMap.keySet()) {
                String fileName = key;
                String content = hashMap.get(key);
                try {
                    File file = new File(dirfile+File.separator+fileName);
                    if (file.exists()){
                        //创建字符输出流对象，负责向文件内写入
                        //fw = new FileWriter(dirfile+File.separator+fileName);
                         fw= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dirfile+File.separator+fileName), "UTF-8"));
                        //将str里面的内容读取到fw所指定的文件中
                        fw.write(content);
                        fw.close();
                    }
                    else{
                        System.out.println(dirfile+File.separator+fileName+"：文件不存在");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
    }

    /**
     * @param sourceFile  旧文件
     * @param targetFile  新文件
     */
    private static void copyFile(File sourceFile, File targetFile) {
        if (!sourceFile.canRead()) {
            System.out.println("源文件" + sourceFile.getAbsolutePath() + "不可读，无法复制！");
            return;
        } else {
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            try {
                fis = new FileInputStream(sourceFile);
                bis = new BufferedInputStream(fis);
                fos = new FileOutputStream(targetFile);
                bos = new BufferedOutputStream(fos);
                int len = 0;
                while ((len = bis.read()) != -1) {
                    bos.write(len);
                }
                bos.flush();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                    if (bis != null) {
                        bis.close();
                    }
                    if (fos != null) {
                        fos.close();
                    }
                    if (bos != null) {
                        bos.close();
                    }
                    System.out.println("文件" + sourceFile.getAbsolutePath() + "复制到" + targetFile.getAbsolutePath() + "完成");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param sourcePathString  旧文件夹
     * @param targetPathString  新文件夹
     */
    private static void copyDirectory(String sourcePathString, String targetPathString) {
        if (!new File(sourcePathString).canRead()) {
            System.out.println("源文件夹" + sourcePathString + "不可读，无法复制！");
            return;
        } else {
            (new File(targetPathString)).mkdirs();
            File[] files = new File(sourcePathString).listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    copyFile(new File(sourcePathString + File.separator + files[i].getName()), new File(targetPathString + File.separator + files[i].getName()));
                } else if (files[i].isDirectory()) {
                    copyDirectory(sourcePathString + File.separator + files[i].getName(), targetPathString + File.separator + files[i].getName());
                }
            }
            System.out.println("复制文件夹" + sourcePathString + "到" + targetPathString + "完成");
        }
    }

    /**
     * @param fileJsonPath json文件路径
     * @param EdaitInfo    修改json文件提示语
     */
//    public static void WriteExecute(String fileJsonPath, String EdaitInfo) {
//        int number = 0;
//        String toolTip = "请您输入" + EdaitInfo + "：";
//        String isSubmit = "请您核对需要修改的参数信息，确认无误按1确定，0取消";
//        String obj =new String();//接收记事本中修改修改的提示语
//        Object value = null;//接收控制台输入修改参数
//        try {
//            if (EdaitInfo.startsWith("\uFEFF")){
//                EdaitInfo=EdaitInfo.substring(1);
//            }
//            switch (EdaitInfo) {
//                case "网络端口":
//                    obj = "port";
//                    break;
//                case "是否开启日志":
//                    obj = "uselogserver";
//                    break;
//                case "修改超时时间":
//                    obj="session.timeout";
//                    break;
//                case "修改IP":
//                    obj="corsorigins[0]";
//                    break;
//                default:
//                    obj = "";
//                    break;
//            }
//            Scanner scn = new Scanner(System.in);
//            System.out.println(toolTip);
//            value = scn.next();//获取替换的参数
//            if (EdaitInfo.equals("是否开启日志")){
//                value=scn.nextBoolean();
//            }
//            if (EdaitInfo.equals("修改IP")){
//                System.out.println("IP节点共四个，修改第一个按1，第二个按2，第三个按3，第四个按四");
//                int index=scn.nextInt();
//                if (index==1){
//                    obj="corsorigins[0]";
//                }
//                else if (index==2){
//                    obj="corsorigins[1]";
//                }
//                else if (index==3){
//                    obj="corsorigins[2]";
//                }else if (index==4){
//                    obj="corsorigins[3]";
//                }
//            }
//            System.out.println(isSubmit);
//            number = scn.nextInt();//获取是否执行修改操作
//            if (number == 1) {
//                String json = FileUtils.readJsonData(fileJsonPath);
//                //去除json文件中uFEFF字符
//                if (json.startsWith("\uFEFF")) {
//                    json = json.substring(1);
//                }
//                DocumentContext context = JsonPath.parse(json);
//                //修改Json的value
//                JsonPath path = JsonPath.compile("$.." + obj + "");
//                context.set(path, value);
//                String changedJson = context.jsonString();
//                //将json字符串格式化 并写入制定文件
//                FileUtils.writeFile(fileJsonPath, FormatJsonUtlis.formatJson(changedJson));
//                System.out.println("修改" + EdaitInfo + "成功");
//            } else {
//                System.out.println("修改" + EdaitInfo + "错误");
//            }
//        }catch (Exception e){
//            e.toString();
//        }
//    }
}
