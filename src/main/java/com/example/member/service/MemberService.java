package com.example.member.service;

import com.example.member.dto.MemberDTO;
import com.example.member.entity.MemberEntity;
import com.example.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service // 서비스 계층의 역할 수행 클래스. 컨트롤러와 데이터 엑세스 계층(DAO/Repository) 사이에 위치
@RequiredArgsConstructor // fianl 객체 생성자 자동 생성
public class MemberService {

    private final MemberRepository memberRepository;

    public void save(MemberDTO memberDTO) {

        // 1. dto -> entity 변환
        // 2. repository의 save 메서드 호출
        MemberEntity memberEntity = MemberEntity.toMemberEntity(memberDTO);
        memberRepository.save(memberEntity);
        // repository의 save 메서드 호출 (조건. entity 객체를 넘겨줘야 함)
    }

    public MemberDTO login(MemberDTO memberDTO) {

        /*
        1. 회원이 입력한 이메일로 db에서 조회
        2. db에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
         */

        Optional<MemberEntity> byMemberEmail = memberRepository.findByMemberEmail(memberDTO.getMemberEmail());// dto의 email을 받아 repository의 findByMemberEmail에 넣기
        if (byMemberEmail.isPresent()) {
            // 조회 결과가 있다(헤당 이메일을 가진 회원 정보가 있다)

            MemberEntity memberEntity = byMemberEmail.get(); // Optional을 벗기고(get()), 안에 있는 entity 객체 가져오기

            // entity의 패스워드와 dto의 패스워드 비교
            if (memberEntity.getMemberPassword().equals(memberDTO.getMemberPassword())) {
                // 비밀번호 일치

                // entity -> dto 변환 후 리턴 (entity 객체는 service 클래스 안에서만 사용하도록 코딩. controller에서는 dto 객체 사용)
                // entity는 db 객체여서 entity를 화면으로 가져가지 않고 보호하기 위해서.
                MemberDTO dto = MemberDTO.toMemberDTO(memberEntity);
                return dto;
            } else {
                // 비밀번호 불일치(로그인 실패)
                return null;
            }
        } else {
            // 조회 결과가 없다(해다 이메일을 가진 회원이 없다)
            return null;
        }
    }

    public List<MemberDTO> findAll() {
        // repository에 있는 member를 list에 담음
        List<MemberEntity> memberEntityList = memberRepository.findAll(); // repository와 관련된 것은 entity로 주고 받음
        List<MemberDTO> memberDTOList = new ArrayList<>(); // memberDTO로 list 생성
        for (MemberEntity memberEntity: memberEntityList) { // repository에 있는 member를 memberEntity에 주고
            // memberEntity를 memberDTO로 변환 후, dto list에 저장
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
            /*
            MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
            memberDTOList.add(memberDTO);
             */
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        // id가 null인 것을 방지하기 위해 MemberEntity를 Optional로 감싸기
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById(id);
        if (optionalMemberEntity.isPresent()) {
            // id가 존재하면
            // Optional로 감싼 것을 get()으로 풀고, entity를 dto로 변환해서 controller에 반환
            /*
            MemberEntity memberEntity = optionalMemberEntity.get();
            MemberDTO.toMemberDTO(memberEntity);
            return memberDTO;
             */
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public MemberDTO updateForm(String myEmail) {
        // repository에 해당 mail 정보 찾아서 저장
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(myEmail);
        if (optionalMemberEntity.isPresent()) {
            return MemberDTO.toMemberDTO(optionalMemberEntity.get());
        } else {
            return null;
        }
    }

    public void update(MemberDTO memberDTO) {
        /*
        save()는
        db에 id가 없으면 insert query
        db에 id가 있는 entity면 update query 수행
         */
        memberRepository.save(MemberEntity.toUpdateMemberEntity(memberDTO));
    }

    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }
}
