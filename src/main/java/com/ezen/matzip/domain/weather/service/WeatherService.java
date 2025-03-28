package com.ezen.matzip.domain.weather.service;

import com.ezen.matzip.domain.weather.dto.KeywordDTO;
import com.ezen.matzip.domain.weather.entity.Keyword;
import com.ezen.matzip.domain.weather.entity.Weather;
import com.ezen.matzip.domain.weather.repository.KeywordRepository;
import com.ezen.matzip.domain.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final KeywordRepository keywordRepository;
    private final ModelMapper modelMapper;

    public List<KeywordDTO> findKeywordsByWeatherCondition(String weatherCondition) {
        Weather weather = weatherRepository.findByWeatherCondition(weatherCondition);
        List<Keyword> keywords = keywordRepository.findByWeatherCode(weather.getWeatherCode());

        List<KeywordDTO> result = new ArrayList<>();
        List<Integer> rand = new ArrayList<>();

        while(rand.size() < 5)
        {
            int num = (int) (Math.random() * keywords.size());
            if(!rand.contains(num))
            {
                rand.add(num);
                Keyword keyword = keywords.get(num);
                result.add(modelMapper.map(keyword, KeywordDTO.class));
            }
        }

        return result;
    }
}