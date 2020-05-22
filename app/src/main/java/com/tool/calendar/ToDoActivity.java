package com.tool.calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.tool.calendar.SQLite.FeedReaderContract;
import com.tool.calendar.SQLite.FeedReaderDbHelper;

import java.util.Objects;

public class ToDoActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "ToDoActivity";
    private static int flag;
    private Context mContext;
    private static TextView tvStartDate,tvEndDate;
    private MaterialButton nextButton;
    private TextInputEditText editTitle;

    private FeedReaderDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        mContext = ToDoActivity.this;

        dbHelper = new FeedReaderDbHelper(mContext);

        LinearLayout llBack = findViewById(R.id.ll_back);
        llBack.setOnClickListener(this);
        RelativeLayout rlStartDate = findViewById(R.id.rl_start_date);
        rlStartDate.setOnClickListener(this);
        RelativeLayout rlEndDate = findViewById(R.id.rl_end_date);
        rlEndDate.setOnClickListener(this);

        nextButton = findViewById(R.id.next_button);
        nextButton.setOnClickListener(this);

        editTitle = findViewById(R.id.edit_title);

        tvStartDate = findViewById(R.id.tv_start_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        tvStartDate.setText("");
        tvEndDate.setText("");

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

            case R.id.ll_back:
                finish();
                break;

            case R.id.rl_start_date:
                DialogFragment newFragment1 = new DatePickerFragment();
                newFragment1.show(getSupportFragmentManager(), "datePicker1");
                flag = 1;
                /*DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");*/
                break;

            case R.id.rl_end_date:
                DialogFragment newFragment2 = new DatePickerFragment();
                newFragment2.show(getSupportFragmentManager(), "datePicker2");
                flag = 2;
                break;

            case R.id.next_button:
                if (TextUtils.isEmpty(Objects.requireNonNull(editTitle.getText()).toString().trim())){
                    Toast.makeText(mContext, "请输入标题", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(tvStartDate.getText())){
                    Toast.makeText(mContext, "请设置起始日期", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(tvEndDate.getText())){
                    Toast.makeText(mContext, "请设置目标日期", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(mContext, "执行保存", Toast.LENGTH_SHORT).show();
                    //存数据
                    saveUserData(Objects.requireNonNull(editTitle.getText()).toString(),tvStartDate.getText().toString(),tvEndDate.getText().toString());
                    finish();
                }
                break;

        }
    }


    public static void setValue(int year,int month,int day) {

        if (flag == 1){
            tvStartDate.setText(String.format("%s年%s月%s日", year, month, day));
        }else if (flag == 2){
            tvEndDate.setText(String.format("%s年%s月%s日", year, month, day));
        }

    }




    private void saveUserData(final String title, final String startDate, final String endDate) {

       //存SQLite数据库
        new Thread(new Runnable() {
           @Override
           public void run() {
               //以写入模式获取数据存储库
               SQLiteDatabase db = dbHelper.getWritableDatabase();
               //创建一个新的值映射，其中列名是键
               ContentValues values = new ContentValues();
               values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, title);
               values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_START_DATE, startDate);
               values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_END_DATE, endDate);
               //插入新行，并返回新行的主键值
               long newRowId = db.insert(FeedReaderContract.FeedEntry.TABLE_NAME, null, values);
               Log.d(TAG, "newRowId: "+newRowId);
           }
       }).start();

    }



    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }




}
