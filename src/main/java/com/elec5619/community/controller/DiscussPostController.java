package com.elec5619.community.controller;

import com.elec5619.community.entity.*;
import com.elec5619.community.service.CommentService;
import com.elec5619.community.service.DiscussPostService;
import com.elec5619.community.service.UserService;
import com.elec5619.community.util.*;
import com.elec5619.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static com.elec5619.community.util.CommunityConstant.ENTITY_TYPE_COMMENT;
import static com.elec5619.community.util.CommunityConstant.ENTITY_TYPE_POST;

/**
 * @Author: zpj
 * 2023/10/16
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController{

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    @PostMapping(path = "/add")
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtils.getJSONString(-1, "You haven't login.");
        }

        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        discussPostService.addDiscussPost(discussPost);

        return CommunityUtils.getJSONString(0, "Post successfully!");
    }

    @GetMapping("/detail/{discussPostId}")
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, String keyword, Model model, Page page) {
        DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
        int commentCount = commentService.findCommentCount(ENTITY_TYPE_POST, keyword, post.getId());
        post.setCommentCount(commentCount);
        model.addAttribute("post", post);
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/discuss/detail/" + discussPostId);
        page.setRows(post.getCommentCount());
        List<Comment> commentList = commentService.findCommentsByEntity(ENTITY_TYPE_POST, keyword, post.getId(), page.getOffset(), page.getLimit());
        List<Map<String, Object>> commentVOList = new ArrayList<>();
        if (commentList != null) {
            for (Comment comment : commentList) {
                Map<String, Object> commentVO = new HashMap<>();
                commentVO.put("comment", comment);
                commentVO.put("user", userService.findUserById(comment.getUserId()));

                List<Comment> replayList = commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT, keyword, comment.getId(), 0, Integer.MAX_VALUE);
                List<Map<String, Object>> replayVOList = new ArrayList<>();
                if (replayList != null){
                    for (Comment reply :replayList) {
                        Map<String, Object> replyVO = new HashMap<>();
                        replyVO.put("reply", reply);
                        replyVO.put("user", userService.findUserById(reply.getUserId()));
                        User target = reply.getTargetId() == 0 ? null : userService.findUserById(reply.getTargetId());
                        replyVO.put("target", target);

                        replayVOList.add(replyVO);
                    }
                }
                commentVO.put("replies", replayVOList);

                int replyCount = commentService.findCommentCount(ENTITY_TYPE_COMMENT, keyword, comment.getId());
                commentVO.put("replyCount", replyCount);

                commentVOList.add(commentVO);
            }
        }
        model.addAttribute("comments", commentVOList);

        return "/site/discuss-detail";
    }

}
