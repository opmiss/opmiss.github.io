function Init(evt)
{
	initTopics(); 
	addTopics(evt); 
	initDocs(); 
	addDocs(evt);
	SVGDocument = evt.target.ownerDocument;
	SVGRoot = SVGDocument.documentElement;
    TrueCoords = SVGRoot.createSVGPoint();
	GrabPoint = SVGRoot.createSVGPoint();
    Fix = SVGDocument.getElementById('Fix');
}
function initDocs(){
	D[nd++] = new Doc(0.0, 0.0, 0.0, 1.0); D[nd++] = new Doc(0.0, 0.16, 0.67, 0.16); 
	D[nd++] = new Doc(0.0, 0.8, 0.2, 0.0); D[nd++] = new Doc(0.0, 1.0, 0.0, 0.0); 
	D[nd++] = new Doc(0.0, 1.0, 0.0, 0.0); D[nd++] = new Doc(0.33, 0.33, 0.33, 0.0); 
	D[nd++] = new Doc(0.0, 0.857, 0.0, 0.143); D[nd++] = new Doc(0.0, 1.0, 0.0, 0.0); 
	D[nd++] = new Doc(0.0, 1.0, 0.0, 0.0); D[nd++] = new Doc(0.0, 0.82, 0.0, 0.18); 
	D[nd++] = new Doc(1.0, 0.0, 0.0, 0.0); D[nd++] = new Doc(0.0, 0.67, 0.0, 0.33); 
	D[nd++] = new Doc(0.0, 0.5, 0.0, 0.5); D[nd++] = new Doc(0.0, 1.0, 0.0, 0.0); 
	D[nd++] = new Doc(0.0, 0.857, 0.143, 0.0); D[nd++] = new Doc(0.0, 0.0, 0.0, 1.0); 
	D[nd++] = new Doc(0.154, 0.077, 0.615, 0.154); D[nd++] = new Doc(0.0, 0.5, 0.0, 0.5); 
	D[nd++] = new Doc(0.0, 0.0, 0.5, 0.5); 
	D[nd++] = new Doc(0.0, 1.0, 0.0, 0.0); D[nd++] = new Doc(0.0, 1.0, 0.0, 0.0); 
	D[nd++] = new Doc(1.0, 0.0, 0.0, 0.0); D[nd++] = new Doc(0.428, 0.0, 0.429, 0.143); 
	D[nd++] = new Doc(0.0, 0.5, 0.357, 0.143); D[nd++] = new Doc(0.0, 1.0, 0.0, 0.0); 
}
function Grab(evt)
{
    var targetElement = evt.target;
	if (targetElement.parentNode.id!='fix')
	{
        DragTarget = targetElement;
        DragTarget.setAttributeNS(null, 'pointer-events', 'none');
        var transMatrix = DragTarget.getCTM();
        GrabPoint.x = TrueCoords.x - Number(transMatrix.e);
        GrabPoint.y = TrueCoords.y - Number(transMatrix.f);
	}
}
function Drag(evt)
{
	GetTrueCoords(evt);
	if (DragTarget)
    {
        var newX = TrueCoords.x - GrabPoint.x;
        var newY = TrueCoords.y - GrabPoint.y;
        DragTarget.setAttributeNS(null, 'transform', 'translate(' + newX + ',' + newY + ')');
		if (DragTarget.parentNode.id=='topics'){
			updateDocs(); 
		}; 
    }
};
function Drop(evt)
{
    if ( DragTarget )
    {
        DragTarget.setAttributeNS(null, 'pointer-events', 'all');
        DragTarget = null;
    }
};
function GetTrueCoords(evt)
{
    var newScale = SVGRoot.currentScale;
    var translation = SVGRoot.currentTranslate;
    TrueCoords.x = (evt.clientX - translation.x)/newScale;
    TrueCoords.y = (evt.clientY - translation.y)/newScale;
};
function toggle(){
	po=!po; 
	updateDocs(); 
}
