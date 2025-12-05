package com.codeboy.mvc.controller;

import com.codeboy.mvc.model.dto.*;
import com.codeboy.mvc.model.dto.request.JoinQuizRoomRequest;
import com.codeboy.mvc.model.dto.response.ApiResponse;
import com.codeboy.mvc.model.dto.response.GetQuizRoomMembersResponse;
import com.codeboy.mvc.model.service.QuizRoomService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quiz-room")
@Tag(name="Quiz-room RESTful API", description = "Quiz-room CRUD를 할 수 있는 REST API")
public class QuizRoomController {
    private final QuizRoomService quizRoomService;

    public QuizRoomController(QuizRoomService quizRoomService) {
        this.quizRoomService = quizRoomService;
    }

    //호스트 - 퀴즈방 만들기
    //TODO : 테스트용으로 memberId를 PathVariable로 넘김
    @PostMapping("/create/{memberId}")
    //TODO : 로그인 구현되면 memberId를 requestBody로 넘기지 말고 세션에서 가져오도록 하기
    public ResponseEntity<ApiResponse<Long>> createQuizRoom(@PathVariable long memberId) {
        try {
            //새로운 채팅방 생성하기
            long quizRoomId = quizRoomService.createQuizRoom();
            //3. QuizRoomMember 객체 생성
//            Long memberId = 1L;
            QuizRoomMember quizRoomMember = new QuizRoomMember();
            //4. setter로 객체 만들기
            quizRoomMember.setMemberId(memberId);
            quizRoomMember.setRoomId(quizRoomId);
            quizRoomMember.setIsHost(true);

            //5. 채팅방에 넣기
            quizRoomService.joinQuizRoom(quizRoomMember);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED, "퀴즈방이 성공적으로 생성되었습니다.", quizRoomId));

        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    //채팅방 참여하기
    @PostMapping("/join")
    //TODO : 로그인 구현되면 memberId를 requestBody로 넘기지 말고 세션에서 가져오도록 하기
    public ResponseEntity<ApiResponse<String>> joinQuizRoom(@RequestBody JoinQuizRoomRequest request) {
        long memberId = request.getMemberId();
        long roomId = request.getRoomId();
        try {
            //QuizRoomMember 생성
            QuizRoomMember quizRoomMember = new QuizRoomMember();
            quizRoomMember.setIsHost(false);
            quizRoomMember.setMemberId(memberId);
            quizRoomMember.setRoomId(roomId);

            //채팅방에 참가
            quizRoomService.joinQuizRoom(quizRoomMember);
            return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED, "퀴즈방 입장에 성공하였습니다."));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    //채팅방 목록 보여주기
    @GetMapping
    public ResponseEntity<ApiResponse<List<QuizRoom>>> getQuizRoomList() {
        try {
        List<QuizRoom> quizRooms = quizRoomService.getQuizRoomList();
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "퀴즈방 목록 조회 성공", quizRooms));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }


    //현재 참가자 목록 보여주기
    @GetMapping("/{roomId}/member")
    public ResponseEntity<ApiResponse<List<GetQuizRoomMembersResponse>>> getQuizRoomMembers(@PathVariable long roomId) {
        try {
        List<GetQuizRoomMembersResponse> memberList = quizRoomService.getOneQuizRoomMember(roomId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "멤버 리스트를 반환합니다.", memberList));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }

    //채팅방 삭제하기
    @DeleteMapping("/{roomId}")
    public ResponseEntity<ApiResponse<String>> deleteQuizRoom(@PathVariable long roomId) {
        try {
            quizRoomService.deleteQuizRoom(roomId);
            return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(HttpStatus.OK, "채팅방이 성공적으로 삭제되었습니다."));

        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage()));
        }
    }
}