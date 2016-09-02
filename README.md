引用方式：
===

在root的build.gradle的allprojects下添加maven { url "https://jitpack.io" }
例子如下：
```c
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

然后在app/build.gradle下的dependencies中，添加compile 'com.github.tqwboy:LoopScrollBanner:v1.1'，例子如下：
```c
dependencies {
	compile 'com.github.tqwboy:LoopScrollBanner:v1.1'
}
```

使用方法：
===
具体请参考项目中的例子和代码
