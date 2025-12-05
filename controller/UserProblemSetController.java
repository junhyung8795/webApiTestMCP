package com.codeboy.mvc.controller;

import com.codeboy.mvc.model.dto.UserProblemSet;
import com.codeboy.mvc.model.dto.response.ApiResponse; // 실제 패키지에 맞게 수정
import com.codeboy.mvc.model.service.UserProblemSetService;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-problem-sets")
public class UserProblemSetController {


    private final UserProblemSetService userProblemSetService;

    public UserProblemSetController(UserProblemSetService userProblemSetService) {
        this.userProblemSetService = userProblemSetService;
    }

    // 모든 문제 세트 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProblemSet>>> getAllUserProblemSets() {
        try {
            List<UserProblemSet> sets = userProblemSetService.getAllUserProblemSets();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(HttpStatus.OK, "유저 문제세트 전체 조회 성공", sets));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류로 문제세트를 조회하지 못했습니다."));
        }
    }

    // 마이페이지 - 내가 만든 문제세트 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserProblemSet>> getMyUserProblemSet(HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        }

        try {
            UserProblemSet set = userProblemSetService.getUserProblemSetByMemberId(memberId);

            if (set == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.failure(HttpStatus.NOT_FOUND, "등록된 유저 문제세트가 없습니다."));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(HttpStatus.OK, "나의 유저 문제세트 조회 성공", set));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류로 나의 문제세트를 조회하지 못했습니다."));
        }
    }

    // 마이페이지 - 문제세트 생성
    @PostMapping
    public ResponseEntity<ApiResponse<?>> createMyUserProblemSet(HttpSession session) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        }

        try {
            //문제 세트가 생성되기전에는 PK가 존재하질 않아서
            //문제세트를 등록함과 동시에 이 PK값을 UserProblemController에도 넘겨줘야함.
            //따라서 UserProblemSet 객체를 생성하여 memberId를 세팅해서 넘겨줌,
            //프론트에서 이를 받아서 다시 문제 하나하나를 user_problem테이블에 저장 할 때 이 userProblemSetId를 같이 넘겨줘야함.
            //이 부분은 프론트와 백엔드간의 협의가 필요.
            //UserProblemSetMapper에도 generateKey를 추가해서 PK값을 set에 담을 수 있었다.
            UserProblemSet set = new UserProblemSet();
            set.setMemberId(memberId);
            System.out.println(set);

            int result = userProblemSetService.createUserProblemSet(set);
            if (result == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(ApiResponse.failure(HttpStatus.BAD_REQUEST, "유저 문제세트 생성에 실패했습니다."));
            }

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(HttpStatus.CREATED, "유저 문제세트 생성 성공", set));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류로 문제세트를 생성하지 못했습니다."));
        }
    }

    // 마이페이지 - 문제세트 삭제
    @DeleteMapping("/{userProblemSetId}")
    public ResponseEntity<ApiResponse<Void>> deleteUserProblemSet(
            @PathVariable Long userProblemSetId,
            HttpSession session
    ) {
        Long memberId = (Long) session.getAttribute("memberId");
        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.failure(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."));
        }

        // TODO: userProblemSetId가 memberId가 만든 세트인지 owner 체크 로직을 Service/Dao에 추가하면 더 안전함

        try {
            int result = userProblemSetService.deleteUserProblemSet(userProblemSetId);

            if (result == 0) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.failure(HttpStatus.NOT_FOUND, "삭제할 유저 문제세트가 존재하지 않습니다."));
            }

            return ResponseEntity.status(HttpStatus.OK)
                    .body(ApiResponse.success(HttpStatus.OK, "유저 문제세트 삭제 성공", null));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.failure(HttpStatus.BAD_REQUEST, e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.failure(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류로 문제세트를 삭제하지 못했습니다."));
        }
    }
}
