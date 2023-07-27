package com.sparta.foodtruck.domain.food.service;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.foodtruck.domain.food.dto.*;
import com.sparta.foodtruck.domain.food.entity.*;
import com.sparta.foodtruck.domain.food.repository.FoodCommentRepository;
import com.sparta.foodtruck.domain.food.repository.FoodLikeRepository;
import com.sparta.foodtruck.domain.food.repository.FoodRepository;
import com.sparta.foodtruck.domain.food.repository.FoodValueRepository;
import com.sparta.foodtruck.domain.user.entity.AccountInfo;
import com.sparta.foodtruck.domain.user.repository.AccountInfoRepository;
import com.sparta.foodtruck.domain.user.sercurity.UserDetailsImpl;
import com.sparta.foodtruck.global.dto.CustomStatusResponseDto;
import com.sparta.foodtruck.global.exception.CustomStatusException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.sparta.foodtruck.domain.food.entity.QFood.food;
import static com.sparta.foodtruck.domain.food.entity.QFoodComment.foodComment;
import static com.sparta.foodtruck.domain.food.entity.QFoodLike.foodLike;
import static com.sparta.foodtruck.domain.food.entity.QFoodValue.foodValue;


@Slf4j
@Service
@RequiredArgsConstructor
public class FoodService {

    private final FoodRepository foodRepository;
    private final FoodLikeRepository foodLikeRepository;
    private final FoodValueRepository foodValueRepository;
    private final AccountInfoRepository accountInfoRepository;
    private final JPAQueryFactory queryFactory;
    private final FoodCommentRepository foodCommentRepository;

    private final Long MAX_FOOD = 82L;


    public ResponseEntity<CustomStatusResponseDto> createFood(CreateFoodRequestDto requestDto) {
        Food newFood = new Food(requestDto.getFoodName(), requestDto.getImageUrl());
        newFood = foodRepository.save(newFood);

        FoodValue newFoodValue = new FoodValue(newFood, requestDto.getSalty(), requestDto.getHot(), requestDto.getSpicy(), FoodValue.FoodWorldValue.findByNumber(requestDto.getWorld()));
        // FoodValue.FoodWorldValue.findByNumber(requestDto.getWorld()) : FoodValue의 FoodWorldValue enum에서 findByNumber 메서드 호출, requestDto 객체에서 가져온 getWorld() 값에 해당하는 FoodWorldValue를 찾아 반환
        foodValueRepository.save(newFoodValue);

        return ResponseEntity.ok(new CustomStatusResponseDto(true));
    }


    public ResponseEntity<List<FoodResponseDto>> resultFood(FoodRequestDto requestDto) {
        List<Long> findFood;
        if (requestDto.getWorld() == 0) {
            findFood = queryFactory
                    .select(foodValue.id) // foodValue 테이블에서 id 필드 값 조회
                    .from(foodValue) // foodValue Entity 테이블에서
                    .where(foodValue.salty.eq(requestDto.isSalty()), // foodValue의 salty가 requestDto의 salty와 동일한가
                            foodValue.maxSpicy.goe(requestDto.getSpicy()),
                            foodValue.minSpicy.loe(requestDto.getSpicy()),
                            foodValue.hot.eq(requestDto.isHot()))
                    .fetch(); // 쿼리를 실행하고 결과륿 반환하는데 음식 ID의 리스트를 가져와 findFood에 저장
        } else {
            findFood = queryFactory
                    .select(foodValue.id)
                    .from(foodValue)
                    .where(foodValue.salty.eq(requestDto.isSalty()),
                            foodValue.maxSpicy.goe(requestDto.getSpicy()),
                            foodValue.minSpicy.loe(requestDto.getSpicy()),
                            foodValue.world.eq(FoodValue.FoodWorldValue.findByNumber(requestDto.getWorld())),
                            foodValue.hot.eq(requestDto.isHot()))
                    .fetch();
        }

        Collections.shuffle(findFood); // findFood 리스트의 순서를 무작위로 섞음
        while (findFood.size() < 4) {
            Long Index = randomIndex();
            if (!findFood.contains(Index)) {
                findFood.add(Index);
            }
        }
        findFood = findFood.subList(0, 4); // findFood 리스트에서 처음부터 4개의 요소를 추출해 새로운 리스트 생성
                                            // ==> 4개의 음식 ID만 유지
        return ResponseEntity.ok(getResult(findFood).stream().map(FoodResponseDto::new).toList());
    }

    private Long randomIndex() {
        return (long) (Math.random() * (MAX_FOOD + 1)); // 0부터 MAX_FOOD 이하
    }

    private List<Food> getResult(List<Long> num) {
        List<Food> resultFood = queryFactory
                .select(foodValue.food)
                .from(foodValue)
                .where(foodValue.id.in(num)) // in(): 주어진 리스트 안에 해당 필드 값이 포함되는지 비교하는 조건 생성
                .fetch();
        Collections.shuffle(resultFood);
        return resultFood;
    }

    public List<FoodResponseDto> getRandomResult() {
        List<Long> initNumber = new ArrayList<>();
        while (initNumber.size() < 4) {
            Long Index = randomIndex();
            if (!initNumber.contains(Index)) {
                initNumber.add(Index);
            }
        }
        return getResult(initNumber).stream().map(FoodResponseDto::new).toList();
    }

    public List<FoodResponseDto> getFoodRank() {
        Pageable pageable = PageRequest.of(0, 5); // 5개씩 끊어서(1,2,3,4,5까지 조회)
        Page<Food> foodList = foodRepository.findAllByOrderBySelectByDesc(pageable);

        return foodList.map(FoodResponseDto::new).stream().toList();
    }

    @Transactional
    public CustomStatusResponseDto choiceFood(Long foodId) {
        Long sel = queryFactory
                .select(food.selectBy) // food의 selectBy 필드 조회
                .from(food) // food 테이블에서
                .where(food.id.eq(foodId)) // food 테이블의 id 필드 값이 foodId와 동일한
                .fetchOne(); // 단일 결과를 가져옴
        queryFactory
                .update(food) // food 테이블을 업데이트(업데이트 대상 테이블)
                .set(food.selectBy, ++sel) // food 테이블의 selectBy 필드를 업그레이드, sel 변수 값을 1증가시킨 후 할당
                .where(food.id.eq(foodId)) // food 테이블의 id 필드가 foodId와 동일하면
                .execute(); // 설정된 조건에 따라 업데이트 쿼리 실행
        return new CustomStatusResponseDto(true);
    }

    private Food findFood(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(() ->
                new IllegalArgumentException("해당하는 음식이 존재하지 않습니다."));
    }

    public Long likeFood(Long foodId, UserDetailsImpl userDetails) {
        Food food = findFood(foodId);
        AccountInfo accountInfo = userDetails.getAccountInfo();

        if (accountInfo != null) {
            FoodLike foodLike = foodLikeRepository.findByAccountInfoAndFood(accountInfo, food);
            if (foodLike == null) { // 처음 누르면
                foodLike = new FoodLike(accountInfo, food);
                foodLikeRepository.save(foodLike); // 좋아요 저장
            } else
                foodLikeRepository.delete(foodLike); // 좋아요 삭제
        }
        return (long) queryFactory.selectFrom(foodLike).where(foodLike.food.id.eq(foodId)).fetch().size();
    }   // foodLike 테이블 조회 -> foodLike 테이블의 food 필드 id 값이 foodId와 일치하는 레코드 선택하는 조건
        // -> 리스트로 반환 -> 리스트의 크기 반환(음식 ID에 해당하는 foodLike 테이블의 레코드 개수)


    public ResponseEntity<List<CommentResponseDto>> addComment(Long foodId, UserDetailsImpl userDetails, CommentRequestDto requestDto) {
        Food food = findFood(foodId);

        // foodComment food / account = null / account.username / content
        FoodComment foodComment = new FoodComment(food, userDetails.getAccountInfo(), requestDto.getContent());
        foodCommentRepository.save(foodComment);
        // 테스트 ㄱㄱ
        return ResponseEntity.status(201).body(getCommentByFood(foodId));
    }

    @Transactional
    public ResponseEntity<List<CommentResponseDto>> addCommentDebug(Long foodId, CommentRequestDebugDto requestDto) {
        Food food = findFood(foodId);

        // foodComment food / account = null / account.username / content
        FoodComment foodComment = new FoodComment(food, requestDto.getUsername(), requestDto.getContent());
        foodCommentRepository.save(foodComment);
        // 테스트 ㄱㄱ
        return ResponseEntity.status(201).body(getCommentByFood(foodId));
    }

    @Transactional
    public ResponseEntity<List<CommentResponseDto>> addCommentDebugTwo(Long foodId, UserDetailsImpl userDetails, CommentRequestDebugDto requestDto) {
        Food food = findFood(foodId);

        FoodComment foodComment = new FoodComment(food, userDetails.getAccountInfo(), requestDto.getContent());
        foodCommentRepository.save(foodComment);

        return ResponseEntity.status(201).body(getCommentByFood(foodId));
    }

    // 이게 어차피 food 조회해 주는 거잖아 넹

    private List<CommentResponseDto> getCommentByFood(Long foodId) {
        List<FoodComment> foodCommentList = queryFactory.selectFrom(foodComment).where(foodComment.food.id.eq(foodId)).orderBy(foodComment.id.desc()).fetch();
        return foodCommentList.stream().map(CommentResponseDto::new).toList();
    }

    public ResponseEntity<CommentListResponseDto> getCommentByFood(Long foodId, UserDetailsImpl userDetails) {
        List<CommentResponseDto> foodCommentList = getCommentByFood(foodId); // foodId에 해당하는 음식의 코멘트
        CommentListResponseDto responseDto = new CommentListResponseDto();
        responseDto.setData(foodCommentList);
        if (userDetails != null) {
            Long id = queryFactory.select(foodLike.accountInfo.id).from(foodLike).where(foodLike.accountInfo.id.eq(userDetails.getAccountInfo().getId())).fetchOne();
            responseDto.setUserLike((id != null ? true : false));
        } else
            responseDto.setUserLike(false);
        return ResponseEntity.ok(responseDto);
    }

    @Transactional
    public List<CommentResponseDto> patchCommentByFood(Long foodId, UserDetailsImpl userDetails, Long commentId, CommentRequestDebugDto requestDto) {
        checkComment(foodId, commentId, userDetails); // 유효한 코멘트인지 확인
        queryFactory.update(foodComment).set(foodComment.content, requestDto.getContent()).where(foodComment.id.eq(commentId)).execute();

        return getCommentByFood(foodId);
    }

    @Transactional
    public ResponseEntity<CustomStatusResponseDto> DeleteCommentByFood(Long foodId, Long commentId, UserDetailsImpl userDetails) {
        log.info("enter");

        checkComment(foodId, commentId, userDetails);

        queryFactory.delete(foodComment).where(foodComment.id.eq(commentId)).execute();

        return ResponseEntity.ok(new CustomStatusResponseDto(true));
    }

    private void checkComment(Long foodId, Long commentId, UserDetailsImpl userDetails) {
        log.info("enter check");
        Long accountId = queryFactory.select(foodComment.accountInfo.id).from(foodComment).where(foodComment.food.id.eq(foodId), foodComment.id.eq(commentId)).fetchOne();
        if (accountId == null)
            throw CustomStatusException.builder("해당 내용이 존재하지 않습니다.").status(404).build();
        if (accountId != userDetails.getUser().getId()) {
            throw CustomStatusException.builder("사용자가 일치 하지 않습니다.").status(401).build();
        }
    }

}