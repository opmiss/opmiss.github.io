//*************************************************************************************
//**** 2D GEOMETRY CLASSES AND UTILITIES, Jarek Rossignac, REVISED January 2008    ****
//*************************************************************************************

//************************************************************************
//**** POINTS
//************************************************************************
class pt { float x=0,y=0; 
  // CREATE
  pt () {}
  pt (float px, float py) {x = px; y = py;};
  pt (pt P) {x = P.x; y = P.y;};
  pt (pt P, float s, vec V) {x = P.x+s*V.x; y = P.y+s*V.y;};

  // MODIFY
  void setTo(float px, float py) {x = px; y = py;};  
  void setTo(pt P) {x = P.x; y = P.y;}; 
  void setToMouse() { x = mouseX; y = mouseY; }; 
  void moveWithMouse() { x += mouseX-pmouseX; y += mouseY-pmouseY; }; 
  void scaleBy(float f) {x*=f; y*=f;};
  void scaleBy(float u, float v) {x*=u; y*=v;};
  void translateBy(vec V) {x += V.x; y += V.y;};   
  void translateBy(float s, vec V) {x += s*V.x; y += s*V.y;};   
  void translateBy(float u, float v) {x += u; y += v;};
  void translateTowards(float s, pt P) {x+=s*(P.x-x);  y+=s*(P.y-y); };
  void translateToTrack(float s, pt P) {vec V=P.makeVecTo(this).makeUnit(); this.setTo(new pt(P,s,V));};
  void addScaledPt(float s, pt P) {x += s*P.x; y += s*P.y;};        // incorrect notation, but useful for computing weighted averages
  void rotateBy(float a) {float dx=x, dy=y, c=cos(a), s=sin(a); x=c*dx+s*dy; y=-s*dx+c*dy; };     // around origin
  void rotateBy(float a, pt P) {float dx=x-P.x, dy=y-P.y, c=cos(a), s=sin(a); x=P.x+c*dx+s*dy; y=P.y-s*dx+c*dy; };   // around point P
  void rotateBy(float s, float t, pt P) {float dx=x-P.x, dy=y-P.y; dx-=dy*t; dy+=dx*s; dx-=dy*t; x=P.x+dx; y=P.y+dy; };   // s=sin(a); t=tan(a/2);
  void clipToWindow() {x=max(x,0); y=max(y,0); x=min(x,height); y=min(y,height); }
  
  // OUTPUT POINT
  pt makeClone() {return new pt(x,y); };
  pt makeTranslatedBy(vec V) {return(new pt(x + V.x, y + V.y));};
  pt makeTranslatedBy(float s, vec V) {return(new pt(x + s*V.x, y + s*V.y));};
  pt makeTransaltedTowards(float s, pt P) {return(new pt(x + s*(P.x-x), y + s*(P.y-y)));};
  pt makeTranslatedBy(float u, float v) {return(new pt(x + u, y + v));};
  pt makeRotatedBy(float a, pt P) {float dx=x-P.x, dy=y-P.y, c=cos(a), s=sin(a); return(new pt(P.x+c*dx+s*dy, P.y-s*dx+c*dy)); };
  pt makeRotatedBy(float a) {float dx=x, dy=y, c=cos(a), s=sin(a); return(new pt(c*dx+s*dy, -s*dx+c*dy)); };
  pt makeProjectionOnLine(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return(P.makeTransaltedTowards(a/b,Q)); };

   // OUTPUT VEC
  vec makeVecTo(pt P) {return(new vec(P.x-x,P.y-y)); };
  vec makeVecToCenter () {return(new vec(x-height/2.,y-height/2.)); };
  vec makeVecToAverage (pt P, pt Q) {return(new vec((P.x+Q.x)/2.0-x,(P.y+Q.y)/2.0-y)); };
  vec makeVecToAverage (pt P, pt Q, pt R) {return(new vec((P.x+Q.x+R.x)/3.0-x,(P.y+Q.y+R.x)/3.0-y)); };
  vec makeVecToMouse () {return(new vec(mouseX-x,mouseY-y)); };
  vec makeVecToBisectProjection (pt P, pt Q) {float a=this.disTo(P), b=this.disTo(Q);  return(this.makeVecTo(interpolate(P,a/(a+b),Q))); };
  vec makeVecToNormalProjection (pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return(this.makeVecTo(interpolate(P,a/b,Q))); };
 
  // OUTPUT TEST OR MEASURE
  float disTo(pt P) {return(sqrt(sq(P.x-x)+sq(P.y-y))); };
  float disToMouse() {return(sqrt(sq(x-mouseX)+sq(y-mouseY))); };
  boolean isInWindow() {return(((x>0)&&(x<height)&&(y>0)&&(y<height)));};
  boolean projectsBetween(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return((0<a)&&(a<b)); };
  float ratioOfProjectionBetween(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return(a/b); };
  float disToLine(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q).makeUnit().makeTurnedLeft()); return(abs(a)); };
  boolean isLeftOf(pt P, pt Q) {boolean l=dot(P.makeVecTo(this),P.makeVecTo(Q).makeTurnedLeft())>0; return(l);  };
  boolean isInTriangle(pt A, pt B, pt C) { boolean a = this.isLeftOf(B,C); boolean b = this.isLeftOf(C,A); boolean c = this.isLeftOf(A,B); return((a&&b&&c)||(!a&&!b&&!c) );};

  // DRAW , PRINT
  void show() {ellipse(x, y, height/200, height/200); }; // shows point as small dot
  void show(float r) {ellipse(x, y, 2*r, 2*r); }; // shows point as disk of radius r
  void showCross(float r) {line(x-r,y,x+r,y); line(x,y-r,x,y+r);}; 
  void v() {vertex(x,y);};  // used for drawing polygons between beginShape(); and endShape();
  void write() {println("("+x+","+y+")");};  // writes point coordinates in text window
  void showLabel(String s, vec D) {text(s, x+D.x,y+D.y);  };  // show string displaced by vector D from point
  void showLabel(String s) {text(s, x+5,y+4);  };
  void showLabel(int i) {text(str(i), x+5,y+4);  };  // shows integer number next to point
  void showLabel(String s, float u, float v) {text(s, x+u, y+v);  };
  void showSegmentTo (pt P) {line(x,y,P.x,P.y); }; // draws edge to another point
  void to (pt P) {line(x,y,P.x,P.y); }; // draws edge to another point

  } // end of pt class
  
pt mid(pt A, pt B) {return(new pt((A.x+B.x)/2.0,(A.y+B.y)/2.0)); };
pt average(pt A, pt B) {return(new pt((A.x+B.x)/2.0,(A.y+B.y)/2.0)); };
pt average(pt A, pt B, pt C) {return(new pt((A.x+B.x+C.x)/3.0,(A.y+B.y+C.y)/3.0)); };
pt mouse() {return(new pt(mouseX,mouseY));};  // current mouse location
pt pmouse() {return(new pt(pmouseX,pmouseY));};  // previous mouse location
pt mouseInWindow() {float x=mouseX, y=mouseY; x=max(x,0); y=max(y,0); x=min(x,height); y=min(y,height);  return(new pt(x,y));}; // clips mouse to square window
pt screenCenter() {return(new pt(height/2,height/2));}  // point in center of screen
pt interpolate(pt A, float s, pt B) {return(new pt(A.x+s*(B.x-A.x),A.y+s*(B.y-A.y))); };
pt weightedSum(float a, pt A, float b, pt B) {pt P =  A.makeClone(); P.scaleBy(a); P.addScaledPt(b,B); return(P);}
pt weightedSum(float a, pt A, float b, pt B, float c, pt C) {pt P =  A.makeClone(); P.scaleBy(a); P.addScaledPt(b,B);  P.addScaledPt(c,C); return(P);}
pt scaledAverage(pt A, pt B, pt C, float s) {return(new pt((A.x+B.x+C.x)/s,(A.y+B.y+C.y)/s)); };
pt weightedSum(float a, pt A, float b, pt B, float c, pt C, float d, pt D) 
        {pt P =  A.makeClone(); P.scaleBy(a); P.addScaledPt(b,B); P.addScaledPt(c,C); P.addScaledPt(d,D); return(P);}
boolean isLeftTurn(pt A, pt B, pt C) {return(dot(A.makeVecTo(B).makeTurnedLeft() , B.makeVecTo(C) )>0); };  // true if B is a left turn (appears right on the screen, Y goes down)
boolean mouseIsInWindow() {return(((mouseX>0)&&(mouseX<height)&&(mouseY>0)&&(mouseY<height)));};

//************************************************************************
//**** VECTORS
//************************************************************************
class vec { float x=0,y=0; 
 // CREATE
  vec () {};
  vec (vec V) {x = V.x; y = V.y;};
  vec (float s, vec V) {x = s*V.x; y = s*V.y;};
  vec (float px, float py) {x = px; y = py;};
 
 // MODIFY
  void setTo(float px, float py) {x = px; y = py;}; 
  void setTo(pt P, pt Q) {x = Q.x-P.x; y = Q.y-P.y;}; 
  void setTo(vec V) {x = V.x; y = V.y;}; 
  void scaleBy(float f) {x*=f; y*=f;};
  void back() {x=-x; y=-y;};
  void mul(float f) {x*=f; y*=f;};
  void div(float f) {x/=f; y/=f;};
  void scaleBy(float u, float v) {x*=u; y*=v;};
  void normalize() {float n=sqrt(sq(x)+sq(y)); if (n>0.000001) {x/=n; y/=n;};};
  void add(vec V) {x += V.x; y += V.y;};   
  void add(float s, vec V) {x += s*V.x; y += s*V.y;};   
  void add(float u, float v) {x += u; y += v;};
  void turnLeft() {float w=x; x=-y; y=w;};
  void rotateBy (float a) {float xx=x, yy=y; x=xx*cos(a)-yy*sin(a); y=xx*sin(a)+yy*cos(a); };
  
  // OUTPUT VEC
  vec makeClone() {return(new vec(x,y));}; 
  vec makeUnit() {float n=sqrt(sq(x)+sq(y)); if (n<0.000001) n=1; return(new vec(x/n,y/n));}; 
  vec unit() {float n=sqrt(sq(x)+sq(y)); if (n<0.000001) n=1; return(new vec(x/n,y/n));}; 
  vec makeScaledBy(float s) {return(new vec(x*s, y*s));};
  vec makeTurnedLeft() {return(new vec(-y,x));};
  vec left() {return(new vec(-y,x));};
  vec makeOffsetVec(float s, vec V) {return(new vec(x + s*V.x, y + s*V.y));};
  vec makeOffsetVec(float u, float v) {return(new vec(x + u, y + v));};
  vec makeRotatedBy(float a) {return(new vec(x*cos(a)-y*sin(a),x*sin(a)+y*cos(a))); };
  vec makeReflectedVec(vec N) { return makeOffsetVec(-2.*dot(this,N),N);};
  vec makeBack(){ return (new vec(-x, -y));}

  // OUTPUT TEST MEASURE
  float norm() {return(sqrt(sq(x)+sq(y)));}
  boolean isNull() {return((abs(x)+abs(y)<0.000001));}
  float angle() {return(atan2(y,x)); }

  // DRAW, PRINT
  void write() {println("("+x+","+y+")");};
  void showAt (pt P) {line(P.x,P.y,P.x+x,P.y+y); }; 
  void showArrowAt (pt P, float s) {makeScaledBy(s).showArrowAt(P);} 
  void showArrowAt (pt P) {line(P.x,P.y,P.x+x,P.y+y); 
      float n=min(this.norm()/10.,height/50.); 
      pt Q=P.makeTranslatedBy(this); 
      vec U = this.makeUnit().makeScaledBy(-n);
      vec W = U.makeTurnedLeft().makeScaledBy(0.3);
      beginShape(); Q.makeTranslatedBy(U).makeTranslatedBy(W).v(); Q.v(); W.scaleBy(-1); Q.makeTranslatedBy(U).makeTranslatedBy(W).v(); endShape(CLOSE); }; 
  void showArrowAt (pt P, String s) {line(P.x,P.y,P.x+x,P.y+y); 
      float n=min(this.norm()/10.,height/50.); 
      pt Q=P.makeTranslatedBy(this); 
      vec U = this.makeUnit().makeScaledBy(-n);
      vec W = U.makeTurnedLeft().makeScaledBy(0.3);
      beginShape(); Q.makeTranslatedBy(U).makeTranslatedBy(W).v(); Q.v(); W.scaleBy(-1); Q.makeTranslatedBy(U).makeTranslatedBy(W).v(); endShape(CLOSE);
      showLabelAt(s,Q);
       }; 

  void showLabelAt(String s, pt P) {pt Q = P.makeTranslatedBy(0.5,this); Q.showLabel(s); };

  } // end vec class
 
vec mouseDrag() {return new vec(mouseX-pmouseX,mouseY-pmouseY);};
vec average(vec U, vec V) {return(new vec((U.x+V.x)/2.0,(U.y+V.y)/2.0)); };
float dot(vec U, vec V) {return(U.x*V.x+U.y*V.y); };
vec linearInterpolate(vec U, float s, vec V) {return(new vec(U.x+s*(V.x-U.x),U.y+s*(V.y-U.y))); };
vec circularInterpolate(vec U, float s, vec V) {float a = angle(U,V); vec W = U.makeRotatedBy(s*a); return W ; };

//************************************************************************
//**** ANGLES
//************************************************************************
float angle(vec V) {return(atan2(V.y,V.x)); };
float angle(vec U, vec V) {return(atan2(dot(U.makeTurnedLeft(),V),dot(U,V))); };
float mPItoPIangle(float a) { if(a>PI) return(mPItoPIangle(a-2*PI)); if(a<-PI) return(mPItoPIangle(a+2*PI)); return(a);};
float toDeg(float a) {return(a*180/PI);}
float toRad(float a) {return(a*PI/180);}

 //************************************************************************
//**** FRAMES
//************************************************************************
class frame {       // frame [O I J]
  pt O = new pt();
  vec I = new vec(1,0);
  vec J = new vec(0,1);
  frame() {}
  frame(pt pO, vec pI, vec pJ) {O.setTo(pO); I.setTo(pI); J.setTo(pJ);  }
  frame(pt A, pt B, pt C) {O.setTo(B); I=A.makeVecTo(C); I.normalize(); J=I.makeTurnedLeft();}
  frame(pt A, pt B) {O.setTo(A); I=A.makeVecTo(B).makeUnit(); J=I.makeTurnedLeft();}
  frame(pt A, vec V) {O.setTo(A); I=V.makeUnit(); J=I.makeTurnedLeft();}
  frame(float x, float y) {O.setTo(x,y);}
  frame(float x, float y, float a) {O.setTo(x,y); this.rotateBy(a);}
  frame(float a) {this.rotateBy(a);}
  frame makeClone() {return(new frame(O,I,J));}
  void reset() {O.setTo(0,0); I.setTo(1,0); J.setTo(0,1); }
  void setTo(frame F) {O.setTo(F.O); I.setTo(F.I); J.setTo(F.J); }
  void setTo(pt pO, vec pI, vec pJ) {O.setTo(pO); I.setTo(pI); J.setTo(pJ); }
  void show() {float d=height/20; O.show(); I.makeScaledBy(d).showArrowAt(O); J.makeScaledBy(d).showArrowAt(O); }
  void showLabels() {float d=height/20; 
               O.makeTranslatedBy(average(I,J).makeScaledBy(-d/4)).showLabel("O",-3,5); 
               O.makeTranslatedBy(d,I).makeTranslatedBy(-d/5.,J).showLabel("I",-3,5); 
               O.makeTranslatedBy(d,J).makeTranslatedBy(-d/5.,I).showLabel("J",-3,5); 
             }
  void translateBy(vec V) {O.translateBy(V);}
  void translateBy(float x, float y) {O.translateBy(x,y);}
  void rotateBy(float a) {I.rotateBy(a); J.rotateBy(a); }
  frame makeTranslatedBy(vec V) {frame F = this.makeClone(); F.translateBy(V); return(F);}
  frame makeTranslatedBy(float x, float y) {frame F = this.makeClone(); F.translateBy(x,y); return(F); }
  frame makeRotatedBy(float a) {frame F = this.makeClone(); F.rotateBy(a); return(F); }
   
  float angle() {return(I.angle());}
  void apply() {translate(O.x,O.y); rotate(angle());}  // rigid body tansform, use between pushMatrix(); and popMatrix();
  void moveTowards(frame B, float s) {O.translateTowards(s,B.O); rotateBy(s*(B.angle()-angle()));}  // for chasing or interpolating frames
  } // end frame class
 
frame makeMidEdgeFrame(pt A, pt B) {return(new frame(average(A,B),A.makeVecTo(B)));}  // creates frame for edge

frame interpolate(frame A, float s, frame B) {   // creates a frame that is a linear interpolation between two other frames
    frame F = A.makeClone(); F.O.translateTowards(s,B.O); F.rotateBy(s*(B.angle()-A.angle()));
    return(F);
    }

frame twist(frame A, float s, frame B) {   // a circular interpolation
  float d=A.O.disTo(B.O);
  float b=mPItoPIangle(angle(A.I,B.I));
  frame F = A.makeClone(); F.rotateBy(s*b);
  pt M = average(A.O,B.O);   
  if ((abs(b)<0.000001) || (abs(b-PI)<0.000001)) F.O.translateTowards(s,B.O); 
  else {
  float h=d/2/tan(b/2); //else print("/b");
     vec W = A.O.makeVecTo(B.O); W.normalize();
     vec L = W.makeTurnedLeft();   L.scaleBy(h);
     M.translateBy(L);  // fill(0); M.show(6);
     L.scaleBy(-1);  L.normalize(); 
     if (abs(h)>=0.000001) L.scaleBy(abs(h+sq(d)/4/h)); //else print("/h");
     pt N = M.makeClone(); N.translateBy(L);  
     F.O.rotateBy(-s*b,M);
     };   
  return(F);
  }
  
//************************************************************************
//**** EDGES, RAYS, LINES
//************************************************************************
boolean edgesIntersect(pt A, pt B, pt C, pt D) {boolean hit=true; 
    if (isLeftTurn(A,B,C)==isLeftTurn(A,B,D)) hit=false; 
    if (isLeftTurn(C,D,A)==isLeftTurn(C,D,B)) hit=false; 
     return hit; }
     
//************************************************************************
//**** TRIANGLES
//************************************************************************


//************************************************************************
//**** INTEGRAL PRPERTIES, AREA, CENTER
//************************************************************************
float trapezeArea(pt A, pt B) {return((B.x-A.x)*(B.y+A.y)/2.);}
pt trapezeCenter(pt A, pt B) { return(new pt(A.x+(B.x-A.x)*(A.y+2*B.y)/(A.y+B.y)/3., (A.y*A.y+A.y*B.y+B.y*B.y)/(A.y+B.y)/3.) ); }

//************************************************************************
//**** CIRCLES
//************************************************************************
pt circumCenter (pt A, pt B, pt C) {    // computes the center of a circumscirbing circle to triangle (A,B,C)
  vec AB =  A.makeVecTo(B);  float ab2 = dot(AB,AB);
  vec AC =  A.makeVecTo(C); AC.turnLeft();  float ac2 = dot(AC,AC);
  float d = 2*dot(AB,AC);
  AB.turnLeft();
  AB.scaleBy(-ac2); 
  AC.scaleBy(ab2);
  AB.add(AC);
  AB.scaleBy(1./d);
  pt X =  A.makeClone();
  X.translateBy(AB);
  return(X);
  };

void showArcThrough (pt A, pt B, pt C) {
   pt O = circumCenter ( A,  B,  C);
   float r=2.*(O.disTo(A) + O.disTo(B)+ O.disTo(C))/3.;
   float a = O.makeVecTo(A).angle(); average(O,A).showLabel(str(toDeg(a)));
   float c = O.makeVecTo(C).angle(); average(O,C).showLabel(str(toDeg(c)));
   if(isLeftTurn(A,B,C)) arc(O.x,O.y,r,r,a,c); else arc(O.x,O.y,r,r,c,a);
    }

float radius (pt A, pt B, pt C) {
    float a=B.disTo(C);     float b=C.disTo(A);     float c=A.disTo(B);
    float s=(a+b+c)/2;     float d=sqrt(s*(s-a)*(s-b)*(s-c));   float r=a*b*c/4/d;
    return (r);
   };
   
//************************************************************************
//**** CURVES
//************************************************************************
pt s(pt A, float s, pt B) {return(new pt(A.x+s*(B.x-A.x),A.y+s*(B.y-A.y))); };
pt b(pt A, pt B, pt C, float s) {return( s(s(B,s/4.,A),0.5,s(B,s/4.,C))); };                          // returns a tucked B towards its neighbors
pt f(pt A, pt B, pt C, pt D, float s) {return( s(s(A,1.+(1.-s)/8.,B) ,0.5, s(D,1.+(1.-s)/8.,C))); };    // returns a bulged mid-edge point 

pt cubicBezier(pt A, pt B, pt C, pt D, float t) {return( s( s( s(A,t,B) ,t, s(B,t,C) ) ,t, s( s(B,t,C) ,t, s(C,t,D) ) ) ); }
void splitBezier(pt A, pt B, pt C, pt D, int rec) {
  if (rec==0) {B.v(); C.v(); D.v(); return;};
  pt E=mid(A,B);   pt F=mid(B,C);   pt G=mid(C,D);  
           pt H=mid(E,F);   pt I=mid(F,G);  
                    pt J=mid(H,I); J.show(3);   
  splitBezier(A,E,H,J,rec-1);   splitBezier(J,I,G,D,rec-1); 
 }
 
void drawSplitBezier(pt A, pt B, pt C, pt D, float t) {
  pt E=s(A,t,B); E.show(2);  pt F=s(B,t,C); F.show(2);  pt G=s(C,t,D); G.show(2);  E.to(F); F.to(G);
           pt H=s(E,t,F); H.show(2);   pt I=s(F,t,G);  I.show(2); H.to(I);
                    pt J=s(H,t,I); J.show(4);   
 }

void drawCubicBezier(pt A, pt B, pt C, pt D) { beginShape();  for (float t=0; t<=1; t+=0.02) {cubicBezier(A,B,C,D,t).v(); };  endShape(); }
float cubicBezierAngle (pt A, pt B, pt C, pt D, float t) {pt P = s(s(A,t,B),t,s(B,t,C)); pt Q = s(s(B,t,C),t,s(C,t,D)); vec V=P.makeVecTo(Q); float a=atan2(V.y,V.x); return(a);}  
vec cubicBezierTangent (pt A, pt B, pt C, pt D, float t) {pt P = s(s(A,t,B),t,s(B,t,C)); pt Q = s(s(B,t,C),t,s(C,t,D)); vec V=P.makeVecTo(Q); V.makeUnit(); return(V);}  

void drawParabolaInHat(pt A, pt B, pt C, int rec) {
   if (rec==0) { B.showSegmentTo(A); B.showSegmentTo(C); } 
   else { 
     float w = (A.makeVecTo(B).norm()+C.makeVecTo(B).norm())/2;
     float l = A.makeVecTo(C).norm()/2;
     float t = l/(w+l);
     pt L = new pt(A); 
     L.translateBy(t,A.makeVecTo(B)); 
     pt R = new pt(C); R.translateBy(t,C.makeVecTo(B)); 
     pt M = average(L,R);
     drawParabolaInHat(A,L, M,rec-1); drawParabolaInHat(M,R, C,rec-1); 
     };
   };

