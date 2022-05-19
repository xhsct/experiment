package com.example.work_4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    MyAdapter mMyAdapter ;
    List<String> mItemList = new ArrayList<>();
    private Spinner spinner;
    private RecyclerView recyclerView;
    List<item> itemList = new ArrayList<>();
    private Button add_button;
    String color;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.name);
        initSpinner();

        initRecyclerview();

        initBtn();

    }

    private void initBtn() {

        add_button = findViewById(R.id.add);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMyAdapter.addData(itemList.size());
            }
        });
    }

    private void initRecyclerview() {
        recyclerView = findViewById(R.id.items);
        for (int i = 0; i < 20; i++) {
            mItemList.add("标题"+i);
        }

        String[] colors = new String[]{"#FF0000","#FF5722","#2196F3","#FFEB3B","#4CAF50"};
        for (String color : colors) {
            item item = new item("名称：" + color, color);
            itemList.add(item);
        }


        mMyAdapter = new MyAdapter();
        recyclerView.setAdapter(mMyAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);


    }

    private void initSpinner() {

        spinner = findViewById(R.id.spinner_color);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String[] spinner_content = getResources().getStringArray(R.array.colors);
                color = spinner_content[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = View.inflate(MainActivity.this, R.layout.item_list, null);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {
            item item = itemList.get(position);
            holder.mTitleContent.setBackgroundColor(Color.parseColor(item.getColor()));
            holder.mTitleContent.setText(item.getText());
            holder.backColor.setBackgroundColor(Color.parseColor(item.getColor()));

            holder.mTitleContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(holder.delete_btn.getVisibility() == View.INVISIBLE){
                        holder.delete_btn.setVisibility(View.VISIBLE);
                    }else {
                        holder.delete_btn.setVisibility(View.INVISIBLE);
                    }
                }
            });

            holder.delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("是否确认删除？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    deleteItem(position);
                                    holder.delete_btn.setVisibility(View.INVISIBLE);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    holder.delete_btn.setVisibility(View.INVISIBLE);
                                }
                            }).show();

                }
            });

        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }


        public void addData(int position){
            if(color.equals("红")){
                itemList.add(position,new item(editText.getText().toString(), "#FF0000"));
            }else if(color.equals("橙")){
                itemList.add(position,new item(editText.getText().toString(), "#FF5722"));
            }else if(color.equals("蓝")){
                itemList.add(position,new item(editText.getText().toString(), "#2196F3"));
            }else if(color.equals("黄")){
                itemList.add(position,new item(editText.getText().toString(), "#FFEB3B"));
            }else if(color.equals("绿")){
                itemList.add(position,new item(editText.getText().toString(), "#4CAF50"));
            }
            //添加动画
            notifyItemInserted(position);
        }

        public void deleteItem(int position){
            itemList.remove(position);
            notifyItemRemoved(position);
            //必须调用这行代码
            notifyItemRangeChanged(position, itemList.size());
        }


    }



    class MyViewHoder extends RecyclerView.ViewHolder {
        TextView mTitleContent;
        LinearLayout backColor;
        Button delete_btn;
        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            mTitleContent = itemView.findViewById(R.id.text);
            backColor = itemView.findViewById(R.id.backColor);
            delete_btn = itemView.findViewById(R.id.btn_delete);
        }
    }


}