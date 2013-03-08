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

	 public static final String[] keywords = { "QQ", "Sodino", "APK", "GFW", "Ǧ��",//  
         "����", "���澫��", "MacBook Pro", "ƽ�����", "��ʫ����",//  
         "����ŷ TR-100", "�ʼǱ�", "SPY Mouse", "Thinkpad E40", "�������",//  
         "�ڴ�����", "��ͼ", "����", "����", "����",//  
         "ͨѶ¼", "������", "CSDN leak", "��ȫ", "3D",//  
         "��Ů", "����", "4743G", "����", "����",//  
         "ŷ��", "�����", "��ŭ��С��", "mmShow", "���׹�����",//  
         "iciba", "��ˮ��ϵ", "����App", "������", "365����",//  
         "����ʶ��", "Chrome", "Safari", "�й���Siri", "A5������",//  
         "iPhone4S", "Ħ�� ME525", "���� M9", "�῵ S2500" };  
	 
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
