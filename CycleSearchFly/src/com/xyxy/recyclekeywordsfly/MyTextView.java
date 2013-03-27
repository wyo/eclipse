/**
 * ����TextView��ʹ���ܹ�ʵ���Զ��嶯�������������Ĺ���
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

	private Context context;			//��ȡ��������������
	
//	private OnClickListener onClickListener;

	/**
	 * ��ͳ��ʹ��Animation�ķ������ֱ�����ScaleAnimation, AlphaAnimation, TranslateAniamtion, RotateAnimation��
	 */
/*	private ScaleAnimation zero2Normal;
	private ScaleAnimation normal2Zero;
	private AlphaAnimation trans2Opaque;
	private AlphaAnimation opaque2Trans;
	private TranslateAnimation translate;*/	
	
	/**����������*/
	private Interpolator accInterpolator;	
	private Interpolator decInterpolator;
	
/*	private AnimationSet animSet;*/		//���������������ز��õĶ������������ص��Ⱥ�˳�򲥷Ŷ���
	
	private static final int YAXIS = 1;	//�������������Y���������
	
	private int[] xy = new int[3];		//���飬�������TextView��X��Y���꼰����
	private int yCenter = 0;			//��Ļ��ֱ����ĵ��е�
	private int duration = 0;			//��������ʱ��
	
	
	/**
	 * ���캯��
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
	 * ���캯��
	 * @param context
	 * @param attrs
	 */
	public MyTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	/**
	 * ���캯��
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
				android.R.anim.accelerate_interpolator);			//���ټ�����
		decInterpolator = AnimationUtils.loadInterpolator(context, 
				android.R.anim.decelerate_interpolator);			//���ټ�����
		
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
	 * ��ȡAnimationSetʵ��������alpha,translate,duration�Ȳ���
	 * @param xy
	 * @param xCenter
	 * @param yCenter
	 * @param type
	 * @return	��ʼ���õ�ʵ��animSet
	 *//*
	public AnimationSet setInAnimationSet() {
		AnimationSet animIn = new AnimationSet(true);
		translate = new TranslateAnimation(0, 0, 0, (-xy[YAXIS] + yCenter));
		animIn.addAnimation(trans2Opaque);			//��͸���䲻͸��
		animIn.addAnimation(zero2Normal);			//��С���
		animIn.addAnimation(translate);
		animIn.setDuration(duration);
		animIn.setInterpolator(decInterpolator);
		return animIn;
	}

	*//**
	 * ��ȡAnimationSetʵ��������alpha,translate,duration�Ȳ���
	 * @param xy
	 * @param xCenter
	 * @param yCenter
	 * @param type
	 * @return	��ʼ���õ�ʵ��animSet
	 *//*
	private AnimationSet setOutAnimationSet() {
		AnimationSet animOut = new AnimationSet(true);
		LayoutParams params = (LayoutParams) this.getLayoutParams();
		int xy[] = new int[]{params.leftMargin, params.topMargin, params.width};
		translate = new TranslateAnimation(0,0, 0, (-xy[YAXIS] + yCenter)*2);
		animOut.addAnimation(opaque2Trans);			//�ɲ�͸����͸��
		animOut.addAnimation(normal2Zero);			//�ɴ��С
		animOut.addAnimation(translate);
		animOut.setDuration(duration);
		animOut.setInterpolator(accInterpolator);
		return animOut;
	}*/

	/**
	 * ��������
	 * @param coordiate	��ȡTextView������ͳ�����Ϣ
	 * @param i			��Ļ��ֱ�����ϵ��е�
	 * @param duration2	��������ʱ��
	 */
	public void showAnimation(int[] coordiate, int i, int duration2) {
		this.xy = coordiate;
		this.yCenter = i;
		this.duration = duration2;
		this.setAlpha(0.1f);												//TextView��ʼ��͸����
		/**��ʼ����С*/
		this.setScaleX(0);		
		this.setScaleY(0);
		/**������������������Ӧ����*/
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
		/**ͨ�������ζ������䲻ͬ��delayֵ�����֣���ִ�����һ�ζ�����delay 0.01��ִ�еڶ��ζ���*/
		if(animation.getStartDelay() == 0){	
			this.animate().alpha(0.1f).setInterpolator(accInterpolator)
				.scaleX(0).scaleY(0).setDuration(duration >> 1).setStartDelay(10)
				.translationY((-xy[YAXIS] + yCenter)*2)
				.setListener(this);
		}
		/**��ִ�������ζ�����ȡ��TextView�ļ�����������Ϊ���ɼ����ȴ����Ƴ�*/
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
