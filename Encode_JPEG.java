import java.awt.*;
import java.text.DecimalFormat;
import java.io.*;
import javax.swing.*;

public class Encode_JPEG 
{
       	 DecimalFormat df_name = new DecimalFormat("0.0");
       	 DecimalFormat df_name2 = new DecimalFormat("0");
       	 String input="";
       	 static double prev=0;
       	 String bitStream="";
                  
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
   

	
	
   
  /**********************************************
                      CONSTRACTOR
   ********************************************/	
	public Encode_JPEG() throws IOException,Exception
	{	                  
	        /*File ff;
            Button ab = new Button();
            JFileChooser chooser;
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            
            ff=new File(".");
            chooser = new JFileChooser(ff);
            ExampleFileFilter filter = new ExampleFileFilter();
            filter.addExtension("bmp");
            filter.setDescription("BMP Images");
            chooser.setFileFilter(filter);
                            
            int returnVal = chooser.showOpenDialog(ab);
           
            input=chooser.getSelectedFile().getName();*/
           
           DataInputStream file=new DataInputStream(new FileInputStream("lena.bmp"));
           DataOutputStream wfile2=new DataOutputStream(new FileOutputStream("Encode.txt"));
   	       int width,height;
   	 
   	      file.skip(18);
          int by4=file.read();
          int by3=file.read();
	      int by2=file.read();
	      int by1=file.read();
	      width=(((by1 & 0xff) << 24) | ((by2 & 0xff) << 16) | ((by3 & 0xff) << 8) | (by4 & 0xff));
	
		  int by8=file.read();
		  int by7=file.read();
		  int by6=file.read();
		  int by5=file.read();
		  height=(((by5 & 0xff) << 24) | ((by6 & 0xff) << 16) | ((by7 & 0xff) << 8) | (by8 & 0xff));
		 
		  wfile2.write(width);
		  wfile2.write(height);
		  wfile2.close();
		  file.close();
		  
		  DataInputStream file2=new DataInputStream(new FileInputStream("lena.bmp"));
		  int avail=file2.available();
		  System.out.println(file2.available());
   	      file2.skip(1024+54);
   	      	
   	  	  int wblock=width/8;
   	      int hblock=height/8;
   	      int Num_of_blocks=wblock*hblock;
          int[][] Data_2D=new int[height][width];
          
          System.out.println("Number of blocks:   "+Num_of_blocks);
          int num=0;
          
              //2D Matrix
          for(int i=0;i<Data_2D.length;i++)
           for(int j=0;j<Data_2D[0].length;j++)     
             Data_2D[i][j]=file2.read();   
          
          for(int i=0;i<Data_2D.length;i+=8) 
           for(int j=0;j<Data_2D[0].length;j+=8)
           {   
             num++;
        	 
        	 
           	double block[][]=new double[8][8];
            for(int k=0;k<8;k++)
              for(int l=0;l<8;l++)
              {
              	block[k][l]=Data_2D[k+i][l+j];             	 
              	 
                // Minus 128   
              	block[k][l]=block[k][l]-128; 
              }
               
          //#*/#*/#*/#*/#*/#*/#*/#*/#*/#*/#//             	 	  
	     
	     // Forward DCT 
         double DCTBlock[][]=forwardDCT(block);
         
    
         // Quantization  
		 double QuanBlock[][]=quantizatin(DCTBlock);
		 
         // Vectoring
         double vector[]=vectoring(QuanBlock);
         
         //if(num<5)
         //{
         	System.out.println("Block# "+num+"\n");
         	 System.out.println("\nvector   ");
         	for(int dd=0;dd<vector.length;dd++)
           System.out.print("   "+df_name2.format(vector[dd]));
         //}  	
           	
         // Encode DC
         String DC_Code=DC_Encode(vector[0]);
      
         // Run_Length Encoding    
         String codeword=runLengthEncode(vector);

         // The Encoded BitStream    
         bitStream+=DC_Code+codeword;
         
         if(num<5 )  
          System.out.println("\n"+bitStream);
         
         }       
         
            writeToFile(bitStream);
         
       
     }
   		
	
  /**********************************************
                      Forward DCT METHOD
   ********************************************/
	public double[][] forwardDCT(double[][] block)
	{						
		   double DCTblock[][]=new double[8][8];
	       double ci=1;
	       double cj=1;
	       double sum=0;
	    
	    
	       for(int i=0;i<DCTblock.length;i++)
            for(int j=0;j<DCTblock[0].length;j++)
            {
    		   if((i==0))
		  	     ci=1/(Math.sqrt(2));  
		  	   else 
		  	     ci=1;
		    
		       if(j==0)
		         cj=1/(Math.sqrt(2));
		       else 
		         cj=1;  
		         
               
   	          for(int x=0;x<block.length;x++)
   	           for(int y=0;y<block[0].length;y++)
   	            sum+=block[x][y]*Math.cos(((2*x+1)*i*Math.PI)/16)*Math.cos(((2*y+1)*j*Math.PI)/16);	
 
    
          DCTblock[i][j]=((ci*cj*sum)/4);
          sum=0;
         }
         
         return DCTblock;
         	
     }

     
     
     
  /**********************************************
                      QUANTIZATION METHOD
   ********************************************/
     public double[][] quantizatin(double[][] DCTBlock )
     {
     			
		double Quanblock[][]=new double[8][8];
				
		for(int i=0;i<DCTBlock.length;i++)
		 for(int j=0;j<DCTBlock[0].length;j++)		 	
		 	 Quanblock[i][j]=DCTBlock[i][j]/Quantable[i][j]; 	               	               
		
     return Quanblock;
    }
    


    
  /**********************************************
                      VECTORING METHOD
   ********************************************/    
    public double[] vectoring(double[][] QuanBlock)
    {   
        double[] vector=new double[64];
        vector[0]=QuanBlock[0][0];
        vector[1]=QuanBlock[0][1];
        
        int count=1,l=2,x1=1,y1=0;
        
        boolean flag=true;
        
        for(int i=2;i<36;i++)
        {      
          vector[i]=QuanBlock[x1][y1];	
          
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
        
        vector[63]=QuanBlock[7][7];
        vector[62]=QuanBlock[7][6];
        vector[61]=QuanBlock[6][7];
        
        for(int i=60;i>36;i--)
        {
        	
         vector[i]=QuanBlock[x1][y1];	
          
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
          
      return vector;    
    }



  /**********************************************
                       DC_ENCODE METHOD
   ********************************************/
     public String DC_Encode(double x)
     {
   	   String value="";
   	   String negative="";
   	   int sss=0;
   	   String code="";
   	   double diff=0;
 
       diff=x-prev;

         
   	   if(((int)Math.round(diff))==0)
   	   {
   	   	 value="";
   	   	 sss=0;
   	   }
   	   
   	   else if(((int)Math.round(diff))<0)
       {
    	   		value=Integer.toBinaryString(-1*(int)Math.round(diff));
    	   		for(int k=0;k<value.length();k++)
    	   		{
    	   		 if(value.charAt(k)=='0')
    	   		     negative+='1';
    	   		 else
    	   		    negative+='0';    
    	   		}
    	   		
    	   		value="";
    	   		for(int	j=0;j<negative.length();j++)
    	   		   value+=negative.charAt(j);
    	   		   
    	   		negative="";   
        }
    	   	
    	else 
    	   value=Integer.toBinaryString((int)Math.round(diff));
    	   	   
    	sss=value.length();
    	code=sss_codes[sss]+value;
    	
    	prev=x;
    	
    return	code;
   } 
 
 

    /**********************************************
                      RunLengthEncode METHOD
   ********************************************/   
    public String runLengthEncode(double[] vector1) 
    {
    	    		   
    	String value="";
    	String negative="";
    	int skip=0;
    	int sss=0;
    	String skip_sss="";
    	String codeword="";
    		
    	for(int i=1;i<64;i++)
    	{
    		
         if(((Math.round(vector1[i]))==0)&&skip<15)
    	  skip++;
    	  
    	 else 	
    	 {
    	   	
    	   	if(((int)Math.round(vector1[i]))<0)
    	   	{
    	   		value=Integer.toBinaryString(-1*(int)Math.round(vector1[i]));
    	   		for(int k=0;k<value.length();k++)
    	   		{
    	   		 if(value.charAt(k)=='0')
    	   		     negative+='1';
    	   		 else
    	   		    negative+='0';    
    	   		}
    	   		
    	   		value="";
    	   		for(int	j=0;j<negative.length();j++)
    	   		   value+=negative.charAt(j);
    	   		   
    	   		negative="";   
    	   	}
    	   	
    	   	else 
    	   	   value=Integer.toBinaryString((int)Math.round(vector1[i]));

    	   	sss=value.length();
     	   	//System.out.println("\n"+skip+"    "+sss+"   "+value);    	   	   
   	   	
    	   	skip_sss=skip_sss_codes[skip][sss];
    	   	
    	   	codeword+=skip_sss+value;
    	   	

    	   	value="";
    	   	sss=0;
    	   	skip=0;
    	 }
        }
        
        
        sss=0;
        skip=0;
        codeword+=skip_sss_codes[skip][sss];
                
        return codeword; 
    }
    
    
    /**********************************************
                    WriteToFile METHOD
    ********************************************/
  /*  public void writeToFile(String code) throws IOException
    {
    	 int by=0; 
         int l=7;
         int counter=0;
         int remain=0;
         String print2="";
         StringBuffer sbuffer=new StringBuffer();
         DataOutputStream wfile=new DataOutputStream(new FileOutputStream("Encode.txt",true));

     // PADDING // 
         remain=code.length()%8;  
           	                
         for(int i=0;i<code.length()-remain;i++)
           	     print2+=code.charAt(i);	     
           	     
         	     
         int cc=code.length()-remain;
         boolean flag2=true;
 		 while(cc+1<code.length()&&(code.charAt(cc)=='0')&&(flag2!=false))
 		 {
             counter++;
             if(code.charAt(cc+1)!='1')
               flag2=false;
             cc++;
         }           
          	     
           if(code.length()%8!=0)
           {
           	 
             for(int i=code.length()-remain;i<code.length();i++)
               sbuffer.append(code.charAt(i));           	                   
           	                
             for(int i=0;i<8-remain;i++)
               sbuffer.insert(0,'0');
             
            print2+=sbuffer;   
           }
           
         //System.out.println("\nAfter padding:\n-------------\n"+print2);
                 
         for(int i=0;i<print2.length();i++,l--)
         {
           	  
          int b;
          if(print2.charAt(i)=='0')
            b=0;
          else   
            b=1; 
          if(l>=0)  
             by+=b*Math.pow(2,l);
          if(l==0)
          {  
            wfile.write(by);
            l=8; 
            by=0;
          }
          if((remain!=0)&&(i==print2.length()-8))
          {
           	  wfile.write('$');
           	  wfile.write('#');
           	  wfile.write('*');
           	  wfile.write(counter);
           }	  
        }
    }*/
    
   public static void writeToFile(String st){
		
		String s="";
		int z=0;
		
		try{

				DataOutputStream out=new DataOutputStream(new FileOutputStream("Encode.txt",true));
				//out.write((st.length()/8)+2);
				//System.out.println("**   "+st);
				for (int i=0;i<st.length();i++){
					
					s+=st.charAt(i);
					if(s.length()==8){
						for(int j=0;j<8;j++){
							if(s.charAt(j)=='1')
								z+=(int)Math.pow(2,7-j);
						}
						out.write(z);
						//System.out.println(z);
						z=0;
						s="";
					}//if
				}//for
				//if(s.length()!=0){
					
					int x=s.length();
					out.write(x);
					
					while(s.length()<8){
						s="0"+s;
					}
					for(int j=0;j<8;j++){
						if(s.charAt(j)=='1')
							z+=(int)Math.pow(2,7-j);
					}
					out.write(z);
				//}
				
				out.close();
				
			}catch(IOException e)
			{}
	}
    /**********************************************
                       MAIN METHOD
    ********************************************/
	public static void main(String args[]) throws Exception
	{
		Encode_JPEG jpeg=new Encode_JPEG();
	}
	
	
