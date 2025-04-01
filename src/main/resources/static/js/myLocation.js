let geocoder = new kakao.maps.services.Geocoder();
function myLocation() {
    navigator.geolocation.getCurrentPosition(function (position) {

        // position은 현재 사용자의 위치 정보를 가지고 있음
        // 이 정보를 kakao.maps.LatLng 객체로 변환해야 함
        let coords = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
        // 변환된 좌표로 상세 주소 검색
        searchDetailAddrFromCoords(coords, function (result, status) {
            if (status === kakao.maps.services.Status.OK) {

                const form = document.querySelector('form[id="findByLoc"]');
                const locationInput = form.querySelector('input[name="keyword"]');
                locationInput.value = result[0].road_address.road_name;
                form.submit();
            }
        });
    });
}

function searchDetailAddrFromCoords(coords, callback) {
    // 좌표로 법정동 상세 주소 정보를 요청합니다
    geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
}

window.MyApp = window.MyApp || {};


function myLocation2() {
    navigator.geolocation.getCurrentPosition(function (position) {
        // position은 현재 사용자의 위치 정보를 가지고 있음
        // 이 정보를 kakao.maps.LatLng 객체로 변환해야 함
        MyApp.currentCoords = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude);
        // 변환된 좌표로 상세 주소 검색

        const locationReadyEvent = new CustomEvent('locationReady', {
            detail: MyApp.currentCoords
        });
        // console.log(locationReadyEvent);
        document.dispatchEvent(locationReadyEvent);
    });
}

