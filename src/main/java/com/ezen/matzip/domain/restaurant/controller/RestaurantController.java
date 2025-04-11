package com.ezen.matzip.domain.restaurant.controller;

import com.ezen.matzip.domain.bookmark.dto.RestaurantForBookmarkDTO;
import com.ezen.matzip.domain.bookmark.entity.Bookmark;
import com.ezen.matzip.domain.bookmark.service.BookmarkService;
import com.ezen.matzip.domain.restaurant.dto.RegistDTO;
import com.ezen.matzip.domain.restaurant.dto.RestaurantDTO;
import com.ezen.matzip.domain.restaurant.dto.RestaurantImageDTO;
import com.ezen.matzip.domain.restaurant.entity.Restaurant;
import com.ezen.matzip.domain.restaurant.service.RestaurantService;
import com.ezen.matzip.domain.review.dto.ReviewDTO;
import com.ezen.matzip.domain.review.dto.ReviewImageDTO;
import com.ezen.matzip.domain.review.service.ReviewService;
import com.ezen.matzip.domain.user.entity.Business;
import com.ezen.matzip.domain.user.entity.User;
import com.ezen.matzip.domain.user.service.UserIdCheckService;
import com.ezen.matzip.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ResourceLoader resourceLoader;
    private final UserIdCheckService userIdCheckService;
    private final ReviewService reviewService;

    //완수 북마크 기능에 필요
    private final BookmarkService bookmarkService;
    private final UserService userService;


    @GetMapping("/restaurant/{restaurantCode}")
    public String getRestaurantForUsers(@PathVariable int restaurantCode, Model model, HttpServletRequest request, Principal principal) {

        RestaurantDTO restaurant = restaurantService.getRestaurantDetail(restaurantCode);

        List<ReviewDTO> resultReview = restaurantService.getReviewsByRestaurant(restaurantCode);

        for (ReviewDTO review : resultReview) {
            List<ReviewImageDTO> imgDTOs = reviewService.getReviewImages(review.getReviewCode());
            review.setReviewImages(imgDTOs);
        }

        model.addAttribute("reviews", resultReview);

        List<RestaurantImageDTO> imgDTOs = restaurantService.getRestaurantImgs(restaurantCode);
        restaurant.setRestaurantImages(imgDTOs);
        model.addAttribute("selectedRestaurant", restaurant);
        model.addAttribute("restaurant", restaurant);

        // 완수-현재 URL을 모델에 추가
        model.addAttribute("currentUri", request.getRequestURI());

        // 완수-사용자가 로그인한 경우 북마크 여부 확인 후 모델에 추가
        // 로그인한 사용자라면 북마크 여부 체크
        if (principal != null) {
            User user = userService.findByUserId(principal.getName());
            // 북마크 여부를 판단할 때, findByUserAndRestaurant로 조회해 보세요.
            boolean bookmarked = bookmarkService.findByUserAndRestaurant(user, restaurantService.findByRestaurantCode(restaurantCode)).isPresent();
            model.addAttribute("bookmarked", bookmarked);
        } else {
            // 로그인하지 않은 경우 false로 처리
            model.addAttribute("bookmarked", false);
        }

        Double rating = reviewService.getAverageReviewRating(restaurant);
        model.addAttribute("rating", rating);

        return "domain/restaurant/user_restinfo";
    }

    @GetMapping("/admin/restaurant/{restaurantCode}")
    public String getRestaurantDetail(@PathVariable int restaurantCode, Model model) {

        RestaurantDTO restaurant = restaurantService.getRestaurantDetail(restaurantCode);
        List<ReviewDTO> resultReview = restaurantService.getReviewsByRestaurant(restaurantCode);
        List<RestaurantImageDTO> imgDTOs = restaurantService.getRestaurantImgs(restaurantCode);

        restaurant.setRestaurantImages(imgDTOs);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reviews", resultReview);

        Double rating = reviewService.getAverageReviewRating(restaurant);
        model.addAttribute("rating", rating);

        return "domain/restaurant/admin_restinfo";
    }

    @GetMapping("/business/restaurant/{restaurantCode}")
    public String getRestaurantDetail2(@PathVariable int restaurantCode, Model model, Principal principal) {

        String username = principal.getName();
        int myBusinessCode = userIdCheckService.getBusinessCodeByUserid(username);

        RestaurantDTO restaurant = restaurantService.getRestaurantByBusinessCode(myBusinessCode);
        List<ReviewDTO> resultReview = restaurantService.getReviewsByRestaurant(restaurant.getRestaurantCode());
        List<RestaurantImageDTO> imgDTOs = restaurantService.getRestaurantImgs(restaurant.getRestaurantCode());

        restaurant.setRestaurantImages(imgDTOs);

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("reviews", resultReview);

        Double rating = reviewService.getAverageReviewRating(restaurant);
        model.addAttribute("rating", rating);

        return "domain/restaurant/store_restinfo";
    }

    @GetMapping("/business/regist")
    public String registPage(Principal principal, Model model) {

        String username = principal.getName();
        int businessCode = userIdCheckService.getBusinessCodeByUserid(username);
        model.addAttribute("businessCode", businessCode);

        return "domain/store/store_apply";
    }

    @PostMapping("/business/regist/apply")
    public String regist(@ModelAttribute RegistDTO registDTO, @RequestParam List<MultipartFile> multiFiles, Principal principal) throws IOException {
        // Principal에서 현재 로그인된 사용자 이름 가져오기
        String username = principal.getName();
        Integer businessCode = userIdCheckService.getBusinessCodeByUserid(username);
        registDTO.setBusinessCode(businessCode);

        ClassPathResource resource = new ClassPathResource("static/img/restaurant");
        String staticPath;

        try {
            staticPath = resource.getFile().getAbsolutePath();
        } catch (IOException e) {
            // 경로가 없으면 생성
            File directory = new File("src/main/resources/static/img/restaurant");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            staticPath = directory.getAbsolutePath();
        }

        /** 파일에 관한 정보 저장을 위한 처리 */
        List<RestaurantImageDTO> files = new ArrayList<>();
        List<String> savedFiles = new ArrayList<>();

        try {
            int count = 0;
            for (MultipartFile file : multiFiles) {
                if (count >= 3) break;
                /** 파일명 변경 처리 */
                String originFileName = file.getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String savedFileName = UUID.randomUUID().toString().replace("-", "") + ext;

                /** 파일정보 등록 */
                String fileUrl = "/img/restaurant/" + savedFileName;
                files.add(new RestaurantImageDTO(fileUrl, originFileName, savedFileName));

                /** 파일 저장 */
                File targetFile = new File(staticPath + "/" + savedFileName);
                file.transferTo(targetFile);
                savedFiles.add(fileUrl);

                count++;
            }
        } catch (Exception e) {
            for (RestaurantImageDTO file : files) {
                new File(staticPath + "/" + file.getRestaurantSavedName()).delete();
            }
            e.printStackTrace(); // 에러 로그 확인
        }

        Restaurant registedRestaurant = restaurantService.registRestaurant(registDTO, files);

        return "redirect:/business/restaurant/" + registedRestaurant.getRestaurantCode();
    }

    @GetMapping("/business/modify")
    public String modifyPage(Model model, Principal principal) {
        String username = principal.getName();
        int businessCode = userIdCheckService.getBusinessCodeByUserid(username);
        RestaurantDTO foundRestaurant = restaurantService.getRestaurantByBusinessCode(businessCode);
        model.addAttribute("currentRestaurant", foundRestaurant);
        model.addAttribute("businessCode", businessCode);

        return "domain/store/store_edit";
    }

    @PostMapping("/business/modify")
    public String modify(@ModelAttribute RegistDTO registDTO, @RequestParam("multiFiles") List<MultipartFile> multiFiles, Principal principal) {

        Business business = userIdCheckService.findByUserId(principal.getName());
        int businessCode = business.getBusinessCode();
        registDTO.setBusinessCode(businessCode);

        RestaurantDTO foundRestaurant = restaurantService.getRestaurantByBusinessCode(businessCode);
        registDTO.setRestaurantCode(foundRestaurant.getRestaurantCode());
        restaurantService.modifyRestaurant(registDTO, multiFiles);

        return "redirect:/business/restaurant/" + registDTO.getRestaurantCode();
    }

    @GetMapping("/search")
    public String findByRestaurant(@RequestParam String keyword, Model model, HttpSession session, Principal principal) {
        session.setAttribute("lastKeyword", keyword);
        List<RestaurantDTO> restaurants = restaurantService.findByKeywordOrderByScore(keyword);
        restaurants = restaurantService.findRestaurantsAndImgs(restaurants);
        Map<RestaurantDTO, Double> restaurantsWithRating = restaurantService.restaurantAndRating(restaurants);
        model.addAttribute("restaurantList", restaurantsWithRating);
        model.addAttribute("myLoc", keyword);

        //완수- 북마크 기능에 필요
        if (principal != null) {
            User user = userService.findByUserId(principal.getName());
            List<Bookmark> bookmarks = bookmarkService.getBookmarksForUser(user);
            Set<Integer> bookmarkedRestaurantCodes = bookmarks.stream().map(b -> b.getRestaurant().getRestaurantCode()).collect(Collectors.toSet());
            model.addAttribute("bookmarkedRestaurantCodes", bookmarkedRestaurantCodes);
        }

        return "domain/search/user_restlist";
    }

    @GetMapping(value = "/search", params = "categoryCode")
    public String filteringRestaurants(@RequestParam int categoryCode, Model model, HttpSession session, Principal principal) {
        String keyword = (String) session.getAttribute("lastKeyword");
        String nationality = null;
        if (principal != null) {
            User user = userService.findByUserId(principal.getName());
            nationality = user.getNationality();
        }
        List<RestaurantDTO> restaurants = restaurantService.filteredRestaurantsByCategory(keyword, categoryCode, nationality);
        Map<RestaurantDTO, Double> restaurantsWithRating = restaurantService.restaurantAndRating(restaurants);
        model.addAttribute("restaurantList", restaurantsWithRating);
        return "domain/search/user_restlist";
    }

    //완수 북마크 기능에 필요
    // 식당 목록 페이지
    @GetMapping("/restaurants")
    public String restaurantList(Model model, Principal principal) {
        // 전체 식당 목록 조회 (엔티티 목록)
        List<Restaurant> restaurantEntities = restaurantService.findAll();

        // 각 엔티티를 RestaurantForBookmarkDTO로 변환
        List<RestaurantForBookmarkDTO> restaurantDTOs = restaurantEntities.stream().map(restaurant -> restaurantService.convertToRestaurantForBookmarkDTO(restaurant)).collect(Collectors.toList());

        restaurantDTOs.forEach(dto -> log.info("Converted DTO: {}", dto));

        model.addAttribute("restaurantList", restaurantDTOs);

        // 로그인한 사용자의 북마크된 식당 코드 목록 추가 (생략 가능)
        if (principal != null) {
            User user = userService.findByUserId(principal.getName());
            List<Bookmark> bookmarks = bookmarkService.getBookmarksForUser(user);
            Set<Integer> bookmarkedRestaurantCodes = bookmarks.stream().map(b -> b.getRestaurant().getRestaurantCode()).collect(Collectors.toSet());
            log.info("북마크된 식당 코드: {}", bookmarkedRestaurantCodes);
            model.addAttribute("bookmarkedRestaurantCodes", bookmarkedRestaurantCodes);

            // 3번: 변환된 DTO들을 로그로 확인 (디버깅용)
            restaurantDTOs.forEach(dto -> log.info("Converted DTO restaurantCode: {}", dto.getRestaurantCode()));
            log.info("북마크된 식당 코드: {}", bookmarkedRestaurantCodes);
        }


        return "domain/search/user_restlist";
    }

    @GetMapping("/restaurant/storeinfo")
    public String restaurantDetail(@RequestParam("restaurantCode") int restaurantCode, Model model, Principal principal) {

        // 엔티티 대신 DTO 사용
        RestaurantDTO restaurantDTO = restaurantService.getRestaurantDetail(restaurantCode);
        model.addAttribute("restaurant", restaurantDTO);

        // 엔티티를 별도로 조회해서 북마크 여부 확인
        Restaurant restaurant = restaurantService.findByRestaurantCode(restaurantCode);
        if (principal != null) {
            User user = userService.findByUserId(principal.getName());
            boolean bookmarked = bookmarkService.isBookmarked(user, restaurant);
            model.addAttribute("bookmarked", bookmarked);
        }

        // 위치 정보 추가 (필요한 경우, restaurant 엔티티나 별도 조회 메서드를 통해 얻어올 수 있음)
        String location = restaurantService.findLocationByRestaurantCode(restaurantCode);
        model.addAttribute("restaurantLocation", location);

        return "domain/restaurant/user_restinfo";
    }
    // 완수 끝

}

//restaurant/ → 식당 관리 (등록, 수정, 조회)

//Controller → 클라이언트 요청을 처리
//Service → 비즈니스 로직을 처리
//Repository → 데이터베이스 CRUD 처리
//DTO → 클라이언트와 데이터를 주고받을 때 사용하는 객체
//Entity → 실제 데이터베이스 테이블과 매칭되는 객체
