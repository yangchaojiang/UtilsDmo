
# 集微博、微信和QQ第三方登录及分享功能的**轻量**库

### 配置

##### 权限配置

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```

##### 第三方平台信息配置

```xml
<!--QQ-->
<activity
    android:name="com.tencent.tauth.AuthActivity"
    android:launchMode="singleTask"
    android:noHistory="true">
    <intent-filter>
        <action android:name="android.intent.action.VIEW" />

        <category android:name="android.intent.category.DEFAULT" />
        <category android:name="android.intent.category.BROWSABLE" />
        <!-- 需要改写此处的appId，将tencent后面xxx的改为自己申请的appId，如tencent129068312-->
        <data android:scheme="tencentxxx" />
    </intent-filter>
</activity>

<!-- Weixin-->
<activity android:name=".wxapi.WXEntryActivity"
    android:exported="true"
    android:screenOrientation="portrait"
    android:theme="@android:style/Theme.NoDisplay"/>

<!-- Weibo-->
```

**其中```WXEntryActivity```位于```程序包名.wxapi```下，继承自```co.lujun.tpsharelogin.platform.weixin.AssistActivity```**，如下：

```java
package co.lujun.sample.wxapi;
import co.lujun.tpsharelogin.platform.weixin.AssistActivity;

public class WXEntryActivity extends AssistActivity {
}
```

### 使用

##### 初始化 ```TPManager```

```java
TPManager.getInstance().initAppConfig("", "", "", "", "", "", "");
```

**参数分别为微博回调地址、微博```APP KEY```、微博```APP SECRET```、QQ```APPID```、QQ```APPSECRET```、微信```APPID```、微信```APPSECRET```**

##### 登录及分享

分别提供了```QQManager```、```WXManager```和```WBManager```用于 QQ、微信及微博的登录与分享。设置```StateListener<T>```（必须）用于登录/分享回调

###### QQ登录及分享

```java
QQManager qqManager = new QQManager(this);
StateListener<String> qqStateListener = new StateListener<String>() {
    @Override
    public void onComplete(String s) {
        Log.d(TAG, s);
    }

    @Override
    public void onError(String err) {
        Log.d(TAG, err);
    }

    @Override
    public void onCancel() {
        Log.d(TAG, "onCancel()");
    }
};
qqManager.setListener(qqStateListener);
```
```java
//QQ登录
qqManager.onLoginWithQQ();
```
```java
//QQ分享
QQShareContent contentQQ = new QQShareContent();
contentQQ.setShareType(QQShare.SHARE_TO_QQ_TYPE_DEFAULT)
        .setShareExt(QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        .setTitle("TPShareLogin Test")
        .setTarget_url("http://lujun.co")
        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
        .setSummary("This is TPShareLogin test, 4 qq!");
qqManager.share(contentQQ);
```

```setShareType(int param)```方法:

* ```QQShare.SHARE_TO_QQ_TYPE_DEFAULT``` (图文消息，默认)
* ```QQShare.SHARE_TO_QQ_TYPE_IMAGE``` (本地图片)

```setShareExt(int param)```方法，默认对话列表且显示QZone按钮:

* ```QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN``` (分享到QQ客户端时默认QZone)
* ```QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE``` (分享到QQ客户端对话列表不显示QZone按钮)

###### 微信登录及分享

```java
WXManager wxManager = new WXManager(this);
wxManager.setListener(StateListener<String> wxStateListener);
//微信登录
wxManager.onLoginWithWX();
//微信分享
WXShareContent contentWX = new WXShareContent();
contentWX.setScene(WXShareContent.WXSession)
        .setWeb_url("http://lujun.co")
        .setTitle("WebTitle")
        .setDescription("Web description, description, description")
        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
        .setType(WXShareContent.share_type.WebPage);
wxManager.share(contentWX);
```

```setScene(int param)```方法:
* ```WXShareContent.WXSession``` (分享到微信客户端时对话列表，默认)
* ```WXShareContent.WXTimeline``` (分享到微信朋友圈)

###### 微博登录及分享

```java
WBManager wbManager = new WBManager(this);
wbManager.setListener(StateListener<String> wbStateListener);
//微博登录
wbManager.onLoginWithWB();
//微博分享
WBShareContent contentWB = new WBShareContent();
contentWB.setShare_method(WBShareContent.COMMON_SHARE)
        .setContent_type(WBShareContent.WEBPAGE)
        .setShare_type(Config.SHARE_CLIENT)
        .setStatus("This is TPShareLogin test, 4 weibo!@whilu ")
        .setImage_url("http://lujun-wordpress.stor.sinaapp.com/uploads/2014/09/lujun-375x500.jpg")
        .setTitle("title")
        .setDescription("description")
        .setActionUrl("http://lujun.co")
        .setDataUrl("http://lujun.co")
        .setDadtaHdUrl("http://lujun.co")
        .setDefaultText("default action");
wbManager.share(contentWB);
```

```setShare_method(int param)```方法，一般使用客户端进行分享:

* ```WBShareContent.COMMON_SHARE``` (调用客户端分享，默认)
* ```WBShareContent.API_SHARE``` (API分享，不会调用客户端，分享回调到当前应用进行)

```setShare_type(int param)```方法，一般不需要特别指定:

* ```Config.SHARE_CLIENT``` (单条分享，默认)
* ```Config.SHARE_ALL_IN_ONE``` (多种类型集合分享)

**注意授权登录第三方平台返回的信息，返回的数据格式为json字符串，如下：**

```xml
{
  "user_data":{
       //这里面是返回的用户数据信息
  },
  "verify_data":{
       //这里面是返回的认证信息，包括可能有的access_token、openid等(各个平台根据实际情况而定)
  }
}
```

更多详细使用请见[Sample](https://github.com/whilu/TPShareLogin/tree/master/sample)示例。

## 注意事项

##### 依赖库冲突问题

本库使用了[Retrofit v1.9.0](https://github.com/square/retrofit)、[RxAndroid v1.0.1](https://github.com/ReactiveX/RxAndroid)及[RxJava v1.0.14](https://github.com/ReactiveX/RxJava)等库，若你的项目中也使用了这些依赖库并发生了冲突，请在添加本库依赖时进行操作：

```xml
dependencies {
    compile ('co.lujun:tpsharelogin:1.0.3'){
        exclude module:'retrofit'
        exclude module:'rxjava'
        exclude module:'rxandroid'
    }
}
```

##### 混淆

```xml
-keep class com.tencent.mm.sdk.** {*;}
-keep class com.sina.**{*;}
-keep class * extends android.app.Dialog
-keep class retrofit.** { *; }
-keepattributes Signature
-keepattributes Exceptions
```

## 感谢

* [ShareLoginLib](https://github.com/lingochamp/ShareLoginLib)
* [微信开发文档](https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&lang=zh_CN)
* [微博开发文档](http://open.weibo.com/wiki/%E9%A6%96%E9%A1%B5)
* [QQ开发文档](http://wiki.connect.qq.com/)

## Change logs

###1.1.0(2017-3-11)

- 修复微博登录崩溃问题

###1.0.4(2016-5-22)

- 修复QQ登录无法回调问题

## 关于

如您有任何问题，请联系我：[lujun.byte#gmail.com](mailto:lujun.byte@gmail.com).

## License

    Copyright 2015 lujun

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.