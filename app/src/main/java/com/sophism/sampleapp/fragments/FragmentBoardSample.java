package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.sophism.sampleapp.R;
import com.sophism.sampleapp.data.BoardArticleData;
import com.sophism.sampleapp.dialogs.DialogBoardRegister;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by D.H.KIM on 2016. 1. 16.
 */
public class FragmentBoardSample extends Fragment implements View.OnClickListener{

    ArrayList<BoardArticleData> mBoardArticles = new ArrayList<>();
    BoardListAdapter mAdapter;
    ListView mBoardListView;
    Context mContext;
    public FragmentBoardSample(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_sample, container, false);
        mContext = getActivity();
        mBoardListView = (ListView)rootView.findViewById(R.id.listview_board);
        mAdapter = new BoardListAdapter(mContext);
        mBoardListView.setAdapter(mAdapter);
        Button btn_write = (Button)rootView.findViewById(R.id.btn_write);
        btn_write.setOnClickListener(this);

        updateBoardArticle();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_write:
                DialogBoardRegister dialog = new DialogBoardRegister(mContext);
                dialog.show();
                break;
        }
    }

    private void updateBoardArticle(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Board");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                mBoardArticles = new ArrayList<>();
                if (e != null) {
                    mBoardListView.setVisibility(View.GONE);
                    mAdapter.notifyDataSetChanged();
                } else {
                    mBoardListView.setVisibility(View.VISIBLE);
                    for (ParseObject object:objects){
                        BoardArticleData data = new BoardArticleData();
                        data.setArticle((String)object.get("article"));
                        data.setAuthor((String)object.get("author"));
                        data.setTitle((String)object.get("title"));
                        data.setDate((String)object.get("date"));
                        mBoardArticles.add(data);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });


    }
    private class BoardListAdapter extends BaseAdapter{
        private LayoutInflater mInflater;

        public BoardListAdapter(Context context) {
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mBoardArticles.size();
        }

        @Override
        public Object getItem(int position) {
            return mBoardArticles.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = mInflater.inflate(R.layout.list_item_board_article, parent, false);
                holder.board_article_title = (TextView) convertView.findViewById(R.id.board_article_title);
                holder.board_article_author = (TextView) convertView.findViewById(R.id.board_article_author);
                holder.board_article_date = (TextView) convertView.findViewById(R.id.board_article_date);
                holder.board_article_article = (TextView) convertView.findViewById(R.id.board_article_article);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.board_article_title.setText(mBoardArticles.get(position).getTitle());
            holder.board_article_author.setText(mBoardArticles.get(position).getAuthor());
            holder.board_article_date.setText(mBoardArticles.get(position).getDate());
            holder.board_article_article.setText(mBoardArticles.get(position).getArticle());
            return convertView;
        }

        class ViewHolder {
            TextView board_article_title;
            TextView board_article_author;
            TextView board_article_article;
            TextView board_article_date;
        }
    }
}
