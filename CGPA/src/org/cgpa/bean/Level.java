package org.cgpa.bean;


/**
 * 	百分制分数	 	等级 		成绩点数 
 * 	90-100    	 A        4 
 * 	80-89     	 B        3 
 * 	70-79     	 C        2 
 * 	60-69     	 D        1 
 * 	60以下    	 	 E        0  
 * @author zwein
 */
public enum Level{
	A("A",4),B("B",3),C("C",2),D("D",1),E("E",0);
	
	String id = "";
	int gpa = 0;
	
	Level(String id,int gpa){
		this.id = id;
		this.gpa = gpa;
	}
	
	public static Level toGPA(double score){
		Level level = null;
		if(score >= 90){
			level = Level.A;
		}else if (score >= 80 && score< 90){
			level = Level.B;
		}else if(score >= 70 && score < 80){
			level = Level.C;
		}else if(score >= 60 && score < 70){
			level = Level.D;
		}else{
			level = Level.E;
		}
		return level;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getGpa() {
		return gpa;
	}

	public void setGpa(int gpa) {
		this.gpa = gpa;
	}
	
	
}
