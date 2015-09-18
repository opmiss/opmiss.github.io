/*------------------------------- 
   Virtual Fish Tank
   Sketched by Tina W. Zhuo
   Feb 2009
---------------------------------*/

import SpringGUI.*;
SpringGUI gui; 

void setup(){
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
void draw(){
  
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

void handleEvent(String[] parameters){
  if (parameters[2]=="mouseClicked"){
   if(parameters[1]=="Fish_B") {fish=true; food=false; println("click and drag in the window to create swimmers"); }
   if(parameters[1]=="Food_B") {fish=false; food=true; println("click in the window to place food pelletsp"); }
  }
}
