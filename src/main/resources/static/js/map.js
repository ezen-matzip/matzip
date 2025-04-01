
let geocoder = new kakao.maps.services.Geocoder();

let callback = function(result, status) {
    // 정상적으로 검색이 완료됐으면
    // eslint-disable-next-line no-undef
    if (status === kakao.maps.services.Status.OK) {
        // eslint-disable-next-line no-undef
        const coords = new kakao.maps.LatLng(result[0].y, result[0].x);

        // 결과값으로 받은 위치를 마커로 표시합니다
        // eslint-disable-next-line no-undef
        const marker = new kakao.maps.Marker({
            map: map,
            position: coords
        });

        // 지도의 중심을 결과값으로 받은 위치로 이동시킵니다
        map.setCenter(coords);
        console.log(marker);

    }
};
geocoder.addressSearch(restLoc, callback);

let container = document.getElementById('map');
let options = {
    center: new kakao.maps.LatLng(0,0),
    level: 3
};

let map = new kakao.maps.Map(container, options);


if (navigator.geolocation) {

    // GeoLocation을 이용해서 접속 위치를 얻어옵니다
    navigator.geolocation.getCurrentPosition(function(position) {

        var lat = position.coords.latitude, // 위도
            lon = position.coords.longitude; // 경도

        var locPosition = new kakao.maps.LatLng(lat, lon), // 마커가 표시될 위치를 geolocation으로 얻어온 좌표로 생성합니다
            message = '<div style="padding:5px;">여기에 계신가요?!</div>'; // 인포윈도우에 표시될 내용입니다

        // 마커와 인포윈도우를 표시합니다
        displayMarker(locPosition, message);

    });

} else { // HTML5의 GeoLocation을 사용할 수 없을때 마커 표시 위치와 인포윈도우 내용을 설정합니다

    var locPosition = new kakao.maps.LatLng(33.450701, 126.570667),
        message = 'geolocation을 사용할수 없어요..'

    displayMarker(locPosition, message);
}