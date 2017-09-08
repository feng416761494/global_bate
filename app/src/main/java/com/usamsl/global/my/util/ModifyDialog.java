package com.usamsl.global.my.util;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.usamsl.global.R;

/**
 * Created by Administrator on 2017/1/3.
 */
public class ModifyDialog extends Dialog {
    private EditText edit_modify;
    private Button btn_commit;

    public ModifyDialog(Context context,String name) {
        super(context, R.style.noTitleDialog);
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.dialog_modify, null);
        edit_modify = (EditText)view.findViewById(R.id.edit_modify);
        btn_commit = (Button) view.findViewById(R.id.btn_commit);
        edit_modify.setText(name);
        super.setContentView(view);
    }

    public EditText getEditText(){
        return edit_modify;
    }
    public void setOnClickCommitListener(View.OnClickListener listener){
        btn_commit.setOnClickListener(listener);
    }
}
