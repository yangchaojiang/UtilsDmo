package demo.com.aliosslibrary;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.alibaba.sdk.android.oss.model.HeadObjectRequest;
import com.alibaba.sdk.android.oss.model.HeadObjectResult;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.com.aliosslibrary.utils.DownLoadListener;
import demo.com.aliosslibrary.utils.MyOSSCompletedCallback;
import demo.com.aliosslibrary.utils.MyOSSProgressCallback;

/**
 * Created:yangjiang on 2016/11/10 17:48
 * E-Mail:1007181167@qq.com
 * Description: 阿里文件工具类
 */
public class OSSService {

    private OSSClient client;
    @SuppressLint("StaticFieldLeak")
    private volatile static OSSService ossService;
    @SuppressLint("StaticFieldLeak")
    private volatile static Context mApplicationContent;
    private static final String TAG = "OSSService";
    private static String ENDPOINT = "";
    private static String accessKeyId = "LTAIPG2k7kkeF1wh";
    private static String accessKeySecret = "ZTIve12jhiKY7wi79uhNjQgy9c3vEA";
    private boolean isDebug = false;
    private String bucket;//getBucket名称
    private StringBuilder stringBuilder = new StringBuilder();
    private static Properties props = new Properties();

    static {
        try {
            props.load(OSSService.class.getResourceAsStream("/assets/a.properties"));
            ENDPOINT = props.getProperty("ENDPOINT");
            accessKeyId = props.getProperty("LTAIPG2k7kkeF1wh");
            accessKeySecret = props.getProperty("ZTIve12jhiKY7wi79uhNjQgy9c3vEA");
            Log.e(TAG, "加载配置文件ok");
        } catch (IOException e) {
            Log.e(TAG, "加载配置文件失败" + e.getMessage());
        }
    }


    // 创建单例getInstance
    public static OSSService getInstance() {
        if (ossService == null) {
            synchronized (OSSService.class) {
                ossService = new OSSService(mApplicationContent);
            }
        }
        return ossService;
    }

    private OSSService(Context context) {
        getOSSClient(context);
    }

    /***
     * 初始化方式
     *
     * @param app 上下
     ***/
    public static synchronized void init(Application app) {
        mApplicationContent = app.getApplicationContext();
    }

    /****
     *
     * ***/
    private OSSClient getOSSClient(Context context) {
        if (client == null) {
            OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(40 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(40 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            client = new OSSClient(context, ENDPOINT, credentialProvider, conf);
        }
        return client;
    }

    public OSSClient getOSSClient() {
        if (client == null) {
            getOSSClient(mApplicationContent);
        }
        return client;
    }


    /**
     * 获取Bucket名 称
     ***/
    public String getBucket() {
        return bucket;
    }

    /**
     * 设置Bucket名称
     ***/
    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    /***
     * 获取图片url
     *
     * @param imageKey 圖片key
     * @return String  圖片路徑
     */
    public String getImageUrl(String imageKey) {
        if (imageKey != null && (isHttp(imageKey)
                || new File(imageKey).exists()
                || imageKey.contains("file://")
                || imageKey.contains("content://")
                || imageKey.contains("drawable://"))) {
            return imageKey;
        } else {
            return getOSSClient().presignPublicObjectURL(getBucket(), imageKey);
        }
    }

    /***
     * 获取图片url
     *
     * @param imageKey 圖片key
     * @param corners  圆角大小
     * @return String  圖片路徑
     */
    public String getImageUrlRounded(String imageKey, int corners) {
        if (imageKey != null && (isHttp(imageKey)
                || new File(imageKey).exists()
                || imageKey.contains("file://")
                || imageKey.contains("content://")
                || imageKey.contains("drawable://"))) {
            return imageKey;
        } else {
            return getOSSClient().presignPublicObjectURL(getBucket(), imageKey) + "?x-oss-process=image/rounded-corners,r_" + corners;
        }
    }

    /***
     * 获取图片url
     *
     * @param imageKey 圖片key
     * @param with     圆角大小 缩缩略图的宽
     * @param height   圆角大小 缩缩略图的宽
     * @return String  圖片路徑
     */
    public String getImageUrlThumbnail(String imageKey, int with, int height) {
        if (imageKey != null && (isHttp(imageKey)
                || new File(imageKey).exists()
                || imageKey.contains("file://")
                || imageKey.contains("content://")
                || imageKey.contains("drawable://"))) {
            return imageKey;
        } else {
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(0, stringBuilder.length());
            }
            stringBuilder.append(getOSSClient().presignPublicObjectURL(getBucket(), imageKey));
            stringBuilder.append("?x-oss-process=image/resize,");
            stringBuilder.append("w_");
            stringBuilder.append(with);
            stringBuilder.append(",h_");
            stringBuilder.append(height);
            stringBuilder.append("/quality,q_90");
            return stringBuilder.toString();
        }
    }

    /***
     * 获取图片url
     *
     * @param imageKey 圖片key
     * @param with     圆角大小 缩缩略图的宽
     * @param height   圆角大小 缩缩略图的宽
     * @param q        原图质量比 越大越接近原图 0-100
     * @return String  圖片路徑
     */
    public String getImageUrlThumbnail(String imageKey, int with, int height, int q) {
        if (imageKey != null && (isHttp(imageKey)
                || new File(imageKey).exists()
                || imageKey.contains("file://")
                || imageKey.contains("content://")
                || imageKey.contains("drawable://"))) {
            return imageKey;
        } else {
            if (stringBuilder.length() > 0) {
                stringBuilder.delete(0, stringBuilder.length());
            }
            stringBuilder.append(getOSSClient().presignPublicObjectURL(getBucket(), imageKey));
            stringBuilder.append("?x-oss-process=image/resize,");
            stringBuilder.append("w_");
            stringBuilder.append(with);
            stringBuilder.append(",h_");
            stringBuilder.append(height);
            stringBuilder.append("/quality,q_");
            stringBuilder.append(q);
            return stringBuilder.toString();
        }
    }

    /***
     * 获取图片url
     *
     * @param with   圆角大小 缩缩略图的宽
     * @param height 圆角大小 缩缩略图的宽
     * @return String  圖片路徑
     */
    public String getImageUrlVideoThumbnail(String path, int with, int height) {
        if (!path.equals("oss.geneqiao.com") || !path.equals("copy.oss.4coder.cn")) {
            return path;
        }
        stringBuilder.append(path);
        stringBuilder.append("?x-oss-process=image/resize,");
        stringBuilder.append("w_");
        stringBuilder.append(with);
        stringBuilder.append(",h_");
        stringBuilder.append(height);
        stringBuilder.append("/quality,q_90");
        return stringBuilder.toString();
    }

    /***
     * 获取头像图片url
     *
     * @param imageKey 图片key
     */
    public String getHeadImageUrl(String imageKey) {
        if (imageKey != null && (isHttp(imageKey)
                || imageKey.contains("file://")
                || imageKey.contains("content://")
                || imageKey.contains("drawable://"))) {
            return imageKey;
        } else {
            return getOSSClient().presignPublicObjectURL(getBucket(), imageKey) + "?x-oss-process=image/resize,w_300,h_300/quality,q_90";
        }
    }


    /***
     * 上传文件
     *
     * @param path    文件本地路径
     * @param fileKey 自己定义在oss的文件fileKey
     * @return boolean true 上传成功
     **/
    public boolean uploadFile(String path, final String fileKey) throws ClientException, ServiceException {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(getBucket(), fileKey, path);
        getInstance().getOSSClient().putObject(put);
        return true;
    }

    /***
     * 上传文件
     *
     * @param path                文件本地路径
     * @param fileKey             自己定义在oss的文件fileKey
     * @param ossProgressCallback 上传进度回调  都在子线程
     * @return OSSAsyncTask  异步任务
     **/
    public OSSAsyncTask<PutObjectResult> uploadFile(String path, final String fileKey, final MyOSSProgressCallback<String> ossProgressCallback, final MyOSSCompletedCallback<String> completedCallback) {
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(getBucket(), fileKey, path);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest putObjectRequest, long currentSize, long totalSize) {
                if (ossProgressCallback != null) {
                    ossProgressCallback.onProgress(fileKey, (currentSize * 100 / totalSize), totalSize);
                }
            }
        });
        // task.cancel(); // 可以取消任务
        // task.waitUntilFinished(); // 可以等待直到任务完成
        return getInstance().getOSSClient().asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest putObjectRequest, PutObjectResult putObjectResult) {
                if (completedCallback != null) {
                    completedCallback.onSuccess(putObjectResult.getServerCallbackReturnBody(), putObjectRequest.getObjectKey());
                }
            }

            @Override
            public void onFailure(PutObjectRequest putObjectRequest, ClientException clientExcepion, ServiceException serviceException) {
                if (completedCallback != null) {
                    if (clientExcepion.getMessage().contains("cancelled")) {
                        completedCallback.onCancelled();
                    } else {
                        completedCallback.onFailure(clientExcepion.getMessage(), serviceException.getErrorCode());
                    }
                }
            }
        });
    }

    /**
     * 获取文件信息
     ***/
    public OSSAsyncTask getObjectInfo(String fileKey, final MyOSSCompletedCallback<ObjectMetadata> completedCallback) {
        // 创建同步获取文件元信息请求
        HeadObjectRequest head = new HeadObjectRequest(getBucket(), fileKey);
        return getInstance().getOSSClient().asyncHeadObject(head, new OSSCompletedCallback<HeadObjectRequest, HeadObjectResult>() {
            @Override
            public void onSuccess(HeadObjectRequest headObjectRequest, HeadObjectResult headObjectResult) {
                if (completedCallback != null) {
                    completedCallback.onSuccess(headObjectResult.getMetadata(), headObjectRequest.getObjectKey());
                }
            }

            @Override
            public void onFailure(HeadObjectRequest headObjectRequest, ClientException clientExcepion, ServiceException serviceException) {
                if (completedCallback != null) {
                    if (clientExcepion.getMessage().contains("cancelled")) {
                        completedCallback.onCancelled();
                    } else {
                        completedCallback.onFailure(clientExcepion.getMessage(), serviceException.getErrorCode());
                    }
                }
            }
        });
    }

    /****
     * 刪除文件
     *
     * @param key 刪除文件key
     * @return boolean
     ***/
    public boolean deleteFile(Context context, String key) {
        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest(getBucket(), key);
        try {
            getOSSClient(context).deleteObject(delete);
            return true;
        } catch (ClientException | ServiceException e) {//错误记录下文件key 下次继续删除
            e.printStackTrace();
            Log.e(TAG, "deleteFile" + e.getMessage());
            return false;
        }
    }

    /****
     * 刪除文件
     *
     * @param key               刪除文件key
     * @param completedCallback 删除文件回调
     ***/
    public void deleteFileAsync(Context context, String key, final MyOSSCompletedCallback<Integer> completedCallback) {
        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest(getBucket(), key);
        getOSSClient(context).asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest deleteObjectRequest, DeleteObjectResult deleteObjectResult) {
                if (completedCallback != null) {
                    completedCallback.onSuccess(deleteObjectResult.getStatusCode(), deleteObjectRequest.getObjectKey());

                }
            }

            @Override
            public void onFailure(DeleteObjectRequest deleteObjectRequest, ClientException clientExcepion, ServiceException serviceException) {
                if (completedCallback != null) {
                    if (clientExcepion.getMessage().contains("cancelled")) {
                        completedCallback.onCancelled();
                    } else {
                        completedCallback.onFailure(clientExcepion.getMessage(), serviceException.getErrorCode());
                    }
                }
            }
        });

    }

    /*******
     * 下载文件
     *
     * @param key              文件key
     * @param downLoadListener 下载文件回调
     *****/
    public OSSAsyncTask downLoadAsync(String key, final DownLoadListener<InputStream> downLoadListener) {
        GetObjectRequest get = new GetObjectRequest(getBucket(), key);
        return getOSSClient().asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                InputStream inputStream = result.getObjectContent();
                if (downLoadListener != null) {
                    downLoadListener.onSuccess(inputStream, result.getContentLength());
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                if (downLoadListener != null) {
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace();
                        if (clientExcepion.getMessage().contains("cancelled")) {
                            downLoadListener.onCancelled();
                        } else {
                            downLoadListener.onFailure(clientExcepion.getMessage(), clientExcepion.getMessage());
                        }

                    }

                    if (serviceException != null) {
                        // 服务异常
                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                        downLoadListener.onFailure(serviceException.getErrorCode(), serviceException.getRawMessage());
                    }
                }
            }
        });
    }

    /*******
     * 下载文件
     *
     * @param key              文件key
     * @param savePath         保存路径
     * @param downLoadListener 下载文件回调
     *****/
    public OSSAsyncTask downLoadAsync(String key, final String savePath,final MyOSSProgressCallback<String> downLoadListener) {
        GetObjectRequest get = new GetObjectRequest(getBucket(), key);
        return getOSSClient().asyncGetObject(get, new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                // 请求成功
                InputStream inputStream = result.getObjectContent();
                if (downLoadListener != null) {
                    // 请求成功
                    long length = result.getContentLength();
                    byte[] buffer = new byte[2048];
                    int readCount;//记录已经写入多少
                    long wiCount = 0;
                    try {
                        while ((readCount = inputStream.read(buffer)) != -1) {
                            // 处理下载的数据
                            writeFile(savePath, buffer, 0, readCount);
                            Log.i("headObject", "字节" + readCount + "总共长度" + length + "--进度：" + readCount * 100 / length);
                            wiCount = wiCount + readCount;
                            if (wiCount > 1024 * 200) {
                                downLoadListener.onProgress(savePath, (readCount * 100 / length), length);
                            }
                        }
                        inputStream.close();
                        downLoadListener.onSuccess(savePath, request.getObjectKey());
                    } catch (IOException e) {
                        e.printStackTrace();
                        downLoadListener.onFailure(e, e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                if (downLoadListener != null) {
                    // 请求异常
                    if (clientExcepion != null) {
                        // 本地异常如网络异常等
                        clientExcepion.printStackTrace();
                        if (clientExcepion.getMessage().contains("cancelled")) {
                            downLoadListener.onCancelled();
                        } else {
                            downLoadListener.onFailure(clientExcepion, clientExcepion.getMessage());
                        }
                    }
                    if (serviceException != null) {
                        // 服务异常
                        Log.e("ErrorCode", serviceException.getErrorCode());
                        Log.e("RequestId", serviceException.getRequestId());
                        Log.e("HostId", serviceException.getHostId());
                        Log.e("RawMessage", serviceException.getRawMessage());
                        downLoadListener.onFailure(serviceException, serviceException.getRawMessage());
                    }
                }
            }
        });
    }

    /****
     * @param fileKey 文件key
     * @return String  文件路徑
     ***/
    public String getOSSFile(String fileKey) {
        if (fileKey != null && (isHttp(fileKey)
                || fileKey.contains("file://")
                || fileKey.contains("content://")
                || fileKey.contains("drawable://"))) {
            return fileKey;
        } else {
            return getOSSClient().presignPublicObjectURL(getBucket(), fileKey);
          // return getOssClientUrl() + fileKey;
        }
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

    /**
     * 将数据写入一个文件
     *
     * @param destFilePath 要创建的文件的路径
     * @param data         待写入的文件数据
     * @param startPos     起始偏移量
     * @param length       要写入的数据长度
     * @return 成功写入文件返回true, 失败返回false
     */
    private boolean writeFile(String destFilePath, byte[] data, int startPos, int length) {
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
     * 创建一个文件，创建成功返回true
     *
     * @param filePath
     * @return
     */
    private boolean createFile(String filePath) {
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
}
