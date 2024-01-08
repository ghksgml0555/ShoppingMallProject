package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.LoginService;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    @GetMapping("/login")
    public String login(@ModelAttribute("loginForm") LoginForm loginForm){
        return "login/loginForm";
    }

    //@PostMapping("/login") 쿠키사용 로그인 >> 보안문제로 세션을 사용
    //쿠키 > 서블릿리스폰스로 전달해서 response 를 파라미터에 둔다.
    public String login(HttpServletResponse response, @Valid @ModelAttribute LoginForm loginForm, BindingResult result){
        if(result.hasErrors()){
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginMember == null){
            result.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리 TODO
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);
        return "redirect:/";
    }

    @PostMapping("/login")
    public String sessionLogin(HttpServletRequest request, @Valid @ModelAttribute LoginForm loginForm,
                               BindingResult result, @RequestParam(defaultValue = "/") String redirectURL){
        if(result.hasErrors()){
            return "login/loginForm";
        }
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if(loginMember == null){
            result.reject("loginFail","아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        //로그인 성공 처리 TODO
        //서블릿이 제공하는 HttpSession을 request의 getSession()으로 만든다.
        HttpSession session = request.getSession();
        //세션에 로그인 회원 정보를 보관한다
        session.setAttribute("loginMember", loginMember);
        return "redirect:"+redirectURL;
    }


    //@PostMapping("/logout") 쿠키사용로그아웃
    public String logout(HttpServletResponse response){
        expireCookie(response, "memberId");
        return "redirect:/";
    }

    @GetMapping("/logout") //세션사용로그아웃
    public String sessionLogout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session!=null){
            session.invalidate();
        }
        return "redirect:/";
    }

    private static void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
