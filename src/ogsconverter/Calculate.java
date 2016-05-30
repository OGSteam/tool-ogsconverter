/*
 * Calculate.java
 *
 * Created on 1 juillet 2006, 20:01
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package ogsconverter;

import java.util.regex.Pattern;

/**
 *
 * @author MC-20-EG-2003
 */
public class Calculate {
    
    
    public static String expression(String exp) {
        String subexp;
        String subsubexp;
        
        exp = exp.replaceAll("(\\s+|\\.|\\,)", "");
        exp = exp.replaceAll("\\((\\-?\\d+)\\)", "$1"); // 2nd pass
        exp = exp.replaceAll("(\\d|\\.|\\))\\-(\\d|\\()", "$1+-$2");
        exp = exp.replaceAll("(\\d|\\.|\\))\\-(\\d|\\()", "$1+-$2"); // 2nd pass
        
        while(Pattern.compile("\\(\\-?\\d+\\.?\\d*(?:E\\d+)?[\\+\\*\\/\\^\\%]\\-?\\d+\\.?\\d*(?:E\\d+)?(?:[\\+\\*\\/\\^\\%]\\-?\\d+\\.?\\d*(?:E\\d+)?)*\\)", Pattern.CASE_INSENSITIVE).matcher(exp).find()) {
            subexp = exp.replaceFirst("^.*?(\\(\\-?\\d+\\.?\\d*(?:E\\d+)?[\\+\\*\\/\\^\\%]\\-?\\d+\\.?\\d*(?:E\\d+)?(?:[\\+\\*\\/\\^\\%]\\-?\\d+\\.?\\d*(?:E\\d+)?)*\\)).*$", "$1");
            while(Pattern.compile("(\\(\\-?\\d+\\.?\\d*[\\+\\*\\/\\^]\\-?\\d+\\.?\\d*(?:[\\+\\*\\/\\^]\\-?\\d+\\.?\\d*)+\\))", Pattern.CASE_INSENSITIVE).matcher(subexp).find()) {
                if(Pattern.compile("\\-?\\d+\\.?\\d*(?:E\\d+)?\\^\\-?\\d+\\.?\\d*(?:E\\d+)?", Pattern.CASE_INSENSITIVE).matcher(subexp).find()) subsubexp = subexp.replaceFirst(".*?[^\\d\\-\\.](\\-?\\d+\\.?\\d*(?:E\\d+)?\\^\\-?\\d+\\.?\\d*(?:E\\d+)?)[^\\d\\-\\.].*", "$1"); // exposant
                else if(Pattern.compile("\\-?\\d+\\.?\\d*(?:E\\d+)?\\*\\-?\\d+\\.?\\d*(?:E\\d+)?", Pattern.CASE_INSENSITIVE).matcher(subexp).find()) subsubexp = subexp.replaceFirst(".*?[^\\d\\-\\.](\\-?\\d+\\.?\\d*(?:E\\d+)?\\*\\-?\\d+\\.?\\d*(?:E\\d+)?)[^\\d\\-\\.].*", "$1"); // multiplication
                else if(Pattern.compile("\\-?\\d+\\.?\\d*(?:E\\d+)?\\/\\-?\\d+\\.?\\d*(?:E\\d+)?", Pattern.CASE_INSENSITIVE).matcher(subexp).find()) subsubexp = subexp.replaceFirst(".*?[^\\d\\-\\.](\\-?\\d+\\.?\\d*(?:E\\d+)?\\/\\-?\\d+\\.?\\d*(?:E\\d+)?)[^\\d\\-\\.].*", "$1"); // division
                else if(Pattern.compile("\\-?\\d+\\.?\\d*(?:E\\d+)?\\%\\-?\\d+\\.?\\d*(?:E\\d+)?", Pattern.CASE_INSENSITIVE).matcher(subexp).find()) subsubexp = subexp.replaceFirst(".*?[^\\d\\-\\.](\\-?\\d+\\.?\\d*(?:E\\d+)?\\%\\-?\\d+\\.?\\d*(?:E\\d+)?)[^\\d\\-\\.].*", "$1"); // modulo
                else subsubexp = subexp.replaceFirst("\\((\\-?\\d+\\.?\\d*(?:E\\d+)?\\+\\-?\\d+\\.?\\d*(?:E\\d+)?)\\+.*", "$1"); // addition
                exp = exp.replaceFirst("\\Q" + subexp + "\\E", subexp = subexp.replaceFirst("\\Q" + subsubexp + "\\E", calcule(subsubexp)));
            }
            exp = exp.replaceFirst("\\Q" + subexp + "\\E", calcule(subexp));
        }
        
        exp = " " + exp + " ";
        
        while(Pattern.compile("(\\-?\\d+\\.?\\d*(?:E\\d+)?[\\+\\*\\/\\^\\%]\\-?\\d+\\.?\\d*(?:E\\d+)?(?:[\\+\\*\\/\\^\\%]\\-?\\d+\\.?\\d*(?:E\\d+)?)+)", Pattern.CASE_INSENSITIVE).matcher(exp).find()) {
            if(Pattern.compile("\\-?\\d+\\.?\\d*(?:E\\d+)?\\^\\-?\\d+\\.?\\d*(?:E\\d+)?", Pattern.CASE_INSENSITIVE).matcher(exp).find()) subexp = exp.replaceFirst(".*?[^\\d\\-\\.](\\-?\\d+\\.?\\d*\\^\\-?\\d+\\.?\\d*)[^\\d\\-\\.].*", "$1"); // exposant
            else if(Pattern.compile("\\-?\\d+\\.?\\d*(?:E\\d+)?\\*\\-?\\d+\\.?\\d*(?:E\\d+)?", Pattern.CASE_INSENSITIVE).matcher(exp).find()) subexp = exp.replaceFirst(".*?[^\\d\\-\\.](\\-?\\d+\\.?\\d*\\*\\-?\\d+\\.?\\d*)[^\\d\\-\\.].*", "$1"); // multiplication
            else if(Pattern.compile("\\-?\\d+\\.?\\d*(?:E\\d+)?\\/\\-?\\d+\\.?\\d*(?:E\\d+)?", Pattern.CASE_INSENSITIVE).matcher(exp).find()) subexp = exp.replaceFirst(".*?[^\\d\\-\\.](\\-?\\d+\\.?\\d*\\/\\-?\\d+\\.?\\d*)[^\\d\\-\\.].*", "$1"); // division
            else if(Pattern.compile("\\-?\\d+\\.?\\d*(?:E\\d+)?\\%\\-?\\d+\\.?\\d*(?:E\\d+)?", Pattern.CASE_INSENSITIVE).matcher(exp).find()) subexp = exp.replaceFirst(".*?[^\\d\\-\\.](\\-?\\d+\\.?\\d*\\%\\-?\\d+\\.?\\d*)[^\\d\\-\\.].*", "$1"); // modulo
            else subexp = exp.replaceFirst(" (\\-?\\d+\\.?\\d*(?:E\\d+)?\\+\\-?\\d+\\.?\\d*(?:E\\d+)?)\\+.*", "$1"); // addition
            exp = exp.replaceFirst("\\Q" + subexp + "\\E", calcule(subexp));
        }
        exp = calcule(exp.trim());
        
        return Main.formatnumber(Long.toString((long)Math.round(Double.parseDouble(exp))).replaceAll("E", "e+"));
    }
    
    public static String calcule(String subexp) {
        double result = 0;
        
        subexp = subexp.replaceAll("[\\(\\)]", "");
        
        if(Pattern.compile("^\\-?\\d+\\.?\\d*(?:E\\d+)?$", Pattern.CASE_INSENSITIVE).matcher(subexp).find()) return subexp;
        if(!Pattern.compile("\\-?\\d+\\.?\\d*\\D\\-?\\d+\\.?\\d*(?:E\\d+)?", Pattern.CASE_INSENSITIVE).matcher(subexp).find()) return subexp;
        
        double x = Double.parseDouble(subexp.replaceFirst("^(\\-?\\d+\\.?\\d*(?:E\\d+)?)\\D\\-?\\d+\\.?\\d*(?:E\\d+)?$", "$1"));
        char op = subexp.replaceFirst("^\\-?\\d+\\.?\\d*(?:E\\d+)?(\\D)\\-?\\d+\\.?\\d*(?:E\\d+)?$", "$1").charAt(0);
        double y = Double.parseDouble(subexp.replaceFirst("^\\-?\\d+\\.?\\d*(?:E\\d+)?\\D(\\-?\\d+\\.?\\d*(?:E\\d+)?)$", "$1"));
        
        switch(op) {
            case '+':
                result = x + y;
                break;
            case '*':
                result = x * y;
                break;
            case '/':
                if(y!=0) result = x / y;
                break;
            case '%':
                if(y!=0) result = x % y;
                break;
            case '^':
                result = Math.pow(x, y);
                break;
            default:
                result = 1;
        }
        
        return Double.toString(result);
    }
}