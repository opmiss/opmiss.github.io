var view = {hlat:90, hlon:60, zoomlevel:1, focus:{lat:0, lon:0}}

function translateView(px, py, mx, my){
    view.focus.lat = addlat(view.focus.lat, (my-py)/width*view.hlat*2);
    view.focus.lon = addlon(view.focus.lon, (px-mx)/width*view.hlon*2);
}
function zoomInView(){
    if (view.zoomLevel>18) return;
    view.hlat/=2;
    view.hlon/=2;
    view.zoomLevel++;
}
function zoomOutView(){
    if (view.zoomLevel<2) return;
    view.hlat*=2;
    view.hlon*=2;
    view.zoomLevel--;
}

/*var addlat = function(lat1, lat2){
    var l = lat1+lat2;
    if (l<-180) l+=360;
    else if (l>180) l-=360;
    return l;
}

var addlon = function(lon1, lon2){
    var l = lon1+lon2;
    if (l>90) l=90;
    else if (l<-90) l=-90;
    return l;
}*/

var addlat = function(lat1, lat2){
    var l = lat1+lat2;
    if (l>90) l=90;
    else if (l<-90) l=-90;
    return l;
}

var addlon = function(lon1, lon2){
    var l = lon1+lon2;
    if(l<-180) l+=360;
    else if (l>180) l-=360;
    return l;
}

/*var isIn(loc, elat, dlat, elon, dlon){
    var slat = addlat(elat, -loc.lat);
    if (slat<0||slat>dlat) return false;
    var slon = addlon(elon, -loc.lon);
    if (slon<0||slon>dlon) return false;
    return true;
}*/

var isIn = function(loc, focus, hlat, hlon){
    var slat = addlat(focus.lat, -loc.lat);
    if (Math.abs(slat)>hlat) return false;
    var slon = addlon(focus.lon, -loc.lon);
    if (Math.abs(slon)>hlon) return false;
    return true;
}

var loc = function(lat, lon){
  this.lat = lat;
  this.lon = lon;
  //this.proj = function(focus, spanlat, spanlon, width, height){
    //project this point to canvas
  //}
  return this;
}

var vec = function(loc1, loc2){
   this.dlat=addlat(loc2.lat, -loc1.lat);
   this.dlon=addlon(loc2.lon, -loc1.lon);
   return this;
}

var makeVec = function(dlat, dlon){
   this.dlat = dlat;
   this.dlon = dlon;
   return this;
}

var left = function(v){
  return makeVec(-v.dlon, v.dlat);
}

var dis = function(loc1, loc2){
  var v = vec(loc1, loc2);
  return Math.sqrt((v.dlat*v.dlat)+(v.dlon*v.dlon));
}

var mid = function(p1, p2){
  var v = vec(p1, p2);
  var lat = addlat(p1.lat, v.dlat/2);
  var lon = addlon(p1.lon, v.dlon/2);
  return loc(lat, lon);
}

var intersect = function(p1, v1, p2, v2){
  var a = v2.dlat*v1.dlon - v1.dlat*v2.dlon;
  if (a===0) return null;
  var b = p2.lat*v1.dlon - p2.lon*v1.dlat;
  var c = p1.lat*v1.dlon - p1.lon*v1.dlat;
  var x = (c-b)/a;
  return loc(p2.lat+x*v2.dlat, p2.lon+x*v2.dlon);
}

var center = function(p1, p2, p3){
  //compute the circum center of the triangle
  var p1 = mid(p1, p2);
  var v1 = left(vec(p1, p2));
  var p2 = mid(p2, p3);
  var v2 = left(vec(p2, p3));
  return intersect(p1, v1, p2, v2);
}

