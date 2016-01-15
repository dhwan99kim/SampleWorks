package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginDefine;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;
import com.sophism.sampleapp.AppDefine;
import com.sophism.sampleapp.R;
import com.sophism.sampleapp.dialogs.DialogSignInSample;

/**
 * Created by D.H.KIM on 2016. 1. 14.
 */
public class FragmentLoginSample extends Fragment implements View.OnClickListener{

    private static Context mContext;
    private static OAuthLogin mOAuthLoginInstance;
    private static TextView textview_login_state;
    private OAuthLoginButton mOAuthLoginButton;

    public FragmentLoginSample(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_login_sample, container, false);
        mContext = getActivity();
        initData();
        mOAuthLoginButton = (OAuthLoginButton) rootView.findViewById(R.id.buttonOAuthLoginImg);
        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);
        OAuthLoginDefine.DEVELOPER_VERSION = true;
        textview_login_state = (TextView) rootView.findViewById(R.id.textview_login_state);

        TextView sign_in_btn = (TextView) rootView.findViewById(R.id.sign_in_btn);
        sign_in_btn.setOnClickListener(this);

        return rootView;
    }

    public void initData(){
        mOAuthLoginInstance = OAuthLogin.getInstance();
        mOAuthLoginInstance.init(mContext, AppDefine.OAUTH_CLIENT_ID, AppDefine.OAUTH_CLIENT_SECRET, AppDefine.OAUTH_CLIENT_NAME);
    }


    /**
     * startOAuthLoginActivity() 호출시 인자로 넘기거나, OAuthLoginButton 에 등록해주면 인증이 종료되는 걸 알 수 있다.
     */
    static private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
        @Override
        public void run(boolean success) {
            if (success) {
                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
                textview_login_state.setText("로그인 완료");
                textview_login_state.setTextColor(mContext.getResources().getColor(R.color.black));
            } else {
                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
                Toast.makeText(mContext, "errorCode:" + errorCode + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
            }
        };
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_btn:
                DialogSignInSample dialog = new DialogSignInSample(mContext);
                dialog.show();
                break;
        }
    }
}

