package com.shigeodayo.anohana;

import java.util.ArrayList;
import java.util.List;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;

/**
 * あの花のED
 * @author shigeo
 *
 */
public class MainFrame extends PApplet{

	PImage[] hanaImage=null;
	PImage[] hanaGrayImage=null;
	List<Hana> mauhana=null;
	
	final int[] hanaNum={
			240, 200, 100, 20, 10	
	};
	final float[] hanaSize={
			10.0f, 20.0f, 40.0f, 80.0f, 120.0f
	};
	final int[] hanaVelocity={
			5, 4, 3, 2, 1
	};
	boolean bDown=true;
		
	float sval=1.2f*60;
	boolean bScale=false;

	public void setup(){
		size(720, 480, P3D);
		frameRate(30);
		imageMode(CENTER);
		hanaImage=new PImage[5];
		hanaGrayImage=new PImage[5];
		for(int i=0; i<5; i++) {
			hanaImage[i]=loadImage("hana"+i+".png");
			hanaGrayImage[i]=loadImage("hana"+i+"_.png");
		}
		mauhana=new ArrayList<Hana>();
		
		for(int i=0; i<hanaNum.length; i++){
			for(int j=0; j<hanaNum[i]; j++){
				mauhana.add(new Hana(new PVector(random(0, width), -random(0, 1000)),
						 new PVector(0, hanaVelocity[i]), hanaSize[i]));				
			}
		}
	}
	
	public void draw(){
		background(255, 255, 255);
		
		if(bScale){
			if(bDown){
				sval=sval-0.6f;
				if(sval<=1.0f*60){
					bScale=false;
					bDown=false;
				}
			}else{
				sval=sval+0.6f;
				if(sval>=1.2f*60){
					bScale=false;
					bDown=true;
				}
			}
		}
		camera(width/2.0f, height/2.0f, (height/2.0f)/tan((float) (PI*sval/360.0)),
			   width/2.0f, height/2.0f, 0.0f,
			   0.0f, 1.0f, 0.0f); 	
		
		int size=mauhana.size();
		for(int i=0; i<size; i++){
			mauhana.get(i).draw(bDown);
			if(!bScale){
				mauhana.get(i).update(bDown);
			}
		}
	}
	public void mouseClicked(){
		bScale=true;
	}	
	
	class Hana{
		private PVector location;
		private PVector velocity;
		private float angle=0.0f;
		private float size=0.0f;
		private int colorId=-1;

		public Hana(PVector location, PVector velocity, float size){
			this.location=location;
			this.velocity=velocity;
			this.size=size;
			colorId=(int) random(0, hanaImage.length);
		}
						
		public void update(boolean bDown){
			if(bDown){
				angle-=0.01;
				location.add(velocity);
				if(location.y-size>height){
					location=new PVector(random(0, width), -size+height-location.y);
					colorId=(int) random(0, hanaImage.length);
				}
			}else{
				angle+=0.01;
				location.sub(velocity);
				if(location.y+size<0){
					location=new PVector(random(0, width), height+size-location.y);
					colorId=(int) random(0, hanaImage.length);
				}
			}
		}
		
		public void draw(boolean bDown){
			pushMatrix();{
				translate(location.x, location.y);
				rotate(angle);
				if(bDown)
					image(hanaGrayImage[colorId], 0, 0, size, size);
				else
					image(hanaImage[colorId], 0, 0, size, size);
			}popMatrix();
		}
	}
}