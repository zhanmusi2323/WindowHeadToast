package com.example.windowheadtoast;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * 用于自定义一个Toast，模仿类似于QQ和微信来消息时候在顶部弹出的消息提示框
 */
public class WindowHeadToast implements View.OnTouchListener {
    private Context mContext;
    private View headToastView;
    private LinearLayout linearLayout;
    private final static int ANIM_DURATION = 600;
    private final static int ANIM_DISMISSS_DURATION = 2000;
    private final static int ANIM_CLOSE = 20;
    private android.os.Handler mHander = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == ANIM_CLOSE) {
                animDismiss();
            }
        }
    };
    private WindowManager wm;
    private int downX;
    private int downY;

    public WindowHeadToast(Context context) {
        mContext = context;
    }

    public void showCustomToast() {
        initHeadToastView();
        setHeadToastViewAnim();
        // 延迟2s后关闭
        mHander.sendEmptyMessageDelayed(ANIM_CLOSE, 2000);
    }

    private void setHeadToastViewAnim() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", -700, 0);
        animator.setDuration(ANIM_DURATION);
        animator.start();
    }

    private void animDismiss() {
        if (linearLayout == null || linearLayout.getParent() == null) {
            return;
        }
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", 0, -700);
        animator.setDuration(ANIM_DURATION);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                dismiss();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                super.onAnimationRepeat(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }

            @Override
            public void onAnimationPause(Animator animation) {
                super.onAnimationPause(animation);
            }

            @Override
            public void onAnimationResume(Animator animation) {
                super.onAnimationResume(animation);
            }
        });
    }

    /**
     * 移除HeaderToast  (一定要在动画结束的时候移除,不然下次进来的时候由于wm里边已经有控件了，所以会导致卡死)
     */
    private void dismiss() {
        if (null != linearLayout && null != linearLayout.getParent()) {
            wm.removeView(linearLayout);
        }
    }

    public void initHeadToastView() {
        //准备Window要添加的View
        linearLayout = new LinearLayout(mContext);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);
        headToastView = View.inflate(mContext, R.layout.header_toast, null);
        // 为headToastView设置Touch事件
        headToastView.setOnTouchListener(this);
        linearLayout.addView(headToastView);
        // 定义WindowManager 并且将View添加到WindowManagar中去
        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        WindowManager.LayoutParams wm_params = new WindowManager.LayoutParams();
        wm_params.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_FULLSCREEN
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        wm_params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        wm_params.x = 0;
        wm_params.y = 100;
        wm_params.format = -3;  // 会影响Toast中的布局消失的时候父控件和子控件消失的时机不一致，比如设置为-1之后就会不同步
        wm_params.alpha = 1f;
        wm.addView(linearLayout, wm_params);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) event.getRawX();
                downY = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int currentX = (int) event.getRawX();
                int currentY = (int) event.getRawY();
                if (Math.abs(currentX - downX) > 50 || Math.abs(currentY - downY) > 40) {
                    animDismiss();
                }
                break;
            default:
                break;
        }
        return true;
    }
}
