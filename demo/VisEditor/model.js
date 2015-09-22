var Rect = function(obj){
  this.show = function(ctx){
    ctx.fillRect(obj.x, obj.y, obj.w, obj.h);
  }
}

var Circle = function(obj){
  this.show = function(ctx){
    ctx.beginPath();
    ctx.arc(obj.x, obj.y, obj.r, 0, 2*Math.PI);
    ctx.stroke(); 
  }
}
var MMap=new Object();
MMap["circle"]=Circle;
MMap["rect"]=Rect;
