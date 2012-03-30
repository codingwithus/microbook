/**
 * 
 */
package com.junzhilu.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @author eureka
 * 
 */
public class Util {
	/**
	 * ������ַ�ת��λ����
	 */
	public static int charToInt(char c) {
		if (c >= 'a' && c <= 'f') {
			return c - 'a' + 10;
		} else {
			return c - '0';
		}
	}

	public static void ShowTips(Context context, String conment) {
		Toast toast = Toast.makeText(context, conment, Toast.LENGTH_LONG);
		toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}

	public static ProgressDialog ShowProgressDialog(Context context, String text) {
		ProgressDialog progressDialog = new ProgressDialog(context);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// תȦ���
		// ��������ΪProgressDialog.STYLE_HORIZONTAL,ʹ��setMax,setProgress,incrementProgressBy�������ý��
		if (text != null) {
			progressDialog.setMessage(text);
		} else {
			progressDialog.setMessage("正在加载数据......");
		}
		return progressDialog;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}
