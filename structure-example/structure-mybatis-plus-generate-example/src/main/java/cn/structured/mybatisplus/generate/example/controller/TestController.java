package cn.structured.mybatisplus.generate.example.controller;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import cn.structured.mybatisplus.generate.example.dao.OrgPostMapper;
import cn.structured.mybatisplus.generate.example.entity.OrgPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 测试
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2022/5/13 20:33
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private OrgPostMapper orgPostMapper;

    @RequestMapping("/save")
    public void insertList(){

        List<OrgPost> orgPostList = new ArrayList<>();

        OrgPost orgPost = new OrgPost();
        orgPost.setId(11);
        orgPost.setPostName("orgPost");
        orgPost.setParentId(0);
        orgPost.setSort(0);
        orgPostList.add(orgPost);

        OrgPost orgPost2 = new OrgPost();
        orgPost2.setId(12);
        orgPost.setPostName("orgPost2");
        orgPost.setParentId(0);
        orgPost.setSort(0);
        orgPostList.add(orgPost2);

        orgPostMapper.insertList(orgPostList);
    }

    @RequestMapping("/rm")
    public void rm(){
        orgPostMapper.deleteById(1);
    }

}
