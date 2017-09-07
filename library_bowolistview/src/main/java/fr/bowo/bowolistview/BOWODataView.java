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
    public BOWODataView setObject(String key, Object value) {
        datas.put(key, value);
        return this;
    }

    public Object getObject(String key) {
        return datas.get(key);
    }

    /*
    Facilitator
     */
    public BOWODataView setInt(String key, int value) {
        setObject(key, value);
        return this;
    }

    public int getInt(String key) {
        Object result = getObject(key);
        if (result instanceof Integer) {
            return (Integer) result;
        }
        return 0;
    }

    public BOWODataView setString(String key, String value) {
        setObject(key, value);
        return this;
    }

    public String getString(String key) {
        Object result = getObject(key);
        if (result instanceof String) {
            return (String) result;
        }
        return null;
    }

    public BOWODataView setBoolean(String key, boolean value) {
        setObject(key, value);
        return this;
    }

    public boolean getBoolean(String key) {
        Object result = getObject(key);
        if (result instanceof Boolean) {
            return (Boolean) result;
        }
        return false;
    }

    /*
    Common methods
     */
    public BOWODataView setId(int idx) {
        setInt(COMMON_KEY_IDX, idx);
        return this;
    }

    public int getId() {
        return getInt(COMMON_KEY_IDX);
    }

    public BOWODataView setTitle(String title) {
        setString(COMMON_KEY_TITLE, title);
        return this;
    }

    public String getTitle() {
        return getString(COMMON_KEY_TITLE);
    }

    public BOWODataView setSubtitle(String subtitle) {
        setString(COMMON_KEY_SUBTITLE, subtitle);
        return this;
    }

    public String getSubtitle() {
        return getString(COMMON_KEY_SUBTITLE);
    }

    public BOWODataView setText(String text) {
        setString(COMMON_KEY_TEXT, text);
        return this;
    }

    public String getText() {
        return getString(COMMON_KEY_TEXT);
    }

    public BOWODataView setImage(String image) {
        setString(COMMON_KEY_IMAGE, image);
        return this;
    }

    public String getImage() {
        return getString(COMMON_KEY_IMAGE);
    }

    public BOWODataView setIcon(String icon) {
        setString(COMMON_KEY_ICON, icon);
        return this;
    }

    public String getIcon() {
        return getString(COMMON_KEY_ICON);
    }
}
