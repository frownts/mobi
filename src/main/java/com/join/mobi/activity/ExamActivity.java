package com.join.mobi.activity;

import android.os.CountDownTimer;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.android.app.common.manager.DialogManager;
import com.join.android.app.common.utils.DateUtils;
import com.join.mobi.adapter.ExamView;
import com.join.mobi.adapter.ExamViewPagerAdapter;
import com.join.mobi.dto.ExamDto;
import com.join.mobi.dto.ExamItem;
import com.join.mobi.dto.ItemOption;
import com.join.mobi.pref.PrefDef_;
import com.join.mobi.rpc.ExamResult;
import com.join.mobi.rpc.RPCService;
import com.join.mobi.utils.MarkerMap;
import org.androidannotations.annotations.*;
import org.androidannotations.annotations.rest.RestService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.text.DecimalFormat;
import java.util.*;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-11
 * Time: 上午10:48
 */
@EActivity(R.layout.exam_activity_layout)
public class ExamActivity extends BaseActivity {
    public static ExamDto currentExamDto;

    @RestService
    RPCService rpcService;

    @Pref
    PrefDef_ myPref;

    @Extra
    ExamDto examDto;

    @Extra
    int examIndex;

    @ViewById
    ViewPager viewpager;

    @ViewById
    TextView countDown;

    @ViewById
    TextView title;

    @ViewById
    TextView markTxt;

    @ViewById
    TextView total;

    @ViewById
    TextView examNum;

    @ViewById
    ImageView examList;

    @ViewById
    ImageView commitExam;

    @ViewById
    ImageView mark;

    private List<ExamView> pagerView;

    ExamViewPagerAdapter examViewPagerAdapter;

    private int next = 2;
    private int pre = 1;

    //计数器总时长
    long count;
    //当前显示的试题编号
//    long currentExamId = 2;
    private int CurrentPageIndex = 0;//当前页面下标
    ExamItem currentExamItem;

    Date startTime = new Date();
    MyCountDownTimer myCountDownTimer;
    boolean hasFinished = false;

    @AfterViews
    void afterViews() {

        if (examDto != null) currentExamDto = examDto;
        else
            examDto = currentExamDto;
        total.setText(total.getText().toString().replace("$1", examDto.getExamItems().size() + ""));
        examNum.setText(examNum.getText().toString().replace("$1", "1"));
        next = examIndex + 1;
        if (next > examDto.getExamItems().size() - 1)
            next = examIndex;

        pre = examIndex;
        if (pre < 0) pre = 0;

        currentExamItem = examDto.getExamItems().get(examIndex);

        title.setText(examDto.getTitle());
        startCountDown();
        initViews();
        examViewPagerAdapter = new ExamViewPagerAdapter(pagerView);
        viewpager.setAdapter(examViewPagerAdapter);
        viewpager.setOnPageChangeListener(new OnPagerChangeListener());
        if (next != examDto.getExamItems().size() - 1)
            loadNextPage();

        loadPrePage();
        updateMarker();
    }

    void reload() {
//        currentExamId = Long.parseLong(pagerView.get(CurrentPageIndex).getExamId());
        currentExamItem = examDto.getExamItems().get(pagerView.get(CurrentPageIndex).getExamIndex());
        examNum.setText("第" + (pagerView.get(CurrentPageIndex).getExamIndex() + 1) + "题");
        updateMarker();
    }

    Map<String, List<ImageView>> tmpOptionMap = new HashMap<String, List<ImageView>>(0);

    private void generateOptions(ExamItem examItem, View view) {
        List<ImageView> tmpOption = new ArrayList<ImageView>(0);
        tmpOptionMap.put(examItem.getItemId() + "", tmpOption);

        LinearLayout optionContainer = (LinearLayout) view.findViewById(R.id.optionsContainer);
        //1 判断 2单选 3多选
        final int type = examItem.getType();
        final List<ItemOption> options = examItem.getItemOptions();
        for (final ItemOption o : options) {
            LinearLayout option = new LinearLayout(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 30);
            option.setLayoutParams(params);
            final ImageView imageView = new ImageView(this);

            if (type == 3) {
                if (o.isSelected())
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_option_selected));
                else
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_option));
            } else {
                if (o.isSelected()) {
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.radio_option_selected));
                } else
                    imageView.setImageDrawable(getResources().getDrawable(R.drawable.radio_option));
            }


            TextView txt = new TextView(this);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setPadding(20, 0, 0, 0);
            txt.setText(o.getTitle());
            option.addView(imageView);
            option.addView(txt);
            optionContainer.addView(option);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (type == 3) {
                        if (imageView.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.checkbox_option).getConstantState())) {//未选中
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_option_selected));
                            o.setSelected(true);
                        } else {
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.checkbox_option));
                            o.setSelected(false);
                        }
                    } else {//单选
                        if (imageView.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.radio_option).getConstantState())) {//未选中

                            for (ItemOption _o : options) {
                                _o.setSelected(false);
                            }

                            List<ImageView> _tmpOption = tmpOptionMap.get(currentExamItem.getItemId() + "");
                            for (ImageView imageView1 : _tmpOption) {
                                imageView1.setImageDrawable(getResources().getDrawable(R.drawable.radio_option));
                            }
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.radio_option_selected));
                            o.setSelected(true);
                        } else {
                            imageView.setImageDrawable(getResources().getDrawable(R.drawable.radio_option));
                            o.setSelected(false);
                        }
                    }
                }
            });
            tmpOption.add(imageView);
        }


    }


    private void updateMarker() {
        if (MarkerMap.isMarked(currentExamItem.getItemId() + "")) {
            mark.setImageDrawable(this.getResources().getDrawable(R.drawable.gray_flag));
            markTxt.setText("消记号");
        } else {
            mark.setImageDrawable(this.getResources().getDrawable(R.drawable.red_flag));
            markTxt.setText("做记号");
        }
    }


    void startCountDown() {
        count = examDto.getDurationLimit();
        myCountDownTimer = new MyCountDownTimer(count * 1000);
        myCountDownTimer.start();
    }

    @UiThread
    void updatCountDown(String _count) {
        examDto.setDurationLimit(Long.parseLong(_count));
        countDown.setText(DateUtils.SecondToNormalTime(Long.parseLong(_count)));
        if (_count.equals(0)) {
            DialogManager.getInstance().createNormalDialog(this, "时间到", "测试时间已到，系统将自动为您交卷。");
            DialogManager.getInstance().setOk("好的", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogManager.getInstance().dismiss();
                    hasFinished=true;
                    commitExamClicked();
                }
            });
        }
    }

    @Click
    void markClicked() {
        if (MarkerMap.isMarked(currentExamItem.getItemId() + "")) {
            MarkerMap.unMark(currentExamItem.getItemId() + "");
            mark.setImageDrawable(this.getResources().getDrawable(R.drawable.red_flag));
            markTxt.setText("做记号");
        } else {
            MarkerMap.mark(currentExamItem.getItemId() + "");
            mark.setImageDrawable(this.getResources().getDrawable(R.drawable.gray_flag));
            markTxt.setText("消记号");
        }

    }


    @Click
    void examListClicked() {
        ExamListActivity_.intent(this).examItems(examDto.getExamItems()).start();
        finish();
    }


    /**
     * 交卷
     */
    @Click
    void commitExamClicked() {
        //计算结果
        final ExamResult examResult = speculateExamResult();
        if (!hasFinished) {
            DialogManager.getInstance().createNormalDialog(this, "交卷提醒", "还有尚未完成的试题,确定要交卷吗?提示:列表中显示为黑色粗体的试题是尚未完成的。");
            DialogManager.getInstance().setOk("不,继续完成试题", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogManager.getInstance().dismiss();
                }
            });
            DialogManager.getInstance().setCancel("是的,我要交卷", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DialogManager.getInstance().dismiss();
                    sumit(examResult);
                }
            });

        }else
            sumit(examResult);


    }

    /**
     * 将结果提交到服务器
     */
    @Background
    void sumit(ExamResult examResult) {
        rpcService.submitExamResult(myPref.rpcUserId().get(), examDto.getExamId() + "", examResult.getCorrectPercent(), examResult.getFinishPersenct(), examResult.getStartTime(), examResult.getDuration());
        showExamResult(examResult);
    }

    @UiThread
    void showExamResult(ExamResult examResult) {
        ExamResultActivity_.intent(this).examDto(examDto).examResult(examResult).start();
        finish();
    }

    private ExamResult speculateExamResult() {
        int finishNum = 0;
        int correctNum = 0;

        List<ExamItem> examItems = examDto.getExamItems();
        for (ExamItem examItem : examItems) {
            boolean selected = false;
            boolean correct = true;
            List<ItemOption> options = examItem.getItemOptions();

            for (ItemOption option : options) {
                if (option.isSelected()) {
                    selected = true;
                } else {
                    if (option.getOptionCode().equals("t")) correct = false;
                }
            }

            if (correct) {
                correctNum++;
                examItem.setCorrect(true);
            }
            if (selected) finishNum++;

        }

        if (finishNum == examItems.size()) hasFinished = true;
        float finishPercent = ((float) finishNum / (float) examItems.size()) * 100;
        float correctPercent = ((float) correctNum / (float) examItems.size()) * 100;

        DecimalFormat decimalFormat = new DecimalFormat(".00");

        ExamResult examResult = new ExamResult();
        examResult.setCorrectNum(correctNum + "");
        examResult.setIncorrectNum((examItems.size() - correctNum) + "");

        String tempCorrectPercent = decimalFormat.format(correctPercent);
        if(tempCorrectPercent.equals(".00")){
            tempCorrectPercent = "0";
        }else if(tempCorrectPercent.endsWith(".0")||tempCorrectPercent.equals(".00")){
            tempCorrectPercent = tempCorrectPercent.substring(0,tempCorrectPercent.indexOf("."));
        }


        examResult.setCorrectPercent(tempCorrectPercent);
//        examResult.setFinishPersenct(decimalFormat.format(finishPercent));
        examResult.setFinishPersenct(finishNum+"");
        examResult.setDuration(((System.currentTimeMillis()-startTime.getTime())/1000)+"");
        examResult.setStartTime(DateUtils.ConvertDateToNormalString(startTime));

        return examResult;


    }


    /**
     * 初始化当前页面的数据
     */
    private void initViews() {
        pagerView = new ArrayList<ExamView>();
        //动态为组件赋值
        wrapAdapterView(true, examIndex);
    }


    private void wrapAdapterView(boolean isPre, int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.exam_page, null);
        TextView stem = (TextView) view.findViewById(R.id.stem);
        ExamItem examItem = examDto.getExamItems().get(index);
        stem.setText(examItem.getTitle());
        generateOptions(examItem, view);
        ExamView examView = new ExamView(view, index);
        if (isPre) {
            pagerView.add(0, examView);
        } else
            pagerView.add(examView);
    }

    public void onBackPressed() {

    }

    /**
     * 加载下一页数据
     */
    private void loadNextPage() {
        if (CurrentPageIndex == pagerView.size() - 1) {
            if (next == examIndex || next >= (examDto.getExamItems().size())) {
                //无数据了,不需要继续加载
                return;
            }

            //在pagerView最后追加一个页面
            wrapAdapterView(false, next);
            ++next;
            examViewPagerAdapter.notifyDataSetChanged();
        }
    }

    private void loadPrePage() {
        if (CurrentPageIndex == 0) {
            if (pre <= 0) {
                //无数据了,不需要继续加载上一页
                return;
            }
            pre -= 1;

            //在LstView前面追加一个页面
            wrapAdapterView(true, pre);
            examViewPagerAdapter = new ExamViewPagerAdapter(pagerView);
            viewpager.setAdapter(examViewPagerAdapter);

            //因集合下标发生了变化，当前页面的下标需要变动
            CurrentPageIndex = 1;
            viewpager.setCurrentItem(CurrentPageIndex, false);

        }
    }


    /**
     * ViewPager滑动监听事件
     */
    class OnPagerChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int arg0) {
            CurrentPageIndex = arg0;
            loadNextPage();
            loadPrePage();
            reload();
        }

    }

    class MyCountDownTimer extends CountDownTimer {

        public MyCountDownTimer(long millisInFuture) {
//            super(millisInFuture, countDownInterval);
            super(millisInFuture, 1000);
        }

        @Override
        public void onTick(long l) {
            updatCountDown((l / 1000) + "");
        }

        @Override
        public void onFinish() {
            DialogManager.getInstance().makeText(getBaseContext(), "timeOut", DialogManager.DIALOG_TYPE_OK);
        }
    }
}
