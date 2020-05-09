-optimizationpasses 5                                                           # 指定代码的压缩级别
-dontusemixedcaseclassnames                                                     # 是否使用大小写混合
-dontskipnonpubliclibraryclassmembers
-dontskipnonpubliclibraryclasses                                                # 是否混淆第三方jar
-dontpreverify                                                                  # 混淆时是否做预校验
-verbose                                                                        # 混淆时是否记录日志
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*        # 混淆时所采用的算法
-ignorewarnings
-dontoptimize
-dontwarn

-keep public class * extends android.app.Activity                               # 保持哪些类不被混淆
-keep public class * extends android.app.Application                            # 保持哪些类不被混淆
-keep public class * extends android.app.Service                                # 保持哪些类不被混淆
-keep public class * extends android.content.BroadcastReceiver                  # 保持哪些类不被混淆
-keep public class * extends android.content.ContentProvider                    # 保持哪些类不被混淆
-keep public class * extends android.app.backup.BackupAgentHelper               # 保持哪些类不被混淆
-keep public class * extends android.preference.Preference                      # 保持哪些类不被混淆
-keep public class com.android.vending.licensing.ILicensingService              # 保持哪些类不被混淆





# Do not strip any method/class that is annotated with @DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
#
-dontwarn javassist.util.HotSwapper.**
-dontwarn com.sun.jdi.**
-dontwarn org.junit.internal.runners.statements.**
-dontwarn org.junit.rules.**
-dontwarn android.test.**
-dontwarn javassist.tools.rmi.**
# Keep native methods
-keepclassmembers class * {
    native <methods>;
}

-dontwarn okio.**
-dontwarn javax.annotation.**

#share sdk
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class **.R$* {*;}
-keep class **.R{*;}

-keep class com.mob.**{*;}
-dontwarn com.mob.**
-dontwarn cn.sharesdk.**
-dontwarn **.R$*
#share sdk end

-dontwarn com.neovisionaries.ws.client.**
-keep class com.neovisionaries.ws.client.**{*;}

-dontwarn java.net.**
-keep class java.net.**{*;}

-dontwarn org.apache.**                        #不警告此包
-keep class org.apache.** {*;}                 #保留此包下代码不进行混淆

#Retrofit混淆
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions

-dontwarn com.squareup.okhttp.**
-keep interface com.squareup.okhttp.** { *; }
-keep class com.squareup.okhttp.** { *;}
-keep class okio.**{*;}
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okio.**

# Rxjava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

# autolayout
-keep class com.zhy.autolayout.** { *; }
-keep interface com.zhy.autolayout.** { *; }

# RxLifeCycle
-keep class com.trello.rxlifecycle.** { *; }
-keep interface com.trello.rxlifecycle.** { *; }

#Canary
-dontwarn com.squareup.haha.guava.**
-dontwarn com.squareup.haha.perflib.**
-dontwarn com.squareup.haha.trove.**
-dontwarn com.squareup.leakcanary.**
-keep class com.squareup.haha.** { *; }
-keep class com.squareup.leakcanary.** { *; }

#EventBus 混淆
#-libraryjars libs/eventbus.jar
-keep class de.greenrobot.**{*;}
-keepclassmembers class ** {
    public void onEvent*(**);
}
  -keepattributes *Annotation*
  -keepclassmembers class ** {
      @org.greenrobot.eventbus.Subscribe <methods>;
  }
  -keep enum org.greenrobot.eventbus.ThreadMode {
        *;
  }
#EventBus end
-keep class rx.**{*;}
-dontwarn  rx.**

# greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
#greenDao end


#Jpush  混淆
#-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
#Jpush end


# U-App 混淆
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keep public class com.qtimes.wonly.R$*{
public static final int *;
}
# U-App end



# 混淆
-dontwarn myjava.awt.datatransfer.**
-keep class myjava.awt.datatransfer.** {*;}
-dontwarn org.apache.harmony.**
-keep class org.apache.harmony.** {*;}
-keep class org.msgpack.**{*;}
-dontwarn org.msgpack.**

# 混淆
-dontwarn com.loopj.android.http.**
-keep class com.loopj.android.http.** {*;}

# 混淆
-dontwarn cz.msebera.**
-keep class cz.msebera.** {*;}

# 混淆
-dontwarn com.sun.mail.**
-keep class com.sun.mail.** {*;}
-dontwarn javax.mail.**
-keep class javax.mail.** {*;}

# 视屏相关jar
-keep class org.webrtc.**{*;}
-keep class de.tavendo.autobahn.**{*;}

# opencv
-keep class org.opencv.**{*;}

# ProGuard configurations for Bonree-Agent
     -keep public class com.bonree.**{*;}
     -keep public class bonree.**{*;}
     -dontwarn com.bonree.**
     -dontwarn bonree.**
# End Bonree-Agent

#去gosn start
-keep class com.google.** {
    <fields>;
    <methods>;
}
-keepclassmembers class * extends java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# Gson specific classes
-keep class sun.misc.Unsafe {
    <fields>;
    <methods>;
}
 -keep class com.google.gson.stream.** { *; }
# Application classes that will be serialized/deserialized over Gson
-keep class com.qtimes.domain.bean.**{
    <fields>;
    <methods>;
}

-keep class com.qtimes.domain.enums.**{
    <fields>;
    <methods>;
}

-keep class com.qtimes.service.wonly.PersonStatusData{
    <fields>;
    <methods>;
}

-keep class com.qtimes.data.bean.**{
    <fields>;
    <methods>;
}

#DSTracker
-keep class cc.qtimes.dsort.**{
    <fields>;
    <methods>;
}



-keep class com.qtimes.utils.event.**{
    <fields>;
    <methods>;
}

-keep class com.qtimes.wonly.bean.**{
    <fields>;
    <methods>;
}

-keep class com.qtimes.wonly.bean.dao.**{
    <fields>;
    <methods>;
}

#去gosn end

# ButterKnife 混淆
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
# ButterKnife end



# Only required if you use AsyncExecutor
-keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

-keepclasseswithmembernames class * {                                           # 保持 native 方法不被混淆
    native <methods>;
}

-keepclasseswithmembers class * {                                               # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);     # 保持自定义控件类不被混淆
}

#自定义view
-keep public class * extends android.view.View{
    *** get*();
    void set*(***);
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {                        # 保持自定义控件类不被混淆
   public void *(android.view.View);
}

-keepclassmembers enum * {                                                      # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {                                # 保持 Parcelable 不被混淆
  public static final android.os.Parcelable$Creator *;
}
-keepnames class * implements java.io.Serializable
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}



-keepclassmembers class **.R$* {                                                # 保持R文件不被混淆
    public static <fields>;
}
-keep class com.alipay.android.app.IAliPay{*;}                                  # 保持某个接口或类不被混淆
-keepattributes *Annotation*
-keep class com.nineoldandroids**{*;}
-keep class com.longzhu.tga.IFragmentArguments{*;}
-keep class com.android.app.view**{*;}                                          # 保持某个包下所有的类不被混淆


-keep class com.android.app.CommonCallback{                                     # 保持某个类或接口的公有和保护类型的成员和方法不被混淆
    public protected <fields>;
    public protected <methods>;
}

-keep class com.android.app.Platform$*{                                         # 保持内部类不被混淆
     *;
}
#-keep class  com.android.app.** implements  com.android.app.Platform$ICallback {*;}   # 保持内部接口不被混淆

-dontwarn android.support.v4.**
-keep class android.support.v4.**{*;}
-keep interface android.support.v4.app.**{*;}
-keep public class * extends android.support.v4.**
-keep public class * extends android.app.Fragment
-keepattributes SourceFile,LineNumberTable

-keep class cn.jpush.** { *; }
-keep class com.google.**{*;}     #去混淆, 在jpush-android-2.0.5.jar中com.google已混淆过了
-keep class com.google.protobuf.** {*;}
-keep class com.samsung.**{*;}
-keep class com.sec.**{*;}
-keep class com.google.gson.** {*;}



-assumenosideeffects class android.util.Log {
        public static *** d(...);
        public static *** e(...);
        public static *** i(...);
        public static *** v(...);
        public static *** println(...);
        public static *** w(...);
        public static *** wtf(...);
}

-keep interface logging.client.**{*;}


-dontwarn rx.internal.util.**





#不混淆v8下所有代码
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class android.support.v8.renderscript.** { *; }



#Gaode AMap
#3D 地图 V5.0.0之后：
-keep   class com.amap.api.maps.**{*;}
-keep   class com.autonavi.**{*;}
-keep   class com.amap.api.trace.**{*;}

#定位
-keep class com.amap.api.location.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}

#搜索
-keep   class com.amap.api.services.**{*;}

#2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}

#导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}


#UMeng
-keep class com.umeng.** {*;}
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.qtimes.wonly.R$*{
    public static final int *;
}