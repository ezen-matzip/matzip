package com.ezen.matzip.domain.restaurant.service;

import com.ezen.matzip.domain.restaurant.dto.RegistDTO;
import com.ezen.matzip.domain.restaurant.dto.RestaurantDTO;
import com.ezen.matzip.domain.restaurant.entity.*;
import com.ezen.matzip.domain.restaurant.repository.MenuRepository;
import com.ezen.matzip.domain.restaurant.repository.RegistRepository;
import com.ezen.matzip.domain.restaurant.repository.RestaurantRepository;
import com.ezen.matzip.domain.restaurant.repository.KeywordRepository;
import com.ezen.matzip.domain.review.dto.ReviewDTO;
import com.ezen.matzip.domain.review.entity.Review;
import com.ezen.matzip.domain.review.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final KeywordRepository keywordRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final RegistRepository registRepository;

    public List<ReviewDTO> getReviewsByRestaurant(int restaurantCode)
    {
        Restaurant restaurant = restaurantRepository.findByRestaurantCode(restaurantCode);
        List<Review> reviews = reviewRepository.findByRestaurantCode(restaurant);
        List<ReviewDTO> result = new ArrayList<>();
        for (Review review : reviews)
        {
            ReviewDTO dto = new ReviewDTO();
            dto.setRestaurantName(restaurant);
            dto.setRestaurantCode(restaurant);
            dto.setReviewCode(review.getReviewCode());
            dto.setReviewDate(review.getReviewDate());
            dto.setReviewContent(review.getReviewContent());
            dto.setRating(review.getRating());

            result.add(dto);
        }

        return result;
    }

    public RestaurantDTO getRestaurantDetail(int restaurantCode) {
        Restaurant restaurant = restaurantRepository.findByRestaurantCode(restaurantCode);
        if (restaurant == null) {
            System.out.println("🚨 restaurantCode " + restaurantCode + "에 해당하는 레스토랑이 존재하지 않습니다.");
        } else {
            System.out.println("✅ restaurantCode " + restaurantCode + "에 해당하는 레스토랑 이름: " + restaurant.getRestaurantName());
        }
        return new RestaurantDTO(
                restaurant,
                menuRepository.findByRestaurantCode(restaurant),
                keywordRepository.findByRestaurantCode(restaurant)
        );
    }

    public String[] splitKeywords(String keyword)
    {
        String[] keywords = keyword.split("\\s");
        return keywords;
    }

    public List<RestaurantDTO> findByKeywordOrderByScore(String keyword)
    {
        List<Object[]> foundByMenu = menuRepository.findRestaurantAndScoreByMenuName(keyword);
        List<Object[]> foundByRestInfo = restaurantRepository.findRestaurantsByKeywordWithScore(keyword);
        List<Object[]> foundByKeyword = keywordRepository.findRestaurantAndScoreByRestaurantKeyword(keyword);

        Map<RestaurantDTO, Integer> resultSet = new HashMap<>();

        for (Object[] fmenu : foundByMenu)
        {
            Restaurant menu = (Restaurant)fmenu[0];
            Integer score = ((Number) fmenu[1]).intValue();

            resultSet.put(
                    new RestaurantDTO(
                            menu,
                            menuRepository.findByRestaurantCode(menu),
                            keywordRepository.findByRestaurantCode(menu)
                    ), score
            );
        }

        for (Object[] frest : foundByRestInfo)
        {
            Restaurant rest = (Restaurant)frest[0];
            Integer score = ((Number) frest[1]).intValue();
            RestaurantDTO dto = new RestaurantDTO(rest,
                    menuRepository.findByRestaurantCode(rest),
                    keywordRepository.findByRestaurantCode(rest));

            if(resultSet.containsKey(dto))
            {
                Integer newScore = resultSet.get(dto) + score;
                resultSet.put(dto, newScore);
            }
            else
                resultSet.put(dto, score);
        }

        for (Object[] fkeyw : foundByKeyword)
        {
            Restaurant rest = (Restaurant)fkeyw[0];
            Integer score = ((Number) fkeyw[1]).intValue();
            RestaurantDTO dto = new RestaurantDTO(rest,
                    menuRepository.findByRestaurantCode(rest),
                    keywordRepository.findByRestaurantCode(rest));

            if(resultSet.containsKey(dto))
            {
                Integer newScore = resultSet.get(dto) + score;
                resultSet.put(dto, newScore);
            }
            else
                resultSet.put(dto, score);
        }

        resultSet.forEach((key, value) -> {
            System.out.println("Key: " + key + ", Value: " + value);
        });

        List<Map.Entry<RestaurantDTO, Integer>> sortedList = new ArrayList<>(resultSet.entrySet());
        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        List<RestaurantDTO> finalList = new ArrayList<>();
        for (Map.Entry<RestaurantDTO, Integer> restaurant : sortedList)
        {
            finalList.add(restaurant.getKey());
            System.out.println("final: " + restaurant.getKey());
        }

        return finalList;
    }

    public String findLocationByRestaurantCode(Integer restaurantCode)
    {
        Restaurant restarant = restaurantRepository.findByRestaurantCode(restaurantCode);
        System.out.println("location: " + restarant.getRestaurantLocation());
        return restarant.getRestaurantLocation();
    }

    private Category convertToCategory(String categoryString) {
        return new Category(Integer.parseInt(categoryString), categoryString);
    }

    @Transactional
    public void registRestaurant(RegistDTO registDTO) {

        String startTimeString = registDTO.getRestaurantStartTime();
        String endTimeString = registDTO.getRestaurantEndTime();

        Time startTime = Time.valueOf(startTimeString + ":00");
        Time endTime = Time.valueOf(endTimeString + ":00");

        Category category = convertToCategory(registDTO.getRestaurantCategory());

        Restaurant regist =
                new Restaurant(
                        registDTO.getRestaurantCode(),
                        registDTO.getRestaurantName(),
                        registDTO.getRestaurantLocation(),
                        registDTO.getRestaurantContactNumber(),
                        registDTO.getRestaurantDescription(),
                        registDTO.getMainMenu(),
                        startTime,
                        endTime,
                        registDTO.getRestaurantService(),
                        category);



        regist.setRestaurantRegistrationDate(new Date());
        regist.setRestaurantActiveStatus(0);  // 활성 상태
        regist.setRestaurantUniqueKeywords(null);  // 예시 키워드
        regist.setRestaurantStatus(0);
        regist.setBusinessCode(11);

        List<Menu> menuList = IntStream.range(0, registDTO.getMenuName().size())
                .mapToObj(i -> new Menu(registDTO.getMenuName().get(i), registDTO.getMenuPrice().get(i), regist))
                .collect(Collectors.toList());

        regist.setMenus(menuList);

        List<Keyword> keywordList = registDTO.getRestaurantKeyword().stream()
                .map(keyword -> new Keyword(keyword, regist))
                .collect(Collectors.toList());

        regist.setKeywords(keywordList);


        System.out.println(regist);
        registRepository.save(regist);

    }

    @Transactional
    public void modifyRestaurant(RegistDTO registDTO) {
        Restaurant foundModify = restaurantRepository.findByRestaurantCode(registDTO.getRestaurantCode());

        String startTimeString = registDTO.getRestaurantStartTime();
        String endTimeString = registDTO.getRestaurantEndTime();

        Time startTime = Time.valueOf(startTimeString + ":00");
        Time endTime = Time.valueOf(endTimeString + ":00");

        Category category = convertToCategory(registDTO.getRestaurantCategory());

        foundModify.Modify(
                registDTO.getRestaurantCode(),
                registDTO.getRestaurantName(),
                registDTO.getRestaurantLocation(),
                registDTO.getRestaurantContactNumber(),
                registDTO.getRestaurantDescription(),
                registDTO.getMainMenu(),
                startTime,
                endTime,
                registDTO.getRestaurantService(),
                category);


        // 1. 기존 메뉴 목록의 칼럼값만 삭제 (연관 관계 유지)
        foundModify.getMenus().forEach(menu -> {
            menu.ModifyMenu(null,0);
        });

        // 2. 새로운 메뉴 목록 추가
        List<Menu> menuList = IntStream.range(0, registDTO.getMenuName().size())
                .mapToObj(i -> new Menu(registDTO.getMenuName().get(i), registDTO.getMenuPrice().get(i), foundModify))
                .collect(Collectors.toList());
        foundModify.setMenus(menuList);

        // 3. 기존 키워드 목록의 칼럼값만 삭제 (연관 관계 유지)
        foundModify.getKeywords().forEach(keyword -> {
            keyword.ModifyKeyword(null);
        });

        // 4. 새로운 키워드 목록 추가
        List<Keyword> keywordList = registDTO.getRestaurantKeyword().stream()
                .map(keyword -> new Keyword(keyword, foundModify))
                .collect(Collectors.toList());
        foundModify.setKeywords(keywordList);
    }
}
