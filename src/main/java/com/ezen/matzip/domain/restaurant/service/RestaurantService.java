package com.ezen.matzip.domain.restaurant.service;

import com.ezen.matzip.domain.bookmark.dto.RestaurantForBookmarkDTO;
import com.ezen.matzip.domain.restaurant.dto.MenuDTO;
import com.ezen.matzip.domain.restaurant.dto.RegistDTO;
import com.ezen.matzip.domain.restaurant.dto.RestaurantDTO;
import com.ezen.matzip.domain.restaurant.dto.*;
import com.ezen.matzip.domain.restaurant.entity.*;
import com.ezen.matzip.domain.restaurant.repository.*;
import com.ezen.matzip.domain.review.dto.ReviewDTO;
import com.ezen.matzip.domain.review.dto.ReviewImageDTO;
import com.ezen.matzip.domain.review.entity.Review;
import com.ezen.matzip.domain.review.entity.ReviewImage;
import com.ezen.matzip.domain.review.repository.ReviewRepository;
import com.ezen.matzip.domain.user.entity.Business;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Time;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuRepository menuRepository;
    private final RestaurantKeywordRepository restaurantKeywordRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;
    private final RegistRepository registRepository;
    private final RestaurantImageRepository restaurantImageRepository;

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
            dto.setReviewReply(review.getReviewReply());
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
                restaurantKeywordRepository.findByRestaurantCode(restaurant),
                restaurantImageRepository.findRestaurantImageByRestaurantCode(restaurant)
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
        List<Object[]> foundByKeyword = restaurantKeywordRepository.findRestaurantAndScoreByRestaurantKeyword(keyword);

        Map<RestaurantDTO, Integer> resultSet = new HashMap<>();

        for (Object[] fmenu : foundByMenu) {
            Restaurant rest = (Restaurant) fmenu[0];
            Integer score = ((Number) fmenu[1]).intValue();

            if (rest.getRestaurantStatus() != 0) {
                resultSet.put(
                        new RestaurantDTO(
                                rest,
                                menuRepository.findByRestaurantCode(rest),
                                restaurantKeywordRepository.findByRestaurantCode(rest),
                                restaurantImageRepository.findRestaurantImageByRestaurantCode(rest)
                        ), score
                );
            }
        }

        for (Object[] frest : foundByRestInfo) {
            Restaurant rest = (Restaurant) frest[0];
            Integer score = ((Number) frest[1]).intValue();
            RestaurantDTO dto = new RestaurantDTO(rest,
                    menuRepository.findByRestaurantCode(rest),
                    restaurantKeywordRepository.findByRestaurantCode(rest),
                    restaurantImageRepository.findRestaurantImageByRestaurantCode(rest));
            if (rest.getRestaurantStatus() != 0) {
                if (resultSet.containsKey(dto)) {
                    Integer newScore = resultSet.get(dto) + score;
                    resultSet.put(dto, newScore);
                } else
                    resultSet.put(dto, score);
            }
        }

        for (Object[] fkeyw : foundByKeyword)
        {
            Restaurant rest = (Restaurant)fkeyw[0];
            Integer score = ((Number) fkeyw[1]).intValue();
            RestaurantDTO dto = new RestaurantDTO(rest,
                    menuRepository.findByRestaurantCode(rest),
                    restaurantKeywordRepository.findByRestaurantCode(rest),
                    restaurantImageRepository.findRestaurantImageByRestaurantCode(rest));
            if(rest.getRestaurantStatus() != 0) {
                if (resultSet.containsKey(dto)) {
                    Integer newScore = resultSet.get(dto) + score;
                    resultSet.put(dto, newScore);
                } else
                    resultSet.put(dto, score);
            }
        }

        List<Map.Entry<RestaurantDTO, Integer>> sortedList = new ArrayList<>(resultSet.entrySet());
        sortedList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        List<RestaurantDTO> finalList = new ArrayList<>();
        for (Map.Entry<RestaurantDTO, Integer> restaurant : sortedList)
            finalList.add(restaurant.getKey());

        return finalList;
    }

    public List<RestaurantDTO> filteredRestaurantsByCategory(String keyword, int category)
    {
        List<RestaurantDTO> list = findByKeywordOrderByScore(keyword);
        List<RestaurantDTO> filteredList = new ArrayList<>();
        for (RestaurantDTO dto : list)
        {
            if (dto.getCategoryCode().getCategoryCode() == category)
            {
                filteredList.add(dto);
            }
        }

        return filteredList;
    }

//    public List<RestaurantDTO> filteredRestaurantsByCountry(String keyword,)

    public String findLocationByRestaurantCode(Integer restaurantCode)
    {
        Restaurant restaurant = restaurantRepository.findByRestaurantCode(restaurantCode);
        System.out.println("location: " + restaurant.getRestaurantLocation());
        return restaurant.getRestaurantLocation();
    }

    private Category convertToCategory(String categoryString) {
        return new Category(Integer.parseInt(categoryString), categoryString);
    }

    @Transactional
    public Restaurant registRestaurant(RegistDTO registDTO, List<RestaurantImageDTO> restaurantImageDTO) {
        String startTimeString = registDTO.getRestaurantStartTime();
        String endTimeString = registDTO.getRestaurantEndTime();

        Time startTime = Time.valueOf(startTimeString + ":00");
        Time endTime = Time.valueOf(endTimeString + ":00");

        Category category = convertToCategory(registDTO.getRestaurantCategory());

        // 레스토랑 객체 생성
        Restaurant regist = new Restaurant(
                registDTO.getRestaurantCode(),
                registDTO.getRestaurantName(),
                registDTO.getRestaurantLocation(),
                registDTO.getRestaurantContactNumber(),
                registDTO.getRestaurantDescription(),
                registDTO.getMainMenu(),
                startTime,
                endTime,
                registDTO.getRestaurantService(),
                category,
                registDTO.getBusinessCode()
        );

        // 추가적인 레스토랑 속성 설정
        regist.setRestaurantRegistrationDate(new Date());
        regist.setRestaurantActiveStatus(0);  // 활성 상태
        regist.setRestaurantUniqueKeywords(null);  // 예시 키워드
        regist.setRestaurantStatus(0);

        // 메뉴 추가
        List<Menu> menuList = IntStream.range(0, registDTO.getMenuName().size())
                .mapToObj(i -> {
                    // 레스토랑 객체를 Menu 생성자에 전달
                    Menu menu = new Menu(registDTO.getMenuName().get(i), registDTO.getMenuPrice().get(i), regist);
                    return menu;
                })
                .collect(Collectors.toList());

        regist.setMenus(menuList);

        // 키워드 추가
        List<RestaurantKeyword> keywordList = registDTO.getRestaurantKeyword().stream()
                .map(keyword -> new RestaurantKeyword(keyword, regist))
                .collect(Collectors.toList());

        regist.setRestaurantKeywords(keywordList);

        System.out.println(regist);
        // 레스토랑 저장
        registRepository.save(regist);

        for (RestaurantImageDTO dto : restaurantImageDTO) {
            RestaurantImage restaurantImage = new RestaurantImage(
                    regist, dto.getRestaurantImagePath(), dto.getRestaurantOriginalName(), dto.getRestaurantSavedName());
            restaurantImageRepository.save(restaurantImage);
        }

        return regist;
    }

    @Transactional
    public void modifyRestaurant(RegistDTO registDTO, List<MultipartFile> multiFiles) {
        // 레스토랑 코드로 레스토랑 찾기
        Restaurant foundModify = restaurantRepository.findByRestaurantCode(registDTO.getRestaurantCode());
        System.out.println("foundModify : " + foundModify);

        // 시간 변환
        String startTimeString = registDTO.getRestaurantStartTime();
        String endTimeString = registDTO.getRestaurantEndTime();
        Time startTime = Time.valueOf(startTimeString + ":00");
        Time endTime = Time.valueOf(endTimeString + ":00");

        Category category = convertToCategory(registDTO.getRestaurantCategory());

        // 레스토랑 수정
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
                category,
                registDTO.getBusinessCode());


        List<Menu> foundMenus = menuRepository.findByRestaurantCode(foundModify);
        for (Menu menu : foundMenus) {
            menuRepository.delete(menu);
        }
        // 새로운 메뉴 객체 리스트 생성
        List<Menu> newMenus = IntStream.range(0, registDTO.getMenuName().size())
                .mapToObj(i -> {
                    Menu menu = new Menu(registDTO.getMenuName().get(i), registDTO.getMenuPrice().get(i));
                    menu.setRestaurantCode(foundModify);  // 새로운 메뉴에 레스토랑 코드 설정
                    return menu;
                })
                .collect(Collectors.toList());

        // 새로운 메뉴 리스트 추가
        foundModify.getMenus().addAll(newMenus);

        // 기존 키워드 삭제 후 새로운 키워드 추가
        List<RestaurantKeyword> foundKeywords = restaurantKeywordRepository.findByRestaurantCode(foundModify);
        for (RestaurantKeyword restaurantKeyword : foundKeywords) {
            restaurantKeywordRepository.delete(restaurantKeyword);
        }
        foundModify.getRestaurantKeywords().clear(); // 기존 키워드 삭제

        List<RestaurantKeyword> keywordList = registDTO.getRestaurantKeyword().stream()
                .map(keyword -> new RestaurantKeyword(keyword, foundModify)) // 새로운 키워드 추가
                .collect(Collectors.toList());
        foundModify.getRestaurantKeywords().addAll(keywordList);

        List<RestaurantImage> oldImages = restaurantImageRepository.findRestaurantImageByRestaurantCode(foundModify.getRestaurantCode());
        for (RestaurantImage img : oldImages) {
            File oldFile = new File("C:/dev/img/restaurant" + img.getRestaurantImagePath());
            if (oldFile.exists()) oldFile.delete();
            restaurantImageRepository.delete(img);
        }// 새로운 키워드 추가

        List<RestaurantImageDTO> files = new ArrayList<>();
        String filePath;

        try {
            File fileDir = new File("C:/dev/img/restaurant");
            if (!fileDir.exists()) fileDir.mkdirs();
            filePath = fileDir.getAbsolutePath();

            int count = 0;
            for (MultipartFile file : multiFiles) {
                if (file.isEmpty()) continue;
                if (count >= 3) break;

                String originFileName = file.getOriginalFilename();
                String ext = originFileName.substring(originFileName.lastIndexOf("."));
                String savedFileName = UUID.randomUUID().toString().replace("-", "") + ext;

                // 실제 저장
                file.transferTo(new File(filePath + "/" + savedFileName));

                // 파일 정보 저장
                files.add(new RestaurantImageDTO("/img/restaurant/" + savedFileName, originFileName, savedFileName));
                count++;
            }

            for (RestaurantImageDTO dto : files) {
                RestaurantImage restaurantImage = new RestaurantImage(
                        foundModify, dto.getRestaurantImagePath(), dto.getRestaurantOriginalName(), dto.getRestaurantSavedName());
                restaurantImageRepository.save(restaurantImage);
            }


        } catch (IOException e) {
            System.out.println("파일 저장 실패");
            e.printStackTrace();
        }

        // 레스토랑 정보 저장
        restaurantRepository.save(foundModify);
    }


    //완수 북마크 기능에 필요
    public Restaurant findByRestaurantCode(int restaurantCode) {
        return restaurantRepository.findByRestaurantCode(restaurantCode);
    }

    public List<Restaurant> findAll() {
        return restaurantRepository.findAll();
    }

    public RestaurantForBookmarkDTO convertToRestaurantForBookmarkDTO(Restaurant restaurant) {
        RestaurantForBookmarkDTO dto = new RestaurantForBookmarkDTO();
        dto.setRestaurantCode(restaurant.getRestaurantCode());
        dto.setRestaurantName(restaurant.getRestaurantName());
        dto.setMainMenu(restaurant.getMainMenu());
        dto.setRestaurantLocation(restaurant.getRestaurantLocation());
        dto.setRestaurantMenus(
                restaurant.getMenus().stream()
                        .map(menu -> new MenuDTO(menu.getMenuCode(), menu.getMenuName(), menu.getMenuPrice(), restaurant))
                        .collect(Collectors.toList())
        );
        // 변환: restaurant 엔티티에 있는 키워드들을 RestaurantKeywordDTO 리스트로 매핑
        dto.setRestaurantKeywords(
                restaurant.getRestaurantKeywords().stream()
                        .map(keyword -> new RestaurantKeywordDTO(
                                keyword.getRestaurantKeywordCode(),
                                keyword.getRestaurantCode().getRestaurantCode(),
                                keyword.getRestaurantKeyword()))
                        .collect(Collectors.toList())
        );
        return dto;
    }
    //완수 끝
}
