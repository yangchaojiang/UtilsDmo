package com.yutils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
/**
 * Created by yangjiang on 2017/1/6.
 * E-Mail:1007181167@qq.com
 * Description:  文件操作帮助类
 */
public class MyFileUtil {


    /****
     * 文件缓存地址
     **/
    public static String getDiskCacheDir(Context context) {
        return context.getCacheDir().getPath();
    }

    /****
     * 文件存放地址
     *
     * @param context 上下文
     * @param type    存放文件的类型  使用 Environment
     **/
    public static String getDiskFileDir(Context context, String type) {
        String cachePath;
        File file = context.getExternalFilesDir(type);
        if (file != null) {
            cachePath = file.getAbsolutePath();
        } else {
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

//    /***
//     * 获取文件类型
//     *
//     * @param filePath URL
//     ***/
//    public static int getFileType(String filePath) {
//        int res = 0;
//        if (null == filePath || filePath.isEmpty() || filePath.equals(""))
//            return res = R.mipmap.ic_stub;
//        String type = filePath.toLowerCase().substring(filePath.lastIndexOf("."));//统一转换小写
//        Log.v("getFileType", type);
//        if (type.equals(".doc") || type.equals(".docx")) {
//            res = R.mipmap.icon_friend_word;
//        } else if (type.equals(".xls") || type.equals(".xlsx")) {
//            res = R.mipmap.icon_friend_excel;
//        } else if (type.equals(".ppt") || type.equals(".pptx")) {
//            res = R.mipmap.icon_friend_ppt;
//        } else if (type.equals(".jpg") || type.equals(".png") || type.equals(".gif") || type.equals(".bmp")) {
//            res = R.mipmap.icon_friend_picture;
//        } else if (type.equals(".mp4")) {
//            res = R.mipmap.icon_friend_video;
//        } else if (type.equals(".pdf") || type.equals(".pdfx")) {
//            res = R.mipmap.icon_friend_pdf;
//        } else if (type.equals(".mp3")) {
//            res = R.mipmap.icon_friend_voice;
//        }
//        return res;
//    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    public static String getSDAvailableSize(Context context) {
        return Formatter.formatFileSize(context, getSdAvaliableSize());
    }

    /**
     * 获得机身内存总大小
     *
     * @return String
     */
    public static String getRomTotalSize(Context context) {
        return Formatter.formatFileSize(context, getRomTotalSize());
    }

    /**
     * 获得机身可用内存
     *
     * @return String
     */
    public static String getRomAvailableSize(Context context) {
        return Formatter.formatFileSize(context, getRomAvailableSize());
    }

    /**
     * 获得sd卡剩余容量，即可以大小
     *
     * @return long
     */
    public static long getSdAvaliableSize() {
        if (Build.VERSION.SDK_INT < 18) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return blockSize * availableBlocks;
        } else {
            File path = Environment.getExternalStorageDirectory();
            StatFs statFs = new StatFs(path.getPath());
            long blockSize = statFs.getBlockSizeLong();
            long availableBlocks = statFs.getAvailableBlocksLong();
            return blockSize * availableBlocks;
        }
    }

    /**
     * 获得机身内存大小
     *
     * @return long
     */
    private static long getRomTotalSize() {
        if (Build.VERSION.SDK_INT < 18) {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return blockSize * totalBlocks;
        } else {
            File path = Environment.getDataDirectory();
            StatFs statFs = new StatFs(path.getPath());
            long blockSize = statFs.getBlockSizeLong();
            long tatalBlocks = statFs.getBlockCountLong();
            return blockSize * tatalBlocks;
        }
    }

    /**
     * 获得机身可用内存
     *
     * @return long
     */
    public static long getRomAvailableSize() {
        if (Build.VERSION.SDK_INT < 18) {
            File path = Environment.getDataDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return blockSize * availableBlocks;
        } else {
            File path = Environment.getDataDirectory();
            StatFs statFs = new StatFs(path.getPath());
            long blockSize = statFs.getBlockSizeLong();
            long availableBlocks = statFs.getAvailableBlocksLong();
            return blockSize * availableBlocks;
        }

    }

    /**
     * 将数据写入一个文件
     *
     * @param destFilePath 要创建的文件的路径
     * @param data         待写入的文件数据
     * @param startPos     起始偏移量
     * @param length       要写入的数据长度
     * @return 成功写入文件返回true, 失败返回false
     */
    public static boolean writeFile(String destFilePath, byte[] data, int startPos, int length) {
        try {
            if (!createFile(destFilePath)) {
                return false;
            }
            FileOutputStream fos = new FileOutputStream(destFilePath, true);
            fos.write(data, startPos, length);
            fos.flush();
            fos.close();
            return true;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    /**
     * 从一个输入流里写文件
     *
     * @param destFilePath 要创建的文件的路径
     * @param in           要读取的输入流
     * @return 写入成功返回true, 写入失败返回false
     */
    public static boolean writeFile(String destFilePath, InputStream in) {
        if (in == null || destFilePath == null) {
            return false;
        }
        try {
            if (!createFile(destFilePath)) {
                return false;
            }
            FileOutputStream fos = new FileOutputStream(destFilePath);
            int readCount;
            int len = 1024;
            byte[] buffer = new byte[len];
            while ((readCount = in.read(buffer)) != -1) {
                fos.write(buffer, 0, readCount);
            }
            fos.flush();
            fos.close();
            in.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean appendFile(String filename, byte[] data, int datapos, int datalength) {
        try {
            createFile(filename);
            RandomAccessFile rf = new RandomAccessFile(filename, "rw");
            rf.seek(rf.length());
            rf.write(data, datapos, datalength);
            rf.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    /**
     * 读取文件，返回以byte数组形式的数据
     *
     * @param filePath 要读取的文件路径名
     * @return byte[]
     */
    public static byte[] readFile(String filePath) {
        try {
            if (isFileExist(filePath)) {
                FileInputStream fi = new FileInputStream(filePath);
                return readInputStream(fi);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /****
     * bitmap 转换输入流
     * @return  InputStream
     ***/
    public static InputStream bitmapToInput(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        return new ByteArrayInputStream(baos.toByteArray());
    }

    /**
     * 从一个数量流里读取数据,返回以byte数组形式的数据。
     * </br></br>
     * 需要注意的是，如果这个方法用在从本地文件读取数据时，一般不会遇到问题，但如果是用于网络操作，就经常会遇到一些麻烦(available()方法的问题)。所以如果是网络流不应该使用这个方法。
     *
     * @param in 要读取的输入流
     * @return byte[]
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream in) {
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();

            byte[] b = new byte[in.available()];
            int length = 0;
            while ((length = in.read(b)) != -1) {
                os.write(b, 0, length);
            }

            b = os.toByteArray();

            in.close();
            os.close();
            return b;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读取网络流
     *
     * @param in 网络流
     * @return byte[]
     */
    public static byte[] readNetWorkInputStream(InputStream in) {
        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();

            int readCount = 0;
            int len = 1024;
            byte[] buffer = new byte[len];
            while ((readCount = in.read(buffer)) != -1) {
                os.write(buffer, 0, readCount);
            }

            in.close();
            return os.toByteArray();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                os = null;
            }
        }
        return null;
    }

    /**
     * 将一个文件拷贝到另外一个地方
     *
     * @param sourceFile    源文件地址
     * @param destFile      目的地址
     * @param shouldOverlay 是否覆盖
     * @return boolean
     */
    public static boolean copyFiles(String sourceFile, String destFile, boolean shouldOverlay) {
        try {
            if (shouldOverlay) {
                deleteFile(destFile);
            }
            FileInputStream fi = new FileInputStream(sourceFile);
            writeFile(destFile, fi);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 路径名
     * @return boolean
     */
    public static boolean isFileExist(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 创建一个文件，创建成功返回true
     *
     * @param filePath  文件资地址 没有目录先创建目录
     * @return boolean
     */
    public static boolean createFile(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }

                return file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除一个文件
     *
     * @param filePath 要删除的文件路径名
     * @return true if this file was deleted, false otherwise
     */
    public static boolean deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                return file.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 删除 directoryPath目录下的所有文件，包括删除删除文件夹
     *
     * @param dir  directoryPath目录下的所有文件
     */
    public static void deleteDirectory(File dir) {
        if (dir == null || dir.listFiles() == null) {
            return;
        }
        if (dir.isDirectory()) {
            File[] listFiles = dir.listFiles();
            if (listFiles != null)
                for (int i = 0; i < listFiles.length; i++) {
                    deleteDirectory(listFiles[i]);
                }
        }
        dir.delete();
    }

    /**
     * 删除 directoryPath目录下的所有文件，包括删除删除文件夹
     *
     * @param dir  目录地址
     */
    public static void createDirectory(String dir) {
        if (dir == null) {
            return;
        }
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 流转字符串
     *
     * @param is    流转字符串的流
     * @return String
     */
    public static String inputStream2String(InputStream is) {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuffer buffer = new StringBuffer();
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }


}
