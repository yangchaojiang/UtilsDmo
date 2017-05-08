package com.xunfeimodule;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.GrammarListener;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

/**
 * Created by yangc on 2017/5/6.
 * E-Mail:yangchaojiang@outlook.com
 * Deprecated:  讯飞语音帮助类
 */
public class SpeechUtilityManager {
    public static final String TAG = "SpeechUtilityManager";


    /****
     * 初始化讯飞语音
     ***/
    public static void init(Application application, String appId) {
        SpeechUtility.createUtility(application, SpeechConstant.APPID + "= " + appId);
    }


    /***
     * 语音输入 UI 控件 RecognizerDialog 可以用于语音听写、语法识别和语义理解
     *
     * @param context                   acitvity 级别的上下
     * @param mInitListener             初始化回调 可以传null
     * @param mRecognizerDialogListener 语音录制回调
     **/
    public RecognizerDialog showRecognizerDialog(Context context, InitListener mInitListener, RecognizerDialogListener mRecognizerDialogListener) {
        //1.创建RecognizerDialog对象
        RecognizerDialog mDialog = new RecognizerDialog(context, mInitListener);
        // 2.设置accent、language等参数
        mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
        //3. 若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解 //结果
        mDialog.setParameter("asr_sch", "1");
        mDialog.setParameter("nlp_version", "2.0");
        //4.设置回调接口
        mDialog.setListener(mRecognizerDialogListener);
        //5.显示dialog，接收语音输入
        mDialog.show();
        return mDialog;
    }

    /******
     * 语音听写  听写主要指将连续语音快速识别为文字的过程，科大讯飞语音听写能识别通用常见的语句、词 汇，而且不限制说
     *
     * @param context             activity 级别的上下
     * @param mInitListener       初始化回调 可以传null
     * @param mRecognizerListener 听写回调
     * @return ErrorCode  识别结果,错误码
     ****/
    public int speechRecognizer(Context context, InitListener mInitListener, RecognizerListener mRecognizerListener) {
        //1.创建SpeechRecognizer对象，第二个参数：本地识别时传InitListener
        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(context, mInitListener);
        // 2.设置听写参数，详见《MSC Reference Manual》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 3.开始听写
        return mIat.startListening(mRecognizerListener);
    }

    /***
     * 在线命令词识别
     *
     * @param context             activity 级别的上下
     * @param mInitListener       初始化回调 可以传null
     * @param mRecognizerListener 听写回调
     * @return ErrorCode  识别结果,错误码
     ***/
    public int setDistinguish(Context context, InitListener mInitListener, RecognizerListener mRecognizerListener) {
        // 在线命令词识别，不启用终端级语法
        // 1.创建SpeechRecognizer对象
        SpeechRecognizer mAsr = SpeechRecognizer.createRecognizer(context, mInitListener);
        // 2.设置参数
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
        mAsr.setParameter(SpeechConstant.SUBJECT, "asr");
        // 3.开始识别
        return mAsr.startListening(mRecognizerListener);

    }

    /***
     * 终端级命令词识别
     *
     * @param context             activity 级别的上下
     * @param mInitListener       初始化回调 可以传null
     * @param mCloudGrammar       ABNF语法
     * @param grammarId           设置该 Grammar ID
     * @param mGrammarListener    //构建语法监听器
     * @param mRecognizerListener 听写回调
     * @return ErrorCode  识别结果,错误码
     ***/
    public int setDistinguish(Context context, String mCloudGrammar, String grammarId, GrammarListener mGrammarListener, InitListener mInitListener, RecognizerListener mRecognizerListener) {
        // 在线命令词识别，启用终端级语法 // ABNF语法示例
        //  String mCloudGrammar = "#ABNF 1.0 UTF-8; languagezh-CN;  mode voice; root $main; $main = $place1 到$place2 ; $place1 = 北京 | 武汉 | 南京 | 天津 | 天京 | 东京; $place2 = 上海 | 合肥; ";
        // 1.创建SpeechRecognizer对象
        SpeechRecognizer mAsr = SpeechRecognizer.createRecognizer(context, mInitListener);
        // 2.构建语法文件 mAsr.setParameter(SpeechConstant.TEXT_ENCODING, "utf-8");
        int ret = mAsr.buildGrammar("abnf", mCloudGrammar, mGrammarListener);
        if (ret != ErrorCode.SUCCESS) {
            Log.d(TAG, "语法构建失败,错误码：" + ret);
        } else {
            return ret;
        }
        // 3.设置参数
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, "cloud");
        mAsr.setParameter(SpeechConstant.CLOUD_GRAMMAR, grammarId);
        // 4.开始识别,
        return mAsr.startListening(mRecognizerListener);
    }

    /***
     * 离线命令词识别  本地识别
     *
     * @param context             activity 级别的上下
     * @param mInitListener       初始化回调 可以传null
     * @param mRecognizerListener 听写回调
     * @return ErrorCode  识别结果,错误码
     ***/
    public int setOfflineDistinguish(Context context, InitListener mInitListener, RecognizerListener mRecognizerListener) {
        //1.创建 SpeechRecognizer 对象，需传入初始化监听器
        SpeechRecognizer mAsr = SpeechRecognizer.createRecognizer(context, mInitListener);
        // 初始化监听器，只有在使用本地语音服务时需要监听（即安装《语记》，通过《语记》提供本地 服务），初始化成功后才可进行本地操作。
        // 2.构建语法（本地识别引擎目前仅支持 BNF 语法），同在线语法识别 请参照 Demo。
        // 3.开始识别,设置引擎类型为本地
        mAsr.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 设置本地识别使用语法 id(此 id 在语法文件中定义)、门限值
        mAsr.setParameter(SpeechConstant.LOCAL_GRAMMAR, "call");
        mAsr.setParameter(SpeechConstant.ASR_THRESHOLD, "30");
        return mAsr.startListening(mRecognizerListener);

    }

    /***
     * 语音合成 使用云端的情况下不需要监听即可使用，本地需要监听
     *
     * @param context       activity 级别的上下
     * @param mInitListener 初始化回调  初始化监听器,同听写初始化监听器，使用云端的情况下不需要监听即可使用，本地需要监听
     * @param body          需要合成文字
     * @param mSynListener  合成回调
     * @return ErrorCode  识别结果,错误码
     ***/
    public int setSpeechSSynthesis(Context context, String body, InitListener mInitListener, SynthesizerListener mSynListener) {
        //1.创建 SpeechSynthesizer 对象
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, mInitListener);
        // 初始化监听器,同听写初始化监听器，使用云端的情况下不需要监听即可使用，本地需要监听
        // 2.合成参数设置 //设置引擎类型为本地
        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
        // 可跳转到《语记》发音人设置页面进行发音人下载
        SpeechUtility.getUtility().openEngineSettings(SpeechConstant.ENG_TTS);
        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50"); // 设置语速
        mTts.setParameter(SpeechConstant.VOLUME, "80");//设置音量，范围 0~100
        //设置合成音频保存位置（可自定义保存位置），保存在“./sdcard/iflytek.pcm”
        // 保存在 SD 卡需要在 AndroidManifest.xml 添加写 SD 卡权限
        // 仅支持保存为 pcm 和 wav 格式，如果不需要保存合成音频，注释该行代码
        // mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, "./sdcard/iflytek.pcm"
        //3.开始合成
        return mTts.startSpeaking(body, mSynListener);

    }

    /***
     * 语音语义理解
     *
     * @param context               activity 级别的上下
     * @param mInitListener         初始化回调  初始化监听器,同听写初始化监听器，使用云端的情况下不需要监听即可使用，本地需要监听
     * @param mUnderstanderListener 语义理解回调
     * @return ErrorCode  识别结果,错误码
     */
    public int setSpeechUnderstander(Context context, InitListener mInitListener, SpeechUnderstanderListener mUnderstanderListener) {
        //1.创建文本语义理解对象
        SpeechUnderstander understander = SpeechUnderstander.createUnderstander(context, mInitListener);
        // 2.设置参数，语义场景配置请登录 http://osp.voicecloud.cn/
        understander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 3.开始语义理解
        return understander.startUnderstanding(mUnderstanderListener);
    }

    /***
     * 文本语义理解
     *
     * @param context        activity 级别的上下
     * @param mInitListener  初始化回调null  初始化监听器,同听写初始化监听器
     * @param body           语义理解文本
     * @param searchListener 语义理解回调
     * @return ErrorCode  识别结果,错误码
     */
    public int setTextUnderstander(Context context, String body, InitListener mInitListener, TextUnderstanderListener searchListener) {
        //1.创建文本语义理解对象
        TextUnderstander understander = TextUnderstander.createTextUnderstander(context, mInitListener);
        // 2.设置参数，语义场景配置请登录 http://osp.voicecloud.cn/
        understander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 3.开始语义理解
        return understander.understandText(body, searchListener);
    }

    /*****
     * 实例
     *******/
// 识别监听器
    private RecognizerListener mRecognizerListener = new RecognizerListener() {
        // 音量变化
        public void onVolumeChanged(int volume, byte[] data) {
        }

        //  开始说话
        @Override
        public void onBeginOfSpeech() {

        }

        // 结束说话
        @Override
        public void onEndOfSpeech() {

        }

        // 返回结果
        @Override
        public void onResult(final RecognizerResult result, boolean isLast) {
        }

        //  错误回调
        @Override
        public void onError(SpeechError error) {
        }

        //  事件回调
        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
        }
    };
    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {
        // 会话结束回调接口，没有错误时，error为null  public void onCompleted(SpeechError error) {}
        // 缓冲进度回调
        // percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在 文本中结束位置，info为附加信息。
        @Override
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
        }

        // 开始播放
        @Override
        public void onSpeakBegin() {
        }

        // 暂停播放
        @Override
        public void onSpeakPaused() {
        }

        // 播放进度回调
        // percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文 本中结束位置.
        @Override
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
        }

        @Override
        public void onCompleted(SpeechError speechError) {

        }

        // 恢复播放回调接口
        @Override
        public void onSpeakResumed() {
        }

        // 会话事件回调接口
        @Override
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
        }
    };
    // XmlParser为结果解析类，请参照Demo
    // private SpeechUnderstanderListener mUnderstanderListener = new SpeechUnderstanderListener(){
    // public void onResult(UnderstanderResult result) {    String text = result.getResultString();  }
    // public void onError(SpeechError error) {}//会话发生错误回调接口
    // public void onBeginOfSpeech() {}//开始录音
    // public void onVolumeChanged(int volume, byte[] data){} // volume音量值0~30，data音频数据
    // public void onEndOfSpeech() {}//结束录音
    // public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {}//扩展用接口 };
//
}
