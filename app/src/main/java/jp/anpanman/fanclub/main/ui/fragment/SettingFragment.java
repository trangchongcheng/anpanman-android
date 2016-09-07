package jp.anpanman.fanclub.main.ui.fragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.main.R;

import jp.anpanman.fanclub.main.AnpanmanApp;
import jp.anpanman.fanclub.main.util.Constant;

/**
 * Created by linhphan on 7/19/16.
 */
public class SettingFragment extends DialogFragment{

    private DismissCallback callback;

    public void setCallback(DismissCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Holo_Light);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact, container, false);


        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        initAnalytics();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        callback.onDismiss();
    }

    public interface DismissCallback{
        void onDismiss();
    }
    // init Analytics Setting Fragment
    public void initAnalytics(){
        AnpanmanApp application = (AnpanmanApp) getActivity().getApplication();
        application.initAnalyticCategory(Constant.GA_INFO);

    }
}
