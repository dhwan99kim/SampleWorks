package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.sophism.sampleapp.R;
import com.sophism.sampleapp.data.BoardArticleData;

import java.util.ArrayList;

/**
 * Created by D.H.KIM on 2016. 1. 16.
 */
public class FragmentBoardSample extends Fragment {

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

        updateBoardArticle();
        return rootView;
    }

    private void updateBoardArticle(){
        if (mBoardArticles.size() == 0){
            mBoardListView.setVisibility(View.GONE);
        }
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
