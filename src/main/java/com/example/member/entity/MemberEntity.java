package com.example.member.entity;

import com.example.member.dto.MemberDTO;
import com.example.member.service.MemberService;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


// JPA를 사용해 데이터베이스와 연동
// 반드시 기본 생성자 필요. 적절한 식별자(Primary key, @Id) 필요
@Entity // 데이터베이스 테이블과 매핑되어 CRUD 작업 시 JPA가 이 클래스를 기반으로 데이터베이스와 상호작용
@Getter
@Setter
@Table(name = "member_table") // 데이터 베이스에 있는 member_table이라는 이름의 테이블과 매핑
public class MemberEntity {

    @Id // pk(기본키) 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto.increment(데이터베이스가 자동으로 증가시키는 값을 사용하도록 설정). 새로운 엔티티가 저장될 때 데이터베이스에서 자동으로 Id 할당
    private Long id;

    /*
    JPA를 사용해 데이터베이스 테이블의 컬럼과 매핑되는 필드 정의
    @Column: 클래스 필드를 데이터베이스 테이블의 컬럼과 매핑
     */
    @Column(unique = true) // unique 제약조건 추가. 데이터베이스 테이블에서 member_email 값 중복 불가
    private String memberEmail; // member_email이라는 이름의 컬럼에 매핑

    @Column
    private String memberPassword;

    @Column
    private String memberName;

    /*
    위 필드 포함 엔티티 기반 데이터베이스 테이블 예시
    CREATE TABLE member_table (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    member_email VARCHAR(255) UNIQUE,
    member_password VARCHAR(255),
    member_name VARCHAR(255)
    );
     */

    // MemberService에서 dto의 객체를 entity로 변환하기 위한 메서드
    public static MemberEntity toMemberEntity(MemberDTO memberDTO) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMemberEmail(memberDTO.getMemberEmail()); // dto에 담긴 email을 entity의 email에 저장
        memberEntity.setMemberPassword(memberDTO.getMemberPassword()); // dto에 담긴 password를 entity의 password에 저장
        memberEntity.setMemberName(memberDTO.getMemberName()); // dto에 담긴 name을 entity의 name에 저장
        return memberEntity;
    }
}
