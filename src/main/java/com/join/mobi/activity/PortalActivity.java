package com.join.mobi.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.*;
import android.widget.*;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.android.app.common.manager.DialogManager;
import com.join.mobi.adapter.PortalMenuAdapter;
import com.join.mobi.customview.MySpinner;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.RPCService;
import com.php25.PDownload.DownloadApplication;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.ArrayList;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-6-30
 * Time: 下午10:16
 */
@EActivity(R.layout.portal_activity_layout)
public class PortalActivity extends BaseActivity implements View.OnClickListener {

    @Pref
    PrefDef_ myPref;
    @ViewById
    GridView gridViewMenu;
    @ViewById(resName = "imgLogin")
    ImageView imgLogin;
    @ViewById
    RelativeLayout userNameContainer;
    @ViewById
    TextView userName;

    @RestService
    RPCService rpcService;
    Dialog loginDialog;

    long waitTime = 2000;
    long touchTime = 0;
    ArrayList<String> data = new ArrayList<String>();
    EditText branch;
    PortalMenuAdapter menuAdapter;


    @AfterViews
    void afterViews() {
        if(myPref.uncompleteDownload().get())((DownloadApplication)getApplicationContext()).startAllUnCompleteDownload();

        menuAdapter = new PortalMenuAdapter(this, myPref.isLogin().get(), new PortalMenuAdapter.NeedLogin() {
            @Override
            public void login() {
                imgLogin.performClick();
            }
        });
        gridViewMenu.setAdapter(menuAdapter);
    }


    public void showSetting() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_setting, null);
        wrapEvent(view);
        settingDialog = new AlertDialog.Builder(this).create();
        settingDialog.setCancelable(false);
        settingDialog.show();
        Window dialogWindow = settingDialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        dialogWindow.setGravity(Gravity.CENTER);
        int width = DialogManager.getInstance().windowWidth(this);
        lp.width = (int) (width * 0.7); // 宽度

        dialogWindow.setAttributes(lp);
        settingDialog.setContentView(view);

        ImageView cancel = (ImageView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingDialog.dismiss();
            }
        });
        initSetting();
    }

    private void initSetting() {
        boolean buncompleteDownload = myPref.uncompleteDownload().get();
        boolean bcontinueOnWifi = myPref.continueOnWifi().get();
        boolean bannonunceWhenDownload = myPref.annonunceWhenDownload().get();
        boolean bautoLogin = myPref.autoLogin().get();

        if (buncompleteDownload) {
            uncompleteDownload.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
        } else
            uncompleteDownload.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));

        if (bcontinueOnWifi) {
            continueOnWifi.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
        } else
            continueOnWifi.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));

        if (bannonunceWhenDownload) {
            annonunceWhenDownload.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
        } else
            annonunceWhenDownload.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));

        if (bautoLogin) {
            autoLogin.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
        } else
            autoLogin.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));

    }

    private void wrapEvent(View view) {
        uncompleteDownload = (ImageView) view.findViewById(R.id.uncompleteDownload);
        continueOnWifi = (ImageView) view.findViewById(R.id.continueOnWifi);
        annonunceWhenDownload = (ImageView) view.findViewById(R.id.annonunceWhenDownload);
        autoLogin = (ImageView) view.findViewById(R.id.autoLogin);
        logout = (ImageView) view.findViewById(R.id.logout);

        uncompleteDownload.setOnClickListener(this);
        continueOnWifi.setOnClickListener(this);
        annonunceWhenDownload.setOnClickListener(this);
        autoLogin.setOnClickListener(this);
        logout.setOnClickListener(this);

    }

    @Click
    void imgLoginClicked() {
        loginDialog = new Dialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_login, null);
        final EditText loginName = (EditText) view.findViewById(R.id.loginName);
        final EditText passWord = (EditText) view.findViewById(R.id.passWord);
        final EditText branch = (EditText) view.findViewById(R.id.branch);

        //登录
        view.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoading();
                doLogin(loginName.getText().toString(),passWord.getText().toString(),branch.getText().toString());

            }
        });

        final MySpinner spinner = (MySpinner) view.findViewById(R.id.spinner_btn);


        String[] item = {"上海分公司", "深圳分公司", "北京分公司", "江苏分公司", "广东分公司", "佛山分公司", "江门分公司", "东莞分公司"};
        for (int i = 0; i < item.length; i++) {
            data.add(item[i]);
        }
        spinner.setData(data);


        branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner.performClick();
            }
        });

        spinner.setOnItemSelectedListener(new SpinnerOnItemSelectedListener());


        loginDialog.setCancelable(false);
        loginDialog.show();
        Window dialogWindow = loginDialog.getWindow();
        dialogWindow.setBackgroundDrawable(new ColorDrawable(0));
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        int width = DialogManager.getInstance().windowWidth(this);
        lp.width = (int) (width * 0.7); // 宽度

//        lp.alpha = 0.7f; // 透明度
        dialogWindow.setAttributes(lp);
        loginDialog.setContentView(view);

        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginDialog.dismiss();
            }
        });

    }

    @Background
     void doLogin(String userId,String password,String companyId) {
        myPref.userId().put(userId);
        String _companyCode = "0986";
        if(companyId.equals("深圳分公司")){
            _companyCode = "1086";
        }else if(companyId.equals("北京分公司")){
            _companyCode = "1186";
        }else if(companyId.equals("江苏分公司")){
            _companyCode = "1286";
        }else if(companyId.equals("广东分公司")){
            _companyCode = "2586";
        }else if(companyId.equals("佛山分公司")){
            _companyCode = "2686";
        }else if(companyId.equals("江门分公司")){
            _companyCode = "2786";
        }else if(companyId.equals("东莞分公司")){
            _companyCode = "2886";
        }
//        try{
//
//            LoginDto loginDto = rpcService.login(userId, password, _companyCode);
//            String a = loginDto.getLogined();
//            if(!a.equals("S0008")){
//                DialogManager.getInstance().makeText(this,"用户名或密码错误",DialogManager.DIALOG_TYPE_OK);
//                return;
//            }
//            myPref.showName().put(loginDto.getUserName());
//            reloadAfterLogin(loginDto.getUserName());
//        }catch (Exception e){
//            rpcException();
//            return;
//        }

        reloadAfterLogin("abc");
    }


    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - touchTime) >= waitTime) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            touchTime = currentTime;
        } else {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.uncompleteDownload:
                if (uncompleteDownload.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.switch_on).getConstantState())) {
                    uncompleteDownload.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
                    myPref.uncompleteDownload().put(false);
                } else {
                    uncompleteDownload.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
                    myPref.uncompleteDownload().put(true);
                }

                break;
            case R.id.continueOnWifi:
                if (continueOnWifi.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.switch_on).getConstantState())) {
                    continueOnWifi.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
                    myPref.continueOnWifi().put(false);
                } else {
                    continueOnWifi.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
                    myPref.continueOnWifi().put(true);
                }

                break;
            case R.id.annonunceWhenDownload:
                if (annonunceWhenDownload.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.switch_on).getConstantState())) {
                    annonunceWhenDownload.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
                    myPref.annonunceWhenDownload().put(false);
                } else {
                    myPref.annonunceWhenDownload().put(true);
                    annonunceWhenDownload.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
                }
                break;
            case R.id.autoLogin:
                if (autoLogin.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.switch_on).getConstantState())) {
                    autoLogin.setImageDrawable(getResources().getDrawable(R.drawable.switch_off));
                    myPref.autoLogin().put(false);
                } else {
                    autoLogin.setImageDrawable(getResources().getDrawable(R.drawable.switch_on));
                    myPref.autoLogin().put(true);
                }

                break;
            case R.id.logout:
                myPref.clear();
                reloadAfterLogout();
                settingDialog.dismiss();
                break;
        }
    }

    @UiThread
    public void reloadAfterLogin(String _userName) {
        dismissLoading();
        loginDialog.dismiss();
//        createDB(myPref.userId().get());
        menuAdapter.setLogin(true);
        menuAdapter.notifyDataSetChanged();
        userNameContainer.setVisibility(View.VISIBLE);
        userName.setText(_userName);
        imgLogin.setEnabled(false);
    }

    public void reloadAfterLogout() {
        menuAdapter.setLogin(false);
        menuAdapter.notifyDataSetChanged();
        userNameContainer.setVisibility(View.GONE);
        imgLogin.setEnabled(true);
    }

    @UiThread
    public void rpcException(){
        DialogManager.getInstance().makeText(this,getString(R.string.net_excption),DialogManager.DIALOG_TYPE_WARRING);
        dismissLoading();
    }

    class SpinnerOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
            branch.setText(data.get(position));
        }

        public void onNothingSelected(AdapterView<?> arg0) {

        }

    }

    AlertDialog settingDialog;
    ImageView uncompleteDownload;
    ImageView continueOnWifi;
    ImageView annonunceWhenDownload;
    ImageView autoLogin;
    ImageView logout;

}


