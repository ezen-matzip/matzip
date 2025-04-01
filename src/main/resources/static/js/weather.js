async function currentWeather() {
    myLocation2();
    document.addEventListener('locationReady', async function(event) {
        MyApp.currentCoords = event.detail;
        // console.log(event.detail);
        const API_KEY = '03b12504013ff688dca9c9c6e11188cd';
        const lat = parseFloat(MyApp.currentCoords.Ma.toFixed(2));
        const lon = parseFloat(MyApp.currentCoords.La.toFixed(2));
        const apiURL = `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&appid=${API_KEY}&units=metric`;

        try {
            const response = await axios.get(apiURL);
            const weatherKeyword = response.data.weather[0].main;  // 현재 날씨 정보

            const weatherInputValue = document.querySelector('input[name="recommendKeywords"]');
            const weatherInputValue2 = document.querySelector('input[name="weatherCondition"]');
            weatherInputValue.value = weatherKeyword;
            weatherInputValue2.value = weatherKeyword;

            $.ajax({
                type: "POST",
                url: "/weather/hashtags",
                contentType: "application/json",
                dataType: "text",
                data: JSON.stringify({ weatherKeyword: weatherKeyword }),
                beforeSend: function(xhr) {
                    const csrfToken = $("meta[name='_csrf']").attr("content");
                    const csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (data) {
                    // console.log(data);
                    // console.log("이것은 해시태그 데이터: " + data);
                    let hashtags = "";
                    hashtags += data;
                    $('p[id="weatherHashtags"]').text(hashtags);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.error("음식 추천을 불러오지 못했습니다.");
                    console.error("상태:", textStatus);
                    console.error("오류:", errorThrown);
                    console.error("응답:", jqXHR.responseText);
                }
            });

            $.ajax({
                type: "POST",
                url: "/weather",
                contentType: "application/json",
                dataType: "json",
                data: JSON.stringify({ weatherKeyword: weatherKeyword }),
                beforeSend: function(xhr) {
                    const csrfToken = $("meta[name='_csrf']").attr("content");
                    const csrfHeader = $("meta[name='_csrf_header']").attr("content");
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                success: function (data) {
                    let foodListHtml = "";
                    data.forEach(food => {
                        foodListHtml += `
                <form action="/restaurant/search">
                <div class="food-item">
                    <button style="text-decoration: none; color: inherit; display: block; cursor: pointer; background: none;" name="keyword" type="submit">
                        <img src="${food.keywordImgPath}" alt="${food.keyword}" width="100">
                        <p>${food.keyword}</p>
                        <input type="hidden" name="keyword" value="${food.keyword}">
                    </button>
                </div>
                </form>`;
                    });
                    $(".food-list").html(foodListHtml);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.error("음식 추천을 불러오지 못했습니다.");
                    console.error("상태:", textStatus);
                    console.error("오류:", errorThrown);
                    console.error("응답:", jqXHR.responseText);
                }
            });



        } catch (error) {
            console.error("날씨 정보를 가져오는 데 실패했습니다.", error);
        }
    });
}

