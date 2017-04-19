/*
 * Except.java
 *
 * Created on 23 juillet 2006, 11:54
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package fr.ogsteam.ogsconverter;

/**
 *
 * @author MC-20-EG-2003
 */

public class Except extends Throwable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** Creates a new instance of Except */
    public Except(Exception e) {
        super(e);
    }
    
    public String ExceptoString () {
        
        StringBuffer sb = new StringBuffer();
        
        StackTraceElement[] stack = getStackTrace();
     //stackTraceStringBuffer(sb, this.toString(), stack, 0);
 
     // The cause(s)
     Throwable cause = getCause();
     while (cause != null)
       {
   // Cause start first line
         sb.append("\nCaused by: \n");
 
         // Cause stacktrace
         StackTraceElement[] parentStack = stack;
         stack = cause.getStackTrace();
   if (parentStack == null || parentStack.length == 0)
     stackTraceStringBuffer(sb, cause.toString(), stack, 0);
   else
     {
       int equal = 0; // Count how many of the last stack frames are equal
       int frame = stack.length-1;
       int parentFrame = parentStack.length-1;
       while (frame > 0 && parentFrame > 0)
         {
     if (stack[frame].equals(parentStack[parentFrame]))
       {
         equal++;
         frame--;
         parentFrame--;
       }
     else
      break;
         }
       stackTraceStringBuffer(sb, cause.toString(), stack, equal);
     }
         cause = cause.getCause();
       }
 
     return sb.toString();
    }
    
    private void stackTraceStringBuffer(StringBuffer sb, String name,
           StackTraceElement[] stack, int equal)
   {
     String nl = "";
     // (finish) first line
     sb.append(name);
     sb.append(nl);
 
     // The stacktrace
     if (stack == null || stack.length == 0)
       {
   sb.append("   <<No stacktrace available>>");
   sb.append(nl);
       }
     else
       {
   for (int i = 0; i < stack.length-equal; i++)
     {
       sb.append("\n   at ");
       sb.append(stack[i] == null ? "<<Unknown>>" : stack[i].toString());
       sb.append(nl);
     }
   if (equal > 0)
     {
       sb.append("   ...");
       sb.append(equal);
       sb.append(" more");
       sb.append(nl);
     }
       }
   }
}
