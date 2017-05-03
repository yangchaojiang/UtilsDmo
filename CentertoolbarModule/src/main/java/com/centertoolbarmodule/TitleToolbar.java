package com.centertoolbarmodule;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import static android.support.v7.appcompat.R.styleable;

/**
 * Created by felix on 15/12/28.
 */
public class TitleToolbar extends BaseToolbar implements View.OnClickListener {

    private static final int DEFAULT_BACK_MARGIN_RIGHT = 8;
    private LinearLayoutCompat mTitleLayout;
    private TextView mTitleTextView;
    private CharSequence mTitleText;
    private boolean mTitleVisible;
    private TextView mSubtitleTextView;
    private CharSequence mSubTitleText;
    private boolean mSubTitleVisible;
    private TextView mCloseTextView;
    private CharSequence mCloseText;
    private boolean mCloseVisible;
    private TextView mBackTextView;
    private CharSequence mBackText;
    private boolean mBackVisible;
    private Activity activity;

    public TitleToolbar(Context context) {
        super(context);
        onBack(context);
    }

    public TitleToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        onBack(context);
    }

    public TitleToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onBack(context);
    }

    @Override
    protected void initialize(Context context, AttributeSet attrs, int defStyleAttr) {
        super.initialize(context, attrs, defStyleAttr);
        onBack(context);
    }

    private void onBack(Context context) {
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

    }

    @Override
    public void setNavigationOnClickListener(View.OnClickListener listener) {
        super.setNavigationOnClickListener(listener);
    }

    @Override
    public void setNavigationIcon(Drawable icon) {
        super.setNavigationIcon(icon);
        this.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != activity)
                    activity.finish();
            }
        });
    }

    @Override
    public void setNavigationIcon(int resId) {
        super.setNavigationIcon(resId);
        this.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != activity)
                    activity.finish();
            }
        });
    }

    @Override
    protected void initCustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                styleable.Toolbar, defStyleAttr, 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleToolbar);

        if (!isChild(mTitleLayout)) {
            mTitleLayout = new LinearLayoutCompat(context);
            mTitleLayout.setOrientation(LinearLayoutCompat.VERTICAL);
            mTitleLayout.setGravity(typedArray.getInt(
                    R.styleable.TitleToolbar_title_gravity, Gravity.CENTER_VERTICAL));

            addView(mTitleLayout, new Toolbar.LayoutParams(
                    Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
        }

        if (!isChild(mTitleTextView, mTitleLayout)) {
            mTitleTextView = new TextView(context);
            mTitleTextView.setSingleLine();
            mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
            mTitleTextView.setGravity(Gravity.CENTER);

            int titleTextAppearance = a.getResourceId(styleable.Toolbar_titleTextAppearance, 0);
            if (titleTextAppearance != 0) {
                mTitleTextView.setTextAppearance(context, titleTextAppearance);
            }

            if (a.hasValue(styleable.Toolbar_titleTextColor)) {
                int titleColor = a.getColor(styleable.Toolbar_titleTextColor, Color.WHITE);
                mTitleTextView.setTextColor(titleColor);
            }

            if (typedArray.hasValue(R.styleable.TitleToolbar_titleTextSize)) {
                mTitleTextView.setTextSize(
                        typedArray.getDimensionPixelSize(R.styleable.TitleToolbar_backTextSize, 0));
            }

            setTitle(a.getText(styleable.Toolbar_title));
            setTitleVisible(typedArray.getBoolean(R.styleable.TitleToolbar_titleVisible, true));

            mTitleLayout.addView(mTitleTextView,
                    new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT));
        }

        if (!isChild(mSubtitleTextView, mTitleLayout)) {
            mSubtitleTextView = new TextView(context);
            mSubtitleTextView.setSingleLine();
            mSubtitleTextView.setEllipsize(TextUtils.TruncateAt.END);
            mSubtitleTextView.setGravity(Gravity.CENTER);

            int subTextAppearance = a.getResourceId(styleable.Toolbar_subtitleTextAppearance, 0);
            if (subTextAppearance != 0) {
                mSubtitleTextView.setTextAppearance(context, subTextAppearance);
            }

            if (a.hasValue(styleable.Toolbar_subtitleTextColor)) {
                int subTitleColor = a.getColor(styleable.Toolbar_subtitleTextColor, Color.WHITE);
                mSubtitleTextView.setTextColor(subTitleColor);
            }

            if (typedArray.hasValue(R.styleable.TitleToolbar_subtitleTextSize)) {
                mSubtitleTextView.setTextSize(
                        typedArray.getDimensionPixelSize(
                                R.styleable.TitleToolbar_subtitleTextSize, 0));
            }

            setSubtitle(a.getText(styleable.Toolbar_subtitle));
            setSubtitleVisible(
                    typedArray.getBoolean(R.styleable.TitleToolbar_subtitleVisible, false));

            mTitleLayout.addView(mSubtitleTextView,
                    new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.WRAP_CONTENT));
        }

        if (!isChild(mBackTextView)) {
            mBackTextView = new TextView(context);
            mBackTextView.setId(R.id.title_toolbar_back);
            mBackTextView.setSingleLine();
            mBackTextView.setEllipsize(TextUtils.TruncateAt.END);
            mBackTextView.setGravity(Gravity.CENTER_VERTICAL);

            int backTextAppearance =
                    typedArray.getResourceId(R.styleable.TitleToolbar_backTextAppearance, 0);
            if (backTextAppearance != 0) {
                mBackTextView.setTextAppearance(context, backTextAppearance);
            }

            if (typedArray.hasValue(R.styleable.TitleToolbar_backTextColor)) {
                int backTextColor =
                        typedArray.getColor(R.styleable.TitleToolbar_backTextColor, Color.WHITE);
                mBackTextView.setTextColor(backTextColor);
            }

            if (typedArray.hasValue(R.styleable.TitleToolbar_backTextSize)) {
                mBackTextView.setTextSize(
                        typedArray.getDimensionPixelSize(R.styleable.TitleToolbar_backTextSize, 0));
            }

            Drawable drawable = typedArray.getDrawable(R.styleable.TitleToolbar_backIcon);
            if (drawable != null) {
                mBackTextView.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }

            setBackText(typedArray.getText(R.styleable.TitleToolbar_backText));
            setBackVisible(typedArray.getBoolean(R.styleable.TitleToolbar_backVisible, false));

            mBackTextView.setClickable(true);
            mBackTextView.setOnClickListener(this);

            Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                    Toolbar.LayoutParams.MATCH_PARENT, Gravity.LEFT | Gravity.CENTER_VERTICAL);

            layoutParams.rightMargin = typedArray.getDimensionPixelSize(
                    R.styleable.TitleToolbar_backMarginRight, dp2px(DEFAULT_BACK_MARGIN_RIGHT));

            addView(mBackTextView, layoutParams);
        }

        if (!isChild(mCloseTextView)) {
            mCloseTextView = new TextView(context);
            mCloseTextView.setId(R.id.title_toolbar_close);
            mCloseTextView.setSingleLine();
            mCloseTextView.setEllipsize(TextUtils.TruncateAt.END);
            mCloseTextView.setGravity(Gravity.CENTER_VERTICAL);

            int closeTextAppearance =
                    typedArray.getResourceId(R.styleable.TitleToolbar_closeTextAppearance, 0);

            if (closeTextAppearance != 0) {
                mCloseTextView.setTextAppearance(context, closeTextAppearance);
            }

            if (typedArray.hasValue(R.styleable.TitleToolbar_closeTextColor)) {
                int closeTextColor =
                        typedArray.getColor(R.styleable.TitleToolbar_closeTextColor, Color.WHITE);
                mCloseTextView.setTextColor(closeTextColor);
            }

            if (typedArray.hasValue(R.styleable.TitleToolbar_closeTextSize)) {
                mCloseTextView.setTextSize(
                        typedArray.getDimensionPixelSize(
                                R.styleable.TitleToolbar_closeTextSize, 0));
            }

            setCloseText(typedArray.getText(R.styleable.TitleToolbar_closeText));
            setCloseVisible(typedArray.getBoolean(R.styleable.TitleToolbar_closeVisible, false));

            mCloseTextView.setClickable(true);
            mCloseTextView.setOnClickListener(this);

            addView(mCloseTextView, new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT,
                    Toolbar.LayoutParams.MATCH_PARENT, Gravity.LEFT | Gravity.CENTER_VERTICAL));
        }

        typedArray.recycle();
        a.recycle();

    }

    @Override
    public CharSequence getTitle() {
        return mTitleText;
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleText = title;
        if (mTitleTextView != null) {
            mTitleTextView.setText(title);
        }
    }

    @Override
    public void setTitleTextAppearance(Context context, int resId) {
        if (mTitleTextView != null) {
            mTitleTextView.setTextAppearance(context, resId);
        }
    }

    @Override
    public void setTitleTextColor(int color) {
        if (mTitleTextView != null) {
            mTitleTextView.setTextColor(color);
        }
    }

    public boolean getTitleVisible() {
        return mTitleVisible;
    }

    public void setTitleVisible(boolean visible) {
        mTitleVisible = visible;
        mTitleTextView.setVisibility(mTitleVisible ? VISIBLE : GONE);
    }

    @Override
    public CharSequence getSubtitle() {
        return mSubTitleText;
    }

    @Override
    public void setSubtitle(CharSequence subtitle) {
        mSubTitleText = subtitle;
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setText(subtitle);
        }
    }

    @Override
    public void setSubtitleTextAppearance(Context context, int resId) {
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextAppearance(context, resId);
        }
    }

    @Override
    public void setSubtitleTextColor(int color) {
        if (mSubtitleTextView != null) {
            mSubtitleTextView.setTextColor(color);
        }
    }

    public boolean getSubtitleVisible() {
        return mSubTitleVisible;
    }

    public void setSubtitleVisible(boolean visible) {
        mSubTitleVisible = visible;
        mSubtitleTextView.setVisibility(visible ? VISIBLE : GONE);
    }

    public void setCloseText(int resId) {
        setCloseText(getContext().getText(resId));
    }

    public CharSequence getCloseText() {
        return mCloseText;
    }

    public void setCloseText(CharSequence closeText) {
        mCloseText = closeText;
        if (mCloseTextView != null) {
            mCloseTextView.setText(closeText);
        }
    }

    public void setCloseTextColor(int color) {
        mCloseTextView.setTextColor(color);
    }

    public boolean isCloseVisible() {
        return mCloseVisible;
    }

    public void setCloseVisible(boolean visible) {
        mCloseVisible = visible;
        mCloseTextView.setVisibility(mCloseVisible ? VISIBLE : GONE);
    }

    public void setBackText(int resId) {
        setBackText(getContext().getText(resId));
    }

    public CharSequence getBackText() {
        return mBackText;
    }

    public void setBackText(CharSequence backText) {
        mBackText = backText;
        if (mBackTextView != null) {
            mBackTextView.setText(backText);
        }
    }

    public void setBackTextColor(int color) {
        mBackTextView.setTextColor(color);
    }

    public boolean isBackVisible() {
        return mBackVisible;
    }

    public void setBackVisible(boolean visible) {
        mBackVisible = visible;
        mBackTextView.setVisibility(mBackVisible ? VISIBLE : GONE);
    }

    @Override
    public void onClick(View v) {
        if (mOnOptionItemClickListener != null) {
            mOnOptionItemClickListener.onOptionItemClick(v);
        }
    }
}
