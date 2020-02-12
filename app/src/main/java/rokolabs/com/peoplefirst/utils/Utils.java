package rokolabs.com.peoplefirst.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import rokolabs.com.peoplefirst.model.User;

/**
 * Created by iziah on 4/19/18.
 */

public class Utils {

    public static void saveMe(User user, Context ctx){
        SharedPreferences preferences = ctx.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(user);
        editor.putString("me", json);
        editor.commit();
        Log.d("UTILS","Saving "+"me"+ ": "+json);
    }

    public static void savePermanentValue(String key, String value, Context ctx){
        SharedPreferences preferences = ctx.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
        Log.d("UTILS","Saving "+key+ ": "+value);
    }

    public static String getPermanentValue(String key, Context ctx){
        String val = null;
        SharedPreferences preferences = ctx.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        val = preferences.getString(key,"");
        Log.d("UTILS","Returning "+key+ ": "+val);
        return val;
    }

    public static User getMe(Context ctx){
        String val = null;
        SharedPreferences preferences = ctx.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        val = preferences.getString("me","");
        User me = null;
        if (!"".equals(val)) {
            Gson gson = new Gson();
            me = gson.fromJson(val, User.class);
            Log.d("UTILS", "Returning " + "me" + ": " + val);
        }
        return me;
    }

    public static String dateFormat(String datetime) {
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        try {
            Date dt = in.parse(datetime);
            //SimpleDateFormat out = new SimpleDateFormat(DateFormat.getBestDateTimePattern(Locale.US, "MM/dd/yyyy hh:mm a"));
            SimpleDateFormat out = new SimpleDateFormat("MMM dd, yyyy • hh:mm a", Locale.US);
            return out.format(dt);
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * Formats reports's date
     *
     * @param datetime the datetime in "yyyy-MM-dd HH:mm:ss" format
     * @return `String` the date in "MM/dd/yyyy" format or empty `String` if parsing failed
     */
    public static String newDateFormat(String datetime) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return new SimpleDateFormat("MM/dd/yyyy").format(cal.getTime());
    }

    /**
     * Formats reports's time
     *
     * @param datetime the datetime in "yyyy-MM-dd HH:mm:ss" format
     * @return `String` the time in "h:mm a" format or empty `String` if parsing failed
     */
    public static String newTimeFormat(String datetime) {
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(datetime));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return new SimpleDateFormat("h:mm a").format(cal.getTime());
    }



    /**
     * Formats user's date of birth
     *
     * @param date the date in "yyyy-MM-dd" format
     * @return `String` the date in "MM/dd/yyyy" format or empty `String` if parsing failed
     */
    public static String formatUsersDateOfBirth(String date) {
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date dt = in.parse(date);
            SimpleDateFormat out = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            return out.format(dt);
        } catch (Exception e) {
            return "";
        }
    }

    public static long dayBetween(String datetime) {
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        in.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date dt = in.parse(datetime);
            Calendar from = Calendar.getInstance();
            from.set(Calendar.HOUR_OF_DAY, 0);
            from.set(Calendar.MINUTE, 0);
            from.set(Calendar.SECOND, 0);
            from.set(Calendar.MILLISECOND, 0);
            from.setTime(dt);
            Calendar to = Calendar.getInstance();
            to.set(Calendar.HOUR_OF_DAY, 0);
            to.set(Calendar.MINUTE, 0);
            to.set(Calendar.SECOND, 0);
            to.set(Calendar.MILLISECOND, 0);
            long msDiff = to.getTimeInMillis() - from.getTimeInMillis();
            long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
            return daysDiff;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String format24ToFormat12(String time) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.US);
            final Date dateObj = sdf.parse(time);
            return new SimpleDateFormat("hh:mm a", Locale.US).format(dateObj);
        } catch (Exception e) {
            return "";
        }
    }

    public static String format12ToFormat24(String time) {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.US);
            final Date dateObj = sdf.parse(time);
            return new SimpleDateFormat("HH:mm:ss", Locale.US).format(dateObj);
        } catch (Exception e) {
            return "";
        }
    }

    public static String gayDateToNormal(String gay) {
        SimpleDateFormat in = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        try {
            Date dt = in.parse(gay);
            SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            return out.format(dt);
        } catch (Exception e) {
            return "";
        }
    }

    public static String normalDateToGay(String normal) {
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        try {
            Date dt = in.parse(normal);
            SimpleDateFormat out = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
            return out.format(dt);
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isDateInFuture(String dateTime) {
        SimpleDateFormat in = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.US);
        try {
            Date dt = in.parse(dateTime);
            Date now = new Date();
            return dt.after(now);
        }catch (Exception e) {
            return false;
        }
    }

    public static String getFilePath(Context context, Uri uri) {
        String selection = null;
        String[] selectionArgs = null;
        if (DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            } else if (isDownloadsDocument(uri)) {
				final String id = DocumentsContract.getDocumentId(uri);
				uri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
			} else if (isMediaDocument(uri)) {
				final String docId = DocumentsContract.getDocumentId(uri);
				final String[] split = docId.split(":");
				final String type = split[0];
				if ("image".equals(type)) {
					uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				} else if ("video".equals(type)) {
					uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
				} else if ("audio".equals(type)) {
					uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
				}
				selection = "_id=?";
				selectionArgs = new String[]{split[1]};
			}
		}
		if ("content".equalsIgnoreCase(uri.getScheme())) {
			String[] projection = {MediaStore.Images.Media.DATA};
			try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);) {
				int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				if (cursor.moveToFirst()) {
					return cursor.getString(column_index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("file".equalsIgnoreCase(uri.getScheme())) {
			return uri.getPath();
		}
		return null;
	}

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
	private static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
	private static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

}
