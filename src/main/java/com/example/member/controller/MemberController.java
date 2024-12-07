package com.example.member.controller;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/member/")
    public String findAll(Model model) {
        // entity에서 dto로 변환 후, memberDTOList에 저장
        List<MemberDTO> memberDTOList = memberService.findAll(); // member가 여러 명이어서 list로
        // 어떠한 html로 가져갈 데이터가 있다면 model 사용. model에 list 추가하고 model 이름을 memberList로 함
        model.addAttribute("memberList", memberDTOList);
        return "list"; // list.html
    }

    @GetMapping("/member/{id}") // 이 경로 상에 있는 id 값을
    // @PathVariable을 통해 Long id에 가져옴
    // db에서 가져온 회원 정보를 화면에 표시하기 위해 model 필요
    public String findById(@PathVariable Long id, Model model) {
        MemberDTO memberDTO = memberService.findById(id); // 1명 id 반환
        model.addAttribute("member", memberDTO); // member라는 model에 id 값 저장
        return "detail"; // 해당 id에 대한 detail.html 표시
    }

    @GetMapping("/member/update")
    // 현재 사용자의 세션 객체를 받아와 로그인 상태나 사용자 식별 정보 가져옴
    public String updateForm(HttpSession session, Model model) {
        // 현재 로그인한 사용자의 이메일 정보를 세션에서 가쟈와 String으로 캐스팅
        String myEmail = (String)session.getAttribute("loginEmail");
        // myEmail을 이용해 사용자 정보를 db에서 조회하고 memberDTO 객체로 반환
        MemberDTO memberDTO = memberService.updateForm(myEmail);
        // updateMember라는 model에 memberDTO 저장
        model.addAttribute("updateMember", memberDTO);
        return "update"; // update.html
    }

    @PostMapping("/member/update")
    public String update(@ModelAttribute MemberDTO memberDTO) {
        // 클라이언트가 보낸 요청 데이터가 memberDTO 객체에 자동 바인딩

        memberService.update(memberDTO);
        // redirect: 는 클라이언트에서 새로운 url로 이동하라는 명령
        // 수정이 완료된 나의 상세페이지 확인
        return "redirect:/member/" + memberDTO.getId();
    }

    @GetMapping("/member/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        memberService.deleteById(id);
        return "redirect:/member/";
    }

    @GetMapping("member/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // session을 무효화
        return "index";
    }

}
