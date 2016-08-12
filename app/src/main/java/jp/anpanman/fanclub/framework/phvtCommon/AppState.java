package jp.anpanman.fanclub.framework.phvtCommon;

/**
 * Application State
 * @author thaonp & Phatvt
 */
public class AppState {



	public static boolean isCreateDatabase = false;


	//----------------------------------------------------------------------------------------------------
	public static final int TAB_ONE = 1;
	//----------------------------------------------------------------------------------------------------
	public static final int TAB_TWO = 2;
	//----------------------------------------------------------------------------------------------------
	public static final int TAB_THREE = 3;
	//----------------------------------------------------------------------------------------------------
	public static final int TAB_FOUR = 4;
	//----------------------------------------------------------------------------------------------------
	public static boolean sCanGoBack = true;
	//----------------------------------------------------------------------------------------------------
	private static AppState sInstance;
	//----------------------------------------------------------------------------------------------------
	private int mCurrentBottomTabIndex = TAB_ONE;
	//----------------------------------------------------------------------------------------------------
	private boolean mIsEnableFragmentTransitionAnimation = true;

	//----------------------------------------------------------------------------------------------------
	/**
	 * Get Current Bottom navigation tab index
	 */
	public int getCurrentBottomTabIndex() {
		return mCurrentBottomTabIndex;
	}


	//----------------------------------------------------------------------------------------------------
	/**
	 * Set Current Bottom navigation tab index
	 */
	public void setCurrentBottomTabIndex(int currentBottomTabIndex) {
		mCurrentBottomTabIndex = currentBottomTabIndex;
	}

	//----------------------------------------------------------------------------------------------------
	/**
	 * GEt Is enable fragment transition animation
	 * @return
	 */
	public boolean getIsEnableFragmentTransitionAnimation() {
		return mIsEnableFragmentTransitionAnimation;
	}
	//----------------------------------------------------------------------------------------------------
	/**
	 * Enable Fragment Transition Animation
	 */
	public void enableFragmentTranstitionAnimation() {
		mIsEnableFragmentTransitionAnimation = true;
	}
	//----------------------------------------------------------------------------------------------------
	/**
	 * Disable Fragment Transition Animation
	 */
	public void disableFragmentTranstitionAnimation() {
		mIsEnableFragmentTransitionAnimation = false;
	}
	//----------------------------------------------------------------------------------------------------
	/**
	 * Private constructor prevent user creates instance.
	 */
	private AppState() {
	}
	//----------------------------------------------------------------------------------------------------
	/**
	 * Get singleton instance of AppState.
	 * @return Singleton instance of AppState
	 */
	public static synchronized AppState instance() {
		if(sInstance == null) {
			sInstance = new AppState();
		}
		return sInstance;
	}
	//----------------------------------------------------------------------------------------------------
}
