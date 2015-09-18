import processing.core.*; import SpringGUI.*; import java.applet.*; import java.awt.*; import java.awt.image.*; import java.awt.event.*; import java.io.*; import java.net.*; import java.text.*; import java.util.*; import java.util.zip.*; import javax.sound.midi.*; import javax.sound.midi.spi.*; import javax.sound.sampled.*; import javax.sound.sampled.spi.*; import java.util.regex.*; import javax.xml.parsers.*; import javax.xml.transform.*; import javax.xml.transform.dom.*; import javax.xml.transform.sax.*; import javax.xml.transform.stream.*; import org.xml.sax.*; import org.xml.sax.ext.*; import org.xml.sax.helpers.*; public class FishTank extends PApplet {/*------------------------------- 
   Virtual Fish Tank
   Sketched by Tina W. Zhuo
   Feb 2009
---------------------------------*/


SpringGUI gui; 

public void setup(){
  size(800,600);
  frameRate(30); 
  gui = new SpringGUI(this); // <--- this line MUST be placed before gui can be used.
  gui.addChoice("myChoice", 100, 500, 180, 40); // create a Choice named "myChoice"
  gui.addItem("myChoice", "Fish"); // add Items to the Choice
  gui.addItem("myChoice", "Jellyfish");
  gui.selectItem("myChoice", "Fish");
  gui.addButton("Fish_B", "Create Swimmer", 300, 500, 180, 40); 
  gui.addButton("Food_B", "Feed fish", 500, 500, 180, 40);
  fishShoal=new DigitalSwimmer[100];
  foodPellet=new pt[100];
  isThere=new boolean[100];
  
}
public void draw(){
  
  /* if (food){
      fill(255,0,0);
       println(food);
      ellipse(mouseX, mouseY, 20,20);
    }*/
 
  background(250,250,250);
  smooth();
  displayShoal();
  displayFood();
  testFishes();
 /*   testfish.generateGait();
    testfish.testBoundary();
    testfish.upDate();
    testfish.Display();*/
}

public void handleEvent(String[] parameters){
  if (parameters[2]=="mouseClicked"){
   if(parameters[1]=="Fish_B") {fish=true; food=false; println("click and drag in the window to create swimmers"); }
   if(parameters[1]=="Food_B") {fish=false; food=true; println("click in the window to place food pelletsp"); }
  }
}


/*------------------------------- 
   Digital Swimmer---Virtual Fish Tank
   Sketched by Tina W. Zhuo
   Feb 2009
---------------------------------*/
class DigitalSwimmer{
  ///////////////////////////////
 //Define the struction of Digital Swimmer
  int N; 
  int excID; //excitation segment id
  Segment[] Skeleton;
  /////////////////////////////
  //state information
  pt Position; 
  vec Velocity;
  vec Force[];
  /////////////////////////////
  //Parameters in Gait Generation Equation
  float fixPhi[];
  float Phi[];
  float T;
  float Omega;//undulating frequency
  float Amplitude; 
  float dPhi;
  ////////////////////////////////
  //initialize the digital swimmer
  DigitalSwimmer(float sx, float sy, float ex, float ey){//
    N=4;
    T=0;
    excID=0; 
    Amplitude=PI/10;
    fixPhi=new float[N]; 
    Phi=new float[N];
    Force=new vec[3]; 
    Force[0]=new vec(0,0); //water resistance
    Force[1]=new vec(0,0); //propulsion force
    Velocity=new vec(0,0);
    Position=new pt(0,0);
    dPhi=PI/N;
    Omega=0.2f; 
    for (int i=0; i<N; i++) fixPhi[i]=0;
    
    Skeleton=new Segment[N];
    Skeleton[excID]=new Segment(new pt(sx, sy), new vec((ex-sx)/2, (ey-sy)/2));
    Position.setTo(new pt(sx,sy));
    for (int i=excID+1; i<N; i++){ 
      Skeleton[i]=new Segment(Skeleton[i-1],0,Skeleton[excID].Length/(N-1));
    }
  }
  public void generateGait(){
    for (int i=1; i<N; i++){
      Phi[i]=fixPhi[i]+Amplitude*sin(T+abs(i-excID)*dPhi);
    }
  }
  public void updateSegments(){
    for (int i=1; i<N; i++){
      Skeleton[i].Change(Skeleton[i-1], Phi[i]); 
    }
  }
  public void computeForce(){
 //   for (int i=0; i<
    
    Force[0]=(Velocity).makeScaledBy(-Velocity.norm()*0.05f); 
    Force[1]=(Skeleton[0].Tail).makeScaledBy(-Omega*0.1f);
  //  println("Resistance"+Force[0].x+", "+Force[0].y);
   // println("Propulsion"+Force[1].x+", "+Force[1].y);
  }
  public void upDate(){
    T=T+Omega;
   //  println("Velocity"+Velocity.x+", "+Velocity.y);
   
    computeForce();
    Velocity.add(Force[0]);
    Velocity.add(Force[1]);
    Position.translateBy(Velocity);
 
    Skeleton[excID].Head.setTo(Position);
    updateSegments();
  }
  
  //action functions
  public void Turn( float deg){
    Skeleton[excID].Tail.rotateBy(deg);
  
  }
  public void Accelerate(float a){
  Omega=Omega+a;
  if (Omega<0.1f) Omega=0.1f; 
  }
  public void Display(){
  for (int i=0; i<N; i++){
    Skeleton[i].Display();
  }
 }
 
 ////////////////////
 public void testBoundary(){
   float sd=abs(Velocity.y)*20;  
   //sd is the safe distance
  // println("Velocity"+Velocity.x+", "+Velocity.y);
  // println("Tail"+Skeleton[excID].Tail.x+", "+Skeleton[excID].Tail.y);
   
   //test four angle
   //1
   if (Skeleton[excID].Head.x<sd && Skeleton[excID].Head.y<sd) {
       if ( Skeleton[excID].Tail.x > Skeleton[excID].Tail.y && Skeleton[excID].Tail.y>0) {
         Turn(-PI/20);
         return;
       }
       if ( Skeleton[excID].Tail.y > Skeleton[excID].Tail.x && Skeleton[excID].Tail.x>0) {
         Turn(PI/20);
         return;
       }
   }
   //2
   if (Skeleton[excID].Head.x<sd && Skeleton[excID].Head.y>height-sd){
     if (Skeleton[excID].Tail.x > - Skeleton[excID].Tail.y && Skeleton[excID].Tail.y<0) {
       Turn(PI/20);
       return;
     }
     if (-Skeleton[excID].Tail.y > Skeleton[excID].Tail.x && Skeleton[excID].Tail.x>0) {
       Turn(-PI/20);
       return;
     }
   }
   //3
   if (Skeleton[excID].Head.x>height-sd && Skeleton[excID].Head.y>height-sd){
     if ( Skeleton[excID].Tail.x < Skeleton[excID].Tail.y && Skeleton[excID].Tail.y < 0) {
       Turn(-PI/20);
       return;
     }
     if ( Skeleton[excID].Tail.x > Skeleton[excID].Tail.y && Skeleton[excID].Tail.x < 0) {
       Turn(PI/20);
       return;
     }
   }
   //4
   if (Skeleton[excID].Head.x>height-sd && Skeleton[excID].Head.y<sd ){
     if ( -Skeleton[excID].Tail.x > Skeleton[excID].Tail.y && Skeleton[excID].Tail.y > 0) {
       Turn(PI/20);
       return;
     }
     if ( Skeleton[excID].Tail.y > -Skeleton[excID].Tail.x && Skeleton[excID].Tail.x < 0) {
       Turn(-PI/20);
       return;
     }
   }
   
   // test four side
   if ( Skeleton[excID].Tail.y < 0 && Skeleton[excID].Head.y> height - sd){
         if(Skeleton[excID].Tail.x >0 ){ Turn(PI/20); }
         else{ Turn(-PI/20); }
         return;
   }
   if ( Skeleton[excID].Tail.y > 0 && Skeleton[excID].Head.y< sd){
        if(Skeleton[excID].Tail.x > 0){ Turn(-PI/20); }
         else{ Turn(PI/20); }
         return;
   }
   sd=abs(Velocity.x)*20;
    if ( Skeleton[excID].Tail.x < 0 && Skeleton[excID].Head.x> width - sd){
      if(Skeleton[excID].Tail.y >0){ Turn(-PI/20); }
         else{ Turn(PI/20); }
         return;
   
   }
   if ( Skeleton[excID].Tail.x > 0 && Skeleton[excID].Head.x< sd){
      if(Skeleton[excID].Tail.y>0){ Turn(PI/20); }
         else{ Turn(-PI/20); }
         return;
   }
 }
 public void testFood(){
   float dis=1000; int nfi=-1;
   for (int i=0; i<Nfood; i++){
     if (isThere[i]){
       float tdis = foodPellet[i].disTo(Skeleton[excID].Head); 
       if (tdis<dis) {
         dis=tdis;
         nfi=i;
       }
     }
   }
   //suppose the fish's field of view is 10 times of its size
   if (nfi==-1) {Accelerate(-0.005f); return;}
   else if (dis<10){ isThere[nfi]=false; return;}
   else if (dis< Skeleton[excID].Length*10){
     vec d=new vec(0,0);
     d.setTo(foodPellet[nfi], Skeleton[excID].Head);
     float ta=angle(d, Skeleton[excID].Tail);
     //turn towards the food 
     if (ta<PI/2*3) Turn(-ta*Velocity.norm()/10);
    //accelerate when find food
    Accelerate(0.005f);
   }
 
 }
 public void testOtherFish(){
 } 
 
}

DigitalSwimmer testfish=new DigitalSwimmer(200,200,150,150);

vec X=new vec(1,0);
vec Y=new vec(0,1); 




  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
 
  
  
  
  
  
  
  
 
 




  


    
    
    
    
    

/*------------------------------- 
   School Effect---Virtual Fish Tank
   Sketched by Tina W. Zhuo
   Feb 2009
---------------------------------*/

//school effect 

public void testFishes(){
  for (int i=0; i<Nfish-1; i++){
    for(int j=i+1; j<Nfish; j++){
      ///////
      vec vi= fishShoal[i].Skeleton[0].Tail;
      vec vj=fishShoal[j].Skeleton[0].Tail;
      float li=fishShoal[i].Skeleton[0].Length;
      float lj=fishShoal[j].Skeleton[0].Length;
      float ang=angle(vi,vj);
      fishShoal[i].Turn(lj/(li+lj)*ang*0.005f);
      fishShoal[j].Turn(-li/(li+lj)*ang*0.005f);
      /////
      vec vij=fishShoal[i].Skeleton[0].Head.makeVecTo(fishShoal[j].Skeleton[0].Head);
     if (vij.norm()<(li+lj)/2) { 
       fishShoal[j].Turn(angle(vij,vj)*0.001f);
       fishShoal[i].Turn(angle(vi,vij)*0.001f);
     }
     
     if (vij.norm()>(li+lj)){
       fishShoal[j].Turn(angle(vj,vij)*0.001f);
       fishShoal[i].Turn(angle(vij,vi)*0.001f);
     }
       
     
      
    }
      
  }
}  

/*------------------------------- 
   Segment---Virtual Fish Tank
   Sketched by Tina W. Zhuo
   Feb 2009
---------------------------------*/
class Segment{
 pt Head; 
 vec Tail; 
 float Length;
 float Mass; 
 Segment(pt h, vec t){
 Head=h.makeClone();
 Tail=t.makeClone();
 Length=t.norm();
 };
 Segment( Segment pre, float phi, float len){
   Head=(pre.Head).makeTranslatedBy(pre.Tail);
   Tail=((pre.Tail).makeRotatedBy(phi)).unit();
   Tail.scaleBy(len);
   Length=Tail.norm();
 }
 public void Change(Segment pre, float phi){
   Head=(pre.Head).makeTranslatedBy(pre.Tail);
   Tail=((pre.Tail).makeRotatedBy(phi)).unit().makeScaledBy(Length);
 }
 public void Display(){
   fill(250,250,120);
   noStroke();
   Head.show(Length/4);
   pt A=Head.makeTranslatedBy(Tail); 
   pt B=Head.makeTranslatedBy(Length/4, Tail.unit().left()); 
   pt C=Head.makeTranslatedBy(-Length/4, Tail.unit().left()); 
   fill(120,120,250);
   triangle(A.x, A.y, B.x, B.y, C.x, C.y);
 };
}

/*------------------------------- 
   User Interface---Virtual Fish Tank
   Sketched by Tina W. Zhuo
   Feb 2009
---------------------------------*/
DigitalSwimmer fishShoal[];
pt foodPellet[];
boolean isThere[];
int Nfish=0;
int Nfood=0;

float mx, my; 
boolean fish=false; 
boolean food=false;
public void mousePressed(){
  if (fish){
  mx=mouseX; 
  my=mouseY;}
  
}
public void mouseReleased(){
  if (fish){ 
    fishShoal[Nfish]=new DigitalSwimmer(mx, my, mouseX, mouseY); 
    Nfish++; 
  }
  if (food){
    if (Nfood>=100) return;
    foodPellet[Nfood]=new pt(mouseX, mouseY);
    isThere[Nfood]=true;
    Nfood++;
  }
}

public void displayShoal(){
  for (int i=0; i<Nfish; i++){
   fishShoal[i].generateGait();
   fishShoal[i].testBoundary();
   fishShoal[i].testFood();
   fishShoal[i].upDate();
   fishShoal[i].Display();
  }
}
public void displayFood(){
  for(int i=0; i<Nfood; i++){
    if (isThere[i]){
    noStroke();
    fill(250,120,120);
    foodPellet[i].show(5);
    }
  }
  
}


public void keyPressed(){
  if (key=='a'){ testfish.Turn(PI/10); };
  if (key=='b'){ testfish.Accelerate(0.1f);};
  if (key=='c'){testfish.Accelerate(-0.1f);};
  if (key=='d'){fish=true;}
  if (key=='e'){;}
  if (key=='f'){food=true;} 
}
  

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
  public void setTo(float px, float py) {x = px; y = py;};  
  public void setTo(pt P) {x = P.x; y = P.y;}; 
  public void setToMouse() { x = mouseX; y = mouseY; }; 
  public void moveWithMouse() { x += mouseX-pmouseX; y += mouseY-pmouseY; }; 
  public void scaleBy(float f) {x*=f; y*=f;};
  public void scaleBy(float u, float v) {x*=u; y*=v;};
  public void translateBy(vec V) {x += V.x; y += V.y;};   
  public void translateBy(float s, vec V) {x += s*V.x; y += s*V.y;};   
  public void translateBy(float u, float v) {x += u; y += v;};
  public void translateTowards(float s, pt P) {x+=s*(P.x-x);  y+=s*(P.y-y); };
  public void translateToTrack(float s, pt P) {vec V=P.makeVecTo(this).makeUnit(); this.setTo(new pt(P,s,V));};
  public void addScaledPt(float s, pt P) {x += s*P.x; y += s*P.y;};        // incorrect notation, but useful for computing weighted averages
  public void rotateBy(float a) {float dx=x, dy=y, c=cos(a), s=sin(a); x=c*dx+s*dy; y=-s*dx+c*dy; };     // around origin
  public void rotateBy(float a, pt P) {float dx=x-P.x, dy=y-P.y, c=cos(a), s=sin(a); x=P.x+c*dx+s*dy; y=P.y-s*dx+c*dy; };   // around point P
  public void rotateBy(float s, float t, pt P) {float dx=x-P.x, dy=y-P.y; dx-=dy*t; dy+=dx*s; dx-=dy*t; x=P.x+dx; y=P.y+dy; };   // s=sin(a); t=tan(a/2);
  public void clipToWindow() {x=max(x,0); y=max(y,0); x=min(x,height); y=min(y,height); }
  
  // OUTPUT POINT
  public pt makeClone() {return new pt(x,y); };
  public pt makeTranslatedBy(vec V) {return(new pt(x + V.x, y + V.y));};
  public pt makeTranslatedBy(float s, vec V) {return(new pt(x + s*V.x, y + s*V.y));};
  public pt makeTransaltedTowards(float s, pt P) {return(new pt(x + s*(P.x-x), y + s*(P.y-y)));};
  public pt makeTranslatedBy(float u, float v) {return(new pt(x + u, y + v));};
  public pt makeRotatedBy(float a, pt P) {float dx=x-P.x, dy=y-P.y, c=cos(a), s=sin(a); return(new pt(P.x+c*dx+s*dy, P.y-s*dx+c*dy)); };
  public pt makeRotatedBy(float a) {float dx=x, dy=y, c=cos(a), s=sin(a); return(new pt(c*dx+s*dy, -s*dx+c*dy)); };
  public pt makeProjectionOnLine(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return(P.makeTransaltedTowards(a/b,Q)); };

   // OUTPUT VEC
  public vec makeVecTo(pt P) {return(new vec(P.x-x,P.y-y)); };
  public vec makeVecToCenter () {return(new vec(x-height/2.f,y-height/2.f)); };
  public vec makeVecToAverage (pt P, pt Q) {return(new vec((P.x+Q.x)/2.0f-x,(P.y+Q.y)/2.0f-y)); };
  public vec makeVecToAverage (pt P, pt Q, pt R) {return(new vec((P.x+Q.x+R.x)/3.0f-x,(P.y+Q.y+R.x)/3.0f-y)); };
  public vec makeVecToMouse () {return(new vec(mouseX-x,mouseY-y)); };
  public vec makeVecToBisectProjection (pt P, pt Q) {float a=this.disTo(P), b=this.disTo(Q);  return(this.makeVecTo(interpolate(P,a/(a+b),Q))); };
  public vec makeVecToNormalProjection (pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return(this.makeVecTo(interpolate(P,a/b,Q))); };
 
  // OUTPUT TEST OR MEASURE
  public float disTo(pt P) {return(sqrt(sq(P.x-x)+sq(P.y-y))); };
  public float disToMouse() {return(sqrt(sq(x-mouseX)+sq(y-mouseY))); };
  public boolean isInWindow() {return(((x>0)&&(x<height)&&(y>0)&&(y<height)));};
  public boolean projectsBetween(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return((0<a)&&(a<b)); };
  public float ratioOfProjectionBetween(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q)), b=dot(P.makeVecTo(Q),P.makeVecTo(Q)); return(a/b); };
  public float disToLine(pt P, pt Q) {float a=dot(P.makeVecTo(this),P.makeVecTo(Q).makeUnit().makeTurnedLeft()); return(abs(a)); };
  public boolean isLeftOf(pt P, pt Q) {boolean l=dot(P.makeVecTo(this),P.makeVecTo(Q).makeTurnedLeft())>0; return(l);  };
  public boolean isInTriangle(pt A, pt B, pt C) { boolean a = this.isLeftOf(B,C); boolean b = this.isLeftOf(C,A); boolean c = this.isLeftOf(A,B); return((a&&b&&c)||(!a&&!b&&!c) );};

  // DRAW , PRINT
  public void show() {ellipse(x, y, height/200, height/200); }; // shows point as small dot
  public void show(float r) {ellipse(x, y, 2*r, 2*r); }; // shows point as disk of radius r
  public void showCross(float r) {line(x-r,y,x+r,y); line(x,y-r,x,y+r);}; 
  public void v() {vertex(x,y);};  // used for drawing polygons between beginShape(); and endShape();
  public void write() {println("("+x+","+y+")");};  // writes point coordinates in text window
  public void showLabel(String s, vec D) {text(s, x+D.x,y+D.y);  };  // show string displaced by vector D from point
  public void showLabel(String s) {text(s, x+5,y+4);  };
  public void showLabel(int i) {text(str(i), x+5,y+4);  };  // shows integer number next to point
  public void showLabel(String s, float u, float v) {text(s, x+u, y+v);  };
  public void showSegmentTo (pt P) {line(x,y,P.x,P.y); }; // draws edge to another point
  public void to (pt P) {line(x,y,P.x,P.y); }; // draws edge to another point

  } // end of pt class
  
public pt mid(pt A, pt B) {return(new pt((A.x+B.x)/2.0f,(A.y+B.y)/2.0f)); };
public pt average(pt A, pt B) {return(new pt((A.x+B.x)/2.0f,(A.y+B.y)/2.0f)); };
public pt average(pt A, pt B, pt C) {return(new pt((A.x+B.x+C.x)/3.0f,(A.y+B.y+C.y)/3.0f)); };
public pt mouse() {return(new pt(mouseX,mouseY));};  // current mouse location
public pt pmouse() {return(new pt(pmouseX,pmouseY));};  // previous mouse location
public pt mouseInWindow() {float x=mouseX, y=mouseY; x=max(x,0); y=max(y,0); x=min(x,height); y=min(y,height);  return(new pt(x,y));}; // clips mouse to square window
public pt screenCenter() {return(new pt(height/2,height/2));}  // point in center of screen
public pt interpolate(pt A, float s, pt B) {return(new pt(A.x+s*(B.x-A.x),A.y+s*(B.y-A.y))); };
public pt weightedSum(float a, pt A, float b, pt B) {pt P =  A.makeClone(); P.scaleBy(a); P.addScaledPt(b,B); return(P);}
public pt weightedSum(float a, pt A, float b, pt B, float c, pt C) {pt P =  A.makeClone(); P.scaleBy(a); P.addScaledPt(b,B);  P.addScaledPt(c,C); return(P);}
public pt scaledAverage(pt A, pt B, pt C, float s) {return(new pt((A.x+B.x+C.x)/s,(A.y+B.y+C.y)/s)); };
public pt weightedSum(float a, pt A, float b, pt B, float c, pt C, float d, pt D) 
        {pt P =  A.makeClone(); P.scaleBy(a); P.addScaledPt(b,B); P.addScaledPt(c,C); P.addScaledPt(d,D); return(P);}
public boolean isLeftTurn(pt A, pt B, pt C) {return(dot(A.makeVecTo(B).makeTurnedLeft() , B.makeVecTo(C) )>0); };  // true if B is a left turn (appears right on the screen, Y goes down)
public boolean mouseIsInWindow() {return(((mouseX>0)&&(mouseX<height)&&(mouseY>0)&&(mouseY<height)));};

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
  public void setTo(float px, float py) {x = px; y = py;}; 
  public void setTo(pt P, pt Q) {x = Q.x-P.x; y = Q.y-P.y;}; 
  public void setTo(vec V) {x = V.x; y = V.y;}; 
  public void scaleBy(float f) {x*=f; y*=f;};
  public void back() {x=-x; y=-y;};
  public void mul(float f) {x*=f; y*=f;};
  public void div(float f) {x/=f; y/=f;};
  public void scaleBy(float u, float v) {x*=u; y*=v;};
  public void normalize() {float n=sqrt(sq(x)+sq(y)); if (n>0.000001f) {x/=n; y/=n;};};
  public void add(vec V) {x += V.x; y += V.y;};   
  public void add(float s, vec V) {x += s*V.x; y += s*V.y;};   
  public void add(float u, float v) {x += u; y += v;};
  public void turnLeft() {float w=x; x=-y; y=w;};
  public void rotateBy (float a) {float xx=x, yy=y; x=xx*cos(a)-yy*sin(a); y=xx*sin(a)+yy*cos(a); };
  
  // OUTPUT VEC
  public vec makeClone() {return(new vec(x,y));}; 
  public vec makeUnit() {float n=sqrt(sq(x)+sq(y)); if (n<0.000001f) n=1; return(new vec(x/n,y/n));}; 
  public vec unit() {float n=sqrt(sq(x)+sq(y)); if (n<0.000001f) n=1; return(new vec(x/n,y/n));}; 
  public vec makeScaledBy(float s) {return(new vec(x*s, y*s));};
  public vec makeTurnedLeft() {return(new vec(-y,x));};
  public vec left() {return(new vec(-y,x));};
  public vec makeOffsetVec(float s, vec V) {return(new vec(x + s*V.x, y + s*V.y));};
  public vec makeOffsetVec(float u, float v) {return(new vec(x + u, y + v));};
  public vec makeRotatedBy(float a) {return(new vec(x*cos(a)-y*sin(a),x*sin(a)+y*cos(a))); };
  public vec makeReflectedVec(vec N) { return makeOffsetVec(-2.f*dot(this,N),N);};
  public vec makeBack(){ return (new vec(-x, -y));}

  // OUTPUT TEST MEASURE
  public float norm() {return(sqrt(sq(x)+sq(y)));}
  public boolean isNull() {return((abs(x)+abs(y)<0.000001f));}
  public float angle() {return(atan2(y,x)); }

  // DRAW, PRINT
  public void write() {println("("+x+","+y+")");};
  public void showAt (pt P) {line(P.x,P.y,P.x+x,P.y+y); }; 
  public void showArrowAt (pt P, float s) {makeScaledBy(s).showArrowAt(P);} 
  public void showArrowAt (pt P) {line(P.x,P.y,P.x+x,P.y+y); 
      float n=min(this.norm()/10.f,height/50.f); 
      pt Q=P.makeTranslatedBy(this); 
      vec U = this.makeUnit().makeScaledBy(-n);
      vec W = U.makeTurnedLeft().makeScaledBy(0.3f);
      beginShape(); Q.makeTranslatedBy(U).makeTranslatedBy(W).v(); Q.v(); W.scaleBy(-1); Q.makeTranslatedBy(U).makeTranslatedBy(W).v(); endShape(CLOSE); }; 
  public void showArrowAt (pt P, String s) {line(P.x,P.y,P.x+x,P.y+y); 
      float n=min(this.norm()/10.f,height/50.f); 
      pt Q=P.makeTranslatedBy(this); 
      vec U = this.makeUnit().makeScaledBy(-n);
      vec W = U.makeTurnedLeft().makeScaledBy(0.3f);
      beginShape(); Q.makeTranslatedBy(U).makeTranslatedBy(W).v(); Q.v(); W.scaleBy(-1); Q.makeTranslatedBy(U).makeTranslatedBy(W).v(); endShape(CLOSE);
      showLabelAt(s,Q);
       }; 

  public void showLabelAt(String s, pt P) {pt Q = P.makeTranslatedBy(0.5f,this); Q.showLabel(s); };

  } // end vec class
 
public vec mouseDrag() {return new vec(mouseX-pmouseX,mouseY-pmouseY);};
public vec average(vec U, vec V) {return(new vec((U.x+V.x)/2.0f,(U.y+V.y)/2.0f)); };
public float dot(vec U, vec V) {return(U.x*V.x+U.y*V.y); };
public vec linearInterpolate(vec U, float s, vec V) {return(new vec(U.x+s*(V.x-U.x),U.y+s*(V.y-U.y))); };
public vec circularInterpolate(vec U, float s, vec V) {float a = angle(U,V); vec W = U.makeRotatedBy(s*a); return W ; };

//************************************************************************
//**** ANGLES
//************************************************************************
public float angle(vec V) {return(atan2(V.y,V.x)); };
public float angle(vec U, vec V) {return(atan2(dot(U.makeTurnedLeft(),V),dot(U,V))); };
public float mPItoPIangle(float a) { if(a>PI) return(mPItoPIangle(a-2*PI)); if(a<-PI) return(mPItoPIangle(a+2*PI)); return(a);};
public float toDeg(float a) {return(a*180/PI);}
public float toRad(float a) {return(a*PI/180);}

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
  public frame makeClone() {return(new frame(O,I,J));}
  public void reset() {O.setTo(0,0); I.setTo(1,0); J.setTo(0,1); }
  public void setTo(frame F) {O.setTo(F.O); I.setTo(F.I); J.setTo(F.J); }
  public void setTo(pt pO, vec pI, vec pJ) {O.setTo(pO); I.setTo(pI); J.setTo(pJ); }
  public void show() {float d=height/20; O.show(); I.makeScaledBy(d).showArrowAt(O); J.makeScaledBy(d).showArrowAt(O); }
  public void showLabels() {float d=height/20; 
               O.makeTranslatedBy(average(I,J).makeScaledBy(-d/4)).showLabel("O",-3,5); 
               O.makeTranslatedBy(d,I).makeTranslatedBy(-d/5.f,J).showLabel("I",-3,5); 
               O.makeTranslatedBy(d,J).makeTranslatedBy(-d/5.f,I).showLabel("J",-3,5); 
             }
  public void translateBy(vec V) {O.translateBy(V);}
  public void translateBy(float x, float y) {O.translateBy(x,y);}
  public void rotateBy(float a) {I.rotateBy(a); J.rotateBy(a); }
  public frame makeTranslatedBy(vec V) {frame F = this.makeClone(); F.translateBy(V); return(F);}
  public frame makeTranslatedBy(float x, float y) {frame F = this.makeClone(); F.translateBy(x,y); return(F); }
  public frame makeRotatedBy(float a) {frame F = this.makeClone(); F.rotateBy(a); return(F); }
   
  public float angle() {return(I.angle());}
  public void apply() {translate(O.x,O.y); rotate(angle());}  // rigid body tansform, use between pushMatrix(); and popMatrix();
  public void moveTowards(frame B, float s) {O.translateTowards(s,B.O); rotateBy(s*(B.angle()-angle()));}  // for chasing or interpolating frames
  } // end frame class
 
public frame makeMidEdgeFrame(pt A, pt B) {return(new frame(average(A,B),A.makeVecTo(B)));}  // creates frame for edge

public frame interpolate(frame A, float s, frame B) {   // creates a frame that is a linear interpolation between two other frames
    frame F = A.makeClone(); F.O.translateTowards(s,B.O); F.rotateBy(s*(B.angle()-A.angle()));
    return(F);
    }

public frame twist(frame A, float s, frame B) {   // a circular interpolation
  float d=A.O.disTo(B.O);
  float b=mPItoPIangle(angle(A.I,B.I));
  frame F = A.makeClone(); F.rotateBy(s*b);
  pt M = average(A.O,B.O);   
  if ((abs(b)<0.000001f) || (abs(b-PI)<0.000001f)) F.O.translateTowards(s,B.O); 
  else {
  float h=d/2/tan(b/2); //else print("/b");
     vec W = A.O.makeVecTo(B.O); W.normalize();
     vec L = W.makeTurnedLeft();   L.scaleBy(h);
     M.translateBy(L);  // fill(0); M.show(6);
     L.scaleBy(-1);  L.normalize(); 
     if (abs(h)>=0.000001f) L.scaleBy(abs(h+sq(d)/4/h)); //else print("/h");
     pt N = M.makeClone(); N.translateBy(L);  
     F.O.rotateBy(-s*b,M);
     };   
  return(F);
  }
  
//************************************************************************
//**** EDGES, RAYS, LINES
//************************************************************************
public boolean edgesIntersect(pt A, pt B, pt C, pt D) {boolean hit=true; 
    if (isLeftTurn(A,B,C)==isLeftTurn(A,B,D)) hit=false; 
    if (isLeftTurn(C,D,A)==isLeftTurn(C,D,B)) hit=false; 
     return hit; }
     
//************************************************************************
//**** TRIANGLES
//************************************************************************


//************************************************************************
//**** INTEGRAL PRPERTIES, AREA, CENTER
//************************************************************************
public float trapezeArea(pt A, pt B) {return((B.x-A.x)*(B.y+A.y)/2.f);}
public pt trapezeCenter(pt A, pt B) { return(new pt(A.x+(B.x-A.x)*(A.y+2*B.y)/(A.y+B.y)/3.f, (A.y*A.y+A.y*B.y+B.y*B.y)/(A.y+B.y)/3.f) ); }

//************************************************************************
//**** CIRCLES
//************************************************************************
public pt circumCenter (pt A, pt B, pt C) {    // computes the center of a circumscirbing circle to triangle (A,B,C)
  vec AB =  A.makeVecTo(B);  float ab2 = dot(AB,AB);
  vec AC =  A.makeVecTo(C); AC.turnLeft();  float ac2 = dot(AC,AC);
  float d = 2*dot(AB,AC);
  AB.turnLeft();
  AB.scaleBy(-ac2); 
  AC.scaleBy(ab2);
  AB.add(AC);
  AB.scaleBy(1.f/d);
  pt X =  A.makeClone();
  X.translateBy(AB);
  return(X);
  };

public void showArcThrough (pt A, pt B, pt C) {
   pt O = circumCenter ( A,  B,  C);
   float r=2.f*(O.disTo(A) + O.disTo(B)+ O.disTo(C))/3.f;
   float a = O.makeVecTo(A).angle(); average(O,A).showLabel(str(toDeg(a)));
   float c = O.makeVecTo(C).angle(); average(O,C).showLabel(str(toDeg(c)));
   if(isLeftTurn(A,B,C)) arc(O.x,O.y,r,r,a,c); else arc(O.x,O.y,r,r,c,a);
    }

public float radius (pt A, pt B, pt C) {
    float a=B.disTo(C);     float b=C.disTo(A);     float c=A.disTo(B);
    float s=(a+b+c)/2;     float d=sqrt(s*(s-a)*(s-b)*(s-c));   float r=a*b*c/4/d;
    return (r);
   };
   
//************************************************************************
//**** CURVES
//************************************************************************
public pt s(pt A, float s, pt B) {return(new pt(A.x+s*(B.x-A.x),A.y+s*(B.y-A.y))); };
public pt b(pt A, pt B, pt C, float s) {return( s(s(B,s/4.f,A),0.5f,s(B,s/4.f,C))); };                          // returns a tucked B towards its neighbors
public pt f(pt A, pt B, pt C, pt D, float s) {return( s(s(A,1.f+(1.f-s)/8.f,B) ,0.5f, s(D,1.f+(1.f-s)/8.f,C))); };    // returns a bulged mid-edge point 

public pt cubicBezier(pt A, pt B, pt C, pt D, float t) {return( s( s( s(A,t,B) ,t, s(B,t,C) ) ,t, s( s(B,t,C) ,t, s(C,t,D) ) ) ); }
public void splitBezier(pt A, pt B, pt C, pt D, int rec) {
  if (rec==0) {B.v(); C.v(); D.v(); return;};
  pt E=mid(A,B);   pt F=mid(B,C);   pt G=mid(C,D);  
           pt H=mid(E,F);   pt I=mid(F,G);  
                    pt J=mid(H,I); J.show(3);   
  splitBezier(A,E,H,J,rec-1);   splitBezier(J,I,G,D,rec-1); 
 }
 
public void drawSplitBezier(pt A, pt B, pt C, pt D, float t) {
  pt E=s(A,t,B); E.show(2);  pt F=s(B,t,C); F.show(2);  pt G=s(C,t,D); G.show(2);  E.to(F); F.to(G);
           pt H=s(E,t,F); H.show(2);   pt I=s(F,t,G);  I.show(2); H.to(I);
                    pt J=s(H,t,I); J.show(4);   
 }

public void drawCubicBezier(pt A, pt B, pt C, pt D) { beginShape();  for (float t=0; t<=1; t+=0.02f) {cubicBezier(A,B,C,D,t).v(); };  endShape(); }
public float cubicBezierAngle (pt A, pt B, pt C, pt D, float t) {pt P = s(s(A,t,B),t,s(B,t,C)); pt Q = s(s(B,t,C),t,s(C,t,D)); vec V=P.makeVecTo(Q); float a=atan2(V.y,V.x); return(a);}  
public vec cubicBezierTangent (pt A, pt B, pt C, pt D, float t) {pt P = s(s(A,t,B),t,s(B,t,C)); pt Q = s(s(B,t,C),t,s(C,t,D)); vec V=P.makeVecTo(Q); V.makeUnit(); return(V);}  

public void drawParabolaInHat(pt A, pt B, pt C, int rec) {
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


  static public void main(String args[]) {     PApplet.main(new String[] { "FishTank" });  }}