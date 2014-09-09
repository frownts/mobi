package com.join.mobi.rpc;

import com.join.mobi.dto.LiveCourseDto;
import com.join.mobi.dto.LiveDto;
import com.join.mobi.dto.MainContentDto;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mawanjin@join-cn.com
 * Date: 14-9-9
 * Time: 下午8:44
 */
public class RPCTestData {

    public static MainContentDto getMainContentDto() {
        MainContentDto mainContentDto = new MainContentDto();
        List<LiveDto> liveDtos = new ArrayList<LiveDto>(0);
        LiveDto live = new LiveDto();
        live.setAuthor("王老师");
        live.setCreateTime("2012-12-12");
        live.setLiveId(1000);
        live.setTitle("直播一");
        live.setUrl("http://www.baidu.com/img/bd_logo1.png");
        liveDtos.add(live);

        LiveDto live1 = new LiveDto();
        live1.setAuthor("李老师");
        live1.setCreateTime("2012-12-12");
        live1.setLiveId(1001);
        live1.setTitle("直播二");
        live1.setUrl("http://www.baidu.com/img/bd_logo1.png");
        liveDtos.add(live1);
        mainContentDto.setLives(liveDtos);
        //在线课程
        List<LiveCourseDto> courseDtos = new ArrayList<LiveCourseDto>(0);
        LiveCourseDto liveCourseDto = new LiveCourseDto();
        liveCourseDto.setUrl("http://www.baidu.com/img/bd_logo1.png");
        liveCourseDto.setTitle("课程一");
        liveCourseDto.setCourseHour(1000);
        liveCourseDto.setCourseId(1);
        liveCourseDto.setLastLearn("1小时前");
        liveCourseDto.setLearningTimes(1);
        liveCourseDto.setTotalDuration("20:20");

        courseDtos.add(liveCourseDto);

        mainContentDto.setCourses(courseDtos);


        return mainContentDto;
    }
}
