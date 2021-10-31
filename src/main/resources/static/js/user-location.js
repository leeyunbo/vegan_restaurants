var main = {
    init : function () {
        var _this = this;
        _this.getLocation();
    },

    getLocation : function () {
        if('geolocation' in navigator) {
            navigator.geolocation.getCurrentPosition((position => {

                Proj4js.reportError = function(msg) { alert(msg); }
                Proj4js.defs['GRS80'] = '+proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs';
                Proj4js.defs['WGS84'] ='+proj=longlat +ellps=WGS84 +datum=WGS84 +no_defs';

                var GRS80 = new Proj4js.Proj('GRS80');
                var WGS84 = new Proj4js.Proj('WGS84');

                var utmk = new Proj4js.Point(position.coords.longitude, position.coords.latitude);
                Proj4js.transform(WGS84, GRS80, utmk);

                document.cookie = "location=" + utmk.x + "|" + utmk.y;
            }));
        }
        else {
            alert('위치 정보를 가져올 수 없습니다.');
        }
    },
};

main.init();
