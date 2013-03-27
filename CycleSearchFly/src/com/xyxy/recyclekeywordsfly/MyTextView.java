/**
 * 重载TextView，使用能够实现自定义动画，动画监听的功能
 */

package com.xyxy.recyclekeywordsfly;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.TextView;

public class MyTextView extends TextView implements AnimatorListener {

	private Context context;			//获取父容器的上下文
	
//	private OnClickListener onClickListener;

	/**
	 * 传统地使用Animation的方法，分别设置ScaleAnimation, AlphaAnimation, TranslateAniamtion, RotateAnimation等
	 */
/*	private ScaleAnimation zero2Normal;
	private ScaleAnimation normal2Zero;
	private AlphaAnimation trans2Opaque;
	private AlphaAnimation opaque2Trans;
	private TranslateAnimation translate;*/	
	
	/**动画加速器*/
	private Interpolator accInterpolator;	
	private Interpolator decInterpolator;
	
/*	private AnimationSet animSet;*/		//动画集，用来加载不用的动画，并按加载的先后顺序播放动画
	
	private static final int YAXIS = 1;	//数组中用来存放Y坐标的索引
	
	private int[] xy = new int[3];		//数组，用来存放TextView的X，Y坐标及长度
	private int yCenter = 0;			//屏幕垂直方向的的中点
	private int duration = 0;			//动画运行时间
	
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public MyTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}
	
	/**
	 * 构造函数
	 * @param context
	 * @param attrs
	 */
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	/**
	 * 构造函数
	 * @param context
	 */
	public MyTextView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	private void init(){
		//this.setOnClickListener(onClickListener);
/*		zero2Normal = new ScaleAnimation(0, 2, 0, 2);
		normal2Zero = new ScaleAnimation(2, 0, 2, 0);
		trans2Opaque = new AlphaAnimation(0.1f, 1.0f);
		opaque2Trans = new AlphaAnimation(1.0f, 0.1f);*/
		accInterpolator = AnimationUtils.loadInterpolator(context,
				android.R.anim.accelerate_interpolator);			//加速加速器
		decInterpolator = AnimationUtils.loadInterpolator(context, 
				android.R.anim.decelerate_interpolator);			//减速加速器
		
		/*animSet = new AnimationSet(true);
		animSet.addAnimation(new AlphaAnimation(0.1f, 0.1f));*/
	}
	
/*	public void setCustomClickListener(OnClickListener listener){
		this.onClickListener = listener;
	}*/

	/**
	 * AnimationListener
	 */
/*	@Override
	public void onAnimationStart(Animation animation) {
		this.setClickable(true);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		this.setOnClickListener(null);
		this.setFocusable(false);
		this.setVisibility(View.GONE);
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}*/
	
	
/*	*//**
	 * 获取AnimationSet实例，设置alpha,translate,duration等参数
	 * @param xy
	 * @param xCenter
	 * @param yCenter
	 * @param type
	 * @return	初始化好的实例animSet
	 *//*
	public AnimationSet setInAnimationSet() {
		AnimationSet animIn = new AnimationSet(true);
		translate = new TranslateAnimation(0, 0, 0, (-xy[YAXIS] + yCenter));
		animIn.addAnimation(trans2Opaque);			//由透明变不透明
		animIn.addAnimation(zero2Normal);			//由小变大
		animIn.addAnimation(translate);
		animIn.setDuration(duration);
		animIn.setInterpolator(decInterpolator);
		return animIn;
	}

	*//**
	 * 获取AnimationSet实例，设置alpha,translate,duration等参数
	 * @param xy
	 * @param xCenter
	 * @param yCenter
	 * @param type
	 * @return	初始化好的实例animSet
	 *//*
	private AnimationSet setOutAnimationSet() {
		AnimationSet animOut = new AnimationSet(true);
		LayoutParams params = (LayoutParams) this.getLayoutParams();
		int xy[] = new int[]{params.leftMargin, params.topMargin, params.width};
		translate = new TranslateAnimation(0,0, 0, (-xy[YAXIS] + yCenter)*2);
		animOut.addAnimation(opaque2Trans);			//由不透明变透明
		animOut.addAnimation(normal2Zero);			//由大变小
		animOut.addAnimation(translate);
		animOut.setDuration(duration);
		animOut.setInterpolator(accInterpolator);
		return animOut;
	}*/

	/**
	 * 启动动画
	 * @param coordiate	获取TextView的坐标和长度信息
	 * @param i			屏幕垂直方向上的中点
	 * @param duration2	动画运行时间
	 */
	public void showAnimation(int[] coordiate, int i, int duration2) {
		this.xy = coordiate;
		this.yCenter = i;
		this.duration = duration2;
		this.setAlpha(0.1f);												//TextView初始化透明度
		/**初始化大小*/
		this.setScaleX(0);		
		this.setScaleY(0);
		/**启动动画，并设置相应参数*/
		this.animate().alpha(1.0f).setInterpolator(decInterpolator)
						.scaleX(1).scaleY(1).setDuration(duration >> 1)
						.translationY((-coordiate[YAXIS] + yCenter))
						.setListener(this);
		/*//animSet.addAnimation(this.setInAnimationSet());
		animSet = this.setInAnimationSet();
		animSet.addAnimation(this.setOutAnimationSet());
		this.startAnimation(animSet);
		this.setClickable(true);
		this.setFocusable(true);
		this.setFocusableInTouchMode(true);
		animSet.setAnimationListener(this);*/
	}

	/**
	 * AnimatorListener
	 */
	@Override
	public void onAnimationStart(Animator animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationEnd(Animator animation) {
		/**通过给两段动画分配不同的delay值来区分，当执行完第一段动画后delay 0.01秒执行第二段动画*/
		if(animation.getStartDelay() == 0){	
			this.animate().alpha(0.1f).setInterpolator(accInterpolator)
				.scaleX(0).scaleY(0).setDuration(duration >> 1).setStartDelay(10)
				.translationY((-xy[YAXIS] + yCenter)*2)
				.setListener(this);
		}
		/**当执行完两段动画后，取消TextView的监听，并设置为不可见，等待被移除*/
		else{
			this.setOnClickListener(null);
			this.setFocusable(false);
			this.setVisibility(View.GONE);
		}
	}

	@Override
	public void onAnimationCancel(Animator animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationRepeat(Animator animation) {
		// TODO Auto-generated method stub
		
	}
	
}
