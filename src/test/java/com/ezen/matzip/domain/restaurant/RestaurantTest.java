package com.ezen.matzip.domain.restaurant;

import com.ezen.matzip.domain.restaurant.repository.MenuRepository;
import com.ezen.matzip.domain.restaurant.repository.RestaurantRepository;
import com.ezen.matzip.domain.restaurant.repository.KeywordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class RestaurantTest {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private KeywordRepository keywordRepository;


    @DisplayName("메뉴 테스트")
    @Test
    public void menuTest()
    {
        List<Object[]> results = menuRepository.findRestaurantAndScoreByMenuName("연어");

        for (Object[] result : results)
        {
            System.out.println("rest: " + result[0]);
            System.out.println("score: " + result[1]);
        }
    }

    @DisplayName("레스토랑 테스트")
    @Test
    public void restTest()
    {
        List<Object[]> results = restaurantRepository.findRestaurantsByKeywordWithScore("스시");

        for (Object[] result : results)
        {
            System.out.println("rest: " + result[0]);
            System.out.println("score: " + result[1]);
        }

//        Map<Integer, Long> resultMap = new HashMap<>();
//        for (Object[] result : results) {
//            Integer restaurant = result[0].getRestaurantCode();
//            Long score = (Long) result[1];
//            resultMap.put(restaurant, score);
//        }
//
//        resultMap.forEach((key, value) -> {
//            System.out.println("key: " + key + " value: " + value);
//        });

    }

    @DisplayName("키워드 점수 테스트")
    @Test
    public void keywTest()
    {
        List<Object[]> results = keywordRepository.findRestaurantAndScoreByRestaurantKeyword("가성비");

        for (Object[] result : results)
        {
            System.out.println("rest: " + result[0]);
            System.out.println("score: " + result[1]);
        }


    }



}
