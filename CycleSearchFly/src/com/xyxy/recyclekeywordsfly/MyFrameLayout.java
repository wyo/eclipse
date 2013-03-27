/**
 * 重载FrameLayout，实现获取子VIEW的数量以及对子VIEW的清理等功能
 */

package com.xyxy.recyclekeywordsfly;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class MyFrameLayout extends FrameLayout implements OnGlobalLayoutListener{

	public static int width;		//获取FrameLayout的宽度
	public static int height;		//获取FrameLayout的高度
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	public MyFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyFrameLayout(Context context) {
		super(context);
		init();
	}
	
	/**
	 * 移除可见性为GONE的所有Child view
	 * Note:一定使用index从大到小的顺序移除
	 */
	public void clear(){
		int childSize = getChildCount();
		for(int i = (childSize - 1); i >= 0; i--){					
			final TextView goneTextView = (TextView) getChildAt(i);
			if(goneTextView.getVisibility() == View.GONE){
				removeView(goneTextView);
			}
		}
	}
	
	/**
	 * 移除此FrameLayout上所有的子view
	 * Note:一定使用index从大到小的顺序移除
	 */
	public void clearAllView(){
		int childSize = getChildCount();
		for(int i = (childSize - 1); i >= 0; i--){
			removeViewAt(i);
		}
	}
	
	/**
	 * 获取子View的数量
	 */
	@Override
	public int getChildCount() {
		return super.getChildCount();
	}
	
	/**
	 * 初始化，添加OnGloaBalLayoutListener，以获Layout的尺寸
	 */
	private void init(){
		getViewTreeObserver().addOnGlobalLayoutListener(this);	//添加OnGlobalLayoutListenr监听器
	}
	
	/**
	 * 通过实现onGlobalLayoutListener接口方法获得layout的尺寸
	 */
	@Override
	public void onGlobalLayout() {
		int tempWidth = getWidth();
		int tempHeight = getHeight();
		if(tempWidth != width){
			width = tempWidth;
			//Log.d("MultiThread", "tempWidth:" + tempWidth + " Width:" + width);
		}
		if(tempHeight != height){
			height = tempHeight;
			//Log.d("MultiThread", "tempHeight:" + tempHeight + " Height:" + height);
		}
	}
}
