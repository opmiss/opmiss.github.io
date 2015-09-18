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
 void Change(Segment pre, float phi){
   Head=(pre.Head).makeTranslatedBy(pre.Tail);
   Tail=((pre.Tail).makeRotatedBy(phi)).unit().makeScaledBy(Length);
 }
 void Display(){
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
