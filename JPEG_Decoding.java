import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.text.DecimalFormat;

public class JPEG_Decoding

{
	
  public JPEG_Decoding()
  
{
	//''''''''''''''''''''''''''''''''''''''''''''''''Tables'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''// 
  int Quantization_table[][]={
  	                             
  	      {16,11,10,16,24,40,51,61},  	      
  	      {12,12,14,19,26,58,60,55},  	      
  	      {14,13,16,24,40,57,69,56},  	      
  	      {14,17,22,29,51,87,80,62},  	      
  	      {18,22,37,56,68,109,103,77},  	      
  	      {24,35,55,64,81,104,113,92},	      
          {49,61,78,87,103,121,120,101},
  	      {72,92,95,95,98,112,100,103,99}
       };
       
  /////////////////////////////////////////////////skip/sss tabel//////////////////////////////////////////////////
  String skip_sss[][]={
  	                   
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
  	                   
  /////////////////////////////////////////////sss table///////////////////////////////////////////////////////////////////////     
  
  String sss[]=        {"010","011","100","00","101","110","1110","11110","111110","1111110","11111110","111111110"};
                 
  	                  	
//----------------------------------------Decoding Steps---------------------------------------------------------------------//
try{  
  
  //1-Convert Bytes to 0's and 1's
  String stream=Bit_Stream();
  
  //2-Get Vector 
  int []vector=getVector(stream,sss,skip_sss);
  
  //3-Get Quantized Matrix
  int [][] Quantized_Matrix=get_Quantized_Matrix(vector);
  
  //4-Get Transformed Matrix
  int [][] Transformed_Matrix=get_Transformed_Matrix(Quantized_Matrix,Quantization_table);
  
  //5-Apply inverse Forward DCT
  int [][] inversed_Matrix=inverse_DCT(Transformed_Matrix); 
  
  //6-Add 128
  int [][] Origenal_Matrix=Add_128(inversed_Matrix);
}

catch(FileNotFoundException ex)
{
     System.out.println("File Not Found ");		
}
    
catch(IOException ex)
{
    		
}

    		
}



//////////////////////////////////////////Bit Stream Method///////////////////////////////////////////////////////////////////
public String Bit_Stream() throws FileNotFoundException,IOException
{
  
  DataInputStream input=new DataInputStream(new FileInputStream("bytes.txt"));
  StringBuffer temp=new StringBuffer(); 
  String bits="";
  String padding="";
  String zeros="";
  double value=0;
  double prev=0;
  
  while((value=input.read())!=-1)
  { //System.out.println(value);
    if(value=='*')
    {
      int length=input.read();      
      padding=Integer.toBinaryString(input.read());
      for(int i=0;i<length-padding.length();i++)
       zeros+="0";
      
      bits+=zeros+padding;  	
    }
    else
    {
      prev=value;
      for(int i=0;i<8;i++)
      {
        value=prev/2;     
        if(prev%2==0)
        {      
         temp.append("0");
         prev=value;      	
        } 
     
        else if(prev%2!=0)
        { 
         temp.append("1");
         prev=value-0.5;
        }   	
   	
       }
      
      bits+=temp.reverse();   
      temp.delete(0,temp.length());    	
    }
  
 } 
 input.close();
 System.out.println("Stream is : \n");	
 System.out.println(bits+"\n");
 return bits;	
	
}


/////////////////////////////////////////////get_vector Function//////////////////////////////////////////////////////////////////
public int [] getVector(String stream,String sss[],String skip_sss[][])
{
 int vector []=new int [64];
 
 //-------------------------------------------get DC value-----------------------------------------------------------//
 String temp="";
 String DC_Binary="";
 int DC=0;
 int sss_length=0;
 int sss_value=0;  //sss as dicemal 
 char DC_arr[];
 boolean negative=false;
 
 //get sss value and binary stream of dc
 for(int i=0;temp!="*";i++)
 {
  temp+=stream.charAt(i);
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
   
   /*convert again to string
   for(int i=0;i<DC_arr.length;i++)*/     	   	
  	          	   	
 }
 
 //convert to integer
 for(int i=0;i<DC_Binary.length();i++)
   if(DC_Binary.charAt(i)=='1')
      DC+=Math.pow(2,DC_Binary.length()-1-i);   
 
 if(negative)
   DC=-1*DC;
   
 vector[0]=DC;  
 
 //System.out.println(DC);
 //-------------------------------------------get AC values----------------------------------------------------------//
 temp=""; 
 int count=1;
 char AC_arr[]; 
 String temp_AC=""; 
 
 for(int i=sss_length+DC_Binary.length();i<stream.length();i++)
 {
  temp+=stream.charAt(i);
  for(int x=0;x<skip_sss.length;x++)
   for(int y=0;y<skip_sss[0].length;y++)	
   	if(temp.equals(skip_sss[x][y]))	
   	{   		
   		//x=skip
   		//y=sss
   		for(int j=0;j<x;j++)
   		{
   		 vector[count]=0;
   		 count++;   			
   		}
   		     		
   		for(int z=0;z<y;z++)
   		  temp_AC+=stream.charAt(i+z+1);
   		
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
 System.out.println("Vector is:"+"\n");	
 for(int i=0;i<vector.length;i++)
  System.out.print(vector[i]+" ");
  
 System.out.println("\n"+"---------------------------------------------------------"); 
 
  
 return vector;
	
}
/////////////////////////////////////////////get Quantized Matrix Function//////////////////////////////////////////////////////////////////
public int [][] get_Quantized_Matrix(int vector[])
{
    int a[][]=new int [8][8];
    a[0][0]=vector[0];
    a[7][7]=vector[63];
   
       
    int x=0;
    int y=1;
    int countx=2;
    int county=3;
    
    /////////upper half///////////////////
    for(int i=1;i<36;i++)
    { 
      if(x==0)
      {
       
       while(y!=-1)
       {
        a[x][y]=vector[i];
        x++;
        y--;
        i++;
       }	
       i--;
         
       x=countx;
       countx=countx+2;
       y=0;
       
      }
      
      else if(y==0)
      {
      	while(x!=-1)
        {
         a[x][y]=vector[i];
         x--;
         y++;
         i++;
        }	
        i--;
        
        y=county;
        county=county+2;
        x=0;		
      }
         	
    }
    ///////////lower half///////////////////
    x=7;
    y=1;
    countx=2;
    county=3;
    for(int i=36;i<vector.length-1;i++)
    {
     
     if(x==7)
     {
      
      while(y!=8)
      {
        a[x][y]=vector[i];
        x--;
        y++;
        i++;     	
      }
      
      i--;
      x=countx;
      countx=countx+2;	
      y=7;	
     	
     }
     
     else if(y==7)
     {
     	
      while(x!=8)
      {
       	a[x][y]=vector[i];
        x++;
        y--;
        i++;      	
      }
      
      i--;
      y=county;
      county=county+2;
      x=7;	
     	
     }
     	
    	
   }
   System.out.println();	     
   System.out.println("Quantized Matrix is: ");
   show(a);	
   return a;	
} 
/////////////////////////////////////////////get Transformed Matrix Function//////////////////////////////////////////////////////////////////
public int [][] get_Transformed_Matrix(int Quantized_Matrix[][],int Quantization_table[][] )
{
 int a[][]=new int [8][8];
 for(int i=0;i<a.length;i++)
  for(int j=0;j<a[0].length;j++)
   a[i][j]=Quantized_Matrix[i][j]*Quantization_table[i][j];
 
 System.out.println("Transformed Matrix is: ");
 show(a);		
 
 return a;	
  	
}
/////////////////////////////////////////////get Inversed Matrix Function//////////////////////////////////////////////////////////////////
public int [][]inverse_DCT(int Transformed_Matrix[][])
{
 
  int inversed [][] =new int[8][8]; 	
  double C_i=0;
  double C_j=0; 
  
  double rows=0;
  
  //apply the transformation
  for(int x=0;x<Transformed_Matrix.length;x++)
   for(int y=0;y<Transformed_Matrix[0].length;y++)
   {
   	    
   	for(int i=0;i<Transformed_Matrix.length;i++)
   	{ 
   	  for(int j=0;j<Transformed_Matrix[0].length;j++)
   	  {
   	    if((i==0))          
         C_i=1/Math.sqrt(2);    
       
        else       
         C_i=1;     
    
        if((j==0))            
         C_j=1/Math.sqrt(2);     
    
        else      
         C_j=1;
           	  
   	    rows+=(C_i*C_j)*Transformed_Matrix[i][j]*Math.cos(( ((2*x)+1) * (i*Math.PI) )/16)*Math.cos(( ((2*y)+1) * (j*Math.PI) )/16);	
   	 	
   	  }
   	     	 
   	  inversed[x][y]=(int)Math.round(rows/4);	 
   	 
    }  
    
    rows=0;
    C_i=0;
    C_j=0;
  }
  
  System.out.println("inversed Matrix is: ");
  show(inversed);		
  return inversed;
	
}
/////////////////////////////////////////////get Origenal Matrix Function///////////////////////////////////////////////////////////////
public int [][] Add_128(int inversed[][])
{
 int a [][]=new int[8][8];
 for(int i=0;i<inversed.length;i++) 
  for(int j=0;j<inversed[0].length;j++)	
 	a[i][j]=inversed[i][j]+128;
 
 System.out.println("Origenal Matrix is: ");
 show(a);
 return a;	
}

//////////////////////////////////show///////////////////////////////////////////////////
 public void show (int a[][])
 {
 	DecimalFormat d = new DecimalFormat("#,###,##0");
 	System.out.println();
 	for(int i=0;i<a.length;i++)	
    {
     for(int j=0;j<a[0].length;j++)
     {
   	  
   	   if((a[i][j]<0)&&(a[i][j]>=-9))
   	    System.out.print(d.format(a[i][j])+"   ");
   	    
   	   else if((a[i][j]<0)&&(a[i][j]>=-99)) 
   	    System.out.print(d.format(a[i][j])+"  ");
   	    
   	   else if((a[i][j]<0)&&(a[i][j]>=-999)) 
   	    System.out.print(d.format(a[i][j])+" "); 
   	    
   	   else if((a[i][j]>=0)&&(a[i][j]<=9))
   	    System.out.print(" "+d.format(a[i][j])+"   ");
   	    
   	   else if((a[i][j]>=0)&&(a[i][j]<=99))
   	    System.out.print(" "+d.format(a[i][j])+"  ");
   	   
   	   else if ((a[i][j]>=0)&&(a[i][j]<=999))
   	    System.out.print(" "+d.format(a[i][j])+" "); 
   	   	   
     }
     System.out.println(); 
    }
    
    System.out.println("--------------------------------------------------------");
 }
 
  	
	
/********************************************main method**********************************************************************/	

 public static void main(String srg[])
 
 {
		
  JPEG_Decoding	 d=new JPEG_Decoding();	 	
		
		
 }	
	
}











