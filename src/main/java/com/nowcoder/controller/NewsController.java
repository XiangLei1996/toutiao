package com.nowcoder.controller;

import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.QiniuService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nowcoder on 2016/7/2.
 */
@Controller
public class NewsController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    NewsService newsService;

    @Autowired
    QiniuService qiniuService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;


    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model){
        News news = newsService.getById(newsId);
        if(news != null){
            //评论  entityId对应该评论所属对象的id---类似 外键？？
            List<Comment> comments = commentService.getCommentsByEntity(news.getId(), EntityType.ENTITY_NEWS);
            List<ViewObject> commentVOs = new ArrayList<>();
            for(Comment comment : comments){
                ViewObject commentVo = new ViewObject();
                commentVo.set("comment", comment);
                commentVo.set("user", userService.getUser(comment.getUserId()));
                commentVOs.add(commentVo);
            }
            model.addAttribute("comments", commentVOs);
        }

        //返回视图中添加news对象 和  发表者名称
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        return "detail";
    }

    /**
     * 使用 POST 方法增加评论
     * @param newsId 评论所属的新闻对象的id
     * @param content 评论内容
     * @return 重定向到当前页面
     */
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComments(@RequestParam("newsId") int newsId,
                              @RequestParam("content") String content){
        try {
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setCreatedDate(new Date());
            //这里通过ThreadLocal获取User对象的Id ， 即发表评论的用户的id -- 可以加判断，为空则弹出登录框等
            comment.setUserId(hostHolder.getUsers().getId());
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            comment.setStatus(0);
            commentService.addComment(comment);

            //更新评论后，异步更新该新闻评论数量
            int count = commentService.getCommentCount(newsId, EntityType.ENTITY_NEWS);
            newsService.updateCommentCount(newsId, count);
            return "redirect:/news/" + newsId;
        } catch (Exception e){
            logger.error("增加评论失败" + e.getMessage());
            return ToutiaoUtil.getJsonString(1, "增加评论失败");
        }
    }

    /**
     * 读取图片   用于展示用户保存的图片
     * @param imageName 图片名
     * @param response 响应对象，用response.getOutputStream()来输出文件流
     *
     */
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName,
                         HttpServletResponse response) {
        try {
            response.setContentType("image/jpeg");
            StreamUtils.copy(new FileInputStream(new
                    File(ToutiaoUtil.IMAGE_DIR + imageName)), response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误" + imageName + e.getMessage());
        }
    }

    /**
     *
     * @param file 文件  注意  MultipartFile类，上传时用来保存文件的
     * @return
     */
    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String fileUrl = newsService.saveImage(file);
            //String fileUrl = qiniuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJsonString(1, "上传图片失败");
            }
            return ToutiaoUtil.getJsonString(0, fileUrl);
        } catch (Exception e) {
            logger.error("上传图片失败" + e.getMessage());
            return ToutiaoUtil.getJsonString(1, "上传失败");
        }
    }

    /**
     * 添加咨询
     * @param image 咨询的图片
     * @param title 标题
     * @param link 咨询链接
     * @return
     */
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {
        try {
            News news = new News();
            news.setCreatedDate(new Date());
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            if (hostHolder.getUsers() != null) {
                news.setUserId(hostHolder.getUsers().getId());
            } else {
                // 设置一个匿名用户
                news.setUserId(3);
            }
            newsService.addNews(news);
            return ToutiaoUtil.getJsonString(0);
        } catch (Exception e) {
            logger.error("添加资讯失败" + e.getMessage());
            return ToutiaoUtil.getJsonString(1, "发布失败");
        }
    }
}
