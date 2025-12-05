# 전체 컨트롤러 API 테스트 결과

**테스트 일시:** 2025. 12. 5. 오후 3:13:13

## 요약

- 총 테스트 수: 19
- 성공: 19
- 실패: 0
- 성공률: 100.0%

## 성공한 테스트

### [1] 로그인 시도 (회원 존재 확인)
- **Method:** POST
- **URL:** /api/auth/login
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "로그인 성공",
  "data": {
    "nickname": "asdasd",
    "id": "1",
    "memberId": 1
  }
}
```

### [2] 아이디 중복 체크
- **Method:** POST
- **URL:** /api/check-id
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "아이디 중복 확인 완료",
  "data": {
    "duplicated": true
  }
}
```

### [3] 이메일 중복 체크
- **Method:** POST
- **URL:** /api/check-email
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "이메일 중복 확인 완료",
  "data": {
    "duplicated": false
  }
}
```

### [4] 닉네임 중복 체크
- **Method:** POST
- **URL:** /api/check-nickname
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "닉네임 중복 확인 완료",
  "data": {
    "duplicated": false
  }
}
```

### [5] 회원 정보 조회 (하드코딩 memberId=1)
- **Method:** GET
- **URL:** /api/members/
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "회원정보가 조회되었습니다",
  "data": {
    "memberId": 1,
    "id": "1",
    "password": "1234",
    "nickname": "asdasd",
    "email": "junhyung8795@naver.com",
    "signupDate": "2025-12-04T14:53:15.000+00:00",
    "isActive": true,
    "deletedDate": "2025-12-04T14:53:15.000+00:00"
  }
}
```

### [6] 유저 문제세트 생성
- **Method:** POST
- **URL:** /api/user-problem-sets
- **Status:** 201
- **Response:**
```json
{
  "status": "CREATED",
  "message": "유저 문제세트 생성 성공",
  "data": {
    "userProblemSetId": 46,
    "memberId": 1
  }
}
```

### [7] 내 유저 문제세트 조회
- **Method:** GET
- **URL:** /api/user-problem-sets/me
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "나의 유저 문제세트 조회 성공",
  "data": [
    {
      "userProblemSetId": 1,
      "memberId": 1
    },
    {
      "userProblemSetId": 2,
      "memberId": 1
    },
    {
      "userProblemSetId": 3,
      "memberId": 1
    },
    {
      "userProblemSetId": 4,
      "memberId": 1
    },
    {
      "userProblemSetId": 5,
      "memberId": 1
    },
    {
      "userProblemSetId": 6,
      "memberId": 1
    },
    {
      "userProblemSetId": 7,
      "memberId": 1
    },
    {
      "userProblemSetId": 8,
      "memberId": 1
    },
    {
      "userProblemSetId": 9,
      "memberId": 1
    },
    {
      "userProblemSetId": 10,
      "memberId": 1
    },
    {
      "userProblemSetId": 11,
      "memberId": 1
    },
    {
      "userProblemSetId": 12,
      "memberId": 1
    },
    {
      "userProblemSetId": 13,
      "memberId": 1
    },
    {
      "userProblemSetId": 14,
      "memberId": 1
    },
    {
      "userProblemSetId": 15,
      "memberId": 1
    },
    {
      "userProblemSetId": 16,
      "memberId": 1
    },
    {
      "userProblemSetId": 17,
      "memberId": 1
    },
    {
      "userProblemSetId": 18,
      "memberId": 1
    },
    {
      "userProblemSetId": 19,
      "memberId": 1
    },
    {
      "userProblemSetId": 20,
      "memberId": 1
    },
    {
      "userProblemSetId": 21,
      "memberId": 1
    },
    {
      "userProblemSetId": 22,
      "memberId": 1
    },
    {
      "userProblemSetId": 23,
      "memberId": 1
    },
    {
      "userProblemSetId": 24,
      "memberId": 1
    },
    {
      "userProblemSetId": 25,
      "memberId": 1
    },
    {
      "userProblemSetId": 26,
      "memberId": 1
    },
    {
      "userProblemSetId": 27,
      "memberId": 1
    },
    {
      "userProblemSetId": 28,
      "memberId": 1
    },
    {
      "userProblemSetId": 29,
      "memberId": 1
    },
    {
      "userProblemSetId": 30,
      "memberId": 1
    },
    {
      "userProblemSetId": 31,
      "memberId": 1
    },
    {
      "userProblemSetId": 32,
      "memberId": 1
    },
    {
      "userProblemSetId": 33,
      "memberId": 1
    },
    {
      "userProblemSetId": 34,
      "memberId": 1
    },
    {
      "userProblemSetId": 35,
      "memberId": 1
    },
    {
      "userProblemSetId": 36,
      "memberId": 1
    },
    {
      "userProblemSetId": 37,
      "memberId": 1
    },
    {
      "userProblemSetId": 38,
      "memberId": 1
    },
    {
      "userProblemSetId": 39,
      "memberId": 1
    },
    {
      "userProblemSetId": 40,
      "memberId": 1
    },
    {
      "userProblemSetId": 41,
      "memberId": 1
    },
    {
      "userProblemSetId": 42,
      "memberId": 1
    },
    {
      "userProblemSetId": 43,
      "memberId": 1
    },
    {
      "userProblemSetId": 44,
      "memberId": 1
    },
    {
      "userProblemSetId": 45,
      "memberId": 1
    },
    {
      "userProblemSetId": 46,
      "memberId": 1
    }
  ]
}
```

### [8] 유저 문제세트 전체 조회
- **Method:** GET
- **URL:** /api/user-problem-sets
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "유저 문제세트 전체 조회 성공",
  "data": [
    {
      "userProblemSetId": 1,
      "memberId": 1
    },
    {
      "userProblemSetId": 2,
      "memberId": 1
    },
    {
      "userProblemSetId": 3,
      "memberId": 1
    },
    {
      "userProblemSetId": 4,
      "memberId": 1
    },
    {
      "userProblemSetId": 5,
      "memberId": 1
    },
    {
      "userProblemSetId": 6,
      "memberId": 1
    },
    {
      "userProblemSetId": 7,
      "memberId": 1
    },
    {
      "userProblemSetId": 8,
      "memberId": 1
    },
    {
      "userProblemSetId": 9,
      "memberId": 1
    },
    {
      "userProblemSetId": 10,
      "memberId": 1
    },
    {
      "userProblemSetId": 11,
      "memberId": 1
    },
    {
      "userProblemSetId": 12,
      "memberId": 1
    },
    {
      "userProblemSetId": 13,
      "memberId": 1
    },
    {
      "userProblemSetId": 14,
      "memberId": 1
    },
    {
      "userProblemSetId": 15,
      "memberId": 1
    },
    {
      "userProblemSetId": 16,
      "memberId": 1
    },
    {
      "userProblemSetId": 17,
      "memberId": 1
    },
    {
      "userProblemSetId": 18,
      "memberId": 1
    },
    {
      "userProblemSetId": 19,
      "memberId": 1
    },
    {
      "userProblemSetId": 20,
      "memberId": 1
    },
    {
      "userProblemSetId": 21,
      "memberId": 1
    },
    {
      "userProblemSetId": 22,
      "memberId": 1
    },
    {
      "userProblemSetId": 23,
      "memberId": 1
    },
    {
      "userProblemSetId": 24,
      "memberId": 1
    },
    {
      "userProblemSetId": 25,
      "memberId": 1
    },
    {
      "userProblemSetId": 26,
      "memberId": 1
    },
    {
      "userProblemSetId": 27,
      "memberId": 1
    },
    {
      "userProblemSetId": 28,
      "memberId": 1
    },
    {
      "userProblemSetId": 29,
      "memberId": 1
    },
    {
      "userProblemSetId": 30,
      "memberId": 1
    },
    {
      "userProblemSetId": 31,
      "memberId": 1
    },
    {
      "userProblemSetId": 32,
      "memberId": 1
    },
    {
      "userProblemSetId": 33,
      "memberId": 1
    },
    {
      "userProblemSetId": 34,
      "memberId": 1
    },
    {
      "userProblemSetId": 35,
      "memberId": 1
    },
    {
      "userProblemSetId": 36,
      "memberId": 1
    },
    {
      "userProblemSetId": 37,
      "memberId": 1
    },
    {
      "userProblemSetId": 38,
      "memberId": 1
    },
    {
      "userProblemSetId": 39,
      "memberId": 1
    },
    {
      "userProblemSetId": 40,
      "memberId": 1
    },
    {
      "userProblemSetId": 41,
      "memberId": 1
    },
    {
      "userProblemSetId": 42,
      "memberId": 1
    },
    {
      "userProblemSetId": 43,
      "memberId": 1
    },
    {
      "userProblemSetId": 44,
      "memberId": 1
    },
    {
      "userProblemSetId": 45,
      "memberId": 1
    },
    {
      "userProblemSetId": 46,
      "memberId": 1
    }
  ]
}
```

### [9] 점수 등록
- **Method:** POST
- **URL:** /api/scores
- **Status:** 201
- **Response:**
```json
{
  "status": "CREATED",
  "message": "점수 등록 성공",
  "data": null
}
```

### [10] 전체 점수 조회
- **Method:** GET
- **URL:** /api/scores
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "점수 목록 조회 성공",
  "data": [
    {
      "memberId": 1,
      "score": 100
    }
  ]
}
```

### [11] 특정 회원 점수 조회
- **Method:** GET
- **URL:** /api/scores/1
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "점수 조회 성공",
  "data": {
    "memberId": 1,
    "score": 100
  }
}
```

### [12] 점수 수정
- **Method:** PUT
- **URL:** /api/scores
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "점수 수정 성공",
  "data": null
}
```

### [13] 기본 문제 조회 (limit=10, category=INFOENGINEERING)
- **Method:** GET
- **URL:** /api/problem?limit=10&category=INFOENGINEERING
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "문제가 성공적으로 반환되었습니다.",
  "data": [
    {
      "problemId": 3,
      "problemDescription": "문제3",
      "choice1": "GET",
      "choice2": "POST",
      "choice3": "PUT",
      "choice4": "DELETE",
      "answer": "POST",
      "category": "INFOENGINEERING"
    }
  ]
}
```

### [14] 오답노트 전체 조회
- **Method:** GET
- **URL:** /api/incorrect-note
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "오답노트가 성공적으로 조회되었습니다",
  "data": [
    {
      "incorrectNoteId": 2,
      "problemDescription": "수정된 문제입니다!",
      "choice1": "수정1",
      "choice2": "수정2",
      "choice3": "수정3",
      "choice4": "수정4",
      "answer": "2",
      "category": "INFOENGINEERING"
    },
    {
      "incorrectNoteId": 4,
      "problemDescription": "수정된 문제입니다!",
      "choice1": "수정1",
      "choice2": "수정2",
      "choice3": "수정3",
      "choice4": "수정4",
      "answer": "2",
      "category": "INFOENGINEERING"
    },
    {
      "incorrectNoteId": 6,
      "problemDescription": "수정된 문제입니다!",
      "choice1": "수정1",
      "choice2": "수정2",
      "choice3": "수정3",
      "choice4": "수정4",
      "answer": "2",
      "category": "INFOENGINEERING"
    },
    {
      "incorrectNoteId": 8,
      "problemDescription": "수정된 문제입니다!",
      "choice1": "수정1",
      "choice2": "수정2",
      "choice3": "수정3",
      "choice4": "수정4",
      "answer": "2",
      "category": "INFOENGINEERING"
    },
    {
      "incorrectNoteId": 10,
      "problemDescription": "수정된 문제입니다!",
      "choice1": "수정1",
      "choice2": "수정2",
      "choice3": "수정3",
      "choice4": "수정4",
      "answer": "2",
      "category": "INFOENGINEERING"
    }
  ]
}
```

### [15] 퀴즈방 목록 조회
- **Method:** GET
- **URL:** /api/quiz-room
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "퀴즈방 목록 조회 성공",
  "data": [
    {
      "roomId": 1
    },
    {
      "roomId": 2
    },
    {
      "roomId": 3
    },
    {
      "roomId": 4
    },
    {
      "roomId": 5
    },
    {
      "roomId": 6
    },
    {
      "roomId": 7
    },
    {
      "roomId": 8
    },
    {
      "roomId": 9
    },
    {
      "roomId": 10
    },
    {
      "roomId": 11
    },
    {
      "roomId": 12
    },
    {
      "roomId": 13
    },
    {
      "roomId": 14
    },
    {
      "roomId": 15
    },
    {
      "roomId": 16
    },
    {
      "roomId": 17
    }
  ]
}
```

### [16] 퀴즈방 생성 (방장)
- **Method:** POST
- **URL:** /api/quiz-room/create/1
- **Status:** 201
- **Response:**
```json
{
  "status": "CREATED",
  "message": "퀴즈방이 성공적으로 생성되었습니다.",
  "data": 53
}
```

### [17] 퀴즈방 멤버 조회
- **Method:** GET
- **URL:** /api/quiz-room/53/member
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "멤버 리스트를 반환합니다.",
  "data": [
    {
      "memberId": 1,
      "nickname": "asdasd"
    }
  ]
}
```

### [18] 퀴즈방 삭제
- **Method:** DELETE
- **URL:** /api/quiz-room/53
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": null,
  "data": "채팅방이 성공적으로 삭제되었습니다."
}
```

### [19] 로그아웃
- **Method:** POST
- **URL:** /api/auth/logout
- **Status:** 200
- **Response:**
```json
{
  "status": "OK",
  "message": "로그아웃 성공",
  "data": null
}
```

## 실패한 테스트

없음 (모든 테스트 성공! 🎉)

