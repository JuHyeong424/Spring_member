package com.example.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller // spring MVC에서 이 클래스가 웹 요청을 처리하는 컨트롤러. Spring의 컴포넌트 스캔에 의해 자동으로 감지되어 Bean으로 등록됨
public class HomeController {

    // 기본페이지 요청 메서드
    @GetMapping("/") // 루트 URL에 대한 HTTP GET 요청 처리할 메서드 매핑.
    public String index() {
        return "index"; // templates 폴더의 index.html 찾음
    }
}
