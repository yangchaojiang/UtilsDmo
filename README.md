# UtilsDmo
 
 ##依赖
`compile 'com.ycjiang:yutils:1.0.0'`

JsonManager  json解析  采用 fastjson

TimeUtils  时间类
除了时间格式化，提供用于视频的时长，和语音的时长 时间格式化

YUtils 类 例如
获取屏幕宽高。 Toast 提示（防止重复显示） 是否有网络
手机是否有虚拟导航，状态栏等 单位互相转换dp,px,sp 


```java

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        YUtils.initialize(this);
        YUtils.setGravity(Gravity.CENTER);
    }
}
public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button2:
                Log.d("MainActivity","asdasda");
                YUtils.Toast("哈哈");
                break;
            case R.id.button3:
                YUtils.Toast(TimeUtils.getDataTimeCNString(new Date()));
                break;
            case R.id.button4:
                YUtils.Toast("是否虚拟导航键："+YUtils.isNavigationBarExist2(this));
                break;
            case R.id.button5:
                  String lists= JsonManager.beanToJson(list);
                YUtils.Toast("json："+lists);
                break;
            case R.id.button6:
                CountDownTimer cc= TimeUtils.countDown(this, 60, 1, new TimeUtils.CountDownListener() {
                    @Override
                    public void onFinish(String text) {
                        YUtils.Toast("onFinish："+text);
                    }

                    @Override
                    public void onTick(long millisUntilFinished, String text) {
                        YUtils.Toast("onTick："+text);
                    }
                });
                break;
            case R.id.button7:
                YUtils.Toast("视频时长："+TimeUtils.getVideoTime(100));
                break;
            case R.id.button8:
                YUtils.Toast("视频文件："+YUtils.formatFileSizeAll(1500000L));
                break;
        }
        ···
   更多用法请看类库说明，方法都有注释
 
