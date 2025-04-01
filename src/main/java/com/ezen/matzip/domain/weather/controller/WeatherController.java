package com.ezen.matzip.domain.weather.controller;

<<<<<<< HEAD
public class WeatherController {
=======
import com.ezen.matzip.domain.weather.dto.KeywordDTO;
import com.ezen.matzip.domain.weather.entity.Weather;
import com.ezen.matzip.domain.weather.service.WeatherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping
@RequiredArgsConstructor
public class WeatherController {
    private final WeatherService weatherService;

    @PostMapping("/weather")
    @ResponseBody
    public List<KeywordDTO> getWeatherKeywords(@RequestBody WeatherRequest request) {
        List<KeywordDTO> weatherKeywords = weatherService.findKeywordsByWeatherCondition(request.getWeatherKeyword());
        return weatherKeywords;
    }

    @PostMapping("/weather/hashtags")
    @ResponseBody
    public String getWeatherCondition(@RequestBody WeatherRequest request)
    {
        String weatherConditionHashtags = weatherService.weatherKeyword(request.getWeatherKeyword());
        System.out.println("잘 되고 있나?: " + weatherConditionHashtags);
        return weatherConditionHashtags;
    }

    static class WeatherRequest {
        private String weatherKeyword;

        // getter, setter
        public String getWeatherKeyword() {
            return weatherKeyword;
        }

        public void setWeatherKeyword(String weatherKeyword) {
            this.weatherKeyword = weatherKeyword;
        }
    }

//    @PostMapping("/weather")
//    public String getWeatherKeyword(@RequestParam String weatherKeyword, Model model)
//    {
//        System.out.println("실행되는지 테스트하기");
//
//        List<KeywordDTO> keywords = weatherService.findKeywordsByWeatherCondition(weatherKeyword);
//        model.addAttribute("recommendKeywords", keywords);
//        return "main/main";
//    }
>>>>>>> a9292fb8e498916dfeb81cd7c6ea8e23c2f1c68f
}

//weather/ → 날씨 기반 추천 기능

//Controller → 클라이언트 요청을 처리
//Service → 비즈니스 로직을 처리
//Repository → 데이터베이스 CRUD 처리
//DTO → 클라이언트와 데이터를 주고받을 때 사용하는 객체
//Entity → 실제 데이터베이스 테이블과 매칭되는 객체
