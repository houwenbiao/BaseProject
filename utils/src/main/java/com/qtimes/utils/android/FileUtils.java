package com.qtimes.utils.android;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;

import androidx.annotation.NonNull;

import android.text.TextUtils;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by liuj on 2016/6/24.
 */
public class FileUtils {

    /**
     * 判断外部存储卡是否可用
     */
    public static boolean isExternalStorageAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    /**
     * 获取磁盘缓存文件
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 根据string.xml资源格式化字符串
     *
     * @param context
     * @param resource
     * @param args
     * @return
     */
    public static String formatResourceString(Context context, int resource, Object... args) {
        String str = context.getResources().getString(resource);
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        return String.format(str, args);
    }

    /**
     * 读取assets文件
     *
     * @return 每行作为list的一个元素
     */
    public static List<String> getAssetsFileList(Context context, String fileName) {
        try {
            List<String> list = new ArrayList();
            InputStream in = context.getResources().getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String str = null;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPath(Context context) {
        String path;
        boolean hasSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        String packageName = context.getPackageName() + File.separator + "/appCache/";
        if (hasSDCard) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + packageName;
        } else {
            return null;
        }
        File file = new File(path);
        boolean isExist = file.exists();
        if (!isExist) {
            file.mkdirs();
        }
        return file.getPath();
    }

    /**
     * 获取拍照相片存储文件
     *
     * @param context
     * @return
     */
    public static File createFile(Context context) {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(Environment.getExternalStorageDirectory() +
                    File.separator + timeStamp + ".jpg");
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(cacheDir, timeStamp + ".jpg");
        }
        return file;
    }


    /**
     * 获取应用sd卡中的目录
     */
    public static String getAppPath(Context context) {
        String path;
        boolean hasSDCard = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        String packageName = context.getPackageName();
        if (hasSDCard) {
            path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                    File.separator + packageName;
        } else {
            return null;
        }
        File file = new File(path);
        boolean isExist = file.exists();
        if (!isExist) {
            file.mkdirs();
        }
        return file.getPath();
    }

    /**
     * 获取应用sd卡中的目录
     */
    public static String getAppPathFile(Context context, String path) {
        StringBuilder appPath = new StringBuilder(getAppPath(context));
        if (!path.startsWith("/")) {
            appPath.append(File.separator);
        }
        appPath.append(path);
        File file = new File(appPath.toString());
        boolean isExist = file.getParentFile().exists();
        if (!isExist) {
            file.getParentFile().mkdirs();
        }
        return appPath.toString();
    }

    /**
     * 获取拍照相片存储文件
     */
    public static File createTimeJpgFile(Context context) {
        File file;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(Environment.getExternalStorageDirectory() +
                    File.separator + timeStamp + ".jpg");
        } else {
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(cacheDir, timeStamp + ".jpg");
        }
        return file;
    }

    /**
     * 获取文件夹大小
     *
     * @param file File实例
     * @return long
     */
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //return size/1048576;
        return size;
    }

    /**
     * 读取本地json文件中的数据，判断本设备是否在白名单中
     *
     * @return -1,检测失败；0，不在白名单中；1，在白名单中
     */
    public static int checkWhiteList(Context context, String filePath) {
        int canUseHard = -1;
        InputStream inStream = null;
        try {
            inStream = context.getAssets().open(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream));
            StringBuilder builder = new StringBuilder();
            String buf;
            do {
                buf = reader.readLine();
                builder.append(buf != null ? buf : "");
            } while (buf != null);

            JSONArray jsyList = new JSONArray(builder.toString());
            if (jsyList.length() > 0) {
                String deviceModel = Build.MODEL.toLowerCase(); // 机型（手机型号）
                canUseHard = 0;
                for (int i = 0; i < jsyList.length(); i++) {
                    boolean canUse = jsyList.getString(i).toLowerCase().contains(deviceModel);
                    if (canUse) {
                        canUseHard = 1;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            canUseHard = -1;
        }
        finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return canUseHard;
    }

    /**
     * 获取缓存路径
     *
     * @return
     */
    public static File getCacheDir(Context context) {
        if (isExternalStorageAvailable()) {
            return context.getExternalCacheDir();
        } else {
            return context.getFilesDir();
        }
    }

    /**
     * @param fileDir  文件目录
     * @param fileType 后缀名
     * @return 特定目录下的所有后缀名为fileType的文件列表
     */
    public static List<String> getFiles(File fileDir, String fileType) throws Exception {
        List<String> lfile = new ArrayList<String>();
        File[] fs = fileDir.listFiles();
        for (File f : fs) {
            if (f.isFile()) {
                if (fileType
                        .equals(f.getName().substring(
                                f.getName().lastIndexOf(".") + 1,
                                f.getName().length()))) {
                    lfile.add(f.getName());
                }
            }
        }
        return lfile;
    }

    public static String getLoadPatchName(File fileDir, String fileType, String versionCode) throws Exception {
        List<String> files = getFiles(fileDir, fileType);
        int maxPatchVersion = 0;
        for (String name : files) {
            if (name.startsWith(versionCode + "_")) {
                int patchVersion = Integer.valueOf(name.substring(name.indexOf("_") + 1, name.indexOf(".")));
                maxPatchVersion = Math.max(maxPatchVersion, patchVersion);
            }
        }
        return String.valueOf(maxPatchVersion);
    }

    public static String readAssetsString(Context context, String fileName) {
        InputStream inputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            String line = null;
            bufferedReader = new BufferedReader(new InputStreamReader(context.getAssets().open(fileName)));
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return stringBuilder.toString();
    }


    /**
     * 注意此ResponseBody为okHttp3版本下的
     *
     * @param body
     * @param path
     * @return
     */
    public static boolean writeResponseBodyToDisk(@NonNull okhttp3.ResponseBody body, @NonNull String path) {
        try {
            File futureStudioIconFile = new File(path);
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                byte[] fileReader = new byte[4096];
                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);
                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            }
            finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 创建根目录
     *
     * @param filePath
     */
    public static boolean makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                return file.mkdir();
            } else {
                return true;
            }
        } catch (Exception e) {
            PluLog.i("Make dir " + filePath + " error");
        }
        return false;
    }

    /**
     * 递归创建目录
     *
     * @param filePath
     */
    public static boolean makeDirectories(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                boolean is = file.mkdirs();
                if (is) {
                    PluLog.i("Make dir success");
                } else {
                    PluLog.i("Make dir failed");
                }
                return is;
            } else {
                PluLog.i("The dir is exist");
                return true;
            }
        } catch (Exception e) {
            PluLog.i("Make dir " + filePath + " error");
        }
        return false;
    }


    private static final int WRITE_BUFFER_SIZE = 1024 * 8;

    /**
     * 复制文件
     *
     * @param fromFileStream     原始文件流
     * @param destinationDirPath 目标文件夹路径
     * @throws Exception
     */
    public static File copyFile(InputStream fromFileStream, String destinationDirPath, String rName) throws Exception {
        File destDir = new File(destinationDirPath);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        File destFile = new File(destDir, rName);
        BufferedInputStream fromBufferedStream = null;
        FileOutputStream destStream = null;
        byte[] buffer = new byte[WRITE_BUFFER_SIZE];
        try {
            fromBufferedStream = new BufferedInputStream(fromFileStream);
            destStream = new FileOutputStream(destFile);
            int bytesRead;
            while ((bytesRead = fromBufferedStream.read(buffer)) > 0) {
                destStream.write(buffer, 0, bytesRead);
            }
        }
        finally {
            try {
                if (fromFileStream != null) {
                    fromFileStream.close();
                }
                if (fromBufferedStream != null) {
                    fromBufferedStream.close();
                }
                if (destStream != null) {
                    destStream.close();
                }
            } catch (IOException e) {
                throw new IOException("Error closing IO resources.", e);
            }
        }
        return destFile;
    }


    /**
     * 解压zip 文件到指定文件夹
     *
     * @param zipFile
     * @param destination
     * @throws IOException
     */
    public static void unzipFile(File zipFile, String destination) throws IOException {
        FileInputStream fileStream = null;
        BufferedInputStream bufferedStream = null;
        ZipInputStream zipStream = null;
        try {
            fileStream = new FileInputStream(zipFile);
            bufferedStream = new BufferedInputStream(fileStream);
            zipStream = new ZipInputStream(bufferedStream);
            ZipEntry entry;

            File destinationFolder = new File(destination);
            destinationFolder.mkdirs();

            byte[] buffer = new byte[WRITE_BUFFER_SIZE];
            while ((entry = zipStream.getNextEntry()) != null) {
                String fileName = entry.getName();
                File file = new File(destinationFolder, fileName);
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    File parent = file.getParentFile();
                    if (!parent.exists()) {
                        parent.mkdirs();
                    }
                    FileOutputStream fout = new FileOutputStream(file);
                    try {
                        int numBytesRead;
                        while ((numBytesRead = zipStream.read(buffer)) != -1) {
                            fout.write(buffer, 0, numBytesRead);
                        }
                    }
                    finally {
                        fout.close();
                    }
                }
                long time = entry.getTime();
                if (time > 0) {
                    file.setLastModified(time);
                }
            }
        }
        finally {
            try {
                if (zipStream != null) {
                    zipStream.close();
                }
                if (bufferedStream != null) {
                    bufferedStream.close();
                }
                if (fileStream != null) {
                    fileStream.close();
                }
            } catch (IOException e) {
                throw new IOException("Error closing IO resources.", e);
            }
        }
    }


    /**
     * 解压zip文件，使用的是anj.jar包的api,当系统api解压失败时，可尝试使用该方法
     */
    public static boolean unzipFile(@NonNull String zipPath, @NonNull String outPutFile) {
        File file = new File(zipPath);
        org.apache.tools.zip.ZipFile zipFile = null;
        try {
            zipFile = new org.apache.tools.zip.ZipFile(file);
            File f = new File(outPutFile);
            if (!f.exists()) {
                f.mkdir();
            }
            Enumeration entrys = zipFile.getEntries();
            int i = 0;
            while (entrys.hasMoreElements()) {
                org.apache.tools.zip.ZipEntry zipEntry = (org.apache.tools.zip.ZipEntry) entrys.nextElement();
                if (zipEntry == null) {
                    continue;
                }
                if (zipEntry.isDirectory()) {
                    continue;
                }
                BufferedInputStream input = new BufferedInputStream(
                        zipFile.getInputStream(zipEntry));
                BufferedOutputStream output = new BufferedOutputStream(
                        new FileOutputStream(outPutFile + zipEntry.getName()));

                int len;
                byte[] bytes = new byte[1024];
                while ((len = input.read(bytes)) > 0) {
                    output.write(bytes, 0, len);
                    output.flush();
                }
                output.close();
                input.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            File f = new File(outPutFile);
            if (f.exists()) {
                FileUtils.delFile(f);
            }
            return false;
        }
        finally {
            if (null != zipFile) {
                try {
                    zipFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return true;
    }

    /**
     * 输入一个字符串文本
     *
     * @param content  文本内容
     * @param filePath 文本保存路径
     * @throws IOException
     */
    public static void writeStringToFile(String content, String filePath) throws IOException {
        PrintWriter out = null;
        try {
            out = new PrintWriter(filePath);
            out.print(content);
        }
        finally {
            if (out != null) {
                out.close();
            }
        }
    }


    /**
     * 递归删除某文件夹下的所有文件
     *
     * @param file
     */
    public static void delFile(@NonNull File file) {
        if (file.isFile() || file.list().length == 0) {
            file.delete();
        } else {
            File[] files = file.listFiles();
            for (File f : files) {
                delFile(f);
                f.delete();
            }
        }
        PluLog.i("Delete file success");
    }


    /**
     * 跟据文件名，获取context.getExternalFilesDir下的路径
     * 如果sd卡不可用时，返回""
     *
     * @param context
     * @param dirName
     * @return
     */
    public static String getAppFilesDirPath(@NonNull Context context, @NonNull String dirName) {
        if (isExternalStorageAvailable()) {
            File dir = context.getExternalFilesDir(dirName);
            if (dir != null) {
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                return dir.getAbsolutePath();
            }
        }
        return "";
    }

    public static String getAppFilePath(@NonNull Context context, @NonNull String fileName) {
        if (isExternalStorageAvailable()) {
            String dir = getAppFilesDirPath(context, "");
            if (!TextUtils.isEmpty(dir)) {
                File file = new File(dir + File.separator + fileName);
                if (dir != null) {
                    return file.getPath();
                }
            }
        }
        return "";
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     * 单位MB
     *
     * @return
     */

    public static long getSDAvailableSize() {
        if (isExternalStorageAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long free_memory = 0; //return value is in bytes
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                free_memory = stat.getAvailableBlocksLong() * stat.getBlockSizeLong();
            } else {
                free_memory = stat.getAvailableBlocks() * stat.getBlockSize();
            }
            PluLog.d("getSDAvailableSize " + free_memory / 1024 / 1024);
            return free_memory / 1024 / 1024;
        } else {
            PluLog.d("getSDAvailableSize " + 0);
            return 0;
        }
    }

    /**
     * 判断文件夹是否是存放动画的
     */
    public static boolean isHasAnimFile(File file, String giftIcon) {
        if (TextUtils.isEmpty(giftIcon) || file == null) {
            return false;
        }

        try {
            File giftFile = new File(file.getAbsolutePath() + "/" + giftIcon);
            return isHasAnim(giftFile);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断文件夹中是否有动画文件
     */
    public static boolean isHasAnim(File giftFile) {
        return giftFile != null && giftFile.length() > 1
                && giftFile.listFiles() != null
                && giftFile.listFiles().length > 0;
    }

    /**
     * 判断文件是否存在
     */
    public static boolean isLwfExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        try {
            File file = new File(filePath);
            return file != null && file.length() > 1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 读取本地文件中JSON字符串
     *
     * @param filePath
     */
    public static String getFileJson(String filePath) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream inputStream = new FileInputStream(filePath);
            BufferedReader bf = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }


    //获取目录下所有文件
    public static List<File> getAllFile(Context context, int group) {
        String CacheFilePath = context.getExternalCacheDir().getAbsolutePath();
        String dir = CacheFilePath + "/" + group + "/";
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            return null;
        }
        File[] files = dirFile.listFiles();
        List<File> result = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                result.add(files[i]);
            }
        }
        return result;
    }

    public static void bitmapToFile(Context context, int group, String name, Bitmap bitmap) {

        try {
            String CacheFilePath = context.getExternalCacheDir().getAbsolutePath();
            String groupFilePath = CacheFilePath + "/" + group + "/";
            File groupFile = new File(groupFilePath);
            if ("0".equals(name)) {
                deleteDirectory(groupFilePath);
            }
            if (!groupFile.exists() || !groupFile.isDirectory()) {
                groupFile.mkdirs();
            }
            File file = new File(groupFilePath + name + ".jpg");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
//            Logger.e("save dir= " + file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteGroup(Context context, int group) {
        try {
            String CacheFilePath = context.getExternalCacheDir().getAbsolutePath();
            String groupFilePath = CacheFilePath + "/" + group + "/";
            deleteDirectory(groupFilePath);
        } catch (Exception e) {

        }
    }

    public static void appStringToFile(String fileName, String content) {
        FileWriter writer = null;
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            writer = new FileWriter(fileName, true);
            writer.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteAllGroup(Context context) {
        try {
            String CacheFilePath = context.getExternalCacheDir().getAbsolutePath();
            String groupFilePath = CacheFilePath + "/";
            deleteDirectory(groupFilePath);
        } catch (Exception e) {
            PluLog.e(e);
        }
    }

    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            PluLog.i("Delete file failed," + fileName + " not exist！");
            return false;
        } else {
            if (file.isFile()) {
                return deleteFile(fileName);
            } else {
                return deleteDirectory(fileName);
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
//                PluLog.i("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                PluLog.e("Delete file " + fileName + "failed！");
                return false;
            }
        } else {
            PluLog.i("Delete file failed, " + fileName + "not exist！");
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator)) {
            dir = dir + File.separator;
        }
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            PluLog.i("Delete path failed, " + dir + "not exist！");
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag) {
                    break;
                }
            }
        }
        if (!flag) {
            PluLog.i("Delete path failed！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            PluLog.i("Delete path " + dir + " success！");
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据byte数组生成文件
     *
     * @param bytes 生成文件用到的byte数组
     */
    public static void createFileWithByte(byte[] bytes, String filePath) {
        // TODO Auto-generated method stub
        /**
         * 创建File对象，其中包含文件所在的目录以及文件的命名
         */
        File file = new File(filePath);
        // 创建FileOutputStream对象
        FileOutputStream outputStream = null;
        // 创建BufferedOutputStream对象
        BufferedOutputStream bufferedOutputStream = null;
        try {
            // 如果文件存在则删除
            if (file.exists()) {
                file.delete();
            }
            // 在文件系统中根据路径创建一个新的空文件
            file.createNewFile();
            // 获取FileOutputStream对象
            outputStream = new FileOutputStream(file);
            // 获取BufferedOutputStream对象
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // 往文件所在的缓冲输出流中写byte数据
            bufferedOutputStream.write(bytes);
            // 刷出缓冲输出流，该步很关键，要是不执行flush()方法，那么文件的内容是空的。
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // 打印异常信息
            e.printStackTrace();
        }
        finally {
            // 关闭创建的流对象
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }
}


