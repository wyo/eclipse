package com.xyxy.flysearchwords;

import java.util.Random;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity implements OnClickListener{

	 public static final String[] keywords = { "QQ", "Sodino", "APK", "GFW", "铅笔",//  
         "短信", "桌面精灵", "MacBook Pro", "平板电脑", "雅诗兰黛",//  
         "卡西欧 TR-100", "笔记本", "SPY Mouse", "Thinkpad E40", "捕鱼达人",//  
         "内存清理", "地图", "导航", "闹钟", "主题",//  
         "通讯录", "播放器", "CSDN leak", "安全", "3D",//  
         "美女", "天气", "4743G", "戴尔", "联想",//  
         "欧朋", "浏览器", "愤怒的小鸟", "mmShow", "网易公开课",//  
         "iciba", "油水关系", "网游App", "互联网", "365日历",//  
         "脸部识别", "Chrome", "Safari", "中国版Siri", "A5处理器",//  
         "iPhone4S", "摩托 ME525", "魅族 M9", "尼康 S2500" };  
	 
	 private FlyWords flyWords;
	 private Button btnIn, btnOut;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		btnIn = (Button) findViewById(R.id.btnIn);
		btnOut = (Button) findViewById(R.id.btnOut);
		btnIn.setOnClickListener(this);
		btnOut.setOnClickListener(this);
		
		flyWords = (FlyWords) findViewById(R.id.custFrameLayout01);
		flyWords.setDuration(801);
		flyWords.setOnItemClickListener(this);
		
		feedKeywords(flyWords, keywords);
	}

	private void feedKeywords(FlyWords flyWords, String[] keywords) {
		Random random = new Random();
		for(int i = 0 ; i != FlyWords.MAX; i++){
			int ran = random.nextInt(keywords.length);
			String keyword = keywords[ran];
			flyWords.feedKeywords(keyword);
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnIn) {  
            flyWords.clearKeywords();  
            feedKeywords(flyWords, keywords);  
            flyWords.go2Show(FlyWords.ANIMATION_IN);  
        } else if (v == btnOut) {  
        	flyWords.clearKeywords();  
            feedKeywords(flyWords, keywords);  
            flyWords.go2Show(FlyWords.ANIMATION_OUT);    
        } else if (v instanceof TextView) {  
            String keyword = ((TextView) v).getText().toString();  
            Intent intent = new Intent();  
            intent.setAction(Intent.ACTION_VIEW);  
            intent.addCategory(Intent.CATEGORY_DEFAULT);  
            intent.setData(Uri.parse("http://www.google.com.hk/#q=" + keyword));  
            startActivity(intent);  
        }  
	}

}
