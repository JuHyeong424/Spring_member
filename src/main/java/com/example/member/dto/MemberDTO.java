package com.example.member.dto;

import com.example.member.entity.MemberEntity;
import lombok.*;

@Getter // Getter 메서드 자동 생성(getId() 등)
@Setter // Setter 메서드 자동 생성(setId(Long id))
@NoArgsConstructor // 파라미터가 없는 기본 새성자 자동 생성(public MemberDTO(){})
@ToString // 객체의 모든 필드 값을 문자열로 출력. public String toString() { return id; }
public class MemberDTO {
    // 회원 데이터 관리(DTO-data transfer object)
    // 데이터 계층(entity)와 서비스 계층 간의 데이터 전송
    private Long id;
    private String memberEmail;
    private String memberPassword;
    private String memberName;

    // Service class에서 entity를 dto로 변환하기 위한 메서드
    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setId(memberEntity.getId());
        memberDTO.setMemberEmail(memberEntity.getMemberEmail());
        memberDTO.setMemberPassword(memberEntity.getMemberPassword());
        return memberDTO;
    }
}
