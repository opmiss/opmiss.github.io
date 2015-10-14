var isDrag = false, isZoomIn = false, isZoomOut = false;
var px, py, mx, my;
function mouseDown(e){
    if (isZoomIn) {
        zoomInView();
        draw();
    } else if (isZoomOut){
        zoomOutView();
        draw();
    } else { isDrag=true;}
}
function mouseUp(e){
    isDrag=false;
}
function mouseMove(e){
    px = mx; py = my;
    getMouse(e);
    if (isDrag){
        translateView(px, py, mx, my);
        draw();
    }else {
        drawZoomTool();
    }
}
function getMouse(e){
    mx = e.clientX;
    my = e.clientY;
}
