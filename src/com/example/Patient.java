package com.example;

import   javax.jdo.annotations.IdGeneratorStrategy ;
import   javax.jdo.annotations.IdentityType ;
import   javax.jdo.annotations.PersistenceCapable ;
import   javax.jdo.annotations.Persistent ;
import   javax.jdo.annotations.PrimaryKey ;

@PersistenceCapable ( identityType =  IdentityType. APPLICATION )
public class Patient {
	
		@PrimaryKey
        @Persistent ( valueStrategy =  IdGeneratorStrategy. IDENTITY ) 
        private   Long  id ;
        private   String  patientName ;
        private   String  email ;
        private   String gID;
        
        public   Long  getId ()   {
                return  id ;
        }
        public   void  setId ( Long  id )   {
                this . id   =  id ;
        }
        public   String  getPatientName ()   {
                return  patientName ;
        }
        public   void  setPatientName ( String  patientName )   {
                this . patientName   =  patientName ;
        }
        public   String  getEmail ()   {
            return  email ;
        }
        public   void  setEmail ( String  email )   {
            this . email   =  email ;
        }
        public   String  getGoogleId ()   {
            return  gID ;
        }
        public   void  setGoogleId ( String  gID )   {
            this . gID   =  gID ;
        }
        

}
