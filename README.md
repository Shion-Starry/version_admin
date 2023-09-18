## 合并翻译
合并线上翻译步骤

1. 下载线上翻译，命名为CloudTranslation.csv
2. 将csv放到 sdk_string/translation 目录
3. 在根目录执行
```shell 
./gradlew :sdk_string:mergeTranslation
```

<br>

## 自增构建号
- 构建版本号配置文件路径，自动根据版本号自增构建号
- 只要保证路径可读写，文件不存在时会自动创建文件
- 配置文件中已经根据内测标志做了区分无需配置不同的文件
- 本地调试留空即可
- 建议打包机的配置路径(与签名文件处于相同目录) /private/android/config/app.properties

## 打包命令

命令主要分两种，

1. 内测打包，用于测试阶段打包使用
2. 生产打包，用于发布阶段打包使用

<br>

### 内测打包
```shell
# 替换推送配置 app目录下执行
# dev/testing
ln -fs google-services-staging.json google-services.json

# 打包 项目根目录执行
./gradlew clean assembleRelease -PBUILD_NUMBER_CONFIG=/private/android/config/app.properties
```

<br>

### 生产打包
需要同时输出APK和AAB包，
1. APK包用于自由渠道分发以及直接安装使用
2. AAB包用于GooglePlay渠道分发，安装命令在下面
> ！！！打包前先合并翻译！！！
```shell
# 替换推送配置 app目录下执行
# prod
ln -fs google-services-prod.json google-services.json

# 打包(使用代码中设置的版本号) 项目根目录执行
./gradlew clean assembleRelease bundleRelease --refresh-dependencies -PDESTY_DEBUG=false -PBUILD_NUMBER_CONFIG=/private/android/config/app.properties

# 注入修改版本号
./gradlew clean assembleRelease bundleRelease --refresh-dependencies -PDESTY_DEBUG=false -PMAJOR_VERSION=$MajorVersion -PSUB_VERSION=$SubVersion -PFIX_VERSION=$FixVersion -PBUILD_NUMBER_CONFIG=/private/android/config/app.properties

keyStorePath=你的证书地址
keyStorePassword=证书密码
keyAlias=钥匙别名
keyPassword=钥匙密码
# aab包转apks包
java -jar bundletool.jar build-apks --bundle=xxx.aab --output=xxx.apks --ks=${keyStorePath} --ks-pass=pass:${keyStorePassword} --ks-key-alias=${keyAlias} --key-pass=pass:${keyPassword}
# 给设备安装apks包
java -jar bundletool.jar install-apks --apks=xxx.apks
```