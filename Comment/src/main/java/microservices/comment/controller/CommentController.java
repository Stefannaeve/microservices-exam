package microservices.comment.controller;

import microservices.comment.models.Comment;
import microservices.comment.service.CommentService;
import microservices.comment.utils.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
public class CommentController {

    CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService){
        this.commentService = commentService;
    }

    @GetMapping("/fetchAll")
    public List<Comment> fetchAll(){
        ApiResponse<List<Comment>> comments = commentService.fetchAll();

        for (int i = 0; i < comments.Value.get().size(); i++) {
            System.out.println(comments.Value.get().get(i).getText());
        }
        return comments.Value.get();
    }

    @PostMapping("/saveOneComment")
    public ApiResponse<Comment> saveOneComment(RequestBody Comment comment){
        return commentService.saveOneComment(comment);
    }
}
