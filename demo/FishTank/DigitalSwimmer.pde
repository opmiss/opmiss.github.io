
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
    Omega=0.2; 
    for (int i=0; i<N; i++) fixPhi[i]=0;
    
    Skeleton=new Segment[N];
    Skeleton[excID]=new Segment(new pt(sx, sy), new vec((ex-sx)/2, (ey-sy)/2));
    Position.setTo(new pt(sx,sy));
    for (int i=excID+1; i<N; i++){ 
      Skeleton[i]=new Segment(Skeleton[i-1],0,Skeleton[excID].Length/(N-1));
    }
  }
  void generateGait(){
    for (int i=1; i<N; i++){
      Phi[i]=fixPhi[i]+Amplitude*sin(T+abs(i-excID)*dPhi);
    }
  }
  void updateSegments(){
    for (int i=1; i<N; i++){
      Skeleton[i].Change(Skeleton[i-1], Phi[i]); 
    }
  }
  void computeForce(){
 //   for (int i=0; i<
    
    Force[0]=(Velocity).makeScaledBy(-Velocity.norm()*0.05); 
    Force[1]=(Skeleton[0].Tail).makeScaledBy(-Omega*0.1);
  //  println("Resistance"+Force[0].x+", "+Force[0].y);
   // println("Propulsion"+Force[1].x+", "+Force[1].y);
  }
  void upDate(){
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
  void Turn( float deg){
    Skeleton[excID].Tail.rotateBy(deg);
  
  }
  void Accelerate(float a){
  Omega=Omega+a;
  if (Omega<0.1) Omega=0.1; 
  }
  void Display(){
  for (int i=0; i<N; i++){
    Skeleton[i].Display();
  }
 }
 
 ////////////////////
 void testBoundary(){
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
 void testFood(){
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
   if (nfi==-1) {Accelerate(-0.005); return;}
   else if (dis<10){ isThere[nfi]=false; return;}
   else if (dis< Skeleton[excID].Length*10){
     vec d=new vec(0,0);
     d.setTo(foodPellet[nfi], Skeleton[excID].Head);
     float ta=angle(d, Skeleton[excID].Tail);
     //turn towards the food 
     if (ta<PI/2*3) Turn(-ta*Velocity.norm()/10);
    //accelerate when find food
    Accelerate(0.005);
   }
 
 }
 void testOtherFish(){
 } 
 
}

DigitalSwimmer testfish=new DigitalSwimmer(200,200,150,150);

vec X=new vec(1,0);
vec Y=new vec(0,1); 




  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
 
  
  
  
  
  
  
  
 
 




  


    
    
    
    
    
