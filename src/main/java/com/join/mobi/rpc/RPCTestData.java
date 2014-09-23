package com.join.mobi.rpc;

import com.join.mobi.dto.*;

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
        live.setUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        liveDtos.add(live);

        LiveDto live1 = new LiveDto();
        live1.setAuthor("李老师");
        live1.setCreateTime("2012-12-12");
        live1.setLiveId(1001);
        live1.setTitle("直播二");
        live1.setUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        liveDtos.add(live1);
        mainContentDto.setLives(liveDtos);
        //在线课程
        List<LiveCourseDto> courseDtos = new ArrayList<LiveCourseDto>(0);
        LiveCourseDto liveCourseDto = new LiveCourseDto();
        liveCourseDto.setUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        liveCourseDto.setTitle("课程一");
        liveCourseDto.setCourseHour(1000);
        liveCourseDto.setCourseId(1);
        liveCourseDto.setLastLearn("1小时前");
        liveCourseDto.setLearningTimes(1);
        liveCourseDto.setTotalDuration("60");
        courseDtos.add(liveCourseDto);

        LiveCourseDto liveCourseDto1 = new LiveCourseDto();
        liveCourseDto1.setUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        liveCourseDto1.setTitle("课程二");
        liveCourseDto1.setCourseHour(2000);
        liveCourseDto1.setCourseId(2);
        liveCourseDto1.setLastLearn("2小时前");
        liveCourseDto1.setLearningTimes(1);
        liveCourseDto1.setTotalDuration("80");
        courseDtos.add(liveCourseDto1);

        mainContentDto.setCourse(courseDtos);

        //共享资源
        List<ResourceShareDto> resourceShares = new ArrayList<ResourceShareDto>(0);
        ResourceShareDto share = new ResourceShareDto();
        share.setName("视频一");
        share.setFileSize(1232434);
        share.setType(1);
        share.setUrl("http://download.springsource.com/release/STS/3.2.0/dist/e3.8/spring-tool-suite-3.2.0.RELEASE-e3.8.2-macosx-cocoa-x86_64-installer.dmg");
        resourceShares.add(share);

        share = new ResourceShareDto();
        share.setName("音乐一");
        share.setFileSize(1232434);
        share.setType(2);
        share.setUrl("http://download-ln.jetbrains.com/idea/ideaIU-12.1.4.dmg");
        resourceShares.add(share);

        share = new ResourceShareDto();
        share.setName("pdf一");
        share.setFileSize(1232434);
        share.setType(3);
        share.setUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        resourceShares.add(share);

        share = new ResourceShareDto();
        share.setName("图片一");
        share.setFileSize(1232434);
        share.setType(4);
        share.setUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        resourceShares.add(share);

        mainContentDto.setResourceShare(resourceShares);


        return mainContentDto;
    }

    public static CourseDetailDto getCourseDetailDto() {
        CourseDetailDto courseDetailDto = new CourseDetailDto();
        //详情
        courseDetailDto.setCourseId(1);
        courseDetailDto.setName("课程标题一");
        courseDetailDto.setDescription("课程描述");
        courseDetailDto.setInstructor("课程老师一");
        courseDetailDto.setBranch("开课机构GZ");
        courseDetailDto.setCreateTime("课程创建时间");
        courseDetailDto.setTotalStudent(100);
        courseDetailDto.setLastLearn("最后学习时间:2014-06-12");
        courseDetailDto.setTotalHour(99);
        courseDetailDto.setCourseHour(1988);


        //测试
        List<ExamDto> exams = new ArrayList<ExamDto>(0);
        ExamDto exam = new ExamDto();
        exam.setExamId(100);
        exam.setName("测试标题");
        exam.setItemCount(10);
        exam.setStatus(0);
        exam.setFinishPercent("80");
        exam.setDurationLimit(50);
        exams.add(exam);

        ExamDto exam1 = new ExamDto();
        exam1.setExamId(101);
        exam1.setName("测试标题");
        exam1.setItemCount(10);
        exam1.setStatus(0);
        exam1.setFinishPercent("80");
        exam1.setDurationLimit(50);
        exams.add(exam1);
        courseDetailDto.setExam(exams);

        //章节

        List<ChapterDto> chapterDtos = new ArrayList<ChapterDto>(0);
        ChapterDto chapter = new ChapterDto();
        chapter.setChapterId(100);
        chapter.setTitle("第一章");
        chapter.setChapterDuration(2000);
        chapter.setLearnedTime(260);
        chapter.setBookMark("书签");
        chapter.setFileSize(260000);
        chapter.setPlayUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        chapter.setDownloadUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        chapter.setValidUntil("30");
        chapterDtos.add(chapter);

        ChapterDto chapter1 = new ChapterDto();
        chapter1.setChapterId(101);
        chapter1.setTitle("第二章");
        chapter1.setChapterDuration(2000);
        chapter1.setLearnedTime(260);
        chapter1.setBookMark("书签");
        chapter1.setFileSize(260000);
        chapter1.setPlayUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        chapter1.setDownloadUrl("http://cdn.mysql.com/Downloads/MySQL-5.6/mysql-5.6.10-osx10.7-x86_64.tar.gz");
        chapter.setValidUntil("30");
        chapterDtos.add(chapter1);

        ChapterDto chapter2 = new ChapterDto();
        chapter2.setChapterId(102);
        chapter2.setTitle("第三章");
        chapter2.setChapterDuration(2000);
        chapter2.setLearnedTime(260);
        chapter2.setBookMark("书签");
        chapter2.setFileSize(260000);
        chapter2.setPlayUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        chapter2.setDownloadUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        chapter.setValidUntil("30");
        chapterDtos.add(chapter2);
        courseDetailDto.setChapter(chapterDtos);

        //资料
        List<ReferenceDto> referenceDtos = new ArrayList<ReferenceDto>(0);
        ReferenceDto reference = new ReferenceDto();
        reference.setReferenceId(100);
        reference.setTitle("资料一");
        reference.setFileSize(20034343);
        reference.setType(1);
        reference.setUrl("http://s0.hao123img.com/res/r/image/2014-09-17/d6f8c018bd3e21e10a2911e1fac7e474.jpg");
        referenceDtos.add(reference);

        ReferenceDto reference1 = new ReferenceDto();
        reference1.setReferenceId(101);
        reference1.setTitle("资料二");
        reference1.setFileSize(20034343);
        reference1.setType(2);
        reference1.setUrl("https://repository.mulesoft.org/nexus/content/repositories/releases/org/mule/distributions/mule-standalone/3.5.0/mule-standalone-3.5.0.zip");
        referenceDtos.add(reference1);
        courseDetailDto.setReference(referenceDtos);


        return courseDetailDto;
    }


    public static ExamDto getExamDetail() {
        ExamDto examDto = new ExamDto();
        examDto.setTitle("章节测试001");
        examDto.setItemCount(5);
        examDto.setDurationLimit(50);
        examDto.setCreateTime("2013-12-12");
        examDto.setExamTime("2014-12-12");
        examDto.setCorrectPercent("66%");
        examDto.setFinishPercent("100");
        examDto.setDuration(16);

        List<ExamItem> examItems = new ArrayList<ExamItem>(0);

        ExamItem examItem = new ExamItem();
        examItem.setItemId(1);
        examItem.setTitle("这里是题目标题");
        examItem.setType(1);
        examItem.setCreateTime("2014-12-12");
        examItems.add(examItem);

        ItemOption option = new ItemOption();
        option.setOptionId(1);
        option.setOptionCode("t");
        option.setTitle("答案一");

        ItemOption option1 = new ItemOption();
        option1.setOptionId(2);
        option1.setOptionCode("f");
        option1.setTitle("答案二");

        List<ItemOption> options = new ArrayList<ItemOption>(0);
        options.add(option);
        options.add(option1);
        examItem.setItemOptions(options);


        ExamItem examItem1 = new ExamItem();
        examItem1.setItemId(2);
        examItem1.setTitle("这里是题目二标题");
        examItem1.setType(1);
        examItem1.setCreateTime("2014-12-12");
        examItems.add(examItem1);

        ItemOption option2 = new ItemOption();
        option2.setOptionId(1);
        option2.setOptionCode("t");
        option2.setTitle("答案一");

        ItemOption option3 = new ItemOption();
        option3.setOptionId(2);
        option3.setOptionCode("f");
        option3.setTitle("答案二");

        List<ItemOption> options1 = new ArrayList<ItemOption>(0);
        options1.add(option2);
        options1.add(option3);
        examItem1.setItemOptions(options1);


        examItem1 = new ExamItem();
        examItem1.setItemId(3);
        examItem1.setTitle("这里是题目三标题");
        examItem1.setType(3);
        examItem1.setCreateTime("2014-12-12");
        examItems.add(examItem1);

        option2 = new ItemOption();
        option2.setOptionId(1);
        option2.setOptionCode("t");
        option2.setTitle("答案一");

        option3 = new ItemOption();
        option3.setOptionId(2);
        option3.setOptionCode("f");
        option3.setTitle("答案二");

        options1 = new ArrayList<ItemOption>(0);
        options1.add(option2);
        options1.add(option3);
        examItem1.setItemOptions(options1);


        examDto.setExamItems(examItems);
        return examDto;
    }
}
