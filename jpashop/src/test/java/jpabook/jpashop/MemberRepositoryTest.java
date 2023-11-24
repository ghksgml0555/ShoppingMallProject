package jpabook.jpashop;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class) //스프링 관련 테스트라고 junit에 알림
@SpringBootTest //스프링부트 테스트
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional //엔티티매니저를 통한 데이터 변경은 반드시 트랙잭션이 있어야함
    @Rollback(false)
    public void testMember() throws  Exception{
        //given
        Member member = new Member();
        //member.setUsername("memberA");
        
        //when
       // Long savedId = memberRepository.save(member);
        //Member findMember = memberRepository.find(savedId);

        //then
        //Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        //Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
       // Assertions.assertThat(findMember).isEqualTo(member); // ==비교를 해도 같다 같은 영속성 컨테스트안에서는 id가 같으면 같은 엔티티


    }

}