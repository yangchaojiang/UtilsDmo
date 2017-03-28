# UtilsDmo
 
 ##依赖
`compile 'com.ycjiang:yutils:1.0.0'`
JsonManager  json解析  采用 fastjson

TimeUtils  时间帮类
除了时间格式化，提供用于视频的时长，和语音的时长的时间格式化

YUtils  类例如
获取屏幕宽高。 Toast 提示（防止重复显示） 是否有网络
手机是否有虚拟导航，状态栏等 单位互相转换dp,px,sp 


` `` Java
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        YUtils.initialize(this);
        YUtils.setGravity(Gravity.CENTER);
    }
}
 
