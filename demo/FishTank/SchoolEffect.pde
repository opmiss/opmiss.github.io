/*------------------------------- 
   School Effect---Virtual Fish Tank
   Sketched by Tina W. Zhuo
   Feb 2009
---------------------------------*/

//school effect 

void testFishes(){
  for (int i=0; i<Nfish-1; i++){
    for(int j=i+1; j<Nfish; j++){
      ///////
      vec vi= fishShoal[i].Skeleton[0].Tail;
      vec vj=fishShoal[j].Skeleton[0].Tail;
      float li=fishShoal[i].Skeleton[0].Length;
      float lj=fishShoal[j].Skeleton[0].Length;
      float ang=angle(vi,vj);
      fishShoal[i].Turn(lj/(li+lj)*ang*0.005);
      fishShoal[j].Turn(-li/(li+lj)*ang*0.005);
      /////
      vec vij=fishShoal[i].Skeleton[0].Head.makeVecTo(fishShoal[j].Skeleton[0].Head);
     if (vij.norm()<(li+lj)/2) { 
       fishShoal[j].Turn(angle(vij,vj)*0.001);
       fishShoal[i].Turn(angle(vi,vij)*0.001);
     }
     
     if (vij.norm()>(li+lj)){
       fishShoal[j].Turn(angle(vj,vij)*0.001);
       fishShoal[i].Turn(angle(vij,vi)*0.001);
     }
       
     
      
    }
      
  }
}  
