package com.join.mobi.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.join.android.app.common.R;
import com.php25.PDownload.DownloadFile;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-5
 * Time: 下午11:58
 */
public class DownloadOpDialog extends AlertDialog {
    Context context;
    Op op;
    DownloadFile downloadFile;

    public DownloadOpDialog(Context _context, DownloadFile _downloadFile,Op _op) {
        super(_context);
        context = _context;
        op = _op;
        downloadFile = _downloadFile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.download_op_popupwindow, null);


        TextView oper =  (TextView)view.findViewById(R.id.oper);
        if(downloadFile.isDownloadingNow()){
            oper.setText("暂停");
        }else{
            oper.setText("启动");
        }
        oper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                op.onOpAction();
            }
        });

        ((TextView)view.findViewById(R.id.title)).setText(downloadFile.getDtype() + ":" + downloadFile.getShowName());

        view.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadOpDialog.this.dismiss();
            }
        });
        //删除
        view.findViewById(R.id.del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (op != null) op.onDel();
            }
        });
        setContentView(view);
    }

    public interface Op {
        public void onDel();

        /** 启动和暂停操作*/
        public void onOpAction();
    }

}
