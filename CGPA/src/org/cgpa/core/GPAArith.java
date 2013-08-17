package org.cgpa.core;

import java.util.List;

import org.cgpa.bean.*;
import org.cgpa.bean.gpa.GPA.COURSE;

public class GPAArith {

	/**
	 * 使用4分制计算
	 * 
	 * PA＝（4*4＋3*3＋2*4＋6*2＋3*3）／（4＋3＋2＋6＋3）＝3.00
	 * 
	 * @param list
	 * @return
	 */
	public static double calInFPS(List<COURSE> list) {
		int totalOfCredit = 0;
		double sumOfCreditPlusScore = 0;
		double result = 0.00;

		for (COURSE course : list) {
			sumOfCreditPlusScore += course.getCREDIT()
					* Level.toGPA(course.getSCORE()).getGpa();
			totalOfCredit += course.getCREDIT();
		}
		result = sumOfCreditPlusScore / totalOfCredit;
		return result;
	}

	/**
	 * GPA＝［（92*4＋80*3＋98*2＋70*6＋89*3）*4］／［（4＋3＋2＋6＋3）*100］＝3.31
	 * 
	 * @param list
	 * @return
	 */
	public static double callInSAS(List<COURSE> list) {
		int totalOfCredit = 0;
		double sumOfCreditPlusScore = 0;
		double result = 0.00;
		for (COURSE course : list) {
			sumOfCreditPlusScore += course.getCREDIT() * course.getSCORE();
			totalOfCredit += course.getCREDIT();
		}

		result = (sumOfCreditPlusScore * 4) / (totalOfCredit * 100);

		return result;
	}

	public static double callInBOLE(List<COURSE> list) {
		int totalOfCredit = 0;
		double sumOfCreditPlusScore = 0;
		double result = 0.00;
		for (COURSE course : list) {
			sumOfCreditPlusScore += course.getCREDIT() * course.getSCORE();
			totalOfCredit += course.getCREDIT();
			// System.out.println("test:    "+course.getCREDIT());
		}

		result = (sumOfCreditPlusScore) / (totalOfCredit);
		// System.out.println(sumOfCreditPlusScore);
		return result;
	}
}
