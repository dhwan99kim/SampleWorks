package com.sophism.sampleapp.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.sophism.sampleapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by D.H.KIM on 2016. 1. 18.
 */
public class DialogBoardRegister extends Dialog implements View.OnClickListener{

    Context mContext;
    EditText mEditTextTitle;
    EditText mEditTextAuthor;
    EditText mEditTextArticle;

    public DialogBoardRegister(Context context) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_board_register);

        mEditTextTitle = (EditText) findViewById(R.id.edittext_title);
        mEditTextAuthor = (EditText) findViewById(R.id.edittext_author);
        mEditTextArticle = (EditText) findViewById(R.id.edittext_article);

        Button btn_board_register_submit = (Button) findViewById(R.id.btn_board_register_submit);
        btn_board_register_submit.setOnClickListener(this);

        ImageView btn_board_register_back = (ImageView) findViewById(R.id.btn_board_register_back);
        btn_board_register_back.setOnClickListener(this);
        TextView btn_board_register_cancel = (TextView) findViewById(R.id.btn_board_register_cancel);
        btn_board_register_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_board_register_submit:
                registerArticle();
                break;
            case R.id.btn_board_register_cancel:
            case R.id.btn_board_register_back:
                dismiss();
                break;
        }
    }

    private void registerArticle(){
        ParseObject object = new ParseObject("Board");
        object.put("title", mEditTextTitle.getText().toString());
        object.put("author", mEditTextAuthor.getText().toString());
        object.put("article", mEditTextArticle.getText().toString());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        final String current = simpleDateFormat.format(date);
        object.put("date",current);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(mContext, "게시물이 등록되었습니다", Toast.LENGTH_SHORT);
                    dismiss();
                } else {
                    e.printStackTrace();
                    Toast.makeText(mContext, "Oops!", Toast.LENGTH_SHORT);
                }

            }
        });

    }
}
