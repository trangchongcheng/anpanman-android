package jp.anpanman.fanclub.framework.phvtFragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Toast;

import jp.anpanman.fanclub.framework.phvtActivity.BaseActivity;
import jp.anpanman.fanclub.framework.phvtCommon.AppState;
import jp.anpanman.fanclub.framework.phvtCommon.FragmentTransitionInfo;
import jp.anpanman.fanclub.framework.phvtRest.HttpRequestClient;
import jp.anpanman.fanclub.framework.phvtUtils.AppLog;

import java.util.ArrayList;

/**
 * Base Fragment for all Fragment in the application
 *
 * @author thaonp & phatvt
 */
public abstract class BaseFragment extends Fragment {
    //----------------------------------------------------------------------------------------------------
    /**
     * Root layout view
     */
    protected View mRootLayout;
    //----------------------------------------------------------------------------------------------------

    /**
     * Get class tag
     *
     * @return Full quality class name
     */
    public static String classTag() {
        return BaseFragment.class.getName();
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Get root layout view id of the fragment.
     *
     * @return Root layout view id
     */
    public abstract @LayoutRes int getRootLayoutId();

    protected abstract void getMandatoryViews(View root, Bundle savedInstanceState);

    protected abstract void registerEventHandlers();
    //----------------------------------------------------------------------------------------------------

    /**
     * Get top navigation fragment container ID
     *
     * @return
     */
    public int getTopNavigationFragmentContainerId() {
        return -1;
    }

    //----------------------------------------------------------------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRootLayout =  inflater.inflate(getRootLayoutId(), container, false);
        getMandatoryViews(mRootLayout, savedInstanceState);
        registerEventHandlers();
        return mRootLayout;
    }

    //--------------------------------------------------------------------------------------------------------------------
    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        if (!AppState.instance().getIsEnableFragmentTransitionAnimation()) {
            Animation nullAnimation = new Animation() {
            };
            nullAnimation.setDuration(0);
            return nullAnimation;
        }
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Add child fragment to parent fragment
     *
     * @param child       Child fragment
     * @param viewGroupId Fragment container ID
     * @param tag         Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
     * @param transition  Transition animations
     * @return <code>true</code> if added successfully, otherwise is <code>false</code>
     */
    public boolean addChildFragment(final Fragment child, int viewGroupId, String tag, FragmentTransitionInfo transition) {
        return FragmentHelper.addChildFragment(this, child, viewGroupId, tag, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Add child fragment to parent fragment by child fragment class tag name
     *
     * @param viewGroupId               Fragment container ID
     * @param childFragmentClassNameTag Full quality child fragment class name
     * @param addToBackStack            <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>childFragmentClassNameTag</code> tag name
     * @param data                      Passed data
     * @param transition                Transition animations
     * @return <code>true</code> if added successfully, otherwise is <code>false</code>
     */
    public boolean addChildFragment(int viewGroupId, String childFragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
        return FragmentHelper.addChildFragment(this, viewGroupId, childFragmentClassNameTag, addToBackStack, data, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Replace fragment from parent fragment of specific ViewGroup ID
     *
     * @param child       Child fragment
     * @param viewGroupId Fragment container ID
     * @param tag         Null or empty string means that be not pushed into back stack, otherwise is pushed with specific tag name
     * @param transition  Transition animations
     * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
     */
    public boolean replaceChildFragment(final Fragment child, int viewGroupId, String tag, FragmentTransitionInfo transition) {
        return FragmentHelper.replaceChildFragment(this, child, viewGroupId, tag, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Replace fragment from parent fragment of specific ViewGroup ID by child fragment class tag name
     *
     * @param viewGroupId               Fragment container ID
     * @param childFragmentClassNameTag Full quality child fragment class name
     * @param addToBackStack            <code>true</code> is not pushed into back stack, otherwise is pushed with specific <code>childFragmentClassNameTag</code> tag name
     * @param data                      Passed data
     * @param transition                Transition animations
     * @return <code>true</code> if replaced successfully, otherwise is <code>false</code>
     */
    public boolean replaceChildFragment(int viewGroupId, String childFragmentClassNameTag, boolean addToBackStack, Bundle data, FragmentTransitionInfo transition) {
        return FragmentHelper.replaceChildFragment(this, viewGroupId, childFragmentClassNameTag, addToBackStack, data, transition);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Find child fragment from parent fragment by tab name.
     *
     * @param tag Tag of child fragment when add or replace to parent fragment by transaction
     * @return Found <code>Fragment</code> or <code>null</code> if not found
     */
    public Fragment findChildFragmentByTag(final String tag) {
        return FragmentHelper.findChildFragmentByTag(this, tag);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Clear children fragment back stack
     */
    public void clearChildBackStack() {
        FragmentHelper.clearChildBackStack(this);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Clear children fragment back stack by Iterator
     */
    public void clearChildrenBackStackByIterator() {
        FragmentHelper.clearChildrenBackStackByIterator(this);
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Pop back stack fragment
     */
    public void popChildFragmentBackStack() {
        getChildFragmentManager().popBackStack();
    }
    //-------------------------------------------------------------------------------------------------------------------

    /**
     * Open URL by Default Browser from Fragment
     */
    public void openUrlByDefaultBrowser(String url) {
        if (url == null || url.trim().isEmpty()) {
            Toast.makeText(this.getActivity().getApplicationContext(), "Can not open null or empty URL", Toast.LENGTH_SHORT).show();
            return;
        }
        Uri uri = Uri.parse(url);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Get owner class which is holding this fragment
     *
     * @return SampleActivity instance or null if activity of fragment is not an instance of SampleActivity
     */
    @SuppressWarnings("unchecked")
    public <T extends BaseActivity>T getOwnerActivity() {
        Activity activity = getActivity();
        if (activity instanceof BaseActivity) {
            return (T) activity;
        } else {
            return null;
        }
    }
    //----------------------------------------------------------------------------------------------------


    // Asynctask Management
    private ArrayList<AsyncTask> serviceList;
    private final int MAX_REQUEST = 5;

    //----------------------------------------------------------------------------------------------------

    /**
     * Add running AsyncHttpTask
     *
     * @param aRunningTask
     */
    public void addAsyncHttpTask(AsyncTask aRunningTask) {
        if (aRunningTask == null) {
            return;
        }
        if (serviceList == null) {
            serviceList = new ArrayList<>();
        }
        if (serviceList.size() > MAX_REQUEST) {
            AppLog.log("[PHVT Network]", "Network Sync is full. It's need recycling...Remove top ! Current Size = " + serviceList.size());
            AsyncTask request = serviceList.get(0);
            if (request != null)
                request.cancel(true);

            serviceList.remove(0);
        }
        serviceList.add(aRunningTask);
        AppLog.log("[PHVT Network]", "Manager Async :: Size = " + serviceList.size() + " ");
    }

    /**
     * Remove running AsyncHttpTask out of the list
     *
     * @param aRunningTask
     */
    public void removeAsyncHttpTask(HttpRequestClient aRunningTask) {
        if (aRunningTask == null || serviceList == null) {
            return;
        }
        serviceList.remove(aRunningTask);
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Cancel (stop/interrupt) running AsyncHttpTask and remove it out of the list
     *
     * @param aRunningTask
     */
    public void cancelAysncHttpTask(AsyncTask aRunningTask, boolean mayInterrrup) {
        if (aRunningTask == null || serviceList == null) {
            return;
        }
        int indexOfRunningTask = serviceList.indexOf(aRunningTask);
        if (indexOfRunningTask != -1) {
            AsyncTask asyncHttpRequest = serviceList.get(indexOfRunningTask);
            asyncHttpRequest.cancel(mayInterrrup);
            serviceList.remove(indexOfRunningTask);
            asyncHttpRequest = null;
        }
    }
    //----------------------------------------------------------------------------------------------------

    /**
     * Cancel (stop/interrupt) all running AsyncHttpTask and clear the list
     */
    public void cancelAllAsyncHttpTask(boolean mayInterrrup) {

        if (serviceList != null) {
            for (AsyncTask asyncHttpRequest : serviceList) {
                AppLog.log("[PHVT Network]", "CLear - Cancel Asyntask " + asyncHttpRequest.getClass().hashCode());
                asyncHttpRequest.cancel(mayInterrrup);
                asyncHttpRequest = null;
            }
            serviceList.clear();
        }
    }


    //----------------------------------------------------------------------------------------------------

    /**
     * Client request :
     * Request API Method general
     *
     * @param client
     */

    public void requestAPI(HttpRequestClient client) {
        addAsyncHttpTask(client);
        client.execute();
    }
}
