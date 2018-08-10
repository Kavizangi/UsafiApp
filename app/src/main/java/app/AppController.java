package app;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import helper.SQLiteHandler;

public class AppController extends Application {

    // Login table name
    private static final String TABLE_USER = "user";

    public static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public String getUserDetails() {

        String arrData = null;

        String selectQuery = "SELECT * FROM " + TABLE_USER;

        SQLiteHandler helper = new SQLiteHandler(this);

        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //if TABLE has rows
        if (cursor.moveToFirst()) {

            arrData = cursor.getString(3);
        }
        db.close();
        return arrData;
    }

}
