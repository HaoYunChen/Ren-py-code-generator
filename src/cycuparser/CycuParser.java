
package cycuparser;
import java.util.*;
import java.io.*;
import java.lang.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Scanner;


/*
使用說明:
1. 到句號會自動斷行
2. $表示結束
3. 遇到對話後，到下個對話或$會結束
4. 對話前面會是'='
5. 非對話前面會是'*'



*/
public class CycuParser {
  
  private static Formatter x;
  private static Formatter y;
  
  static void OpenWriteFile() throws Throwable {
    
    try { 
      FileWriter filewriter = new FileWriter ( "ParseResult.txt" );
			y = new Formatter ( filewriter );
    
    } // try
    catch ( Exception e ) {
      System.out.println( "smthing is wrong." );
    } // catch()
  
  } // OpenWriteFile()

  

  
  static boolean OpenReadFile() throws Throwable {
    
    try {
      G.sFileReader = new Scanner( new File( "script.txt" ) );
      System.out.println( "檔案找到了:)" );
      return true;
    } // try
    catch( FileNotFoundException e ) {
      File file = new File( "script.txt" );
      file.createNewFile();
      System.out.println( "已在同個資料夾中幫你建立script.txt。請把文本貼入script.txt中並重新執行此程式。" );
      return false;
    } // catch
    
    
  } // CreateTxt
  
	
	static void ReadData() throws Throwable {
		
		int i = 0;
    String inputScript = "";
    String name = "";
    String inputString = "";
    boolean isQuote = false;
    
    if ( OpenReadFile() ) {
      OpenWriteFile(); // 開要寫的檔
    } // 開要讀的檔
    else {
      
      return;
    }
    
    int count = 0;
    
    
    G.sFileReader = new Scanner( new File( "script.txt" ) );
    

		
		while ( G.sFileReader.hasNext() ) { //一行行讀進來
			
      
      inputString = G.sFileReader.nextLine();
      System.out.println( "HI" );
      if ( !inputString.equals( "" ) )
        inputScript = inputScript+ '*' + inputString ;
      
      count++;
      

		} // while
    
    String tempString = "";
    

    for ( i = 0 ; i < inputScript.length() ; i++ ) { // 處理對話
       if ( inputScript.charAt(i) == '：' && ( inputScript.charAt(i-3) == '*' || inputScript.charAt(i-4) == '*' ) ) {
         
         if ( inputScript.charAt(i-1) == '六' ) {
           
           inputScript = inputScript.substring(0, i - 4) + "=" + inputScript.substring(i - 3, inputScript.length()); // 把*吃掉
           
         } // if ()
         else if ( inputScript.charAt(i-1) == '穆' ){
           inputScript = inputScript.substring(0, i - 3) + "=" + inputScript.substring(i - 2, inputScript.length());
           
         }
         
         i++;
       } // if ( : )
      
    
    } 
    
    System.out.println( inputScript );
            
            
            
    for ( i = 0 ; i < inputScript.length() ; i ++ ) { // 開始分析
      
      if ( inputScript.charAt(i) != '*' && inputScript.charAt(i) != '=' )  // 把文字前面的符號讀掉
        tempString = tempString + inputScript.charAt(i);
      
      if ( inputScript.charAt(i) == '「' )
            isQuote = true;
      else if ( inputScript.charAt(i) == '」' )
        isQuote = false;
      
      if ( inputScript.charAt(i) == '=' ){ // 如果是對話

        if ( inputScript.charAt(i+2) == ( '穆' ) ){ // 第幾個字是隨便取的

          tempString = "m";
          y.format( "show 阿穆1\n" ); // 圖片
        } // if ()
        else if ( inputScript.charAt(i+1) == ( '知' ) ) {
          tempString = "s";
          y.format( "show 知母六1\n" ); // 圖片
        } // else if ()
        
        while ( inputScript.charAt(i) != '：' ) { // 把名字讀掉
          i++;
        }
        
        name = tempString;
        tempString = "";
        i++;
        while ( inputScript.charAt(i) != '=' 
                && inputScript.charAt(i) != '$'  && inputScript.charAt(i) != '*' ) {
          
          if ( inputScript.charAt(i) == '「' )
            isQuote = true;
          else if ( inputScript.charAt(i) == '」' )
            isQuote = false;
          
          tempString = tempString + inputScript.charAt(i);
          if ( ( inputScript.charAt(i) == '。' || inputScript.charAt(i) == '？' || inputScript.charAt(i) == '！'
                  || inputScript.charAt(i) == '$' ) && !isQuote ) {
            
            y.format( "%s \"%s\"\n", name, tempString ); // 寫檔
            tempString = "";
          } // else if ( 。 )
          i++;
          
        } // 
        i--;

        if ( name.equals( "m" ) ){
          y.format( "hide 阿穆1\n" );
        } // if ()
        else if ( name.equals( "s" ) ) {
          y.format( "hide 知母六1\n" );
        } // else if ()
        tempString = "";

      } // if ()
      else if ( inputScript.charAt(i) == '&' ) { // 背景
       
        y.format( "#%s\n", tempString ); // 寫檔
        
        tempString = "";
      } // 地點
      
      else if ( ( inputScript.charAt(i) == '。' || inputScript.charAt(i) == '？' || inputScript.charAt(i) == '！' 
              || inputScript.charAt(i) == '$' ) && !isQuote ) {
        y.format( "\"%s\"\n", tempString ); // 寫檔
        
        tempString = "";
      } // else if ( 。 )
      
      
            
      
    } // for()
    
    
		
    y.close();
    CloseReadFile();
  } // ReadData()
  
  static void CloseReadFile() {
    G.sFileReader.close();
  } // CloseFile()


  public static void main(String[] args)  throws Throwable {
    
    G.Init();

    ReadData();
    
  }
  
}
