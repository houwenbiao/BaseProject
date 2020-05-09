# WonlySmart-RKNN项目

### Warning toast一定要少用，尽量不用

### 添加http请求repository流程
    1.data/net/service 目录下创建service接口
    2.domain/repository目录下创建repository接口
    3.data/repository目录下创建repositoryImpl实现类,注意:construct使用Inject注解
    4.data/module/ApiModule下提供provider和map
    5.useCase的construct使用Inject注解
    
### Dagger activity 传参注意事项
    在initdata 中 QtTicketTypeActivity.inject(this);

###  butterknife的OnLongClick需要返回boolean类型

### 系统签名
    1.manifests中添加 android:sharedUserId="android.uid.system"
    2.java -Djava.library.path=../toybrick/out/host/linux-x86/lib64/ -jar signapk.jar platform.x509.pem platform.pk8 in.apk out.apk

### gradle-wrapper 配置
    distributionUrl=https\://services.gradle.org/distributions/gradle-4.10.1-all.zip
