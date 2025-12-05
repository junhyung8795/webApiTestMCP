package com.codeboy.mvc.controller;

import java.util.ArrayList;
import java.util.List;

import com.codeboy.mvc.model.dto.request.CommentUpdateRequest;
import com.codeboy.mvc.model.dto.response.ApiResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codeboy.mvc.model.dto.Comment;
import com.codeboy.mvc.model.service.CommentService;

@RestController
@RequestMapping("/api/comments")
public class CommentController {
	private final CommentService commentService;


	@Autowired
	public CommentController(CommentService commentService) {
		this.commentService = commentService;
	}
	
	
	@GetMapping("{userProblemSetId}")
    //숫자가 아닌 값이 userProblemSetId에 오면 스프링이 컨트롤러에 도달하기전에 400BAD_REQUEST를 보내줌
	public ResponseEntity<ApiResponse<List<Comment>>> getAllCommentsById(@PathVariable("userProblemSetId") long userProblemSetId){
		List<Comment> comments = commentService.getAllCommentsById(userProblemSetId);
		
        if (comments.isEmpty()) {
            //댓글이 달리지 않은 경우
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(HttpStatus.OK, "댓글이 없습니다.", new ArrayList<Comment>()));
        }
		//댓글 조회에 성공한 경우
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK,  "댓글 조회 성공", comments));
		
	}

    @PostMapping("{userProblemSetId}")
    public ResponseEntity<ApiResponse<Void>> addComment(@PathVariable long userProblemSetId,
                                                        @RequestBody Comment comment, HttpSession session) {
        if (comment.getContent() == null || comment.getContent().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure(HttpStatus.BAD_REQUEST, "댓글 내용은 비어 있을 수 없습니다."));
        }
        Long memberId =  (Long) session.getAttribute("memberId");
        if (memberId == null) {
            // 인증 안 됨
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        }
        comment.setMemberId(memberId);
        int result = commentService.addComment(userProblemSetId, comment);

        if (result == 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure(HttpStatus.BAD_REQUEST, "잘못된 요청. 댓글 추가 실패"));
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "댓글 추가 성공", null));
    }


    //리소스의 일부(content)만 수정하므로 패치매핑
    @PatchMapping("{userProblemSetId}/{commentId}")
    public ResponseEntity<ApiResponse<Void>> updateComment(
            @PathVariable long userProblemSetId,
            @PathVariable long commentId,
            @RequestBody CommentUpdateRequest commentUpdateRequest,
            HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            // 인증 안 됨
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        }
        commentUpdateRequest.setMemberId(memberId);
        if (!memberId.equals(commentUpdateRequest.getMemberId())) {
            // 인가 실패
            // 로그인 중인 회원이 자신이 작성한 댓글이 아닌 것을 수정하려할 때
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.failure(HttpStatus.FORBIDDEN, "본인의 댓글만 수정할 수 있습니다."));
        }
    
        //댓글이 비어있을 때
        if (commentUpdateRequest.getContent() == null || commentUpdateRequest.getContent().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure(HttpStatus.BAD_REQUEST, "수정할 댓글 내용을 입력해주세요."));
        }

        Comment comment = new Comment();
        comment.setContent(commentUpdateRequest.getContent());

        int result = commentService.updateComment(commentId, comment);

        if (result == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.failure(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."));

        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, "댓글 수정 성공", null));
    }


    @DeleteMapping("{userProblemSetId}/{commentId}")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable long userProblemSetId,
                                                           @PathVariable long commentId,
                                                           HttpSession session) {

        Long memberId = (Long) session.getAttribute("memberId");

        // 로그인 안한 상태
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        }

        // DB에서 댓글 작성자 ID 조회
        Long ownerId = commentService.getCommentOwnerId(commentId);

        // 댓글ID가 존재하지 않는 경우
        if (ownerId == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.failure(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."));
        }

        // 권한 없음 (본인 댓글 아님)
        if (!memberId.equals(ownerId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.failure(HttpStatus.FORBIDDEN, "본인이 작성한 댓글만 삭제할 수 있습니다."));
        }

        // 삭제 실행
        int result = commentService.deleteComment(commentId);

        if (result == 0) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "댓글 삭제에 실패했습니다."));
        }

        // 삭제 실패 (DB 오류 등의 상황)

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, "댓글 삭제 성공", null));
    }


}
