var Rect = function(obj){
  this.show = function(ctx){
    if (obj.color) {
      ctx.fillStyle=obj.color;
    }
    ctx.fillRect(obj.x, obj.y, obj.w, obj.h);
    if (obj.pick||obj.drag){
      if (isIn()){
        ctx.strokeRect(obj.x, obj.y, obj.w, obj.h);
        if (isDrag&&obj.drag){
          obj.x = mx-obj.w/2; obj.y = my-obj.h/2;
        }
      }
    }
  }
  this.isIn(){
    return (mx>obj.x && mx<obj.x+obj.w && my>obj.y && my<obj.y+obj.h)
  }
}

var Circle = function(obj){
  this.show = function(ctx){
    if (obj.color){
      ctx.strokeStyle=obj.color;
    }
    ctx.beginPath();
    ctx.arc(obj.x, obj.y, obj.r, 0, 2*Math.PI);
    ctx.stroke();
    if (obj.pick||obj.drag){
      if (isIn()){
        ctx.fill();
        if (isDrag&&obj.drag){
          obj.x = mx-obj.w/2; obj.y = my-obj.h/2;
        }
      }
    }
  }
  this.isIn(){
    var dx = mx-obj.x, dy = my-obj.y;
    return (dx*dx+dy*dy<obj.r*obj.r);
  }
}
var MMap=new Object();
MMap["circle"]=Circle;
MMap["rect"]=Rect;
