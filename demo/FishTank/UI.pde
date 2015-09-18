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
void mousePressed(){
  if (fish){
  mx=mouseX; 
  my=mouseY;}
  
}
void mouseReleased(){
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

void displayShoal(){
  for (int i=0; i<Nfish; i++){
   fishShoal[i].generateGait();
   fishShoal[i].testBoundary();
   fishShoal[i].testFood();
   fishShoal[i].upDate();
   fishShoal[i].Display();
  }
}
void displayFood(){
  for(int i=0; i<Nfood; i++){
    if (isThere[i]){
    noStroke();
    fill(250,120,120);
    foodPellet[i].show(5);
    }
  }
  
}


void keyPressed(){
  if (key=='a'){ testfish.Turn(PI/10); };
  if (key=='b'){ testfish.Accelerate(0.1);};
  if (key=='c'){testfish.Accelerate(-0.1);};
  if (key=='d'){fish=true;}
  if (key=='e'){;}
  if (key=='f'){food=true;} 
}
  
