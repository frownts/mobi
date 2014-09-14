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
        live.setUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        liveDtos.add(live);

        LiveDto live1 = new LiveDto();
        live1.setAuthor("李老师");
        live1.setCreateTime("2012-12-12");
        live1.setLiveId(1001);
        live1.setTitle("直播二");
        live1.setUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        liveDtos.add(live1);
        mainContentDto.setLives(liveDtos);
        //在线课程
        List<LiveCourseDto> courseDtos = new ArrayList<LiveCourseDto>(0);
        LiveCourseDto liveCourseDto = new LiveCourseDto();
        liveCourseDto.setUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        liveCourseDto.setTitle("课程一");
        liveCourseDto.setCourseHour(1000);
        liveCourseDto.setCourseId(1);
        liveCourseDto.setLastLearn("1小时前");
        liveCourseDto.setLearningTimes(1);
        liveCourseDto.setTotalDuration("20:20");

        courseDtos.add(liveCourseDto);

        LiveCourseDto liveCourseDto1 = new LiveCourseDto();
        liveCourseDto1.setUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        liveCourseDto1.setTitle("课程二");
        liveCourseDto1.setCourseHour(2000);
        liveCourseDto1.setCourseId(1);
        liveCourseDto1.setLastLearn("2小时前");
        liveCourseDto1.setLearningTimes(1);
        liveCourseDto1.setTotalDuration("21:20");

        courseDtos.add(liveCourseDto1);

        mainContentDto.setCourses(courseDtos);

        //共享资源
        List<ResourceShareDto> resourceShares = new ArrayList<ResourceShareDto>(0);
        ResourceShareDto share = new ResourceShareDto();
        share.setName("视频一");
        share.setFileSize(1232434);
        share.setType(1);
        share.setUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        resourceShares.add(share);

        share = new ResourceShareDto();
        share.setName("音乐一");
        share.setFileSize(1232434);
        share.setType(2);
        share.setUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        resourceShares.add(share);

        share = new ResourceShareDto();
        share.setName("pdf一");
        share.setFileSize(1232434);
        share.setType(3);
        share.setUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        resourceShares.add(share);

        share = new ResourceShareDto();
        share.setName("图片一");
        share.setFileSize(1232434);
        share.setType(4);
        share.setUrl("http://s0.hao123img.com/res/img/logo/logonew.png");
        resourceShares.add(share);

        mainContentDto.setResourceShares(resourceShares);


        return mainContentDto;
    }

    public static CourseDetailDto getCourseDetailDto() {
        CourseDetailDto courseDetailDto = new CourseDetailDto();
        //详情
        courseDetailDto.setName("课程标题一");
        courseDetailDto.setDescription("课程描述");
        courseDetailDto.setInstructor("课程老师一");
        courseDetailDto.setBranch("开课机构GZ");
        courseDetailDto.setCreateTime("课程创建时间");
        courseDetailDto.setTotalStudent(100);
        courseDetailDto.setLastLearn("最后学习时间:2014-06-12");
        courseDetailDto.setTotalHour(99);
        courseDetailDto.setCourseHour(1988);
        courseDetailDto.setValidUntil(100);


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
        courseDetailDto.setExams(exams);

        //章节

        List<ChapterDto> chapterDtos = new ArrayList<ChapterDto>(0);
        ChapterDto chapter = new ChapterDto();
        chapter.setChapterId(100);
        chapter.setTitle("第一章");
        chapter.setChapterDuration(2000);
        chapter.setLearnedTime(260);
        chapter.setBookmark("书签");
        chapter.setFilesize(260000);
        chapter.setPlayUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        chapter.setDownloadUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        chapterDtos.add(chapter);

        ChapterDto chapter1 = new ChapterDto();
        chapter1.setChapterId(101);
        chapter1.setTitle("第二章");
        chapter1.setChapterDuration(2000);
        chapter1.setLearnedTime(260);
        chapter1.setBookmark("书签");
        chapter1.setFilesize(260000);
        chapter1.setPlayUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        chapter1.setDownloadUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        chapterDtos.add(chapter1);

        ChapterDto chapter2 = new ChapterDto();
        chapter2.setChapterId(101);
        chapter2.setTitle("第三章");
        chapter2.setChapterDuration(2000);
        chapter2.setLearnedTime(260);
        chapter2.setBookmark("书签");
        chapter2.setFilesize(260000);
        chapter2.setPlayUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        chapter2.setDownloadUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        chapterDtos.add(chapter2);
        courseDetailDto.setChapters(chapterDtos);

        //资料
        List<ReferenceDto> referenceDtos = new ArrayList<ReferenceDto>(0);
        ReferenceDto reference = new ReferenceDto();
        reference.setReferenceId(100);
        reference.setTitle("资料一");
        reference.setFileSize(20034343);
        reference.setType(1);
        reference.setUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        referenceDtos.add(reference);

        ReferenceDto reference1 = new ReferenceDto();
        reference1.setReferenceId(101);
        reference1.setTitle("资料二");
        reference1.setFileSize(20034343);
        reference1.setType(1);
        reference1.setUrl("http://su.bdimg.com/static/superplus/img/logo_white_ee663702.png");
        referenceDtos.add(reference1);
        courseDetailDto.setReferences(referenceDtos);


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
