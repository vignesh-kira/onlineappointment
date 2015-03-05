package com.example;

import   javax.jdo.annotations.IdGeneratorStrategy ;
import   javax.jdo.annotations.IdentityType ;
import   javax.jdo.annotations.PersistenceCapable ;
import   javax.jdo.annotations.Persistent ;
import   javax.jdo.annotations.PrimaryKey ;

@PersistenceCapable ( identityType =  IdentityType. APPLICATION )
public class Doctor {
	
		@PrimaryKey
        @Persistent ( valueStrategy =  IdGeneratorStrategy. IDENTITY ) 
        private   Long  id ;
        private   String  doctorName ;
        
        public   Long  getId ()   {
                return  id ;
        }
        public   void  setId ( Long  id )   {
                this . id   =  id ;
        }
        public   String  getDoctorName ()   {
                return  doctorName ;
        }
        public   void  setDoctorName ( String  doctorName )   {
                this . doctorName   =  doctorName ;
        }
        
        

}
