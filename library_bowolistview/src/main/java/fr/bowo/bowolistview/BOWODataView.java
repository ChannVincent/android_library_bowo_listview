package fr.bowo.bowolistview;

import java.util.HashMap;

/**
 * Created by vincentchann on 07/08/16.
 */
public class BOWODataView {

    /*
    Attributes
     */
    public int resourceViewIdx;
    protected HashMap<String, Object> datas;
    protected String COMMON_KEY_IDX = "idx";
    protected String COMMON_KEY_TITLE = "title";
    protected String COMMON_KEY_SUBTITLE = "subtitle";
    protected String COMMON_KEY_TEXT = "text";
    protected String COMMON_KEY_IMAGE = "image";
    protected String COMMON_KEY_ICON = "icon";

    /*
    Constructor
     */
    public BOWODataView(int resourceViewIdx) {
        this.resourceViewIdx = resourceViewIdx;
        datas = new HashMap<>();
    }

    /*
    Main functions
     */
    public void set(String key, Object value) {
        datas.put(key, value);
    }

    public Object get(String key) {
        return datas.get(key);
    }

    /*
    Facilitator
     */
    public void setInt(String key, int value) {
        set(key, value);
    }

    public int getInt(String key) {
        Object result = get(key);
        if (result instanceof Integer) {
            return (Integer) result;
        }
        return 0;
    }

    public void setString(String key, String value) {
        set(key, value);
    }

    public String getString(String key) {
        Object result = get(key);
        if (result instanceof String) {
            return (String) result;
        }
        return null;
    }

    public void setBoolean(String key, boolean value) {
        set(key, value);
    }

    public boolean getBoolean(String key) {
        Object result = get(key);
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        return false;
    }

    /*
    Common methods
     */
    public void setId(int idx) {
        setInt(COMMON_KEY_IDX, idx);
    }

    public int getId() {
        return getInt(COMMON_KEY_IDX);
    }

    public void setTitle(String title) {
        setString(COMMON_KEY_TITLE, title);
    }

    public String getTitle() {
        return getString(COMMON_KEY_TITLE);
    }

    public void setSubtitle(String subtitle) {
        setString(COMMON_KEY_SUBTITLE, subtitle);
    }

    public String getSubtitle() {
        return getString(COMMON_KEY_SUBTITLE);
    }

    public void setText(String text) {
        setString(COMMON_KEY_TEXT, text);
    }

    public String getText() {
        return getString(COMMON_KEY_TEXT);
    }

    public void setImage(String image) {
        setString(COMMON_KEY_IMAGE, image);
    }

    public String getImage() {
        return getString(COMMON_KEY_IMAGE);
    }

    public void setIcon(String icon) {
        setString(COMMON_KEY_ICON, icon);
    }

    public String getIcon() {
        return getString(COMMON_KEY_ICON);
    }
}
