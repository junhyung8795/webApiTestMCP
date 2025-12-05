package com.codeboy.mvc.model.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codeboy.mvc.model.dao.UserScoreDao;
import com.codeboy.mvc.model.dto.UserScore;


@Service
public class UserScoreServiceImpl implements UserScoreService {

    private final UserScoreDao userScoreDao;

    public UserScoreServiceImpl(UserScoreDao userScoreDao) {
        this.userScoreDao = userScoreDao;
    }

    @Override
    public int registerScore(UserScore userScore) {
        // 1. 파라미터 유효성 검증 → IllegalArgumentException
        if (userScore == null) {
            throw new IllegalArgumentException("점수 정보가 비어 있습니다.");
        }
        if (userScore.getMemberId() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 회원 ID입니다.");
        }
        if (userScore.getScore() < 0) {
            throw new IllegalArgumentException("점수는 0 이상이어야 합니다.");
        }

        // 2. DAO 호출 → 삽입된 row 수 반환
        int result = userScoreDao.insertScore(userScore);
        // result가 0인지 아닌지는 컨트롤러에서 판단
        return result;
    }

    @Override
    public List<UserScore> getAllUserScores() {
        // 여기서는 그냥 DAO 결과를 그대로 반환하고
        // 목록이 비었는지 여부는 컨트롤러에서 판단함. 
        return userScoreDao.selectAllUserScores();
    }

    @Override
    public UserScore getUserScoreById(Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 회원 ID입니다.");
        }

        // 없으면 null 반환 → 컨트롤러에서 404 처리
        return userScoreDao.selectOneUserScore(memberId);
    }

    @Override
    public int updateUser(UserScore userScore) {
        if (userScore == null) {
            throw new IllegalArgumentException("점수 정보가 비어 있습니다.");
        }
        if (userScore.getMemberId() <= 0) {
            throw new IllegalArgumentException("유효하지 않은 회원 ID입니다.");
        }
        if (userScore.getScore() < 0) {
            throw new IllegalArgumentException("점수는 0 이상이어야 합니다.");
        }

        int result = userScoreDao.updateUserScore(userScore);
        return result;
    }
}