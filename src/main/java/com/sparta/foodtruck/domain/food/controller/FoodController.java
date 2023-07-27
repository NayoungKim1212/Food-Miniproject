package com.sparta.foodtruck.domain.food.controller;

import com.sparta.foodtruck.domain.food.dto.*;
import com.sparta.foodtruck.domain.food.service.FoodService;
import com.sparta.foodtruck.domain.user.sercurity.UserDetailsImpl;
import com.sparta.foodtruck.global.dto.CustomStatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/food")
@RequiredArgsConstructor
public class FoodController {

    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<CustomStatusResponseDto> createFood(@RequestBody CreateFoodRequestDto requestDto) {
        return foodService.createFood(requestDto);
    }

    /**
     * @param requestDto {
     *                   gender: true | false
     *                   salty: true | false
     *                   spicy: (0 - 4)
     *                   world : (0 - 4)
     *                   hot = true | false
     *                   }
     * @return foodList : [
     * {
     * id : Number
     * name: “foodName”,
     * ImageUrl : “url”
     * } ….
     * ]
     */
    @PostMapping("/result")
    public ResponseEntity<List<FoodResponseDto>> resultFood(@RequestBody FoodRequestDto requestDto) {
        return foodService.resultFood(requestDto);
    }

    @GetMapping("/result/random")
    public List<FoodResponseDto> getRandomResult() {
        return foodService.getRandomResult();
    }

    @PatchMapping("/{foodId}/choice")
    public CustomStatusResponseDto choiceFood(@PathVariable Long foodId) {
        return foodService.choiceFood(foodId);
    }

    @GetMapping("/rank")
    public List<FoodResponseDto> getFoodRank() {
        return foodService.getFoodRank();
    }

    @PostMapping("/{foodId}/like")
    public Long likeFood(@PathVariable Long foodId,
                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return foodService.likeFood(foodId, userDetails);
    }

    @PostMapping("/{foodId}/comment")
    public ResponseEntity<List<CommentResponseDto>> addComment(@PathVariable Long foodId,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @RequestBody CommentRequestDto requestDto) {
        return foodService.addComment(foodId, userDetails, requestDto);
    }

    @PostMapping("/{foodId}/comment/debug")
    public ResponseEntity<List<CommentResponseDto>> addCommentDebug(@PathVariable Long foodId,
                                                                    @RequestBody CommentRequestDebugDto requestDto) {
        return foodService.addCommentDebug(foodId, requestDto);
    }

    @GetMapping("/{foodId}/comment")
    public ResponseEntity<CommentListResponseDto> getCommentByFood(@PathVariable Long foodId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return foodService.getCommentByFood(foodId, userDetails);
    }

    // 댓글 수정, 삭제
    @PatchMapping("/{foodId}/comment/{commentId}")
    public List<CommentResponseDto> patchCommentByFood(@PathVariable Long foodId,
                                                       @PathVariable Long commentId,
                                                       @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @RequestBody CommentRequestDebugDto requestDto) {
        List<CommentResponseDto> responseDtos = foodService.patchCommentByFood(foodId, userDetails, commentId, requestDto);
        return responseDtos;
    }

    @DeleteMapping("/{foodId}/comment/{commentId}")
    public ResponseEntity<CustomStatusResponseDto> DeleteCommentByFood(@PathVariable Long foodId,
                                                                       @PathVariable Long commentId,
                                                                       @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return foodService.DeleteCommentByFood(foodId, commentId, userDetails);
    }

    @PostMapping("/{foodId}/comment/debugtwo")
    public ResponseEntity<List<CommentResponseDto>> addCommentDebug(@PathVariable Long foodId,
                                                                    @AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                    @ModelAttribute CommentRequestDebugDto requestDto
    ) {
        return foodService.addCommentDebugTwo(foodId, userDetails, requestDto);
    }
}
