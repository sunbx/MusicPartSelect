# MusicPartSelect
这是一款音乐轨迹选择控件
****

|Author|JM|
|---|---
|E-mail|283122529@qq.com
|QQ群|588530304

****

添加方式
    
    allprojects {
	    repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  	dependencies {
	   compile 'com.github.sunbx:MusicPartSelect:v1.0.1'
	}

使用方法

    <mps.jm.com.musicselectlibrar.MusicPartSelectView
        android:id="@+id/mps_view"
        android:layout_width="match_parent"
        android:layout_height="120dp"
        android:background="@mipmap/icon_wg"
    />
    

数据设置

    /**
     * Sets data. 设置数据
     *
     * @param musicPath the music path 音乐的本地路径
     * @param partPath  the part path 音轨图本地路径
     * @param videoTime the video time 视频的时长(秒)
     * @param listener  the listener 回调
     */
     mps_view.setData();
 
