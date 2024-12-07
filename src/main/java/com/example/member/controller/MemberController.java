package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller // spring MVC에서 컨트롤러. 클라이언트 요청 처리 및 적절한 뷰로 반환 및 데이터 응담
@RequiredArgsConstructor // final 또는 @NonNull로 선언된 필드의 생성자 자동 생성(memberService)
public class MemberController {

    /*
    spring container가 membercontoller 생성
    membercontroller class에 선언된 final MemberService memberService가 생성자를 통해 주입
    memberService는 MemberContoller 내에서 비즈니스 로직 처리하는데 사용(컨트롤러가 직접 비즈니스 로직을 처리하지 않고, 서비스 계층에 로직을 위임)
     */

    // 생성자 주입
    private final MemberService memberService;

    /*
    // Lombok이 생성하는 생성자
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
     */

    // 회원가입 페이지 출력 요청
    @GetMapping("/member/save") // /member/save에 대한 GET 요청 시 메서드 매핑
    // 회원 가입 폼 페이지 보여줄 때
    public String saveForm() {
        return "save"; // save.html 요청
    }

    /*
    @PostMapping("/member/save") // POST로 /member/save URL에 접근할 때 실행
    // 회원 가입 폼에서 데이터를 제출할 때
    // @RequestParam: HTTP 요청 파라미터를 메서드 매개변수(String)로 전달받음
    public String save(@RequestParam("memberEmail") String memberEmail,
                       @RequestParam("memberPassword") String memberPassword,
                       @RequestParam("memberName") String memberName) {
        System.out.println("MemberController.save");
        System.out.println("memberEmail = " + memberEmail + ", memberPassword = " + memberPassword + ", memberName = " + memberName);
        return "index";
    }
     */

    // @RequestParam 대신 @ModelAttribute MemberDTO를 사용하면 더 간단
    @PostMapping("/member/save")
    // @ModelAttribute: 클라이언트로부터 전송된 요청 데이터를 MemberDTO 객체의 필드와 자동으로 매핑
    // 'MemberDTO 클래스에 있는 private 객체'와 'save.html의 name'이 같아야 매핑
    public String save(@ModelAttribute MemberDTO memberDTO) {
        System.out.println("MemberController.save");
        System.out.println("memberDTO = " + memberDTO);
        memberService.save(memberDTO); // MemberService class에서 save 메서드 생성
        return "login";
    }

    @GetMapping("/member/login") // 이 주소 요청 시 login 페이지 보여줌
    public String loginForm() {
        return "login";
    }

    @PostMapping("/member/login")
    // 요청 데이터를 DTO 객체의 필드와 자동으로 매핑
    // 현재 사용자의 세션 객체를 받아와 로그인 상태 정보 저장
    public String login(@ModelAttribute MemberDTO memberDTO, HttpSession session) {
        MemberDTO loginResult = memberService.login(memberDTO);
        if (loginResult != null) {
            // 로그인 성공
            // 사용자의 이메일 정보를 loginEmail 세션에 저장. 이후 사용자 식별이나 권한 검증에 활용
            session.setAttribute("loginEmail", loginResult.getMemberEmail());
            return "main"; // main.html
        } else {
            // 로그인 실패
            return "login"; // login.html
        }
    }
}
