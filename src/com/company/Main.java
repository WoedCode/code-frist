package com.company;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
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
            boolean isEmpty = false;
            //判断文件是绝对路径还是相对路径
            boolean isPath = true;
            //判断获取需要修改文件目录的路径信息
            boolean isContent = true;
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
            // 替换
            String line = null;
            //读取txt路径
            File txtFile = new File(txtPath);
            //默认配置相对路径
            golbPath = System.getProperty("user.dir");
            Scanner scn = new Scanner(System.in);
            InputStreamReader inputReader = new InputStreamReader(new FileInputStream(txtFile), "GBK");
            BufferedReader bf = new BufferedReader(inputReader);
            //获取所有的文件路径
            List<String> dirList = new ArrayList<String>();
            // 按行读取字符串
            String txt;
            while ((txt = bf.readLine()) != null) {
                if (!txt.isEmpty()) {
                    //判断是否有需要的修改的json参数 否则进行下一个文件修改
                    if (txt.contains(" ")) {
                        strPut = txt.split(" ")[0];
                        System.out.println(strPut);
                        srcStr = txt.split(" ")[1];
                        replaceStr = scn.next();
                        //判断文件中的空行
                        if (isEmpty == false) {
                            //判断如果得到的路径中包含特殊字符进行切割
                            if (luj.indexOf("\uFEFF") != -1) {
                                int index = luj.indexOf("\uFEFF");
                                temp = luj.substring(0, index) + luj.substring(index + 1);
                            } else {
                                temp = luj;
                            }
                            File fileExists = new File(temp);
                            //判断文件是否存在
                            if (fileExists.exists() && fileExists.isFile()) {
                                FileReader in = new FileReader(temp);
                                BufferedReader bufIn = new BufferedReader(in);
                                // 内存流, 作为临时流
                                CharArrayWriter tempStream = new CharArrayWriter();
                                //根据替换符进行替换
                                while ((line = bufIn.readLine()) != null) {
                                    // 替换每行中, 符合条件的字符串
                                    line = line.replaceAll(srcStr, replaceStr);
                                    // 将该行写入内存
                                    tempStream.write(line);
                                    // 添加换行符
                                    tempStream.append(System.getProperty("line.separator"));
                                }
                                // 关闭 输入流
                                bufIn.close();
                                // 将内存中的流 写入 文件
                               // FileWriter out = new FileWriter(temp);
                                BufferedWriter out= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), "UTF-8"));
                                tempStream.writeTo(out);
                                out.close();
                            } else {
                                System.out.println(temp + "文件不存在");
                            }
                        } else {
                            temp = luj;
                            FileReader in = new FileReader(temp);
                            BufferedReader bufIn = new BufferedReader(in);
                            // 内存流, 作为临时流
                            CharArrayWriter tempStream = new CharArrayWriter();
                            while ((line = bufIn.readLine()) != null) {
                                // 替换每行中, 符合条件的字符串
                                line = line.replaceAll(srcStr, replaceStr);
                                // 将该行写入内存
                                tempStream.write(line);
                                // 添加换行符
                                tempStream.append(System.getProperty("line.separator"));
                            }
                            // 关闭 输入流
                            bufIn.close();
                            // 将内存中的流 写入 文件
                            //FileWriter out = new FileWriter(temp);
                            //修复修改中文字符乱码问题
                            BufferedWriter out= new BufferedWriter(new OutputStreamWriter(new FileOutputStream(temp), "UTF-8"));
                            //out.write(temp);
                            tempStream.writeTo(out);
                            out.close();
                        }
                    } else {
                        //根据：判断是为新的盘符路径 非相对路径
                        if (txt.contains(":")) {
                            if (!new File(golbPath).exists()) {
                                System.out.println("指定文件目录不存在，请仔细核对文件信息！！");
                                break;
                            }
                            luj = txt;
                            dirList.add(luj);
                            isPath = false;//判断读取的是绝对路径 true读相对路径
                        } else {
                            if (isPath == true) {
                                if (isContent == true) {
                                    //获取全部路径
                                    luj = golbPath + File.separator + txt;
                                    dirList.add(luj);
                                    isContent = false;
                                } else {
                                    luj = dirList.get(0) + File.separator + txt;
                                    dirList.add(luj);
                                }
                            } else {
                                luj = dirList.get(0) + File.separator + txt;
                            }
                        }
                    }
                }
                //输出空行信息
                else {
                    isEmpty = true;
                }
            }
            bf.close();
            inputReader.close();//关闭
            try {
                String oldPathDir = dirList.get(0);
                createDialog(oldPathDir);

            } catch (Exception ex) {
                System.out.println("选择文件错误：" + ex.toString());
            }
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
    public static void createDialog(String oldPathDir) {
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
            if (nullFile != null) {
                //获取新的文件路径
                String newPathDir = fileChooser.getSelectedFile().getAbsolutePath();
                //判断旧文件路径是否是文件否则就是目录
                if ((new File(oldPathDir)).isFile()) {
                    copyFile(new File(oldPathDir), new File(newPathDir));
                } else if ((new File(oldPathDir)).isDirectory()) {
                    copyDirectory(oldPathDir, newPathDir);
                }
            } else {
                System.out.println("重新选择需要copy的文件路径，1重新选择，0取消copy文件操作");
                Scanner scn = new Scanner(System.in);
                int number = scn.nextInt();
                if (number == 1) {
                    createDialog(oldPathDir);
                }else {
                    return;
                }

            }
        } catch (Exception ex) {
            System.out.println("选择文件弹出错误：" + ex.toString());
        }
    }

    /*
     * @sourceFile 旧文件
     * @targetFile 新文件
     * */
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

    /*
     * @sourceFile 旧文件夹
     * @targetFile 新文件夹
     * */
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
