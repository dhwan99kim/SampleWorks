package com.sophism.sampleapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.sophism.sampleapp.AppUtil;
import com.sophism.sampleapp.R;

/**
 * Created by D.H.KIM on 2016. 1. 15.
 */
public class DialogSignInSample extends Dialog{

    private Context mContext;
    private EditText mEditTextID, mEditTextPW, mEditTextPWConfirm;
    public DialogSignInSample(Context context){
        super(context,android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.height = WindowManager.LayoutParams.MATCH_PARENT;
        lpWindow.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_signin_sample);

        mEditTextID = (EditText)findViewById(R.id.edittext_id);
        mEditTextPW = (EditText)findViewById(R.id.edittext_pw);
        mEditTextPWConfirm = (EditText)findViewById(R.id.edittext_pw_confirm);

        TextView sign_in_btn = (TextView) findViewById(R.id.sign_in_btn);
        sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEditTextPW.getText().toString().equals(mEditTextPWConfirm.getText().toString())) {

                    ParseQuery<ParseObject> query = ParseQuery.getQuery("User");
                    query.whereEqualTo("username", mEditTextID.getText().toString());
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e != null) {
                                signIn();
                            } else {
                                Toast.makeText(mContext,"존재하는 ID입다",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(mContext,"비밀번호가 일치하지 않습니다",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void signIn(){
        ParseObject object = new ParseObject("User");
        try {
            object.put("username", mEditTextID.getText().toString());
            object.put("password", AppUtil.AES_Encode(mEditTextPW.getText().toString(), AppUtil.AES_KEY));
            object.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(mContext, "회원 가입되었습니다", Toast.LENGTH_SHORT);
                        dismiss();
                    } else {
                        e.printStackTrace();
                        Toast.makeText(mContext, "Oops!", Toast.LENGTH_SHORT);
                    }

                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(mContext, "Oops!", Toast.LENGTH_SHORT);
        }
    }
}
