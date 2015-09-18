function Topic() {
	if (arguments.length==2) {this.x=arguments[0]; this.y=arguments[1];}
    else {this.x = 0; this.y = 0;}
	this.node=null; 
}
Topic.prototype.resetNode = function() {
	this.node.setAttributeNS(null, 'transform', 'translate(' + 0 + ',' + 0 + ')');
}
function resetTopics(){
	for (var i=0; i<nt; i++){
		T[i].resetNode(); 
	}
	updateDocs(); 
}
function Doc() {
	this.w = new Array(); 
	this.x = 0; this.y = 0;
	this.dx=0; this.dy=0; 
	if (arguments.length==nt && T.length==nt) {
		for(var i=0; i<nt; i++) {
			this.w[i]= arguments[i];
			this.x += this.w[i]*T[i].x; 
			this.y += this.w[i]*T[i].y;
		}
	}		
	else for(var i=0; i<nt; i++) {
		alert("invalid arguments or topics not initialized !"); 
		this.w[i]= 0; 
	}
	this.node=null; 
}
Doc.prototype.updateNode = function() {
	var tx =0; var ty=0; 
	for (var i=0; i<nt; i++){
		var transMatrix = T[i].node.getCTM();
		tx += Number(this.w[i] * transMatrix.e);
        ty += Number(this.w[i] * transMatrix.f);
	}
	this.x= Number(this.node.getAttribute('cx'))+tx; 
	this.y= Number(this.node.getAttribute('cy'))+ty; 
	this.node.setAttributeNS(null, 'transform', 'translate(' + tx + ',' + ty + ')');
}
function updateDocs(){
	for (var i=0; i<nd; i++){
		D[i].updateNode(); 
	}
	if (po) iterate(); 
}
function initTopics(){ 
	T[0] = new Topic(100, 100); T[1] = new Topic(500, 100); T[2] = new Topic(500, 500); T[3] = new Topic(100, 500); 
}
function addTopics(evt){ 
	svgDocument = evt.target.ownerDocument;
	var top = svgDocument.getElementById('topics');
	if (T.length!=nt) alert("topics not initialized!"); 
	else for (var i=0; i<nt; i++){ 
		var node=svgDocument.createElementNS(svgns,"rect");  	
		node.setAttributeNS(null,"x",T[i].x-tr/2);
		node.setAttributeNS(null,"y",T[i].y-tr/2);
		node.setAttributeNS(null,"width",tr);
		node.setAttributeNS(null,"height",tr);
		node.setAttributeNS(null,"style",t_stl);
		T[i].node = node; 
		top.appendChild(T[i].node);
	}
}
function addDocs(evt){
	svgDocument = evt.target.ownerDocument;
	var doc = svgDocument.getElementById('docs');
	if (D.length!=nd) alert("docs not initialized!"); 
	else for (var i=0; i<nd; i++){ 
		var node=svgDocument.createElementNS(svgns,"circle");  	
		node.setAttributeNS(null,"cx",D[i].x);
		node.setAttributeNS(null,"cy",D[i].y);
		node.setAttributeNS(null,"r",dr);
		node.setAttributeNS(null,"style",d_stl);
		D[i].node = node; 
		doc.appendChild(D[i].node);
	}
}
function repelDocs(){
	for (var i=0; i<nd; i++){
		for (var j=i+1; j<nd; j++){
			var dx = D[i].x - D[j].x; 
			var dy = D[i].y - D[j].y; 
			var dx2= dx*dx; 
			var dy2 = dy*dy; 
			var d2 = dx2+dy2; 
			if ( d2 < dr22){
				var d = Math.sqrt(d2); 
				var f = (dr2-d)/dr2; 
				var f=1; 
				var fx = f*dx; var fy = f*dy; 
				if (d==0){
					dx = Math.random()-0.5; dx2= dx*dx; dy = Math.random()-0.5; dy2 = dy*dy;
					fx = Math.sqrt(dx2/(dx2+dy2))*dr2; if (dx<0) fx =-fx; 
					fy = Math.sqrt(dy2/(dx2+dy2))*dr2; if (dy<0) fy =-fy; 
				}
				var mat = D[j].node.getCTM(); 
				var ne = Number(mat.e-fx); 
				var nf = Number(mat.f-fy); 
				D[j].node.setAttributeNS(null, 'transform', 'translate(' + ne + ',' + nf + ')');
				mat = D[i].node.getCTM(); 
				ne = Number(mat.e+fx); 
				nf = Number(mat.f+fy); 
				//D[j].x+=fx; D[j].y+=fy; 
				D[j].node.setAttributeNS(null, 'transform', 'translate(' + ne + ',' + nf + ')');
			}
		}
	}
}
function repel(){
	for (var i=0; i<nd; i++){
		for (var j=i+1; j<nd; j++){
			var dx = D[i].x + D[i].dx - D[j].x - D[j].dx; 
			var dy = D[i].y + D[i].dy - D[j].y - D[j].dy; 
			var dx2= dx*dx; 
			var dy2 = dy*dy; 
			var d2 = dx2+dy2; 
			if ( d2 < dr22){
				var d = Math.sqrt(d2); 
				var f = (dr2-d)/dr2; 
				var fx = f*dx; var fy = f*dy; 
				if (d==0){
					dx = Math.random()-0.5; dx2= dx*dx; dy = Math.random()-0.5; dy2 = dy*dy;
					fx = Math.sqrt(dx2/(dx2+dy2))*dr2; if (dx<0) fx =-fx; 
					fy = Math.sqrt(dy2/(dx2+dy2))*dr2; if (dy<0) fy =-fy; 
				}
				D[j].dx-=fx; D[j].dy-=fy; 
				D[i].dx+=fx; D[i].dx+=fy; 
			}
		}
	}
}
function sink(){
	for (var i=0; i<nd; i++){
		D[i].dx*=0.99;
		D[i].dy*=0.99; 
	}
}
function iterate(){
	for (var i=0; i<10; i++){
		repel(); 
		sink(); 
	}; 
	for (var i=0; i<nd; i++){
		var mat = D[i].node.getCTM(); 
		var ne = Number(mat.e)+ Number(D[i].dx); 
		var nf = Number(mat.f)+ Number(D[i].dy); 
		D[i].node.setAttributeNS(null, 'transform', 'translate(' + ne + ',' + nf + ')');
	}
}
