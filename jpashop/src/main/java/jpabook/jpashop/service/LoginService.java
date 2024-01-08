package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(String loginId, String password){
        List<Member> members = memberRepository.findByLoginId(loginId);
        for(Member member : members){
            if(member.getPassword().equals(password)){
                return member;
            }else {
                return null;
            }
        }
        return null;
    }

}
