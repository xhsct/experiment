package com.example.exp_7;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

public class adapter extends BaseAdapter {

    private List<String> list;
    private LayoutInflater inflater;
    public Context context;

    public adapter(Context context, List<String> list){
        this.list = list;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = new ViewHolder();
        if(view == null){
            view = inflater.inflate(R.layout.img_item, null);
            viewHolder = new ViewHolder();
            viewHolder.img = view.findViewById(R.id.img);
            view.setTag(viewHolder);
        } else{
            viewHolder = (ViewHolder)view.getTag();
        }

        File file = new File(list.get(i));
        Uri imgUri = FileProvider.getUriForFile(context, "com.exp_7.fileProvider", file);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(imgUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            viewHolder.img.setImageBitmap(bitmap);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return view;
    }

    class ViewHolder{
        ImageView img;
    }

}
