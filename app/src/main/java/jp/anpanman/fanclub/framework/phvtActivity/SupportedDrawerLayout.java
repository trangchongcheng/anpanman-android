package jp.anpanman.fanclub.framework.phvtActivity;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;
import android.util.AttributeSet;
import android.view.KeyEvent;

/**
 * Support Drawer Layout
 * @author thaonp & phatvt
 */
public class SupportedDrawerLayout extends DrawerLayout {
	//-------------------------------------------------------------------------------------------------------
	/**
	 * Get class tag
	 * @return Full quality class name
	 */
	public static String classTag() {
		return SupportedDrawerLayout.class.getName();
	}
	//-------------------------------------------------------------------------------------------------------
	/**
	 * No push content.
	 */
	public static final int PUSH_SIDE_NONE = -1;
	//-------------------------------------------------------------------------------------------------------
	/**
	 * Push content in two sides (left + right)
	 */
	public static final int PUSH_SIDE_BOTH = 0;
	//-------------------------------------------------------------------------------------------------------
	/**
	 * Push content only left side
	 */
	public static final int PUSH_SIDE_LEFT = 1;
	//-------------------------------------------------------------------------------------------------------
	/**
	 * Push content only right side
	 */
	public static final int PUSH_SIDE_RIGHT = 2;
	//-------------------------------------------------------------------------------------------------------
	public SupportedDrawerLayout(Context context) {
		super(context);
	}
	//-------------------------------------------------------------------------------------------------------	
	public SupportedDrawerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	//-------------------------------------------------------------------------------------------------------
	public SupportedDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	//-------------------------------------------------------------------------------------------------------
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			closeDrawers();
		}
		return super.onKeyDown(keyCode, event);
	}
	//-------------------------------------------------------------------------------------------------------
}
