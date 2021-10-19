

var main = {
    init : function () {
        var _this = this;
        _this.getUserLocation();
    },

    getUserLocation : function () {
        if('geolocation' in navigator) {
            navigator.geolocation.getCurrentPosition((position => {
                var map = new naver.maps.Map("map", {
                    center: new naver.maps.LatLng(position.coords.latitude, position.coords.longitude),
                    zoom: 16
                })
                var latlng = map.getCenter();
                var utmk = naver.maps.TransCoord.fromLatLngToUTMK(latlng);
                utmk.x = parseFloat(utmk.x.toFixed(1));
                utmk.y = parseFloat(utmk.y.toFixed(1));
                document.cookie = "location=" + utmk.x + "|" + utmk.y;
            }));
        }
        else {
            alert('위치 정보를 가져올 수 없어요.')
        }
    }
};

main.init();
