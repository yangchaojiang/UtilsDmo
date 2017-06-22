package com.yangc.richeditorlibrary.view.editor;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hitomi on 2016/6/3.
 */
public class SEditorData implements Parcelable {

    private String briefText;
    private int  briefTextType;
    private String  ImagerPath;
    public String getBriefText() {
        return briefText;
    }

    public void setBriefText(String briefText) {
        this.briefText = briefText;
    }

    public int getBriefTextType() {
        return briefTextType;
    }

    public void setBriefTextType(int briefTextType) {
        this.briefTextType = briefTextType;
    }

    public SEditorData() {
    }

    public String getImagerPath() {
        return ImagerPath;
    }

    public void setImagerPath(String imagerPath) {
        ImagerPath = imagerPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.briefText);
        dest.writeInt(this.briefTextType);
        dest.writeString(this.ImagerPath);
    }

    protected SEditorData(Parcel in) {
        this.briefText = in.readString();
        this.briefTextType = in.readInt();
        this.ImagerPath = in.readString();
    }

    public static final Creator<SEditorData> CREATOR = new Creator<SEditorData>() {
        public SEditorData createFromParcel(Parcel source) {
            return new SEditorData(source);
        }

        public SEditorData[] newArray(int size) {
            return new SEditorData[size];
        }
    };
}
