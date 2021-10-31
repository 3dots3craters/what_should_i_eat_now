package seoul42.openproject.selectfood.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import seoul42.openproject.selectfood.advice.exception.CUserNotFoundException;
import seoul42.openproject.selectfood.domain.*;
import seoul42.openproject.selectfood.dto.member.MemberSignUpDto;
import seoul42.openproject.selectfood.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FoodService foodService;

    @Transactional
    public Long signUpWithFoods(MemberSignUpDto memberInfo) {
        List<Food> foods1 = foodService.findByNameList(memberInfo.getLikeFoodNames());
        List<LikeFood> likeFoods = new ArrayList<>();
        for (Food food : foods1) {
            likeFoods.add(LikeFood.createLikeFood(food));
        }
        List<Food> foods2 = foodService.findByNameList(memberInfo.getDislikeFoodNames());
        List<DislikeFood> dislikeFoods = new ArrayList<>();
        for (Food food : foods2) {
            dislikeFoods.add(DislikeFood.createDislikeFood(food));
        }
        Member member = Member.createMember(memberInfo, likeFoods, dislikeFoods);
        memberRepository.save(member);
        return member.getId();
    }

//    private void validateDuplicateMember(Member member) {
//        memberRepository.findByEmail(member.getEmail())
//                // isPresent() 와 혼동 주의
//                .ifPresent(m -> {
//                    throw new IllegalStateException("이미 존재하는 회원입니다.");
//                });
//    }

    public Optional<Member> validateDuplicateEmail(String email) {
        return memberRepository.findByEmail(email);
    }

//    public Optional<Member> validateDuplicateNickName(String nickName) {
//        return memberRepository.findByNickName(nickName);
//    }

    public List<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findId(Long memberId) {
        return memberRepository.findById(memberId);
    }

    public Optional<Member> findEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public List<String> getLikeFoods(String email) {
        List<String> foodNames = new ArrayList<>();
        List<LikeFood> likeFoods = memberRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new).getLikeFoods();
        for (LikeFood likeFood:
                likeFoods) {
            foodNames.add(likeFood.getFood().getName());
        }
        return foodNames;
    }

    public List<String> getDisLikeFoods(String email) {
        List<String> foodNames = new ArrayList<>();
        List<DislikeFood> dislikeFoods = memberRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new).getDislikeFoods();
        for (DislikeFood dislikeFood:
                dislikeFoods) {
            foodNames.add(dislikeFood.getFood().getName());
        }
        return foodNames;
    }

    public List<String> getSelectedFoods(String email) {
        List<String> foodNames = new ArrayList<>();
        List<SelectedFood> selectedFoods = memberRepository.findByEmail(email).orElseThrow(CUserNotFoundException::new).getSelectedFoods();
        for (SelectedFood selectedFood:
                selectedFoods) {
            foodNames.add(selectedFood.getFood().getName());
        }
        return foodNames;
    }


//    public Long updatePickedFood(MemberEditDto memberEditDto) {
//        Optional<Member> member = memberRepository.findByEmail(memberEditDto.getEmail());
//        member.ifPresent(member1 -> {
//            member1.setLikeFoodList(member1.getPickedFoodList() + ", " + memberEditDto.getPickedFood());
//            memberRepository.save(member1);
//        });
//        if (member.isPresent())
//            return member.get().getId();
//        return -1L;
//    }
//
//    public Long updateLikeFood(MemberEditDto memberEditDto) {
//        Optional<Member> member = memberRepository.findByEmail(memberEditDto.getEmail());
//        member.ifPresent(member1 -> {
//            member1.setLikeFoodList(member1.getLikeFoodList() + ", " + memberEditDto.getLikeFood());
//            memberRepository.save(member1);
//        });
//        if (member.isPresent())
//            return member.get().getId();
//        return -1L;
//    }
//
//    public Long updateDislikeFood(MemberEditDto memberEditDto) {
//        Optional<Member> member = memberRepository.findByEmail(memberEditDto.getEmail());
//        member.ifPresent(member1 -> {
//            member1.setDislikeFoodList(member1.getDislikeFoodList() + ", " + memberEditDto.getDislikeFood());
//            memberRepository.save(member1);
//        });
//        if (member.isPresent())
//            return member.get().getId();
//        return -1L;
//    }
}
