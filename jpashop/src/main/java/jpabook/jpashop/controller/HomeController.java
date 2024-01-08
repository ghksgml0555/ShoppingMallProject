package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller
@Slf4j
public class HomeController {

    private final MemberRepository memberRepository;


    //@GetMapping("/") 쿠키사용 홈화면
    //로그인 쿠키를 받아서 있으면 로그인된화면 없으면 기본화면(로그인을해야하는)
    public String homeLogin(@CookieValue(name ="memberId", required = false)Long memberId, Model model){
        //memberId 스트링이지만 Long으로 스프링이 변환
        if (memberId == null) {
            return "home";
        }

        //로그인성공사용자
        Member loginMember = memberRepository.findOne(memberId);
        if (loginMember == null) { //쿠키가 너무 옛날에 많들어 졌거나해서 db에 없을시 홈으로보내기
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "login/loginHome";

    }

    @GetMapping("/") //세션사용 홈화면
    //로그인 쿠키를 받아서 있으면 로그인된화면 없으면 기본화면(로그인을해야하는)
    public String homeLoginSession(HttpServletRequest request, Model model){
        //로그인하지 않은 처음들어온 사용자도 세션이 만들어지면 안되니까 false를 준다.
        HttpSession session = request.getSession(false);
        if(session==null){
            return "home";
        }

        Member loginMember = (Member)session.getAttribute("loginMember");

        //세션에 회원 데이터가 없으면 home
        if (loginMember == null) {
            return "home";
        }

        //세션이 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "login/loginHome";
    }


}
