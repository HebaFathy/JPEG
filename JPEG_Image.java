import java.io.*;
import java.awt.*;
import javax.swing.*;

public class JPEG_Image extends JFrame
{
	
 public JPEG_Image() throws FileNotFoundException ,IOException 
 
 {
   super("Decode JPEG Image");
   Container c=getContentPane();	
   
   Decode_JPEG d=new Decode_JPEG(); 
   c=getContentPane();
   c.add(d);
   
   setSize(270,270);
   setVisible(true);
 	
 } 	

 public static void main(String arg[])
 
 {
  try{	
  JPEG_Image frame=new JPEG_Image();	
  frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	
  }
  
  catch(Exception ex)
  {}
 }	
	
	
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

class Decode_JPEG extends JPanel
{
	    DataInputStream rfile=new DataInputStream(new FileInputStream("Encode.txt")); 
	    int width=rfile.read();
	    int height=rfile.read();
	    
	    int num_of_blocks=width*height/64;
	    int vector[]=new int[width*height];
	    int prev=0;
        static String combine="";
	    int ddd=0;
	    int block_number=0;
	    
	    // Quantized Table
	    double Quantable[][]={
	     		              {16,11,10,16,24,40,51,61},
			                  {12,12,14,19,26,58,60,55},
			                  {14,13,16,24,40,57,69,56},
			                  {14,17,22,29,51,87,80,62},
			                  {18,22,37,56,68,109,103,77},
			                  {24,35,55,64,81,104,113,92},
			                  {49,64,78,87,103,121,120,101},
			                  {72,92,95,98,112,100,103,99} };
			                  
      // Skip/SSS Tabel 
	   String skip_sss_codes[][]={
  	                   
  	                   {"1010","00","01","100","1011","11010","111000","1111000","1111110110","1111111110000010","1111111110000011"},
  	                   
  	                   {"","1100","111001","1111001","111110110","1111111010","111111110000101","1111111110000101","1111111110000110","1111111110000111","1111111110001000"},
  	                   
  	                   {"","11011","11111000","1111110111","1111111110001001","1111111110001010","1111111110001011","1111111110001100","1111111110001101","1111111110001110","1111111110001111"},
  	                   
  	                   {"","111010","111110111","11111110111","1111111110010000","1111111110010001","1111111110010010","1111111110010011","1111111110010100","1111111110010101","1111111110010110"},
  	                   
  	                   {"","111011","1111111000","1111111110010111","1111111110011000","1111111110011001","111111111001101","1111111110011011","111111111001110","1111111110011101","1111111110011110"},
  	                   
  	                   {"","1111010","1111111001","1111111110011111","1111111110100000","1111111110100001","1111111110100010","11111111100011","1111111110100100","1111111110100101","1111111110100110"},
  	                   
  	                   {"","1111011","11111111000","1111111110100111","1111111110101000","1111111110101001","1111111110101010","1111111110101011","1111111110101100","1111111110101101","1111111110101110"},
  	                   
  	                   {"","11111001","11111111001","1111111110101111","1111111110110000","1111111110110001","1111111110110010","1111111110110011","1111111110110100","111111110110101","1111111110110110"},
  	                   
  	                   {"","11111010","111111111000000","1111111101110111","1111111110111000","1111111110111001","1111111110111010","1111111110111011","1111111110111100","1111111110111101","11111111011110"},
  	                   
  	                   {"","111111000","1111111110111111","1111111111000000","1111111111000001","1111111111000010","1111111111000011","1111111111000100","1111111111000101","1111111111000110","1111111111000111"},
  	                   
  	                   {"","111111001","1111111111001000","1111111111001001","1111111111001010","1111111111001011","1111111111001100","1111111111001101","1111111111001110","1111111111001111","1111111111010000"},
  	                   
  	                   {"","111111010","1111111111010001","1111111111010010","1111111111010011","1111111111010100","1111111111010101","1111111111010110","1111111111010111","1111111111011000","1111111111011001"},
  	                   
  	                   {"","111111010","1111111111011010","1111111111011011","1111111111011100","1111111111011101","111111111101110","1111111111011111","11111111111000000","1111111111100001","1111111111100010"},
  	                   
  	                   {"","11111111010","1111111111100011","1111111111100100","1111111111100101","1111111111100110","1111111111100111","1111111111101000","1111111111101001","1111111111101010","1111111111101011"},
  	                   
  	                   {"","111111110110","1111111111101100","1111111111101101","1111111111101110","1111111111101111","111111111110000","111111111110001","1111111111110010","1111111111110011","111111111110100"},
  	                   
  	                   {"111111110111","1111111111110101","1111111111110110","1111111111110111","1111111111111000","1111111111111001","1111111111111010","111111111111011","111111111111100","111111111111101","1111111111111110"}
  	
  	                   };
  	                   
  	      // SSS Table
  
          String sss_codes[]={"010","011","100","00","101","110","1110","11110","111110","1111110","11111110","111111110"};
   
   
   
	    
	    public Decode_JPEG() throws FileNotFoundException ,IOException

 
    	{
    		rfile.close();
        	String bitStream=decode(); 
        	getVector(bitStream,sss_codes,skip_sss_codes,Quantable);
        	/*int vector[]=Decode_Huffman(bitStream);
        	evaluate(vector);*/
    	}
    	
    	
    /*	public String decode() throws IOException
     	{
	       int x1,x2;
	       String s1="";
	       int bytes_count=0;
	       StringBuffer s2=new StringBuffer();
	  
		   while((x1=rfile.read())!=-1){
		   	
		   	bytes_count=bytes_count+3;
	  	   	//if(((char)x1=='$')&&(((char)rfile.read())=='#')&&(((char)rfile.read())=='*'))
	  	   	if((char)x1=='$')
	  	   	{
	  	   	 if(((char)rfile.read())=='#')
	  	   	  if(((char)rfile.read())=='*')
	  	   	  {
	  	   	    int counter=(int)rfile.read();
	  	   	    for(int i=0;i<counter;i++)
	  	   	     s1+='0';
	            s1+=Integer.toBinaryString((char)rfile.read());
	          }
	        }   
	        else{ 
	         // markSupported();
	          /*rfile.mark(bytes_count-2);
	          rfile.mark(bytes_count-2); 
	          
	          System.out.print("  "+(bytes_count-2)+"     "+rfile.markSupported());
	          System.out.print("  "+rfile.available());	  
	          rfile.reset(); 
	          System.out.print("  "+rfile.available());	   
	          
	  	   	  x2=x1;
	  	   	  for(int i=0;i<=7;i++)
	  	   	  {	
	  	   	    s2.append(x2%2);
	  	   	    x2=x2/2;
	  	   	  }   
	  		  s2.reverse();
	  		  s1+=s2;
	  		  s2=new StringBuffer();
	  	   }
	  	 }
	     rfile.close();
         //System.out.println("\n* The bit Stream:\n\n"+s1);
	  	 
	  	 return s1;
	    }*/
	    
	    public static String decode(){
		
		int code;
		String st="";
		String str="";
		String stream="";
		
		try{
			
			DataInputStream in=new DataInputStream(new FileInputStream("Encode.txt"));
			
			//width=in.readInt();
			//height=in.readInt();
			in.skip(2);
			
			//System.out.println(width+"\t"+height);
			
			while((code=in.read())!=-1){
				//System.out.println(code);
				st=Integer.toBinaryString(code);
				
				while(st.length()<8){
					
					st="0"+st;
				}
				str+=st;
			}
							
			stream+=str.substring(0,str.length()-16);
			
			int q=0;
			String st1="";
			
			st1=str.substring(str.length()-16,str.length()-8);
			
			for(int i=0;i<st1.length();i++){
				
				if(st1.charAt(i)=='1')
 					q+= (int)Math.pow(2,7-i);
			}
			String st2="";
 			st2=str.substring(str.length()-8,str.length());
			
			for(int i=(8-q);i<st2.length();i++){
				
				stream+=st2.charAt(i);
			}
			
		}
		catch(IOException e){
			
		}
		//System.out.println("\n\nThe Stream is: \n"+stream);
		return stream;
	}	


	     	
	 
	    
	 /**********************************************
                   Decode_Huffman METHOD
     ********************************************/
	  public int[] Decode_Huffman(String s2)
	  {
	  	int skip=0,sss=0;
	  	String output1="",output="",s3="";
	  	boolean flag=true;
	  	boolean sign;
	  	int num=0;
	  	int pos=0;
	  	
	  /*	if(block_number==0) 
	     System.out.println("* "+s2);*/

	  	for(int i=0;i<s2.length();i++)
        {   
            //DC Encode     
        	if(flag==true)
        	{
         	 s3+=s2.charAt(i);
          	 for(int j=0;j<sss_codes.length;j++)
               if(s3.equals(sss_codes[j])) 
               {
               	sss=j;
               	combine+=s3;
               	
               	if(s2.charAt(i+1)=='0')
               	{
               		sign=true;
               		for(int u=0;u<sss;u++)
              	    {
               		 i++;
               		 if(s2.charAt(i)=='0')
               		   output1+='1';
               		 else
               		   output1+='0';
               		   
               		 flag=false;  
               		}
               		 
               	}
               	
               	else
               	{
               	 sign=false;	
              	 for(int u=0;u<sss;u++)
              	 {
              	  i++;	
       	          output1+=s2.charAt(i); 
                  flag=false;
       	         }
       	        }
       	         
       	         vector[num]=convertToInt(output1,sign);
       	         vector[num]=vector[num]+prev;	    
       	          
       	         prev=vector[num];
       	         //System.out.println(num+"  DC: "+vector[num]+"   "+output1+"   prev= "+prev);
       	         combine+=output1;
               	 num++;
       	         s3="";
       	         j=sss_codes.length;
       	         output1="";
       	         flag=false;
       	          
               }
             }//DC  
             
            //AC Decode
            else if(flag==false)
            {	  
             s3+=s2.charAt(i);
         
            for(int j=0;j<skip_sss_codes.length;j++)
             for(int k=0;k<skip_sss_codes[0].length;k++)
             {
          	
       	      if(s3.equals(skip_sss_codes[j][k]))
       	      {
       	   	       
       	   	   skip=j;
       	   	   sss=k;
       	   	   combine+=skip_sss_codes[j][k];
       	   	   k=skip_sss_codes[0].length;
       	   	   j=skip_sss_codes.length;
       	   	   s3="";
  
       	   	   // System.out.print("      "+skip+"/"+sss);
   	   	
       	   	   // EOB 
       	   	   if((skip==0&&sss==0)&&(block_number!=num_of_blocks))
       	   	   {
       	   	   	  i=pos;
       	   	   	  i++;       	   	   	
       	   	      block_number++;	
       	   	      if(block_number<5)
       	   	      {	
       	   	       System.out.println("Block# "+block_number); 
       	   	       System.out.println("***  "+combine);     	   
       	   	      }
                  flag=true; 
       	   	   }
       	   	   else
       	   	   {      	   	
       	        
       	        for(int g=0;g<skip;g++)
       	        {
       	         vector[num]=0;   
       	         num++;
       	        }  
       	   
       	       if(i+1<s2.length())  
       	       {
         	    if((s2.charAt(i+1)=='0'))
           	    {
           	     sign=true;
           		 for(int u=0;u<sss;u++)
           	     {
            		i++;
               	    if(s2.charAt(i)=='0')
               		  output+='1';
               		else
               		  output+='0';
              	 }   
                }
               	
               	else
               	{
               	 sign=false;	
              	 for(int u=0;u<sss;u++)
              	 {
              	  i++;
              	  if(i<s2.length())
       	            output+=s2.charAt(i); 
       	       
       	         }
       	        }
       	        
       	        vector[num]=convertToInt(output,sign);
  
       	        combine+=output;	    
               	num++;       	        
       	   	  }
       	   	  output=""; 	       	   	
       	    } 
       	   }  
          }
          evaluate(vector); 	
 
       	  vector=new int[width];
       	  num=0;
        }
        pos=combine.length();
        combine="";
       } 
      return vector;
	}

	    
	/**********************************************
                  ConvertToInt METHOD
    ********************************************/
    public int convertToInt(String output,boolean sign)
    {
    	 int by=0; 
    	 for(int i=output.length()-1;i>=0;i--)
         {
           	  
          int b;
          if(output.charAt(i)=='0')
            b=0;
          else   
            b=1;  
            
          by+=b*Math.pow(2,output.length()-1-i);             
          }
          if(sign==true)
            by=by*-1;  
            
        return by;          
    }   
        
	    
	    
	    
	/**********************************************
                       MAIN METHOD
    ********************************************/
	   public static void main(String arg[]) 
	   {
		  try{
		     Decode_JPEG  app=new Decode_JPEG();
		     }
		     catch(IOException  ex)
		     {} 
       }
       
       
    /***********************************************
                 InverseVectoring METHOD
    ********************************************/
	public int[][] inverseVectoring(int[] vector)
	{
		int[][] matrix=new int[8][8];
        matrix[0][0]=vector[0];
        matrix[0][1]=vector[1];
        boolean flag=true;
           
        int count=1,l=2,x1=1,y1=0;
		for(int i=2;i<36;i++)
        {      
          matrix[x1][y1]=vector[i];	
          
          if((count<l)&&(flag==true))
          {
          	x1++;
          	y1--;
          	count++;
          }
          
          if((count<l)&&(flag==false))
          {
          	y1++;
          	x1--;
          	count++;
          }
                  
          if(y1<0)           	
           y1=0;
          if(x1<0)
           x1=0; 
           
          if(count==l)
          {
          	l=l+1;
          	count=0;
          	if(flag==true)
          	{
          	  flag=false;
          	  y1=0;
            }
            else
            {
            	flag=true;
            	x1=0;
            }
          } 
        }
        
        x1=5;
        y1=7;
        l=4;
        count=1;
        flag=true;
        
        matrix[7][7]=vector[63];
        matrix[7][6]=vector[62];
        matrix[6][7]=vector[61];
        
        for(int i=60;i>36;i--)
        {
        	
         matrix[x1][y1]=vector[i];	
          
          if((count<l)&&(flag==true))
          {
          	x1++;
          	y1--;
          	count++;
          }
          
          if((count<l)&&(flag==false))
          {
          	y1++;
          	x1--;
          	count++;
          }
                  
          if(y1>7)           	
           y1=7;
          if(x1>7)
           x1=7; 
           
          if(count==l)
          {
          	l=l+1;
          	count=1;
          	
          	if(flag==true)
          	  flag=false;
            else
            	flag=true;
          } 	
        }  
        
        
        
        return matrix;        		
	}
	 
	 
	/**********************************************
                  DeQuantization METHOD
    ********************************************/	 
	public int[][] deQuantization(int[][] QuanBlock)
	{
	   int[][] DCTblock=new int[8][8];
	   for(int i=0;i<QuanBlock.length;i++)
	    for(int j=0;j<QuanBlock[0].length;j++)	
	      DCTblock[i][j]=(int)(QuanBlock[i][j]*Quantable[i][j]);	
	  return DCTblock;    
	}	
	
	
	
	/**********************************************
                    InverseDCT METHOD
    ********************************************/  	  
    public int[][] inverseDCT(int[][] DCTBlock)
    {
    	   int block[][]=new int[8][8];
	       double ci=1;
	       double cj=1;
	       double sum=0;
	    
	    
	       for(int x=0;x<DCTBlock.length;x++)
            for(int y=0;y<DCTBlock[0].length;y++)
            {
 
   	         for(int i=0;i<DCTBlock.length;i++)
   	          for(int j=0;j<DCTBlock[0].length;j++)
   	          {
   	           if((i==0))
		  	     ci=1/(Math.sqrt(2));  
		  	   else 
		  	     ci=1;
		    
		       if(j==0)
		         cj=1/(Math.sqrt(2));
		       else 
		         cj=1;  
		         
   	           sum+=ci*cj*DCTBlock[i][j]*Math.cos(((2*x+1)*i*Math.PI)/16)*Math.cos(((2*y+1)*j*Math.PI)/16);	
   	          }
 
    
          block[x][y]=(int)(Math.round(sum/4));
          sum=0;
         }
         
         return block;
    }
    
    
    //
    
    public int [][] evaluate(int[] vector)
    {
       int QuanBlock[][]=inverseVectoring(vector);     
       int DCTBlock[][]=deQuantization(QuanBlock);     
       int block[][]=inverseDCT(DCTBlock);  
       
       
       for(int i=0;i<block.length;i++)
       {
      	//System.out.println();
        for(int j=0;j<block[0].length;j++)
        {
         int result=block[i][j]+128;	
         if(result>=0&&result<10)	
          System.out.print("     "+result);
         else
          System.out.print("   "+result); 
        } 
       } 
       ddd++;
       if(ddd<5)
       System.out.println(".........Done Block# "+ddd+"......... ");
       return block;

    }
    
    
    ////////////////
    public boolean markSupported()
    {
    	return true;
    }
    
    
 ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
public void getVector(String stream,String sss[],String skip_sss[][],double Quantization_table[][]) throws FileNotFoundException ,IOException
{
 
 DataOutputStream output=new DataOutputStream(new FileOutputStream("DecodeImage.txt"));
 
 int vector []=new int [64];
 int data[][]=new int[256][256]; 
 
 //-------------------------------------------get DC value-----------------------------------------------------------//
 String temp="";
 String DC_Binary="";
 String s="";
 
 
 int DC=0;
 int pre_DC=0;
 
 int sss_length=0;
 int sss_value=0;  //sss as dicemal 
 
 
 char DC_arr[]; 
 boolean negative=false;
 int block=0;/****/
 int x_d=0;
 int y_d=0;
 
 for(int position=0;position<stream.length();position++)
 {
  //get sss value and binary stream of dc
  for(int i=position;temp!="*";i++)
  {
  
   temp+=stream.charAt(i);
   s+=stream.charAt(i);
   
   
   for(int j=0;j<sss.length;j++)
    if(temp.equals(sss[j]))
  	  {
  	    sss_length=temp.length();
  	    sss_value=j;   	  
  	   
  	    //get binary stream of dc 
  	    for(int x=0;x<sss_value;x++)  	  
  	     DC_Binary+=stream.charAt(i+x+1);	      	  	     
  	  	
  	    temp="*";
  	  }  
  	 
   }
   
   
  s+=DC_Binary;
  
 
 //convret DC_Binary to integer
 //if negative i.e (start with 0)
  if(DC_Binary.startsWith("0"))
  {
    DC_arr=DC_Binary.toCharArray();   	    
    DC_Binary="";
    negative=true;
   	   
    for(int i=0;i<DC_arr.length;i++)
    {
   	  if(DC_arr[i]=='0')
   	    DC_arr[i]='1';
   	   	   
   	  else    	   	   
   	    DC_arr[i]='0';
   	   
   	  DC_Binary+=DC_arr[i];      
    }      	   	
  	          	   	
  }
 
  //convert to integer
  for(int i=0;i<DC_Binary.length();i++)
    if(DC_Binary.charAt(i)=='1')
       DC+=Math.pow(2,DC_Binary.length()-1-i);   
 
  if(negative)
    DC=-1*DC;
  
  int DC_length=sss_length+DC_Binary.length();
  
    
  vector[0]=DC+pre_DC; 
  pre_DC=vector[0];
  DC=0;
    
  sss_length=0;
  sss_value=0;
 
  DC_Binary="";
  
  negative=false;
 //-------------------------------------------get AC values----------------------------------------------------------//
 temp=""; 
 int count=1;
 char AC_arr[]; 
 String temp_AC="";
 
  
 for(int i=position+DC_length;i<stream.length();i++)
 {
  temp+=stream.charAt(i);
  s+=stream.charAt(i);
  
  
  for(int x=0;x<skip_sss.length;x++)
   for(int y=0;y<skip_sss[0].length;y++)	
   	if(temp.equals(skip_sss[x][y]))	
   	{   		
   		
   		if((x==0)&&(y==0))
   		 i=stream.length();
   		 
   		for(int j=0;j<x;j++)
   		{
   		 vector[count]=0;
   		 count++;   			
   		}
   		     		
   		for(int z=0;z<y;z++)   	      
   		  temp_AC+=stream.charAt(i+z+1);
   		  
   	
   		
   		s+=temp_AC;  		
   		
   		//put AC's values in vector
   		if(temp_AC.startsWith("0"))
   		{
   		  negative=true;
   		  AC_arr=temp_AC.toCharArray();
   		  temp_AC="";
   		  for(int j=0;j<AC_arr.length;j++)
   	      {
   	        if(AC_arr[j]=='0')
   	          AC_arr[j]='1';
   	   	   
   	        else    	   	   
   	          AC_arr[j]='0';
   	   
   	        temp_AC+=AC_arr[j] ;
          }		 	
   			
   		}
   		//convert to integer
   		for(int j=0;j<temp_AC.length();j++)
          if(temp_AC.charAt(j)=='1')
            vector[count]+=Math.pow(2,temp_AC.length()-1-j);
        
        if(negative)
         vector[count]=vector[count]*-1; 
         
        negative=false;    		
   	       		
   	    i=i+y;
   	    
   	    x=skip_sss.length;
   	    y=skip_sss[0].length;
   	    temp="";
   	    temp_AC="";
   	    count++;
   	    	
   	}
  
 }
 
 temp="";
 block++;
 int len=0;
 len=s.length();  
 position=position+len-1; 
 
 
 //if(block<4)
 //{   
    System.out.println("Vector is:"+"\n");	
    System.out.println("Vector is:"+"\n");	
 	for(int i=0;i<vector.length;i++)
      System.out.print(vector[i]+" ");
 
    System.out.println();
    System.out.println(s+"\n"+"block "+block);
    
    System.out.println("\n"+"---------------------------------------------------------");    
 
 //}   
 
  s=""; 
 
  int [][] Origenal_Matrix=evaluate(vector);
  
  //7-collect data in matrix
  for(int m=0;m<Origenal_Matrix.length;m++)
    for(int n=0;n<Origenal_Matrix[0].length;n++)
      data[x_d+m][y_d+n]=Origenal_Matrix[m][n];
  
  
   
  y_d=y_d+8;  
  if(y_d==data[0].length)
  {
  	y_d=0;
  	x_d=x_d+8;
  }
  
  
   for(int i=0;i<vector.length;i++)
   vector[i]=0;
   
    
 //}
 
 }//big for  
 
 
 for(int i=0;i<data.length;i++)
  for(int j=0;j<data[0].length;j++)
   output.write(data[i][j]);  
 }
 
 
 ///////////////////////////////////////////////Paint Function//////////////////////////////////////////////////////////// 	
 public void paint (Graphics g) 
 {
 	
  try{
  	
  DataInputStream input=new DataInputStream(new FileInputStream("Encode.txt"));
  	
  /*int width=input.read();      
  int hieght=input.read();*/
  int width=256;
  int hieght=256; 
   
  input.close();
  
  input=new DataInputStream(new FileInputStream("DecodeImage.txt"));
  
  int color=0;
  
  for(int h=hieght-1;h>=0;h--)
   for(int w=0;w<width;w++)
    {
      color=input.read();
      if(color<0)
       color=color*-1;
     
     g.setColor(new Color(color,color,color));
     g.drawLine(w,h,w,h);   
    }  
  
  }
  
  catch(Exception ex)
  {}	
 	
 }	


 
}
