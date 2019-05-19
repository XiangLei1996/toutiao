package com.nowcoder.service;

import com.nowcoder.dao.NewsDAO;
import com.nowcoder.model.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by nowcoder on 2016/6/26.
 * 通过 @Service注解使service接口的实现类能在 @Controller注解的类中自动装配
 * ServiceImpl通过自动注入DAO层对象并进行相应操作，完成服务
 */
@Service
public class ToutiaoService {
    public String say() {
        return "This is from ToutiaoService";
    }

    @Autowired
    private NewsDAO newsDAO;

    //根据三个参数，获得最新的offset~offset+limit条新闻
    public List<News> getLatestNews(int userId, int offset, int limit) {
        return newsDAO.selectByUserIdAndOffset(userId, offset, limit);
    }
}
