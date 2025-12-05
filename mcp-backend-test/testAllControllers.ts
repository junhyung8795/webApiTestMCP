import axios, { AxiosInstance } from "axios";
import { CookieJar } from "tough-cookie";
import { wrapper } from "axios-cookiejar-support";
import * as fs from "fs";

const BACKEND_BASE_URL = "http://localhost:8080";
const OUTPUT_FILE = "./testAllControllers_result.json";

// 쿠키 관리용 CookieJar
const cookieJar = new CookieJar();

interface TestResult {
    step: number;
    name: string;
    method: string;
    url: string;
    success: boolean;
    status?: number;
    response?: any;
    error?: any;
}

let axiosInstance: AxiosInstance;
let loggedInMemberId: number | null = null;
const results: TestResult[] = [];

async function testAPI(
    step: number,
    name: string,
    method: string,
    url: string,
    data?: any
): Promise<TestResult> {
    const fullUrl = `${BACKEND_BASE_URL}${url}`;
    console.log(`\n[${step}] ${name}`);
    console.log(`  ${method} ${fullUrl}`);
    if (data !== undefined) {
        console.log(`  Request Body:`, JSON.stringify(data, null, 2));
    }

    try {
        let response;
        switch (method.toUpperCase()) {
            case "GET":
                response = await axiosInstance.get(fullUrl);
                break;
            case "POST":
                response = await axiosInstance.post(fullUrl, data);
                break;
            case "PUT":
                response = await axiosInstance.put(fullUrl, data);
                break;
            case "PATCH":
                response = await axiosInstance.patch(fullUrl, data);
                break;
            case "DELETE":
                response = await axiosInstance.delete(fullUrl);
                break;
            default:
                throw new Error(`Unknown method: ${method}`);
        }

        const result: TestResult = {
            step,
            name,
            method,
            url,
            success: response.status >= 200 && response.status < 300,
            status: response.status,
            response: response.data,
        };

        console.log(`  ✅ Status: ${response.status}`);
        console.log(`  Response:`, JSON.stringify(response.data, null, 2));
        results.push(result);
        return result;
    } catch (err: any) {
        const status = err.response?.status;
        const errorBody = err.response?.data ?? err.message;

        const result: TestResult = {
            step,
            name,
            method,
            url,
            success: false,
            status,
            error: errorBody,
        };
        console.log(`  ❌ Status: ${status ?? "N/A"}`);
        console.log(`  Error:`, JSON.stringify(errorBody, null, 2));
        results.push(result);
        return result;
    }
}

async function main() {
    console.log("=== testAllControllers.ts 시작 ===");
    console.log(`백엔드: ${BACKEND_BASE_URL}`);

    axiosInstance = wrapper(
        axios.create({
            baseURL: BACKEND_BASE_URL,
            withCredentials: true,
            jar: cookieJar,
            headers: {
                "Content-Type": "application/json",
            },
        })
    );

    let step = 1;

    try {
        // 1. 회원 존재 여부 확인 (로그인 시도)
        const loginCheck = await testAPI(
            step++,
            '로그인 시도 (회원 존재 확인)',
            "POST",
            "/api/auth/login",
            { id: "1", password: "1234" }
        );

        if (!loginCheck.success) {
            // 2. 회원가입 시도 (id=1, password=1234)
            const signup = await testAPI(
                step++,
                "회원가입 (id=1, password=1234)",
                "POST",
                "/api/member",
                {
                    id: "1",
                    password: "1234",
                    nickname: "테스트유저1",
                    email: "test1@test.com",
                }
            );

            if (!signup.success) {
                console.log("회원가입 실패, 이후 테스트는 로그인 실패 상태에서 진행될 수 있습니다.");
            }

            // 3. 다시 로그인
            const loginAgain = await testAPI(
                step++,
                "로그인 재시도",
                "POST",
                "/api/auth/login",
                { id: "1", password: "1234" }
            );

            const apiResp = loginAgain.response;
            const memberId =
                apiResp?.data?.memberId ??
                apiResp?.memberId ??
                loginAgain.response?.memberId;
            if (loginAgain.success && typeof memberId === "number") {
                loggedInMemberId = memberId;
            }
        } else {
            const apiResp = loginCheck.response;
            const memberId =
                apiResp?.data?.memberId ??
                apiResp?.memberId ??
                loginCheck.response?.memberId;
            if (typeof memberId === "number") {
                loggedInMemberId = memberId;
            }
        }

        console.log(`로그인된 memberId: ${loggedInMemberId ?? "알 수 없음"}`);

        // === MemberController 관련 엔드포인트 테스트 ===
        await testAPI(step++, "아이디 중복 체크", "POST", "/api/check-id", {
            value: "1",
        });
        await testAPI(step++, "이메일 중복 체크", "POST", "/api/check-email", {
            value: "test1@test.com",
        });
        await testAPI(
            step++,
            "닉네임 중복 체크",
            "POST",
            "/api/check-nickname",
            { value: "테스트유저1" }
        );
        await testAPI(step++, "회원 정보 조회 (하드코딩 memberId=1)", "GET", "/api/members/");

        // updateMe, deleteMember, updateMember 는 현재 하드코딩된 memberId=1에 대해
        // 비즈니스 로직적으로 위험할 수 있어 여기서는 생략하거나 마지막에만 시도하는 것이 안전합니다.

        // === UserProblemSetController ===
        const createSet = await testAPI(
            step++,
            "유저 문제세트 생성",
            "POST",
            "/api/user-problem-sets"
        );

        let userProblemSetId: number | null = null;

        const mySet = await testAPI(
            step++,
            "내 유저 문제세트 조회",
            "GET",
            "/api/user-problem-sets/me"
        );
        if (mySet.success && mySet.response?.data?.userProblemSetId) {
            userProblemSetId = mySet.response.data.userProblemSetId;
        }

        await testAPI(
            step++,
            "유저 문제세트 전체 조회",
            "GET",
            "/api/user-problem-sets"
        );

        // === UserProblemController ===
        let userProblemIds: number[] = [];
        if (userProblemSetId) {
            await testAPI(
                step++,
                "유저 문제 일괄 등록",
                "POST",
                `/api/user-problems/sets/${userProblemSetId}`,
                [
                    {
                        problemDescription: "컨트롤러 전체 테스트 문제 1",
                        category: "INFOENGINEERING",
                        choice1: "1",
                        choice2: "2",
                        choice3: "3",
                        choice4: "4",
                        answer: "1",
                    },
                    {
                        problemDescription: "컨트롤러 전체 테스트 문제 2",
                        category: "INFOENGINEERING",
                        choice1: "1",
                        choice2: "2",
                        choice3: "3",
                        choice4: "4",
                        answer: "2",
                    },
                ]
            );

            const problems = await testAPI(
                step++,
                "유저 문제 목록 조회",
                "GET",
                `/api/user-problems/sets/${userProblemSetId}`
            );
            if (problems.success && Array.isArray(problems.response?.data)) {
                userProblemIds = problems.response.data
                    .map((p: any) => p.userProblemId)
                    .filter((id: any) => typeof id === "number");
            }
        }

        if (userProblemIds.length > 0) {
            await testAPI(
                step++,
                "유저 문제 수정",
                "PUT",
                `/api/user-problems/${userProblemIds[0]}`,
                {
                    problemDescription: "수정된 컨트롤러 테스트 문제",
                    category: "INFOENGINEERING",
                    choice1: "A",
                    choice2: "B",
                    choice3: "C",
                    choice4: "D",
                    answer: "2",
                }
            );
        }

        // === CommentController ===
        let commentIds: number[] = [];
        if (userProblemSetId) {
            const commentsBefore = await testAPI(
                step++,
                "댓글 조회 (초기)",
                "GET",
                `/api/comments/${userProblemSetId}`
            );
            if (commentsBefore.success && Array.isArray(commentsBefore.response?.data)) {
                commentIds = commentsBefore.response.data
                    .map((c: any) => c.commentId)
                    .filter((id: any) => typeof id === "number");
            }

            for (let i = 1; i <= 2; i++) {
                await testAPI(
                    step++,
                    `댓글 작성 ${i}`,
                    "POST",
                    `/api/comments/${userProblemSetId}`,
                    { content: `컨트롤러 전체 테스트 댓글 ${i}` }
                );
            }

            const commentsAfter = await testAPI(
                step++,
                "댓글 재조회",
                "GET",
                `/api/comments/${userProblemSetId}`
            );
            if (commentsAfter.success && Array.isArray(commentsAfter.response?.data)) {
                commentIds = commentsAfter.response.data
                    .map((c: any) => c.commentId)
                    .filter((id: any) => typeof id === "number");
            }

            if (commentIds.length > 0) {
                await testAPI(
                    step++,
                    "댓글 수정",
                    "PATCH",
                    `/api/comments/${userProblemSetId}/${commentIds[0]}`,
                    { content: "수정된 컨트롤러 테스트 댓글" }
                );
            }

            if (commentIds.length > 1) {
                await testAPI(
                    step++,
                    "댓글 삭제",
                    "DELETE",
                    `/api/comments/${userProblemSetId}/${commentIds[1]}`
                );
            }
        }

        // === UserScoreController ===
        await testAPI(
            step++,
            "점수 등록",
            "POST",
            "/api/scores",
            { score: 100 }
        );
        await testAPI(
            step++,
            "전체 점수 조회",
            "GET",
            "/api/scores"
        );
        if (loggedInMemberId) {
            await testAPI(
                step++,
                "특정 회원 점수 조회",
                "GET",
                `/api/scores/${loggedInMemberId}`
            );
        }
        await testAPI(
            step++,
            "점수 수정",
            "PUT",
            "/api/scores",
            { score: 150 }
        );

        // === ProblemController ===
        await testAPI(
            step++,
            "기본 문제 조회 (limit=10, category=INFOENGINEERING)",
            "GET",
            "/api/problem?limit=10&category=INFOENGINEERING"
        );

        // === IncorrectNoteController ===
        let incorrectNoteId: number | null = null;
        if (userProblemIds.length > 0) {
            const addNote = await testAPI(
                step++,
                "오답노트에 유저제작 문제 추가",
                "POST",
                "/api/incorrect-note",
                {
                    problemId: null,
                    userProblemId: userProblemIds[0],
                    isUserProblem: true,
                }
            );
            if (addNote.success && addNote.response?.data) {
                incorrectNoteId = addNote.response.data;
            }
        }

        await testAPI(
            step++,
            "오답노트 전체 조회",
            "GET",
            "/api/incorrect-note"
        );

        if (incorrectNoteId) {
            await testAPI(
                step++,
                "오답노트 항목 삭제",
                "DELETE",
                `/api/incorrect-note/${incorrectNoteId}`
            );
        }

        // === QuizRoomController ===
        await testAPI(
            step++,
            "퀴즈방 목록 조회",
            "GET",
            "/api/quiz-room"
        );

        let quizRoomId: number | null = null;
        if (loggedInMemberId) {
            const createRoom = await testAPI(
                step++,
                "퀴즈방 생성 (방장)",
                "POST",
                `/api/quiz-room/create/${loggedInMemberId}`
            );
            if (createRoom.success && createRoom.response?.data) {
                quizRoomId = createRoom.response.data;
            }
        }

        if (quizRoomId) {
            await testAPI(
                step++,
                "퀴즈방 멤버 조회",
                "GET",
                `/api/quiz-room/${quizRoomId}/member`
            );

            await testAPI(
                step++,
                "퀴즈방 삭제",
                "DELETE",
                `/api/quiz-room/${quizRoomId}`
            );
        }

        // 로그아웃
        await testAPI(
            step++,
            "로그아웃",
            "POST",
            "/api/auth/logout"
        );

        // 결과 요약
        const total = results.length;
        const successCount = results.filter(r => r.success).length;
        const failCount = total - successCount;
        const successRate = total === 0 ? 0 : (successCount / total) * 100;

        console.log("\n========================================");
        console.log("전체 컨트롤러 테스트 결과 요약");
        console.log("========================================");
        console.log(`총 테스트 수: ${total}`);
        console.log(`성공: ${successCount}`);
        console.log(`실패: ${failCount}`);
        console.log(`성공률: ${successRate.toFixed(1)}%`);

        console.log("\n✅ 성공한 테스트 목록:");
        results
            .filter(r => r.success)
            .forEach(r => {
                console.log(
                    `  [${r.step}] ${r.name} - ${r.method} ${r.url} (${r.status})`
                );
            });

        console.log("\n❌ 실패한 테스트 상세:");
        const failed = results.filter(r => !r.success);
        if (failed.length === 0) {
            console.log("  없음 (모든 테스트 성공)");
        } else {
            failed.forEach(r => {
                console.log(
                    `  [${r.step}] ${r.name} - ${r.method} ${r.url} (${r.status ?? "N/A"})`
                );
                console.log(`     Error: ${JSON.stringify(r.error, null, 2)}`);
            });
        }

        fs.writeFileSync(
            OUTPUT_FILE,
            JSON.stringify(
                {
                    total,
                    success: successCount,
                    failed: failCount,
                    successRate: `${successRate.toFixed(1)}%`,
                    results: results.map(r => ({
                        step: r.step,
                        name: r.name,
                        method: r.method,
                        url: r.url,
                        success: r.success,
                        status: r.status,
                    })),
                    failedDetails: failed.map(r => ({
                        step: r.step,
                        name: r.name,
                        method: r.method,
                        url: r.url,
                        status: r.status,
                        error: r.error,
                    })),
                },
                null,
                2
            )
        );
        console.log(`\n📄 결과가 ${OUTPUT_FILE} 파일로 저장되었습니다.`);
    } catch (e: any) {
        console.error("테스트 실행 중 예외 발생:", e.message);
        fs.writeFileSync(
            OUTPUT_FILE,
            JSON.stringify({ fatalError: e.message, stack: e.stack }, null, 2)
        );
        process.exit(1);
    }
}

main().catch(e => {
    console.error("치명적인 에러:", e);
    fs.writeFileSync(
        OUTPUT_FILE,
        JSON.stringify({ fatalError: e.message, stack: e.stack }, null, 2)
    );
    process.exit(1);
});


