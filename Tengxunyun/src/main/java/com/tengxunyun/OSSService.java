package com.tengxunyun;

import android.content.Context;
import android.util.Log;

import com.tencent.cos.COSClient;
import com.tencent.cos.COSClientConfig;
import com.tencent.cos.COSConfig;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.cos.model.COSRequest;
import com.tencent.cos.model.COSResult;
import com.tencent.cos.model.CreateDirRequest;
import com.tencent.cos.model.CreateDirResult;
import com.tencent.cos.model.DeleteObjectRequest;
import com.tencent.cos.model.DeleteObjectResult;
import com.tencent.cos.model.GetObjectMetadataRequest;
import com.tencent.cos.model.GetObjectMetadataResult;
import com.tencent.cos.model.GetObjectRequest;
import com.tencent.cos.model.PutObjectRequest;
import com.tencent.cos.model.PutObjectResult;
import com.tencent.cos.task.listener.ICmdTaskListener;
import com.tencent.cos.task.listener.IDownloadTaskListener;
import com.tencent.cos.task.listener.IUploadTaskListener;
import com.tengxunyun.config.ParamPreference;
import com.tengxunyun.utils.TencentUtils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created:yangjiang on 2016/11/10 17:48
 * E-Mail:1007181167@qq.com
 * Description: 阿里文件工具类
 */
public final class OSSService {
    private COSClient client;
    private volatile static OSSService ossService;
    private static final String TAG = "OSSService";
    private boolean isDebug = false;
    private String cosPath;

    /**
     * cos sdk 配置设置; 根据需要设置
     */

    // 创建单例getInstance
    public static OSSService getInstance() {
        if (ossService == null) {
            synchronized (OSSService.class) {
                ossService = new OSSService();
            }
        }
        return ossService;
    }

    /***
     * 初始化方式
     *
     * @param context 上下
     ***/
    public void init(Context context) {
        synchronized (this) {
            getOSSClient(context);
        }
    }

    /****
     * d获取
     ***/
    private COSClient getOSSClient(Context context) {
        if (client == null) {
            //持久化 ID，每个 COSClient 需设置一个唯一的 ID 用于持久化保存未完成任务 列表
            // ，以便应用退出重进后能够继续进行上传；传入为 Null，则不会进行持久化保存
            String persistenceId = "AKIDumXZVFgLkAQWQ7O8f084oQq46UilgHlF";
            //创建COSClientConfig对象，根据需要修改默认的配置参数
            COSClientConfig config = new COSClientConfig();
            config.setEndPoint(COSEndPoint.COS_TJ);
            client = new COSClient(context, ParamPreference.TENCENT_COS_APPID, config, persistenceId);
        }
        return client;
    }

    public COSClient getOSSClient() {

        return client;
    }


    /**
     * 获取Bucket名 称
     ***/
    public String getBucket() {
        return ParamPreference.TENCENT_COS_BUCKET;
    }

    /***
     * 创建目录
     ***/

    public void creareDir() {
        CreateDirRequest createDirRequest = new CreateDirRequest();
        createDirRequest.setBucket(getBucket());
        createDirRequest.setCosPath(cosPath);
        Log.d(TAG, TencentUtils.getTencentSign());
        createDirRequest.setSign(TencentUtils.getTencentSign());
        createDirRequest.setListener(new ICmdTaskListener() {
            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
                final CreateDirResult createDirResult = (CreateDirResult) cosResult;
                Log.w(TAG, "目录创建成功： ret=" + createDirResult.code + "; msg=" + createDirResult.msg
                        + "ctime = " + createDirResult.ctime);
            }

            @Override
            public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
                Log.w(TAG, "目录创建失败： ret=" + cosResult.code + "; msg=" + cosResult.msg);
            }
        });
        getOSSClient().createDirAsyn(createDirRequest);
    }

    /***
     * 上传文件
     *
     * @param path    文件本地路径
     * @param fileKey 自己定义在oss的文件fileKey
     * @return boolean true 上传成功
     **/
    public PutObjectResult uploadFile(String path, String fileKey) {
        // 构造上传请求
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(getBucket());
        putObjectRequest.setCosPath(cosPath + "/" + fileKey);
        putObjectRequest.setSrcPath(path);
        putObjectRequest.setSign(TencentUtils.getTencentSign());
        return getOSSClient().putObject(putObjectRequest);
    }

    /***
     * 上传文件
     *
     * @param path                文件本地路径
     * @param fileKey             自己定义在oss的文件fileKey
     * @param iUploadTaskListener 上传进度回调
     * @return OSSAsyncTask  异步任务
     **/
    public void uploadFileAsyn(String path, String fileKey, IUploadTaskListener iUploadTaskListener) {
        PutObjectRequest putObjectRequest = new PutObjectRequest();
        putObjectRequest.setBucket(getBucket());
        putObjectRequest.setCosPath(cosPath + '/' + fileKey);
        putObjectRequest.setSrcPath(path);
        putObjectRequest.setSign(TencentUtils.getTencentSign());
        //设置是否允许覆盖同名文件： "0"，允许覆盖；"1",不允许覆盖；
        putObjectRequest.setInsertOnly("1");
        //设置是否开启分片上传
        putObjectRequest.setSliceFlag(true);//设置是否分片上传：true，分片上传;false,简单文件上传
        putObjectRequest.setSlice_size(1024 * 1024);//分片上传时，分片的大小
//        putObjectRequest.setListener(new IUploadTaskListener() {
//            @Override
//            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
//
//                PutObjectResult result = (PutObjectResult) cosResult;
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append(" 上传结果： ret=" + result.code + "; msg =" + result.msg + "\n");
//                stringBuilder.append(" access_url= " + result.access_url + "\n");
//                stringBuilder.append(" resource_path= " + result.resource_path + "\n");
//                stringBuilder.append(" url= "result.url);
//                Log.w("TEST", stringBuilder.toString();
//            }
//
//            @Override
//            public void onFailed(COSRequest COSRequest, final COSResult cosResult) {
//                Log.w("TEST", "上传出错： ret =" + cosResult.code + "; msg =" + cosResult.msg);
//            }
//
//            @Override
//            public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
//                float progress = (float) currentSize / totalSize;
//                progress = progress * 100;
//                Log.w("TEST", "进度：  " + (int) progress + "%");
//            }
//        });
        putObjectRequest.setListener(iUploadTaskListener);
        getOSSClient().putObjectAsyn(putObjectRequest);
    }

    /**
     * 获取文件信息
     ***/
    public GetObjectMetadataResult getObjectInfo(String fileKey) {
        // 创建同步获取文件元信息请求
        GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest();
        getObjectMetadataRequest.setBucket(getBucket());
        getObjectMetadataRequest.setCosPath(cosPath + "/" + fileKey);
        getObjectMetadataRequest.setSign(TencentUtils.getTencentSign());
        return getOSSClient().getObjectMetadata(getObjectMetadataRequest);
    }

    /**
     * 获取文件信息
     *
     * @param fileKey 文件key
     ***/
    public void getObjectInfoAsyn(String fileKey, ICmdTaskListener iCmdTaskListener) {
        // 创建同步获取文件元信息请求
        GetObjectMetadataRequest getObjectMetadataRequest = new GetObjectMetadataRequest();
        getObjectMetadataRequest.setBucket(getBucket());
        getObjectMetadataRequest.setCosPath(cosPath + "/" + fileKey);
        getObjectMetadataRequest.setSign(TencentUtils.getTencentSign());
        getObjectMetadataRequest.setListener(iCmdTaskListener);
        //      getObjectMetadataRequest.setListener(new ICmdTaskListener() {
//            @Override
//            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
//                GetObjectMetadataResult result = (GetObjectMetadataResult) cosResult;
//                StringBuilder stringBuilder = new StringBuilder();
//                stringBuilder.append("code=" + result.code + "; msg=" +result.msg + "\n");
//                stringBuilder.append("ctime =" +result.ctime + "; mtime=" +result.mtime + "\n" );
//                stringBuilder.append("biz_attr=" + result.biz_attr == null ? "" : result.biz_attr );
//                stringBuilder.append("sha=" + result.sha);
//                Log.w("TEST",stringBuilder.toString());
//            }
//
//            @Override
//            public void onFailed(COSRequest cosRequest, final COSResult cosResult) {
//                Log.w("TEST", cosResult.code+" : "+cosResult.msg);
//            }
//
//        });
        getOSSClient().getObjectMetadataAsyn(getObjectMetadataRequest);
    }

    /****
     * 刪除文件
     *
     * @param key 刪除文件key
     * @return boolean
     ***/
    public boolean deleteFile(String key) {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest();
            deleteObjectRequest.setBucket(getBucket());
            deleteObjectRequest.setCosPath(key);
            deleteObjectRequest.setSign(TencentUtils.getTencentSign());
            DeleteObjectResult result = getOSSClient().deleteObject(deleteObjectRequest);
            return result.code == 200;
        } catch (Exception e) {
            return false;
        }

    }

    /****
     * 刪除文件
     *
     * @param key              刪除文件key
     * @param iCmdTaskListener 删除文件回调
     ***/
    public void deleteFileAsync(String key, ICmdTaskListener iCmdTaskListener) {
        // 创建删除请求
        DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest();
        deleteObjectRequest.setBucket(getBucket());
        deleteObjectRequest.setCosPath(key);
        deleteObjectRequest.setSign(TencentUtils.getTencentSign());
        deleteObjectRequest.setListener(iCmdTaskListener);
//        deleteObjectRequest.setListener(new ICmdTaskListener() {
//            @Override
//            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
//                Log.w("TEST", cosResult.code+" : "+cosResult.msg);
//            }
//
//            @Override
//            public void onFailed(COSRequest COSRequest, COSResult cosResult) {
//                Log.w("TEST", cosResult.code+" : "+cosResult.msg);
//            }
//        });
        getOSSClient().deleteObjectAsyn(deleteObjectRequest);

    }

    /*****
     * 下载文件
     *
     * @param downloadURl 下载路径
     * @param savePath    保存文件路径
     *****/
    public void fileDownAsync(String downloadURl, String savePath, IDownloadTaskListener iDownloadTaskListener) {
        GetObjectRequest getObjectRequest = new GetObjectRequest(downloadURl, savePath);
        getObjectRequest.setSign(null);
        getObjectRequest.setListener(iDownloadTaskListener);
//        getObjectRequest.setListener(new IDownloadTaskListener() {
//            @Override
//            public void onProgress(COSRequest cosRequest, final long currentSize, final long totalSize) {
//                float progress = currentSize / (float) totalSize;
//                progress = progress * 100;
//                progressText.setText("progress =" + (int) (progress) + "%");
//                Log.w("TEST", "progress =" + (int) (progress) + "%");
//            }
//
//            @Override
//            public void onSuccess(COSRequest cosRequest, COSResult cosResult) {
//                Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
//            }
//
//            @Override
//            public void onFailed(COSRequest COSRequest, COSResult cosResult) {
//                Log.w("TEST", "code =" + cosResult.code + "; msg =" + cosResult.msg);
//            }
//        });
        getOSSClient().getObjectAsyn(getObjectRequest);

    }


    /****
     * @param fileKey 文件key
     * @return String  文件路徑
     ***/
    public String getOSSFile(String fileKey) {
        if (fileKey != null && (isHttp(fileKey)
                || fileKey.contains("file://")
                || fileKey.contains("content://")
                || fileKey.contains("drawable://"))
                || new File(fileKey).exists()
                ) {
            return fileKey;
        } else {
            return getOssObjecttUrl() + fileKey;
        }
    }


    /***
     * 域名管理
     **/
    private String getOssObjecttUrl() {
        return "yangjiang-1251471829.costj.myqcloud.com";
    }

    /**
     * 判断url是否为网址
     *
     * @param url
     * @return URL 链接
     */
    private static boolean isHttp(String url) {
        if (null == url) return false;
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(url);
        return matcher.matches();
    }

}
