import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.text.DecimalFormat;

public class JPEG_Image_Encoding
{
  
  int number_of_blocks=0;
  String s="";
   	
  public JPEG_Image_Encoding()
  {
		
	//''''''''''''''''''''''''''''''''''''''''''''''''Tables'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''// 
    double Quantization_table[][]={
  	                             
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
                 
  	                                 
       
       
 //----------------------------------------Encoding steps---------------------------------------------------------------//

	
	
	
	try
    {
    
    String file="";
    File name;
    Button button=new Button();
    JFileChooser choose;
    name=new File(".");
	choose=new JFileChooser(name);
	ExampleFileFilter ex=new ExampleFileFilter();
	ex.addExtension("bmp");
    ex.setDescription("JPEG Image Encoding");
    choose.setFileFilter(ex);	
	int interval=choose.showOpenDialog(button);
	file=choose.getSelectedFile().getName();  
	DataInputStream input=new DataInputStream(new FileInputStream (file));
    DataOutputStream output_wh=new DataOutputStream(new FileOutputStream("JPEG_Encode.txt"));	  
    //--------------------------------------------------------------------------------------------------------//
      
    int a=0;
    int b=0;
    int c=0;
    int d=0;
    int width=0;
    int hieght=0;
      
    //get width & hieght
    input.skip(18);
    a=input.read();
    b=input.read();
	c=input.read();
    d=input.read();           
	  
	output_wh.write(d);
	output_wh.write(c);
    output_wh.write(b);
	output_wh.write(a);
	  
	width=(((d & 0xff) << 24) | ((c & 0xff) << 16) | ((b & 0xff) << 8) | (a & 0xff));
    
    a=input.read();
    b=input.read();
    c=input.read();
    d=input.read();
      
    output_wh.write(d);
	output_wh.write(c);
	output_wh.write(b);
	output_wh.write(a);
	        
    hieght=(((d & 0xff) << 24) | ((c & 0xff) << 16)|  ((b & 0xff) << 8) | (a & 0xff));    
    input.close();
      
    output_wh.close();
      
    ///////////////////////////////////get image data into matrix////////////////////////// 
    input=new DataInputStream(new FileInputStream (file));
    int data[][]=new int[hieght][width];
    int vector[]=new int[width*hieght];          
    int numOfBlocks=(width*hieght)/64;
    number_of_blocks=numOfBlocks;
    int counter=0;
      
    double matrix [][]             =new double [8][8];
    double subed_matrix [][]       =new double [8][8];
    double transformed_matrix [][] =new double [8][8]; 
    double Quantized_matrix[][]    =new double [8][8];
    int vectoring[]                =new int [64] ;
      
      
    input.skip(1078);  //(256*4)+54            
      
    //put data into 2D Matrix
    for(int i=0;i<data.length;i++)
     for(int j=0;j<data[0].length;j++)     
       data[i][j]=input.read();       
    
     
    int pre_DC=0;
    int curr_DC=0;
    int block=0; 
      
    //devide data inot 8x8 matrecies & apply encoding 
    for(int x=0;x<data.length;x=x+8)
    {
      for(int y=0;y<data[0].length;y=y+8) 	
      { 
      
       for(int i=0;i<matrix.length;i++)
        for(int j=0;j<matrix[0].length;j++)        
         matrix[i][j]=(double)data[i+x][j+y];      
      
        //if(block<3)
         //show(matrix);
              
       //1- subtract 128 from the values in the matrix
       subed_matrix =sub_128(matrix);
      
       //2- Apply Forward DCT     
       transformed_matrix =DCT(subed_matrix);       
      
       //3-Quantization stage
       Quantized_matrix =Quantization(transformed_matrix,Quantization_table);
       // show (Quantized_matrix);
      
       //4-Vactoring of Quantized_matrix
       vectoring=vectoring(Quantized_matrix);
       curr_DC=(int)vectoring[0];
       
       //5-Entropy Encoding	
       Entropy_Encoding(vectoring,sss,skip_sss,pre_DC,block);
      
       block++;
       pre_DC=curr_DC;    
    
     }
   
    }  
    
    //convert(s);  
    save(s);
        
  }//try 
  
  catch (Exception  ex)
  { }  
 		
}

/*.................................................Functions.............................................................*/ 
 
 
 /////////////////////////////////////subtract 128//////////////////////////////////////////////
 
 public double [][] sub_128(double a[][])
 {
 	
  for(int i=0;i<a.length;i++)	
   for(int j=0;j<a[0].length;j++)
   {   	
    a[i][j]=a[i][j]-128;   	
   }
   
   return a;
 	
 }
 
 /////////////////////////////////////Forward DCT//////////////////////////////////////////////
 public double [][] DCT (double a[][])
 {
 	
  
  double result [][] =new double[8][8]; 	
  double C_i=0;
  double C_j=0;
  
  double x1=0;
  double x2=0;
    
  double rows=0;
  
  //apply the transformation
  for(int i=0;i<result.length;i++)
   for(int j=0;j<result[0].length;j++)
   {
    
    if((i==0))     
    {
     C_i=1/(Math.sqrt(2));
     
    } 
    
    else
    {
      C_i=1;
    }
    
    if((j==0))     
    {
     
     C_j=1/(Math.sqrt(2));
    } 
    
    else
    {
      C_j=1;
    }
   	
   	for(int x=0;x<a.length;x++)
   	{ 
   	 for(int y=0;y<a[0].length;y++)
   	 {
   	  x1=Math.cos(( ((2*x)+1) * (i*Math.PI) )/16);
   	  x2=Math.cos(( ((2*y)+1) * (j*Math.PI) )/16);
   	  
   	  rows+=a[x][y]*Math.cos(((2*x+1)*i*Math.PI)/16)*Math.cos(((2*y+1)*j*Math.PI)/16);	
   	 	
   	 }	 
   	 
    }
    
    result[i][j]=(C_i*C_j*rows)/4;
    rows=0;
    C_i=0;
    C_j=0;
  }
  
  return result;
 	
 }
 
 
/////////////////////////////////////Quantization////////////////////////////////////////////
 public double [][] Quantization (double a[][],double q[][])
 {
 	
  double result [][]=new double [8][8];	
  
  for (int i=0;i<result.length;i++)	
   for(int j=0;j<result[0].length;j++)    
     result[i][j]=Math.round(a[i][j]/q[i][j]);    
     
  return result;	
 }
 
/////////////////////////////////////Vectoring////////////////////////////////////////////
  public int  [] vectoring (double a [][])
  {   
    int index=0;
    int vector []=new int[64];   
    vector[0]=(int)a[0][0];
    vector[63]=(int)a[7][7];
   
       
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
        vector[i]=(int)a[x][y];
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
         vector[i]=(int)a[x][y];
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
        vector[i]=(int)a[x][y];
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
       	vector[i]=(int)a[x][y];
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
    
       
  	return vector;
  }
   
  
/////////////////////////////////////Entropy Encoding////////////////////////////////////////////
   public void Entropy_Encoding (int vector[],String sss_table[],String skip_sss[][],int pre_DC,int num ) throws FileNotFoundException,IOException
   {
   	 
   	 //Differential encoding
   	 int difference=vector[0]-pre_DC;  	 
   	 String Encode_Stream=DC_Encoding(difference,sss_table); 	 
   	 String value="";
   	 String temp=""; 
   	 int skip=0;
   	 int sss=0;
   	 char arr[];
   	 
   	 //Apply Run length   	 
   	 for(int i=1;i<vector.length;i++)
   	 {
   	    if(vector[i]==0)
   	     skip++;
   	    
   	     /////////////////////////////////////////////////////////////////////////
   	     else if(vector[i]!=0) 
   	     {
   	  	   
   	       if(vector[i]<0)   	   
   	       {   	   
   	         
   	         vector[i]=vector[i]*-1;
   	         value=Integer.toBinaryString(vector[i]);   	    

   	         //convert to array for 1's complement
   	         arr=value.toCharArray();   	    
   	         value="";
   	   
   	         for(int j=0;j<arr.length;j++)
   	         { 
   	   	       if(arr[j]=='0')
   	   	        arr[j]='1';
   	   	   
   	   	       else    	   	   
   	            arr[j]='0';
   	         }
   	      	   
   	        //convert again to string
   	        for(int y=0;y<arr.length;y++)
   	        {   	   	
   	   	     value+=arr[y];   	   	
   	        }    	   
   	     	
   	     	vector[i]=vector[i]*-1; 	
   	       }//if
   	 
   	      //if value postive number
   	      else 
   	      {   	  	   	   
   	       value=Integer.toBinaryString(vector[i]);
   	        	    	
   	      }
   	      
   	      
   	      sss=value.length();
   	      if(skip>15)
   	      { 
   	        
   	        if(skip-15>15)
   	       	 temp+=skip_sss[15][0]+skip_sss[15][0]+skip_sss[skip-30][sss]+value;
   	       	else
   	       	  temp+=skip_sss[15][0]+skip_sss[skip-15][sss]+value;
   	       	
   	      }
   	      else 
   	       temp+=skip_sss[skip][sss]+value;
   	     
   	      skip=0;	
   	  	
   	    } //else if  
   	  
   }
    
   Encode_Stream+=temp+skip_sss[0][0]; 
   if(num<20)
   {
   	
   	
   	System.out.println(num+1);
   	System.out.println("Difference "+difference+" ");
   	for(int i=0;i<vector.length;i++)
   	 System.out.print(vector[i]+" ");
    
   	System.out.println(); 
    System.out.println(" Stream of Bits is:"+"\n");  	 
   	System.out.println(Encode_Stream+"\n");
   	System.out.println("-------------------------------------------------------------------------------");
   	
   	
   }
   	
   	s+=Encode_Stream;
   	 	
   
}


   
///////////////////////////////////////DC_Encoding//////////////////////////////////////////////
   public String DC_Encoding(int DC,String t [])
   {   	
   	 String DC_Binary="";
   	 String DC_stream="";
   	 char DC_arr[];
   	 int sss=0;  //sss as decimal
   	 
   	 //if DC negative number
   	 if(DC<0)
   	 {   	   
   	    DC=DC*-1;
   	    DC_Binary=Integer.toBinaryString(DC);   	    

   	    //convert to array for 1's complement
   	    DC_arr=DC_Binary.toCharArray();   	    
   	    DC_Binary="";
   	   
   	    for(int i=0;i<DC_arr.length;i++)
   	    {
   	   	 if(DC_arr[i]=='0')
   	   	   DC_arr[i]='1';
   	   	   
   	   	  else    	   	   
   	       DC_arr[i]='0';
   	    }
   	      	   
   	   //convert again to string
   	   for(int i=0;i<DC_arr.length;i++)
   	   {   	   	
   	   	DC_Binary+=DC_arr[i];   	   	
   	   }    	   
   	     	 	
   	  }
   	 
   	  //if DC postive number
   	  else if(DC>0)
   	  {   	  	   	   
   	    DC_Binary=Integer.toBinaryString(DC);   	    	
   	  }   
    
     //calculate sss of CD
     sss=DC_Binary.length();
     DC_stream=t[sss]+DC_Binary;    
          
   	 return DC_stream;
}

/////////////////////////////////////Convert to Bytes////////////////////////////////////////////
public static void save(String stream)
{
 String s="";
 int value=0;
		
 try{
  DataOutputStream output=new DataOutputStream(new FileOutputStream("JPEG_Encode.txt",true));
  for (int i=0;i<stream.length();i++)
  {
	s+=stream.charAt(i);
	if(s.length()==8)
	{
	  for(int j=0;j<8;j++)
	  {
	   if(s.charAt(j)=='1')
		value+=(int)Math.pow(2,7-j);
	  }
	  
	  output.write(value);						
	  value=0;
	  s="";
	}//if
  }//for
  
  
  int extra=s.length();
  output.write(extra);
  while(s.length()<8)
  {
    s="0"+s;
  }
  
  for(int j=0;j<8;j++)
  {
    if(s.charAt(j)=='1')
	value+=(int)Math.pow(2,7-j);
  }
  output.write(value);
 output.close();
				
}
catch(IOException e)
{}
}
  
/////////////////////////////////show///////////////////////////////////////////////////
public void show (double a[][])
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
 
 	
 /******************************main method*************************************************/
  public static void main(String args[])
  {
    	
  	JPEG_Image_Encoding JPEG=new JPEG_Image_Encoding();
  	
  } 
	
	
	
}