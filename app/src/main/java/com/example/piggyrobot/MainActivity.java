package com.example.piggyrobot;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<Msg> msgList = new ArrayList<Msg>();

    private EditText inputText;

    private Button send;

    private RecyclerView msgRecyclerView;

    private MsgAdapter adapter;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case 1:{
                    Bundle data = msg.getData();
                    String result = data.getString("result");
                    Msg robotMsg = new Msg(result,Msg.TYPE_RECEIVED);
                    msgList.add(robotMsg);
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                }break;
                case 2:{}break;
                default:break;
            }
        }
    };

    private void getInter(String content){
        MyConnection.getResponse(RobotManager.getUrl(content), new GetConnection() {
            @Override
            public void onFinish(String response) {
                ContentBean contentBean = new ContentBean();
                Log.e("getResult",response);
                Message msg = new Message();
                Bundle data = new Bundle();
                Gson gson = new Gson();
                contentBean = gson.fromJson(response,ContentBean.class);
                if(contentBean.getResult()==0){
                    data.putString("result",contentBean.getContent());
                }else{
                    data.putString("result","我听不懂你在说什么呀！");
                }
                msg.setData(data);
                msg.what = 1;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initMsgs(); // 初始化消息数据
        inputText = findViewById(R.id.input_text);
        send =  findViewById(R.id.send);
        msgRecyclerView =  findViewById(R.id.msg_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);
        adapter = new MsgAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = inputText.getText().toString();
                if (!"".equals(content)) {
                    Msg msg = new Msg(content, Msg.TYPE_SENT);
                    msgList.add(msg);
                    getInter(content);
                    Log.e("url",RobotManager.getUrl(content));
                    adapter.notifyItemInserted(msgList.size() - 1); // 当有新消息时，刷新ListView中的显示
                    msgRecyclerView.scrollToPosition(msgList.size() - 1); // 将ListView定位到最后一行
                    inputText.setText(""); // 清空输入框中的内容

                }
            }
        });
    }

    private void initMsgs() {
        Msg msg1 = new Msg("我是菲菲，快来和我聊天吧* ( ´͈ ᵕ `͈ )◞♡", Msg.TYPE_RECEIVED);
        msgList.add(msg1);
    }

}
