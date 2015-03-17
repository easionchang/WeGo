//
//  FileHelper.java
//  FeOA
//
//  Created by Administrator on 2011-12-29.
//  Copyright 2011 flyrise. All rights reserved.
//

package com.msn.support.network.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;

public class FileHelper {
    private static String userID = "nobody";
    private static String baseFilePath = Environment.getExternalStorageDirectory().toString()+ "/oncampus";
    private static String dowloadFilePath = baseFilePath + "/" + userID + "/FILETEMP";
    /**安全的文件路径，经加密后的文件存放在该目录*/
    private static String safeFilePath = baseFilePath + "/" + userID + "/SAFEFILE";
    /**下载文件的临时路径*/
    private static String tempDirPath = baseFilePath + "/" + userID + "/TEMPDir"; 
    /**通讯录头像路径*/
    private static String contactsPath = baseFilePath + "/" + userID + "/portrait";
    /**通讯录同步文件路径*/
	private static String addressbookPath = baseFilePath+"/"+ userID + "/addressbook";
    
	/**获得通讯录本地文件的位置*/
    public static String getAddressbookPath() {
		return addressbookPath;
	}

	private static String keystoreFilePath = baseFilePath+"/"+"KEYSTORE";


    private static String[] wrongChars = {
        "/", "\\", "*", "?", "<", ">", "\"", "|"};
    // 创建文件
    public void newFile(File f) {
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    /**创建目录
     * @param f
     * */
    public static void newDirFile(File f) {
        if (!f.exists()) {
            f.mkdir();
        }
    }

    // 取得指定目录的录音文件
    public ArrayList<String> checkFile(File f) {
        ArrayList<String> data = new ArrayList<String>();
        File[] files = f.listFiles();
        String strtemp = null;
        for (int i = 0; i < files.length; i++) {
            strtemp = files[i].toString();
            strtemp = strtemp.substring(strtemp.lastIndexOf("/") + 1);

            if (strtemp.endsWith(".amr") || strtemp.endsWith(".mp3")) {
                data.add(strtemp);
                strtemp = null;
            }

        }

        return data;
    }

    public ArrayList<String> getVoice(File f) {
        ArrayList<String> data = new ArrayList<String>();
        File[] files = f.listFiles();
        String strtemp = null;
        for (int i = 0; i < files.length; i++) {
            strtemp = files[i].toString();
            strtemp = strtemp.substring(strtemp.lastIndexOf("/") + 1);

            if (strtemp.endsWith(".jpg") || strtemp.endsWith(".png")) {
                data.add(strtemp);
                strtemp = null;
            }
        }

        return data;
    }

//    public static String getFileSize(String path) {
//        File file = new File(path);
//        if (file.exists()) {
//            long size = file.length();
//            return getFileSize(size);
//        } else {
//            return FEApplication.getContext().getString(R.string.util_unknow_size);
//        }
//    }
    
//    public static String getFileSize(long size){
//        if (size >= 1024 * 1024) {
//            double dousize = (double)size / (1024 * 1024);
//            java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
//            return df.format(dousize) + "  Mb";
//        } else if (size >= 1024) {
//            double dousize = size / 1024;
//            return (int)dousize + "  Kb";
//        } else if (size > 0) {
//            return size + "  b";
//        } else {
//            return FEApplication.getContext().getString(R.string.util_unknow_size);
//        }
//    }
    

//    public static String getWebFileSize(String path) {
//        try {
//            URL url = new URL(path);
//            HttpURLConnection con = (HttpURLConnection)url.openConnection();
//            long size = con.getContentLength();
//            if (size >= 1024 * 1024) {
//                double dousize = (double)size / (1024 * 1024);
//                java.text.DecimalFormat df = new java.text.DecimalFormat("#.00");
//                return df.format(dousize) + "  Mb";
//            } else if (size >= 1024) {
//                double dousize = size / 1024;
//                return (int)dousize + "  Kb";
//            } else if (size > 0) {
//                return size + "  b";
//            } else {
////                return FEApplication.getContext().getString(R.string.util_unknow_size);
//            }
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
////            return FEApplication.getContext().getString(R.string.util_unknow_size);
//        }
//    }

    // 获取一个文件列表的里的总文件大小
    public static double getSize(List<String> willupload) {
        return (double)getSizeUnitByte(willupload) / (1024 * 1024);
    };
    
    /**
     * 计算文件的大小，单位是字节
     * @param willupload
     * @return
     */
    public static long getSizeUnitByte(List<String> willupload){
        long allfilesize = 0;
        for (int i = 0; i < willupload.size(); i++) {
            File newfile = new File(willupload.get(i));
            if (newfile.exists() && newfile.isFile()) {
                allfilesize = allfilesize + newfile.length();
            }
        }
        return allfilesize;
    }

    /**
     * 根据文件名获取文件类型
     */
//    public static String getFileType(String name) {
//        if (name.lastIndexOf(".") != -1) {
//            String type = name.substring(name.lastIndexOf("."), name.length());
//            return type;
//        }
//        return FEApplication.getContext().getString(R.string.util_unknow_type);
//    }
    /**
     * 获取默认文件存放路径
     */
    public static String getFileDefaultPath() {
        return dowloadFilePath;
    }

    public static String getBaseFilePath() {
        return baseFilePath;
    }
    /**获取通讯录路径*/
    public static String getContactsPath() {
        return contactsPath;
    }

    /**获取下载文件的临时路径*/
    public static String getTempDirPath() {
        return tempDirPath;
    }
    /**获取加密文件存放路径*/
    public static String getSafeFilePath(){
        return safeFilePath;
    }
    
    public static String getKeystoreFilePath() {
        return keystoreFilePath;
    }

    public static void setKeystoreFilePath(String keystoreFilePath) {
        FileHelper.keystoreFilePath = keystoreFilePath;
    }
    
    /**  
     *  复制单个文件  
     *  @param  oldPath  String  原文件路径  如：c:/fqf.txt  
     *  @param  newPath  String  复制后路径  如：f:/fqf.txt  
     *  @return  boolean  
     */  
    public static boolean  copyFile(String  oldPath,  String  newPath)  {
        boolean iscopy = false;
        InputStream  inStream  =  null;  
        FileOutputStream  fs  =  null;
        try  { 
            int  byteread  =  0;  
            File  oldfile  =  new  File(oldPath);  
            if  (oldfile.exists()){  //文件存在时  
                inStream  =  new  FileInputStream(oldPath); //读入原文件  
                fs  =  new  FileOutputStream(newPath);
                byte[]  buffer  =  new  byte[1024];  
                while  ((byteread  =  inStream.read(buffer))  !=  -1)  {  
                    fs.write(buffer,  0,  byteread);  
                }
                iscopy = true;
            }  
        }  
        catch  (Exception  e)  {  
            e.printStackTrace();  
        }finally{
            try {
                if(inStream != null){
                    inStream.close();
                } 
            } catch (IOException e) {
                e.printStackTrace();
            } 
            try {
                if(fs != null){
                    fs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }  
        return iscopy;
    }
   
    /**
     * 获取已经存在或已经下载好的文件
     * 
     * @return File 已经存在SDcard文件
     */
//    public static File getExistsFile(String id,String fileName) {
//        String filepath = null;
//        FEApplication feapp = (FEApplication)FEApplication.getContext();
//        if(feapp.isSupportFileEncrypt){
//            filepath = FileHelper.getSafeFilePath();
//        }else{
//            filepath = FileHelper.getFileDefaultPath();
//        }
//        File file = new File(filepath, "(" + filterIDChars(id) + ")" + fileName);
//        if (file.exists()) {
//            return file;
//        }
//        return null;
//    }
    
    /**
     * (设置用户ＩＤ) 
     * @param userId　 用户ID
     */
    public static void setUserID(String userId){
        userID = userId;
        safeFilePath = baseFilePath + "/" + userID + "/SAFEFILE";
        dowloadFilePath = baseFilePath + "/" + userID + "/FILETEMP";
        tempDirPath = baseFilePath + "/" + userID + "/TEMPDir";
        contactsPath = baseFilePath + "/" + userID + "/portrait";
       // addressbookPath = baseFilePath + "/" + userID + "/addressbook";
        
    }
    
    public static String getUserID(){
        return userID;
    };
    
    /**
     * 过滤附件ID中某些不能存在在文件名中的字符
     */
    public static String filterIDChars(String attID) {
        if (attID != null) {
            for (int i = 0; i < wrongChars.length; i++) {
                String c = wrongChars[i];
                if (attID.contains(c)) {
                    attID = attID.replaceAll(c, "");
                }
            }
        }
        return attID;
    }
    
    public static void putAttachmentIntoUserFolder(){
        File folder = new File(baseFilePath + "/FILETEMP");
        if(folder.exists() && !userID.equals("nobody")){
           File[] files = folder.listFiles();
           int filesize = files.length; 
           for(int i = 0;i < filesize;i ++){
               File file = files[i];
               if(file.isFile()){
                   String filename = file.getPath();
                   filename = filename.substring(filename.lastIndexOf("/"), filename.length());
                   File newFile = new File(dowloadFilePath + "/" + filename);
                   file.renameTo(newFile);
               }
           }
           folder.delete();
        }
    }
    
    /**
     * 获取过滤ID后的文件名
     */
    public static String getFilterFileName(String flieName) {
        if (flieName == null || "".equals(flieName)) {
            return flieName;
        }
        boolean isNeedFilter = flieName.startsWith("(");
        int index = flieName.indexOf(")");
        if (isNeedFilter && index != -1) {
            int startIndex = index + 1;
            int endIndex = flieName.length();
            if (startIndex < endIndex) {
                return flieName.substring(startIndex, endIndex);
            }
        }
        return flieName;
    }
    

}
