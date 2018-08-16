package com.flipkart.chatheads.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.ImageView;

import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringListener;
import com.facebook.rebound.SpringSystem;
import com.flipkart.chatheads.ChatHeadUtils;

import java.io.Serializable;

/**
 * Created by kirankumar on 10/02/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ChatHead<T extends Serializable> extends ImageView implements SpringListener {

    final int CLOSE_ATTRACTION_THRESHOLD = ChatHeadUtils.dpToPx(getContext(), 110);
    private final int touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    private final float DELTA = ChatHeadUtils.dpToPx(getContext(), 10);
    private ChatHeadManager manager;
    private SpringSystem springSystem;
    private boolean isSticky = false;
    private State state;
    private T key;
    private float downX = -1;
    private float downY = -1;
    private VelocityTracker velocityTracker;
    private boolean isDragging;
    private float downTranslationX;
    private float downTranslationY;
    private int unreadCount = 0;
    private SpringListener xPositionListener;
    private SpringListener yPositionListener;
    private Spring scaleSpring;
    private Spring xPositionSpring;
    private Spring yPositionSpring;
    private Bundle extras;
    private ImageView imageView;
    private boolean isHero;

    public ChatHead(Context context) {
        super(context);
        throw new IllegalArgumentException("This constructor cannot be used");
    }

    public ChatHead(Context context, AttributeSet attrs) {
        super(context, attrs);
        throw new IllegalArgumentException("This constructor cannot be used");
    }

    public ChatHead(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        throw new IllegalArgumentException("This constructor cannot be used");
    }

    public ChatHead(ChatHeadManager manager, SpringSystem springsHolder, Context context, boolean isSticky) {
        super(context);
        this.manager = manager;
        this.springSystem = springsHolder;
        this.isSticky = isSticky;
        init();
    }

    public boolean isHero() {
        return isHero;
    }

    public void setHero(boolean hero) {
        isHero = hero;
    }

    public Bundle getExtras() {
        return extras;
    }

    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

    public Spring getHorizontalSpring() {
        return xPositionSpring;
    }

    public Spring getVerticalSpring() {
        return yPositionSpring;
    }

    public boolean isSticky() {
        return isSticky;
    }

    private void init() {
        xPositionListener = new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                manager.getChatHeadContainer().setViewX(ChatHead.this, (int)spring.getCurrentValue());
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                super.onSpringAtRest(spring);
            }
        };
        xPositionSpring = springSystem.createSpring();
        xPositionSpring.addListener(xPositionListener);
        xPositionSpring.addListener(this);

        yPositionListener = new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                manager.getChatHeadContainer().setViewY(ChatHead.this, (int)spring.getCurrentValue());
            }

            @Override
            public void onSpringAtRest(Spring spring) {
                super.onSpringAtRest(spring);
            }
        };
        yPositionSpring = springSystem.createSpring();
        yPositionSpring.addListener(yPositionListener);
        yPositionSpring.addListener(this);

        scaleSpring = springSystem.createSpring();
        scaleSpring.addListener(new SimpleSpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                super.onSpringUpdate(spring);
                double currentValue = spring.getCurrentValue();
                setScaleX((float) currentValue);
                setScaleY((float) currentValue);
            }
        });
        scaleSpring.setCurrentValue(1).setAtRest();
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        if (unreadCount != this.unreadCount) {
            manager.reloadDrawable(key);
        }
        this.unreadCount = unreadCount;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public T getKey() {
        return key;
    }

    public void setKey(T key) {
        this.key = key;
    }

    @Override
    public void onSpringUpdate(Spring spring) {
        if (xPositionSpring != null && yPositionSpring != null) {
            Spring activeHorizontalSpring = xPositionSpring;
            Spring activeVerticalSpring = yPositionSpring;
            if (spring != activeHorizontalSpring && spring != activeVerticalSpring)
                return;
            int totalVelocity = (int) Math.hypot(activeHorizontalSpring.getVelocity(), activeVerticalSpring.getVelocity());
            if (manager.getActiveArrangement() != null)
                manager.getActiveArrangement().onSpringUpdate(this, isDragging, manager.getMaxWidth(), manager.getMaxHeight(), spring, activeHorizontalSpring, activeVerticalSpring, totalVelocity);
        }
    }

    @Override
    public void onSpringAtRest(Spring spring) {
        if (manager.getListener() != null)
            manager.getListener().onChatHeadAnimateEnd(this);
    }

    @Override
    public void onSpringActivate(Spring spring) {
        if (manager.getListener() != null)
            manager.getListener().onChatHeadAnimateStart(this);
    }

    @Override
    public void onSpringEndStateChange(Spring spring) {

    }

    public SpringListener getHorizontalPositionListener() {
        return xPositionListener;
    }

    public SpringListener getVerticalPositionListener() {
        return yPositionListener;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        super.onTouchEvent(event);

        if(xPositionSpring==null || yPositionSpring==null) return false;
        //Chathead view will set the correct active springs on touch
        Spring activeHorizontalSpring = xPositionSpring;
        Spring activeVerticalSpring = yPositionSpring;

        int action = event.getAction();
        float rawX = event.getRawX();
        float rawY = event.getRawY();
        float offsetX = rawX - downX;
        float offsetY = rawY - downY;
        boolean showCloseButton = manager.getActiveArrangement().shouldShowCloseButton(this);
        event.offsetLocation(manager.getChatHeadContainer().getViewX(this), manager.getChatHeadContainer().getViewY(this));
        if (action == MotionEvent.ACTION_DOWN) {
            if (velocityTracker == null) {
                velocityTracker = VelocityTracker.obtain();
            } else {
                velocityTracker.clear();

            }
            activeHorizontalSpring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
            activeVerticalSpring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
            setState(ChatHead.State.FREE);
            downX = rawX;
            downY = rawY;
            downTranslationX = (float) activeHorizontalSpring.getCurrentValue();
            downTranslationY = (float) activeVerticalSpring.getCurrentValue();
            scaleSpring.setEndValue(.9f);
            activeHorizontalSpring.setAtRest();
            activeVerticalSpring.setAtRest();
            velocityTracker.addMovement(event);


        } else if (action == MotionEvent.ACTION_MOVE) {
            if (Math.hypot(offsetX, offsetY) > touchSlop) {
                isDragging = true;
                if (showCloseButton) {
                    manager.getCloseButton().appear();
                }
            }
            velocityTracker.addMovement(event);

            if (isDragging) {
                manager.getCloseButton().pointTo(rawX, rawY);
                if (manager.getActiveArrangement().canDrag(this)) {
                    double distanceCloseButtonFromHead = manager.getDistanceCloseButtonFromHead(rawX, rawY);
                    if (distanceCloseButtonFromHead < CLOSE_ATTRACTION_THRESHOLD && showCloseButton) {
                        setState(ChatHead.State.CAPTURED);
                        activeHorizontalSpring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                        activeVerticalSpring.setSpringConfig(SpringConfigsHolder.NOT_DRAGGING);
                        int[] coords = manager.getChatHeadCoordsForCloseButton(this);
                        activeHorizontalSpring.setEndValue(coords[0]);
                        activeVerticalSpring.setEndValue(coords[1]);
                        manager.getCloseButton().onCapture();

                    } else {
                        setState(ChatHead.State.FREE);
                        activeHorizontalSpring.setSpringConfig(SpringConfigsHolder.DRAGGING);
                        activeVerticalSpring.setSpringConfig(SpringConfigsHolder.DRAGGING);
                        activeHorizontalSpring.setCurrentValue(downTranslationX + offsetX);
                        activeVerticalSpring.setCurrentValue(downTranslationY + offsetY);
                        manager.getCloseButton().onRelease();
                    }

                    velocityTracker.computeCurrentVelocity(1000);
                }

            }

        } else {
            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
                boolean wasDragging = isDragging;
                activeHorizontalSpring.setSpringConfig(SpringConfigsHolder.DRAGGING);
                activeHorizontalSpring.setSpringConfig(SpringConfigsHolder.DRAGGING);
                isDragging = false;
                scaleSpring.setEndValue(1);
                int xVelocity = (int) velocityTracker.getXVelocity();
                int yVelocity = (int) velocityTracker.getYVelocity();
                velocityTracker.recycle();
                velocityTracker = null;
                if(xPositionSpring!=null && yPositionSpring!=null) {
                    ChatHeadArrangement chatHeadArrangement = manager.getActiveArrangement();
                    if(chatHeadArrangement instanceof MaximizedArrangement){
                        if(manager.getChatHeads().size()<2){
                            manager.setArrangement(MinimizedArrangement.class, null);
                            }
                        }
                    boolean touchUpHandled = manager.getActiveArrangement().handleTouchUp(this, xVelocity, yVelocity, activeHorizontalSpring, activeVerticalSpring, wasDragging);
                }
            }
        }

        return true;
    }

    public void onRemove() {
        xPositionSpring.setAtRest();
        xPositionSpring.removeAllListeners();
        xPositionSpring.destroy();
        xPositionSpring = null;
        yPositionSpring.setAtRest();
        yPositionSpring.removeAllListeners();
        yPositionSpring.destroy();
        yPositionSpring = null;
        scaleSpring.setAtRest();
        scaleSpring.removeAllListeners();
        scaleSpring.destroy();
        scaleSpring = null;
    }

    public void setImageDrawable(Drawable chatHeadDrawable) {
        super.setImageDrawable(chatHeadDrawable);
    }


    public enum State {
        FREE, CAPTURED
    }


}

