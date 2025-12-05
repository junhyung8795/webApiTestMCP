package com.codeboy.mvc.model.service;

import com.codeboy.mvc.model.dao.MemberDao;
import com.codeboy.mvc.model.dao.UserScoreDao;
import com.codeboy.mvc.model.dto.Member;
import com.codeboy.mvc.model.dto.UserScore;
import com.codeboy.mvc.model.dto.request.MemberUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;
    
    @Autowired 
    UserScoreDao userScoreDao;
    
    //회원 정보 가져오기기
    public Member getMemberById(Long memberId) {
        isMemberExist(memberId);

        Member member = memberDao.selectMemberById(memberId);
        if (member == null) {
            throw new NoSuchElementException("Id에 해당하는 회원 정보를 찾을 수 없습니다. memberId:" + memberId);
        }
        return member;
    };

    //회원 탈퇴
    public void deactivateMember(Long memberId){
        isMemberExist(memberId);

        int updatedRows = memberDao.deactivateMemberById(memberId);
        if (updatedRows == 0) {
            throw new IllegalStateException("회원 탈퇴 실패 : memberId: " + memberId);
        }
    }


    public void updateMember(Long memberId, MemberUpdateRequest memberUpdateRequest){
        isMemberExist(memberId);
        //중복된 id, email이 있는지 검증 로직
        String id = memberUpdateRequest.getId();
        String nickname = memberUpdateRequest.getNickname();
        String email = memberUpdateRequest.getEmail();
        validateMemberUpdate(id, nickname, email);

        int affectedRows = memberDao.updateMemberById(memberId, memberUpdateRequest);

        if (affectedRows == 0) {
            throw new IllegalStateException("회원 정보 수정에 실패하였습니다. memberId: " + memberId);
        }

    };
    public boolean checkIdDuplicate(String id) {
        if (id == null) {
            throw new IllegalArgumentException("유효하지 않은 Id 입니다.");
        }
        return  memberDao.existsId(id);
    }

    public boolean checkNicknameDuplicate(String nickname ) {
        if (nickname == null) {
            throw new IllegalArgumentException("유효하지 않은 Id 입니다.");
        }
        return  memberDao.existsNickname(nickname);
    }

    public boolean checkEmailDuplicate(String email) {
        if (email == null) {
            throw new IllegalArgumentException("유효하지 않은 Id 입니다.");
        }
        return  memberDao.existsEmail(email);
    }

    @Override
    public int signUp(Member member) {
    	UserScore userScore = new UserScore(member.getMemberId(), 0);
    	userScoreDao.insertScore(userScore);
        return memberDao.insertMember(member);
    }

    public void isMemberExist(Long memberId) {
        if (memberId == null) {
            throw  new IllegalArgumentException("유효하지 않은 memberId입니다. memberId: " );
        }

        boolean isActive = memberDao.isMemberActive(memberId);
        if (!isActive) {
            throw new IllegalArgumentException("비활성화된 멤버 id 입니다. memberId: " + memberId);
        }
    }

    // 최종 제출 시 전체 검증
    public void validateMemberUpdate(String id, String nickname, String email) {
        if (memberDao.existsId(id)) {
            throw new IllegalArgumentException("중복된 ID입니다.");
        }
        if (memberDao.existsNickname(nickname)) {
            throw new IllegalArgumentException("중복된 닉네임입니다.");
        }
        if (memberDao.existsEmail(email)) {
            throw new IllegalArgumentException("중복된 이메일입니다.");
        }
    }
    
    // 로그인
    public Member login(String id, String password) {
        if (id == null || password == null) {
            throw new IllegalArgumentException("ID와 비밀번호를 입력해주세요.");
        }
        
        Member member = memberDao.selectMemberByIdAndPassword(id, password);
        if (member == null) {
            throw new IllegalArgumentException("ID 또는 비밀번호가 올바르지 않습니다.");
        }
        
        return member;
    }
 

};