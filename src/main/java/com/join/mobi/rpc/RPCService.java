package com.join.mobi.rpc;

import android.util.Log;
import com.join.mobi.dto.CourseDetailDto;
import com.join.mobi.dto.ExamDto;
import com.join.mobi.dto.LoginDto;
import com.join.mobi.dto.MainContentDto;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

import java.io.IOException;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-8
 * Time: 下午7:54
 */
@Rest(rootUrl = " http://192.168.1.190/app/", converters = MappingJacksonHttpMessageConverter.class, interceptors = HttpBasicAuthenticatorInterceptor.class)
public interface RPCService {

    /**
     * 登录
     *
     * @param userId
     * @param password
     * @param companyId
     * @return
     */
    @Get("login.jsp?userId={userId}&password={password}&companyId={companyId}")
    public LoginDto login(String userId, String password, String companyId);

    /**
     * 学习记录上报
     *
     * @param userId
     * @param startTime
     * @param duration
     * @param courseId
     * @param chapterId
     * @param bookmark
     */
    @Get("commitLearningLog.jsp?userId={userId}&startTime={startTime}&duration={duration}&courseId={courseId}&chapterId={chapterId}&bookmark={bookmark}&type=4")
    public void commitLearningLog(String userId, String startTime, String duration, String courseId, String chapterId, String bookmark);


    /**
     * 首页数据
     *
     * @param userId
     * @return
     */
    @Get("index.jsp?userId={userId}")
    public MainContentDto getMainContent(String userId);


    /**
     * 课程详情
     *
     * @param userId
     * @param courseId
     * @return
     */
    @Get("courseDetail.jsp?userId={userId}&courseId={courseId}")
    public CourseDetailDto getCourseDetail(String userId, String courseId);

    /**
     * 个人考试详情
     *
     * @param userId
     * @return
     */
    @Get("examDetail.jsp?userId={userId}")
    public ExamDto getExamDetail(String userId);

    /**
     * 交卷
     *
     * @param userId
     * @param examId
     * @param correctPercent
     * @param finishPercent
     * @param startTime
     * @param duration
     */
    @Get("submitExamResult.jsp?userId={userId}&examId={examId}&correctPercent={correctPercent}&finishPercent={finishPercent}&startTime={startTime}&duration={duration}")
    public void SubmitExam(String userId, String examId, String correctPercent, String finishPercent, String startTime, String duration);

    /**
     * 共享资源下载汇报
     *
     * @param userId
     * @param shareResourceId
     */
    @Get("recordDownloadShareResource.jsp?userId={userId}&shareResourceId={shareResourceId}")
    public void recordDownloadShareResource(String userId, String shareResourceId);

    /**
     * 课件资源下载汇报
     *
     * @param userId
     * @param coursewareId
     * @param resourceId
     * @param courseId
     */
    @Get("recordDownloadCourseware.jsp?userId={userId}&coursewareId={coursewareId}&resourceId={resourceId}&courseId={courseId}")
    public void recordDownloadCourseware(String userId, String coursewareId, String resourceId, String courseId);

    /**
     * 离线数据同步
     *
     * @param chapterIds
     * @param referenceIds
     * @param shareResourceIds
     */
    @Get("synchronousData.jsp?chapterIds={chapterIds}&referenceIds={referenceIds}&shareResourceIds={shareResourceIds}")
    public void synchronousData(String chapterIds, String referenceIds, String shareResourceIds);

}

class HttpBasicAuthenticatorInterceptor implements ClientHttpRequestInterceptor {
    public HttpBasicAuthenticatorInterceptor() {
    }

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution) throws IOException {
        // do something
        Log.d(RPCService.class.getName(), "retrieve from" + request.getURI().toString());
        return execution.execute(request, data);
    }
}
