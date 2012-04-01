public class Transformer {
	
	/*Delete spaces from input expression*/
	static public String deleteSpaces(String str){
		str = str.replace(" ", "");
		return str;
	}
	
	/*Replace substring in specified bounds on other string*/
	static public String replace(String exp, int[] bounds, String result){
		StringBuffer sb = new StringBuffer(exp);
		sb.replace(bounds[0], bounds[1], result);
		return sb.toString();
	}
	
	/*Replace '+-' on '-' and '--' on '+', 
	  '-+' on '-' and '++' on '+'*/
	public static String resolveOperation(String str){
		int len = str.length();
		
		if( len == 1 && ( str.charAt(0) == '-' )){
			return "";
		}
		
		for(int i = 0; i < len; i++){
			char c = str.charAt(i);
			if( c == '+' && (i+1) < len && str.charAt(i+1)=='-'){
				str = str.replace("+-", "-");
			}
			else if ( c == '-' && (i+1) < len && str.charAt(i+1)=='+' ){
				str = str.replace("-+", "-");
			}
			else if( c == '+' &&  (i+1) < len && str.charAt(i+1)=='+' ){
				str = str.replace("++", "+");
			}
			else if( c == '-' && (i+1) < len && str.charAt(i+1)=='-'){
				str = str.replace("--", "+");
			}
			len = str.length();
		}
		//Delete '+' from the expression, if it first 
		if( len > 0 &&  str.charAt(0) == '+'  ){
			StringBuffer sb = new StringBuffer(str);
			sb.deleteCharAt(0);
			str = sb.toString();
		}
		return str;
	}
	
	/*Delete unneeded brackets from expression*/
	private static String removeUnneededBrackets(String str){
		char c;
		int lbracketpos = 0;
		int flag = 0;
		StringBuffer sb =  new StringBuffer(str);
		
		for (int i = sb.length() - 1; i >= 0; i--){
			
			c = sb.charAt(i); 
			if( '(' == sb.charAt(i) ){
				lbracketpos = i;

				for(int j = lbracketpos; j < sb.length(); j++){
					if( ((j+1) < sb.length()) && 
							( sb.charAt(j+1) == '-' ||
							( '0' <= sb.charAt(i+1) && 
							  '9' >= sb.charAt(i+1) ) ) ){
						int counter = j + 1;
						while( c != ')' ){
							if( counter >= sb.length() ){
								return null;
							}
							c = sb.charAt(counter);
							counter++;
							if( c == '-' || 
								c == '+' || 
								c == '*' || 
								c == '/' ){
								flag = 1;
								break;
							}
					
							if( c == ')' ){
								sb.deleteCharAt(lbracketpos);
								sb.deleteCharAt(counter - 2);
							}
						}
						if( flag == 1 ){
							flag = 0;
							break;
						}
					}
				}
			}
		}
		
		for(int i = 0; i < sb.length(); i++){
			c = sb.charAt(i);
			if( c == '(' && 
				( (i+2) < sb.length() ) && 
				sb.charAt(i+1) == '-' && 
				sb.charAt(i+2) == '-' ){
				sb.delete(i+1, i+3);
				lbracketpos = i;
				int counter = i + 1;
				while( c != ')' ){
					counter++;
					if( counter > sb.length() ){
						return null;
					}
					c = sb.charAt(counter);
					if( c == '-' || 
						c == '+' || 
						c == '*' || 
						c == '/' ){
						break;
					}
				
					if( c == ')'){
						sb.deleteCharAt(lbracketpos);
						sb.deleteCharAt(counter - 1);
					}
				}
			}
		}
		return sb.toString();
	}
	
	/*Prepare the input expression to parse*/
	static public String prepareExpression(String str){
		char c;
		StringBuffer sb = new StringBuffer(str);
		
		for ( int i = 0; i < sb.length(); i++ ){
			c = sb.charAt(i);
			if( c == ')' && 
				( i+1 < sb.length() ) && 
				sb.charAt(i+1) == '('){
				sb.insert(i+1, "*");
			}
			else if( (c >= '0' && c <= '9') &&
				(i+1 < sb.length()) &&
				sb.charAt(i+1) == '(' ){
				sb.insert(i+1, "*");
			}
		}
	
		str = Transformer.removeUnneededBrackets(sb.toString());
		if(str == null){
			return null;
		}
		return Transformer.resolveOperation(str);
	}
	
	/*Prepare calculated result before output*/
	static public String prepareResultForOutput(String str){
		
		str = str.replace("(", "");
		str = str.replace(")", "");
		str = str.replace("", "");
		str = str.replace("+", "");
		
		//Cut '.0' from expression before output
		if( str.charAt(str.length() - 1) == '0' &&
			str.charAt(str.length() - 2) == '.' ){
			StringBuffer sb = new StringBuffer(str);
			sb.delete(str.length() - 2, str.length());
			str = sb.toString();
		}
		
		return str.replace(str, "=" + str);
	}
}
