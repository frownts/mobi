package com.join.android.app.common.activity;

import com.BaseActivity;
import com.join.android.app.common.R;
import com.join.android.app.common.rest.BookmarkClient;
import com.join.android.app.common.rest.RPCResult;
import com.join.android.app.common.rest.dto.Recommend;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.rest.RestService;
import org.springframework.web.client.RestClientException;

import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-2-21
 * Time: 下午4:23
 */
@EActivity(R.layout.restful_activity)
public class RestFulActivity extends BaseActivity {
    @RestService
    BookmarkClient bookmarkClient;

    @AfterViews
    void afterViews() {
        recommendThread();
    }

    @Background
    void recommendThread(){
        try {
            RPCResult<List<Recommend>> recommendRPCResult = bookmarkClient.getAccounts();
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }
}
