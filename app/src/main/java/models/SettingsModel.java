package models;

import android.graphics.Bitmap;

public class SettingsModel {

    Bitmap flag;
    String languageName;
    boolean isSelected;

    public SettingsModel(String languageName) {
        this.languageName = languageName;
    }

    public Bitmap getFlag() {
        return flag;
    }

    public void setFlag(Bitmap flag) {
        this.flag = flag;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
